/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.web.context;

import java.io.File;
import java.util.Properties;
import java.util.StringTokenizer;

import org.jboss.tools.common.meta.action.impl.SpecialWizardSupport;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.util.XModelObjectLoaderUtil;
import org.jboss.tools.common.util.FileUtil;
import org.jboss.tools.jst.web.project.WebModuleConstants;
import org.jboss.tools.jst.web.project.WebModuleImpl;

public abstract class AdoptWebProjectContext {
    protected SpecialWizardSupport support = null;
    protected String webinfLocation = null;
    protected String webxmlLocation = null;
    protected XModelObject[] modules = new XModelObject[0];
	protected String applicationName;
	protected String libPath;
	protected String classesPath;
	protected String buildPath;

    public AdoptWebProjectContext() {}

    public void setSupport(SpecialWizardSupport support) {
        this.support = support;
    }

    public void reset() {
        webinfLocation = null;
        webxmlLocation = null;
        modules = new XModelObject[0];
    }

	public String getApplicationName() {
		return support.getAttributeValue(1, "name");
	}
	
	public String getLibPath() {
		return support.getAttributeValue(3, "lib");
	}
	
	public String getClassesPath() {
		return support.getAttributeValue(3, "classes");
	}
	
	public String getBuildPath() {
		return support.getAttributeValue(3, "build");
	}

    public String getWebInfLocation() {
        return webinfLocation;
    }

    public String getWebXMLLocation() {
        return webxmlLocation;
    }

    public XModelObject[] getModules() {
        return modules;
    }
    
    public String getWebRootPath() {
    	String result = null;
		for (int i = 0; i < modules.length && result == null; i++)
			if ("<default>".equals(getModuleName(modules[i]))) 
				result = modules[i].getAttributeValue("root");
    	return result;
    }

    public void setWebInfLocation(String location) {
        location = location.replace('\\', '/');
        if(location.equals(webinfLocation)) return;
        webinfLocation = location;
        File f = new File(location);
        if(f.getName().equals("WEB-INF")) {
            support.setAttributeValue(1, "name", f.getParentFile().getName());
            File w = new File(f, "web.xml");
            if(w.isFile()) support.setAttributeValue(1, "web.xml location", w.getAbsolutePath().replace('\\', '/'));
            w = new File(f, "classes");
            if(w.isDirectory()) support.setAttributeValue(3, "classes", w.getAbsolutePath().replace('\\', '/'));
            w = new File(f, "lib");
            if(w.isDirectory()) support.setAttributeValue(3, "lib", w.getAbsolutePath().replace('\\', '/'));
        } else {
            support.setAttributeValue(1, "web.xml location", "");
        }
    }

    public void setWebXMLLocation(String location) throws Exception {
        location = location.replace('\\', '/');
        if(location.equals(webxmlLocation)) return;
        modules = new XModelObject[0];
        File f = new File(location);
        if(!f.isFile()) throw new Exception("File " + location + " does not exist.");
        String body = FileUtil.readFile(f);
        String entity = support.getTarget().getModel().getEntityRecognizer().getEntityName("xml", body);
        if(entity == null || !entity.startsWith("FileWebApp")) throw new Exception("File " + location + "is not recognized as web descriptor file.");
        XModelObject webxml = null;
        try {
            webxml = support.getTarget().getModel().createModelObject(entity, null);
            webxml.setAttributeValue("name", "web");
            XModelObjectLoaderUtil.setTempBody(webxml, body);
            XModelObjectLoaderUtil.getObjectLoader(webxml).load(webxml);
            webxml.getChildren();
        } catch (Exception e) {
            throw new Exception("Cannot load web descriptor file " + location + ".");
        }
        if("yes".equals(webxml.getAttributeValue("isIncorrect")))
          throw new Exception("Web descriptor file " + location + "is corrupted.");
        webxmlLocation = location;
        modules = createModulesInfo(webxml, new File(webinfLocation));
    }

	public abstract XModelObject[] createModulesInfo(XModelObject web, File webinf);

	protected String getWebModuleEntity() {
		return WebModuleConstants.ENTITY_WEB_MODULE;
	}

    public XModelObject createModuleInfo(XModel model, String name, String uri, File webinf) {
        return createModuleInfo(model, name, uri, webinf, false);
    }

    public XModelObject createModuleInfo(XModel model, String name, String uri, File webinf, boolean create) {
        String loc = webinf.getAbsolutePath().replace('\\', '/');
        String parent = webinf.getParentFile().getAbsolutePath().replace('\\', '/');
        if(parent.indexOf("..") >= 0) 
        	try { 
        		parent = new File(parent).getCanonicalPath().replace('\\', '/'); 
        	} catch (Exception t) {}
        Properties p = new Properties();
        p.setProperty("name", name);
        p.setProperty("URI", uri);
        String path = getPathOnDisk(parent, webinf.getName(), uri);
        if(path != null) p.setProperty("path on disk", path);
        if(name.length() == 0) {
            if("WEB-INF".equals(webinf.getName()) || path != null) p.setProperty("root", parent);
			String src = guessSrc(loc, false);
			if(src != null) p.setProperty("java src", src);
        } else {
            String modulepath = parent + name;
            File mf = new File(modulepath);
            if(mf.isDirectory() || (create && !mf.exists())) p.setProperty("root", modulepath);
            String modulesrc = modulepath + "/src";
            if(new File(modulesrc).isDirectory()) p.setProperty("java src", modulesrc);
            else {
				String src = guessSrc(loc, create);
            	if(src != null) p.setProperty("java src", src);
            }
        }
        XModelObject o = model.createModelObject(getWebModuleEntity(), p);
        if(uri.indexOf(',') >= 0 && o instanceof WebModuleImpl) {
			((WebModuleImpl)o).setURI(uri);
			StringTokenizer st = new StringTokenizer(uri, ",");
			if(st.hasMoreTokens()) {
				String t = st.nextToken().trim();
				path = getPathOnDisk(parent, webinf.getName(), t);
				if(path != null) o.setAttributeValue("path on disk", path);
				while(st.hasMoreTokens()) {
					t = st.nextToken().trim();
					String cp = t.replace('/', '#');
					XModelObject c = o.getChildByPath(cp);
					path = getPathOnDisk(parent, webinf.getName(), t);
					if(c != null && path != null) c.setAttributeValue("path on disk", path);
				}
			}
        }
        return o;
    }
    
    private String getPathOnDisk(String parent, String win, String uri) {
		String path = parent + uri;
		if(new File(path).isFile()) return path;
		if(uri.startsWith("/WEB-INF/")) {
			path = parent + "/" + win + uri.substring(8);
			if(new File(path).isFile()) return path;
		} else if(uri.startsWith("WEB-INF/")) {
			path = parent + "/" + win + uri.substring(7);
			if(new File(path).isFile()) return path;
		}
		return null;
    }
    
    protected String guessSrc(String loc, boolean create) {
    	String src = loc + "/src";
		File sf = new File(src);
		if(sf.isDirectory() || (create && !sf.exists())) return src;
		src = loc + "/../../JavaSource";
		sf = new File(src);
		if(sf.isDirectory()) {
			try {
				return sf.getCanonicalPath().replace('\\', '/');
			} catch (Exception e) {}
			return src;
		}
    	return null;
    }
    
    public void validateModules() throws Exception {
        for (int i = 0; i < modules.length; i++) {
            String n = getModuleName(modules[i]);
            String uri = modules[i].getAttributeValue("URI");
            String path = modules[i].getAttributeValue("path on disk");
            if(path.length() == 0) throw new Exception("Path on disk for URI " + uri + " is not set.");
            if(!new File(path).isFile()) throw new Exception("Path on disk " + path + "\nfor URI " + uri + " does not exist.");
            path = modules[i].getAttributeValue("root");
            if(path.length() == 0) throw new Exception("Root for " + n + " is not set.");
            if(!new File(path).isDirectory()) throw new Exception("Root " + path + "\nfor " + n + " does not exist.");
        }
    }

    private String getModuleName(XModelObject module) {
        String n = module.getAttributeValue("name");
        return (n.length() == 0) ? "<default>" : n;
    }

}

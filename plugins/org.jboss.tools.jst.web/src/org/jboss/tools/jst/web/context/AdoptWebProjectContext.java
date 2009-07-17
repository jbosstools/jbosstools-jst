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
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.StringTokenizer;

import org.jboss.tools.common.meta.action.impl.SpecialWizardSupport;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelException;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.util.XModelObjectLoaderUtil;
import org.jboss.tools.common.util.FileUtil;
import org.jboss.tools.jst.web.WebModelPlugin;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;
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
		return support.getAttributeValue(1, "name"); //$NON-NLS-1$
	}
	
	public String getLibPath() {
		return support.getAttributeValue(3, "lib"); //$NON-NLS-1$
	}
	
	public String getClassesPath() {
		return support.getAttributeValue(3, "classes"); //$NON-NLS-1$
	}
	
	public String getBuildPath() {
		return support.getAttributeValue(3, "build"); //$NON-NLS-1$
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
			if ("<default>".equals(getModuleName(modules[i])))  //$NON-NLS-1$
				result = modules[i].getAttributeValue("root"); //$NON-NLS-1$
    	return result;
    }

	public void setWebInfLocation(String location) {
        location = location.replace('\\', '/');
        if(location.equals(webinfLocation)) return;
        webinfLocation = location;
        File f = new File(location);
        if(f.getName().equals("WEB-INF")) { //$NON-NLS-1$
            support.setAttributeValue(1, "name", f.getParentFile().getName()); //$NON-NLS-1$
            File w = new File(f, "web.xml"); //$NON-NLS-1$
            if(w.isFile()) support.setAttributeValue(1, "web.xml location", w.getAbsolutePath().replace('\\', '/')); //$NON-NLS-1$
            w = new File(f, "classes"); //$NON-NLS-1$
            if(w.isDirectory()) support.setAttributeValue(3, "classes", w.getAbsolutePath().replace('\\', '/')); //$NON-NLS-1$
            w = new File(f, "lib"); //$NON-NLS-1$
            if(w.isDirectory()) support.setAttributeValue(3, "lib", w.getAbsolutePath().replace('\\', '/')); //$NON-NLS-1$
        } else {
            support.setAttributeValue(1, "web.xml location", ""); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    public void setWebXMLLocation(String location) throws XModelException {
        location = location.replace('\\', '/');
        if(location.equals(webxmlLocation)) return;
        modules = new XModelObject[0];
        File f = new File(location);
        if(!f.isFile()) throw new XModelException(MessageFormat.format(WebUIMessages.AdoptWebProjectContext_FileDoesNotExist, location));
        String body = FileUtil.readFile(f);
        String entity = support.getTarget().getModel().getEntityRecognizer().getEntityName("xml", body); //$NON-NLS-1$
        if(entity == null || !entity.startsWith("FileWebApp")) throw new XModelException(MessageFormat.format( //$NON-NLS-1$
				"File {0}is not recognized as web descriptor file.", location)); //$NON-NLS-1$
        XModelObject webxml = null;
        webxml = support.getTarget().getModel().createModelObject(entity, null);
        webxml.setAttributeValue("name", "web"); //$NON-NLS-1$ //$NON-NLS-2$
        XModelObjectLoaderUtil.setTempBody(webxml, body);
        XModelObjectLoaderUtil.getObjectLoader(webxml).load(webxml);
        webxml.getChildren();
        if("yes".equals(webxml.getAttributeValue("isIncorrect"))) //$NON-NLS-1$ //$NON-NLS-2$
          throw new XModelException(MessageFormat.format(WebUIMessages.AdoptWebProjectContext_WebDescriptorFileIsCorrupted, location));
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
        if(parent.indexOf("..") >= 0)  //$NON-NLS-1$
        	try { 
        		parent = new File(parent).getCanonicalPath().replace('\\', '/'); 
        	} catch (IOException t) {
        		WebModelPlugin.getPluginLog().logError(t);
        	}
        Properties p = new Properties();
        p.setProperty("name", name); //$NON-NLS-1$
        p.setProperty("URI", uri); //$NON-NLS-1$
        String path = getPathOnDisk(parent, webinf.getName(), uri);
        if(path != null) p.setProperty("path on disk", path); //$NON-NLS-1$
        if(name.length() == 0) {
            if("WEB-INF".equals(webinf.getName()) || path != null) p.setProperty("root", parent); //$NON-NLS-1$ //$NON-NLS-2$
			String src = guessSrc(loc, false);
			if(src != null) p.setProperty("java src", src); //$NON-NLS-1$
        } else {
            String modulepath = parent + name;
            File mf = new File(modulepath);
            if(mf.isDirectory() || (create && !mf.exists())) p.setProperty("root", modulepath); //$NON-NLS-1$
            String modulesrc = modulepath + "/src"; //$NON-NLS-1$
            if(new File(modulesrc).isDirectory()) p.setProperty("java src", modulesrc); //$NON-NLS-1$
            else {
				String src = guessSrc(loc, create);
            	if(src != null) p.setProperty("java src", src); //$NON-NLS-1$
            }
        }
        XModelObject o = model.createModelObject(getWebModuleEntity(), p);
        if(uri.indexOf(',') >= 0 && o instanceof WebModuleImpl) {
			((WebModuleImpl)o).setURI(uri);
			StringTokenizer st = new StringTokenizer(uri, ","); //$NON-NLS-1$
			if(st.hasMoreTokens()) {
				String t = st.nextToken().trim();
				path = getPathOnDisk(parent, webinf.getName(), t);
				if(path != null) o.setAttributeValue("path on disk", path); //$NON-NLS-1$
				while(st.hasMoreTokens()) {
					t = st.nextToken().trim();
					String cp = t.replace('/', '#');
					XModelObject c = o.getChildByPath(cp);
					path = getPathOnDisk(parent, webinf.getName(), t);
					if(c != null && path != null) c.setAttributeValue("path on disk", path); //$NON-NLS-1$
				}
			}
        }
        return o;
    }
    
	private String getPathOnDisk(String parent, String win, String uri) {
		String path = parent + uri;
		if(new File(path).isFile()) return path;
		if(uri.startsWith("/WEB-INF/")) { //$NON-NLS-1$
			path = parent + "/" + win + uri.substring(8); //$NON-NLS-1$
			if(new File(path).isFile()) return path;
		} else if(uri.startsWith("WEB-INF/")) { //$NON-NLS-1$
			path = parent + "/" + win + uri.substring(7); //$NON-NLS-1$
			if(new File(path).isFile()) return path;
		}
		return null;
    }
    
	protected String guessSrc(String loc, boolean create) {
    	String src = loc + "/src"; //$NON-NLS-1$
		File sf = new File(src);
		if(sf.isDirectory() || (create && !sf.exists())) return src;
		src = loc + "/../../JavaSource"; //$NON-NLS-1$
		sf = new File(src);
		if(sf.isDirectory()) {

				try {
					return sf.getCanonicalPath().replace('\\', '/');
				} catch (IOException e) {
					WebModelPlugin.getPluginLog().logError(e);
				}

			return src;
		}
    	return null;
    }
    
    public void validateModules() throws XModelException {
        for (int i = 0; i < modules.length; i++) {
            String n = getModuleName(modules[i]);
            String uri = modules[i].getAttributeValue("URI"); //$NON-NLS-1$
            String path = modules[i].getAttributeValue("path on disk"); //$NON-NLS-1$
            if(path.length() == 0) throw new XModelException(MessageFormat.format(WebUIMessages.AdoptWebProjectContext_PathIsNotSet,
					uri));
            if(!new File(path).isFile()) throw new XModelException(MessageFormat.format(
					WebUIMessages.AdoptWebProjectContext_PathDoesNotExist, path,
					uri));
            path = modules[i].getAttributeValue("root"); //$NON-NLS-1$
            if(path.length() == 0) throw new XModelException(MessageFormat.format(WebUIMessages.AdoptWebProjectContext_RootIsNotSet, n));
            if(!new File(path).isDirectory()) throw new XModelException(MessageFormat.format(WebUIMessages.AdoptWebProjectContext_RootDoesNotExist, path,
					n));
        }
    }

    private String getModuleName(XModelObject module) {
        String n = module.getAttributeValue("name"); //$NON-NLS-1$
        return (n.length() == 0) ? "<default>" : n; //$NON-NLS-1$
    }

}

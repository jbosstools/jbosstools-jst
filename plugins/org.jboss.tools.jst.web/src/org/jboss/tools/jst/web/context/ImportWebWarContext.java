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
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.osgi.util.NLS;

import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelException;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.plugin.ModelPlugin;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.common.util.FileUtil;
import org.jboss.tools.jst.web.WebModelPlugin;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;
import org.jboss.tools.jst.web.project.WebModuleImpl;

public abstract class ImportWebWarContext extends ImportWebProjectContext {
	protected String warLocation = null;
	protected String customerLocation;
	protected String warError;
	protected String nameError;
	protected ZipFile zip = null;
	protected Set<String> entries = new HashSet<String>();
	protected boolean isClassicEclipseProject = true;
	protected String[] originalSources;
	
	public void setCustomerLocation(String location) {
		customerLocation = location;
	}
	
	public String getProjectLocation() {
		IProject p = getProjectHandle();
		return (p == null || p.getLocation() == null) ? null : p.getLocation().toString();
	}
	
	public String getSuggestedProjectLocation() {
		return customerLocation;
	}
	
	public void setWarLocation(String location) {
		if(location.equals(warLocation)) return;
		entries.clear();
		warError = null;
		warLocation = location;
		if(warLocation.length() == 0) {
			warError = WebUIMessages.WAR_LOCATION_MUST_BE_SET;
			return;			
		}
		File f = new File(location);
		if(!f.isFile()) {
			warError = NLS.bind(WebUIMessages.FILE_DOESNOT_EXIST_P,location);
			return;
		}
		try {
			zip = new ZipFile(f);
			loadEntries();
		} catch (ZipException e) {
			WebModelPlugin.getPluginLog().logError(e);
			warError = NLS.bind(WebUIMessages.FILE_ISNOT_CORRECT,location); 
			return;
		} catch (IOException e) {
			WebModelPlugin.getPluginLog().logError(e);
			warError = NLS.bind(WebUIMessages.FILE_ISNOT_CORRECT,location); 
			return;
		}
		ZipEntry entry = null;
		try {
			entry = zip.getEntry("WEB-INF/web.xml");  //$NON-NLS-1$
		} catch (IllegalStateException e) {
			WebModelPlugin.getPluginLog().logError(e);
		}
		if(entry == null) {
			warError = NLS.bind(WebUIMessages.FILE_DOESNOT_CONTAIN_WEBXML,location); 
			return;
		}
		String body = null;
		try {
			InputStream s = zip.getInputStream(entry);
			body = FileUtil.readStream(s);
		} catch (ZipException e) {
			WebModelPlugin.getPluginLog().logError(e);
			warError = NLS.bind(WebUIMessages.CANNOT_READ_WEBXML, location); 
			return;
		} catch (IOException e) {
			WebModelPlugin.getPluginLog().logError(e);
			warError = NLS.bind(WebUIMessages.CANNOT_READ_WEBXML, location); 
			return;
		}
		try {
			loadWebXML(body, "WEB-INF/web.xml"); //$NON-NLS-1$
		} catch (XModelException e) {
			WebModelPlugin.getPluginLog().logError(e);
			warError = e.getMessage();
			return;
		}
		warError = null;
		String name = f.getName();
		if(name.lastIndexOf('.') > 0) name = name.substring(0, name.lastIndexOf('.'));
		if(name.length() > 0) name = name.substring(0, 1).toUpperCase() + name.substring(1);
		setProjectName(name);
		setServletVersion(getWebXMLVersion());
	}
	
	public void setProjectName(String value) {
		if(projectName != null && projectName.equals(value)) return;
		super.setProjectName(value);
		registry.setApplicationName(value);
		if(value == null || value.length() == 0) {
			nameError = WebUIMessages.NAME_MUST_BE_SET;
		} else {
			IStatus nameStatus = ModelPlugin.getWorkspace().validateName(projectName, IResource.PROJECT);
			if (!nameStatus.isOK()) {
				nameError = nameStatus.getMessage();
			} else if(getProjectHandle() != null && getProjectHandle().exists()) {
				nameError = NLS.bind(WebUIMessages.PROJECT_ALREADY_EXISTS_IN_THE_WORKSPACE, value); 
			} else if(EclipseResourceUtil.projectExistsIgnoreCase(projectName)) {
				nameError = NLS.bind(WebUIMessages.PROJECT_ALREADY_EXISTS_IN_THE_WORKSPACE, value); 
			} else {
				nameError = null;
			}
		}
		projectName = value;
	}
	
	
	private void loadEntries() {
		Enumeration en = zip.entries();
		while(en.hasMoreElements()) {
			ZipEntry entry = (ZipEntry)en.nextElement();
			String n = entry.getName();
			entries.add(n);
			while(true) {
				if(n.endsWith("/")) n = n.substring(0, n.length() - 1); //$NON-NLS-1$
				int i = n.lastIndexOf("/"); //$NON-NLS-1$
				if(i < 0) break;
				n = n.substring(0, i + 1);
				if(entries.contains(n)) break;
				entries.add(n);
			}
		}
	}
	
	public String getWarLocation() {
		return warLocation;
	}
	
	public String getErrorMessage() {
		if(warLocation == null) setWarLocation(""); //$NON-NLS-1$
		if(warError != null) return warError;
		if(nameError != null) return nameError;
		return registry.getErrorMessage();
	}
	
	public boolean canFinish() {
		return getErrorMessage() == null;
	}
	
	public void setClassicEclipseProject(boolean b) {
		isClassicEclipseProject = b;
	}
	
	public boolean isClassicEclipseProject() {
		return isClassicEclipseProject;
	}
	
	public void prepare() {
		String root = getSuggestedProjectLocation();
		setAddLibraries(false);
		setServletVersion("2.4"); //$NON-NLS-1$
		webInfLocation = (!isClassicEclipseProject) ? root + "/WEB-INF" : root + "/WebContent/WEB-INF"; //$NON-NLS-1$ //$NON-NLS-2$
		setClassesLocation(webInfLocation + "/classes"); //$NON-NLS-1$
		setLibLocation(webInfLocation + "/lib"); //$NON-NLS-1$
		webXmlLocation = webInfLocation + "/web.xml"; //$NON-NLS-1$
		findJavaSources();
		createModules();
	}
	
	public void prepareModules() {
		findJavaSources();
		createModules();
	}
	
	protected void createModules() {}
	
	protected void findJavaSources() {
		String[] ps = (String[])entries.toArray(new String[0]);
		Set<String> classes = new HashSet<String>();
		Set<String> js = new HashSet<String>();
		for (int i = 0; i < ps.length; i++) {
			String path = ps[i];
			if(!path.endsWith(".class") || !path.startsWith("WEB-INF/classes")) continue; //$NON-NLS-1$ //$NON-NLS-2$
			String classname = path.substring("WEB-INF/classes/".length(), path.length() - ".class".length()).replace('/', '.'); //$NON-NLS-1$ //$NON-NLS-2$
			classes.add(classname);			
		}
		for (int i = 0; i < ps.length; i++) {
			String path = ps[i];
			if(!path.endsWith(".java")) continue; //$NON-NLS-1$
			path = path.substring(0, path.length() - ".java".length()); //$NON-NLS-1$
			String prefix = ""; //$NON-NLS-1$
			while(true) {
				int k = path.indexOf('/');
				if(k < 0) break;
				prefix += "/" + path.substring(0, k); //$NON-NLS-1$
				path = path.substring(k + 1);
				String cn = path.replace('/', '.');
				if(classes.contains(cn)) {
					js.add(prefix);
					break;
				}
			}
		}
		originalSources = (String[])js.toArray(new String[0]);
		if(originalSources.length == 0) {
			existingSources = new String[0];
			return;
		}
		String root = getSuggestedProjectLocation();
		String rootOriginal = root;
		existingSources = new String[originalSources.length];
		if(isClassicEclipseProject) {
			rootOriginal += "/WebContent"; //$NON-NLS-1$
			if(existingSources.length == 1) {
				if(originalSources[0].equals("/WEB-INF/classes")) { //$NON-NLS-1$
					existingSources[0] = rootOriginal + originalSources[0];
				} else {
					existingSources[0] = root + "/JavaSource"; //$NON-NLS-1$
				}
			} else {
				for (int i = 0; i < existingSources.length; i++) {
					String s = originalSources[i];
					if(s.equals("/WEB-INF/classes")) { //$NON-NLS-1$
						existingSources[i] = root + "/JavaSource"; //$NON-NLS-1$
					} else {
						existingSources[i] = root + "/" + s.substring(s.lastIndexOf('/') + 1); //$NON-NLS-1$
					}
				}				
			}
		} else {
			for (int i = 0; i < existingSources.length; i++) {
				existingSources[i] = root + originalSources[i];
			}
		}
		for (int i = 0; i < originalSources.length; i++) {
			originalSources[i] = rootOriginal + originalSources[i];
		}
	}
	
	public String[] getOriginalSources() {
		return originalSources;
	}

	protected abstract String getWebModuleEntity();

	protected XModelObject createModuleInfo(XModel model, String name, String uri) {
        String loc = getWebInfLocation();
        String parent = loc.substring(0, loc.length() - "/WEB-INF".length()); //$NON-NLS-1$
        Properties p = new Properties();
        p.setProperty("name", name); //$NON-NLS-1$
        p.setProperty("URI", uri); //$NON-NLS-1$
        String path = getPathOnDisk(parent, "WEB-INF", uri); //$NON-NLS-1$
        if(path != null) p.setProperty("path on disk", path); //$NON-NLS-1$
        if(name.length() == 0) {
            if("WEB-INF".equals("WEB-INF") || path != null) p.setProperty("root", parent); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			String src = guessSrc();
			if(src != null) p.setProperty("java src", src); //$NON-NLS-1$
        } else {
            String modulepath = parent + name;
            p.setProperty("root", modulepath); //$NON-NLS-1$
			String src = guessSrc();
           	if(src != null) p.setProperty("java src", src); //$NON-NLS-1$
        }
        XModelObject o = model.createModelObject(getWebModuleEntity(), p);
        if(uri.indexOf(',') >= 0 && o instanceof WebModuleImpl) {
			((WebModuleImpl)o).setURI(uri);
			StringTokenizer st = new StringTokenizer(uri, ","); //$NON-NLS-1$
			if(st.hasMoreTokens()) {
				String t = st.nextToken().trim();
				path = getPathOnDisk(parent, "WEB-INF", t); //$NON-NLS-1$
				if(path != null) o.setAttributeValue("path on disk", path); //$NON-NLS-1$
				while(st.hasMoreTokens()) {
					t = st.nextToken().trim();
					String cp = t.replace('/', '#');
					XModelObject c = o.getChildByPath(cp);
					path = getPathOnDisk(parent, "WEB-INF", t); //$NON-NLS-1$
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

	protected String guessSrc() {
		if(existingSources != null && existingSources.length > 0) return existingSources[0];
		return null;		
	}

}

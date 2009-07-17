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
package org.jboss.tools.jst.web.project.version;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.eclipse.osgi.util.NLS;
import org.jboss.tools.common.model.util.XMLUtil;
import org.jboss.tools.common.model.util.XModelObjectLoaderUtil;
import org.jboss.tools.common.xml.XMLUtilities;
import org.jboss.tools.jst.web.WebModelPlugin;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;
import org.jboss.tools.jst.web.project.handlers.AddVersionSupport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class ProjectVersions {
	protected String descriptorFileName;
	protected long timeStamp;
	protected String path;
	protected File file;
	protected Map<String,ProjectVersion> versions = new TreeMap<String,ProjectVersion>();
	protected String errorMessage = null;
	Document document = null;
	
	public void setPath(String path) {
		if(this.path != null && this.path.equals(path) && file.lastModified() == timeStamp) return;
		this.path = path;
		file = new File(path + descriptorFileName);
		timeStamp = file.isFile() ? file.lastModified() : -1;
		update();
	}
	
	public String getPath() {
		try {
			return new File(path).getCanonicalPath().replace('\\', '/');
		} catch (IOException e) {
			WebModelPlugin.getPluginLog().logError(e);
			return path;
		}
	}
	
	public void setDescriptorFileName(String fileName) {
		descriptorFileName = fileName;
	}	
	
	public void update() {
		versions.clear();
		errorMessage = null;
		if(!file.isFile()) {
			errorMessage = NLS.bind(WebUIMessages.CANNOT_FIND_PROJECT_VERSIONS_DESCRIPTORFILE, file.getAbsolutePath());
			return;
		}
		Element e = XMLUtil.getElement(file);
		document = (e == null) ? null : e.getOwnerDocument();
		if(e == null) {
			try {
				FileReader reader = new FileReader(file);
				String[] errors = XMLUtil.getXMLErrors(reader, false);
				errorMessage = (errors == null || errors.length == 0) ? NLS.bind(WebUIMessages.CANNOT_PARSE_PROJECT_VERSIONS_DESCRIPTORFILE, file.getAbsolutePath())
				   : NLS.bind(WebUIMessages.CANNOT_PARSE_PROJECT_VERSIONS_DESCRIPTORFILE, file.getAbsolutePath()) + ":\n" + errors[0]; //$NON-NLS-1$
			} catch (IOException exc) {
				WebModelPlugin.getPluginLog().logError(exc);
				errorMessage = NLS.bind(WebUIMessages.CANNOT_READ_PROJECT_VERSIONS_DESCRIPTORFILE, file.getAbsolutePath());
			}
		} else {
			Element[] vs = XMLUtil.getChildren(e, "version"); //$NON-NLS-1$
			for (int i = 0; i < vs.length; i++) loadVersion(vs[i]);
		}
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public String[] getVersionList() {
		return (String[])versions.keySet().toArray(new String[0]);
	}
	
	public ProjectVersion getVersion(String name) {
		return name == null ? null : versions.get(name);
	}
	
	protected ProjectVersion loadVersion(Element e) {
		ProjectVersion version = new ProjectVersion(this);
		version.load(e);
		versions.put(version.name, version);
		return version;
	}
	
	String getAbsoluteLocation(String location) {
		File f = new File(location);
		if(!f.isAbsolute()) { 
			f = new File(path + "/" + location); //$NON-NLS-1$
		}
		try {
			return f.getCanonicalPath().replace('\\', '/');
		} catch (IOException e) {
			WebModelPlugin.getPluginLog().logError(e);
			return f.getAbsolutePath().replace('\\', '/');
		}
	}
	
	public void save() {
		if(document == null) return;
		try {
			XModelObjectLoaderUtil.serialize(document.getDocumentElement(), file.getAbsolutePath());
		} catch (IOException e) {
			WebModelPlugin.getPluginLog().logError(e);
		}
	}
	
	protected abstract String getWizardEntitySuffix();

	public String addVersion() {
		return AddVersionSupport.run(this, "ProjectVersion" + getWizardEntitySuffix()); //$NON-NLS-1$
	}
	
	public void addVersion(Properties p) {
		Element root = document.getDocumentElement();
		String location = p.getProperty("templates location"); //$NON-NLS-1$
		if(location.equals(getPath())) {
			location += "/" + p.getProperty("name"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		File f = new File(location);
		if(!f.isDirectory()) f.mkdirs();
		location = ProjectVersion.getRelativePath(path, location);
		Element pe = XMLUtilities.createElement(root, "version"); //$NON-NLS-1$
		pe.setAttribute("displayName", p.getProperty("name")); //$NON-NLS-1$ //$NON-NLS-2$
		Element tl = XMLUtilities.createElement(pe, "projectTempl"); //$NON-NLS-1$
		tl.setAttribute("location", location); //$NON-NLS-1$
		ProjectVersion v = loadVersion(pe);
		String core = p.getProperty("core library"); //$NON-NLS-1$
		if(core != null && core.length() > 0) v.addLibrary(core, "core"); //$NON-NLS-1$
		String common = p.getProperty("common library"); //$NON-NLS-1$
		if(common != null && common.length() > 0) v.addLibrary(common, "common"); //$NON-NLS-1$
		save();
		update();
	}
	
	
	public void removeVersion(String name) {
		ProjectVersion v = getVersion(name);
		if(v == null || v.element == null) return;
		if(!ProjectVersion.confirm(NLS.bind(WebUIMessages.YOU_WANT_TO_DELETE_IMPLEMENTATION,name))) return; 
		v.element.getParentNode().removeChild(v.element);
		save();
		update();
	}
	
}

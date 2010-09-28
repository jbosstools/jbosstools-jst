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
package org.jboss.tools.jst.web.project.helpers;

import java.io.*;
import java.util.*;

import org.eclipse.core.resources.IProject;
import org.eclipse.osgi.util.NLS;

import org.jboss.tools.common.model.ServiceDialog;
import org.jboss.tools.common.model.options.PreferenceModelUtilities;
import org.jboss.tools.common.util.FileUtil;
import org.jboss.tools.jst.web.WebModelPlugin;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;
import org.jboss.tools.jst.web.project.handlers.AddProjectTemplateSupport;
import org.jboss.tools.jst.web.project.handlers.EditProjectTemplateSupport;
import org.jboss.tools.jst.web.project.version.*;

public abstract class AbstractWebProjectTemplate implements IWebProjectTemplate {
	
	public AbstractWebProjectTemplate() {
	}

	protected abstract String getNatureDir();
	public abstract ProjectVersions getProjectVersions();

	public String getTemplatesBase() {
		return WebModelPlugin.getTemplateStateLocation() + "templates"; //$NON-NLS-1$
	}
	
	public String getProjectTemplatesLocation() {
		return getTemplatesBase() + "/"; //$NON-NLS-1$
	}

	public String getProjectTemplatesLocation(String version) {
		ProjectVersion v = getProjectVersions().getVersion(version);
		return (v == null) ? null : v.getProjectTemplatesLocation();
	}

	public String[] getTemplateList(String version) {
		String result[] = new String[0];
		String location = getProjectTemplatesLocation(version);
		if(location == null) return new String[0];
		File templateDir = new File(location);
		if (templateDir.isDirectory()) {
			String[] order = getProjectVersions().getVersion(version).getOrder();
			
			File subDirs[] = templateDir.listFiles(
				new FileFilter() {
					public boolean accept(File pathname) {
						return pathname.isDirectory();
					}
				}
			);
			if (subDirs != null) {
				Set<String> set = new TreeSet<String>();
				for (int i = 0; i < subDirs.length; i++) set.add(subDirs[i].getName());
				result = new String[subDirs.length];
				int k = 0;
				for (int i = 0; i < order.length; i++) {
					if(set.contains(order[i])) {
						set.remove(order[i]);
						result[k] = order[i];
						++k;
					}
				}
				String[] last = set.toArray(new String[0]);
				System.arraycopy(last, 0, result, k, last.length);
			}
		}
		return result;
	}
	
	public String getPageTemplatesLocation() {
		return getTemplatesBase() + "/pages"; //$NON-NLS-1$
	}

	public String[] getVersionList() {
		return getProjectVersions().getVersionList();
	}

	public String[] getLibraries(String version) {
		List<String> jars = new ArrayList<String>();
		ProjectVersion v = getProjectVersions().getVersion(version);
		if(v != null) {
			String[] locations = v.getLibraryLocations();
			for (int k = 0; k < locations.length; k++) {
				if(locations[k] != null && new File(locations[k]).isDirectory()) {
					File[] fs = new File(locations[k]).listFiles();
					if(fs != null) for (int i = 0; i < fs.length; i++) {
						if(!fs[i].isFile()) continue;
						String path = fs[i].getAbsolutePath();
						if(!jars.contains(path)) jars.add(path);
					}
				}
			}
		}
		return jars.toArray(new String[jars.size()]);
	}

	protected String getDefaultVersion(String preference) {
		String[] vs = getVersionList();
		if(vs.length == 0) return ""; //$NON-NLS-1$
		for (int i = 0; i < vs.length; i++) {
			if(vs[i].equals(preference)) return preference;
		}
		return vs[0];
	}

	protected String getDefaultTemplate(String version, String preference) {
		String[] vs = getTemplateList(version);
		if(vs == null || vs.length == 0) return ""; //$NON-NLS-1$
		for (int i = 0; i < vs.length; i++) {
			if(vs[i].equals(preference)) return preference;
		}
		return vs[0];
	}
	
	public void setDefaultTemplate(String template) {
		
	}

	protected abstract String getWizardEntitySuffix();

	public String addProjectTemplate(String version) {
		return AddProjectTemplateSupport.run(this, "ProjectTemplate" + getWizardEntitySuffix(), version); //$NON-NLS-1$
	}
	
	public String addProjectTemplate(IProject project) {
		return AddProjectTemplateSupport.run(this, "ProjectTemplate" + getWizardEntitySuffix(), project); //$NON-NLS-1$
	}
	
	public String editProjectTemplate(String version, String name) {
		return EditProjectTemplateSupport.run(this, "ProjectTemplate" + getWizardEntitySuffix(), version, name); //$NON-NLS-1$
	}
	
	public void removeProjectTemplate(String version, String name) {
		String location = getProjectTemplatesLocation(version);
		if(location == null) return;
		File f = new File(location);
		f = new File(f, name);
		if(f.isDirectory()) {
			if(!confirm(NLS.bind(WebUIMessages.YOU_WANT_TO_DELETE_TEMPLATE,name, version))) return;
			FileUtil.remove(f);			
		}
	}
	
	static boolean confirm(String message) {
		ServiceDialog d = PreferenceModelUtilities.getPreferenceModel().getService();
		return d.showDialog(WebUIMessages.CONFIRMATION, message, new String[]{WebUIMessages.OK, WebUIMessages.CANCEL}, null, ServiceDialog.QUESTION) == 0;
	}
	
	/**
	 * Returns new ProjectTemplate object for edit.
	 */	
	public ProjectTemplate getProjectTemplate(String version, String name) {
		String location = getProjectTemplatesLocation(version);
		if(location == null) return null;
		if(name == null || name.length() == 0) return null;
		ProjectTemplate template = new ProjectTemplate();
		template.setProjectVersion(getProjectVersions().getVersion(version));
		template.init(name, location);
		return template;
	}
	
}

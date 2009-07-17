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

import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.loaders.impl.PropertiesLoader;
import org.jboss.tools.common.model.options.PreferenceModelUtilities;
import org.jboss.tools.common.model.util.XModelObjectLoaderUtil;
import org.jboss.tools.common.util.FileUtil;
import org.jboss.tools.jst.web.project.version.ProjectVersion;

public class ProjectTemplate {
	public static String PREPROCESSING_PROPERTIES = ".preprocessing.properties"; //$NON-NLS-1$
	public static String PREPROCESSING = ".preprocessing"; //$NON-NLS-1$
	ProjectVersion projectVersion;
	
	File root;
	File location;
	String name;
	File preprocessingFile;
	File propertiesFile;
	ArrayList<String> preprocessingFiles;
	XModelObject properties;
	
	public ProjectTemplate() {}
	
	public void setProjectVersion(ProjectVersion version) {
		this.projectVersion = version;
	}
	
	public ProjectVersion getProjectVersion() {
		return projectVersion;
	}
	
	public void init(String name, String root) {
		this.root = new File(root);
		this.location = new File(root, name);
		this.name = name;
		preprocessingFile = new File(location, PREPROCESSING);
		propertiesFile = new File(location, PREPROCESSING_PROPERTIES);
		loadPreprocessing();
		loadProperties();
	}
	
	public File getLocation() {
		return location;
	}
	
	void loadPreprocessing() {
		String body = FileUtil.readFile(preprocessingFile);
		StringTokenizer st = new StringTokenizer(body, "\r\n"); //$NON-NLS-1$
		preprocessingFiles = new ArrayList<String>();
		while(st.hasMoreTokens()) {
			String t = st.nextToken().trim();
			if(t.length() > 0) preprocessingFiles.add(t);
		}
	}
	
	void loadProperties() {
		XModel model = PreferenceModelUtilities.getPreferenceModel();
		properties = model.createModelObject("FilePROPERTIES", null); //$NON-NLS-1$
		properties.setAttributeValue("name", ".preprocessing"); //$NON-NLS-1$ //$NON-NLS-2$
		properties.setAttributeValue("extension", "properties"); //$NON-NLS-1$ //$NON-NLS-2$
		PropertiesLoader loader = new PropertiesLoader();
		XModelObjectLoaderUtil.setTempBody(properties, FileUtil.readFile(propertiesFile));
		loader.load(properties);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public ArrayList<String> getPreprocessingFiles() {
		return preprocessingFiles;
	}
	
	public XModelObject getProperties() {
		return properties;
	}
	
	public boolean isNameModified() {
		return !name.equals(location.getName());
	}
	
	public void commit() {
		commitPreprocessingFile();
		commitPropertiesFile();
		commitName();
	}
	
	void commitPreprocessingFile() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < preprocessingFiles.size(); i++) {
			sb.append(preprocessingFiles.get(i)).append("\n"); //$NON-NLS-1$
		}
		String body = sb.toString();
		String oldBody = FileUtil.readFile(preprocessingFile);
		if(oldBody.equals(body)) return;
		FileUtil.writeFile(preprocessingFile, body);		
	}
	
	void commitPropertiesFile() {
		PropertiesLoader loader = new PropertiesLoader();
		String body = loader.getBody(properties);
		String oldBody = FileUtil.readFile(propertiesFile);
		if(oldBody.equals(body)) return;
		FileUtil.writeFile(propertiesFile, body);		
	}
	
	void commitName() {
		if(!isNameModified()) return;
		String oldName = location.getName();
		File newLocation = new File(root, name);
		boolean b = location.renameTo(newLocation);
		if(!b) {
			FileUtil.copyDir(location, newLocation, true);
			FileUtil.remove(location);
		}
		updateOrder(oldName);
		location = newLocation;
		preprocessingFile = new File(location, ".preprocessing"); //$NON-NLS-1$
		propertiesFile = new File(location, ".preprocessing.properties"); //$NON-NLS-1$
	}
	
	private void updateOrder(String oldName) {
		if(projectVersion == null) return;
		String[] order = projectVersion.getOrder();
		for (int i = 0; i < order.length; i++) {
			if(order[i].equals(oldName)) {
				order[i] = name;
				projectVersion.setOrder(order);
				return;
			}
		}
	}

}

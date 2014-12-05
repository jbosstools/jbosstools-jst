/******************************************************************************* 
 * Copyright (c) 2014 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.palette.internal;

import org.jboss.tools.jst.web.ui.WebUiPlugin;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;

public class PaletteSettings {
	private static final QualifiedName HTML5_RECOGNIZED_GROUPS_ONLY = new QualifiedName(WebUiPlugin.PLUGIN_ID, "HTML5_RECOGNIZED_GROUPS_ONLY");
	private static final QualifiedName HTML5_FILTER_TEXT = new QualifiedName(WebUiPlugin.PLUGIN_ID, "HTML5_FILTER_TEXT");
	private static PaletteSettings instance = new PaletteSettings();

	public static PaletteSettings getInstance() {
	    return instance;
	}

	private boolean recognizedCategoriesOnly;
	private String filterString;

	private PaletteSettings() {
	    recognizedCategoriesOnly = new Boolean(getProperty(HTML5_RECOGNIZED_GROUPS_ONLY));
	    filterString = getProperty(HTML5_FILTER_TEXT);
	    if(filterString == null) {
	        filterString = "";
	    }
	}

	public boolean isRecognizedGroupsOnly(){
	    return recognizedCategoriesOnly;
	}

	public void setRecognizedGroupsOnly(boolean value) {
	    recognizedCategoriesOnly = value;
	    setProperty(HTML5_RECOGNIZED_GROUPS_ONLY, ""+value);
	}

	public String getFilterString() {
	    return filterString;
	}

	public void setFilterString(String value) {
	    filterString = value;
	    setProperty(HTML5_FILTER_TEXT, value);
	}

	private synchronized String getProperty(QualifiedName propertyName){
		try {
			return ResourcesPlugin.getWorkspace().getRoot().getPersistentProperty(propertyName);
		} catch (CoreException e) {
			WebUiPlugin.getDefault().logError(e);
		}
		return null;
	}

	private synchronized void setProperty(QualifiedName propertyName, String value){
		try {
			ResourcesPlugin.getWorkspace().getRoot().setPersistentProperty(propertyName, value);
		} catch (CoreException e) {
			WebUiPlugin.getDefault().logError(e);
		}
	}
}

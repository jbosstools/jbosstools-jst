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
import org.jboss.tools.jst.web.ui.palette.PaletteUIMessages;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;

public class PaletteSettings {
	private static final QualifiedName HTML5_RECOGNIZED_GROUPS_ONLY = new QualifiedName(WebUiPlugin.PLUGIN_ID, "HTML5_RECOGNIZED_GROUPS_ONLY");
	private static final QualifiedName HTML5_FILTER_TEXT = new QualifiedName(WebUiPlugin.PLUGIN_ID, "HTML5_FILTER_TEXT");
	private static final QualifiedName HTML5_DYNAMIC_GROUP_TYPE = new QualifiedName(WebUiPlugin.PLUGIN_ID, "HTML5_DYNAMIC_GROUP_TYPE");
	private static final QualifiedName HTML5_DYNAMIC_GROUP_NUMBER = new QualifiedName(WebUiPlugin.PLUGIN_ID, "HTML5_DYNAMIC_GROUP_NUMBER");
	public static final String TYPE_MOST_POPULAR = PaletteUIMessages.TYPE_MOST_POPULAR;
	public static final String TYPE_LAST_USED = PaletteUIMessages.TYPE_LAST_USED;
	
	private static PaletteSettings instance = new PaletteSettings();

	public static PaletteSettings getInstance() {
	    return instance;
	}

	private boolean recognizedCategoriesOnly=false;
	private String filterString;
	private int dynamicGroupNumber;
	private String dynamicGroupType;

	private PaletteSettings() {
		String property = getProperty(HTML5_RECOGNIZED_GROUPS_ONLY);
		if(property != null){
			recognizedCategoriesOnly = new Boolean(property);
		}
	    filterString = getProperty(HTML5_FILTER_TEXT);
	    if(filterString == null) {
	        filterString = "";
	    }
	    property = getProperty(HTML5_DYNAMIC_GROUP_NUMBER);
	    if(property != null){
	    	dynamicGroupNumber = new Integer(property);
	    }else{
	    	dynamicGroupNumber = 20;
	    }
	    dynamicGroupType = getProperty(HTML5_DYNAMIC_GROUP_TYPE);
	    if(dynamicGroupType == null){
	    	dynamicGroupType = TYPE_MOST_POPULAR;
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
	
	public void setDynamicGroupNumber(int number){
		dynamicGroupNumber = number;
		setProperty(HTML5_DYNAMIC_GROUP_NUMBER, ""+number);
	}
	
	public int getDynamicGroupNumber(){
		return dynamicGroupNumber;
	}
	
	public void setDynamicGroupType(String type){
		if(!TYPE_LAST_USED.equals(type) && !TYPE_MOST_POPULAR.equals(type)){
			throw new IllegalArgumentException("type must be \""+TYPE_LAST_USED+"\" or \""+TYPE_MOST_POPULAR+"\", but was - "+type);
		}
		dynamicGroupType = type;
		setProperty(HTML5_DYNAMIC_GROUP_TYPE, type);
	}
	
	public String getDynamicGroupType(){
		return dynamicGroupType;
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

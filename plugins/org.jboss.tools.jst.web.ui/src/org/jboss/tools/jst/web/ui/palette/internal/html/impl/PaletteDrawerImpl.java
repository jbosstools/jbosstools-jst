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
package org.jboss.tools.jst.web.ui.palette.internal.html.impl;

import java.util.ArrayList;

import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteSeparator;
import org.jboss.tools.jst.web.kb.taglib.IHTMLLibraryVersion;
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteCategory;
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteGroup;
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteItem;
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteVersionGroup;

/**
 * 
 * @author Daniel Azarov
 *
 */
public class PaletteDrawerImpl extends PaletteDrawer{
	private IPaletteGroup group;
	
	public PaletteDrawerImpl(IPaletteGroup group) {
		super(group.getName(), group.getImageDescriptor());
		this.group = group;
		loadVersion(group.getSelectedVersionGroup().getVersion());
	}
	
	public void loadVersion(String name){
		IHTMLLibraryVersion version = getVersion(name);
		loadVersion(version);
	}
	
	public void setPreferredVersion(String name){
		IHTMLLibraryVersion version = getVersion(name);
		group.getPaletteModel().getPaletteContents().setPreferredVersion(getLabel(), version);
	}
	
	@SuppressWarnings("rawtypes")
	public void loadVersion(IHTMLLibraryVersion version){
		group.setSelectedVersion(version);
		setChildren(new ArrayList());
		
		IPaletteVersionGroup versionGroup = group.getSelectedVersionGroup();
		for(IPaletteCategory category : versionGroup.getCategories()){
			for(IPaletteItem item : category.getItems()){
				PaletteTool tool = new PaletteTool(item);
				add(tool);
			}
			add(new PaletteSeparator());
		}
	}
	
	public String[] getVersions(){
		String[] strings = new String[group.getVersions().length];
		for(int i = 0; i < group.getVersions().length; i++){
			strings[i] = group.getVersions()[i].toString();
		}
		return strings;
	}
	
	private IHTMLLibraryVersion getVersion(String versionString){
		for(IHTMLLibraryVersion version : group.getVersions()){
			if(version.toString().equals(versionString)){
				return version;
			}
		}
		return null;
	}
	
	public IHTMLLibraryVersion getVersion(){
		return group.getSelectedVersionGroup().getVersion();
	}
	
	public IPaletteGroup getPaletteGroup(){
		return group;
	}
	
	public void refresh(){
		listeners.firePropertyChange(PROPERTY_DEFAULT, "old", "new");
	}
}

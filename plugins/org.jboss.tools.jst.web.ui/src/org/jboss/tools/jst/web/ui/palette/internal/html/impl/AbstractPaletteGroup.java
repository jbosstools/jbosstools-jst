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
import java.util.List;

import org.jboss.tools.jst.web.kb.internal.taglib.html.IHTMLLibraryVersion;
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteGroup;
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteVersionGroup;
import org.jboss.tools.jst.web.ui.palette.model.IPaletteModel;
/**
 * Default implementation of palette group
 * 
 * @see IPaletteGroup
 * 
 * @author Daniel Azarov
 *
 */
public abstract class AbstractPaletteGroup implements IPaletteGroup {
	private ArrayList<IPaletteVersionGroup> versionGroups = new ArrayList<IPaletteVersionGroup>();
	private IPaletteModel model;
	private IPaletteVersionGroup selectedVersion;
	
	@Override
	public void setPaletteModel(IPaletteModel model){
		this.model = model;
	}
	
	protected void add(IPaletteVersionGroup versionGroup){
		versionGroups.add(versionGroup);
		versionGroup.setGroup(this);
	}
	
	public IHTMLLibraryVersion[] getVersions(){
		IHTMLLibraryVersion[] versions = new IHTMLLibraryVersion[getPaletteVersionGroups().size()];
		for(int index = 0; index < versions.length; index++){
			versions[index] = getPaletteVersionGroups().get(index).getVersion();
		}
				
		return versions;
	}
	
	@Override
	public List<IPaletteVersionGroup> getPaletteVersionGroups() {
		return versionGroups;
	}
	
	@Override
	public IPaletteModel getPaletteModel(){
		return model;
	}
	
	@Override
	public IPaletteVersionGroup getSelectedVersionGroup(){
		return selectedVersion;
	}
	
	@Override
	public void setSelectedVersion(IHTMLLibraryVersion version){
		for(IPaletteVersionGroup vGroup : getPaletteVersionGroups()){
			if(vGroup.getVersion().equals(version)){
				selectedVersion = vGroup;
				return;
			}
		}
		throw new IllegalArgumentException();
	}
	
	@Override
	public IPaletteVersionGroup getLastVersionGroup(){
		return versionGroups.get(versionGroups.size()-1);
	}
}

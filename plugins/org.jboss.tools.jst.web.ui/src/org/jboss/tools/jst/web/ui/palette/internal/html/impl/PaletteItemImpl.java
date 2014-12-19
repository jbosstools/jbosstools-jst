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
import java.util.StringTokenizer;

import org.eclipse.jface.resource.ImageDescriptor;
import org.jboss.tools.common.model.ui.views.palette.IPositionCorrector;
import org.jboss.tools.jst.web.ui.WebUiPlugin;
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteCategory;
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteItem;
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteItemWizard;

/**
 * Implementation of palette item
 * 
 * @see IPaletteItem
 * 
 * @author Daniel Azarov
 *
 */
public class PaletteItemImpl implements IPaletteItem{
	private String name;
	private String toolTip;
	private String keywords;
	private boolean reformat=true;
	private ImageDescriptor imageDescriptor;
	private Class<? extends IPaletteItemWizard> wizardClass;
	private Class<? extends IPositionCorrector> positionCorrectorClass;
	private String startText="";
	private String endText="";
	private IPaletteCategory category;
	private ArrayList<String> keywordList;
	private long numberOfCalls, count;
	private static long globalCount;

	public PaletteItemImpl(String name, String toolTip, String keywords,
			ImageDescriptor imageDescriptor, Class<? extends IPaletteItemWizard> wizardClass,
			Class<? extends IPositionCorrector> positionCorrectorClass) {
		this.name = name;
		this.toolTip = toolTip;
		this.keywords = keywords;
		this.imageDescriptor = imageDescriptor;
		this.wizardClass = wizardClass;
		this.positionCorrectorClass = positionCorrectorClass;
	}
	
	public PaletteItemImpl(String name, String toolTip, String keywords,
			ImageDescriptor imageDescriptor, Class<? extends IPaletteItemWizard> wizardClass,
			Class<? extends IPositionCorrector> positionCorrectorClass, String startText, String endText) {
		this(name, toolTip, keywords, imageDescriptor, wizardClass, positionCorrectorClass);
		
		this.startText = startText;
		this.endText = endText;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getTooltip() {
		return toolTip;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return imageDescriptor;
	}
	
	@Override
	public boolean hasWizard(){
		return wizardClass != null;
	}

	@Override
	public IPaletteItemWizard createWizard() {
		IPaletteItemWizard wizard = null;
		if(wizardClass != null){
			try {
				wizard = wizardClass.newInstance();
				wizard.setPaletteItem(this);
			} catch (InstantiationException e1) {
				WebUiPlugin.getDefault().logError(e1);
			} catch (IllegalAccessException e1) {
				WebUiPlugin.getDefault().logError(e1);
			}
		}
		return wizard;
	}

	@Override
	public IPositionCorrector createPositionCorrector() {
		IPositionCorrector positionCorrector = null;
		if(positionCorrectorClass != null){
			try {
				positionCorrector = positionCorrectorClass.newInstance();
			} catch (InstantiationException e1) {
				WebUiPlugin.getDefault().logError(e1);
			} catch (IllegalAccessException e1) {
				WebUiPlugin.getDefault().logError(e1);
			}
		}
		return positionCorrector;
	}

	@Override
	public String getStartText() {
		return startText;
	}

	@Override
	public String getEndText() {
		return endText;
	}

	@Override
	public List<String> getKeywords() {
		if(keywordList == null){
			keywordList = new ArrayList<String>();
			StringTokenizer tokenizer = new StringTokenizer(keywords, " ", false);
			while(tokenizer.hasMoreTokens()){
				keywordList.add(tokenizer.nextToken());
			}
		}
		return keywordList;
	}

	@Override
	public String getKeywordsAsString() {
		if(keywords == null || keywords.isEmpty()){
			return name;
		}
		return keywords;
	}

	@Override
	public boolean isReformat() {
		return reformat;
	}

	public void setReformat(boolean reformat) {
		this.reformat = reformat;
	}

	@Override
	public IPaletteCategory getCategory() {
		return category;
	}

	@Override
	public void setCategory(IPaletteCategory category) {
		this.category = category;
	}

	@Override
	public String getId() {
		return "/"+category.getVersionGroup().getGroup().getName()+
				"/"+category.getVersionGroup().getVersion().toString()+
				"/"+getName();
	}

	@Override
	public long getCountIndex() {
		return count;
	}
	
	@Override
	public void setCountIndex(long count) {
		this.count = count;
		if(count > globalCount){
			globalCount = count;
		}
	}
	
	public static void setStaticCountIndex(long count) {
			globalCount = count;
	}

	@Override
	public long getNumberOfCalls() {
		return numberOfCalls;
	}

	@Override
	public void setNumberOfCalls(long numberOfCalls) {
		this.numberOfCalls = numberOfCalls;
	}
	
	@Override
	public void called() {
		numberOfCalls++;
		globalCount++;
		count = globalCount;
		if(numberOfCalls == Long.MAX_VALUE || globalCount == Long.MAX_VALUE){
			((PaletteModelImpl)getCategory().getVersionGroup().getGroup().getPaletteModel()).savePaletteItems();
		}else{
			((PaletteModelImpl)getCategory().getVersionGroup().getGroup().getPaletteModel()).savePaletteItem(this);
		}
	}
	
}

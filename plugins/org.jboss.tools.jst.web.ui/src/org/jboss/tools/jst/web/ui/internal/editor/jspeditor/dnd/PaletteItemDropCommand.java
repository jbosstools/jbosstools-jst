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
package org.jboss.tools.jst.web.ui.internal.editor.jspeditor.dnd;

import java.util.Properties;

import org.jboss.tools.common.model.options.SharableConstants;
import org.jboss.tools.common.model.ui.editors.dnd.IDropWizard;
import org.jboss.tools.common.model.ui.editors.dnd.IDropWizardModel;
import org.jboss.tools.common.model.ui.editors.dnd.IElementGenerator;
import org.jboss.tools.common.model.ui.editors.dnd.PaletteDropWizardModel;
import org.jboss.tools.common.model.ui.views.palette.IPositionCorrector;
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteItem;

public class PaletteItemDropCommand extends FileDropCommand  {
	private String newLine;

	private Properties properties = new Properties();
	
	private IPaletteItem paletteItem;
	private boolean doNotShowWizard = false;
	
	
	private IPositionCorrector corrector;
	
	public PaletteItemDropCommand(IPaletteItem paletteItem, boolean doNotShowWizard){
		super();
		this.paletteItem = paletteItem;
		this.doNotShowWizard = doNotShowWizard;
		corrector = paletteItem.createPositionCorrector();
	}
	
	public IPositionCorrector getPositionCorrector(){
		return corrector;
	}
	
	public void setDoNotShowDialog(boolean doNotShowWizard){
		this.doNotShowWizard = doNotShowWizard;
	}
	
	@Override
	protected boolean doNotShowDialog(){
		return doNotShowWizard;
	}

	/**
	 * 
	 */
	protected IDropWizardModel createSpecificModel() {
		return new PaletteDropWizardModel(tagProposalFactory);
	}
	
	public IPaletteItem getPaletteItem(){
		return paletteItem;
	}

	protected void addCustomProperties(Properties runningProperties) {		
		newLine = properties.getProperty(MobilePaletteInsertHelper.PROPERTY_NEW_LINE);
		if (newLine == null) newLine="true"; //$NON-NLS-1$
		runningProperties.setProperty(MobilePaletteInsertHelper.PROPERTY_NEW_LINE, newLine);
		String addTaglib = properties.getProperty(JSPPaletteInsertHelper.PROPOPERTY_ADD_TAGLIB);
		if(addTaglib == null) addTaglib = "true"; //$NON-NLS-1$
		runningProperties.setProperty(JSPPaletteInsertHelper.PROPOPERTY_ADD_TAGLIB, addTaglib);
	}
	
	public void execute() {
		if(paletteItem.hasWizard()){
			IElementGenerator generator = getDefaultModel().getElementGenerator();
			properties.put(MobilePaletteInsertHelper.PROPERTY_START_TEXT, generator.generateStartTag());
			properties.put(MobilePaletteInsertHelper.PROPERTY_END_TEXT, generator.generateEndTag());
		}else{
			properties.put(MobilePaletteInsertHelper.PROPERTY_START_TEXT, paletteItem.getStartText());
			properties.put(MobilePaletteInsertHelper.PROPERTY_END_TEXT, paletteItem.getEndText());
		}

		properties.setProperty(MobilePaletteInsertHelper.PROPERTY_REFORMAT_BODY, getReformatBodyProperty());
		properties.setProperty(SharableConstants.PALETTE_PATH, MobilePaletteInsertHelper.MOBILE_PATH);
		if(newLine != null) properties.setProperty(MobilePaletteInsertHelper.PROPERTY_NEW_LINE, newLine);
		MobilePaletteInsertHelper.getInstance().insertIntoEditor(
				getDefaultModel().getDropData().getSourceViewer(),
				properties, this
		);
	}

	static String[] PROPERTIES_FOR_RUN = new String[]{
		SharableConstants.PALETTE_PATH,
		MobilePaletteInsertHelper.PROPOPERTY_JQUERY_MOBILE_INSERT_JS_CSS,
		MobilePaletteInsertHelper.PROPOPERTY_PREFERRED_JS_LIB_VERSIONS
	};

	protected void fillPropertiesForRun(Properties properties) {
		for (String name: PROPERTIES_FOR_RUN) {
			if(this.properties.containsKey(name)) {
				properties.put(name, this.properties.get(name));
			}
		}
	}

	public void initialize() {
		boolean isWizardEnabled = getCustomWizardName() != null;
		if(getDefaultModel() instanceof PaletteDropWizardModel) {
			((PaletteDropWizardModel)getDefaultModel()).setWizardEnabled(isWizardEnabled);
		}
		
		getDefaultModel().setTagProposal(IDropWizardModel.UNDEFINED_TAG_PROPOSAL);
	}

	protected String getReformatBodyProperty() {
		if(paletteItem.isReformat()){
			return "yes";
		}else{
			return "no";
		}
	}
	
	protected String getCustomWizardName(){
		if(paletteItem.hasWizard()){
			return "";
		}
		return null;
	}

	protected IDropWizard createDropWizard() {
		IDropWizard wizard = paletteItem.createWizard();
		wizard.setCommand(this);
		return wizard;
	}
	
	public Properties getProperties() {
		return properties;
	}
	
}
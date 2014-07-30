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
package org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard;

import java.beans.PropertyChangeEvent;

import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewSlideboxWizardPage extends NewIonicWidgetWizardPage {
	SlideEditor slides = new SlideEditor(this, 1, 6);

	public NewSlideboxWizardPage() {
		super("newSlidebox", IonicWizardMessages.newSlideboxWizardTitle);
		setDescription(IonicWizardMessages.newSlideboxWizardDescription);
	}

	@Override
	protected void createFieldPanel(Composite parent) {
		IFieldEditor delegate = IonicFieldEditorFactory.createDelegateHandleEditor();
		addEditor(delegate, parent);
		createIDEditor(parent, true);
		addID.setValue(Boolean.FALSE);

		createSeparator(parent);

		addEditor(IonicFieldEditorFactory.createDoesContinueEditor(), parent);
		addEditor(IonicFieldEditorFactory.createAutoplayEditor(), parent);
		addEditor(IonicFieldEditorFactory.createSlideIntervalEditor(), parent);

		createSeparator(parent);

		addEditor(IonicFieldEditorFactory.createShowPagerEditor(), parent);
		addEditor(IonicFieldEditorFactory.createPagerClickEditor(), parent);

		createSeparator(parent);

		addEditor(IonicFieldEditorFactory.createOnSlideChangedEditor(), parent);
		addEditor(IonicFieldEditorFactory.createActiveSlideEditor(), parent);

		slides.createControl(parent, WizardMessages.itemsLabel);

		updateEnablement();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(slides.isSwitching()) {
			return;
		}
		String name = evt.getPropertyName();
		String value = evt.getNewValue().toString();
		if(slides.onPropertyChange(name, value)) {
			//
		}
		if(name.equals(ATTR_DOES_CONTINUE) || name.equals(ATTR_SHOW_PAGER)) {
			updateEnablement();
			//
		}
		//TODO
		super.propertyChange(evt);
	}

	void updateEnablement() {
		boolean doesContinue = isTrue(ATTR_DOES_CONTINUE);
		setEnabled(ATTR_SLIDE_INTERVAL, doesContinue);
		boolean showPager = isTrue(ATTR_SHOW_PAGER);
		setEnabled(ATTR_PAGER_CLICK, showPager);
	}

}

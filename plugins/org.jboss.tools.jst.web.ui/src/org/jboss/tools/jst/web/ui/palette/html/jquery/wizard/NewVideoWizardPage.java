/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.palette.html.jquery.wizard;

import java.beans.PropertyChangeEvent;

import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.jst.web.ui.internal.properties.advanced.LayoutUtil.TwoColumns;
import org.jboss.tools.jst.web.ui.palette.html.wizard.NewHTMLWidgetWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewVideoWizardPage extends NewHTMLWidgetWizardPage {
	VideoSourceEditor items = new VideoSourceEditor(this, 1, 3);

	public NewVideoWizardPage() {
		super("newVideo", WizardMessages.newVideoWizardTitle);
		setDescription(WizardMessages.newVideoWizardDescription);
	}

	protected void createFieldPanel(Composite parent) {
		createIDEditor(parent, true);
		items.createControl(parent, WizardMessages.sourcesLabel);

		addEditor(JQueryFieldEditorFactory.createPosterEditor(getWizard().getFile()), parent);
		TwoColumns columns = createTwoColumns(parent);
		addEditor(JQueryFieldEditorFactory.createWidthEditor(WizardDescriptions.videoWidth), columns.left());
		addEditor(JQueryFieldEditorFactory.createHeightEditor(WizardDescriptions.videoHeight), columns.right());
		addEditor(JQueryFieldEditorFactory.createAutoplayEditor(WizardDescriptions.videoAutoplay), columns.left());
		addEditor(JQueryFieldEditorFactory.createControlsEditor(WizardDescriptions.videoControls), columns.right());
		addEditor(JQueryFieldEditorFactory.createLoopEditor(WizardDescriptions.videoLoop), columns.left());
		addEditor(JQueryFieldEditorFactory.createMutedEditor(WizardDescriptions.videoMuted), columns.right());
		addEditor(JQueryFieldEditorFactory.createPreloadVideoEditor(), parent);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(items.isSwitching) {
			return;
		}
		String name = evt.getPropertyName();
		String value = evt.getNewValue().toString();
		items.onPropertyChange(name, value);

		if(EDITOR_ID_AUTOPLAY.equals(evt.getPropertyName())) {
			updatePreloadEnablement();
		}
		super.propertyChange(evt);
	}

	protected void updatePreloadEnablement() {
		setEnabled(EDITOR_ID_PRELOAD, !isTrue(EDITOR_ID_AUTOPLAY));
	}

}

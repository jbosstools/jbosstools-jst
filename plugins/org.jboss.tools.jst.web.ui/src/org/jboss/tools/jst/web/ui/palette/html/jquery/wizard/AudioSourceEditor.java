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

import org.jboss.tools.jst.web.ui.palette.html.wizard.AbstractNewHTMLWidgetWizardPage;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class AudioSourceEditor extends ItemsEditor {

	public AudioSourceEditor(AbstractNewHTMLWidgetWizardPage page, int minNumber, int maxNumber) {
		super(page, minNumber, maxNumber, 1);
		for (int i = 0; i < maxNumber; i++) {
			items[i].setValue(EDITOR_ID_SRC, "");
			items[i].setValue(EDITOR_ID_AUDIO_TYPE, "");
		}
	}

	protected void createItemEditors() {
		addItemEditor(JQueryFieldEditorFactory.createAudioSrcEditor(page.getWizard().getFile()));
		addItemEditor(JQueryFieldEditorFactory.createAudioTypeEditor());
	}

	public String getSrc(int i) {
		return items[i].getValue(EDITOR_ID_SRC);
	}

	public String getType(int i) {
		return items[i].getValue(EDITOR_ID_AUDIO_TYPE);
	}

}

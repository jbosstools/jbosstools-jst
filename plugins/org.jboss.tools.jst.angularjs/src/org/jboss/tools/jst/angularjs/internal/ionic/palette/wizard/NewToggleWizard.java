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

import org.jboss.tools.common.model.ui.editors.dnd.DropWizardMessages;
import org.jboss.tools.common.model.ui.editors.dnd.IElementGenerator.ElementNode;
import org.jboss.tools.jst.angularjs.internal.ui.AngularJsUIImages;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryConstants;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewToggleWizard extends NewIonicWidgetWizard<NewToggleWizardPage> implements IonicConstants {
	static String prefixName = "toggle-";
	static String prefixId = "toggle-";

	public NewToggleWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(AngularJsUIImages.getInstance()
				.getOrCreateImageDescriptor(AngularJsUIImages.TOGGLE_IMAGE));
	}

	@Override
	protected NewToggleWizardPage createPage() {
		return new NewToggleWizardPage();
	}

	@Override
	protected void addContent(ElementNode parent, boolean browser) {
		String label = page.getEditorValue(JQueryConstants.EDITOR_ID_LABEL);
		ElementNode input = parent.addChild(TAG_ION_TOGGLE, label);

		addID(prefixId, input);

		addAttributeIfNotEmpty(input, ATTR_NG_MODEL, ATTR_NG_MODEL);
		addAttributeIfNotEmpty(input, ATTR_NAME, JQueryConstants.EDITOR_ID_NAME);

		if(isTrue(JQueryConstants.EDITOR_ID_CHECKED)) {
			input.addAttribute(ATTR_NG_CHECKED, TRUE);
		}
		if(isTrue(JQueryConstants.EDITOR_ID_DISABLED)) {
			input.addAttribute(ATTR_NG_DISABLED, TRUE);
		}
		addAttributeIfNotEmpty(input, ATTR_TOGGLE_CLASS, ATTR_TOGGLE_CLASS);
		addAttributeIfNotEmpty(input, ATTR_NG_TRUE_VALUE, ATTR_NG_TRUE_VALUE);
		addAttributeIfNotEmpty(input, ATTR_NG_FALSE_VALUE, ATTR_NG_FALSE_VALUE);
		addAttributeIfNotEmpty(input, ATTR_NG_CHANGE, ATTR_NG_CHANGE);
	}

}

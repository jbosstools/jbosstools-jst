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

import org.jboss.tools.common.model.ui.editors.dnd.DropWizardMessages;
import org.jboss.tools.common.model.ui.editors.dnd.IElementGenerator.ElementNode;
import org.jboss.tools.jst.web.ui.JSTWebUIImages;
import org.jboss.tools.jst.web.ui.palette.html.wizard.AbstractNewHTMLWidgetWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewFormButtonWizard extends NewJQueryWidgetWizard<NewFormButtonWizardPage> implements JQueryConstants {

	public NewFormButtonWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(JSTWebUIImages.getInstance()
				.getOrCreateImageDescriptor(JSTWebUIImages.FORM_BUTTON_IMAGE));
	}

	protected NewFormButtonWizardPage createPage() {
		return new NewFormButtonWizardPage();
	}

	protected void addContent(ElementNode parent) {
		ElementNode a = parent.addChild(TAG_INPUT);
		addAttributeIfNotEmpty(a, ATTR_TYPE, EDITOR_ID_FORM_BUTTON_TYPE);

		String value = page.getEditorValue(EDITOR_ID_VALUE);
		if(value.length() == 0) {
			String type = page.getEditorValue(EDITOR_ID_FORM_BUTTON_TYPE);
			if(BUTTON_TYPE_BUTTON.equals(type)) {
				value = "Input";
			} else if(BUTTON_TYPE_RESET.equals(type)) {
				value = WizardMessages.buttonTypeResetLabel;
			} else if(BUTTON_TYPE_SUBMIT.equals(type)) {
				value = WizardMessages.buttonTypeSubmitLabel;
			}
		}
		a.addAttribute(ATTR_VALUE, value);
		
		if(TRUE.equals(page.getEditorValue(EDITOR_ID_DISABLED))) {
			a.addAttribute(ATTR_DISABLED, ATTR_DISABLED);
		}

		addID("button-", a);

//		a.addAttribute(ATTR_DATA_ROLE, ROLE_BUTTON);
		String icon = page.getEditorValue(EDITOR_ID_ICON);
		if(icon.length() > 0) {
			a.addAttribute(ATTR_DATA_ICON, icon);
		}
		if(TRUE.equals(page.getEditorValue(EDITOR_ID_ICON_ONLY))) {
			a.addAttribute(ATTR_DATA_ICONPOS, ICONPOS_NOTEXT);
		} else {
			String iconpos = page.getEditorValue(EDITOR_ID_ICON_POS);
			if(iconpos.length() > 0) {
				a.addAttribute(ATTR_DATA_ICONPOS, iconpos);
			}
		}
		if(isMini()) {
			a.addAttribute(ATTR_DATA_MINI, TRUE);
		}
		if(TRUE.equals(page.getEditorValue(EDITOR_ID_INLINE))) {
			a.addAttribute(ATTR_DATA_INLINE, TRUE);
		}
		if(!isTrue(EDITOR_ID_CORNERS)) {
			a.addAttribute(ATTR_DATA_CORNERS, FALSE);
		}

		String themeValue = page.getEditorValue(EDITOR_ID_THEME);
		if(themeValue.length() > 0) {
			a.addAttribute(ATTR_DATA_THEME, themeValue);
		}
	}

	public static <P extends AbstractNewHTMLWidgetWizardPage> void applyAction(P page, ElementNode a) {
		String action = page.getEditorValue(EDITOR_ID_ACTION);
		if(WizardMessages.actionDialogLabel.equals(action)) {
			a.addAttribute(ATTR_DATA_REL, DATA_REL_DIALOG);
		} else if(WizardMessages.actionPopupLabel.equals(action)) {
			a.addAttribute(ATTR_DATA_REL, DATA_REL_POPUP);
		} else if(WizardMessages.actionBackLabel.equals(action)) {
			a.addAttribute(ATTR_DATA_REL, DATA_REL_BACK);
		} else if(WizardMessages.actionCloseLabel.equals(action)) {
			a.addAttribute(ATTR_DATA_REL, DATA_REL_CLOSE);
		} else if(WizardMessages.actionExternalLabel.equals(action)) {
			a.addAttribute(ATTR_DATA_REL, DATA_REL_EXTERNAL);
		}
	}

	protected void createBodyForBrowser(ElementNode body) {
		ElementNode form = getFormNode(body);
		ElementNode div = form.addChild(TAG_DIV);
		div.addAttribute(ATTR_STYLE, "padding: 20px 20px 20px 20px;");
		addContent(div);
	}
	
}

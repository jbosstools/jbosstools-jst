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
public class NewButtonWizard extends NewIonicWidgetWizard<NewButtonWizardPage> implements IonicConstants {

	public NewButtonWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(AngularJsUIImages.getInstance()
				.getOrCreateImageDescriptor(AngularJsUIImages.BUTTON_IMAGE));
	}

	protected NewButtonWizardPage createPage() {
		return new NewButtonWizardPage();
	}

	@Override
	protected void addContent(ElementNode parent, boolean browser) {
		if(browser) {
			ElementNode div = parent.addChild(TAG_DIV);
			div.addAttribute(ATTR_STYLE, "padding: 20px 20px 20px 20px;");
			parent = div;
		}
		String tag = page.getEditorValue(EDITOR_ID_BUTTON_TYPE);
		ElementNode a = parent.addChild(tag, page.getEditorValue(JQueryConstants.EDITOR_ID_LABEL));

		addID("button-", a);

		addButtonAttributes(a);
	}

	private void addButtonAttributes(ElementNode a) {
		StringBuilder cls = new StringBuilder();
		cls.append("button");
		String icon = page.getEditorValue(JQueryConstants.ATTR_ICON);
		if(icon.length() > 0) {
			String iconpos = page.getEditorValue(JQueryConstants.EDITOR_ID_ICON_POS);
			if(iconpos ==  null || iconpos.length() == 0) iconpos = "left";
			addClass(cls, "icon-" + iconpos);
			addClass(cls, icon);
		}

		addClassByEditorID(EDITOR_ID_BUTTON_WIDTH, cls);
		addClassByEditorID(EDITOR_ID_BUTTON_SIZE, cls);
		addClassByEditorID(EDITOR_ID_BUTTON_FILL, cls);
		addClassByEditorID(EDITOR_ID_BAR_COLOR, cls);
		
		if(isTrue(JQueryConstants.EDITOR_ID_DISABLED)) {
			a.addAttribute("disabled", "disabled");
		}

		a.addAttribute(ATTR_CLASS, cls.toString());
	}

	private void addClassByEditorID(String editorID, StringBuilder cls) {
		String value = page.getEditorValue(editorID);
		if(value.length() > 0 && !"none".equals(value)) {
			addClass(cls, value);
		}
	}
	
}

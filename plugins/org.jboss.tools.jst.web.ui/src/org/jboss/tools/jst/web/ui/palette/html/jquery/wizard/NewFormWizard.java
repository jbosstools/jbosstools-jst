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
import org.jboss.tools.jst.web.ui.JSTWebUIImages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewFormWizard extends NewJQueryWidgetWizard<NewFormWizardPage> implements JQueryConstants {
	static String prefixId = "form-";

	public NewFormWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(JSTWebUIImages.getInstance()
				.getOrCreateImageDescriptor(JSTWebUIImages.FORM_IMAGE));
	}

	protected NewFormWizardPage createPage() {
		return new NewFormWizardPage();
	}

	protected void addContent(ElementNode parent) {
		ElementNode form = parent.addChild(TAG_FORM, "");
		addAttributeIfNotEmpty(form, ATTR_NAME, EDITOR_ID_NAME);
		addID(prefixId, form);
		addAttributeIfNotEmpty(form, ATTR_ACTION, EDITOR_ID_FORM_ACTION);
		if(METHOD_POST.equals(page.getEditorValue(EDITOR_ID_FORM_METHOD))) {
			form.addAttribute(ATTR_METHOD, METHOD_POST);
		}
		if(!isTrue(EDITOR_ID_AUTOCOMPLETE)) {
			form.addAttribute(ATTR_AUTOCOMPLETE, AUTOCOMPLETE_OFF);
		}
		if(!isTrue(EDITOR_ID_VALIDATE)) {
			form.addAttribute(ATTR_NOVALIDATE, ATTR_NOVALIDATE);
		}
		form.getChildren().add(SEPARATOR);
	}

	protected void createBodyForBrowser(ElementNode body) {
	}

}

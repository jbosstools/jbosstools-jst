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
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewLinkWizard extends NewJQueryWidgetWizard<NewLinkWizardPage> implements JQueryConstants {

	public NewLinkWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(JSTWebUIImages.getInstance()
				.getOrCreateImageDescriptor(JSTWebUIImages.LINK_IMAGE));
	}

	protected NewLinkWizardPage createPage() {
		return new NewLinkWizardPage();
	}

	protected void addContent(ElementNode parent) {
		ElementNode a = parent.addChild(TAG_A, page.getEditorValue(EDITOR_ID_LABEL));

		a.addAttribute(ATTR_HREF, page.getEditorValue(EDITOR_ID_URL));

		NewButtonWizard.applyAction(page, a);

		String transition = page.getEditorValue(EDITOR_ID_TRANSITION);
		if(transition.length() > 0) {
			a.addAttribute(ATTR_DATA_TRANSITION, transition);
		}
	}

	protected void createBodyForBrowser(ElementNode body) {
		ElementNode form = getFormNode(body);
		ElementNode div = form.addChild(TAG_DIV);
		div.addAttribute(ATTR_STYLE, "padding: 20px 20px 20px 20px;");
		addContent(div);
	}
	
}

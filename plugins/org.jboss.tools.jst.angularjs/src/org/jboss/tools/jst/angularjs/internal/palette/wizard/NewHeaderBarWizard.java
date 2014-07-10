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
package org.jboss.tools.jst.angularjs.internal.palette.wizard;

import org.jboss.tools.common.model.ui.editors.dnd.DropWizardMessages;
import org.jboss.tools.common.model.ui.editors.dnd.IElementGenerator.ElementNode;
import org.jboss.tools.jst.web.ui.JSTWebUIImages;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryConstants;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewHeaderBarWizard extends NewIonicWidgetWizard<NewHeaderBarWizardPage> implements IonicConstants {

	public NewHeaderBarWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(JSTWebUIImages.getInstance()
				.getOrCreateImageDescriptor(JSTWebUIImages.HEADER_IMAGE));
	}

	@Override
	protected NewHeaderBarWizardPage createPage() {
		return new NewHeaderBarWizardPage();
	}

	@Override
	protected void addContent(ElementNode parent, boolean browser) {
		ElementNode header = parent.addChild(TAG_ION_HEADER_BAR);

		addAttributeIfNotEmpty(header, ATTR_ALIGN_TITLE, ATTR_ALIGN_TITLE);

		if(isTrue(ATTR_NO_TAP_SCROLL)) {
			header.addAttribute(ATTR_NO_TAP_SCROLL, TRUE);
		}

		StringBuilder cls = new StringBuilder();
		if(isTrue(CLASS_BAR_SUBHEADER)) {
			addClass(cls, CLASS_BAR_SUBHEADER);
		}

		String barColor = page.getEditorValue(EDITOR_ID_BAR_COLOR);
		if(barColor.length() > 0) {
			addClass(cls, barColor);
		}
		if(cls.length() > 0) {
			header.addAttribute(ATTR_CLASS, cls.toString());
		}

		addID("header-", header);

		if(isTrue(JQueryConstants.EDITOR_ID_LEFT_BUTTON)) {
			ElementNode leftButtons = header.addChild(TAG_DIV);
			leftButtons.addAttribute(ATTR_CLASS, CLASS_BUTTONS);
			String label = page.getEditorValue(JQueryConstants.EDITOR_ID_LEFT_BUTTON_LABEL);
			ElementNode leftButton = leftButtons.addChild(TAG_BUTTON, label);
			String leftButtonClick = page.getEditorValue(EDITOR_ID_LEFT_BUTTON_CLICK);
			if(leftButtonClick.length() > 0) {
				leftButton.addAttribute(ATTR_NG_CLICK, leftButtonClick);
			}
			cls = new StringBuilder();
			addClass(cls, CLASS_BUTTON);
			String icon = page.getEditorValue(JQueryConstants.EDITOR_ID_LEFT_BUTTON_ICON);
			if(icon.length() > 0) {
				addClass(cls, CLASS_ICON);
				addClass(cls, icon);
			}
			leftButton.addAttribute(ATTR_CLASS, cls.toString());
		}

		String title = page.getEditorValue(JQueryConstants.EDITOR_ID_TITLE);
		ElementNode titleNode = header.addChild("h1", title);
		titleNode.addAttribute(ATTR_CLASS, CLASS_TITLE);

		if(isTrue(JQueryConstants.EDITOR_ID_RIGHT_BUTTON)) {
			ElementNode rightButtons = header.addChild(TAG_DIV);
			rightButtons.addAttribute(ATTR_CLASS, CLASS_BUTTONS);
			String label = page.getEditorValue(JQueryConstants.EDITOR_ID_RIGHT_BUTTON_LABEL);
			ElementNode rightButton = rightButtons.addChild(TAG_BUTTON, label);
			String rightButtonClick = page.getEditorValue(EDITOR_ID_RIGHT_BUTTON_CLICK);
			if(rightButtonClick.length() > 0) {
				rightButton.addAttribute(ATTR_NG_CLICK, rightButtonClick);
			}
			cls = new StringBuilder();
			addClass(cls, CLASS_BUTTON);
			String icon = page.getEditorValue(JQueryConstants.EDITOR_ID_RIGHT_BUTTON_ICON);
			if(icon.length() > 0) {
				addClass(cls, CLASS_ICON);
				addClass(cls, icon);
			}
			rightButton.addAttribute(ATTR_CLASS, cls.toString());
		}
	}

	@Override
	protected void createBodyForBrowser(ElementNode body) {
		super.createBodyForBrowser(body);
	}
}
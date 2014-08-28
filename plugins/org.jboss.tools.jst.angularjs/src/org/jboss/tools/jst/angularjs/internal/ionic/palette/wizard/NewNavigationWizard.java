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

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewNavigationWizard extends NewIonicWidgetWizard<NewNavigationWizardPage> implements IonicConstants {

	public NewNavigationWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(AngularJsUIImages.getInstance()
				.getOrCreateImageDescriptor(AngularJsUIImages.NAVIGATION_IMAGE));
	}

	@Override
	protected NewNavigationWizardPage createPage() {
		return new NewNavigationWizardPage();
	}

	@Override
	protected void addContent(ElementNode parent, boolean browser) {
		ElementNode navbar = parent.addChild(TAG_ION_NAV_BAR);
		addAttributeIfNotEmpty(navbar, ATTR_DELEGATE_HANDLE, ATTR_DELEGATE_HANDLE);
		addAttributeIfNotEmpty(navbar, ATTR_ALIGN_TITLE, ATTR_ALIGN_TITLE);
		addAttributeIfNotEmpty(navbar, ATTR_ANIMATION, EDITOR_ID_NAV_BAR_ANIMATION);
		String barcolor = page.getEditorValue(EDITOR_ID_BAR_COLOR);
		if(barcolor.length() > 0) {
			navbar.addAttribute(ATTR_CLASS, barcolor);
		}

		if(isTrue(ATTR_NO_TAP_SCROLL)) {
			navbar.addAttribute(ATTR_NO_TAP_SCROLL, TRUE);
		}
		if(isTrue(TAG_ION_NAV_BACK_BUTTON)) {
			String tag = (!browser) ? TAG_ION_NAV_BACK_BUTTON : TAG_BUTTON;
			ElementNode back = navbar.addChild(tag);
			back.addAttribute(ATTR_CLASS, "button-clear");
			back.addChild("i", "Back").addAttribute(ATTR_CLASS, "ion-arrow-left-c");
		}
		navbar.addComment("Initial content");

		if(!browser) {
			ElementNode navview = parent.addChild(TAG_ION_NAV_VIEW);
			addAttributeIfNotEmpty(navview, ATTR_NAME, ATTR_NAME);
			addAttributeIfNotEmpty(navview, ATTR_ANIMATION, EDITOR_ID_NAV_VIEW_ANIMATION);
			navview.addComment("Initial content.");
		} else {
			ElementNode view = parent.addChild(TAG_ION_VIEW);
			view.addChild(TAG_ION_CONTENT, "Initial view content");
			view.addAttribute(ATTR_TITLE, "View Title");
			view.addAttribute(ATTR_HIDE_BACK_BUTTON, FALSE);
		}
	}

}
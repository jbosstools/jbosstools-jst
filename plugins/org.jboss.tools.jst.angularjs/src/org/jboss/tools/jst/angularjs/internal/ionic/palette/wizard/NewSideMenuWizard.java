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
public class NewSideMenuWizard extends NewIonicWidgetWizard<NewSideMenuWizardPage> implements IonicConstants {
	static String prefixId = "sidemenu-";

	public NewSideMenuWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(AngularJsUIImages.getInstance()
				.getOrCreateImageDescriptor(AngularJsUIImages.SIDEMENU_IMAGE));
	}

	@Override
	protected NewSideMenuWizardPage createPage() {
		return new NewSideMenuWizardPage();
	}

	@Override
	protected void addContent(ElementNode parent, boolean browser) {
		ElementNode menus = parent.addChild(TAG_ION_SIDE_MENUS);
		addAttributeIfNotEmpty(menus, ATTR_DELEGATE_HANDLE, ATTR_DELEGATE_HANDLE);

		addID(prefixId, menus);

		ElementNode content = menus.addChild(TAG_ION_SIDE_MENU_CONTENT, null);

		if(!isTrue(ATTR_DRAG_CONTENT)) {
			content.addAttribute(ATTR_DRAG_CONTENT, FALSE);
		}
		addAttributeIfNotEmpty(content, ATTR_EDGE_DRAG_THRESHOLD, ATTR_EDGE_DRAG_THRESHOLD);

		if(isTrue(EDITOR_ID_LEFT_MENU)) {
			ElementNode leftMenu = menus.addChild(TAG_ION_SIDE_MENU, null);
			leftMenu.addAttribute(ATTR_SIDE, SIDE_LEFT);
			boolean hasHeader = page.getEditorValue(EDITOR_ID_LEFT_MENU_TITLE).length() > 0;
			if(hasHeader) {
				ElementNode header = leftMenu.addChild(TAG_ION_HEADER_BAR);
				header.addChild("h4", "" + page.getEditorValue(EDITOR_ID_LEFT_MENU_TITLE));
			}
			ElementNode leftContent = leftMenu.addChild(TAG_ION_CONTENT);
			if(hasHeader) leftContent.addAttribute(ATTR_CLASS, "has-header");
			leftContent.addComment("left menu content");
			if(!isTrue(EDITOR_ID_LEFT_IS_ENABLED)) {
				leftMenu.addAttribute(ATTR_IS_ENABLED, FALSE);
			}
			addAttributeIfNotEmpty(leftMenu, ATTR_WIDTH, EDITOR_ID_LEFT_WIDTH);
		}

		if(isTrue(EDITOR_ID_RIGHT_MENU)) {
			ElementNode rightMenu = menus.addChild(TAG_ION_SIDE_MENU, null);
			rightMenu.addAttribute(ATTR_SIDE, "right");
			boolean hasHeader = page.getEditorValue(EDITOR_ID_RIGHT_MENU_TITLE).length() > 0;
			if(hasHeader) {
				ElementNode header = rightMenu.addChild(TAG_ION_HEADER_BAR);
				header.addChild("h4", "" + page.getEditorValue(EDITOR_ID_RIGHT_MENU_TITLE));
			}
			ElementNode rightContent = rightMenu.addChild(TAG_ION_CONTENT);
			if(hasHeader) rightContent.addAttribute(ATTR_CLASS, "has-header");
			rightContent.addComment("right menu content");
			if(!isTrue(EDITOR_ID_RIGHT_IS_ENABLED)) {
				rightMenu.addAttribute(ATTR_IS_ENABLED, FALSE);
			}
			addAttributeIfNotEmpty(rightMenu, ATTR_WIDTH, EDITOR_ID_RIGHT_WIDTH);
		}

		boolean addLeftMenuToggle = isTrue(EDITOR_ID_LEFT_MENU) && isTrue(EDITOR_ID_LEFT_ADD_MENU_TOGGLE);
		boolean addRightMenuToggle = isTrue(EDITOR_ID_RIGHT_MENU) && isTrue(EDITOR_ID_RIGHT_ADD_MENU_TOGGLE);

		if(addLeftMenuToggle || addRightMenuToggle) {
			ElementNode bar = content.addChild("ion-nav-bar");
			if(addLeftMenuToggle) {
				ElementNode buttons = bar.addChild(TAG_ION_NAV_BUTTONS);
				buttons.addAttribute(ATTR_CLASS, "bar-stable nav-title-slide-ios7");
				ElementNode button = buttons.addChild(TAG_BUTTON, "");
				buttons.addAttribute(ATTR_SIDE, SIDE_LEFT);
				button.addAttribute(ATTR_MENU_TOGGLE, SIDE_LEFT);
				button.addAttribute(ATTR_CLASS, "button button-icon icon ion-navicon");
			}
			if(addRightMenuToggle) {
				ElementNode buttons = bar.addChild(TAG_ION_NAV_BUTTONS);
				buttons.addAttribute(ATTR_CLASS, "bar-stable nav-title-slide-ios7");
				ElementNode button = buttons.addChild(TAG_BUTTON, "");
				buttons.addAttribute(ATTR_SIDE, SIDE_RIGHT);
				button.addAttribute(ATTR_MENU_TOGGLE, SIDE_RIGHT);
				button.addAttribute(ATTR_CLASS, "button button-icon icon ion-navicon");
			}
			ElementNode view = content.addChild(TAG_ION_VIEW, "");
			view.addAttribute(ATTR_TITLE, "Title");
		}

		if(browser) {
			content.addChild(TAG_ION_VIEW).addChild(TAG_ION_CONTENT, "content");
		} else {
			content.addChild(TAG_ION_VIEW).addChild(TAG_ION_CONTENT).addComment("center content");
		}
	}

}

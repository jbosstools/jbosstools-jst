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
public class NewListWizard extends NewIonicWidgetWizard<NewListWizardPage> {

	public NewListWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(AngularJsUIImages.getInstance()
				.getOrCreateImageDescriptor(AngularJsUIImages.LIST_IMAGE));
	}

	@Override
	protected NewListWizardPage createPage() {
		return new NewListWizardPage();
	}

	@Override
	protected void addContent(ElementNode parent, boolean browser) {
		ElementNode list = parent.addChild(TAG_ION_LIST);
		addAttributeIfNotEmpty(list, ATTR_DELEGATE_HANDLE, ATTR_DELEGATE_HANDLE);
		addID("list-", list);
		addAttributeIfNotEmpty(list, ATTR_CLASS, ATTR_TYPE);

		if(isTrue(ATTR_SHOW_DELETE)) {
			list.addAttribute(ATTR_SHOW_DELETE, TRUE);
		}

		if(isTrue(ATTR_SHOW_REORDER)) {
			list.addAttribute(ATTR_SHOW_REORDER, TRUE);
		}

		if(!isTrue(ATTR_CAN_SWIPE)) {
			list.addAttribute(ATTR_CAN_SWIPE, FALSE);
		}

		String optionLabel = page.getEditorValue(EDITOR_ID_OPTION_BUTTON);
		boolean deleteButton = isTrue(EDITOR_ID_DELETE_BUTTON);
		boolean reorderButton = isTrue(EDITOR_ID_REORDER_BUTTON);
		boolean nobutton = optionLabel.length() == 0 && !deleteButton && !reorderButton;

		int n = page.list.getNumber();
		for (int i = 0; i < n; i++) {
			String label = page.list.getLabel(i);
			String icon = page.list.getIcon(i);
			String style = page.list.getStyle(i);
			boolean isDivider = page.list.isDivider(i);
			ElementNode item = list.addChild(TAG_ION_ITEM);

			if((nobutton || isDivider) && icon.length() == 0) {
				item.addTextChild(label);
			} else {
				ElementNode span = item.addChild("span", label);
				if(icon.length() > 0) {
					span.addAttribute(ATTR_CLASS, CLASS_ITEM_ICON_LEFT);
					span.addChild("i", "").addAttribute(ATTR_CLASS, "icon " + icon);
				}
			}
			
			StringBuilder cls = new StringBuilder();
			if(isDivider) {
				addClass(cls, CLASS_ITEM_DIVIDER);
			} else {
				if(style.length() > 0) addClass(cls, style);
			}
			if(cls.length() > 0) {
				item.addAttribute(ATTR_CLASS, cls.toString());
			}

			if(isDivider) {
				continue;
			}
			if(optionLabel.length() > 0) {
				ElementNode option = item.addChild(TAG_ION_OPTION_BUTTON, optionLabel);
				option.addAttribute(ATTR_CLASS, "button-calm");
				option.addAttribute(ATTR_NG_CLICK, "");
			}			
			if(deleteButton) {
				ElementNode delete = item.addChild(TAG_ION_DELETE_BUTTON, "");
				delete.addAttribute(ATTR_CLASS, "ion-minus-circled");
				delete.addAttribute(ATTR_NG_CLICK, "");
			}
			if(reorderButton) {
				ElementNode reorder = item.addChild(TAG_ION_REORDER_BUTTON, "");
				reorder.addAttribute(ATTR_CLASS, "ion-navicon");
				reorder.addAttribute(ATTR_ON_REORDER, "");
			}
		}
		
	}
}

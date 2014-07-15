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
public class NewContentWizard extends NewIonicWidgetWizard<NewContentWizardPage> implements IonicConstants {

	public NewContentWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(AngularJsUIImages.getInstance()
				.getOrCreateImageDescriptor(AngularJsUIImages.CONTENT_IMAGE));
	}

	@Override
	protected NewContentWizardPage createPage() {
		return new NewContentWizardPage();
	}

	protected void addContent(ElementNode parent, boolean browser) {
		ElementNode pg = parent.addChild(TAG_ION_CONTENT, "\n");
		if(browser) {
			ElementNode table = pg.addChild(TAG_TABLE);
			table.addAttribute(ATTR_WIDTH, "150%");
			for (int i = 0; i < 10; i++) {
				ElementNode tr = table.addChild(TAG_TR);
				for (int j = 0; j < 6; j++) {
					ElementNode td = tr.addChild(TAG_TD, "content");
					td.addAttribute(ATTR_HEIGHT, "30mm");
				}
			}
		}
		addAttributeIfNotEmpty(pg, ATTR_DELEGATE_HANDLE, ATTR_DELEGATE_HANDLE);

		addID("content-", pg);

		if(!isTrue(ATTR_SCROLL)) {
			pg.addAttribute(ATTR_SCROLL, FALSE);
		}

		boolean scrollEnabled = isTrue(ATTR_SCROLL);
		if(scrollEnabled) {
			if(isTrue(ATTR_OVERFLOW_SCROLL)) {
				pg.addAttribute(ATTR_OVERFLOW_SCROLL, TRUE);
			}
		}
		boolean ionicScrollEnabled = scrollEnabled && !isTrue(ATTR_OVERFLOW_SCROLL);
		if(ionicScrollEnabled) {
			addAttributeIfNotEmpty(pg, ATTR_DIRECTION, ATTR_DIRECTION);
		}
		String direction = page.getEditorValue(ATTR_DIRECTION);
		boolean xEnabled = direction.indexOf("x") >= 0;
		boolean yEnabled = direction.indexOf("y") >= 0 || direction.length() == 0;
		if(!isTrue(ATTR_SCROLLBAR_X) && ionicScrollEnabled && xEnabled) {
			pg.addAttribute(ATTR_SCROLLBAR_X, FALSE);
		}
		if(!isTrue(ATTR_SCROLLBAR_Y) && ionicScrollEnabled && yEnabled) {
			pg.addAttribute(ATTR_SCROLLBAR_Y, FALSE);
		}
		if(ionicScrollEnabled) {
			addAttributeIfNotEmpty(pg, ATTR_START_Y, ATTR_START_Y);
		}
		addAttributeIfNotEmpty(pg, ATTR_PADDING, ATTR_PADDING);
		if(scrollEnabled) {
			addAttributeIfNotEmpty(pg, ATTR_ON_SCROLL, ATTR_ON_SCROLL);
			addAttributeIfNotEmpty(pg, ATTR_ON_SCROLL_COMPLETE, ATTR_ON_SCROLL_COMPLETE);
		}

	}
}
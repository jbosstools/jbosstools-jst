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
package org.jboss.tools.jst.angularjs.internal.palette.wizard;

import org.jboss.tools.common.model.ui.editors.dnd.DropWizardMessages;
import org.jboss.tools.common.model.ui.editors.dnd.IElementGenerator.ElementNode;
import org.jboss.tools.jst.web.ui.JSTWebUIImages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewContentWizard extends NewIonicWidgetWizard<NewContentWizardPage> implements IonicConstants {

	public NewContentWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(JSTWebUIImages.getInstance()
				.getOrCreateImageDescriptor(JSTWebUIImages.PAGE_IMAGE));
	}

	@Override
	protected NewContentWizardPage createPage() {
		return new NewContentWizardPage();
	}

	@Override
	protected void addContent(ElementNode parent) {
		ElementNode pg = parent.addChild(TAG_ION_CONTENT, "content");
//		pg.addAttribute(ATTR_DATA_ROLE, ROLE_PAGE);
		addID("content-", pg);
		
//		String themeValue = page.getEditorValue(EDITOR_ID_THEME);
//
//		if(isTrue(EDITOR_ID_ADD_HEADER)) {
//			ElementNode header = pg.addChild(TAG_DIV);
//			header.addAttribute(ATTR_DATA_ROLE, ROLE_HEADER);
//			header.addChild("h1", page.getEditorValue(EDITOR_ID_HEADER_TITLE));
//			if(themeValue.length() > 0) {
//				header.addAttribute(ATTR_DATA_THEME, themeValue);
//			}
//		}
	}
}
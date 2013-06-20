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

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewGridWizard extends NewJQueryWidgetWizard<NewGridWizardPage> implements JQueryConstants {

	public NewGridWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(JSTWebUIImages.getInstance()
				.getOrCreateImageDescriptor(JSTWebUIImages.GRID_IMAGE));
	}

	protected NewGridWizardPage createPage() {
		return new NewGridWizardPage();
	}

	protected void addContent(ElementNode parent) {
//		String themeValue = page.getEditorValue(EDITOR_ID_THEME);
		
		String columns = page.getEditorValue(EDITOR_ID_GRID_COLUMNS);
		String rows = page.getEditorValue(EDITOR_ID_GRID_ROWS);
		ElementNode div = parent.addChild(TAG_DIV);
		int c = Integer.parseInt(columns);
		int r = Integer.parseInt(rows);
		String s = c == 1 ? "solo" : "" + (char)('a' + (c - 2));
		div.addAttribute(ATTR_CLASS, "ui-grid-" + s);

		for (int k = 0; k < r; k++) {
			for (int i = 0; i < c; i++) {
				ElementNode block = div.addChild(TAG_DIV, "");
				block.addAttribute(ATTR_CLASS, "ui-block-" + (char)('a' + i));
			}
		}
	}

	protected void createBodyForBrowser(ElementNode body) {
		ElementNode form = getFormNode(body);
//		ElementNode div = form.addChild(TAG_DIV);
//		div.addAttribute(ATTR_STYLE, "padding: 20px 20px 20px 20px;");

		String columns = page.getEditorValue(EDITOR_ID_GRID_COLUMNS);
		String rows = page.getEditorValue(EDITOR_ID_GRID_ROWS);
		ElementNode div = form.addChild(TAG_DIV);
		int c = Integer.parseInt(columns);
		int r = Integer.parseInt(rows);
		String s = c == 1 ? "solo" : "" + (char)('a' + (c - 2));
		div.addAttribute(ATTR_CLASS, "ui-grid-" + s);

		for (int k = 0; k < r; k++) {
			for (int i = 0; i < c; i++) {
				ElementNode block = div.addChild(TAG_DIV, "");
				block.addAttribute(ATTR_CLASS, "ui-block-" + (char)('a' + i));
				ElementNode b = block.addChild(TAG_DIV, "Block " + (char)('A' + i));
				b.addAttribute(ATTR_CLASS, "ui-bar ui-bar-e");
				b.addAttribute(ATTR_STYLE, "height:20px");
			}
		}
	}
	
}

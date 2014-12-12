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
package org.jboss.tools.jst.web.ui.palette.internal.html.html5.wizard;

import org.jboss.tools.common.model.ui.editors.dnd.DropWizardMessages;
import org.jboss.tools.common.model.ui.editors.dnd.IElementGenerator.ElementNode;
import org.jboss.tools.jst.web.ui.JSTWebUIImages;
import org.jboss.tools.jst.web.ui.palette.html.wizard.HTMLConstants;
import org.jboss.tools.jst.web.ui.palette.html.wizard.NewHTMLWidgetWizard;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewCanvasWizard extends NewHTMLWidgetWizard<NewCanvasWizardPage> implements HTMLConstants {
	static String prefixId = "canvas-";

	public NewCanvasWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(JSTWebUIImages.getInstance()
				.getOrCreateImageDescriptor(JSTWebUIImages.CANVAS_IMAGE));
	}

	@Override
	protected NewCanvasWizardPage createPage() {
		return new NewCanvasWizardPage();
	}

	@Override
	protected void addContent(ElementNode parent) {
		boolean addScriptTemplate = isTrue(EDITOR_ID_ADD_SCRIPT_TEMPLATE);
		addContentInternal(parent, false, addScriptTemplate);
	}

	private void addContentInternal(ElementNode parent, boolean browser, boolean addScriptTemplate) {
		ElementNode canvas = parent.addChild(TAG_CANVAS, "");
		addID(prefixId, canvas);
		addAttributeIfNotEmpty(canvas, ATTR_WIDTH, ATTR_WIDTH);
		addAttributeIfNotEmpty(canvas, ATTR_HEIGHT, ATTR_HEIGHT);

		if(browser || addScriptTemplate) {
			String id = getID(prefixId);
			String indent = "    ";
			StringBuilder sb = new StringBuilder();
			sb.append("\n");
			sb.append(indent).append("var canvas = document.getElementById('" + id + "');\n");
			sb.append(indent).append("var ctx = canvas.getContext('2d');\n");
			sb.append(indent).append("//draw to ctx object\n");
			sb.append(indent).append("//Sample code fills canvas with grey color:\n");
			sb.append(indent).append("ctx.fillStyle = '#EEEEEE';\n");
			sb.append(indent).append("ctx.fillRect(0, 0, canvas.width, canvas.height);\n");
			parent.addChild(TAG_SCRIPT, sb.toString());		
		}
	}

	protected void createBodyForBrowser(ElementNode body) {
		ElementNode div = body.addChild(TAG_DIV);
		div.addAttribute(ATTR_STYLE, "padding: 20px 20px 20px 20px;");
		addContentInternal(div, true, true);
	}
	
}

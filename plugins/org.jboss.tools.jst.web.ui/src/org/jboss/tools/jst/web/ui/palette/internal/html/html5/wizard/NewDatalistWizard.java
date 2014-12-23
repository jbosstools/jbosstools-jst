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
public class NewDatalistWizard extends NewHTMLWidgetWizard<NewDatalistWizardPage> implements HTMLConstants {
	protected static String prefix = "datalist-";

	public NewDatalistWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(JSTWebUIImages.getInstance()
				.getOrCreateImageDescriptor(JSTWebUIImages.DATALIST_IMAGE));
	}

	@Override
	protected NewDatalistWizardPage createPage() {
		return new NewDatalistWizardPage();
	}

	@Override
	protected void addContent(ElementNode parent) {
		if(isTrue(TAG_INPUT)) {
			String id = getID(prefix);
			ElementNode input = parent.addChild(TAG_INPUT);
			input.addAttribute(ATTR_LIST, id);
		}
		ElementNode listRoot = parent.addChild(TAG_DATALIST);

		addID(prefix, listRoot);

		for (int i = 0; i < page.items.getNumber(); i++) {
			String text = page.items.getLabel(i);
			String value = page.items.getValue(i);
			ElementNode li = listRoot.addChild(TAG_OPTION, text);
			li.addAttribute(ATTR_VALUE, value);
		}
	}

	@Override
	protected void createBodyForBrowser(ElementNode body) {
		ElementNode form = getFormNode(body);
		ElementNode div = form.addChild(TAG_DIV);
		div.addAttribute(ATTR_STYLE, "padding: 20px 20px 20px 20px;");
		
		addContent(div);
	}
	
}

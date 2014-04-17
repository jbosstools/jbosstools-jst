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
import org.jboss.tools.jst.web.ui.palette.html.wizard.NewHTMLWidgetWizard;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewImageWizard extends NewHTMLWidgetWizard<NewImageWizardPage> implements JQueryConstants {
	static String prefixId = "image-";

	public NewImageWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(JSTWebUIImages.getInstance()
				.getOrCreateImageDescriptor(JSTWebUIImages.IMG_IMAGE));
	}

	protected NewImageWizardPage createPage() {
		return new NewImageWizardPage();
	}

	protected void addContent(ElementNode parent) {
		addContent(parent, false);
	}

	protected void addContent(ElementNode parent, boolean forBrowser) {
		ElementNode img = parent.addChild(TAG_IMG, null);
		addID(prefixId, img);
		
		String alt = page.getEditorValue(EDITOR_ID_ALT);
		img.addAttribute(ATTR_ALT, alt);

		String src = page.getEditorValue(EDITOR_ID_SRC);
		if(forBrowser) {
			src = SRCUtil.getAbsoluteSrc(getFile(), src);
		}
		img.addAttribute(ATTR_SRC, src);
		
		addAttributeIfNotEmpty(img, ATTR_WIDTH, EDITOR_ID_WIDTH);
		addAttributeIfNotEmpty(img, ATTR_HEIGHT, EDITOR_ID_HEIGHT);
		if(isTrue(EDITOR_ID_ISMAP)) {
			img.addAttribute(ATTR_ISMAP, ATTR_ISMAP);
		}
		addAttributeIfNotEmpty(img, ATTR_USEMAP, EDITOR_ID_USEMAP);
		addAttributeIfNotEmpty(img, ATTR_CROSSORIGIN, EDITOR_ID_CROSSORIGIN);
	}

	protected void createBodyForBrowser(ElementNode body) {
//		body = getPageContentNode(body);
		ElementNode div = body.addChild(TAG_DIV);
		div.addAttribute(ATTR_STYLE, "padding: 20px 20px 20px 20px;");
		div.addAttribute("align", "center");
		addContent(div, true);
	}

}

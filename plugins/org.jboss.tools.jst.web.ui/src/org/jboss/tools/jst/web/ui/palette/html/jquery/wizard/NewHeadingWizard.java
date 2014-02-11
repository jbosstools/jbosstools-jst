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
package org.jboss.tools.jst.web.ui.palette.html.jquery.wizard;

import org.jboss.tools.common.model.ui.editors.dnd.DropWizardMessages;
import org.jboss.tools.common.model.ui.editors.dnd.IElementGenerator.ElementNode;
import org.jboss.tools.jst.web.ui.JSTWebUIImages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewHeadingWizard extends NewJQueryWidgetWizard<NewHeadingWizardPage> implements JQueryConstants {
	static String PREFIX_HEADING = "heading-";

	public NewHeadingWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(JSTWebUIImages.getInstance()
				.getOrCreateImageDescriptor(JSTWebUIImages.HEADING_IMAGE));
	}

	protected NewHeadingWizardPage createPage() {
		return new NewHeadingWizardPage();
	}

	protected void addContent(ElementNode parent) {
		String title = page.getEditorValue(EDITOR_ID_TITLE);
		String content = "Content";
		String size = page.getEditorValue(EDITOR_ID_HEADING_SIZE);

		boolean corners = isTrue(EDITOR_ID_CORNERS);
		boolean contentCorners = isTrue(EDITOR_ID_HEADING_CONTENT_CORNERS);
		String themeValue = page.getEditorValue(EDITOR_ID_THEME);
		String contentThemeValue = page.getEditorValue(EDITOR_ID_HEADING_CONTENT_THEME);
		
		String layout = page.getEditorValue(EDITOR_ID_HEADING_LAYOUT);
		if(HEADING_LAYOUT_DEFAULT.equals(layout)) {
			ElementNode h3 = parent.addChild(size, title);
			addID(PREFIX_HEADING, h3);
			StringBuilder cls = new StringBuilder();
			cls.append(CLASS_UI_BAR);
			if(themeValue.length() > 0) {
				addClass(cls, CLASS_UI_BAR_PREFIX + themeValue);
			}
			if(corners) {
				addClass(cls, CLASS_UI_CORNER_ALL);
			}
			h3.addAttribute(ATTR_CLASS, cls.toString());

			ElementNode div = parent.addChild(TAG_DIV);
			cls = new StringBuilder();
			cls.append(CLASS_UI_BODY);
			if(contentThemeValue.length() > 0) {
				addClass(cls, CLASS_UI_BODY_PREFIX + contentThemeValue);
			}
			if(contentCorners) {
				addClass(cls, CLASS_UI_CORNER_ALL);
			}
			div.addAttribute(ATTR_CLASS, cls.toString());
			div.addChild(TAG_P, content);
		} else if(HEADING_LAYOUT_COMBINED.equals(layout)) {
			ElementNode div = parent.addChild(TAG_DIV);
			addID(PREFIX_HEADING, div);
			div.addChild(size, title);
			div.addChild(TAG_P, content);

			StringBuilder cls = new StringBuilder();
			cls.append(CLASS_UI_BODY);
			if(themeValue.length() > 0) {
				addClass(cls, CLASS_UI_BODY_PREFIX + themeValue);
			}
			if(corners) {
				addClass(cls, CLASS_UI_CORNER_ALL);
			}
			div.addAttribute(ATTR_CLASS, cls.toString());
		} else if(HEADING_LAYOUT_ATTACHED.equals(layout)) {
			ElementNode div = parent.addChild(TAG_DIV);
			addID(PREFIX_HEADING, div);
			StringBuilder cls = new StringBuilder();
			if(corners || contentCorners) {
				addClass(cls, CLASS_UI_CORNER_ALL);
				addClass(cls, "custom-corners");
				div.addAttribute(ATTR_CLASS, cls.toString());
			}
			
			ElementNode div_h3 = div.addChild(TAG_DIV);
			cls = new StringBuilder();
			addClass(cls, CLASS_UI_BAR);
			if(themeValue.length() > 0) {
				addClass(cls, CLASS_UI_BAR_PREFIX + themeValue);
			}
			div_h3.addAttribute(ATTR_CLASS, cls.toString());
			div_h3.addChild("h3", title); //no size option

			ElementNode div_p = div.addChild(TAG_DIV);
			cls = new StringBuilder();
			addClass(cls, CLASS_UI_BODY);
			if(contentThemeValue.length() > 0) {
				addClass(cls, CLASS_UI_BODY_PREFIX + contentThemeValue);
			}
			div_p.addAttribute(ATTR_CLASS, cls.toString());
			div_p.addChild(TAG_P, content);

			if(corners || contentCorners) {
				StringBuilder styleText = new StringBuilder();
				if(corners) {
				String styleText1 = ".custom-corners .ui-bar {\n"
				+ "  -webkit-border-top-left-radius: inherit;\n"
				+ "  border-top-left-radius: inherit;"
				+ "  -webkit-border-top-right-radius: inherit;"
				+ "  border-top-right-radius: inherit;"
				+ "}\n";
				styleText.append(styleText1);
				}
				if(contentCorners) {
				String styleText2 = ".custom-corners .ui-body {"
				+ "  border-top-width: 0;"
				+ "  -webkit-border-bottom-left-radius: inherit;"
				+ "  border-bottom-left-radius: inherit;"
				+ "  -webkit-border-bottom-right-radius: inherit;"
				+ "  border-bottom-right-radius: inherit;"
				+ "}";
				styleText.append(styleText2);
				}
			
				ElementNode style = div.addChild(TAG_STYLE);
				style.addAttribute(ATTR_SCOPED, ATTR_SCOPED);
				style.addTextChild(styleText.toString());
			}
		}
		
	}

	protected void createBodyForBrowser(ElementNode body) {
		ElementNode form = getFormNode(body);
		ElementNode div = form.addChild(TAG_DIV);
		div.addAttribute(ATTR_STYLE, "padding: 20px 20px 20px 20px;");
		addContent(div);
	}
	
}

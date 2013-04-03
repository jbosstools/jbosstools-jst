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
import org.jboss.tools.jst.web.ui.JSTWebUIImages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewRangeSliderWizard extends NewJQueryWidgetWizard<NewRangeSliderWizardPage> implements JQueryConstants {

	public NewRangeSliderWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(JSTWebUIImages.getInstance()
				.getOrCreateImageDescriptor(JSTWebUIImages.RANGE_SLIDER_IMAGE));
	}

	protected NewRangeSliderWizardPage createPage() {
		return new NewRangeSliderWizardPage();
	}

	protected boolean isRange() {
		return isTrue(EDITOR_ID_RANGE);
	}

	protected void addContent(ElementNode parent) {
		String id = page.getEditorValue(EDITOR_ID_ID);

		if(isRange()) {
			ElementNode div = parent.addChild(TAG_DIV);
			div.addAttribute(ATTR_DATA_ROLE, ROLE_RANGE_SLIDER);
			applyContainerAttributes(div);
			
			String id1 = id;
			String id2 = id;		
			if(id1.length() == 0) {
				id1 = "range-" + generateIndex("range-", "a", 1) + "a";
				id2 = "range-" + generateIndex("range-", "b", 1) + "b";
			} else {
				id2 = id1 + generateIndex(id1, "", 1);
			}

			ElementNode label = div.addChild(TAG_LABEL, page.getEditorValue(EDITOR_ID_LABEL));
			ElementNode input = div.addChild(TAG_INPUT);
			applyAttributes(label, input, id1, EDITOR_ID_VALUE);

			label = div.addChild(TAG_LABEL, page.getEditorValue(EDITOR_ID_LABEL));
			input = div.addChild(TAG_INPUT);
			applyAttributes(label, input, id2, EDITOR_ID_RVALUE);
		} else {
			if(id.length() == 0) {
				id = "range-" + generateIndex("range-", "", 1);
			}

			ElementNode label = parent.addChild(TAG_LABEL, page.getEditorValue(EDITOR_ID_LABEL));
			ElementNode input = parent.addChild(TAG_INPUT);
			applyAttributes(label, input, id, EDITOR_ID_VALUE);
		}
	}

	void applyAttributes(ElementNode label, ElementNode input, String id, String valueEditorID) {
		label.addAttribute(ATTR_FOR, id);
		if(isTrue(EDITOR_ID_HIDE_LABEL)) {
			label.addAttribute(ATTR_CLASS, CLASS_HIDDEN_ACCESSIBLE);
		}

		input.addAttribute(ATTR_NAME, id);
		input.addAttribute(ATTR_ID, id);
		if(!isRange()) {
			applyContainerAttributes(input);
		}
		if(isTrue(EDITOR_ID_DISABLED)) {
			input.addAttribute(ATTR_DISABLED, ATTR_DISABLED);
		}
		input.addAttribute(ATTR_DATA_MIN, page.getEditorValue(EDITOR_ID_MIN));
		input.addAttribute(ATTR_DATA_MAX, page.getEditorValue(EDITOR_ID_MAX));
		String step = page.getEditorValue(EDITOR_ID_STEP);
		if(step.length() > 0) {
			input.addAttribute(ATTR_DATA_STEP, step);
		}
		input.addAttribute(ATTR_DATA_VALUE, page.getEditorValue(valueEditorID));
		input.addAttribute(ATTR_TYPE, TYPE_RANGE);
	}

	void applyContainerAttributes(ElementNode input) {
		if(isMini()) {
			input.addAttribute(ATTR_DATA_MINI, TRUE);
		}
		if(isTrue(EDITOR_ID_HIGHLIGHT)) {
			if(!isRange()) {
				input.addAttribute(ATTR_DATA_HIGHLIGHT, TRUE);
			}
		} else {
			if(isRange()) {
				input.addAttribute(ATTR_DATA_HIGHLIGHT, FALSE);
			}
		}
		String themeValue = page.getEditorValue(EDITOR_ID_THEME);
		if(themeValue.length() > 0) {
			input.addAttribute(ATTR_DATA_THEME, themeValue);
		}
		String trackThemeValue = page.getEditorValue(EDITOR_ID_TRACK_THEME);
		if(trackThemeValue.length() > 0) {
			input.addAttribute(ATTR_DATA_TRACK_THEME, trackThemeValue);
		}
	}

	protected void createBodyForBrowser(ElementNode body) {
		ElementNode form = getFormNode(body);
		ElementNode div = form.addChild(TAG_DIV);
		div.addAttribute(ATTR_STYLE, "padding: 20px 20px 20px 20px;");
		if(isRange() && "mozilla".equals(page.getBrowserType())) {
			div.addChild(TAG_DIV, "Preview is not implemented for this element.");
		} else {
			addContent(div);
		}
	}
	
}

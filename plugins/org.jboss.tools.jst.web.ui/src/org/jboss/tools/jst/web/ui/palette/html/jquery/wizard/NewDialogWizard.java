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
public class NewDialogWizard extends NewJQueryWidgetWizard<NewDialogWizardPage> implements JQueryConstants {

	public NewDialogWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(JSTWebUIImages.getInstance()
				.getOrCreateImageDescriptor(JSTWebUIImages.DIALOG_IMAGE));
	}

	protected NewDialogWizardPage createPage() {
		return new NewDialogWizardPage();
	}

	protected void addContent(ElementNode parent) {
		addContent(parent, false);
	}

	protected void addContent(ElementNode parent, boolean browser) {
		String id = getID("dialog-");
		
		ElementNode dialogDiv = parent.addChild(TAG_DIV);
		if(browser) {
			dialogDiv.addAttribute(ATTR_DATA_ROLE, ROLE_DIALOG);
		} else {
			dialogDiv.addAttribute(ATTR_DATA_ROLE, ROLE_DIALOG);
		}
		dialogDiv.addAttribute(ATTR_ID, id);
		String dataClose = page.getEditorValue(EDITOR_ID_CLOSE_BUTTON);
		if(dataClose.length() > 0 && !CLOSE_LEFT.equals(dataClose)) {
			dialogDiv.addAttribute(ATTR_DATA_CLOSE_BTN, dataClose);
		}
		
//      will work only in webkit 
//		dialogDiv.addAttribute("data-corners", "false");
		
		ElementNode header = dialogDiv.addChild(TAG_DIV);
		header.addAttribute(ATTR_DATA_ROLE, ROLE_HEADER);
		header.addChild("h1", page.getEditorValue(EDITOR_ID_TITLE));
		if(browser) {
			//browser mozilla
			if(!dataClose.equals(CLOSE_NONE)) {
				ElementNode a = header.addChild(TAG_A, "Close");
				a.addAttribute(ATTR_ID, "#");
				if(dataClose.equals(CLOSE_RIGHT)) {
					a.addAttribute(ATTR_CLASS, CLASS_BUTTON_RIGHT);
				} else {
					a.addAttribute(ATTR_CLASS, CLASS_BUTTON_LEFT);
				}
				a.addAttribute(ATTR_DATA_ROLE, ROLE_BUTTON);
				a.addAttribute(ATTR_DATA_ICON, "delete");
				a.addAttribute(ATTR_DATA_ICONPOS, ICONPOS_NOTEXT);
				a.addAttribute(ATTR_DATA_MINI, TRUE);
				a.addAttribute(ATTR_DATA_INLINE, TRUE);
			}
		}
		ElementNode content = dialogDiv.addChild(TAG_DIV);
		content.addAttribute(ATTR_DATA_ROLE, ROLE_CONTENT);
		content.addChild(TAG_H6, "Dialog content.");
		content.getChildren().add(SEPARATOR);
		ElementNode a1 = content.addChild(TAG_A, "OK");
		a1.addAttribute(ATTR_HREF, "#" + id);
		a1.addAttribute(ATTR_DATA_ROLE, ROLE_BUTTON);
		if(!browser) {
			a1.addAttribute(ATTR_DATA_REL, DATA_REL_BACK);
		}
	}

	protected void createBodyForBrowser(ElementNode body) {
		String id = getID("dialog-");
		
		String click = "$('#xxx').trigger('click');";
		
		//use for webkit
		//click = "window.setTimeout(function() {$('#xxx').click();},100);";

		{ //browser mozilla - remove close button provided by jQuery.
			if(page.getEditorValue(EDITOR_ID_CLOSE_BUTTON).length() >= 0) {
				String removeA = "vd = document.getElementById('" + id + "');"
			               + "v1 = vd.getElementsByTagName('a')[0];"
			               + "v2 = v1.parentNode;"
			               + "v2.removeChild(v1);";
				click += removeA;
			}
		}
		
		body.addAttribute("onload", click);
		ElementNode page = getPageContentNode(body);
		ElementNode a = page.addChild(TAG_A, "open");
		a.addAttribute(ATTR_ID, "xxx");
		a.addAttribute(ATTR_HREF, "#" + id);
		a.addAttribute(ATTR_DATA_REL, "dialog");

		addContent(body, true);
	}

}

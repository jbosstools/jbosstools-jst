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
public class NewSlideboxWizard extends NewIonicWidgetWizard<NewSlideboxWizardPage> {

	public NewSlideboxWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(AngularJsUIImages.getInstance()
				.getOrCreateImageDescriptor(AngularJsUIImages.SLIDEBOX_IMAGE));
	}

	@Override
	protected NewSlideboxWizardPage createPage() {
		return new NewSlideboxWizardPage();
	}

	@Override
	protected void addContent(ElementNode parent, boolean browser) {
		if(browser) {
			ElementNode c = parent.addChild(TAG_ION_CONTENT);
			parent = c;
		}
		ElementNode slidebox = parent.addChild(TAG_ION_SLIDE_BOX);
		addAttributeIfNotEmpty(slidebox, ATTR_DELEGATE_HANDLE, ATTR_DELEGATE_HANDLE);
		addID("slidebox-", slidebox);

		if(isTrue(ATTR_DOES_CONTINUE)) {
			slidebox.addAttribute(ATTR_DOES_CONTINUE, TRUE);
			addAttributeIfNotEmpty(slidebox, ATTR_SLIDE_INTERVAL, ATTR_SLIDE_INTERVAL);
		}

		addAttributeIfNotEmpty(slidebox, ATTR_AUTO_PLAY, ATTR_AUTO_PLAY);
		if(isTrue(ATTR_SHOW_PAGER)) {
			addAttributeIfNotEmpty(slidebox, ATTR_PAGER_CLICK, ATTR_PAGER_CLICK);
		} else {
			slidebox.addAttribute(ATTR_SHOW_PAGER, FALSE);
		}
		addAttributeIfNotEmpty(slidebox, ATTR_ON_SLIDE_CHANGED, ATTR_ON_SLIDE_CHANGED);
		addAttributeIfNotEmpty(slidebox, ATTR_ACTIVE_SLIDE, ATTR_ACTIVE_SLIDE);
		
		int n = page.slides.getNumber();
		for (int i = 0; i < n; i++) {
			String slideName = page.slides.getTitle(i);

			ElementNode slide = slidebox.addChild(TAG_ION_SLIDE);
			ElementNode div = slide.addChild(TAG_DIV);
			div.addAttribute(ATTR_CLASS, "box");
			div.addChild("h1", slideName);
			if(!browser) {
				div.addComment("Slide " + (i + 1) + " content");
			} else {
				div.addTextChild("Content of slide " + (i + 1));
			}
		}
		
	}
}

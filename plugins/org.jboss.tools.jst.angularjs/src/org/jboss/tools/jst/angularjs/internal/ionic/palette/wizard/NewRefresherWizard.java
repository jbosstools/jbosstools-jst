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
public class NewRefresherWizard extends NewIonicWidgetWizard<NewRefresherWizardPage> implements IonicConstants {

	public NewRefresherWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(AngularJsUIImages.getInstance()
				.getOrCreateImageDescriptor(AngularJsUIImages.REFRESHER_IMAGE));
	}

	@Override
	protected NewRefresherWizardPage createPage() {
		return new NewRefresherWizardPage();
	}

	@Override
	protected void addContent(ElementNode parent, boolean browser) {
		String text = (browser) ? "Content of tab" : null;
		if(browser) {
			ElementNode content = parent.addChild(TAG_ION_CONTENT);
			ElementNode div = content.addChild(TAG_DIV);
			div.addAttribute(ATTR_STYLE, "padding: 20px 20px 20px 20px;");
			div.addChild(TAG_P, "Pull content down to refresh.");
			div.addChild(TAG_P, "Then pull content up to cancel refresh.");
			parent = content;
		}
		ElementNode refresher = parent.addChild(TAG_ION_REFRESHER, text);

		addAttributeIfNotEmpty(refresher, ATTR_ON_REFRESH, ATTR_ON_REFRESH);
		addAttributeIfNotEmpty(refresher, ATTR_ON_PULLING, ATTR_ON_PULLING);
		addAttributeIfNotEmpty(refresher, ATTR_PULLING_ICON, ATTR_PULLING_ICON);
		addAttributeIfNotEmpty(refresher, ATTR_PULLING_TEXT, ATTR_PULLING_TEXT);
		addAttributeIfNotEmpty(refresher, ATTR_REFRESHING_ICON, ATTR_REFRESHING_ICON);
		addAttributeIfNotEmpty(refresher, ATTR_REFRESHING_TEXT, ATTR_REFRESHING_TEXT);
		
//		addID("refresher-", refresher);
	}

}
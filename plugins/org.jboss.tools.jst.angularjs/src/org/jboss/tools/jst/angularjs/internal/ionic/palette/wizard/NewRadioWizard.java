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
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryConstants;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewRadioWizard extends NewIonicWidgetWizard<NewRadioWizardPage> implements IonicConstants {

	public NewRadioWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(AngularJsUIImages.getInstance()
				.getOrCreateImageDescriptor(AngularJsUIImages.RADIO_IMAGE));
	}

	@Override
	protected NewRadioWizardPage createPage() {
		return new NewRadioWizardPage();
	}

	@Override
	protected void addContent(ElementNode parent, boolean browser) {
		String model = page.getEditorValue(ATTR_NG_MODEL);
		String name = page.getEditorValue(JQueryConstants.EDITOR_ID_NAME);
		
		for (int i = 0; i < page.buttons.getNumber(); i++) {
			ElementNode item = parent.addChild(TAG_ION_RADIO, page.buttons.getLabel(i));
			item.addAttribute(ATTR_TYPE, JQueryConstants.TYPE_RADIO);
			if(model.length() > 0) item.addAttribute(ATTR_NG_MODEL, model);
			if(name.length() > 0) item.addAttribute(ATTR_NAME, name);
			if(page.buttons.isNgValue(i)) {
				item.addAttribute(ATTR_NG_VALUE, page.buttons.getValue(i));
			} else {
				item.addAttribute(ATTR_VALUE, page.buttons.getValue(i));
			}
			String ngChange = page.buttons.getNgChangel(i);
			if(ngChange.length() > 0) {
				item.addAttribute(ATTR_NG_CHANGE, ngChange);
			}
		}
	}

}

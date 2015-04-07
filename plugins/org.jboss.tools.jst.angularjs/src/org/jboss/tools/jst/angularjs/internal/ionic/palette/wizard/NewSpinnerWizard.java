/******************************************************************************* 
 * Copyright (c) 2015 Red Hat, Inc. 
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
public class NewSpinnerWizard extends NewIonicWidgetWizard<NewSpinnerWizardPage> implements IonicConstants {

	public NewSpinnerWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(AngularJsUIImages.getInstance()
				.getOrCreateImageDescriptor(AngularJsUIImages.SPINNER_IMAGE));
	}

	@Override
	protected NewSpinnerWizardPage createPage() {
		return new NewSpinnerWizardPage();
	}

	@Override
	protected void addContent(ElementNode parent, boolean browser) {
		ElementNode spinner = parent.addChild(TAG_ION_SPINNER, "");
		String icon = page.getEditorValue(IonicConstants.ATTR_ICON + "-default");
		if(!"default".equals(icon)) {
			spinner.addAttribute(IonicConstants.ATTR_ICON, icon);
		}
		addAttributeIfNotEmpty(spinner, ATTR_CLASS, TAG_ION_SPINNER);
	}

}

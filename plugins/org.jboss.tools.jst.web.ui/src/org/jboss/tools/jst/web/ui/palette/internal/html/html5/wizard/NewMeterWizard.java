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
package org.jboss.tools.jst.web.ui.palette.internal.html.html5.wizard;

import org.eclipse.swt.SWT;
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
public class NewMeterWizard extends NewHTMLWidgetWizard<NewMeterWizardPage> implements HTMLConstants {
	static String prefix = "meter-";

	public NewMeterWizard() {
		setWindowTitle(DropWizardMessages.Wizard_Window_Title);
		setDefaultPageImageDescriptor(JSTWebUIImages.getInstance()
				.getOrCreateImageDescriptor(JSTWebUIImages.METER_IMAGE));
	}

	@Override
	public NewMeterWizardPage createPage() {
		return new NewMeterWizardPage();
	}

	@Override
	protected void addContent(ElementNode parent) {
		ElementNode meter = parent.addChild(TAG_METER, "");
		meter.addAttribute(ATTR_VALUE, page.getEditorValue(ATTR_VALUE));
		addAttributeIfNotEmpty(meter, ATTR_MIN, ATTR_MIN);
		addAttributeIfNotEmpty(meter, ATTR_MAX, ATTR_MAX);
		addAttributeIfNotEmpty(meter, ATTR_LOW, ATTR_LOW);
		addAttributeIfNotEmpty(meter, ATTR_HIGH, ATTR_HIGH);
		addAttributeIfNotEmpty(meter, ATTR_OPTIMUM, ATTR_OPTIMUM);
		addAttributeIfNotEmpty(meter, ATTR_FORM, ATTR_FORM);
		addID(prefix, meter);

	}

}

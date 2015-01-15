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

import java.beans.PropertyChangeEvent;

import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.common.model.ui.editors.dnd.ValidationException;
import org.jboss.tools.jst.web.ui.internal.properties.advanced.LayoutUtil.TwoColumns;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryFieldEditorFactory;
import org.jboss.tools.jst.web.ui.palette.html.wizard.NewHTMLWidgetWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewMeterWizardPage extends NewHTMLWidgetWizardPage {

	public NewMeterWizardPage() {
		super("newMeter", WizardMessages.newMeterWizardTitle);
		setDescription(WizardMessages.newMenuitemWizardDescription);
	}

	@Override
	protected void createFieldPanel(Composite parent) {
		TwoColumns columns = createTwoColumns(parent);
		addEditor(HTMLFieldEditorFactory.createMeterValueEditor(), columns.left());
		addEditor(HTMLFieldEditorFactory.createMeterOptimumEditor(), columns.right());
		addEditor(HTMLFieldEditorFactory.createMeterMinEditor(), columns.left());
		addEditor(HTMLFieldEditorFactory.createMeterMaxEditor(), columns.right());
		addEditor(HTMLFieldEditorFactory.createMeterLowEditor(), columns.left());
		addEditor(HTMLFieldEditorFactory.createMeterHighEditor(), columns.right());
		createIDEditor(parent, true);
		addEditor(JQueryFieldEditorFactory.createFormReferenceEditor(), parent);

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		super.propertyChange(evt);
	}

	static Double ZERO = Double.valueOf(0);
	static Double ONE = Double.valueOf(1);

	@Override
	public void validate() throws ValidationException {
		Double value = readNumber(ATTR_VALUE);
		if(value == null) {
			throw new ValidationException(WizardMessages.errorMeterValueRequired);
		}
		String[] attrsOrder = {ATTR_MIN, ATTR_LOW, ATTR_OPTIMUM, ATTR_HIGH, ATTR_MAX};
		Double[] defaults = {ZERO, null, null, null, ONE}; 
		for (int i = 0; i < attrsOrder.length; i++) {
			for (int j = i + 1; j < attrsOrder.length; j++) {
				assertOrder(attrsOrder[i], defaults[i], attrsOrder[j], defaults[j]);
			}
		}
		assertOrder(ATTR_MIN, ZERO, ATTR_VALUE, null);
		assertOrder(ATTR_VALUE, null, ATTR_MAX, ONE);
		super.validate();
	}

	Double readNumber(String editorID) throws ValidationException {
		String value = getEditorValue(editorID).trim();
		if(value.length() == 0) {
			return null;
		}
		try {
			return Double.parseDouble(value);
		} catch (NumberFormatException e) {
			throw new ValidationException(NLS.bind(WizardMessages.errorValueNotNumber, editorID));
		}
	}

	void assertOrder(String lower, Double defaultLower, String upper, Double defaultUpper) throws ValidationException {
		Double lowerValue = readNumber(lower);
		if(lowerValue == null) lowerValue = defaultLower;
		Double upperValue = readNumber(upper);
		if(upperValue == null) upperValue = defaultUpper;
		if(lowerValue == null || upperValue == null) {
			return;
		}
		if(lowerValue.compareTo(upperValue) > 0) {
			throw new ValidationException(NLS.bind(WizardMessages.errorShouldBeLessThan, lower, upper), true);
		}
	}

	@Override
	protected int getPreferredBrowser() {
		return SWT.WEBKIT;
	}
}

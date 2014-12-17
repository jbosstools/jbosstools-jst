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
package org.jboss.tools.jst.web.ui.palette.internal.html.html5.wizard;

import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.common.ui.widget.editor.SwtFieldEditorFactory;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.WizardDescriptions;
import org.jboss.tools.jst.web.ui.palette.html.wizard.HTMLConstants;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class HTMLFieldEditorFactory implements HTMLConstants {

	/**
	 * Used in New Context context.
	 * @return checkbox field editor unselected by default
	 */
	public static IFieldEditor createAddScriptTemplateEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_ADD_SCRIPT_TEMPLATE, 
				WizardMessages.addScriptTemplate, false,
				WizardDescriptions.canvasAddScriptTemplate);
	}

	/**
	 * Used in New Datalist wizard.
	 * @return text efield editor for option label
	 */
	public static IFieldEditor createLabelEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_LABEL, WizardMessages.labelLabel, "",
				WizardDescriptions.optionLabel);
	}

	/**
	 * Used in New Datalist wizard.
	 * @return text efield editor for option value
	 */
	public static IFieldEditor createValueEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_VALUE, WizardMessages.valueLabel, "",
				WizardDescriptions.optionValue);
	}

	/**
	 * Used in New Datalist wizard.
	 * @return checkbox field editor unselected by default
	 */
	public static IFieldEditor createAddInputEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(TAG_INPUT, 
				WizardMessages.addInput, false,
				WizardDescriptions.datalistAddInput);
	}

}

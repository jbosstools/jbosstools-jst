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
package org.jboss.tools.jst.angularjs.internal.palette.wizard;

import java.util.Arrays;
import java.util.List;

import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.common.ui.widget.editor.SwtFieldEditorFactory;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.WizardDescriptions;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class IonicFieldEditorFactory implements IonicConstants {

	public static IFieldEditor createIconEditor(String editorID) {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(editorID, WizardMessages.iconLabel, IonicIconFactory.getInstance().getIcons(), "", true,
				WizardDescriptions.buttonIcon);
	}

	/**
	 * Used in New Header wizard wizard.
	 * @return
	 */
	public static IFieldEditor createSubheaderEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(CLASS_BAR_SUBHEADER, IonicWizardMessages.subheaderLabel, false,
				"");
	}

	/**
	 * Used in New Header wizard wizard.
	 * @return
	 */
	public static IFieldEditor createNoTapScrollEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(ATTR_NO_TAP_SCROLL, IonicWizardMessages.noTapScrollLabel, false,
				"");
	}

	static String[] BAR_COLORS = {
		"", "bar-light", "bar-stable", "bar-positive", "bar-calm", "bar-balanced", "bar-energized", "bar-assertive", "bar-royal", "bar-dark",  
	};

	static List<String> BAR_COLOR_LIST = Arrays.asList(BAR_COLORS);

	/**
	 * Used in New Header wizard wizard.
	 * @return
	 */
	public static IFieldEditor createBarColorEditor(String editorID) {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(editorID, IonicWizardMessages.barColorLabel, BAR_COLOR_LIST, "", true,
				"");
	}

	static List<String> ALIGN_TITLE_LIST = Arrays.asList(new String[]{"", "left", "center", "right"});

	/**
	 * Used in New Header wizard wizard.
	 * @return
	 */
	public static IFieldEditor createAlignTitleEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(ATTR_ALIGN_TITLE, IonicWizardMessages.alignTitleLabel, ALIGN_TITLE_LIST, "", true,
				"");
	}

	/**
	 * Used in New Dialog wizard, New Header wizard, New Footer wizard.
	 * @return
	 */
	public static IFieldEditor createNgClickEditor(String editorID) {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(editorID, IonicWizardMessages.ngClickLabel, "",
				"");
	}
}
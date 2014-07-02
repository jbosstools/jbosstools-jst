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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.jboss.tools.common.ui.widget.editor.CheckBoxFieldEditor;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.angularjs.AngularJsPlugin;
import org.jboss.tools.jst.web.ui.palette.html.wizard.AbstractNewHTMLWidgetWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.wizard.VersionedNewHTMLWidgetWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewIonicWidgetWizardPage extends VersionedNewHTMLWidgetWizardPage implements IonicConstants {

	public NewIonicWidgetWizardPage(String pageName, String title) {
		super(pageName, title);
	}

	@Override
    public NewIonicWidgetWizard<?> getWizard() {
        return (NewIonicWidgetWizard<?>)super.getWizard();
    }

	protected IonicVersion getVersion() {
		return getWizard().getVersion();
	}

	@Override
	public boolean canFlipToNextPage() {
		if(!isTrue(AbstractNewHTMLWidgetWizardPage.ADD_JS_CSS_SETTING_NAME)) {
			return false;
		}

		return super.canFlipToNextPage();
	}

	@Override
	protected IFieldEditor createAddLibsEditor(Composite parent) {
		boolean addJSCSS = false; 
		IDialogSettings settings = AngularJsPlugin.getDefault().getDialogSettings();
		IDialogSettings insertTagSettings = settings.getSection(SECTION_NAME);
		if(insertTagSettings != null) {
			addJSCSS = insertTagSettings.getBoolean(ADD_JS_CSS_SETTING_NAME);
		} else {
			insertTagSettings = DialogSettings.getOrCreateSection(settings, SECTION_NAME);
			insertTagSettings.put(ADD_JS_CSS_SETTING_NAME, true);
		}
		if(getWizard().getPreferredVersions().areAllLibsDisabled()) {
			addJSCSS = false;
		}
		final IFieldEditor addLibs = new CheckBoxFieldEditor(ADD_JS_CSS_SETTING_NAME, WizardMessages.addReferencesToJSCSSLabel, Boolean.valueOf(addJSCSS)){
			@Override
			public void doFillIntoGrid(Object parent) {
				Composite c = (Composite) parent;
				final Control[] controls = (Control[]) getEditorControls(c);
				Button button = (Button)controls[0];
				button.setText(WizardMessages.addReferencesToJSCSSLabel);
				button.setToolTipText(WizardMessages.addReferencesToJSCSSTooltip);
				GridData d = new GridData(GridData.FILL_HORIZONTAL);
				d.horizontalSpan = 1;
				d.minimumWidth = 200;
				button.setLayoutData(d);
			}
		};
		addLibs.doFillIntoGrid(parent);
		addEditor(addLibs);
		if(getWizard().getPreferredVersions().areAllLibsDisabled()) {
			addLibs.setEnabled(false);
		}
		addLibs.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				IDialogSettings settings = AngularJsPlugin.getDefault().getDialogSettings();
				IDialogSettings insertTagSettings = settings.getSection(SECTION_NAME);
				insertTagSettings.put(ADD_JS_CSS_SETTING_NAME, Boolean.parseBoolean(addLibs.getValue().toString()));
			}
		});
		addLibs.addPropertyChangeListener(this);

		return addLibs;
	}
}
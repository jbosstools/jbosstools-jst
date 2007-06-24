/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.web.debug.ui.internal.views.properties;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.jboss.tools.common.model.ui.attribute.editor.CheckTreeEditor;
import org.jboss.tools.common.model.ui.attribute.editor.IFieldEditor;
import org.jboss.tools.common.model.ui.widgets.IWidgetSettings;
import org.jboss.tools.common.model.ui.widgets.WhiteSettings;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;

/**
 * @author au
 */

public class WebDataPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	private IWidgetSettings settings = new WhiteSettings();
	private WebDataTreeAdapter adapter;
	private CheckTreeEditor editor;
	private ButtonController buttonController;
	private static final String LABEL = WebUIMessages.EDIT_FILTER;
	
	public class ButtonController {
		Button button;
		boolean enabled = Boolean.FALSE.booleanValue();
		SelectionListener listener;
		
		public Control createButton(Composite composite) {
			button = new Button(composite, SWT.PUSH);
			button.setText(LABEL);
			button.setEnabled(enabled);
			if (listener!=null)	button.addSelectionListener(listener);
			return button;
		}
		/**
		 * @return Returns the enabled.
		 */
		public boolean isEnabled() {
			return enabled;
		}
		/**
		 * @param enabled The enabled to set.
		 */
		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
			if (button != null) button.setEnabled(enabled);
		}
		/**
		 * @param listener The listener to set.
		 */
		public void setListener(SelectionListener listener) {
			if (button!=null && this.listener!=null) button.removeSelectionListener(this.listener);
			this.listener = listener;
			if (button!=null) button.addSelectionListener(listener);
		}
		
		public void dispose() {
			if (button!=null) {
				button.removeSelectionListener(this.listener);
				listener = null;
				if (!button.isDisposed()) button.dispose();
				button = null;
			}
		}
	}

	public WebDataPreferencePage() {
		buttonController = new ButtonController();
		adapter = new WebDataTreeAdapter();
		adapter.setAutoStore(Boolean.FALSE.booleanValue());
		adapter.setButtonController(buttonController);
		editor = new CheckTreeEditor(settings);  
		editor.setLabelText(""); //$NON-NLS-1$
		editor.setInput(adapter);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performApply()
	 */
	protected void performApply() {
		adapter.store();
		super.performApply();
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
	 */
	protected void performDefaults() {
		adapter.loadDefaults();
		if (adapter.viewer != null) {
        	adapter.viewer.refresh();
        }

		super.performDefaults();
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.IPreferencePage#performOk()
	 */
	public boolean performOk() {
		adapter.store();
		return super.performOk();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents(Composite parent) {

		Composite treeButtonComposite = new Composite(parent, SWT.NONE);
		treeButtonComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout(2, Boolean.FALSE.booleanValue());
		layout.horizontalSpacing = 10;
		layout.verticalSpacing = 10;
		layout.marginHeight = 5;
		layout.marginWidth = 0;
		treeButtonComposite.setLayout(layout);
		
		Control[] control;
		GridData gd;
		try {
			control = ((IFieldEditor)editor.getFieldEditor(treeButtonComposite)).getControls(treeButtonComposite);

			control[0].dispose(); // cannot show label

			gd = new GridData(GridData.FILL_BOTH);
			control[1].setLayoutData(gd);
		} catch (Exception e) {
			//ignore
		}
		
		Control button = buttonController.createButton(treeButtonComposite);
		gd = new GridData();
		gd.verticalAlignment = SWT.TOP;
		button.setLayoutData(gd);
		
		return treeButtonComposite;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#dispose()
	 */
	public void dispose() {
		super.dispose();
		if (editor!=null) editor.dispose();
		editor = null;
		if (adapter!=null) adapter.dispose();
		adapter = null;
		if (buttonController!=null) buttonController.dispose();
		buttonController = null;
	}
}

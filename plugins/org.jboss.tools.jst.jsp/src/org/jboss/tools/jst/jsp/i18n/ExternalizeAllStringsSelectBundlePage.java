/*******************************************************************************
 * Copyright (c) 2007-2011 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.jsp.i18n;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.jboss.tools.common.model.ui.ModelUIImages;
import org.jboss.tools.jst.jsp.messages.JstUIMessages;

public class ExternalizeAllStringsSelectBundlePage extends WizardPage {

	IFile editorFile;
	File bundleFile;
	private FileFieldEditor fileFieldEditor;
	
	protected ExternalizeAllStringsSelectBundlePage(String pageName) {
		super(pageName,
				JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_TITLE, 
				ModelUIImages.getImageDescriptor(ModelUIImages.WIZARD_DEFAULT));
		setDescription(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_DESCRIPTION);
		setPageComplete(false);
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));

		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		composite.setLayoutData(gd);
		fileFieldEditor = new FileFieldEditor("storageKey", "Select bundle file:", composite);
		fileFieldEditor.setFileExtensions(new String[] {"*.properties"});
		fileFieldEditor.setEmptyStringAllowed(false);
		
		if (getWizard() instanceof ExternalizeAllStringsWizard) {
			ExternalizeAllStringsWizard wiz = (ExternalizeAllStringsWizard) getWizard();
			IEditorInput in = wiz.getEditor().getEditorInput();
			if (in instanceof IFileEditorInput) {
				IFileEditorInput fin = (IFileEditorInput) in;
				editorFile = fin.getFile();
			}
			if (null != editorFile) {
				fileFieldEditor.setFilterPath(editorFile.getProject().getLocation().toFile());
			}
		}
		final Table table = ExternalizeStringsUtils.createPropertiesTable(composite, SWT.BORDER);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,true, 3, 1));
		fileFieldEditor.setPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if (FileFieldEditor.VALUE.equalsIgnoreCase(event.getProperty())) {
					bundleFile = new File((String)event.getNewValue());
					ExternalizeStringsUtils.populatePropertiesTable(table, bundleFile);
					/*
					 * Set page complete
					 */
					setPageComplete(isPageComplete());
				}
			}
		});
		/*
		 * Wizard Page control should be initialized.
		 */
		setControl(composite);
	}

	@Override
	public boolean isPageComplete() {
		return fileFieldEditor.isValid();
	}
	
	public File getBundleFile() {
		return bundleFile;
	}
}

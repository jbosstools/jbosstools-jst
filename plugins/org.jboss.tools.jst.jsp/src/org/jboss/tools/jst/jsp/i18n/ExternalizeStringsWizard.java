/*******************************************************************************
 * Copyright (c) 2007-2010 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.jsp.i18n;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jboss.tools.common.model.ui.ModelUIImages;
import org.jboss.tools.jst.jsp.bundle.BundleMap;
import org.jboss.tools.jst.jsp.messages.JstUIMessages;

public class ExternalizeStringsWizard extends Wizard {
	
	public String ExternalizeStringsWizardPageName = "ExternalizeStringsWizardPage"; //$NON-NLS-1$
	public String NewFileCreationPageName = "NewFileCreationPage"; //$NON-NLS-1$
	
	StructuredTextEditor editor = null;
	BundleMap bm = null;
	ExternalizeStringsWizardPage page1 = null;
	WizardNewFileCreationPage page2 = null;
	
	public ExternalizeStringsWizard(StructuredTextEditor editor, BundleMap bm) {
		super();
		setHelpAvailable(false);
		setWindowTitle(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_TITLE);
		this.editor = editor; 
		this.bm = bm;
	}
	
	@Override
	public void addPages() {
		super.addPages();
		page1 = new ExternalizeStringsWizardPage(
				ExternalizeStringsWizardPageName, editor, bm);
		page2 = new WizardNewFileCreationPage(NewFileCreationPageName,
				(IStructuredSelection) editor.getSelectionProvider().getSelection()) {
			protected InputStream getInitialContents() {
				return new ByteArrayInputStream(page1.getKeyValuePair().getBytes());
			}
		};
		page2.setTitle(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_TITLE);
		page2.setDescription(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_DESCRIPTION);
		page2.setImageDescriptor(ModelUIImages.getImageDescriptor(ModelUIImages.WIZARD_DEFAULT));
		addPage(page1);
		addPage(page2);
	}

	@Override
	public boolean canFinish() {
		return (!page1.isNewFile() && page1.isPageComplete())
				|| (page1.isNewFile() && page2.isPageComplete());
	}

	@Override
	public boolean performFinish() {
		IFile bundleFile = null;
		if (page1.isNewFile()) {
			bundleFile = page2.createNewFile();
		} else {
			bundleFile = page1.getBundleFile();
		}
		/*
		 * Exit when the file is null
		 */
		if (bundleFile == null) {
			return false;
		}
		/*
		 * Add "key=value" to the bundle if file is not new,
		 * If it is new, it got input by getInitialContent()
		 */
		if (bundleFile.exists() && !page1.isNewFile()) {
			InputStream is = new ByteArrayInputStream(page1.getKeyValuePair().getBytes());
			try {
				bundleFile.appendContents(is, false, true, null);
				is.close();
				is = null;
			} catch (CoreException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		/*
		 * Replace text in the editor
		 */
		page1.replaceText();
		
		return true;
	}

}

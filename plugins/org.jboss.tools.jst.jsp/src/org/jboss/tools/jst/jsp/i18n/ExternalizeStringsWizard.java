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
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.texteditor.ITextEditor;
import org.jboss.tools.common.model.ui.ModelUIImages;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.bundle.BundleMap;
import org.jboss.tools.jst.jsp.messages.JstUIMessages;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Constants;

public class ExternalizeStringsWizard extends Wizard {
	
	public String ExternalizeStringsWizardPageName = "ExternalizeStringsWizardPage"; //$NON-NLS-1$
	public String NewFileCreationPageName = "NewFileCreationPage"; //$NON-NLS-1$
	
	ITextEditor editor = null;
	BundleMap bm = null;
	ExternalizeStringsWizardPage page1 = null;
	WizardNewFileCreationPage page2 = null;
	
	public ExternalizeStringsWizard(ITextEditor editor, BundleMap bm) {
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
		/*
		 * https://jira.jboss.org/browse/JBIDE-7247
		 * Set initial values for the new properties file
		 */
		if (editor.getEditorInput() instanceof IStorageEditorInput) {
			try {
				IPath fullPath = ((IStorageEditorInput) editor.getEditorInput()).getStorage().getFullPath();
				page2.setContainerFullPath(fullPath);
			} catch (CoreException e) {
				JspEditorPlugin.getDefault().logError(e);
			}
			
		}
		String fileName = editor.getEditorInput().getName();
		int pos = fileName.lastIndexOf(Constants.DOT);
		if (pos != -1) {
			fileName = fileName.substring(0, pos) + Constants.PROPERTIES_EXTENTION;
		}
		page2.setFileName(fileName);
		/*
		 * The new file should not exist
		 */
		page2.setAllowExistingResources(false);
		/*
		 * Add all the pages to the wizard
		 */
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
		 * Add "key=value" to the bundle file that is already exists. 
		 * When the file is new key and value will be written to the file content
		 * via getInitialContent() method of the page2 during the file creation. 
		 */
		if (bundleFile.exists() && !page1.isNewFile()) {
			/*
			 * https://jira.jboss.org/browse/JBIDE-7218
			 * Add only one line before adding the value. 
			 */
			String writeToFile = "\n" + page1.getKeyValuePair(); //$NON-NLS-1$
			InputStream is = new ByteArrayInputStream(writeToFile.getBytes());
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

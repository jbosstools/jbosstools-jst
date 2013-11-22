/*******************************************************************************
 * Copyright (c) 2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.internal.text.ext.hyperlink.internal;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.internal.wizards.NewWizardRegistry;
import org.eclipse.ui.wizards.IWizardDescriptor;
import org.jboss.tools.common.text.ext.ExtensionsPlugin;
import org.jboss.tools.common.text.ext.hyperlink.AbstractHyperlink;
import org.jboss.tools.common.ui.dialog.CheckboxMessageDialog;
import org.jboss.tools.jst.web.kb.preferences.CreateNewFilePreferences;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;

public class CreateNewFileHyperlink extends AbstractHyperlink{
	public static final String NEW_CSS_WIZARD_ID = "org.eclipse.wst.css.ui.internal.wizard.NewCSSWizard"; //$NON-NLS-1$
	public static final String NEW_JS_WIZARD_ID = "org.eclipse.wst.jsdt.ui.NewJSWizard"; //$NON-NLS-1$
	public static final String NEW_JSP_WIZARD_ID = "org.eclipse.jst.jsp.ui.internal.wizard.NewJSPWizard"; //$NON-NLS-1$
	public static final String NEW_XHTML_WIZARD_ID = "org.jboss.tools.jst.web.ui.wizards.newfile.NewXHTMLWizard"; //$NON-NLS-1$
	public static final String NEW_HTML_WIZARD_ID = "org.eclipse.wst.html.ui.internal.wizard.NewHTMLWizard"; //$NON-NLS-1$
	
	public static final int WIZARD_NOT_FOUND = -123;
	
	private IFile file;
	private String fileName;

	public CreateNewFileHyperlink(IDocument document, IRegion region, String fileName, IFile file) {
		this.fileName = fileName;
		this.file = file;
		setRegion(region);
		setDocument(document);
	}
	
	public IFile getFile(){
		return file;
	}

	@Override
	public String getHyperlinkText() {
		return NLS.bind(WebUIMessages.FileDoesNotExistClickToCreate, fileName);
	}
	
	@Override
	protected void doHyperlink(IRegion region) {
		doHyperlinkInternal(false);
	}
	
	public void test(){
		doHyperlinkInternal(true);
	}
	
	public static String getWizardID(String fileExtension){
		if("css".equals(fileExtension)){ //$NON-NLS-1$
			return NEW_CSS_WIZARD_ID;
		}else if("js".equals(fileExtension)){ //$NON-NLS-1$
			return NEW_JS_WIZARD_ID;
		}else if("jsp".equals(fileExtension)){ //$NON-NLS-1$
			return NEW_JSP_WIZARD_ID;
		}else if("xhtml".equals(fileExtension)){ //$NON-NLS-1$
			return NEW_XHTML_WIZARD_ID;
		}else if("html".equals(fileExtension) || "htm".equals(fileExtension)){ //$NON-NLS-1$ //$NON-NLS-2$
			return NEW_HTML_WIZARD_ID;
		}
		return null;
	}

	protected void doHyperlinkInternal(boolean test) {
		String wizardId = getWizardID(file.getFileExtension().toLowerCase());
		if(wizardId != null){
			NewWizardRegistry registry = NewWizardRegistry.getInstance();
			IWizardDescriptor descriptor = registry.findWizard(wizardId);
			if(descriptor == null){
				ExtensionsPlugin.getDefault().logError(NLS.bind(WebUIMessages.WizardNotFound, wizardId));
				return;
			}
			MessageDialog mDialog = new CheckboxMessageDialog(Display.getCurrent().getActiveShell(), WebUIMessages.Question,
					NLS.bind(WebUIMessages.FileDoesNotExistDoYouWantToCreate, fileName),
					NLS.bind(WebUIMessages.UseWizard, descriptor.getLabel()), CreateNewFilePreferences.isWizardUsed());
			boolean confirm = false;
			boolean runWizard = false;
			if(test){
				confirm = true;
				runWizard = true;
			}else{
				int result = mDialog.open();
				if ((result == CheckboxMessageDialog.CHECKBOX_SELECTED) || (result == MessageDialog.OK)) {
					confirm = true;
				}
				runWizard = ((result & CheckboxMessageDialog.CHECKBOX_SELECTED) > 0);
			}
			if(!file.exists() && confirm){
				CreateNewFilePreferences.setWizardUsed(runWizard);
				
				// create folders if needed
				createFolders();
				
				if(runWizard){
					int returnCode = runWizard(descriptor, test);
					if(returnCode != WizardDialog.OK){
						// user pressed Cancel, so we delete folders which we created before
						deleteFolders();
					}
				}else{
					// create file without wizard
					createFile();
					if(!test){
						openFileInEditor(file);
					}
				}
			}
		}else{
			if(!file.exists() && (test || MessageDialog.openQuestion(Display.getCurrent().getActiveShell(), WebUIMessages.Question, NLS.bind(WebUIMessages.FileDoesNotExistDoYouWantToCreate, fileName)))){
				// create folders if needed
				createFolders();
				
				createFile();
			}
		}
	}
	
	private ArrayList<IFolder> folders = null;
	
	private void createFolders(){
		folders = new ArrayList<IFolder>();
		IContainer folder = file.getParent();
		while(folder instanceof IFolder && !folder.exists()){
			folders.add((IFolder)folder);
			folder = folder.getParent();
		}
		for(int index = folders.size()-1; index >= 0; index--){
			try {
				folders.get(index).create(true, true, new NullProgressMonitor());
			} catch (CoreException e) {
				ExtensionsPlugin.getDefault().logError(e);
			}
		}
	}
	
	private void deleteFolders(){
		if(folders.size() > 0){
		IFolder folder = folders.get(folders.size()-1);
			try {
				folder.delete(true, new NullProgressMonitor());
			} catch (CoreException e) {
				ExtensionsPlugin.getDefault().logError(e);
			}
		}
	}
	
	private void createFile(){
		try {
			ByteArrayInputStream stream = new ByteArrayInputStream(new byte[]{});
			file.create(stream, true, new NullProgressMonitor());
		} catch (CoreException e) {
			ExtensionsPlugin.getDefault().logError(e);
		}
	}
	
	private int runWizard(IWizardDescriptor descriptor, boolean test){
		try {
			IWorkbenchWizard wizard = descriptor.createWizard();
			
			StructuredSelection selection = new StructuredSelection(file);
			wizard.init(Workbench.getInstance(), selection);
			WizardDialog wDialog = new WizardDialog(Display.getCurrent().getActiveShell(), wizard);
			
			wDialog.setBlockOnOpen(false);
			wDialog.open();
			
			IWizardPage[] pages = wizard.getPages();
			for(IWizardPage page : pages){
				if(page instanceof WizardNewFileCreationPage){
					((WizardNewFileCreationPage)page).setFileName(file.getName());
				}
			}
			
			if(test){
				wizard.performFinish();
				return Window.OK;
			}
			
			wDialog.setBlockOnOpen(true);
			return wDialog.open();
		} catch (CoreException e) {
			ExtensionsPlugin.getDefault().logError(e);
		}
		return -1;
	}
}

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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.texteditor.ITextEditor;
import org.jboss.tools.common.EclipseUtil;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.bundle.BundleMap;
import org.jboss.tools.jst.jsp.bundle.BundleMap.BundleEntry;
import org.jboss.tools.jst.jsp.messages.JstUIMessages;
import org.jboss.tools.jst.jsp.util.Constants;

public class ExternalizeAllStringsWizard extends Wizard {
	
	private ITextEditor editor = null;
	private BundleMap bm = null;
	ExternalizeAllStringsSelectBundlePage page1;
	ExternalizeAllStringsKeysListPage page2;
	ExternalizeStringsWizardRegisterBundlePage page3;
	
	public ExternalizeAllStringsWizard(ITextEditor editor, BundleMap bm) {
		super();
		setHelpAvailable(false);
		setWindowTitle(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_TITLE);
		this.editor = editor; 
		this.bm = bm;
		if (this.bm == null) {
			this.bm = ExternalizeStringsUtils.createBundleMap(editor);
		}
	}

	@Override
	public void addPages() {
		super.addPages();
		page1 = new ExternalizeAllStringsSelectBundlePage("ExternalizeAllStringsSelectBundlePage"); //$NON-NLS-1$
		page2 = new ExternalizeAllStringsKeysListPage("ExternalizeAllStringsKeysListPage"); //$NON-NLS-1$
		page3 = new ExternalizeStringsWizardRegisterBundlePage("ExternalizeStringsWizardRegisterBundlePage"); //$NON-NLS-1$
		/*
		 * Add all the pages to the wizard
		 */
		addPage(page1);
		addPage(page2);
		addPage(page3);
	}
	
	protected ITextEditor getEditor() {
		return editor;
	}
	
	protected IDocument getDocument() {
		return editor.getDocumentProvider().getDocument(editor.getEditorInput());
	}
	
	@Override
	public boolean performFinish() {
		File bundleFile = page1.getBundleFile();
		/*
		 * Exit when the file is null
		 */
		if (bundleFile == null) {
			JspEditorPlugin.getDefault().logError(
					"Cannot externalize all string, because bundle file is not found"); //$NON-NLS-1$
			return false;
		}
		if (bundleFile.exists()) {
			PrintWriter out = null;
			try {
				out = new PrintWriter(new BufferedWriter(new FileWriter(bundleFile, true)));
				out.println();
				out.println(page2.getKeyValuePairsList());
			} catch (IOException e) {
				JspEditorPlugin.getDefault().logError(e);
			} 
			if (out.checkError()) {
				JspEditorPlugin.getDefault().logError(
						"Error while writing to the properties file"); //$NON-NLS-1$
			}
			out.close();
			out = null;
		}
		String bundleName = page3.getBundleName();
		String bundlePrefix = findBundlePrefix();
		/*
		 * When no registered bundles found for entered properties file
		 * the wizard will generate its own bundler prefix and path.  
		 */
		if (Constants.EMPTY.equalsIgnoreCase(bundlePrefix)) {
			/*
			 * In such a case there was no matching bundle prefix in the bundle map
			 * and user has entered its own bundle prefix.
			 * Thus this new name should be registered on the page depending on the
			 * choice where to register the bundle.
			 * Otherwise already found prefix is used without any additional registration.
			 */
			String bundlePath;
			String var;
			/*
			 * Get the source folders for the project
			 */
			String userDefinedPath = bundleFile.getAbsolutePath().replaceAll("\\\\", "/"); //$NON-NLS-1$ //$NON-NLS-2$
			String srcPath = userDefinedPath;
			IResource[] src = EclipseUtil.getJavaSourceRoots(ExternalizeStringsUtils.getProject(editor));
			if (src != null) {
				if (src.length > 1) {
					for (IResource res : src) {
						srcPath =  res.getFullPath().toString();
						if (userDefinedPath.indexOf(srcPath) > -1) {
							break;
						}
					}
				} else if (src.length == 1) {
					srcPath = src[0].getFullPath().toString();
				}
			}
			/*
			 * After the source folder has been found --
			 * generate the bundle path.
			 */
			int srcPathIndex = userDefinedPath.indexOf(srcPath);
			if (srcPathIndex > -1) {
				/*
				 * User has selected the folder in the projects's source path.
				 * Replace the beginning
				 */
				bundlePath = userDefinedPath.substring(srcPathIndex + srcPath.length() + 1).replaceAll("/", "\\."); //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				/*
				 * User defined path is different from the source folder.
				 * Thus base-name would be incorrect.
				 */
				bundlePath = userDefinedPath;
			}
			int pos = bundlePath.indexOf(Constants.PROPERTIES_EXTENTION);
			if (pos > -1) {
				bundlePath = bundlePath.substring(0, pos);
			}
			
			if (!Constants.EMPTY.equalsIgnoreCase(bundleName)) {
				/*
				 * Get the name from the last page
				 */
				var = bundleName;
			} else {
				/*
				 * var = file name from page1
				 */
				var = bundlePath.substring(bundlePath.lastIndexOf("/") + 1); //$NON-NLS-1$
				bundleName = var;
			}
			if (page3.isInFacesConfig()) {
				ExternalizeStringsUtils.registerInFacesConfig(editor, bundlePath, var);
			} else if (page3.isViaLoadBundle()) {
				ExternalizeStringsUtils.registerViaLoadBundle(editor, bundlePath, var);
			}
		}
		page2.replaceAllStrings(bundleName);
		return true;
	}

	public String findBundlePrefix() {
		String bundlePrefix = Constants.EMPTY;
		if (page1.getBundleFile() != null) {
			String ap = getUserDefinedFile().getAbsolutePath();
			for (BundleEntry be : bm.getBundles()) {
				IFile bf = bm.getBundleFile(be.uri);
				if (ap.equalsIgnoreCase(bf.getLocation().toOSString())) {
					bundlePrefix = be.prefix;
					break;
				}
			}
		}
		return bundlePrefix;
	}
	
	public Properties getOriginalProperties() {
		if (null != page1) {
			return page1.getOriginalProperties();
		}
		return null;
	}
	public void setUpdatedProperties(Properties p) {
		page2.updateTable(p);
	}
	public void setPage3BundleName() {
		page3.setBundleName(findBundlePrefix());
	}
	protected IProject getProject() {
		return ExternalizeStringsUtils.getProject(editor);
	}
	protected File getUserDefinedFile() {
		return page1.getBundleFile();
	}
}

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
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.texteditor.ITextEditor;
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
	ExternalizeStringsWizardRegisterBundlePage page3 = null;
	
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
		page1 = new ExternalizeAllStringsSelectBundlePage("page1");
		page2 = new ExternalizeAllStringsKeysListPage("page2");
		page3 = new ExternalizeStringsWizardRegisterBundlePage("page3");
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
			JspEditorPlugin.getDefault().logError("Cannot externalize all string, because bundle file is not found");
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
				JspEditorPlugin.getDefault().logError("Error while writing to the properties file");
			}
			out.close();
			out = null;
			}
		/*
		 * Find bundle prefix
		 */
		String bundlePrefix = Constants.EMPTY;
		String ap = bundleFile.getAbsolutePath();
		for (BundleEntry be : bm.getBundles()) {
			IFile bf = bm.getBundleFile(be.uri);
			if (ap.equalsIgnoreCase(bf.getLocation().toOSString())) {
				bundlePrefix = be.prefix;
				break;
			}
		}
		page2.replaceAllStrings(bundlePrefix);
		return true;
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
}

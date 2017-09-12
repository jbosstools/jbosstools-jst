/******************************************************************************* 
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.reddeer.wst.css.ui.wizard;

import org.eclipse.reddeer.core.reference.ReferencedComposite;
import org.eclipse.reddeer.jface.wizard.WizardPage;
import org.eclipse.reddeer.swt.impl.text.DefaultText;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.swt.impl.tree.DefaultTree;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * Wizard page for creating a HTML File.
 * @author vpakan
 */
public class NewCSSFileWizardPage extends WizardPage {
	public NewCSSFileWizardPage(ReferencedComposite referencedComposite) {
		super(referencedComposite);
	}

	/**
	 * Sets a given file name.
	 * 
	 * @param name Name
	 */
	public void setFileName(String fileName){
		new LabeledText(referencedComposite, "File name:").setText(fileName);
	}
	
	/**
	 * Gets a file name.
	 * 
	 */
	public String getFileName(){
		return new LabeledText(referencedComposite, "File Name:").getText();
	}
	/**
	 * Selects parent folder
	 * @param path
	 */
	public void selectParentFolder (String... path){
		new DefaultTreeItem(new DefaultTree(referencedComposite),path).select();
	}
	/**
	 * Sets parent folder
	 * @param path
	 */
	public void setParentFolder(String path){
		new DefaultText(referencedComposite, 0).setText(path);
	}
	/**
	 * Gets parent folder
	 * @return
	 */
	public String getParentFolder(){
		return new DefaultText(referencedComposite, 0).getText();
	}
}

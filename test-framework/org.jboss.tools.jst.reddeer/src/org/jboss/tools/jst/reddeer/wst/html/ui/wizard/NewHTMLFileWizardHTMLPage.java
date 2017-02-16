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
package org.jboss.tools.jst.reddeer.wst.html.ui.wizard;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * Wizard page for creating a HTML File.
 * @author vpakan
 */
public class NewHTMLFileWizardHTMLPage extends WizardPage {
	/**
	 * Sets a given file name.
	 * 
	 * @param name Name
	 */
	public void setFileName(String fileName){
		new DefaultText(1).setText(fileName);
	}
	
	/**
	 * Gets a file name.
	 * 
	 */
	public String getFileName(){
		return new DefaultText(1).getText();
	}
	/**
	 * Selects parent folder
	 * @param path
	 */
	public void selectParentFolder (String... path){
		new DefaultTreeItem(path).select();
	}
	/**
	 * Sets parent folder
	 * @param path
	 */
	public void setParentFolder(String path){
		new DefaultText(0).setText(path);
	}
	/**
	 * Gets parent folder
	 * @return
	 */
	public String getParentFolder(){
		return new DefaultText(0).getText();
	}
}

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
package org.jboss.tools.jst.reddeer.web.ui;

import org.eclipse.reddeer.core.reference.ReferencedComposite;
import org.eclipse.reddeer.jface.wizard.WizardPage;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;

/**
 * Wizard page for selecting template for new XHTML File.
 * @author vpakan
 */
public class NewXHTMLFileWizardXHTMLTemplatePage extends WizardPage {
	public NewXHTMLFileWizardXHTMLTemplatePage(ReferencedComposite referencedComposite) {
		super(referencedComposite);
	}
	/**
	 * Sets a given template.
	 * 
	 * @param name Name
	 */
	public void setTemplate(String template){
		new DefaultTable(referencedComposite).select(template);
	}	
	/**
	 * Sets Use XHTML template checkbox
	 * @param checked
	 */
	public void setUseXHTMLTemplate (boolean checked){
		new CheckBox(referencedComposite, "Use XHTML Template").toggle(checked);
	}
	/**
	 * Gets Use XHTML template checkbox
	 */
	public boolean getUseXHTMLTemplate (){
		return new CheckBox(referencedComposite, "Use XHTML Template").isChecked();
	}
}

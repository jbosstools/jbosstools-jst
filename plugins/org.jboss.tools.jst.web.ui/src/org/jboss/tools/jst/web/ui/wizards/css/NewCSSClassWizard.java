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
package org.jboss.tools.jst.web.ui.wizards.css;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.jboss.tools.jst.jsp.outline.cssdialog.CSSClassDialog;

public class NewCSSClassWizard extends Wizard implements INewWizard {
   
    private NewCSSClassWizardPage page;
    
    

    /**
     * Constructor for SampleNewWizard.
     */
    public NewCSSClassWizard() {
	super();
    }

    public void init(IWorkbench workbench, IStructuredSelection selection) {

    }

    /**
     * Adding the page to the wizard.
     */

    public void addPages() {
	page = new NewCSSClassWizardPage();
	addPage(page);
    }

    /**
     * This method is called when 'Finish' button is pressed in the wizard. We
     * will create an operation and run it using wizard as execution context.
     */
    public boolean performFinish() {
	page.saveChanges();
	return true;
    }

    private class NewCSSClassWizardPage extends WizardPage {

	private CSSClassDialog dialog;
	
	final static String WIZARD_TITLE = "Create New CSS Class";
	final static String WIZARD_DESCRIPTION = "Create New CSS Class";

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public NewCSSClassWizardPage() {
	    super("newCSSClassWizard");
	    setTitle(WIZARD_TITLE);
	    setDescription(WIZARD_DESCRIPTION);
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
	    Composite container = new Composite(parent, SWT.NULL);
	    GridLayout layout = new GridLayout();
	    container.setLayout(layout);

	    dialog = new CSSClassDialog(getShell(), true);
	    dialog.createDialogComposite(container);
	    setControl(container);
	}
	
	public void saveChanges() {
	    dialog.saveChanges();
	}
    }

}
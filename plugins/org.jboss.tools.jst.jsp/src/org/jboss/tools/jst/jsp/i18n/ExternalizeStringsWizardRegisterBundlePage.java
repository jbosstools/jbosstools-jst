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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.jboss.tools.common.EclipseUtil;
import org.jboss.tools.common.model.ui.ModelUIImages;
import org.jboss.tools.jst.jsp.messages.JstUIMessages;
import org.jboss.tools.jst.jsp.util.Constants;

/**
 * The Class ExternalizeStringsWizardRegisterBundlePage.
 */
public class ExternalizeStringsWizardRegisterBundlePage extends WizardPage
		implements SelectionListener {
	
	public static final String PAGE_NAME = "ExternalizeStringsWizardRegisterBundlePage"; //$NON-NLS-1$
	/*
	 * Constants that indicate user selection
	 */
	public static final int FACES_CONFIG = 1; 
	public static final int LOAD_BUNDLE = 2; 
	public static final int USER_DEFINED = 3; 
	
	private final int DIALOG_WIDTH = 450;
	private final int DIALOG_HEIGHT = 650;

	private Button facesConfig;
	private Button laodBundle;
	private Button userDefined;
	private Label bundleLabel;
	private Text bundleName;

	/**
	 * Instantiates a new externalize strings wizard register bundle page.
	 *
	 * @param pageName the page name
	 */
	public ExternalizeStringsWizardRegisterBundlePage(String pageName) {
		super(pageName, JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_TITLE, 
				ModelUIImages.getImageDescriptor(ModelUIImages.WIZARD_DEFAULT));
		setDescription(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_DESCRIPTION);
		setPageComplete(true);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		/*
		 * Create basic container
		 */
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.widthHint = DIALOG_WIDTH;
		gd.heightHint = DIALOG_HEIGHT;
		composite.setLayoutData(gd);
		/*
		 * Input field with bundle name
		 */
		bundleLabel = new Label(composite, SWT.NONE);
		bundleLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.NONE, false, false, 1, 1));
		bundleLabel.setText(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_BUNDLE_NAME);
		bundleName = new Text(composite, SWT.BORDER);
		bundleName.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 1, 1));
		String fileName = Constants.EMPTY;
		if (getWizard() instanceof ExternalizeStringsWizard) {
			fileName = ((ExternalizeStringsWizard) getWizard()).editor
					.getEditorInput().getName();
			int pos = fileName.lastIndexOf(Constants.DOT);
			if (pos != -1) {
				fileName = fileName.substring(0, pos);
			}
		}
		bundleName.setText(fileName);
		
		/*
		 * Group with a place for bundle
		 */
		Group group = new Group(composite, SWT.SHADOW_ETCHED_IN);
		group.setLayout(new GridLayout(1, true));
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		group.setText(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_SAVE_RESOURCE_BUNDLE);
		
		facesConfig = new Button(group, SWT.RADIO);
		laodBundle = new Button(group, SWT.RADIO); 
		userDefined = new Button(group, SWT.RADIO);
		facesConfig.setText(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_FACES_CONFIG);
		laodBundle.setText(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_LOAD_BUNDLE);
		userDefined.setText(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_USER_DEFINED);
		
		userDefined.setSelection(true);
		
		facesConfig.addSelectionListener(this);
		laodBundle.addSelectionListener(this);
		userDefined.addSelectionListener(this);

		updateBundleNameField();
		/*
		 * Wizard Page control should be initialized.
		 */
		setControl(composite);
	}

	/**
	 * Gets the user specified bundle name.
	 *
	 * @return the bundle name
	 */
	public String getBundleName() {
		String name = Constants.EMPTY;
		if (bundleName != null) {
			name = bundleName.getText();
		}
		return name;
	}
	
	/**
	 * Checks if user has selected faces config.
	 *
	 * @return true, if in faces config
	 */
	public boolean isInFacesConfig() {
		return (null != facesConfig) && facesConfig.getSelection();
	}
	
	/**
	 * Checks if user has selected load bundle.
	 *
	 * @return true, if via load bundle
	 */
	public boolean isViaLoadBundle() {
		return (null != laodBundle) && laodBundle.getSelection();
	}
	
	/**
	 * Checks if user will register the bundle manually.
	 *
	 * @return true, if user defined
	 */
	public boolean isUserDefined() {
		return (null != userDefined) && userDefined.getSelection();
	}
	
	/**
	 * Update the dialog status.
	 */
	public void updateStatus() {
		/*
		 * If user has entered a path that is different from the source folder
		 * the base-name will be incorrect.
		 * Thus show warning message.
		 */
		if (isSourceFolderSelected()) {
			setMessage(null, IMessageProvider.NONE);
		} else {
			setMessage(  
					JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_WRONG_BUNDLE_PLACEMENT,
					IMessageProvider.WARNING);
		}
	}
	
	/**
	 * Gets the selected place.
	 *
	 * @return the selected place
	 */
	private int getSelectedPlace() {
		int place = FACES_CONFIG;
		if (isInFacesConfig()) {
			place = FACES_CONFIG;
		} else if (isViaLoadBundle()) {
			place = LOAD_BUNDLE;
		} else if (isUserDefined()) {
			place = USER_DEFINED;
		}
		return place;
	}

	/**
	 * Update bundle name field.
	 */
	private void updateBundleNameField() {
		if (isUserDefined()) {
			bundleLabel.setEnabled(false);
			bundleName.setEnabled(false);
		} else {
			bundleLabel.setEnabled(true);
			bundleName.setEnabled(true);
		}
	}
	
	/**
	 * Checks if the source folder is selected.
	 *
	 * @return true, if the source folder is selected.
	 */
	private boolean isSourceFolderSelected() {
		boolean sourceFolderSelected = false;
		if (getWizard() instanceof ExternalizeStringsWizard) {
			ExternalizeStringsWizard wiz = (ExternalizeStringsWizard) getWizard(); 
			if (wiz.editor.getEditorInput() instanceof IFileEditorInput) {
				IProject project = ((IFileEditorInput)(wiz.editor.getEditorInput())).getFile().getProject();
				WizardNewFileCreationPage page2 = (WizardNewFileCreationPage)wiz.getPage(
						ExternalizeStringsWizard.EXTERNALIZE_STRINGS_DIALOG_NEW_FILE_PAGE);
				String userDefinedPath = page2.getContainerFullPath().toString();
				/*
				 * Get the source folders for the project
				 */
				IResource[] src = EclipseUtil.getJavaSourceRoots(project);
				/*
				 * When there are multiple source folders --
				 * match user defined folder to them.
				 */
				String srcPath = Constants.EMPTY;
					for (IResource res : src) {
						srcPath =  res.getFullPath().toString();
						if (userDefinedPath.indexOf(srcPath) > -1) {
							sourceFolderSelected = true;
							break;
						}
					}
			}
		}
		return sourceFolderSelected;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.WizardPage#isPageComplete()
	 */
	public boolean isPageComplete() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetSelected(SelectionEvent e) {
		updateBundleNameField();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected(SelectionEvent e) {
		updateBundleNameField();
	}
}

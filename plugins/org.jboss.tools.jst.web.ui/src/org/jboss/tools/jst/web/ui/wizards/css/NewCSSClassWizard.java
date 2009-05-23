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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.jboss.tools.jst.jsp.outline.cssdialog.CSSClassDialog;
import org.jboss.tools.jst.web.ui.wizards.messages.WebUIMessages;

/**
 * New CSS class wizard.
 */
public class NewCSSClassWizard extends Wizard implements INewWizard {

	private CSSClassDescription classDescription = new CSSClassDescription();

	// workbench selection when the wizard was started
	protected IStructuredSelection selection;
	// the workbench instance
	protected IWorkbench workbench;

	// wizard contains only one page
	private NewCSSClassWizardPage editFilePage;

	private WizardNewCssClassPage selectFilePage;

	/**
	 * Constructor for SampleNewWizard.
	 */
	public NewCSSClassWizard() {
		super();
		setWindowTitle(WebUIMessages.WIZARD_WINDOW_TITLE);
	}

	/**
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;

		IResource selectedResource = (IResource) selection.getFirstElement();
		if (selectedResource != null) {
			if (selectedResource.getType() == IFile.FILE
					&& !WizardNewCssClassPage.CSS_FILE_EXTENSION
							.equals(selectedResource.getFileExtension())) {
				selectedResource = selectedResource.getParent();
			}
			classDescription.setCssFile(selectedResource);
		}

	}

	/**
	 * Adding the page to the wizard.
	 */
	@Override
	public void addPages() {

		selectFilePage = new WizardNewCssClassPage(classDescription);
		editFilePage = new NewCSSClassWizardPage();
		addPage(selectFilePage);
		addPage(editFilePage);
	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We
	 * will create an operation and run it using wizard as execution context.
	 */
	@Override
	public boolean performFinish() {
		if (editFilePage.dialog != null) {
			editFilePage.dialog.save();
			editFilePage.dialog.releaseResources();
		}
		return true;
	}

	/**
	 * @see org.eclipse.jface.wizard.IWizard#canFinish()
	 */
	@Override
	public boolean canFinish() {
		return selectFilePage.canFlipToNextPage()
				&& getContainer().getCurrentPage() == editFilePage
				&& editFilePage.canFinish;
	}

	/**
	 * @see org.eclipse.jface.wizard.IWizard#performCancel()
	 */
	@Override
	public boolean performCancel() {
		if (editFilePage.dialog != null) {
			editFilePage.dialog.releaseResources();
		}
		return true;
	}

	public class CSSClassDescription {
		private IResource cssFile;
		private String cssClassName;

		public IResource getCssFile() {
			return cssFile;
		}

		public void setCssFile(IResource cssFile) {
			this.cssFile = cssFile;
		}

		public String getCssClassName() {
			return cssClassName;
		}

		public void setCssClassName(String cssClassName) {
			this.cssClassName = cssClassName;
		}
	}

	/**
	 * Class representing the first page of the wizard.
	 */
	private class NewCSSClassWizardPage extends WizardPage {

		private CSSClassDialog dialog;

		boolean canFinish = true;

		/**
		 * Constructor for SampleNewWizardPage.
		 * 
		 * @param pageName
		 */
		public NewCSSClassWizardPage() {
			super("newCSSClassWizard"); //$NON-NLS-1$
			setTitle(WebUIMessages.WIZARD_TITLE);
			setDescription(WebUIMessages.WIZARD_DESCRIPTION);

		}

		/**
		 * @see IDialogPage#createControl(Composite)
		 */
		public void createControl(final Composite parent) {

			final Composite container = new Composite(parent, SWT.NONE);

			container.setLayout(new GridLayout(1, true));
			GridData gridData = new GridData(GridData.FILL, GridData.FILL,
					true, true);
			container.setLayoutData(gridData);

			if (getWizard().getContainer() instanceof WizardDialog) {
				final WizardDialog wd = (WizardDialog) getWizard()
						.getContainer();
				wd.addPageChangedListener(new IPageChangedListener() {

					// set console configuration as treeViewer input
					public void pageChanged(PageChangedEvent event) {
						if (event.getSelectedPage() == editFilePage) {

							if (dialog == null) {
								dialog = new CSSClassDialog(getShell(),
										(IFile) classDescription.getCssFile(),
										selection) {
									protected void handleStatusChanged(
											IStatus newStatus) {
										if (newStatus.isOK()
												&& !getStatus().isOK()) {
											NewCSSClassWizardPage.this
													.setErrorMessage(null);
											canFinish = true;
										} else if (newStatus.getSeverity() == IStatus.ERROR) {
											NewCSSClassWizardPage.this
													.setErrorMessage(newStatus
															.getMessage());
											canFinish = false;
										}

										setStatus(newStatus);
									};
								};
								dialog.createControlPane(container);
								container.layout();
							} else {
								dialog.setFile((IFile) classDescription
										.getCssFile());
							}
							dialog.reinit();
							dialog.addNewClass(classDescription.cssClassName);
						}
					}
				});
			}

			setControl(container);

		}

	}

}

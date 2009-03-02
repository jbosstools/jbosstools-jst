/*******************************************************************************
 * Copyright (c) 2007-2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.jst.web.ui.wizards.css;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.FileExtensionFilter;
import org.jboss.tools.jst.web.ui.wizards.css.NewCSSClassWizard.CSSClassDescription;
import org.jboss.tools.jst.web.ui.wizards.messages.WebUIMessages;
import org.w3c.dom.css.CSSFontFaceRule;

/**
 * @author Sergey Dzmitrovich
 * 
 */
public class WizardNewCssClassPage extends WizardPage implements ModifyListener {

	private final static String REQUIRED_FIELD_SIGN = "*"; //$NON-NLS-1$
	private final static String CSS_FILE_EXTENSION = "css"; //$NON-NLS-1$
	private CSSClassDescription classDescription;
	private final static String[] fileExtensions = { CSS_FILE_EXTENSION };
	private IFile currentFile;
	private int numColumns = 3;
	private Text selectFileText;
	private Text classNameText;

	/**
	 * @param pageName
	 */
	public WizardNewCssClassPage(CSSClassDescription classDescription) {
		super("WizardNewCssClassPage"); //$NON-NLS-1$
		this.classDescription = classDescription;
		setTitle(WebUIMessages.WIZARD_TITLE);
		setDescription(WebUIMessages.WIZARD_DESCRIPTION);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = numColumns;
		layout.makeColumnsEqualWidth = false;
		container.setLayout(layout);

		Label selectFileLabel = new Label(container, SWT.NONE);
		selectFileLabel.setText(WebUIMessages.FILE_SELECT_LABEL
				+ REQUIRED_FIELD_SIGN);

		selectFileText = new Text(container, SWT.SINGLE | SWT.BORDER);
		selectFileText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		selectFileText.setFont(parent.getFont());
		selectFileText.addModifyListener(this);

		Button selectFileButton = new Button(container, SWT.NONE);
		selectFileButton.setText(WebUIMessages.FILE_SELECT_BUTTON);
		selectFileButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(
						getShell(), new WorkbenchLabelProvider(),
						new BaseWorkbenchContentProvider());
				dialog.addFilter(new FileExtensionFilter(fileExtensions));
				dialog.setTitle(WebUIMessages.FILE_SELECT_DIALOG_TITLE);
				dialog.setMessage(WebUIMessages.FILE_SELECT_DIALOG_MESSAGE);
				dialog.setInput(ResourcesPlugin.getWorkspace().getRoot());
				dialog.setAllowMultiple(false);
				dialog.setDoubleClickSelects(true);
				if (currentFile != null) {
					dialog.setInitialSelection(currentFile);
				}
				dialog
						.setEmptyListMessage(WebUIMessages.FILE_SELECT_DIALOG_EMPTY_MESSAGE);

				if (dialog.open() == Window.OK) {
					currentFile = (IFile) dialog.getFirstResult();
					selectFileText
							.setText(currentFile.getFullPath().toString());
				}

			}
		});

		Label classNameLabel = new Label(container, SWT.NONE);
		classNameLabel.setText(WebUIMessages.CSS_CLASS_NAME_LABEL
				+ REQUIRED_FIELD_SIGN);

		classNameText = new Text(container, SWT.SINGLE | SWT.BORDER);
		classNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		classNameText.setFont(parent.getFont());
		classNameText.addModifyListener(this);

		setControl(container);
	}

	@Override
	public boolean canFlipToNextPage() {
		if ((classNameText.getText().length() != 0)
				&& (getCssFile(selectFileText.getText()) != null)) {
			return true;
		}
		return false;
	}

	public void modifyText(ModifyEvent e) {

		classDescription.setCssClassName(classNameText.getText());
		classDescription.setCssFile(getCssFile(selectFileText.getText()));
		getContainer().updateButtons();

	}

	private IFile getCssFile(String path) {
		if (path != null) {
			IResource cssFile = ResourcesPlugin.getWorkspace().getRoot()
					.findMember(path);
			if ((cssFile != null)
					&& (CSS_FILE_EXTENSION.equals(cssFile.getFileExtension()))) {
				return (IFile) cssFile;
			}
		}
		return null;
	}

}

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
package org.jboss.tools.jst.jsp.outline.cssdialog;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.jst.jsp.messages.JstUIMessages;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.CSSModel;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.CSSSelectorValidator;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.ICSSDialogModel;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Util;
import org.jboss.tools.jst.jsp.outline.cssdialog.tabs.TabPreviewControl;

/**
 * This dialog represents CSSClass dialog.
 * 
 * @author Igor Zhukov (izhukov@exadel.com)
 */
public class CSSClassDialog extends AbstractCSSDialog {

	// model is the core of the CSS Class Dialog, it manages style attributes
	private ICSSDialogModel cssModel;

	// workbench selection when the wizard was started
	protected IStructuredSelection selection;

	private IFile file;

	private Text fileText;

	// css style classes
	private Combo classCombo;

	private String selectorLabel;

	private Button addNewClassButton;

	// apply button
	private Button applyButton;

	// preview tab
	private TabPreviewControl preview;

	/**
	 * 
	 * @param parentShell
	 * @param file
	 * @param selection
	 */
	public CSSClassDialog(Shell parentShell, IFile file,
			IStructuredSelection selection) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX
				| SWT.APPLICATION_MODAL);

		this.file = file;
		this.cssModel = new CSSModel(file);
		this.selection = selection;

	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(JstUIMessages.CSS_STYLE_CLASS_EDITOR_TITLE);

	}

	@Override
	protected Control createContents(Composite parent) {
		Control contents = super.createContents(parent);
		updateControlPane();
		return contents;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle(JstUIMessages.CSS_STYLE_CLASS_EDITOR_TITLE);
		return super.createDialogArea(parent);
	}

	@Override
	protected StyleComposite createStyleComposite(Composite parent) {
		StyleComposite styleComposite = super.createStyleComposite(parent);

		// add preview tab to styleComposite
		preview = new TabPreviewControl(styleComposite.getTabFolder(),
				getStyleAttributes(), cssModel);
		styleComposite.createTabItem(preview, preview,
				JstUIMessages.PREVIEW_SHEET_TAB_NAME,
				JstUIMessages.PREVIEW_SHEET_TAB_NAME);

		return styleComposite;
	}

	@Override
	protected void createExtensionComposite(final Composite parent) {
		Composite fileControlPanel = new Composite(parent, SWT.BORDER);
		fileControlPanel.setLayoutData(new GridData(GridData.FILL,
				GridData.BEGINNING, true, false));
		fileControlPanel.setLayout(new GridLayout(3, false));

		// add file path control
		addLabel(fileControlPanel, JstUIMessages.CSS_CLASS_DIALOG_FILE_LABEL);

		GridData fileLayoutData = new GridData(GridData.FILL, GridData.CENTER,
				true, false);
		fileLayoutData.horizontalSpan = 2;

		fileText = new Text(fileControlPanel, SWT.BORDER | SWT.READ_ONLY);
		fileText.setLayoutData(fileLayoutData);

		// add class control
		addLabel(fileControlPanel,
				JstUIMessages.CSS_CLASS_DIALOG_STYLE_CLASS_LABEL);

		classCombo = new Combo(fileControlPanel, SWT.BORDER | SWT.READ_ONLY);
		classCombo.setLayoutData(new GridData(GridData.FILL, GridData.CENTER,
				true, false));

		// this listener is responsible for processing dialog header message
		// events
		classCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				handleSelectorSwitched();
			}
		});
		// creates a button for add new class
		addNewClassButton = new Button(fileControlPanel, SWT.PUSH);
		addNewClassButton.setText(JstUIMessages.BUTTON_ADD_NEW_STYLE_CLASS);
		addNewClassButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				InputDialog dlg = new InputDialog(parent.getShell(),
						JstUIMessages.ENTER_CSS_CLASS_NAME,
						JstUIMessages.ENTER_CSS_CLASS_NAME, classCombo
								.getText(), CSSSelectorValidator.getInstance());
				if (dlg.open() == Window.OK) {
					addNewClass(dlg.getValue().trim());
				}
			}
		});

	}

	@Override
	protected Composite createControlComposite(Composite parent) {
		Composite controlComposite = super.createControlComposite(parent);
		createCustomButtonPanel(controlComposite);

		return controlComposite;
	}

	/**
	 * 
	 * @param parent
	 * @param label
	 */
	protected Label addLabel(Composite parent, String label) {
		Label labelControl = new Label(parent, SWT.LEFT);
		labelControl.setLayoutData(new GridData(GridData.END, GridData.CENTER,
				false, false));
		labelControl.setText(label);
		return labelControl;
	}

	/**
	 * This method is used to create custom button panel.
	 * 
	 * @param parent
	 *            Composite component
	 */
	private void createCustomButtonPanel(Composite parent) {
		Composite buttonComposite = new Composite(parent, SWT.NONE);
		buttonComposite.setLayoutData(new GridData(GridData.END,
				GridData.BEGINNING, true, false));
		buttonComposite.setLayout(new GridLayout());
		// add APPLY button
		applyButton = createCustomButton(buttonComposite,
				JstUIMessages.BUTTON_APPLY, JstUIMessages.CSS_APPLY_CHANGES,
				false);
		applyButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				save();
				applyButton.setEnabled(false);
			}
		});
		// add CLEAR button
		Button clearButton = createCustomButton(buttonComposite,
				JstUIMessages.BUTTON_CLEAR,
				JstUIMessages.CSS_CLEAR_STYLE_SHEET, true);
		clearButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				getStyleComposite().clearStyleComposite();
				getStyleComposite().updateCurrentTab();
			}
		});
	}

	/**
	 * This method is used to create custom button.
	 * 
	 * @param parent
	 *            Composite component
	 * @param label
	 *            Button label value
	 */
	protected Button createCustomButton(Composite parent, String label,
			String tooltip, boolean defaultState) {
		// increment the number of columns in the button bar
		((GridLayout) parent.getLayout()).numColumns++;
		Button button = new Button(parent, SWT.PUSH);
		button.setText(label);
		button.setToolTipText(tooltip);
		button.setEnabled(defaultState);
		return button;
	}

	/**
	 * Method is used to correctly process style class change operation.
	 */
	protected void handleSelectorSwitched() {
		if (selectorLabel != null)
			cssModel.updateCSSStyle(selectorLabel, getStyleAttributes());
		selectorLabel = classCombo.getText();
		preview.setSelector(selectorLabel);
		classCombo.setToolTipText(cssModel.getCSSRuleText(selectorLabel));
		getStyleComposite().setStyleProperties(
				cssModel.getClassProperties(selectorLabel));
		getStyleComposite().updateCurrentTab();
	}

	/**
	 * Add New Class to CSS Class Dialog
	 * 
	 * @param styleClassName
	 *            - name of new style class
	 */
	public void addNewClass(String styleClassName) {
		// add new css class
		String newSelectorLabel = cssModel.addCSSRule(styleClassName);
		// add it to combobox
		classCombo.add(newSelectorLabel);
		// select new class, after it in handleSelectorSwitched() dialog will be
		// updated
		classCombo.select(classCombo.getItemCount() - 1);
		
		//this is require 
		applyButton.setEnabled(true);
	}

	@Override
	public void releaseResources() {

		super.releaseResources();
		preview.releaseModel();

		if (cssModel != null) {
			cssModel.release();
			cssModel = null;
		}
	}

	/**
	 * Method should be called in case of dialog closure operation.
	 */
	public void save() {
		cssModel.updateCSSStyle(selectorLabel, getStyleAttributes());
		cssModel.save();
	}

	/**
	 * TODO Gets current selected style class value.
	 * 
	 * @return selector name
	 */
	public String getSelectorName() {
		return Util.formatCSSSelectorToStyleClassView(selectorLabel);
	}

	/**
	 * @see org.eclipse.jface.dialogs.Dialog#close()
	 */
	@Override
	public boolean close() {
		int code = getReturnCode();
		switch (code) {
		case OK:
			save();
			break;
		case CANCEL:
		default:
			// make some closure operation
		}
		return super.close();
	}

	public void reinit() {
		preview.releaseModel();
		cssModel.setFile(file);
		cssModel.reinit();
		preview.reinit(cssModel);
		getStyleComposite().clearStyleComposite();
		updateControlPane();

	}

	public void setFile(IFile file) {
		this.file = file;
	}

	@Override
	protected void handleStyleChanged() {
		super.handleStyleChanged();

		if (getStatus().isOK())
			applyButton.setEnabled(true);

	}

	@Override
	protected void handleStatusChanged(IStatus newStatus) {

		if (newStatus.getSeverity() != getStatus().getSeverity()) {
			applyButton.setEnabled(newStatus.isOK());
			getButton(OK).setEnabled(newStatus.isOK());
			classCombo.setEnabled(newStatus.isOK());
			addNewClassButton.setEnabled(newStatus.isOK());
		}

		super.handleStatusChanged(newStatus);
	}

	protected void updateControlPane() {

		fileText.setText(file.getFullPath().toString());
		classCombo.removeAll();
		classCombo.setEnabled(true);
		selectorLabel = cssModel
				.getSelectorLabel(Util.getSelectionInFile(file).x);

		getStyleComposite().setStyleProperties(
				cssModel.getClassProperties(selectorLabel));
		preview.setSelector(selectorLabel);

		List<String> selectors = cssModel.getSelectorLabels();
		for (int i = 0; i < selectors.size(); i++) {
			String label = selectors.get(i);
			classCombo.add(label);
			if (label.equals(selectorLabel))
				classCombo.select(i);

		}
		
		if(classCombo.getSelectionIndex() == -1 ){
			classCombo.select(0);
		}

		applyButton.setEnabled(false);

	}

	protected Button getApplyButton() {
		return applyButton;
	}

	protected Button getAddNewClassButton() {
		return addNewClassButton;
	}

	protected Combo getClassCombo() {
		return classCombo;
	}

}

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
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.CSSModel;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Constants;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.FileExtensionFilter;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.MessageUtil;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Util;
import org.jboss.tools.jst.jsp.outline.cssdialog.events.ChangeStyleEvent;
import org.jboss.tools.jst.jsp.outline.cssdialog.events.ChangeStyleListener;
import org.jboss.tools.jst.jsp.outline.cssdialog.events.StyleAttributes;

public class CSSClassDialog extends Dialog {

    private Browser browser = null;
    private StyleComposite styleComposite = null;
    private StyleAttributes styleAttributes = null;
    private Text text;
    private IFile file;
    private Combo classCombo;
    private CSSModel cssModel;
    private String selectorName;
    private boolean allProject;

    final static int MIN_HEIGHT_FOR_BROWSER = 60;
    final static String[] fileExtensions = { "css" };
    final static String CSS_FILE_SELECT_DIALOG_TITLE = "CSS File Selection";
    final static String CSS_FILE_SELECT_DIALOG = "Select CSS file from the tree:";
    final static String CSS_FILE_SELECT_DIALOG_EMPTY_MESSAGE = "No CSS file in the current project";
    final static String SKIP_FIRST_CHAR = ".";

    /**
     * 
     * @param parentShell
     * @param allProject
     *            (if allProject is true - browse css file in all projects, else
     *            only in current project)
     */
    public CSSClassDialog(Shell parentShell, boolean allProject) {
	super(parentShell);
	this.allProject = allProject;
	setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX
		| SWT.APPLICATION_MODAL);
	styleAttributes = new StyleAttributes();
    }

    /**
     * 
     * @param parent
     * @return
     */
    public Control createDialogComposite(Composite composite) {
	GridLayout layout = new GridLayout();
	layout.numColumns = 1;
	composite.setLayout(layout);
	browser = new Browser(composite, SWT.BORDER);

	/* ============================================= */
	Composite classComposite = new Composite(composite, SWT.BORDER);
	GridData gridData = new GridData(GridData.FILL, GridData.FILL, true,
		false);
	classComposite.setLayoutData(gridData);

	final GridLayout gridLayout = new GridLayout();
	gridLayout.numColumns = 3;
	classComposite.setLayout(gridLayout);

	Label label = new Label(classComposite, SWT.LEFT);
	label.setLayoutData(new GridData(GridData.END, GridData.CENTER, false,
		false));
	label.setText("CSS file :");

	text = new Text(classComposite, SWT.BORDER);
	text.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true,
		false));
	text.addModifyListener(new ModifyListener() {
	    public void modifyText(ModifyEvent e) {

		IResource res = null;
		if (allProject) {
		    IWorkspace workspace = ResourcesPlugin.getWorkspace();
		    res = workspace.getRoot().findMember(text.getText());
		} else {
		    IProject project = Util.getCurrentProject();
		    if (project != null)
			res = project.findMember(text.getText());
		}

		if (res != null) {
		    if (res instanceof IFile) {
			file = (IFile) res;
			cssModel = new CSSModel(file);
			classCombo.removeAll();
			List<String> selectors = cssModel.getSelectors();
			for (String selector : selectors) {
			    classCombo.add(selector);
			}
		    }
		}
	    }

	});

	Button button = new Button(classComposite, SWT.PUSH);
	button.setLayoutData(new GridData(GridData.END, GridData.CENTER, false,
		false));

	ImageDescriptor imageDesc = JspEditorPlugin
		.getImageDescriptor(Constants.IMAGE_FOLDERLARGE_FILE_LOCATION);
	Image image = imageDesc.createImage();
	button.setImage(image);
	button.setToolTipText(MessageUtil
		.getString("CSS_BROWSE_BUTTON_TOOLTIP"));
	button.addDisposeListener(new DisposeListener() {
	    public void widgetDisposed(DisposeEvent e) {
		Button button = (Button) e.getSource();
		button.getImage().dispose();
	    }
	});

	label = new Label(classComposite, SWT.LEFT);
	label.setLayoutData(new GridData(GridData.END, GridData.CENTER, false,
		false));
	label.setText("Style class :");

	gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
	gridData.horizontalSpan = 2;
	classCombo = new Combo(classComposite, SWT.BORDER);
	classCombo.setLayoutData(gridData);
	classCombo.addSelectionListener(new SelectionListener() {

	    public void widgetDefaultSelected(SelectionEvent e) {

	    }

	    public void widgetSelected(SelectionEvent e) {
		String style = cssModel.getStyle(classCombo.getText());
		classCombo.setToolTipText(cssModel.getCSSText(classCombo
			.getText()));
		styleComposite.recreateStyleComposite(style);
	    }

	});

	button.addSelectionListener(new SelectionAdapter() {
	    public void widgetSelected(SelectionEvent event) {

		IAdaptable project = ResourcesPlugin.getWorkspace();
		if (!allProject) {
		    project = Util.getCurrentProject();
		}

		ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(
			getShell(), new WorkbenchLabelProvider(),
			new BaseWorkbenchContentProvider());
		dialog.addFilter(new FileExtensionFilter(fileExtensions));
		dialog.setTitle(CSS_FILE_SELECT_DIALOG_TITLE);
		dialog.setMessage(CSS_FILE_SELECT_DIALOG);
		dialog.setInput(project);
		dialog.setAllowMultiple(false);
		dialog
			.setEmptyListMessage(CSS_FILE_SELECT_DIALOG_EMPTY_MESSAGE);
		if (dialog.open() == Window.OK) {
		    IResource res = (IResource) dialog.getFirstResult();
		    if (allProject) {
			text.setText(res.getFullPath().toOSString());
		    } else {
			text.setText(res.getProjectRelativePath().toOSString());
		    }
		}
	    }
	});

	/* ============================================ */
	styleAttributes.addChangeStyleListener(new ChangeStyleListener() {
	    public void styleChanged(ChangeStyleEvent event) {
		String styleForSpan = "";
		String html = "";

		Set<String> keySet = styleAttributes.keySet();

		for (String key : keySet)
		    styleForSpan += key + Constants.COLON_STRING
			    + styleAttributes.getAttribute(key)
			    + Constants.SEMICOLON_STRING;

		html = Constants.OPEN_DIV_TAG + styleForSpan
			+ Constants.TEXT_FOR_PREVIEW + Constants.CLOSE_DIV_TAG;
		browser.setText(html);
	    }
	});
	styleComposite = new StyleComposite(composite, styleAttributes, "");

	gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
	gridData.minimumHeight = MIN_HEIGHT_FOR_BROWSER;
	browser.setLayoutData(gridData);

	return composite;
    }

    public void saveChanges() {
	styleComposite.updateStyle();
	String newStyle = styleComposite.getNewStyle();
	cssModel.setCSS(classCombo.getText(), newStyle);
	cssModel.saveModel();
    }

    public String getSelectorName() {
	return selectorName;
    }

    /**
     * Method for creating dialog area
     * 
     * @param parent
     * 
     */
    protected Control createDialogArea(final Composite parent) {

	final Composite composite = (Composite) super.createDialogArea(parent);

	return createDialogComposite(composite);
    }

    /**
     * Method for setting title for dialog
     * 
     * @param newShell
     */
    protected void configureShell(Shell newShell) {
	super.configureShell(newShell);
	newShell.setText(MessageUtil.getString("CSS_STYLE_CLASS_DIALOG_TITLE"));
    }

    protected void okPressed() {
	saveChanges();
	String sel = classCombo.getText();
	if (sel.trim().startsWith(SKIP_FIRST_CHAR)) {
	    sel = sel.substring(1);
	}
	selectorName = sel;
	super.okPressed();
    }
}

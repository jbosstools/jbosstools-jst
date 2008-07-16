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
package org.jboss.tools.jst.jsp.outline.cssdialog.tabs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.outline.cssdialog.CSSDialog;
import org.jboss.tools.jst.jsp.outline.cssdialog.ImageSelectionDialog;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.CSSConstants;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Constants;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.ImageCombo;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.MessageUtil;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Util;
import org.jboss.tools.jst.jsp.outline.cssdialog.parsers.ColorParserListener;

/**
 * Class for creating control in Background tab
 * 
 * @author dsakovich@exadel.com
 * 
 */
public class TabBackgroundControl extends Composite {

    private ImageCombo colorCombo;

    private Combo backgroundImageCombo;
    private Combo backgroundRepeatCombo;

    private HashMap<String, String> attributesMap;

    private static final int numColumns = 3;

    private CSSDialog cssDialog;

    public TabBackgroundControl(final Composite composite,
	    HashMap<String, ArrayList<String>> comboMap,
	    final HashMap<String, String> attributesMap, CSSDialog dialog) {
	super(composite, SWT.NONE);
	this.attributesMap = attributesMap;
	this.cssDialog = dialog;

	final GridLayout gridLayout = new GridLayout();
	gridLayout.numColumns = numColumns;
	setLayout(gridLayout);

	Label label = new Label(this, SWT.LEFT);
	label.setLayoutData(new GridData(GridData.END, GridData.CENTER, false,
		false));
	label.setText(MessageUtil.getString("BACKGROUND_COLOR"));

	colorCombo = new ImageCombo(this, SWT.BORDER);
	colorCombo.setLayoutData(new GridData(GridData.FILL, GridData.CENTER,
		true, false));
	// ////////////////////////////////////////////////////////////////////////
	Set<Entry<String, String>> set = ColorParserListener.getMap()
		.entrySet();

	for (Map.Entry<String, String> me : set) {
	    RGB rgb = Util.getColor(me.getKey());
	    colorCombo.add(me.getValue(), rgb);
	}

	colorCombo.addModifyListener(new ModifyListener() {
	    public void modifyText(ModifyEvent event) {
		String tmp = colorCombo.getText();
		if (tmp != null) {
		    if (tmp.trim().length() > 0)
			attributesMap.put(CSSConstants.BACKGROUND_COLOR, tmp);
		    else
			attributesMap.remove(CSSConstants.BACKGROUND_COLOR);
		}
		cssDialog.setStyleForPreview();
	    }
	});

	Button button = new Button(this, SWT.PUSH);
	button.setLayoutData(new GridData(GridData.END, GridData.CENTER, false,
		false));
	button.setToolTipText(MessageUtil.getString("BACKGROUND_COLOR_TIP"));
	ImageDescriptor colorDesc = JspEditorPlugin
		.getImageDescriptor(Constants.IMAGE_COLORLARGE_FILE_LOCATION);
	Image im = colorDesc.createImage();
	button.setImage(im);
	button.addDisposeListener(new DisposeListener() {
	    public void widgetDisposed(DisposeEvent e) {
		Button button = (Button) e.getSource();
		button.getImage().dispose();
	    }
	});
	button.addSelectionListener(new SelectionAdapter() {
	    public void widgetSelected(SelectionEvent event) {
		ColorDialog dlg = new ColorDialog(getShell());

		dlg
			.setRGB(Util.getColor((colorCombo.getText().trim())) == null ? new RGB(
				0, 0, 0)
				: Util.getColor((colorCombo.getText().trim())));
		dlg.setText(MessageUtil.getString("COLOR_DIALOG_TITLE"));
		RGB rgb = dlg.open();
		if (rgb != null) {
		    String colorStr = Util.createColorString(rgb);
		    colorCombo.setText(colorStr);
		}
	    }
	});

	label = new Label(this, SWT.LEFT);
	label.setLayoutData(new GridData(GridData.END, GridData.CENTER, false,
		false));
	label.setText(MessageUtil.getString("BACKGROUND_IMAGE"));

	backgroundImageCombo = new Combo(this, SWT.BORDER);
	final GridData backgroundImageGridData = new GridData(GridData.FILL,
		GridData.CENTER, true, false);
	backgroundImageCombo.setLayoutData(backgroundImageGridData);
	backgroundImageCombo.add(Constants.NONE);

	backgroundImageCombo.addModifyListener(new ModifyListener() {
	    public void modifyText(ModifyEvent event) {
		String tmp = backgroundImageCombo.getText();
		if (tmp != null) {
		    if (tmp.trim().length() > 0)
			attributesMap.put(CSSConstants.BACKGROUND_IMAGE, tmp);
		    else
			attributesMap.remove(CSSConstants.BACKGROUND_IMAGE);
		}
		cssDialog.setStyleForPreview();
	    }
	});

	button = new Button(this, SWT.PUSH);
	button.setLayoutData(new GridData(GridData.END, GridData.CENTER, false,
		false));
	button.setToolTipText(MessageUtil.getString("BACKGROUND_IMAGE"));
	ImageDescriptor imageDesc = JspEditorPlugin
		.getImageDescriptor(Constants.IMAGE_FOLDERLARGE_FILE_LOCATION);
	Image image = imageDesc.createImage();
	button.setImage(image);
	button.addDisposeListener(new DisposeListener() {
	    public void widgetDisposed(DisposeEvent e) {
		Button button = (Button) e.getSource();
		button.getImage().dispose();
	    }
	});
	button.addSelectionListener(new SelectionAdapter() {
	    public void widgetSelected(SelectionEvent event) {
		IProject project = getCurrentProject();
		ImageSelectionDialog dialog = new ImageSelectionDialog(
			getShell(), new WorkbenchLabelProvider(),
			new WorkbenchContentProvider());
		dialog.setTitle(MessageUtil.getString("IMAGE_DIALOG_TITLE"));
		dialog
			.setMessage(MessageUtil
				.getString("IMAGE_DIALOG_MESSAGE"));
		dialog.setEmptyListMessage(MessageUtil
			.getString("IMAGE_DIALOG_EMPTY_MESSAGE"));
		dialog.setAllowMultiple(false);
		dialog.setInput(project);
		if (dialog.open() == ImageSelectionDialog.OK) {
		    IFile file = (IFile) dialog.getFirstResult();
		    String value = file.getFullPath().toString();
		    backgroundImageCombo.add(value);
		    backgroundImageCombo.setText(value);
		}
	    }
	});

	label = new Label(this, SWT.LEFT);
	label.setLayoutData(new GridData(GridData.END, GridData.CENTER, false,
		false));
	label.setText(MessageUtil.getString("BACKGROUND_REPEAT"));

	backgroundRepeatCombo = new Combo(this, SWT.BORDER);
	backgroundRepeatCombo.setLayoutData(new GridData(GridData.FILL,
		GridData.CENTER, false, false));

	backgroundRepeatCombo.addModifyListener(new ModifyListener() {
	    public void modifyText(ModifyEvent event) {
		String tmp = backgroundRepeatCombo.getText();
		if (tmp != null) {
		    if (tmp.trim().length() > 0)
			attributesMap.put(CSSConstants.BACKGROUND_REPEAT, tmp);
		    else
			attributesMap.remove(CSSConstants.BACKGROUND_REPEAT);
		}
		cssDialog.setStyleForPreview();
	    }
	});

	ArrayList<String> list = comboMap.get(CSSConstants.BACKGROUND_REPEAT);
	for (String str : list)
	    backgroundRepeatCombo.add(str);

	label = new Label(this, SWT.LEFT);
	label.setLayoutData(new GridData(GridData.END, GridData.CENTER, false,
		false));
	label.setText(Constants.EMPTY_STRING);

    }

    /**
     * Method for get current project
     * 
     * @return IProject
     */
    public static IProject getCurrentProject() {
	IEditorPart editor = JspEditorPlugin.getDefault().getWorkbench()
		.getActiveWorkbenchWindow().getActivePage().getActiveEditor();
	IEditorInput input = editor.getEditorInput();
	IFile file = null;
	if (input instanceof IFileEditorInput) {
	    file = ((IFileEditorInput) input).getFile();
	}
	if (file == null)
	    return null;

	IProject project = file.getProject();
	return project;

    }

    /**
     * Method for get data in controls (if param equal true ), or set data (if
     * param equal false).
     * 
     * @param param
     */
    public void updateData(boolean param) {
	String tmp;
	
	//TODO Evgeny Zheleznyakov remove block comment
	/*if (param) {
	    tmp = colorCombo.getText();
	    if (tmp != null) {
		if (tmp.trim().length() > 0)
		    attributesMap.put(CSSConstants.BACKGROUND_COLOR, tmp);
		else
		    attributesMap.remove(CSSConstants.BACKGROUND_COLOR);
	    }
	    tmp = backgroundImageCombo.getText();
	    if (tmp != null) {
		if (tmp.trim().length() > 0)
		    attributesMap.put(CSSConstants.BACKGROUND_IMAGE, tmp);
		else
		    attributesMap.remove(CSSConstants.BACKGROUND_IMAGE);
	    }
	    tmp = backgroundRepeatCombo.getText();
	    if (tmp != null) {
		if (tmp.trim().length() > 0)
		    attributesMap.put(CSSConstants.BACKGROUND_REPEAT, tmp);
		else
		    attributesMap.remove(CSSConstants.BACKGROUND_REPEAT);
	    }
	} else {*/
	    if ((tmp = attributesMap.get(CSSConstants.BACKGROUND_COLOR)) != null)
		colorCombo.setText(tmp);
	    else
		colorCombo.setText(Constants.EMPTY_STRING);
	    if ((tmp = attributesMap.get(CSSConstants.BACKGROUND_IMAGE)) != null)
		backgroundImageCombo.setText(tmp);
	    else
		backgroundImageCombo.setText(Constants.EMPTY_STRING);
	    if ((tmp = attributesMap.get(CSSConstants.BACKGROUND_REPEAT)) != null)
		backgroundRepeatCombo.setText(tmp);
	    else
		backgroundRepeatCombo.setText(Constants.EMPTY_STRING);
    }
}
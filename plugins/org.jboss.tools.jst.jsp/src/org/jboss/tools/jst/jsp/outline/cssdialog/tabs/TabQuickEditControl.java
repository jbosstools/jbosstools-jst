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
import java.util.Collections;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.outline.cssdialog.FontFamilyDialog;
import org.jboss.tools.jst.jsp.outline.cssdialog.ImageSelectionDialog;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.CSSConstants;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Constants;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.ImageCombo;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.MessageUtil;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Util;
import org.jboss.tools.jst.jsp.outline.cssdialog.events.StyleAttributes;
import org.jboss.tools.jst.jsp.outline.cssdialog.parsers.ColorParser;


/**
 * 
 * Class for creating control in Quick edit tab
 * 
 * @author Evgeny Zheleznyakov
 * 
 */
public class TabQuickEditControl extends Composite {

    private static String COLON_STRING = ":"; //$NON-NLS-1$
    private static String SPACE_STRING = " "; //$NON-NLS-1$

    private static String DASH_STRING = "-"; //$NON-NLS-1$

    private static int FIRST_INDEX = 0;
    private static int SECOND_INDEX = 1;

    private static int GRID_NUM_COLUMNS = 3;

    private static int VALUE_NUMBER = 0;
    private static int EXT_VALUE_NUMBER = 1;

    private Label label = null;

    private HashMap<String, ArrayList<String>> comboMap;
    private StyleAttributes styleAttributes;
    
  //TODO Dzmitry Sakovich
    //private CSSDialog dialog;

    /**
     * Constructor for creating controls
     * 
     * @param composite
     *                The parent composite for tab
     */
    public TabQuickEditControl(Composite sc,
	    HashMap<String, ArrayList<String>> comboMap,
	    StyleAttributes styleAttributes) {
	super(sc, SWT.NONE);

	//TODO Dzmitry Sakovich
	//this.dialog = dialog;
	this.styleAttributes = styleAttributes;
	this.comboMap = comboMap;
	GridLayout gridLayout = new GridLayout();
	gridLayout.numColumns = GRID_NUM_COLUMNS;
	setLayout(gridLayout);

	ArrayList<String> listKeys = new ArrayList<String>();

	for (String key : styleAttributes.keySet())
	    listKeys.add(key);

	Collections.sort(listKeys);

	for (String key : listKeys) {

	    label = new Label(this, SWT.LEFT);
	    label.setText(format(key));
	    label.setLayoutData(new GridData(GridData.END, GridData.CENTER,
		    false, false));
	    if (comboMap.keySet().contains(key)) {
	    	createCombo(this, key, styleAttributes.getAttribute(key));
	    } else {
	    	createText(this, key, styleAttributes.getAttribute(key));
	    }
	}
    }
    
    public void updateData() {
	
	Control[] controls = this.getChildren();
	if (controls!= null) {
	    for (int i=0; i<controls.length; i++) {
		if (!controls[i].isDisposed()) {
		    controls[i].dispose();
		}
	    }
	}
	
	
	ArrayList<String> listKeys = new ArrayList<String>();

	for (String key : styleAttributes.keySet())
	    listKeys.add(key);

	Collections.sort(listKeys);

	for (String key : listKeys) {

	    label = new Label(this, SWT.LEFT);
	    label.setText(format(key));
	    label.setLayoutData(new GridData(GridData.END, GridData.CENTER,
		    false, false));
	    if (comboMap.keySet().contains(key)) {
	    	createCombo(this, key, styleAttributes.getAttribute(key));
	    } else {
	    	createText(this, key, styleAttributes.getAttribute(key));
	    }
	}
	this.layout();
    }

    /**
     * Method for creating combo controls
     * 
     * @param composite
     *                The parent composite for tab
     * @param name
     *                Name of css element
     * @param value
     *                Value of css element
     */
    private void createCombo(final Composite composite, final String name,
	    final String value) {

	Button btn = null;
	//if css attribute with color combo
	if (name.indexOf(CSSConstants.COLOR) != Constants.DONT_CONTAIN) {

	    final ImageCombo colorCombo = new ImageCombo(composite, SWT.BORDER);
	    colorCombo.setText(value);
	    colorCombo.setLayoutData(new GridData(GridData.FILL,
		    GridData.CENTER, true, false));

	    btn = new Button(composite, SWT.NONE);
	    btn.setLayoutData(new GridData(GridData.END, GridData.CENTER,
		    false, false));
	    ImageDescriptor imageDes = JspEditorPlugin
		    .getImageDescriptor(Constants.IMAGE_COLOR_FILE_LOCATION);
	    Image colorImage = imageDes.createImage();
	    btn.setImage(colorImage);
	    btn.setToolTipText(MessageUtil.getString("COLOR_DIALOG_TITLE")); //$NON-NLS-1$
	    btn.addDisposeListener(new DisposeListener() {
		public void widgetDisposed(DisposeEvent e) {
		    Button button = (Button) e.getSource();
		    button.getImage().dispose();
		}
	    });
	    btn.addSelectionListener(new SelectionAdapter() {

		public void widgetSelected(SelectionEvent event) {

		    RGB startRgb = Util.getColor((colorCombo.getText()
			    .toLowerCase().trim()));

		    if (startRgb == null)
			startRgb = Constants.RGB_BLACK;

		    ColorDialog colorDialog = new ColorDialog(composite
			    .getShell());
		    colorDialog.setRGB(startRgb);
		    colorDialog.setText(MessageUtil
			    .getString("COLOR_DIALOG_TITLE")); //$NON-NLS-1$
		    RGB rgb = colorDialog.open();
		    if (rgb != null) {

			String str = Util.createColorString(rgb);

			if (ColorParser.getInstance().getMap().get(str) != null)
			    colorCombo.setText(ColorParser.getInstance().getMap()
				    .get(str));
			else
			    colorCombo.setText(str);
		    }
		}
	    });

	    colorCombo.addModifyListener(new ModifyListener() {

		String key = name;

		public void modifyText(ModifyEvent event) {

		    if (!colorCombo.getText().trim().equals(
			    Constants.EMPTY_STRING))
			styleAttributes.addAttribute(key, colorCombo.getText().trim());
		    else
			styleAttributes.removeAttribute(key);
		  //TODO Dzmitry Sakovich
		    //dialog.setStyleForPreview();
		}
	    });

	    HashMap<String, String> colorMap = ColorParser.getInstance().getMap();

	    for (String key : colorMap.keySet()) {
		RGB rgb = Util.getColor(key);
		colorCombo.add(colorMap.get(key), rgb);
	    }

	    if (btn == null) {
		new Label(composite, SWT.NONE);
	    }

	    colorCombo.setText(value);

	    //if css attribute contain choose_folder button
	} else if (Util.containFolder(name)) {
	    final Combo combo = new Combo(composite, SWT.NONE);
	    combo.setLayoutData(new GridData(GridData.FILL, GridData.CENTER,
		    true, false));
	    btn = new Button(composite, SWT.NONE);
	    btn.setLayoutData(new GridData(GridData.END, GridData.CENTER,
		    false, false));
	    ImageDescriptor imageDes = JspEditorPlugin
		    .getImageDescriptor(Constants.IMAGE_FOLDER_FILE_LOCATION);
	    Image chooserImage = imageDes.createImage();
	    btn.setImage(chooserImage);
	    btn.addDisposeListener(new DisposeListener() {
		public void widgetDisposed(DisposeEvent e) {
		    Button button = (Button) e.getSource();
		    button.getImage().dispose();
		}
	    });
	    btn.setToolTipText(MessageUtil.getString("COLOR_DIALOG_TITLE")); //$NON-NLS-1$
	    ArrayList<String> list = comboMap.get(name);
	    for (String str : list)
		combo.add(str);

	    btn.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent event) {
		    IProject project = Util.getCurrentProject();
		    ImageSelectionDialog dialog = new ImageSelectionDialog(
			    composite.getShell(), new WorkbenchLabelProvider(),
			    new WorkbenchContentProvider());
		    dialog
			    .setTitle(MessageUtil
				    .getString("IMAGE_DIALOG_TITLE")); //$NON-NLS-1$
		    dialog.setMessage(MessageUtil
			    .getString("IMAGE_DIALOG_MESSAGE")); //$NON-NLS-1$
		    dialog.setEmptyListMessage(MessageUtil
			    .getString("IMAGE_DIALOG_EMPTY_MESSAGE")); //$NON-NLS-1$
		    dialog.setAllowMultiple(false);
		    dialog.setInput(project);
		    if (dialog.open() == ImageSelectionDialog.OK) {
			IFile file = (IFile) dialog.getFirstResult();
			String value = file.getFullPath().toString();
			combo.setText(value);
		    }
		}
	    });

	    combo.setText(value);
	    combo.addModifyListener(new ModifyListener() {

		String key = name;

		public void modifyText(ModifyEvent event) {
		    if (!combo.getText().trim().equals(Constants.EMPTY_STRING))
			styleAttributes.addAttribute(key, combo.getText().trim());
		    else
			styleAttributes.removeAttribute(key);

		  //TODO Dzmitry Sakovich
		    //dialog.setStyleForPreview();
		}
	    });

	} else {
	    final Combo combo = new Combo(composite, SWT.CENTER);
	    combo.setLayoutData(new GridData(GridData.FILL, GridData.CENTER,
		    true, false));

	    ArrayList<String> list = comboMap.get(name);
	    for (String str : list)
		combo.add(str);

	    final Combo extCombo;
	    String values[] = null;
	    boolean ext = false;
	    if (Util.searchInExtElement(name)) {
		ext = true;
		values = Util.convertExtString(value);
		extCombo = new Combo(composite, SWT.BORDER | SWT.READ_ONLY);
		extCombo.setLayoutData(new GridData(GridData.END,
			GridData.CENTER, false, false));
		for (int i = 0; i < Constants.extSizes.length; i++)
		    extCombo.add(Constants.extSizes[i]);
		extCombo.select(extCombo.indexOf(values[EXT_VALUE_NUMBER]));

		extCombo.addModifyListener(new ModifyListener() {

		    String key = name;

		    public void modifyText(ModifyEvent event) {
			String tmpCombo = combo.getText().trim();
			if (tmpCombo == null)
			    return;
			if (tmpCombo.equals(Constants.EMPTY_STRING))
			    return;

			String tmpExt = extCombo.getText();

			if (tmpExt != null) {
			    styleAttributes.addAttribute(key, tmpCombo + tmpExt);
			  //TODO Dzmitry Sakovich
			    //dialog.setStyleForPreview();
			}
		    }
		});

		combo.addModifyListener(new ModifyListener() {

		    String key = name;

		    public void modifyText(ModifyEvent e) {

			String currentText = combo.getText();

			String[] items = combo.getItems();

			for (int i = 0; i < items.length; i++) {
			    if (currentText.equalsIgnoreCase(items[i])) {
				extCombo.select(VALUE_NUMBER);
				extCombo.setEnabled(false);
				styleAttributes.addAttribute(key, currentText);
				//TODO Dzmitry Sakovich
				//dialog.setStyleForPreview();
				return;
			    }
			}
			extCombo.setEnabled(true);

			String tmp = combo.getText().trim();
			if (tmp != null) {
			    if (!tmp.equals(Constants.EMPTY_STRING)) {
				String extTmp = extCombo.getText().trim();
				if (extTmp != null)
				    styleAttributes.addAttribute(key, tmp + extTmp);
				else
				    styleAttributes.addAttribute(key, tmp);

			    } else
				styleAttributes.removeAttribute(key);
			} else
			    styleAttributes.removeAttribute(key);
			//TODO Dzmitry Sakovich
			//dialog.setStyleForPreview();
		    }
		});

	    } else {
		new Label(composite, SWT.NONE).setLayoutData(new GridData(
			GridData.END, GridData.CENTER, false, false));
		combo.addModifyListener(new ModifyListener() {

		    String key = name;

		    public void modifyText(ModifyEvent event) {

			if (!combo.getText().trim().equals(
				Constants.EMPTY_STRING))
			    styleAttributes.addAttribute(key, combo.getText().trim());
			else
			    styleAttributes.removeAttribute(key);

			//TODO Dzmitry Sakovich
			//dialog.setStyleForPreview();
		    }
		});
	    }

	    if (ext)
		combo.setText(values[VALUE_NUMBER]);
	    else
		combo.setText(value);
	}
    }

    /**
     * Method for creating text controls
     * 
     * @param composite
     *                The parent composite for tab
     * @param name
     *                Name of css element
     * @param value
     *                Value of css element
     */
    private void createText(final Composite composite, final String name,
	    final String value) {

	Button btn = null;

	final Text text = new Text(composite, SWT.BORDER);
	text.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true,
		false));

	if (name.equalsIgnoreCase(CSSConstants.FONT_FAMILY)) {
	    btn = new Button(composite, SWT.NONE);
	    btn.setLayoutData(new GridData(GridData.END, GridData.CENTER,
		    false, false));
	    ImageDescriptor imageDes = JspEditorPlugin
		    .getImageDescriptor(Constants.IMAGE_FONT_FILE_LOCATION);
	    Image fontImage = imageDes.createImage();
	    btn.setImage(fontImage);
	    btn.setToolTipText(MessageUtil.getString("FONT_FAMILY_TIP")); //$NON-NLS-1$
	    btn.addDisposeListener(new DisposeListener() {
		public void widgetDisposed(DisposeEvent e) {
		    Button button = (Button) e.getSource();
		    button.getImage().dispose();
		}
	    });
	    btn.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent event) {
		    FontFamilyDialog dialog = new FontFamilyDialog(composite
			    .getShell(), text.getText());
		    if (dialog.open() == Window.OK) {
			text.setText(dialog.getFontFamily());
		    }
		}
	    });

	    text.addModifyListener(new ModifyListener() {

		String key = name;

		public void modifyText(ModifyEvent event) {

		    if (!text.getText().trim().equals(Constants.EMPTY_STRING))
			styleAttributes.addAttribute(key, text.getText().trim());
		    else
			styleAttributes.removeAttribute(key);

		  //TODO Dzmitry Sakovich
		    //dialog.setStyleForPreview();
		}
	    });
	}

	final Combo extCombo;
	String values[] = null;
	boolean ext = false;
	if (btn == null) {
	    if (Util.searchInExtElement(name)) {
		values = Util.convertExtString(value);
		ext = true;
		extCombo = new Combo(composite, SWT.BORDER | SWT.READ_ONLY);
		extCombo.setLayoutData(new GridData(GridData.END,
			GridData.CENTER, false, false));
		for (int i = 0; i < Constants.extSizes.length; i++)
		    extCombo.add(Constants.extSizes[i]);
		extCombo.select(extCombo.indexOf(values[EXT_VALUE_NUMBER]));

		extCombo.addModifyListener(new ModifyListener() {

		    String key = name;

		    public void modifyText(ModifyEvent event) {
			String tmpCombo = text.getText().trim();
			if (tmpCombo == null)
			    return;
			if (tmpCombo.equals(Constants.EMPTY_STRING))
			    return;

			String tmpExt = extCombo.getText();

			if (tmpExt != null) {
			    styleAttributes.addAttribute(key, tmpCombo + tmpExt);
			  //TODO Dzmitry Sakovich
			    //dialog.setStyleForPreview();
			}
		    }
		});

		text.addModifyListener(new ModifyListener() {

		    String key = name;

		    public void modifyText(ModifyEvent e) {

			String tmp = text.getText().trim();
			if (tmp != null) {
			    if (!tmp.equals(Constants.EMPTY_STRING)) {
				String extTmp = extCombo.getText().trim();
				if (extTmp != null)
				    styleAttributes.addAttribute(key, tmp + extTmp);
				else
				    styleAttributes.addAttribute(key, tmp);

			    } else
				styleAttributes.removeAttribute(key);
			} else
			    styleAttributes.removeAttribute(key);
			//TODO Dzmitry Sakovich
			//dialog.setStyleForPreview();
		    }
		});

	    } else {
		new Label(composite, SWT.NONE).setLayoutData(new GridData(
			GridData.END, GridData.CENTER, false, false));

		text.addModifyListener(new ModifyListener() {

		    String key = name;

		    public void modifyText(ModifyEvent event) {

			if (!text.getText().trim().equals(
				Constants.EMPTY_STRING))
			    styleAttributes.addAttribute(key, text.getText().trim());
			else
			    styleAttributes.removeAttribute(key);

			//TODO Dzmitry Sakovich
			//dialog.setStyleForPreview();
		    }
		});

	    }
	}

	if (ext)
	    text.setText(values[VALUE_NUMBER]);
	else
	    text.setText(value);
    }

    /**
     * Method for format css names (text-decoration : Text Decoration)
     * 
     * @param str
     *                CSS name
     * @return Format ss name
     */
    private String format(String str) {

	StringTokenizer st = new StringTokenizer(str, DASH_STRING);
	String finalStr = Constants.EMPTY_STRING;

	while (st.hasMoreTokens()) {
	    StringBuffer sb = new StringBuffer(st.nextToken());
	    String upper = sb.substring(FIRST_INDEX, SECOND_INDEX)
		    .toUpperCase();
	    sb.replace(FIRST_INDEX, SECOND_INDEX, upper);
	    finalStr += sb;
	    if (st.hasMoreTokens())
		finalStr += SPACE_STRING;
	    else
		finalStr += COLON_STRING;
	}
	return finalStr;
    }
}
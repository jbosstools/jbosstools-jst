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
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
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
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.messages.JstUIMessages;
import org.jboss.tools.jst.jsp.outline.cssdialog.ImageSelectionDialog;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.CSSConstants;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Constants;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.ImageCombo;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Util;
import org.jboss.tools.jst.jsp.outline.cssdialog.events.AttributeModifyListener;
import org.jboss.tools.jst.jsp.outline.cssdialog.events.StyleAttributes;
import org.jboss.tools.jst.jsp.outline.cssdialog.parsers.ColorParser;


/**
 * Class for creating control in Background tab
 *
 * @author Igor Zhukov (izhukov@exadel.com)
 */
public class TabBackgroundControl extends BaseTabControl {

    private static final int numColumns = 3;
    private ImageCombo colorCombo;
    private Combo backgroundImageCombo;
    private Combo backgroundRepeatCombo;

    /**
     * Constructor for creating controls
     *
     * @param composite Composite element
     * @param comboMap
     * @param styleAttributes the StyleAttributes object
     */
    public TabBackgroundControl(final Composite composite, HashMap<String, ArrayList<String>> comboMap,
        final StyleAttributes styleAttributes) {
        super(composite, SWT.NONE);
        this.styleAttributes = styleAttributes;

        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = numColumns;
        setLayout(gridLayout);

        // =====================================================================================
        // Add BACKGROUND_COLOR element
        // =====================================================================================
        Label label = new Label(this, SWT.LEFT);
        label.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
        label.setText(JstUIMessages.BACKGROUND_COLOR);

        colorCombo = new ImageCombo(this, SWT.BORDER);
        colorCombo.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        colorCombo.addModifyListener(new AttributeModifyListener(this, CSSConstants.BACKGROUND_COLOR,
        		AttributeModifyListener.MODIFY_SIMPLE_ATTRIBUTE_FIELD));
        Set<Entry<String, String>> set = ColorParser.getInstance().getMap().entrySet();
        for (Map.Entry<String, String> me : set) {
            RGB rgb = Util.getColor(me.getKey());
            colorCombo.add(me.getValue(), rgb);
        }

        Button button = new Button(this, SWT.PUSH);
        button.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
        button.setToolTipText(JstUIMessages.BACKGROUND_COLOR_TIP);

        ImageDescriptor colorDesc = JspEditorPlugin.getImageDescriptor(Constants.IMAGE_COLORLARGE_FILE_LOCATION);
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

        		dlg.setRGB((Util.getColor((colorCombo.getText().trim())) == null) ? new RGB(0, 0, 0)
        			: Util.getColor((colorCombo.getText().trim())));
        		dlg.setText(JstUIMessages.COLOR_DIALOG_TITLE);

        		RGB rgb = dlg.open();
        		if (rgb != null) {
        			String colorStr = Util.createColorString(rgb);
        			colorCombo.setText(colorStr);
        		}
        	}
        });

        // =====================================================================================
        // Add BACKGROUND_IMAGE element
        // =====================================================================================
        label = new Label(this, SWT.LEFT);
        label.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
        label.setText(JstUIMessages.BACKGROUND_IMAGE);

        backgroundImageCombo = new Combo(this, SWT.BORDER);
        final GridData backgroundImageGridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
        backgroundImageCombo.setLayoutData(backgroundImageGridData);
        backgroundImageCombo.add(Constants.NONE);
        backgroundImageCombo.addModifyListener(new AttributeModifyListener(this, CSSConstants.BACKGROUND_IMAGE,
        		AttributeModifyListener.MODIFY_SIMPLE_ATTRIBUTE_FIELD) {
        	@Override
        	protected String adjustAttributeValue(String attribute) {
        		return adjustBackgroundURL(attribute);
        	}
        });

        button = new Button(this, SWT.PUSH);
        button.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
        button.setToolTipText(JstUIMessages.BACKGROUND_IMAGE);

        ImageDescriptor imageDesc = JspEditorPlugin.getImageDescriptor(Constants.IMAGE_FOLDERLARGE_FILE_LOCATION);
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
        		IAdaptable project = Util.getCurrentProject();
        		ImageSelectionDialog dialog = new ImageSelectionDialog(getShell(),
        				new WorkbenchLabelProvider(), new WorkbenchContentProvider());
        		dialog.setTitle(JstUIMessages.IMAGE_DIALOG_TITLE);
        		dialog.setMessage(JstUIMessages.IMAGE_DIALOG_MESSAGE);
        		dialog.setEmptyListMessage(JstUIMessages.IMAGE_DIALOG_EMPTY_MESSAGE);
        		dialog.setAllowMultiple(false);
        		dialog.setInput(project);

        		if (dialog.open() == ImageSelectionDialog.OK) {
        			IFile file = (IFile) dialog.getFirstResult();
        			String value = file.getFullPath().toString();
                    backgroundImageCombo.add(value);
                    value = adjustBackgroundURL(value);
                    backgroundImageCombo.setText(value);
        		}
        	}
        });

        // =====================================================================================
        // Add BACKGROUND_REPEAT element
        // =====================================================================================
        label = new Label(this, SWT.LEFT);
        label.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
        label.setText(JstUIMessages.BACKGROUND_REPEAT);

        GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData.horizontalSpan = 2;
        backgroundRepeatCombo = new Combo(this, SWT.BORDER);
        backgroundRepeatCombo.setLayoutData(gridData);
        backgroundRepeatCombo.addModifyListener(new AttributeModifyListener(this, CSSConstants.BACKGROUND_REPEAT,
        		AttributeModifyListener.MODIFY_SIMPLE_ATTRIBUTE_FIELD));
        ArrayList<String> list = comboMap.get(CSSConstants.BACKGROUND_REPEAT);
        for (String str : list) {
            backgroundRepeatCombo.add(str);
        }
    }

    /**
     * Method for get data in controls (if param equal true ), or set data (if
     * param equal false).
     *
     * @param param
     */
    public void updateData(boolean param) {
    	updateDataFromStyleAttributes = true;
        // set BACKGROUND_COLOR attribute
        String tmp = styleAttributes.getAttribute(CSSConstants.BACKGROUND_COLOR);
        if (!tmp.equals(colorCombo.getText())) {
            colorCombo.setText(tmp);
        }
        // set BACKGROUND_IMAGE attribute
        tmp = styleAttributes.getAttribute(CSSConstants.BACKGROUND_IMAGE);
        if (!tmp.equals(backgroundImageCombo.getText())) {
            backgroundImageCombo.setText(tmp);
        }
        // set BACKGROUND_REPEAT attribute
        tmp = styleAttributes.getAttribute(CSSConstants.BACKGROUND_REPEAT);
        if (!tmp.equals(backgroundRepeatCombo.getText())) {
            backgroundRepeatCombo.setText(tmp);
        }
        updateDataFromStyleAttributes = false;
    }

    // Fix for JBIDE-3084
    // in css background image should always be wrapped into url(*);
    private static String adjustBackgroundURL(String backgroundURL) {
        if ((backgroundURL != null && !backgroundURL.trim().equals(Constants.EMPTY))
        		&& (backgroundURL.matches("(url)\\(.*\\)") == false)) { //$NON-NLS-1$
            return "url(" + backgroundURL + ")"; //$NON-NLS-1$//$NON-NLS-2$
        }

        return backgroundURL;
    }
}
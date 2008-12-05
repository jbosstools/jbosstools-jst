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

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
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
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.messages.JstUIMessages;
import org.jboss.tools.jst.jsp.outline.cssdialog.FontFamilyDialog;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.CSSConstants;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Constants;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.ImageCombo;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Util;
import org.jboss.tools.jst.jsp.outline.cssdialog.events.AttributeModifyListener;
import org.jboss.tools.jst.jsp.outline.cssdialog.events.StyleAttributes;
import org.jboss.tools.jst.jsp.outline.cssdialog.parsers.ColorParser;

/**
 * Class for creating Text tab controls
 *
 * @author Igor Zhukov (izhukov@exadel.com)
 */
public class TabTextControl extends BaseTabControl {

    private static final int numColumns = 3;
    private Text fontFamilyText;
    private ImageCombo colorCombo;
    private Combo fontSizeCombo;
    private Combo extFontSizeCombo;
    private Combo fontStyleCombo;
    private Combo fontWeigthCombo;
    private Combo textDecorationCombo;
    private Combo textAlignCombo;
    private ArrayList<String> list;

    /**
     * Constructor for creating controls
     *
     * @param composite Composite element
     * @param comboMap
     * @param styleAttributes the StyleAttributes object
     */
    public TabTextControl(final Composite composite, final HashMap<String, ArrayList<String>> comboMap,
        final StyleAttributes styleAttributes) {
        super(composite, SWT.NONE);
        this.styleAttributes = styleAttributes;

        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = numColumns;
        setLayout(gridLayout);

        // =====================================================================================
        // Add FONT_FAMILY element
        // =====================================================================================
        Label label = new Label(this, SWT.LEFT);
        label.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
        label.setText(JstUIMessages.FONT_FAMILY);

        fontFamilyText = new Text(this, SWT.BORDER | SWT.SINGLE);
        fontFamilyText.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        fontFamilyText.addModifyListener(new AttributeModifyListener(this, CSSConstants.FONT_FAMILY,
        		AttributeModifyListener.MODIFY_SIMPLE_ATTRIBUTE_FIELD));
        Button button = new Button(this, SWT.PUSH);
        button.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
        button.setToolTipText(JstUIMessages.FONT_FAMILY_TIP);

        ImageDescriptor fontDesc = JspEditorPlugin.getImageDescriptor(Constants.IMAGE_FONTLARGE_FILE_LOCATION);
        Image fontImage = fontDesc.createImage();
        button.setImage(fontImage);
        button.addDisposeListener(new DisposeListener() {
                public void widgetDisposed(DisposeEvent e) {
                    Button button = (Button) e.getSource();
                    button.getImage().dispose();
                }
            });
        button.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent event) {
                    FontFamilyDialog dialog = new FontFamilyDialog(getShell(), fontFamilyText.getText());
                    if (dialog.open() == Window.OK) {
                        fontFamilyText.setText(dialog.getFontFamily());
                    }
                }
            });

        // =====================================================================================
        // Add COLOR element
        // =====================================================================================
        label = new Label(this, SWT.LEFT);
        label.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
        label.setText(JstUIMessages.COLOR);

        colorCombo = new ImageCombo(this, SWT.BORDER);
        colorCombo.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        colorCombo.addModifyListener(new AttributeModifyListener(this, CSSConstants.COLOR,
        		AttributeModifyListener.MODIFY_SIMPLE_ATTRIBUTE_FIELD));
        Set<Entry<String, String>> set = ColorParser.getInstance().getMap().entrySet();
        for (Map.Entry<String, String> me : set) {
            RGB rgb = Util.getColor(me.getKey());
            colorCombo.add(me.getValue(), rgb);
        }

        button = new Button(this, SWT.PUSH);
        button.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
        button.setToolTipText(JstUIMessages.COLOR_TIP);

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

                    dlg.setRGB((Util.getColor((colorCombo.getText().trim())) == null)
                        ? Constants.RGB_BLACK : Util.getColor((colorCombo.getText().trim())));
                    dlg.setText(JstUIMessages.COLOR_DIALOG_TITLE);

                    RGB rgb = dlg.open();
                    if (rgb != null) {
                        String colorStr = Util.createColorString(rgb);
                        colorCombo.setText(colorStr);
                    }
                }
            });

        // =====================================================================================
        // Add FONT_SIZE element
        // =====================================================================================
        label = new Label(this, SWT.LEFT);
        label.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
        label.setText(JstUIMessages.FONT_SIZE);

        // Create container that take up 2 cells and contains fontSizeCombo and extFontSizeCombo elements.
        // Is created for correct layout.
        Composite tmpComposite = getCompositeElement();
        fontSizeCombo = new Combo(tmpComposite, SWT.BORDER | SWT.SINGLE);
        fontSizeCombo.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        extFontSizeCombo = new Combo(tmpComposite, SWT.BORDER | SWT.READ_ONLY);
        extFontSizeCombo.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        extFontSizeCombo.addModifyListener(new AttributeModifyListener(this, fontSizeCombo, CSSConstants.FONT_SIZE,
        		AttributeModifyListener.MODIFY_COMBO_EXTENSION_ATTRIBUTE_FIELD));
        for (int i = 0; i < Constants.extSizes.length; i++) {
            extFontSizeCombo.add(Constants.extSizes[i]);
        }

        fontSizeCombo.addModifyListener(new AttributeModifyListener(this, extFontSizeCombo, CSSConstants.FONT_SIZE,
        		AttributeModifyListener.MODIFY_COMBO_ATTRIBUTE_FIELD_WITH_COMBO_EXTENSION));
        list = comboMap.get(CSSConstants.FONT_SIZE);
        for (String str : list) {
            fontSizeCombo.add(str);
        }

        // =====================================================================================
        // Add FONT_STYLE element
        // =====================================================================================
        label = new Label(this, SWT.LEFT);
        label.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
        label.setText(JstUIMessages.FONT_STYLE);

        GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData.horizontalSpan = 2;
        fontStyleCombo = new Combo(this, SWT.BORDER);
        fontStyleCombo.setLayoutData(gridData);
        fontStyleCombo.addModifyListener(new AttributeModifyListener(this, CSSConstants.FONT_STYLE,
        		AttributeModifyListener.MODIFY_SIMPLE_ATTRIBUTE_FIELD));
        list = comboMap.get(CSSConstants.FONT_STYLE);
        for (String str : list) {
            fontStyleCombo.add(str);
        }

        // =====================================================================================
        // Add FONT_WEIGHT element
        // =====================================================================================
        label = new Label(this, SWT.LEFT);
        label.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
        label.setText(JstUIMessages.FONT_WEIGHT);

        gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData.horizontalSpan = 2;
        fontWeigthCombo = new Combo(this, SWT.BORDER);
        fontWeigthCombo.setLayoutData(gridData);
        fontWeigthCombo.addModifyListener(new AttributeModifyListener(this, CSSConstants.FONT_WEIGHT,
        		AttributeModifyListener.MODIFY_SIMPLE_ATTRIBUTE_FIELD));
        list = comboMap.get(CSSConstants.FONT_WEIGHT);
        for (String str : list) {
            fontWeigthCombo.add(str);
        }

        // =====================================================================================
        // Add TEXT_DECORATION element
        // =====================================================================================
        label = new Label(this, SWT.LEFT);
        label.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
        label.setText(JstUIMessages.TEXT_DECORATION);

        gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData.horizontalSpan = 2;
        textDecorationCombo = new Combo(this, SWT.BORDER);
        textDecorationCombo.setLayoutData(gridData);
        textDecorationCombo.addModifyListener(new AttributeModifyListener(this, CSSConstants.TEXT_DECORATION,
        		AttributeModifyListener.MODIFY_SIMPLE_ATTRIBUTE_FIELD));
        list = comboMap.get(CSSConstants.TEXT_DECORATION);
        for (String str : list) {
            textDecorationCombo.add(str);
        }

        // =====================================================================================
        // Add TEXT_ALIGN element
        // =====================================================================================
        label = new Label(this, SWT.LEFT);
        label.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
        label.setText(JstUIMessages.TEXT_ALIGN);

        gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData.horizontalSpan = 2;
        textAlignCombo = new Combo(this, SWT.BORDER);
        textAlignCombo.setLayoutData(gridData);
        textAlignCombo.addModifyListener(new AttributeModifyListener(this, CSSConstants.TEXT_ALIGN,
        		AttributeModifyListener.MODIFY_SIMPLE_ATTRIBUTE_FIELD));
        list = comboMap.get(CSSConstants.TEXT_ALIGN);
        for (String str : list) {
            textAlignCombo.add(str);
        }
    }

    /**
     * Create container that take up 2 cells and contains fontSizeCombo and extFontSizeCombo elements.
     */
    private Composite getCompositeElement() {
        GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData.horizontalSpan = 2;
        GridLayout gridLayoutTmp = new GridLayout(2, false);
        gridLayoutTmp.marginHeight = 0;
        gridLayoutTmp.marginWidth = 0;
        Composite classComposite = new Composite(this, SWT.NONE);
        classComposite.setLayoutData(gridData);
        classComposite.setLayout(gridLayoutTmp);

        return classComposite;
    }

    /**
     * Method for get data in controls (if parameter equal true ), or set data (if parameter equal false).
     *
     * @param param
     */
    public void updateData(boolean param) {
    	updateDataFromStyleAttributes = true;
        // set FONT_FAMILY attribute
    	String tmp = styleAttributes.getAttribute(CSSConstants.FONT_FAMILY);
        if (!tmp.equals(fontFamilyText.getText())) {
        	fontFamilyText.setText(tmp);
        }
        // set COLOR attribute
    	tmp = styleAttributes.getAttribute(CSSConstants.COLOR);
        if (!tmp.equals(colorCombo.getText())) {
        	colorCombo.setText(tmp);
        }
        // set FONT_SIZE and EXTENSION_FONT_SIZE attributes
        tmp = styleAttributes.getAttribute(CSSConstants.FONT_SIZE);
        String[] str = Util.convertExtString(tmp);
        if (!str[0].equals(fontSizeCombo.getText())) {
        	fontSizeCombo.setText(str[0]);
            if (extFontSizeCombo.indexOf(str[1]) != -1) {
            	extFontSizeCombo.setText(str[1]);
            	extFontSizeCombo.select(extFontSizeCombo.indexOf(str[1]));
            }
        }
        // set TEXT_DECORATION attribute
    	tmp = styleAttributes.getAttribute(CSSConstants.TEXT_DECORATION);
        if (!tmp.equals(textDecorationCombo.getText())) {
        	textDecorationCombo.setText(tmp);
        }
        // set FONT_STYLE attribute
    	tmp = styleAttributes.getAttribute(CSSConstants.FONT_STYLE);
        if (!tmp.equals(fontStyleCombo.getText())) {
        	fontStyleCombo.setText(tmp);
        }
        // set FONT_WEIGHT attribute
    	tmp = styleAttributes.getAttribute(CSSConstants.FONT_WEIGHT);
        if (!tmp.equals(fontWeigthCombo.getText())) {
        	fontWeigthCombo.setText(tmp);
        }
        // set TEXT_ALIGN attribute
    	tmp = styleAttributes.getAttribute(CSSConstants.TEXT_ALIGN);
        if (!tmp.equals(textAlignCombo.getText())) {
        	textAlignCombo.setText(tmp);
        }
        updateDataFromStyleAttributes = false;
    }
}
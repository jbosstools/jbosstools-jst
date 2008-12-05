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
import org.eclipse.jface.resource.JFaceResources;
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
import org.jboss.tools.jst.jsp.outline.cssdialog.common.CSSConstants;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Constants;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.ImageCombo;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Util;
import org.jboss.tools.jst.jsp.outline.cssdialog.events.AttributeModifyListener;
import org.jboss.tools.jst.jsp.outline.cssdialog.events.StyleAttributes;
import org.jboss.tools.jst.jsp.outline.cssdialog.parsers.ColorParser;

/**
 * Class for creating control in Boxes tab
 *
 * @author Igor Zhukov (izhukov@exadel.com)
 */
public class TabBoxesControl extends BaseTabControl {

	private static final int numColumns = 3;
    private Combo extWidthCombo;
    private Combo extHeightCombo;
    private Combo borderWidthCombo;
    private Combo extPaddingCombo;
    private Combo extBorderWidthCombo;
    private Combo borderStyleCombo;
    private Combo extMarginCombo;
    private ImageCombo borderColorCombo;
    private Text widthText;
    private Text heightText;
    private Text marginText;
    private Text paddingText;

    /**
     * Constructor for creating controls
     *
     * @param composite Composite element
     * @param comboMap
     * @param styleAttributes the StyleAttributes object
     */
    public TabBoxesControl(final Composite composite, final HashMap<String, ArrayList<String>> comboMap,
        final StyleAttributes styleAttributes) {
        super(composite, SWT.NONE);
        this.styleAttributes = styleAttributes;

        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = numColumns;
        setLayout(gridLayout);

        Label label = new Label(this, SWT.NONE);
        label.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 3, 1));
        label.setFont(JFaceResources.getFontRegistry().get(JFaceResources.BANNER_FONT));
        label.setText(JstUIMessages.DIMENSION_TITLE);

        // =====================================================================================
        // Add WIDTH element
        // =====================================================================================
        label = new Label(this, SWT.LEFT);
        label.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
        label.setText(JstUIMessages.WIDTH);

        widthText = new Text(this, SWT.BORDER | SWT.SINGLE);
        widthText.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        extWidthCombo = new Combo(this, SWT.BORDER | SWT.READ_ONLY);
        extWidthCombo.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        widthText.addModifyListener(new AttributeModifyListener(this, extWidthCombo, CSSConstants.WIDTH,
        		AttributeModifyListener.MODIFY_SIMPLE_ATTRIBUTE_FIELD_WITH_COMBO_EXTENSION));
        extWidthCombo.addModifyListener(new AttributeModifyListener(this, widthText, CSSConstants.WIDTH,
        		AttributeModifyListener.MODIFY_COMBO_EXTENSION_ATTRIBUTE_FIELD));
        for (int i = 0; i < Constants.extSizes.length; i++) {
            extWidthCombo.add(Constants.extSizes[i]);
        }

        // =====================================================================================
        // Add HEIGHT element
        // =====================================================================================
        label = new Label(this, SWT.LEFT);
        label.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
        label.setText(JstUIMessages.HEIGHT);

        heightText = new Text(this, SWT.BORDER | SWT.SINGLE);
        heightText.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        extHeightCombo = new Combo(this, SWT.BORDER | SWT.READ_ONLY);
        extHeightCombo.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        heightText.addModifyListener(new AttributeModifyListener(this, extHeightCombo, CSSConstants.HEIGHT,
        		AttributeModifyListener.MODIFY_SIMPLE_ATTRIBUTE_FIELD_WITH_COMBO_EXTENSION));
        extHeightCombo.addModifyListener(new AttributeModifyListener(this, heightText, CSSConstants.HEIGHT,
        		AttributeModifyListener.MODIFY_COMBO_EXTENSION_ATTRIBUTE_FIELD));
        for (int i = 0; i < Constants.extSizes.length; i++) {
            extHeightCombo.add(Constants.extSizes[i]);
        }

        label = new Label(this, SWT.NONE);
        label.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 3, 1));
        label.setFont(JFaceResources.getFontRegistry().get(JFaceResources.BANNER_FONT));
        label.setText(JstUIMessages.BORDER_TITLE);

        // =====================================================================================
        // Add BORDER_STYLE element
        // =====================================================================================
        label = new Label(this, SWT.LEFT);
        label.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
        label.setText(JstUIMessages.BORDER_STYLE);

        GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData.horizontalSpan = 2;
        borderStyleCombo = new Combo(this, SWT.BORDER);
        borderStyleCombo.setLayoutData(gridData);
        borderStyleCombo.addModifyListener(new AttributeModifyListener(this, CSSConstants.BORDER_STYLE,
        		AttributeModifyListener.MODIFY_SIMPLE_ATTRIBUTE_FIELD));
        ArrayList<String> list = comboMap.get(CSSConstants.BORDER_STYLE);
        for (String str : list) {
            borderStyleCombo.add(str);
        }

        // =====================================================================================
        // Add BORDER_COLOR element
        // =====================================================================================
        label = new Label(this, SWT.LEFT);
        label.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
        label.setText(JstUIMessages.BORDER_COLOR);

        Composite tmpComposite = getCompositeElement();
        borderColorCombo = new ImageCombo(tmpComposite, SWT.BORDER);
        borderColorCombo.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        borderColorCombo.addModifyListener(new AttributeModifyListener(this, CSSConstants.BORDER_COLOR,
        		AttributeModifyListener.MODIFY_SIMPLE_ATTRIBUTE_FIELD));
        Set<Entry<String, String>> set = ColorParser.getInstance().getMap().entrySet();
        for (Map.Entry<String, String> me : set) {
            RGB rgb = Util.getColor(me.getKey());
            borderColorCombo.add(me.getValue(), rgb);
        }

        final Button button = new Button(tmpComposite, SWT.PUSH);
        button.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
        button.setToolTipText(JstUIMessages.BORDER_COLOR_TIP);

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
                    dlg.setRGB((Util.getColor((borderColorCombo.getText().trim())) == null)
                        ? Constants.RGB_BLACK : Util.getColor((borderColorCombo.getText().trim())));
                    dlg.setText(JstUIMessages.COLOR_DIALOG_TITLE);
                    RGB rgb = dlg.open();
                    if (rgb != null) {
                        String colorStr = Util.createColorString(rgb);
                        borderColorCombo.setText(colorStr);
                    }
                }
            });

        // =====================================================================================
        // Add BORDER_WIDTH element
        // =====================================================================================
        label = new Label(this, SWT.LEFT);
        label.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
        label.setText(JstUIMessages.BORDER_WIDTH);

        borderWidthCombo = new Combo(this, SWT.BORDER | SWT.SINGLE);
        borderWidthCombo.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        list = comboMap.get(CSSConstants.BORDER_WIDTH);
        for (String str : list) {
            borderWidthCombo.add(str);
        }
        extBorderWidthCombo = new Combo(this, SWT.BORDER | SWT.READ_ONLY);
        extBorderWidthCombo.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        borderWidthCombo.addModifyListener(new AttributeModifyListener(this, extBorderWidthCombo,
        		CSSConstants.BORDER_WIDTH, AttributeModifyListener.MODIFY_COMBO_ATTRIBUTE_FIELD_WITH_COMBO_EXTENSION));
        extBorderWidthCombo.addModifyListener(new AttributeModifyListener(this, borderWidthCombo,
        		CSSConstants.BORDER_WIDTH, AttributeModifyListener.MODIFY_COMBO_EXTENSION_ATTRIBUTE_FIELD));
        for (int i = 0; i < Constants.extSizes.length; i++) {
            extBorderWidthCombo.add(Constants.extSizes[i]);
        }

        label = new Label(this, SWT.NONE);
        label.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 3, 1));
        label.setFont(JFaceResources.getFontRegistry().get(JFaceResources.BANNER_FONT));
        label.setText(JstUIMessages.MARGIN_PADDING_TITLE);

        // =====================================================================================
        // Add MARGIN element
        // =====================================================================================
        label = new Label(this, SWT.LEFT);
        label.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
        label.setText(JstUIMessages.MARGIN);

        marginText = new Text(this, SWT.BORDER | SWT.SINGLE);
        marginText.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        extMarginCombo = new Combo(this, SWT.BORDER | SWT.READ_ONLY);
        extMarginCombo.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        marginText.addModifyListener(new AttributeModifyListener(this, extMarginCombo, CSSConstants.MARGIN,
        		AttributeModifyListener.MODIFY_SIMPLE_ATTRIBUTE_FIELD_WITH_COMBO_EXTENSION));
        extMarginCombo.addModifyListener(new AttributeModifyListener(this, marginText, CSSConstants.MARGIN,
        		AttributeModifyListener.MODIFY_COMBO_EXTENSION_ATTRIBUTE_FIELD));
        for (int i = 0; i < Constants.extSizes.length; i++) {
            extMarginCombo.add(Constants.extSizes[i]);
        }

        // =====================================================================================
        // Add PADDING element
        // =====================================================================================
        label = new Label(this, SWT.LEFT);
        label.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
        label.setText(JstUIMessages.PADDING);

        paddingText = new Text(this, SWT.BORDER | SWT.SINGLE);
        paddingText.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        extPaddingCombo = new Combo(this, SWT.BORDER | SWT.READ_ONLY);
        extPaddingCombo.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        paddingText.addModifyListener(new AttributeModifyListener(this, extPaddingCombo, CSSConstants.PADDING,
        		AttributeModifyListener.MODIFY_SIMPLE_ATTRIBUTE_FIELD_WITH_COMBO_EXTENSION));
        extPaddingCombo.addModifyListener(new AttributeModifyListener(this, paddingText, CSSConstants.PADDING,
        		AttributeModifyListener.MODIFY_COMBO_EXTENSION_ATTRIBUTE_FIELD));
        for (int i = 0; i < Constants.extSizes.length; i++) {
            extPaddingCombo.add(Constants.extSizes[i]);
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
     * Method for get data in controls (if parameter equals true ), or set data (if parameter equals false).
     *
     * @param param
     */
    public void updateData(boolean param) {
        updateDataFromStyleAttributes = true;
        // set WIDTH and EXTENSION_WIDTH attributes
        String tmp = styleAttributes.getAttribute(CSSConstants.WIDTH);
        String[] str = Util.convertExtString(tmp);
        if (!str[0].equals(widthText.getText())) {
            widthText.setText(str[0]);
            if (extWidthCombo.indexOf(str[1]) != -1) {
                extWidthCombo.setText(str[1]);
                extWidthCombo.select(extWidthCombo.indexOf(str[1]));
            }
        }
        // set HEIGHT and EXTENSION_HEIGHT attributes
        tmp = styleAttributes.getAttribute(CSSConstants.HEIGHT);
        str = Util.convertExtString(tmp);
        if (!str[0].equals(heightText.getText())) {
        	heightText.setText(str[0]);
            if (extHeightCombo.indexOf(str[1]) != -1) {
            	extHeightCombo.setText(str[1]);
            	extHeightCombo.select(extHeightCombo.indexOf(str[1]));
            }
        }
        // set BORDER_STYLE attribute
        tmp = styleAttributes.getAttribute(CSSConstants.BORDER_STYLE);
        if (!tmp.equals(borderStyleCombo.getText())) {
        	borderStyleCombo.setText(tmp);
        }
        // set BORDER_COLOR attribute
        tmp = styleAttributes.getAttribute(CSSConstants.BORDER_COLOR);
        if (!tmp.equals(borderColorCombo.getText())) {
        	borderColorCombo.setText(tmp);
        }
        // set BORDER_WIDTH and EXTENSION_BORDER_WIDTH attributes
        tmp = styleAttributes.getAttribute(CSSConstants.BORDER_WIDTH);
        str = Util.convertExtString(tmp);
        if (!str[0].equals(borderWidthCombo.getText())) {
        	borderWidthCombo.setText(str[0]);
            if (extBorderWidthCombo.indexOf(str[1]) != -1) {
            	extBorderWidthCombo.setText(str[1]);
            	extBorderWidthCombo.select(extBorderWidthCombo.indexOf(str[1]));
            }
        }
        // set MARGIN and EXTENSION_MARGIN attributes
        tmp = styleAttributes.getAttribute(CSSConstants.MARGIN);
        str = Util.convertExtString(tmp);
        if (!str[0].equals(marginText.getText())) {
        	marginText.setText(str[0]);
            if (extMarginCombo.indexOf(str[1]) != -1) {
            	extMarginCombo.setText(str[1]);
            	extMarginCombo.select(extMarginCombo.indexOf(str[1]));
            }
        }
        // set PADDING and EXTENSION_PADDING attributes
        tmp = styleAttributes.getAttribute(CSSConstants.PADDING);
        str = Util.convertExtString(tmp);
        if (!str[0].equals(paddingText.getText())) {
        	paddingText.setText(str[0]);
            if (extPaddingCombo.indexOf(str[1]) != -1) {
            	extPaddingCombo.setText(str[1]);
            	extPaddingCombo.select(extPaddingCombo.indexOf(str[1]));
            }
        }
        updateDataFromStyleAttributes = false;
    }
}
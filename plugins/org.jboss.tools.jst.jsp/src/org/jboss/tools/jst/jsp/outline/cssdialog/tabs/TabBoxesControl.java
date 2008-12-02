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
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.messages.JstUIMessages;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.CSSConstants;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Constants;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.ImageCombo;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Util;
import org.jboss.tools.jst.jsp.outline.cssdialog.events.ChangeStyleEvent;
import org.jboss.tools.jst.jsp.outline.cssdialog.events.ManualChangeStyleListener;
import org.jboss.tools.jst.jsp.outline.cssdialog.events.StyleAttributes;
import org.jboss.tools.jst.jsp.outline.cssdialog.parsers.ColorParser;


/**
 * Class for creating control in Box tab
 *
 * @author dsakovich@exadel.com
 */
public class TabBoxesControl extends Composite {

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
    private ArrayList<String> list;
    private StyleAttributes styleAttributes;

    private ArrayList<ManualChangeStyleListener> listeners = new ArrayList<ManualChangeStyleListener>();
    private boolean updateDataFromStyleAttributes = false;

    /**
     * Constructor for creating controls
     *
     * @param composite
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
        widthText.addModifyListener(new ModifyListener() {
                public void modifyText(ModifyEvent event) {
                    String tmp = widthText.getText();
                    if (tmp != null && tmp.trim().length() > 0) {
                    	String extWidth = extWidthCombo.getText();
                    	if (extWidth != null) {
                    		tmp += extWidth;
                    	}
                    	styleAttributes.addAttribute(CSSConstants.WIDTH, tmp);
                    } else {
                        styleAttributes.removeAttribute(CSSConstants.WIDTH);
                        extWidthCombo.select(0);
                    }
                    if (!updateDataFromStyleAttributes) {
                    	notifyListeners();
                    }
                }
            });
        extWidthCombo = new Combo(this, SWT.BORDER | SWT.READ_ONLY);
        extWidthCombo.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        extWidthCombo.addModifyListener(new ModifyListener() {
                public void modifyText(ModifyEvent event) {
                    String width = widthText.getText();
                    if (width != null && !width.trim().equals(Constants.EMPTY)) {
                        String tmp = extWidthCombo.getText();
                        if (tmp != null) {
                            styleAttributes.addAttribute(CSSConstants.WIDTH, width + tmp);
                        }
                    } else {
                    	extWidthCombo.select(0);
                    	return;
                    }
                    if (!updateDataFromStyleAttributes) {
                    	notifyListeners();
                    }
                }
            });
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
        heightText.addModifyListener(new ModifyListener() {
                public void modifyText(ModifyEvent event) {
                    String tmp = heightText.getText();
                    if (tmp != null && tmp.trim().length() > 0) {
                    	String extHeight = extHeightCombo.getText();
                    	if (extHeight != null) {
                    		tmp += extHeight;
                    	}
                    	styleAttributes.addAttribute(CSSConstants.HEIGHT, tmp);
                    } else {
                        styleAttributes.removeAttribute(CSSConstants.HEIGHT);
                        extHeightCombo.select(0);
                    }
                    if (!updateDataFromStyleAttributes) {
                    	notifyListeners();
                    }
                }
            });
        extHeightCombo = new Combo(this, SWT.BORDER | SWT.READ_ONLY);
        extHeightCombo.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        extHeightCombo.addModifyListener(new ModifyListener() {
                public void modifyText(ModifyEvent event) {
                    String height = heightText.getText();
                    if (height != null && !height.trim().equals(Constants.EMPTY)) {
                        String tmp = extHeightCombo.getText();
                        if (tmp != null) {
                            styleAttributes.addAttribute(CSSConstants.HEIGHT, height + tmp);
                        }
                    } else {
                    	extHeightCombo.select(0);
                    	return;
                    }
                    if (!updateDataFromStyleAttributes) {
                    	notifyListeners();
                    }
                }
            });
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
        borderStyleCombo.addModifyListener(new ModifyListener() {
                public void modifyText(ModifyEvent event) {
                    String tmp = borderStyleCombo.getText();
                    if (tmp != null && !tmp.trim().equals(Constants.EMPTY)) {
                    	styleAttributes.addAttribute(CSSConstants.BORDER_STYLE, tmp.trim());
                    } else {
                    	styleAttributes.removeAttribute(CSSConstants.BORDER_STYLE);
                    }
                    if (!updateDataFromStyleAttributes) {
                    	notifyListeners();
                    }
                }
            });
        list = comboMap.get(CSSConstants.BORDER_STYLE);
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
        borderColorCombo.addModifyListener(new ModifyListener() {
                public void modifyText(ModifyEvent event) {
                    String tmp = borderColorCombo.getText().trim();
                    if (tmp != null && !tmp.trim().equals(Constants.EMPTY)) {
                    	styleAttributes.addAttribute(CSSConstants.BORDER_COLOR, tmp.trim());
                    } else {
                    	styleAttributes.removeAttribute(CSSConstants.BORDER_COLOR);
                    }
                    if (!updateDataFromStyleAttributes) {
                    	notifyListeners();
                    }
                }
            });
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
        borderWidthCombo.addModifyListener(new ModifyListener() {
                public void modifyText(ModifyEvent e) {
                    String currentText = borderWidthCombo.getText();
                    list = comboMap.get(CSSConstants.BORDER_WIDTH);
                    for (String str : list) {
                        if (currentText.equals(str)) {
                            extBorderWidthCombo.select(0);
                            extBorderWidthCombo.setEnabled(false);
                            styleAttributes.addAttribute(CSSConstants.BORDER_WIDTH, currentText);
                            if (!updateDataFromStyleAttributes) {
                            	notifyListeners();
                            }
                            return;
                        }
                    }
                    extBorderWidthCombo.setEnabled(true);
                    String tmp = borderWidthCombo.getText();
                    if (tmp != null && tmp.trim().length() > 0) {
                    	String extBorderWidth = extBorderWidthCombo.getText();
                    	if (extBorderWidth != null) {
                    		tmp += extBorderWidth;
                    	}
                    	styleAttributes.addAttribute(CSSConstants.BORDER_WIDTH, tmp);
                    } else {
                        styleAttributes.removeAttribute(CSSConstants.BORDER_WIDTH);
                        extBorderWidthCombo.select(0);
                    }
                    if (!updateDataFromStyleAttributes) {
                    	notifyListeners();
                    }
                }
            });
        list = comboMap.get(CSSConstants.BORDER_WIDTH);
        for (String str : list) {
            borderWidthCombo.add(str);
        }
        extBorderWidthCombo = new Combo(this, SWT.BORDER | SWT.READ_ONLY);
        extBorderWidthCombo.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        extBorderWidthCombo.addModifyListener(new ModifyListener() {
                public void modifyText(ModifyEvent event) {
                    String borderWidth = borderWidthCombo.getText();
                    if (borderWidth != null && !borderWidth.trim().equals(Constants.EMPTY)) {
                        String tmp = extBorderWidthCombo.getText();
                        if (tmp != null) {
                            styleAttributes.addAttribute(CSSConstants.BORDER_WIDTH, borderWidth + tmp);
                        }
                    } else {
                    	extBorderWidthCombo.select(0);
                    	return;
                    }
                    if (!updateDataFromStyleAttributes) {
                    	notifyListeners();
                    }
                }
            });
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
        marginText.addModifyListener(new ModifyListener() {
                public void modifyText(ModifyEvent event) {
                    String tmp = marginText.getText();
                    if (tmp != null && tmp.trim().length() > 0) {
                    	String extMargin = extMarginCombo.getText();
                    	if (extMargin != null) {
                    		tmp += extMargin;
                    	}
                    	styleAttributes.addAttribute(CSSConstants.MARGIN, tmp);
                    } else {
                        styleAttributes.removeAttribute(CSSConstants.MARGIN);
                        extMarginCombo.select(0);
                    }
                    if (!updateDataFromStyleAttributes) {
                    	notifyListeners();
                    }
                }
            });
        extMarginCombo = new Combo(this, SWT.BORDER | SWT.READ_ONLY);
        extMarginCombo.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        extMarginCombo.addModifyListener(new ModifyListener() {
                public void modifyText(ModifyEvent event) {
                    String margin = marginText.getText();
                    if (margin != null && !margin.trim().equals(Constants.EMPTY)) {
                        String tmp = extMarginCombo.getText();
                        if (tmp != null) {
                            styleAttributes.addAttribute(CSSConstants.MARGIN, margin + tmp);
                        }
                    } else {
                    	extMarginCombo.select(0);
                    	return;
                    }
                    if (!updateDataFromStyleAttributes) {
                    	notifyListeners();
                    }
                }
            });
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
        paddingText.addModifyListener(new ModifyListener() {
                public void modifyText(ModifyEvent event) {
                    String tmp = paddingText.getText();
                    if (tmp != null && tmp.trim().length() > 0) {
                    	String extPadding = extPaddingCombo.getText();
                    	if (extPadding != null) {
                    		tmp += extPadding;
                    	}
                    	styleAttributes.addAttribute(CSSConstants.PADDING, tmp);
                    } else {
                        styleAttributes.removeAttribute(CSSConstants.PADDING);
                        extPaddingCombo.select(0);
                    }
                    if (!updateDataFromStyleAttributes) {
                    	notifyListeners();
                    }
                }
            });
        extPaddingCombo = new Combo(this, SWT.BORDER | SWT.READ_ONLY);
        extPaddingCombo.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        extPaddingCombo.addModifyListener(new ModifyListener() {
                public void modifyText(ModifyEvent event) {
                    String padding = paddingText.getText();
                    if (padding != null && !padding.trim().equals(Constants.EMPTY)) {
                        String tmp = extPaddingCombo.getText();
                        if (tmp != null) {
                            styleAttributes.addAttribute(CSSConstants.PADDING, padding + tmp);
                        }
                    } else {
                        extPaddingCombo.select(0);
                    	return;
                    }
                    if (!updateDataFromStyleAttributes) {
                    	notifyListeners();
                    }
                }
            });
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
        Composite classComposite = new Composite(this, SWT.CENTER);
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

    /**
     * Add ManualChangeStyleListener object.
     *
     * @param listener ManualChangeStyleListener object to be added
     */
    public void addManualChangeStyleListener(ManualChangeStyleListener listener) {
        listeners.add(listener);
    }

    /**
     * Gets an array of ChangeStyleListener object.
     *
     * @return an array of ChangeStyleListener object
     */
    public ManualChangeStyleListener[] getManualChangeStyleListeners() {
        return listeners.toArray(new ManualChangeStyleListener[listeners.size()]);
    }

    /**
     * Remove ManualChangeStyleListener object passed by parameter.
     *
     * @param listener ManualChangeStyleListener object to be removed
     */
    public void removeManualChangeStyleListener(ManualChangeStyleListener listener) {
        listeners.remove(listener);
    }

    /**
     * Method is used to notify all subscribed listeners about any changes within style attribute map.
     */
    private void notifyListeners() {
        ChangeStyleEvent event = new ChangeStyleEvent(this);
        for (ManualChangeStyleListener listener : listeners) {
            listener.styleChanged(event);
        }
    }
}
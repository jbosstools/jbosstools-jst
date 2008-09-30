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
import org.jboss.tools.jst.jsp.outline.cssdialog.common.CSSConstants;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Constants;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.ImageCombo;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.MessageUtil;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Util;
import org.jboss.tools.jst.jsp.outline.cssdialog.events.StyleAttributes;
import org.jboss.tools.jst.jsp.outline.cssdialog.parsers.ColorParser;

/**
 * Class for creating control in Box tab
 * 
 * @author dsakovich@exadel.com
 * 
 */
public class TabBoxesControl extends Composite {

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

    private static final int numColumns = 3;

    // TODO Dzmitry Sakovich
    // private CSSDialog cssDialog;

    /**
     * Constructor for creating controls
     * 
     * @param composite
     */
    public TabBoxesControl(final Composite composite,
	    final HashMap<String, ArrayList<String>> comboMap,
	    final StyleAttributes styleAttributes) {
	super(composite, SWT.NONE);
	this.styleAttributes = styleAttributes;

	// TODO Dzmitry Sakovich
	// this.cssDialog = dialog;

	final GridLayout gridLayout = new GridLayout();
	gridLayout.numColumns = numColumns;
	setLayout(gridLayout);

	Label label = new Label(this, SWT.NONE);
	label.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER,
		false, false, 3, 1));

	label.setFont(JFaceResources.getFontRegistry().get(
		JFaceResources.BANNER_FONT));
	label.setText(MessageUtil.getString("DIMENSION_TITLE")); //$NON-NLS-1$
	label = new Label(this, SWT.LEFT);
	label.setText(MessageUtil.getString("WIDTH")); //$NON-NLS-1$
	label.setLayoutData(new GridData(GridData.END, GridData.CENTER, false,
		false));

	widthText = new Text(this, SWT.BORDER | SWT.SINGLE);

	widthText.addModifyListener(new ModifyListener() {
	    public void modifyText(ModifyEvent event) {

		String tmp = widthText.getText();

		if (tmp != null) {
		    if (tmp.trim().length() > 0) {
			String extWidth = extWidthCombo.getText().trim();
			if (extWidth != null)
			    styleAttributes.addAttribute(CSSConstants.WIDTH,
				    tmp + extWidth);
			else
			    styleAttributes.addAttribute(CSSConstants.WIDTH,
				    tmp);
		    } else
			styleAttributes.removeAttribute(CSSConstants.WIDTH);
		} else
		    styleAttributes.removeAttribute(CSSConstants.WIDTH);

		// TODO Dzmitry Sakovich
		// cssDialog.setStyleForPreview();
	    }
	});

	widthText.setLayoutData(new GridData(GridData.FILL, GridData.CENTER,
		true, false));

	extWidthCombo = new Combo(this, SWT.BORDER | SWT.READ_ONLY);

	extWidthCombo.addModifyListener(new ModifyListener() {
	    public void modifyText(ModifyEvent event) {
		String width = widthText.getText().trim();
		if (width == null)
		    return;
		if (width.equals(Constants.EMPTY_STRING))
		    return;

		String tmp = extWidthCombo.getText();

		if (tmp != null) {
		    styleAttributes.addAttribute(CSSConstants.WIDTH, width
			    + tmp);
		    // TODO Dzmitry Sakovich
		    // cssDialog.setStyleForPreview();
		}
	    }
	});

	extWidthCombo.setLayoutData(new GridData(GridData.FILL,
		GridData.CENTER, false, false));
	for (int i = 0; i < Constants.extSizes.length; i++) {
	    extWidthCombo.add(Constants.extSizes[i]);
	}

	label = new Label(this, SWT.LEFT);
	label.setText(MessageUtil.getString("HEIGHT")); //$NON-NLS-1$
	label.setLayoutData(new GridData(GridData.END, GridData.CENTER, false,
		false));

	heightText = new Text(this, SWT.BORDER | SWT.SINGLE);

	heightText.addModifyListener(new ModifyListener() {
	    public void modifyText(ModifyEvent event) {
		String tmp = heightText.getText();

		if (tmp != null) {
		    if (tmp.trim().length() > 0) {
			String extHeight = extHeightCombo.getText().trim();
			if (extHeight != null)
			    styleAttributes.addAttribute(CSSConstants.HEIGHT,
				    tmp + extHeight);
			else
			    styleAttributes.addAttribute(CSSConstants.HEIGHT,
				    tmp);
		    } else
			styleAttributes.removeAttribute(CSSConstants.HEIGHT);
		} else
		    styleAttributes.removeAttribute(CSSConstants.HEIGHT);
		// TODO Dzmitry Sakovich
		// cssDialog.setStyleForPreview();
	    }
	});

	heightText.setLayoutData(new GridData(GridData.FILL, GridData.CENTER,
		true, false));

	extHeightCombo = new Combo(this, SWT.BORDER | SWT.READ_ONLY);

	extHeightCombo.addModifyListener(new ModifyListener() {
	    public void modifyText(ModifyEvent event) {
		String height = heightText.getText().trim();
		if (height == null)
		    return;
		if (height.equals(Constants.EMPTY_STRING))
		    return;

		String tmp = extHeightCombo.getText();

		if (tmp != null) {
		    styleAttributes.addAttribute(CSSConstants.HEIGHT, height
			    + tmp);
		    // TODO Dzmitry Sakovich
		    // cssDialog.setStyleForPreview();
		}
	    }
	});

	extHeightCombo.setLayoutData(new GridData(GridData.FILL,
		GridData.CENTER, false, false));
	for (int i = 0; i < Constants.extSizes.length; i++) {
	    extHeightCombo.add(Constants.extSizes[i]);
	}

	label = new Label(this, SWT.NONE);
	label.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER,
		false, false, 3, 1));
	label.setFont(JFaceResources.getFontRegistry().get(
		JFaceResources.BANNER_FONT));
	label.setText(MessageUtil.getString("BORDER_TITLE")); //$NON-NLS-1$

	label = new Label(this, SWT.LEFT);
	label.setLayoutData(new GridData(GridData.END, GridData.CENTER, false,
		false));
	label.setText(MessageUtil.getString("BORDER_STYLE")); //$NON-NLS-1$

	borderStyleCombo = new Combo(this, SWT.BORDER);

	borderStyleCombo.addModifyListener(new ModifyListener() {

	    public void modifyText(ModifyEvent event) {
		String tmp = borderStyleCombo.getText().trim();
		if (tmp != null)
		    if (!tmp.equals(Constants.EMPTY_STRING)) {
			styleAttributes.addAttribute(CSSConstants.BORDER_STYLE,
				tmp);
			// TODO Dzmitry Sakovich
			// cssDialog.setStyleForPreview();
			return;
		    }
		styleAttributes.removeAttribute(CSSConstants.BORDER_STYLE);
		// TODO Dzmitry Sakovich
		// cssDialog.setStyleForPreview();
	    }
	});

	borderStyleCombo.setLayoutData(new GridData(GridData.FILL,
		GridData.CENTER, false, false));
	list = comboMap.get(CSSConstants.BORDER_STYLE);
	for (String str : list) {
	    borderStyleCombo.add(str);
	}

	label = new Label(this, SWT.LEFT);
	label.setLayoutData(new GridData(GridData.END, GridData.CENTER, false,
		false));
	label.setText(Constants.EMPTY_STRING);

	label = new Label(this, SWT.LEFT);
	label.setLayoutData(new GridData(GridData.END, GridData.CENTER, false,
		false));
	label.setText(MessageUtil.getString("BORDER_COLOR")); //$NON-NLS-1$

	borderColorCombo = new ImageCombo(this, SWT.BORDER);

	borderColorCombo.addModifyListener(new ModifyListener() {

	    public void modifyText(ModifyEvent event) {
		String tmp = borderColorCombo.getText().trim();
		if (tmp != null)
		    if (!tmp.equals(Constants.EMPTY_STRING)) {
			styleAttributes.addAttribute(CSSConstants.BORDER_COLOR,
				tmp);

			// TODO Dzmitry Sakovich
			// cssDialog.setStyleForPreview();
			return;
		    }
		styleAttributes.removeAttribute(CSSConstants.BORDER_COLOR);
		// TODO Dzmitry Sakovich
		// cssDialog.setStyleForPreview();
	    }
	});

	borderColorCombo.setLayoutData(new GridData(GridData.FILL,
		GridData.CENTER, true, false));
	Set<Entry<String, String>> set = ColorParser.getInstance().getMap()
		.entrySet();

	for (Map.Entry<String, String> me : set) {
	    RGB rgb = Util.getColor(me.getKey());
	    borderColorCombo.add(me.getValue(), rgb);
	}

	final Button button = new Button(this, SWT.PUSH);
	button.setLayoutData(new GridData(GridData.END, GridData.CENTER, false,
		false));
	button.setToolTipText(MessageUtil.getString("BORDER_COLOR_TIP")); //$NON-NLS-1$
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
			.setRGB(Util.getColor((borderColorCombo.getText()
				.trim())) == null ? Constants.RGB_BLACK : Util
				.getColor((borderColorCombo.getText().trim())));
		dlg.setText(MessageUtil.getString("COLOR_DIALOG_TITLE")); //$NON-NLS-1$
		RGB rgb = dlg.open();
		if (rgb != null) {
		    String colorStr = Util.createColorString(rgb);
		    borderColorCombo.setText(colorStr);
		}
	    }
	});

	label = new Label(this, SWT.LEFT);
	label.setText(MessageUtil.getString("BORDER_WIDTH")); //$NON-NLS-1$
	label.setLayoutData(new GridData(GridData.END, GridData.CENTER, false,
		false));

	borderWidthCombo = new Combo(this, SWT.BORDER | SWT.SINGLE);

	borderWidthCombo.addModifyListener(new ModifyListener() {

	    public void modifyText(ModifyEvent e) {
		String currentText = borderWidthCombo.getText();

		list = comboMap.get(CSSConstants.BORDER_WIDTH);
		for (String str : list) {
		    if (currentText.equals(str)) {
			extBorderWidthCombo.select(0);
			extBorderWidthCombo.setEnabled(false);
			styleAttributes.addAttribute(CSSConstants.BORDER_WIDTH,
				currentText);
			// TODO Dzmitry Sakovich
			// cssDialog.setStyleForPreview();
			return;
		    }
		}
		extBorderWidthCombo.setEnabled(true);

		String tmp = borderWidthCombo.getText();

		if (tmp != null) {
		    if (tmp.trim().length() > 0) {
			String extBorderWidth = extBorderWidthCombo.getText()
				.trim();
			if (extBorderWidth != null)
			    styleAttributes.addAttribute(
				    CSSConstants.BORDER_WIDTH, tmp
					    + extBorderWidth);
			else
			    styleAttributes.addAttribute(
				    CSSConstants.BORDER_WIDTH, tmp);
		    } else
			styleAttributes
				.removeAttribute(CSSConstants.BORDER_WIDTH);
		} else
		    styleAttributes.removeAttribute(CSSConstants.BORDER_WIDTH);

		// TODO Dzmitry Sakovich
		// cssDialog.setStyleForPreview();
	    }
	});

	borderWidthCombo.setLayoutData(new GridData(GridData.FILL,
		GridData.CENTER, true, false));

	list = comboMap.get(CSSConstants.BORDER_WIDTH);
	for (String str : list) {
	    borderWidthCombo.add(str);
	}

	extBorderWidthCombo = new Combo(this, SWT.BORDER | SWT.READ_ONLY);

	extBorderWidthCombo.addModifyListener(new ModifyListener() {
	    public void modifyText(ModifyEvent event) {
		String borderWidth = borderWidthCombo.getText().trim();
		if (borderWidth == null)
		    return;
		if (borderWidth.equals(Constants.EMPTY_STRING))
		    return;

		String tmp = extBorderWidthCombo.getText();

		if (tmp != null) {
		    styleAttributes.addAttribute(CSSConstants.BORDER_WIDTH,
			    borderWidth + tmp);
		    // TODO Dzmitry Sakovich
		    // cssDialog.setStyleForPreview();
		}
	    }
	});

	extBorderWidthCombo.setLayoutData(new GridData(GridData.FILL,
		GridData.CENTER, false, false));
	for (int i = 0; i < Constants.extSizes.length; i++) {
	    extBorderWidthCombo.add(Constants.extSizes[i]);
	}

	borderWidthCombo.addModifyListener(new ModifyListener() {

	    public void modifyText(ModifyEvent e) {
		String currentText = borderWidthCombo.getText();

		list = comboMap.get(CSSConstants.BORDER_WIDTH);
		for (String str : list) {
		    if (currentText.equals(str)) {
			extBorderWidthCombo.select(0);
			extBorderWidthCombo.setEnabled(false);
			return;
		    }
		    extBorderWidthCombo.setEnabled(true);
		}
	    }
	});

	label = new Label(this, SWT.NONE);
	label.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER,
		false, false, 3, 1));
	label.setFont(JFaceResources.getFontRegistry().get(
		JFaceResources.BANNER_FONT));
	label.setText(MessageUtil.getString("MARGIN_PADDING_TITLE")); //$NON-NLS-1$

	label = new Label(this, SWT.LEFT);
	label.setText(MessageUtil.getString("MARGIN")); //$NON-NLS-1$
	label.setLayoutData(new GridData(GridData.END, GridData.CENTER, false,
		false));

	marginText = new Text(this, SWT.BORDER | SWT.SINGLE);

	marginText.addModifyListener(new ModifyListener() {
	    public void modifyText(ModifyEvent event) {

		String tmp = marginText.getText();

		if (tmp != null) {
		    if (tmp.trim().length() > 0) {
			String extMargin = extMarginCombo.getText().trim();
			if (extMargin != null)
			    styleAttributes.addAttribute(CSSConstants.MARGIN,
				    tmp + extMargin);
			else
			    styleAttributes.addAttribute(CSSConstants.MARGIN,
				    tmp);
		    } else
			styleAttributes.removeAttribute(CSSConstants.MARGIN);
		} else
		    styleAttributes.removeAttribute(CSSConstants.MARGIN);
		// TODO Dzmitry Sakovich
		// cssDialog.setStyleForPreview();
	    }
	});

	marginText.setLayoutData(new GridData(GridData.FILL, GridData.CENTER,
		true, false));

	extMarginCombo = new Combo(this, SWT.BORDER | SWT.READ_ONLY);

	extMarginCombo.addModifyListener(new ModifyListener() {
	    public void modifyText(ModifyEvent event) {
		String margin = marginText.getText().trim();
		if (margin == null)
		    return;
		if (margin.equals(Constants.EMPTY_STRING))
		    return;

		String tmp = extMarginCombo.getText();

		if (tmp != null) {
		    styleAttributes.addAttribute(CSSConstants.MARGIN, margin
			    + tmp);
		    // TODO Dzmitry Sakovich
		    // cssDialog.setStyleForPreview();
		}
	    }
	});

	extMarginCombo.setLayoutData(new GridData(GridData.FILL,
		GridData.CENTER, false, false));
	for (int i = 0; i < Constants.extSizes.length; i++) {
	    extMarginCombo.add(Constants.extSizes[i]);
	}

	label = new Label(this, SWT.LEFT);
	label.setText(MessageUtil.getString("PADDING")); //$NON-NLS-1$
	label.setLayoutData(new GridData(GridData.END, GridData.CENTER, false,
		false));

	paddingText = new Text(this, SWT.BORDER | SWT.SINGLE);

	paddingText.addModifyListener(new ModifyListener() {
	    public void modifyText(ModifyEvent event) {

		String tmp = paddingText.getText();

		if (tmp != null) {
		    if (tmp.trim().length() > 0) {
			String extPadding = extPaddingCombo.getText().trim();
			if (extPadding != null)
			    styleAttributes.addAttribute(CSSConstants.PADDING,
				    tmp + extPadding);
			else
			    styleAttributes.addAttribute(CSSConstants.PADDING,
				    tmp);
		    } else
			styleAttributes.removeAttribute(CSSConstants.PADDING);
		} else
		    styleAttributes.removeAttribute(CSSConstants.PADDING);
		// TODO Dzmitry Sakovich
		// cssDialog.setStyleForPreview();
	    }
	});

	paddingText.setLayoutData(new GridData(GridData.FILL, GridData.CENTER,
		true, false));

	extPaddingCombo = new Combo(this, SWT.BORDER | SWT.READ_ONLY);

	extPaddingCombo.addModifyListener(new ModifyListener() {
	    public void modifyText(ModifyEvent event) {
		String padding = paddingText.getText().trim();
		if (padding == null)
		    return;
		if (padding.equals(Constants.EMPTY_STRING))
		    return;

		String tmp = extPaddingCombo.getText();

		if (tmp != null) {
		    styleAttributes.addAttribute(CSSConstants.PADDING, padding
			    + tmp);
		    // TODO Dzmitry Sakovich
		    // cssDialog.setStyleForPreview();
		}
	    }
	});

	extPaddingCombo.setLayoutData(new GridData(GridData.FILL,
		GridData.CENTER, false, false));
	for (int i = 0; i < Constants.extSizes.length; i++) {
	    extPaddingCombo.add(Constants.extSizes[i]);
	}
    }

    /**
     * Method for get data in controls (if param equal true ), or set data (if
     * param equal false).
     * 
     * @param param
     */
    public void updateData(boolean param) {
	String tmp;
	if ((tmp = styleAttributes.getAttribute(CSSConstants.WIDTH)) != null) {
	    String[] str = Util.convertExtString(tmp);
	    widthText.setText(str[0]);
	    if (extWidthCombo.indexOf(str[1]) != -1) {
		extWidthCombo.setText(str[1]);
		extWidthCombo.select(extWidthCombo.indexOf(str[1]));
	    } else
		extWidthCombo.select(0);
	} else {
	    widthText.setText(Constants.EMPTY_STRING);
	    extWidthCombo.select(0);
	}
	if ((tmp = styleAttributes.getAttribute(CSSConstants.HEIGHT)) != null) {
	    String[] str = Util.convertExtString(tmp);
	    heightText.setText(str[0]);
	    if (extHeightCombo.indexOf(str[1]) != -1) {
		extHeightCombo.setText(str[1]);
		extHeightCombo.select(extHeightCombo.indexOf(str[1]));
	    } else
		extHeightCombo.select(0);
	} else {
	    heightText.setText(Constants.EMPTY_STRING);
	    extHeightCombo.select(0);
	}
	if ((tmp = styleAttributes.getAttribute(CSSConstants.BORDER_STYLE)) != null)
	    borderStyleCombo.setText(tmp);
	else
	    borderStyleCombo.setText(Constants.EMPTY_STRING);
	if ((tmp = styleAttributes.getAttribute(CSSConstants.BORDER_COLOR)) != null)
	    borderColorCombo.setText(tmp);
	else
	    borderColorCombo.setText(Constants.EMPTY_STRING);
	if ((tmp = styleAttributes.getAttribute(CSSConstants.BORDER_WIDTH)) != null) {
	    String[] str = Util.convertExtString(tmp);
	    borderWidthCombo.setText(str[0]);
	    if (extBorderWidthCombo.indexOf(str[1]) != -1) {
		extBorderWidthCombo.setText(str[1]);
		extBorderWidthCombo.select(extBorderWidthCombo.indexOf(str[1]));
	    } else
		extBorderWidthCombo.select(0);
	} else {
	    borderWidthCombo.setText(Constants.EMPTY_STRING);
	    extBorderWidthCombo.select(0);
	}
	if ((tmp = styleAttributes.getAttribute(CSSConstants.MARGIN)) != null) {
	    String[] str = Util.convertExtString(tmp);
	    marginText.setText(str[0]);
	    if (extMarginCombo.indexOf(str[1]) != -1) {
		extMarginCombo.setText(str[1]);
		extMarginCombo.select(extMarginCombo.indexOf(str[1]));
	    } else
		extMarginCombo.select(0);
	} else {
	    marginText.setText(Constants.EMPTY_STRING);
	    extMarginCombo.select(0);
	}
	if ((tmp = styleAttributes.getAttribute(CSSConstants.PADDING)) != null) {
	    String[] str = Util.convertExtString(tmp);
	    paddingText.setText(str[0]);
	    if (extPaddingCombo.indexOf(str[1]) != -1) {
		extPaddingCombo.setText(str[1]);
		extPaddingCombo.select(extPaddingCombo.indexOf(str[1]));
	    } else
		extPaddingCombo.select(0);
	} else {
	    paddingText.setText(Constants.EMPTY_STRING);
	    extPaddingCombo.select(0);
	}
    }
}
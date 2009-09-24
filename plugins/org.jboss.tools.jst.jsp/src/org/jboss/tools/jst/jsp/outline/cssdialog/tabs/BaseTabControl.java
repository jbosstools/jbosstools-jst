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
package org.jboss.tools.jst.jsp.outline.cssdialog.tabs;

import java.util.List;
import java.util.Map;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.Observables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.messages.JstUIMessages;
import org.jboss.tools.jst.jsp.outline.cssdialog.FontFamilyDialog;
import org.jboss.tools.jst.jsp.outline.cssdialog.ImageSelectionDialog;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.CSSConstants;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.CSSStyleValueValidator;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Constants;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.StyleAttributes;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Util;
import org.jboss.tools.jst.jsp.outline.cssdialog.widgets.CSSTreeItemWidgetValueProperty;
import org.jboss.tools.jst.jsp.outline.cssdialog.widgets.CSSWidget;
import org.jboss.tools.jst.jsp.outline.cssdialog.widgets.CSSWidgetValueProperty;
import org.jboss.tools.jst.jsp.outline.cssdialog.widgets.ImageCombo;
import org.jboss.tools.jst.jsp.outline.cssdialog.widgets.SizeCombo;
import org.jboss.tools.jst.jsp.outline.cssdialog.widgets.SizeText;

/**
 * This is base tab control component that should be re-implemented by
 * successor.
 * 
 * @author Igor Zhukov
 */
public abstract class BaseTabControl extends Composite implements
		ICSSTabControl {

	private StyleAttributes styleAttributes = null;

	private DataBindingContext bindingContext;

	final static protected int NAME_ATTRIBUTE_COLUMN = 0;
	final static protected int VALUE_ATTRIBUTE_COLUMN = 1;
	final static protected int COMPOSITE_NUM_COLUMNS = 2;

	private static final int COLUMNS = 3;

	/**
	 * Constructs a new instance of this class given its parent and a style
	 * value describing its behavior and appearance.
	 * 
	 * @param parent
	 *            a widget which will be the parent of the new instance (cannot
	 *            be null)
	 * @param style
	 *            the style of widget to construct
	 */
	public BaseTabControl(DataBindingContext bindingContext,
			StyleAttributes styleAttributes, Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(COLUMNS, false));
		this.styleAttributes = styleAttributes;
		this.bindingContext = bindingContext;
	}

	/**
	 * @return the styleAttributes
	 */
	public StyleAttributes getStyleAttributes() {
		return styleAttributes;
	}

	public void setStyleAttributes(StyleAttributes styleAttributes) {
		this.styleAttributes = styleAttributes;
	}

	public DataBindingContext getBindingContext() {
		return this.bindingContext;
	}

	public void setBindingContext(DataBindingContext bindingContext) {
		this.bindingContext = bindingContext;
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
	 * 
	 * @param parent
	 * @param label
	 */
	protected Label addSectionLabel(Composite parent, String label) {

		Label labelControl = new Label(parent, SWT.NONE);
		labelControl.setLayoutData(new GridData(GridData.BEGINNING,
				GridData.CENTER, false, false, 3, 1));
		labelControl.setFont(JFaceResources.getFontRegistry().get(
				JFaceResources.BANNER_FONT));
		labelControl.setText(label);

		return labelControl;
	}

	/**
	 * 
	 * @param parent
	 * @param attribute
	 */
	protected Composite addFontComposite(Composite parent, String attribute) {

		Composite wrapper = createWrapperComposite(parent);

		final Text fontFamilyText = new Text(wrapper, SWT.BORDER | SWT.SINGLE);
		fontFamilyText.setLayoutData(new GridData(GridData.FILL,
				GridData.CENTER, true, false));

		Button button = createButton(wrapper,
				Constants.IMAGE_FONTLARGE_FILE_LOCATION,
				JstUIMessages.FONT_FAMILY_TIP);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				FontFamilyDialog dialog = new FontFamilyDialog(getShell(),
						fontFamilyText.getText());
				if (dialog.open() == Window.OK) {
					fontFamilyText.setText(dialog.getFontFamily());
				}
			}
		});

		bind(fontFamilyText, attribute);

		return wrapper;

	}

	/**
	 * 
	 * @param parent
	 * @param attribute
	 */
	protected Composite addColorComposite(Composite parent, String attribute) {

		Composite wrapper = createWrapperComposite(parent);

		final ImageCombo colorCombo = new ImageCombo(wrapper, SWT.BORDER);

		colorCombo.setLayoutData(new GridData(GridData.FILL, GridData.CENTER,
				true, false));

		for (Map.Entry<String, String> me : CSSConstants.COLORS.entrySet()) {
			RGB rgb = Util.getColor(me.getKey());
			colorCombo.add(me.getValue(), rgb);
		}

		Button button = createButton(wrapper,
				Constants.IMAGE_COLORLARGE_FILE_LOCATION,
				JstUIMessages.COLOR_TIP);

		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				ColorDialog dlg = new ColorDialog(getShell());

				dlg
						.setRGB((Util.getColor((colorCombo.getText().trim())) == null) ? Constants.RGB_BLACK
								: Util.getColor((colorCombo.getText().trim())));
				dlg.setText(JstUIMessages.COLOR_DIALOG_TITLE);

				RGB rgb = dlg.open();
				if (rgb != null) {
					String colorStr = Util.createColorString(rgb);
					colorCombo.setText(colorStr);
				}
			}
		});

		bind(colorCombo, attribute);

		return wrapper;
	}

	protected Composite addImageFileComposite(Composite parent,
			List<String> comboValues, final String attribute) {

		Composite wrapper = createWrapperComposite(parent);

		final Combo combo = new Combo(wrapper, SWT.BORDER);
		combo.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true,
				false));

		for (String string : comboValues) {
			combo.add(string);
		}

		Button button = createButton(wrapper,
				Constants.IMAGE_FOLDERLARGE_FILE_LOCATION,
				JstUIMessages.BACKGROUND_IMAGE);

		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				IAdaptable project = Util.getCurrentProject();
				ImageSelectionDialog dialog = new ImageSelectionDialog(
						getShell(), new WorkbenchLabelProvider(),
						new WorkbenchContentProvider());
				dialog.setTitle(JstUIMessages.IMAGE_DIALOG_TITLE);
				dialog.setMessage(JstUIMessages.IMAGE_DIALOG_MESSAGE);
				dialog
						.setEmptyListMessage(JstUIMessages.IMAGE_DIALOG_EMPTY_MESSAGE);
				dialog.setAllowMultiple(false);
				dialog.setInput(project);

				if (dialog.open() == ImageSelectionDialog.OK) {
					IFile file = (IFile) dialog.getFirstResult();
					String value = file.getFullPath().toString();
					combo.add(value);
					combo.setText(value);
				}
			}
		});

		bind(combo, attribute, new UpdateValueStrategy() {

			@Override
			protected IStatus doSet(IObservableValue observableValue,
					Object value) {

				List<String> values = CSSConstants.CSS_STYLE_VALUES_MAP
						.get(attribute);

				if ((values != null) && !values.contains(value))
					value = adjustBackgroundURL((String) value);

				return super.doSet(observableValue, value);
			}
		}, null);

		return wrapper;
	}

	protected Composite createWrapperComposite(Composite parent) {
		Composite wrapper = new Composite(parent, SWT.None);
		GridLayout layout = new GridLayout(COMPOSITE_NUM_COLUMNS, false);

		layout.marginWidth = 0;
		layout.marginHeight = 0;
		wrapper.setLayout(layout);
		wrapper.setLayoutData(new GridData(GridData.FILL, GridData.CENTER,
				true, false, 2, 1));
		return wrapper;
	}

	/**
	 * 
	 * @param parent
	 * @param comboValues
	 * @param attribute
	 */
	protected Composite addSizeCombo(Composite parent,
			List<String> comboValues, String attribute) {

		SizeCombo combo = new SizeCombo(parent, comboValues);

		bind(combo, attribute);

		return combo;

	}

	/**
	 * 
	 * @param parent
	 * @param comboValues
	 * @param attribute
	 */
	protected Composite addSizeText(Composite parent, String attribute) {

		SizeText combo = new SizeText(parent);

		bind(combo, attribute);

		return combo;

	}

	/**
	 * 
	 * @param parent
	 * @param attribute
	 */
	protected Text addText(Composite parent, String attribute) {

		GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true,
				false);
		gridData.horizontalSpan = 2;

		Text text = new Text(parent, SWT.BORDER);
		text.setLayoutData(gridData);

		bind(text, attribute);

		return text;

	}

	/**
	 * 
	 * @param parent
	 * @param comboValues
	 * @param attribute
	 */
	protected Combo addCombo(Composite parent, List<String> comboValues,
			String attribute) {

		GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true,
				false);
		gridData.horizontalSpan = 2;

		Combo combo = new Combo(parent, SWT.BORDER);
		combo.setLayoutData(gridData);

		for (String str : comboValues) {
			combo.add(str);
		}

		bind(combo, attribute);

		return combo;
	}

	protected TreeItem createBindedTreeItem(TreeItem parent, String attribute) {

		TreeItem item = new TreeItem(parent, SWT.NONE);
		item.setText(NAME_ATTRIBUTE_COLUMN, attribute);
		bind(item, attribute);

		return item;
	}

	protected TreeItem createTreeItem(Tree parent, String text) {

		TreeItem item = new TreeItem(parent, SWT.NONE);
		item.setText(text);

		return item;
	}

	/**
	 * 
	 * @param parent
	 * @param imageFile
	 * @param tooltip
	 * @return
	 */
	private Button createButton(Composite parent, String imageFile,
			String tooltip) {
		Button button = new Button(parent, SWT.PUSH);
		button.setLayoutData(new GridData(GridData.END, GridData.CENTER, false,
				false));
		button.setToolTipText(tooltip);

		button.setImage(JspEditorPlugin.getImageDescriptor(imageFile)
				.createImage());
		button.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				Button button = (Button) e.getSource();
				button.getImage().dispose();
			}
		});

		return button;
	}

	/**
	 * create control by css property
	 * 
	 * @param parent
	 * @param key
	 * @return
	 */
	protected Control createControl(Composite parent, String property) {

		Control control = null;
		if (CSSConstants.CSS_STYLE_VALUES_MAP.keySet().contains(property)) {

			if (property.indexOf(CSSConstants.COLOR) != Constants.DONT_CONTAIN) {
				control = addColorComposite(parent, property);
			} else if (Constants.elemFolder.contains(property)) {
				control = addImageFileComposite(parent,
						CSSConstants.CSS_STYLE_VALUES_MAP.get(property),
						property);
			} else if (Constants.extElem.contains(property)) {
				control = addSizeCombo(parent,
						CSSConstants.CSS_STYLE_VALUES_MAP.get(property),
						property);
			} else {
				control = addCombo(parent, CSSConstants.CSS_STYLE_VALUES_MAP
						.get(property), property);
			}

		} else {
			if (property.equalsIgnoreCase(CSSConstants.FONT_FAMILY)) {
				control = addFontComposite(parent, property);
			} else if (Constants.extElem.contains(property)) {
				control = addSizeText(parent, property);
			} else {
				control = addText(parent, property);
			}
		}
		return control;
	}

	private static String adjustBackgroundURL(String backgroundURL) {
		if ((backgroundURL != null && !backgroundURL.trim().equals(
				Constants.EMPTY))
				&& (backgroundURL.matches("(url)\\(.*\\)") == false)) { //$NON-NLS-1$
			return "url(" + backgroundURL + ")"; //$NON-NLS-1$//$NON-NLS-2$
		}

		return backgroundURL;
	}

	private IObservableValue createAttributeObservableValue(String attributeName) {
		return Observables.observeMapEntry(getStyleAttributes()
				.getObservableMap(), attributeName, String.class);
	}

	private void bind(Widget widget, String attribute) {
		bind(widget, attribute, null, null);
	}

	private void bind(Widget widget, String attribute,
			UpdateValueStrategy targetToModel, UpdateValueStrategy modelToTarget) {

		IObservableValue attributeValue = createAttributeObservableValue(attribute);

		if (targetToModel == null) {
			targetToModel = new UpdateValueStrategy();
		}

		targetToModel.setBeforeSetValidator(CSSStyleValueValidator
				.getInstance());

		if (widget instanceof Text) {
			getBindingContext().bindValue(
					SWTObservables.observeText((Text) widget, SWT.Modify),
					attributeValue, targetToModel, modelToTarget);
		} else if ((widget instanceof Combo)) {
			getBindingContext().bindValue(SWTObservables.observeText(widget),
					attributeValue, targetToModel, modelToTarget);
		} else if (widget instanceof CSSWidget) {
			getBindingContext().bindValue(
					new CSSWidgetValueProperty().observe(widget),
					attributeValue, targetToModel, modelToTarget);
		} else if (widget instanceof TreeItem) {
			getBindingContext().bindValue(
					new CSSTreeItemWidgetValueProperty(VALUE_ATTRIBUTE_COLUMN)
							.observe(widget), attributeValue, targetToModel,
					modelToTarget);
		}

	}

	public void update() {

	}

}
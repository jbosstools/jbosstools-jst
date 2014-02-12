/*******************************************************************************
 * Copyright (c) 2008, 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Red Hat, Inc - backport of Bug #415980
 *******************************************************************************/
package org.jboss.tools.jst.jsp.internal.html.preferences;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.layout.PixelConverter;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IWorkbench;
import org.eclipse.wst.html.core.internal.HTMLCorePlugin;
import org.eclipse.wst.html.core.internal.preferences.HTMLCorePreferenceNames;
import org.eclipse.wst.html.ui.internal.HTMLUIPlugin;
import org.eclipse.wst.html.ui.internal.Logger;
import org.eclipse.wst.sse.ui.internal.preferences.ui.AbstractValidationSettingsPage;
import org.eclipse.wst.sse.ui.internal.preferences.ui.ScrolledPageContent;
import org.jboss.tools.jst.jsp.internal.html.text.HTMLCoreNewPreferences;
import org.jboss.tools.jst.jsp.internal.html.text.HTMLUIMessages;
import org.osgi.service.prefs.BackingStoreException;

public class HTMLAttributeValidationPreferencePage extends AbstractValidationSettingsPage {
	public static final String PROPERTY_PAGE_ID = "org.jboss.tools.jst.jsp.internal.html.ui.propertyPage.project.attribute.validation";
	public static final String PREFERENCE_PAGE_ID = "org.jboss.tools.jst.jsp.internal.html.ui.preferences.attribute.validation";

	private class BooleanData {
		private String fKey;
		private boolean fValue;
		boolean originalValue = false; 
		
		public BooleanData(String key) {
			fKey = key;
		}
		
		public String getKey() {
			return fKey;
		}
		
		/**
		 * Sets enablement for the attribute names ignorance
		 * 
		 * @param severity the severity level
		 */
		public void setValue(boolean value) {
			fValue = value;
		}
		
		/**
		 * Returns the value for the attribute names ignorance
		 * 
		 * @return
		 */
		public boolean getValue() {
			return fValue;
		}
		
		boolean isChanged() {
			return (originalValue != fValue);
		}
	}
	
	private class TextData {
		private String fKey;
		private String fValue;
		String originalValue = ""; //$NON-NLS-1$
		
		public TextData(String key) {
			fKey = key;
		}
		
		public String getKey() {
			return fKey;
		}
		
		/**
		 * Sets the ignored attribute names pattern
		 * 
		 * @param severity the severity level
		 */
		public void setValue(String value) {
			fValue = value;
		}
		
		/**
		 * Returns non-null value for the ignored attribute names pattern
		 * 
		 * @return
		 */
		public String getValue() {
			return fValue != null ? fValue : ""; //$NON-NLS-1$
		}
		
		boolean isChanged() {
			return !originalValue.equalsIgnoreCase(fValue);
		}
	}
	
	public HTMLAttributeValidationPreferencePage() {
		fPreferencesService = Platform.getPreferencesService();
	}
	
	private PixelConverter fPixelConverter;
	private Button fIgnoreAttributeNames;
	private Label fIgnoredAttributeNamesLabel;
	private Text fIgnoredAttributeNames;
	private IPreferencesService fPreferencesService = null;
	
	private boolean fUseOriginOverrides = false;
	private boolean fIgnoreAttributeNamesOriginOverride = HTMLCoreNewPreferences.IGNORE_ATTRIBUTE_NAMES_DEFAULT;
	private String  fIgnoredAttributeNamesOriginOverride = HTMLCoreNewPreferences.ATTRIBUTE_NAMES_TO_IGNORE_DEFAULT;

	private static boolean hasRequiredAPI = HTMLCoreNewPreferences.hasRequiredAPI();
	
	public void overrideOriginValues(boolean enableIgnore, String attributeNames) {
		fIgnoreAttributeNamesOriginOverride = enableIgnore;
		fIgnoredAttributeNamesOriginOverride = attributeNames;
		fUseOriginOverrides = true;
		
		if (fIgnoreAttributeNames != null) {
			BooleanData data = (BooleanData)fIgnoreAttributeNames.getData();
			if (data != null)
				data.originalValue = fIgnoreAttributeNamesOriginOverride;
		}
		if (fIgnoredAttributeNames != null) {
			TextData data = (TextData)fIgnoredAttributeNames.getData();
			if (data != null)
				data.originalValue = fIgnoredAttributeNamesOriginOverride;
		}
	}
	
	protected Control createCommonContents(Composite parent) {
		final Composite page = new Composite(parent, SWT.NULL);
		
		//GridLayout
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		page.setLayout(layout);
		
		fPixelConverter = new PixelConverter(parent);
		
		final Composite content = createValidationSection(page);

		GridData gridData= new GridData(GridData.FILL, GridData.FILL, true, true);
		gridData.heightHint = fPixelConverter.convertHeightInCharsToPixels(20);
		content.setLayoutData(gridData);
		
		return page;
	}
	
	private Composite createValidationSection(Composite page) {
		int nColumns = 3;
		
		boolean hasRequiredAPI = HTMLCoreNewPreferences.hasRequiredAPI();
		
		final ScrolledPageContent spContent = new ScrolledPageContent(page);
		
		Composite composite = spContent.getBody();
		
		GridLayout layout= new GridLayout(nColumns, false);
		layout.marginHeight= 0;
		layout.marginWidth= 0;
		composite.setLayout(layout);
		
		if (!hasRequiredAPI) {
			Label warning = new Label(composite, SWT.LEFT | SWT.WRAP | SWT.BOLD);
			warning.setFont(page.getFont());
			warning.setText(HTMLUIMessages.RequiresWTPSR2);
			warning.setEnabled(true);
		}
		
		// Ignored Attribute Names Pattern
		BooleanData ignoreData = new BooleanData(HTMLCoreNewPreferences.IGNORE_ATTRIBUTE_NAMES);
		fIgnoreAttributeNames = new Button(composite, SWT.CHECK);
		fIgnoreAttributeNames.setData(ignoreData);
		fIgnoreAttributeNames.setFont(page.getFont());
		fIgnoreAttributeNames.setText(HTMLUIMessages.IgnoreAttributeNames);
		fIgnoreAttributeNames.setEnabled(hasRequiredAPI);
		
		boolean ignoreAttributeNamesIsSelected = fPreferencesService.getBoolean(getPreferenceNodeQualifier(), 
				ignoreData.getKey(), HTMLCoreNewPreferences.IGNORE_ATTRIBUTE_NAMES_DEFAULT, createPreferenceScopes());
		ignoreData.setValue(ignoreAttributeNamesIsSelected);
		ignoreData.originalValue = fUseOriginOverrides ? fIgnoreAttributeNamesOriginOverride : ignoreAttributeNamesIsSelected;
		
		fIgnoreAttributeNames.setSelection(ignoreData.getValue());
		fIgnoreAttributeNames.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				controlChanged(e.widget);
			}
			public void widgetSelected(SelectionEvent e) {
				controlChanged(e.widget);
			}
		});
		fIgnoreAttributeNames.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1));
		
		fIgnoredAttributeNamesLabel = new Label(composite, SWT.LEFT | SWT.WRAP);
		fIgnoredAttributeNamesLabel.setFont(composite.getFont());
		fIgnoredAttributeNamesLabel.setEnabled(hasRequiredAPI && ignoreData.getValue());
		fIgnoredAttributeNamesLabel.setText(HTMLUIMessages.IgnoreAttributeNamesPattern);
		fIgnoredAttributeNamesLabel.setLayoutData(new GridData(SWT.FILL, SWT.END, true, false, 3, 1));
		setHorizontalIndent(fIgnoredAttributeNamesLabel, 20);

		TextData data = new TextData(HTMLCoreNewPreferences.ATTRIBUTE_NAMES_TO_IGNORE);
		fIgnoredAttributeNames = new Text(composite, SWT.SINGLE | SWT.BORDER);
		fIgnoredAttributeNames.setData(data);
		fIgnoredAttributeNames.setTextLimit(500);
		fIgnoredAttributeNames.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1));
		setHorizontalIndent(fIgnoredAttributeNames, 20);
		setWidthHint(fIgnoredAttributeNames, convertWidthInCharsToPixels(65));
		String ignoredAttributeNames = fPreferencesService.getString(getPreferenceNodeQualifier(), data.getKey(), HTMLCoreNewPreferences.ATTRIBUTE_NAMES_TO_IGNORE_DEFAULT, createPreferenceScopes());
		data.setValue(ignoredAttributeNames);
		data.originalValue = fUseOriginOverrides ? fIgnoredAttributeNamesOriginOverride : ignoredAttributeNames;
		fIgnoredAttributeNames.setText(data.getValue());
		
		fIgnoredAttributeNames.addModifyListener(new ModifyListener() {
			
			public void modifyText(ModifyEvent e) {
				if (verifyIgnoredAttributeNames()) {
					controlChanged(e.widget);
				}
			}
		});
		controlChanged(fIgnoreAttributeNames);

		return spContent;
	}

	private void setHorizontalIndent(Control control, int indent) {
		Object ld= control.getLayoutData();
		if (ld instanceof GridData) {
			((GridData) ld).horizontalIndent= indent;
		}
	}
	
	private void setWidthHint(Control control, int widthHint) {
		Object ld= control.getLayoutData();
		if (ld instanceof GridData) {
			((GridData)ld).widthHint= widthHint;
		}
	}
	
	private boolean verifyIgnoredAttributeNames() {
		final String text = fIgnoredAttributeNames.getText().trim();
		if (text.length() == 0)
			return true;

		boolean valid = true;
		for (int i = 0; valid && i < text.length(); i++) {
			if (!Character.isJavaIdentifierPart(text.charAt(i)) &&
					'-' != text.charAt(i) && '_' != text.charAt(i) &&
					'*' != text.charAt(i) && '?' != text.charAt(i) &&
					',' != text.charAt(i))
				valid = false;
		}
		
		if (!valid) {
			setErrorMessage(NLS.bind(HTMLUIMessages.BadIgnoreAttributeNamesPattern, text));
			setValid(false);
		}
		else {
			setErrorMessage(null);
			setValid(true);
		}
		return valid;
	}
	
	protected void controlChanged(Widget widget) {
		if (widget instanceof Text) {
			TextData data= (TextData) widget.getData();
			data.setValue(((Text)widget).getText());
		} else if (widget instanceof Button) {
			BooleanData data = (BooleanData) widget.getData();
			if (data != null) {
				data.setValue(((Button)widget).getSelection());
				fIgnoredAttributeNamesLabel.setEnabled(hasRequiredAPI & data.getValue());
				fIgnoredAttributeNames.setEnabled(hasRequiredAPI & data.getValue());
				if (data.getValue()) {
					fIgnoredAttributeNames.setFocus();
				}
			}
		}
	}

	/**
	 * Returns true in case of the Attribute Names to ignore preferences is changed
	 * causing the full validation to be requested.
	 */
	protected boolean shouldRevalidateOnSettingsChange() {
		TextData data = (TextData)fIgnoredAttributeNames.getData();
		if (data.isChanged())
			return true;
		
		BooleanData ignoreData = (BooleanData)fIgnoreAttributeNames.getData();
		if (ignoreData.isChanged())
			return true;
		
		return super.shouldRevalidateOnSettingsChange();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.wst.sse.ui.internal.preferences.ui.AbstractSettingsPage#storeValues()
	 */
	protected void storeValues() {
		if (hasRequiredAPI) {
			IScopeContext[] contexts = createPreferenceScopes();
	
			BooleanData ignoreData = (BooleanData)fIgnoreAttributeNames.getData();
			contexts[0].getNode(getPreferenceNodeQualifier()).putBoolean(ignoreData.getKey(), ignoreData.getValue()); 
	
			TextData data = (TextData)fIgnoredAttributeNames.getData();
			contexts[0].getNode(getPreferenceNodeQualifier()).put(data.getKey(), data.getValue()); 
			
			
			for(int i = 0; i < contexts.length; i++) {
				try {
					contexts[i].getNode(getPreferenceNodeQualifier()).flush();
				} catch (BackingStoreException e) {
					Logger.logException(e);
				}
			}
		}
		super.storeValues();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
	 */
	protected void performDefaults() {
		if (hasRequiredAPI) {
			resetIgnoreAttributeNamesPattern();
			resetSeverities();
		}
		super.performDefaults();
	}
	
	protected void resetIgnoreAttributeNamesPattern() {
		if (!hasRequiredAPI) return;
		IEclipsePreferences defaultContext = new DefaultScope().getNode(getPreferenceNodeQualifier());
		BooleanData ignoreData = (BooleanData)fIgnoreAttributeNames.getData();
		boolean ignoreAttributeNames = defaultContext.getBoolean(ignoreData.getKey(), HTMLCoreNewPreferences.IGNORE_ATTRIBUTE_NAMES_DEFAULT);
		ignoreData.setValue(ignoreAttributeNames);
		fIgnoreAttributeNames.setSelection(ignoreData.getValue());

		TextData data = (TextData)fIgnoredAttributeNames.getData();
		String ignoredAttributeNames = defaultContext.get(data.getKey(), HTMLCoreNewPreferences.ATTRIBUTE_NAMES_TO_IGNORE_DEFAULT);
		data.setValue(ignoredAttributeNames);
		fIgnoredAttributeNames.setText(data.getValue());
		
		controlChanged(fIgnoreAttributeNames);
	}
	
	
	protected IDialogSettings getDialogSettings() {
		return HTMLUIPlugin.getDefault().getDialogSettings();
	}

	protected String getQualifier() {
		return HTMLCorePlugin.getDefault().getBundle().getSymbolicName();
	}
	
	protected String getPreferenceNodeQualifier() {
		return HTMLCorePlugin.getDefault().getBundle().getSymbolicName();
	}

	protected String getPreferencePageID() {
		return PREFERENCE_PAGE_ID;
	}

	protected String getProjectSettingsKey() {
		return HTMLCorePreferenceNames.USE_PROJECT_SETTINGS;
	}

	protected String getPropertyPageID() {
		return PROPERTY_PAGE_ID;
	}

	public void init(IWorkbench workbench) {
	}
}

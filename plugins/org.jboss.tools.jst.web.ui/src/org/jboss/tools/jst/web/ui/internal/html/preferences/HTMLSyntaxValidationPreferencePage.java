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
package org.jboss.tools.jst.web.ui.internal.html.preferences;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.layout.PixelConverter;
import org.eclipse.jface.preference.IPreferenceStore;
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
import org.eclipse.wst.sse.ui.internal.SSEUIPlugin;
import org.eclipse.wst.sse.ui.internal.preferences.ui.AbstractValidationSettingsPage;
import org.eclipse.wst.sse.ui.internal.preferences.ui.ScrolledPageContent;
import org.eclipse.wst.sse.ui.internal.provisional.preferences.CommonEditorPreferenceNames;
import org.jboss.tools.jst.web.ui.internal.html.text.HTMLCoreNewPreferences;
import org.jboss.tools.jst.web.ui.internal.html.text.HTMLUIMessages;
import org.osgi.service.prefs.BackingStoreException;

public class HTMLSyntaxValidationPreferencePage extends AbstractValidationSettingsPage {
	public static final String PROPERTY_PAGE_ID = "org.jboss.tools.jst.web.ui.internal.html.ui.propertyPage.project.syntax.validation";
	public static final String PREFERENCE_PAGE_ID = "org.jboss.tools.jst.web.ui.internal.html.ui.preferences.syntax.validation";

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
		 * Sets enablement for the element names ignorance
		 * 
		 * @param severity the severity level
		 */
		public void setValue(boolean value) {
			fValue = value;
		}
		
		/**
		 * Returns the value for the element names ignorance
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
		 * Sets the ignored element names pattern
		 * 
		 * @param severity the severity level
		 */
		public void setValue(String value) {
			fValue = value;
		}
		
		/**
		 * Returns non-null value for the ignored element names pattern
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
	
	public HTMLSyntaxValidationPreferencePage() {
		fPreferencesService = Platform.getPreferencesService();
	}
	
	private PixelConverter fPixelConverter;
	private Button fIgnoreElementNames;
	private Label fIgnoredElementNamesLabel;
	private Text fIgnoredElementNames;
	private IPreferencesService fPreferencesService = null;
	
	private boolean fUseElementsOriginOverrides = false;
	private boolean fIgnoreElementNamesOriginOverride = HTMLCoreNewPreferences.IGNORE_ELEMENT_NAMES_DEFAULT;
	private String  fIgnoredElementNamesOriginOverride = HTMLCoreNewPreferences.ELEMENT_NAMES_TO_IGNORE_DEFAULT;

	public void overrideIgnoredElementsOriginValues(boolean enableIgnore, String elementNames) {
		fIgnoreElementNamesOriginOverride = enableIgnore;
		fIgnoredElementNamesOriginOverride = elementNames;
		fUseElementsOriginOverrides = true;

		if (fIgnoreElementNames != null) {
			BooleanData data = (BooleanData)fIgnoreElementNames.getData();
			if (data != null)
				data.originalValue = fIgnoreElementNamesOriginOverride;
		}
		if (fIgnoredElementNames != null) {
			TextData data = (TextData)fIgnoredElementNames.getData();
			if (data != null)
				data.originalValue = fIgnoredElementNamesOriginOverride;
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
		
		// Ignored Element Names Pattern
		BooleanData ignoreData = new BooleanData(HTMLCoreNewPreferences.IGNORE_ELEMENT_NAMES);
		fIgnoreElementNames = new Button(composite, SWT.CHECK);
		fIgnoreElementNames.setData(ignoreData);
		fIgnoreElementNames.setFont(page.getFont());
		fIgnoreElementNames.setText(HTMLUIMessages.IgnoreElementNames);
		fIgnoreElementNames.setEnabled(hasRequiredAPI);
		
		boolean ignoreElementNamesIsSelected = fPreferencesService.getBoolean(getPreferenceNodeQualifier(), 
				ignoreData.getKey(), HTMLCoreNewPreferences.IGNORE_ELEMENT_NAMES_DEFAULT, createPreferenceScopes());
		ignoreData.setValue(ignoreElementNamesIsSelected);
		ignoreData.originalValue = fUseElementsOriginOverrides ? fIgnoreElementNamesOriginOverride : ignoreElementNamesIsSelected;
		
		fIgnoreElementNames.setSelection(ignoreData.getValue());
		fIgnoreElementNames.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				controlChanged(e.widget);
			}
			public void widgetSelected(SelectionEvent e) {
				controlChanged(e.widget);
			}
		});
		fIgnoreElementNames.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1));
		
		fIgnoredElementNamesLabel = new Label(composite, SWT.LEFT | SWT.WRAP);
		fIgnoredElementNamesLabel.setFont(composite.getFont());
		fIgnoredElementNamesLabel.setEnabled(hasRequiredAPI && ignoreData.getValue());
		fIgnoredElementNamesLabel.setText(HTMLUIMessages.IgnoreElementNamesPattern);
		fIgnoredElementNamesLabel.setLayoutData(new GridData(SWT.FILL, SWT.END, true, false, 3, 1));
		setHorizontalIndent(fIgnoredElementNamesLabel, 20);

		TextData data = new TextData(HTMLCoreNewPreferences.ELEMENT_NAMES_TO_IGNORE);
		fIgnoredElementNames = new Text(composite, SWT.SINGLE | SWT.BORDER);
		fIgnoredElementNames.setData(data);
		fIgnoredElementNames.setEnabled(hasRequiredAPI && ignoreData.getValue());
		fIgnoredElementNames.setTextLimit(500);
		fIgnoredElementNames.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1));
		setHorizontalIndent(fIgnoredElementNames, 20);
		setWidthHint(fIgnoredElementNames, convertWidthInCharsToPixels(65));
		String ignoredElementNames = fPreferencesService.getString(getPreferenceNodeQualifier(), data.getKey(), HTMLCoreNewPreferences.ELEMENT_NAMES_TO_IGNORE_DEFAULT, createPreferenceScopes());
		data.setValue(ignoredElementNames);
		data.originalValue = fUseElementsOriginOverrides ? fIgnoredElementNamesOriginOverride : ignoredElementNames;
		fIgnoredElementNames.setText(data.getValue());
		
		fIgnoredElementNames.addModifyListener(new ModifyListener() {
			
			public void modifyText(ModifyEvent e) {
				if (verifyIgnoredElementNames()) {
					controlChanged(e.widget);
				}
			}
		});
		controlChanged(fIgnoreElementNames);

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
	
	private boolean verifyIgnoredElementNames() {
		final String text = fIgnoredElementNames.getText().trim();
		if (text.length() == 0)
			return true;

		boolean valid = true;
		String[] values = text.split(",");
		for (int i = 0; i < values.length; i++) {
			String value = values[i].trim();
			for (int j = 0; valid && j < value.length(); j++) {
				if (!Character.isJavaIdentifierPart(value.charAt(j)) &&
						'-' != value.charAt(j) && '_' != value.charAt(j) &&
						'*' != value.charAt(j) && '?' != value.charAt(j))
					valid = false;
			}
		}		
		if (!valid) {
			setErrorMessage(NLS.bind(HTMLUIMessages.BadIgnoreElementNamesPattern, text));
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
				boolean hasRequiredAPI = HTMLCoreNewPreferences.hasRequiredAPI();
				fIgnoredElementNamesLabel.setEnabled(hasRequiredAPI & data.getValue());
				fIgnoredElementNames.setEnabled(hasRequiredAPI & data.getValue());
				if (data.getValue()) {
					fIgnoredElementNames.setFocus();
				}
			}
		}
	}

	/**
	 * Returns true in case of the Element Names to ignore preferences is changed
	 * causing the full validation to be requested.
	 */
	protected boolean shouldRevalidateOnSettingsChange() {
		TextData data = (TextData)fIgnoredElementNames.getData();
		if (data.isChanged())
			return true;
		
		BooleanData ignoreData = (BooleanData)fIgnoreElementNames.getData();
		if (ignoreData.isChanged())
			return true;
		
		return super.shouldRevalidateOnSettingsChange();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.wst.sse.ui.internal.preferences.ui.AbstractSettingsPage#storeValues()
	 */
	protected void storeValues() {
		if (HTMLCoreNewPreferences.hasRequiredAPI()) {
			IScopeContext[] contexts = createPreferenceScopes();
	
			BooleanData ignoreData = (BooleanData)fIgnoreElementNames.getData();
			contexts[0].getNode(getPreferenceNodeQualifier()).putBoolean(ignoreData.getKey(), ignoreData.getValue()); 
			ignoreData.originalValue = ignoreData.getValue();
			
			TextData data = (TextData)fIgnoredElementNames.getData();
			contexts[0].getNode(getPreferenceNodeQualifier()).put(data.getKey(), data.getValue()); 
			data.originalValue = data.getValue();
			
			for(int i = 0; i < contexts.length; i++) {
				try {
					contexts[i].getNode(getPreferenceNodeQualifier()).flush();
				} catch (BackingStoreException e) {
					Logger.logException(e);
				}
			}
		}
		super.storeValues();
		forceReconciling();
	}

	private void forceReconciling() {
		IPreferenceStore store = SSEUIPlugin.getDefault().getPreferenceStore();
		boolean value = store.getBoolean(CommonEditorPreferenceNames.EVALUATE_TEMPORARY_PROBLEMS);
		store.setValue(CommonEditorPreferenceNames.EVALUATE_TEMPORARY_PROBLEMS, !value);
		store.setValue(CommonEditorPreferenceNames.EVALUATE_TEMPORARY_PROBLEMS, value);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
	 */
	protected void performDefaults() {
		if (HTMLCoreNewPreferences.hasRequiredAPI()) {
			resetIgnoreElementNamesPattern();
			resetSeverities();
		}
		super.performDefaults();
	}
	
	protected void resetIgnoreElementNamesPattern() {
		if (!HTMLCoreNewPreferences.hasRequiredAPI()) return;
		IEclipsePreferences defaultContext = new DefaultScope().getNode(getPreferenceNodeQualifier());
		BooleanData ignoreData = (BooleanData)fIgnoreElementNames.getData();
		boolean ignoreElementNames = defaultContext.getBoolean(ignoreData.getKey(), HTMLCoreNewPreferences.IGNORE_ELEMENT_NAMES_DEFAULT);
		ignoreData.setValue(ignoreElementNames);
		fIgnoreElementNames.setSelection(ignoreData.getValue());

		TextData data = (TextData)fIgnoredElementNames.getData();
		String ignoredElementNames = defaultContext.get(data.getKey(), HTMLCoreNewPreferences.ELEMENT_NAMES_TO_IGNORE_DEFAULT);
		data.setValue(ignoredElementNames);
		fIgnoredElementNames.setText(data.getValue());
		
		controlChanged(fIgnoreElementNames);
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

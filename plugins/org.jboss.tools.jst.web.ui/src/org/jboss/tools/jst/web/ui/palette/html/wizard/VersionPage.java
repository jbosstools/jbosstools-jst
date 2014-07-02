/******************************************************************************* 
 * Copyright (c) 2014 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.palette.html.wizard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.jboss.tools.common.model.ui.editors.dnd.IElementGenerator.ElementNode;
import org.jboss.tools.common.model.ui.editors.dnd.IElementGenerator.NodeWriter;
import org.jboss.tools.common.model.ui.editors.dnd.IElementGenerator.RootNode;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.ui.internal.preferences.js.IPreferredJSLibVersion;
import org.jboss.tools.jst.web.ui.internal.preferences.js.JSCSSPreferencePage;
import org.jboss.tools.jst.web.ui.internal.preferences.js.JSLib;
import org.jboss.tools.jst.web.ui.internal.preferences.js.JSLibFactory;
import org.jboss.tools.jst.web.ui.internal.preferences.js.JSLibVersion;
import org.jboss.tools.jst.web.ui.internal.preferences.js.PreferredJSLibVersions;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryFieldEditorFactory;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public abstract class VersionPage extends AbstractWizardPageWithPreview implements IPreferredJSLibVersion, HTMLConstants {
	protected Map<String, IFieldEditor> editors = new HashMap<String, IFieldEditor>();
	protected Map<String, String> checkboxIds = new HashMap<String, String>();
	protected Map<String, String> selectorIds = new HashMap<String, String>();

	protected StyledText text;
	protected Label scale;

	protected VersionPage(String pageName, String title) {
		super(pageName, title);
	}

	public void createFields() {
		createFieldPanel(null);
	}

	public void setVisible(boolean b) {
		if(b && getWizard().getPages()[0] != this) {
			panel.getShell().layout(true, true);
		}
		super.setVisible(b);
		if(b) {
			getContainer().updateMessage();
			textLimit = getTextLimit();
			resetText();
		}
	}

	public boolean shouldAddLib(String libName) {
		return "true".equals(getEditorValue("add " + libName));
	}

	public String getLibVersion(String libName) {
		return getEditorValue("select " + libName);
	}

	protected void createFieldPanel(Composite parent) {
		for (JSLib lib: JSLibFactory.getInstance().getPreferenceModel().getSortedLibs()) {
			if(lib.getVersions().isEmpty()) continue;
			String libName = lib.getName();
			List<String> versions = new ArrayList<String>(lib.getVersionNames());
			String defaultVersion = getPreferredVersions().getLibVersion(libName);

			boolean isAddSelected = getPreferredVersions().shouldAddLib(libName);
			boolean isEnabled = !getPreferredVersions().isLibDisabled(libName);

			String checkboxId = "add " + libName;
			IFieldEditor checkbox = JQueryFieldEditorFactory.createAddJSLibEditor(checkboxId, libName, isAddSelected && isEnabled);
			addEditor(checkbox, parent);
			checkboxIds.put(libName, checkboxId);

			String selectorId = "select " + libName;
			IFieldEditor selector = JQueryFieldEditorFactory.createJSLibVersionEditor(selectorId, versions, defaultVersion);
			addEditor(selector, parent);
			selectorIds.put(libName, selectorId);

			if(!isEnabled && fields != null) {
				checkbox.setEnabled(false);
				selector.setEnabled(false);
				if(defaultMessage == null) {
					setMessage(defaultMessage = "Page already contains " + libName + " library.", IMessageProvider.INFORMATION);
				}
			}
		}

		if(parent != null) createReference(parent);
	}

	void reloadFields() {
		if(fields == null) return;
		getPreferredVersions().applyLibPreference(this);
		for (Control c: fields.getChildren()) {
			c.dispose();
		}
		editors.clear();
		checkboxIds.clear();
		selectorIds.clear();
		getPreferredVersions().updateLibEnablementAndSelection();
		createFieldPanel(fields);
		fields.update();
		fields.layout();

		updatePreviewContent();
		runValidation();
	}

	protected abstract PreferredJSLibVersions getPreferredVersions();

	protected void createPreview() {
		createTextPreview(panel);
	}

	void createTextPreview(Composite parent) {
		text = new StyledText(parent, SWT.MULTI | SWT.READ_ONLY | SWT.BORDER | SWT.V_SCROLL);
		text.setFont(JFaceResources.getTextFont());
		text.setLayoutData(new GridData(GridData.FILL_BOTH));
		/**
		 * We set some initial content to the text widget to provide a reasonable default width
		 * for that widget and for browser. We avoid setting width hint or other ways to 
		 * provide the default width, because text widget and browser should be resizable 
		 * and their content will be formatted to the available width. Also, initial width
		 * is to depend on system font size so that initial content serves best to that purpose. 
		 */
		text.setText("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n<html><body>aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa</body></html>");
		previewPanel = text;
	}

	protected IFieldEditor createAddLibsEditor(Composite parent) {
		GridData d = new GridData(GridData.FILL_HORIZONTAL);
		scale = new Label(parent, 0);
		scale.setVisible(false);
		scale.setText("pppppppppppppppppppppppp");
		scale.setLayoutData(d);
		return null;
	}

	public void addEditor(IFieldEditor editor) {
		editors.put(editor.getName(), editor);
	}

	@Override
    public VersionedNewHTMLWidgetWizard<?,?> getWizard() {
        return (VersionedNewHTMLWidgetWizard<?,?>)super.getWizard();
    }

	public void addEditor(IFieldEditor editor, Composite parent) {
		if(parent != null) editor.doFillIntoGrid(parent);
		editor.addPropertyChangeListener(this);
		addEditor(editor);
		//no content assist
	}

	public IFieldEditor getEditor(String name) {
		return editors.get(name);
	}

	public String getEditorValue(String name) {
		return !editors.containsKey(name) ? null : getEditor(name).getValueAsString();
	}

	protected void createReference(Composite parent) {
		StyledText text = new StyledText(parent, 0);
		text.setBackground(parent.getBackground());
		text.setText(WizardMessages.configureJSCSSLabel);
		text.setCursor(Display.getDefault().getSystemCursor(SWT.CURSOR_HAND));
		GridData d = new GridData();
		d.horizontalSpan = 3;
		text.setLayoutData(d);
		
		StyleRange range = new StyleRange();
		range.start = 0;
		range.length = text.getText().length();
		range.foreground = Display.getDefault().getSystemColor(SWT.COLOR_BLUE);
		range.background = parent.getBackground();
		range.underline = true;
		text.setStyleRanges(new StyleRange[]{range});
		text.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				PreferenceDialog dialog = PreferencesUtil.createPreferenceDialogOn(
						Display.getDefault().getActiveShell(), JSCSSPreferencePage.ID, new String[]{JSCSSPreferencePage.ID}, null);
			    int result = dialog.open();
			    if(result == Window.OK) {
			    	reloadFields();
			    }
			}
		});
	}

	protected String defaultMessage = null;

	protected void updatePreviewContent() {
		resetText();
	}

	private void resetText() {
		String text = AbstractNewHTMLWidgetWizardPage.formatText(getTextForTextView(), this.text, getTextLimit());
		this.text.setStyleRanges(new StyleRange[0]);
		this.text.setText(text);
		this.text.setStyleRanges(AbstractNewHTMLWidgetWizardPage.getRanges(text));
		this.text.update();
		this.text.layout();
	}

	public String getTextForTextView() {
		ElementNode root = RootNode.newRoot();
		addContent(root);
		NodeWriter sb = new NodeWriter(false);
		root.flush(sb, 0);
		return sb.getText();
	}

	void addContent(ElementNode root) {
		ElementNode head = null;

		Collection<JSLib> libs = JSLibFactory.getInstance().getPreferenceModel().getLibs();

		for (JSLib lib: libs) {
			String libName = lib.getName();
			if(getPreferredVersions().isLibDisabled(libName)) continue;
			if(TRUE.equals(getEditorValue(checkboxIds.get(libName)))) {
				String versionName = getEditorValue(selectorIds.get(libName));
				if(versionName == null) continue;
				JSLibVersion version = lib.getVersion(versionName);
				
				if(head == null) {
					head = root.addChild(TAG_HEAD, null);
				}
				List<String> urls = version.getURLs();
				for (String url: urls) {
					if(version.isJS(url)) {
						ElementNode script = head.addChild(TAG_SCRIPT);
						script.addAttribute(ATTR_SRC, url);
					} else {
						ElementNode link = head.addChild(TAG_LINK);
						link.addAttribute(ATTR_REL, "stylesheet");
						link.addAttribute(ATTR_TYPE, "text/css");
						link.addAttribute(ATTR_HREF, url);
					}
				}
			}
		}
		if(head == null) {
			root.addChild(TAG_HEAD, "");
		}
	}

	int getTextLimit() {
		int c = text.getSize().x - 2;
		if(text.getVerticalBar() != null) {
			int w = text.getVerticalBar().getSize().x;
			if(w > 0) {
				c -= w + 5;
			}
		}
		return c < 20 ? 20 : c;
	}

	int textLimit = -1;

}

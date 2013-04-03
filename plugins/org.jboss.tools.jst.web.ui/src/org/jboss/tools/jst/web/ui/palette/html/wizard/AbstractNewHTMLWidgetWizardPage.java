/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.palette.html.wizard;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.compare.Splitter;
import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.common.model.ui.editors.dnd.DefaultDropWizardPage;
import org.jboss.tools.common.ui.widget.editor.CheckBoxFieldEditor;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.common.util.FileUtil;
import org.jboss.tools.common.util.SwtUtil;
import org.jboss.tools.jst.web.ui.WebUiPlugin;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class AbstractNewHTMLWidgetWizardPage extends DefaultDropWizardPage implements PropertyChangeListener {
	protected Button showPreviewButton = null;

	protected Composite left = null;
	protected Map<String, IFieldEditor> editors = new HashMap<String, IFieldEditor>();

	protected Splitter previewPanel = null;
	protected Text text;
	protected Browser browser;
	protected File sourceFile = null;
	protected String sourceURL = null;	
	
	public AbstractNewHTMLWidgetWizardPage(String pageName, String title) {
		super(pageName, title);
	}

	public AbstractNewHTMLWidgetWizardPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

    public AbstractNewHTMLWidgetWizard getWizard() {
        return (AbstractNewHTMLWidgetWizard)super.getWizard();
    }

	@Override
	public void createControl(Composite parent) {
		final Composite panel = new Composite(parent, SWT.NONE);
		GridData d = new GridData(GridData.FILL_BOTH);
		panel.setLayoutData(d);
		GridLayout layout = new GridLayout(3, false);
		panel.setLayout(layout);

		left = new Composite(panel, SWT.BORDER);
		d = new GridData(GridData.FILL_BOTH);
		d.minimumWidth = 400;
		left.setLayoutData(d);
		left.setLayout(new GridLayout(2, false));
		
		Composite fields = new Composite(left, SWT.NONE);
		d = new GridData(GridData.FILL_BOTH);
		d.horizontalSpan = 2;
		fields.setLayoutData(d);
		fields.setLayout(new GridLayout(3, false));

		setUpdating(true);
		createFieldPanel(fields);
		setUpdating(false);
		
		createSeparator(left);
		
		createAddLibsEditor(left);

		showPreviewButton = new Button(left, SWT.PUSH);
		d = new GridData();
		d.minimumWidth = 100;
		showPreviewButton.setText(WizardMessages.showPreviewButtonText);
		showPreviewButton.setLayoutData(d);
		showPreviewButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				flipPreview(false);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
	
		previewPanel = new Splitter(panel, SWT.VERTICAL);
		d = new GridData(GridData.FILL_VERTICAL);
		d.horizontalSpan = 2;
		d.widthHint = 0;
		previewPanel.setVisible(false);
		previewPanel.setLayoutData(d);
		previewPanel.setLayout(new GridLayout());
		
		text = new Text(previewPanel, SWT.MULTI | SWT.READ_ONLY | SWT.BORDER | SWT.V_SCROLL);
		text.setLayoutData(new GridData(GridData.FILL_BOTH));
			text.setText("<html><body>aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa</body></html>");

		/*
		//We can provide webkit in this way
		String property = "org.eclipse.swt.browser.DefaultType";
		String defaultBrowser = System.getProperty(property);
		boolean hasDefaultBrowser = defaultBrowser != null;
		System.getProperties().setProperty(property, "webkit");
		*/

		Composite browserPanel = createBrowserPanel(previewPanel);

		try {
			try {
				browser = new Browser(browserPanel, SWT.READ_ONLY | SWT.MOZILLA | SWT.NO_SCROLL);
			} catch (SWTError e) {
				try {
					browser = new Browser(browserPanel, SWT.READ_ONLY | SWT.NONE | SWT.NO_SCROLL);
				} catch (SWTError e1) {
					WebUiPlugin.getDefault().logError("Cannot create neither Mozilla nor default browser.", e1);
				}
			}
		} finally {
			/*
			//Use if system property was modified
			if(hasDefaultBrowser) {
				System.getProperties().setProperty(property, defaultBrowser);
			}
			*/
		}
//		browser.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		if(browser != null) {
			browser.setLayoutData(gridData);
			browser.pack();
		}

		createDisclaimer(browserPanel);

		previewPanel.setWeights(new int[]{4,6});
		
//		updatePreviewContent();

		setControl(panel);
		
		Display.getCurrent().asyncExec(new Runnable() {
			public void run() {
				if(text == null || text.isDisposed()) {
					return;
				}
				flipPreview(true);
				updatePreviewContent();
//				setVisible(true);
				runValidation();
			}
		});
//		parent.pack(true);
	}

	public String getBrowserType() {
		return browser == null ? null : browser.getBrowserType();
	}

	static final String SECTION_NAME = "InsertTag";
	public static final String ADD_JS_CSS_SETTING_NAME = "addJSCSS";

	IFieldEditor createAddLibsEditor(Composite parent) {
		boolean addJSCSS = true; 
		IDialogSettings settings = WebUiPlugin.getDefault().getDialogSettings();
		IDialogSettings insertTagSettings = settings.getSection(SECTION_NAME);
		if(insertTagSettings != null) {
			addJSCSS = insertTagSettings.getBoolean(ADD_JS_CSS_SETTING_NAME);
		} else {
			insertTagSettings = DialogSettings.getOrCreateSection(settings, SECTION_NAME);
			insertTagSettings.put(ADD_JS_CSS_SETTING_NAME, true);
		}
		final IFieldEditor addLibs = new CheckBoxFieldEditor(ADD_JS_CSS_SETTING_NAME, WizardMessages.addReferencesToJSCSSLabel, Boolean.valueOf(addJSCSS)){
			public void doFillIntoGrid(Object parent) {
				Composite c = (Composite) parent;
				final Control[] controls = (Control[]) getEditorControls(c);
				Button button = (Button)controls[0];
				button.setText(WizardMessages.addReferencesToJSCSSLabel);
				button.setToolTipText(WizardMessages.addReferencesToJSCSSTooltip);
				GridData d = new GridData(GridData.FILL_HORIZONTAL);
				d.horizontalSpan = 1;
				d.minimumWidth = 200;
				button.setLayoutData(d);
			}
		};
		addLibs.doFillIntoGrid(parent);
		addEditor(addLibs);
		addLibs.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				IDialogSettings settings = WebUiPlugin.getDefault().getDialogSettings();
				IDialogSettings insertTagSettings = settings.getSection(SECTION_NAME);
				insertTagSettings.put(ADD_JS_CSS_SETTING_NAME, Boolean.parseBoolean(addLibs.getValue().toString()));
			}
		});
		
		return addLibs;
	}

	private Composite createBrowserPanel(Composite previewPanel) {
		Composite browserPanel = new Composite(previewPanel, SWT.BORDER);
		GridData g = new GridData();
		g.horizontalAlignment = SWT.FILL;
		g.verticalAlignment = SWT.FILL;
		g.grabExcessHorizontalSpace = true;
		g.grabExcessVerticalSpace = true;
		browserPanel.setLayoutData(g);
		GridLayout l = new GridLayout();
		l.verticalSpacing = 0;
		l.marginWidth = 0;
		l.marginHeight = 0;
		browserPanel.setLayout(l);
		return browserPanel;
	}

	private void createDisclaimer(Composite browserPanel) {
		Label disclaimer = new Label(browserPanel, SWT.NONE);
		disclaimer.setText(WizardMessages.previewDisclaimer);
		GridData disclaimerData = new GridData();
		disclaimerData.horizontalAlignment = SWT.CENTER;
		disclaimer.setLayoutData(disclaimerData);
		FontData fd = disclaimer.getFont().getFontData()[0];
		fd.setStyle(SWT.ITALIC);
		fd.setHeight(8);
		Font font = new Font(null, fd);
		disclaimer.setFont(font);
		SwtUtil.bindDisposal(font, disclaimer);
	}

	/**
	 * override it
	 * @param parent
	 */
	protected void createFieldPanel(Composite parent) {		
	}

	public void addEditor(IFieldEditor editor) {
		editors.put(editor.getName(), editor);
	}

	public void addEditor(IFieldEditor editor, Composite parent) {
		editor.doFillIntoGrid(parent);
		editor.addPropertyChangeListener(this);
		addEditor(editor);
	}

	public void addEditor(IFieldEditor editor, Composite parent, boolean expandCombo) {
		addEditor(editor, parent);
		if(expandCombo) {
			expandCombo(editor);
		}
	}

	public void createSeparator(Composite parent) {
		Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData sd = new GridData(GridData.FILL_HORIZONTAL);
		sd.horizontalSpan = 3;
		separator.setLayoutData(sd);
	}

	/**
	 * Utility method expanding combo
	 * @param name
	 * @return
	 */
	public void expandCombo(IFieldEditor editor) {
		Control c = (Control) (editor.getEditorControls()[1]);
		GridData d = (GridData)c.getLayoutData();
		d.horizontalAlignment = SWT.FILL;
		d.grabExcessHorizontalSpace = true;
		c.setLayoutData(d);
	}

	public IFieldEditor getEditor(String name) {
		return editors.get(name);
	}

	public String getEditorValue(String name) {
		return !editors.containsKey(name) ? null : getEditor(name).getValueAsString();
	}

	public void setEditorValue(String name, String value) {
		if(editors.containsKey(name)) {
			getEditor(name).setValue(value);
		}
	}

	boolean isUpdating = false;
	int updateRequest = 0;
	
	private synchronized void setUpdating(boolean b) {
		isUpdating = b;
	}

	protected int getAdditionalHeight() {
		return 0;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		runValidation();
		if(!isUpdating) {
			setUpdating(true);
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					updateRequest++;
					while(true) {
						int u = updateRequest;
						try {
							Thread.sleep(300);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if(u == updateRequest) break;
					}
					if(getShell() == null || getShell().isDisposed()) {
						return;
					}
					getShell().getDisplay().asyncExec(new Runnable() {
						public void run() {
							try {
								while(updateRequest > 0) {
									updateRequest = 0;
									if(getShell() == null || getShell().isDisposed()) {
										return;
									}
									updatePreviewContent();
								}
							} finally {
								setUpdating(false);
							}
						}
					});
				}
			});
			t.start();
		} else {
			updateRequest++;
		}
	}

	void flipPreview(boolean first) {
		if(previewPanel == null || previewPanel.isDisposed()) {
			return;
		}
		if(previewPanel.isVisible()) {			
			previewPanel.setVisible(false);
			showPreviewButton.setText(WizardMessages.showPreviewButtonText);
			GridData d = new GridData(GridData.FILL_VERTICAL);
			d.widthHint = 0;
			previewPanel.setLayoutData(d);
			d = new GridData(GridData.FILL_BOTH);
			left.setLayoutData(d);
			updatePreviewPanel(false, first);
		} else {
			showPreviewButton.setText(WizardMessages.hidePreviewButtonText);
			GridData d = new GridData(GridData.FILL_VERTICAL);
			d.minimumWidth = 400;
			left.setLayoutData(d);
			d = new GridData(GridData.FILL_BOTH);
			int delta = 400;
			d.minimumWidth = delta;
			previewPanel.setLayoutData(d);
			previewPanel.setVisible(true);
			updatePreviewPanel(true, first);
		}
	}
	
	int lastHideShellWidth = -1;
	int lastShowShellWidth = -1;

	private void updatePreviewPanel(boolean show, boolean first) {
		Shell shell = previewPanel.getShell();
		shell.update();
		shell.layout();

		Rectangle r = shell.getBounds();
		if(show) {
			lastHideShellWidth = r.width;
		} else {
			lastShowShellWidth = r.width;
		}
		int width = (first) ? shell.computeSize(-1, -1).x : 
			(show) ? (lastShowShellWidth < 0 ? r.width + 300 : lastShowShellWidth) : 
			(lastHideShellWidth < 0 ? r.width - 300 : lastHideShellWidth);
		if(first) {
			int dh =  left.computeSize(-1, -1).y - left.getSize().y;  //getAdditionalHeight();
			if(dh > 0) {
				r.y -= dh / 2;
				r.height += dh;
			}
			r.x -= (width - r.width) / 2;
		}
		r.width = width;
		shell.setBounds(r);
		shell.update();
		shell.layout();
	}

	File getFile() {
		if(sourceFile == null) {
			try {
				sourceFile = File.createTempFile("jquery_preview", ".html");
				sourceURL = sourceFile.toURI().toURL().toString();
			} catch (IOException e) {
				WebUiPlugin.getDefault().logError(e);
			}
		}
		return sourceFile;
	}

	protected void updatePreviewContent() {
		text.setText(formatText(getWizard().getTextForTextView()));
		if(browser == null) {
			return;
		}
		File f = getFile();
		FileUtil.writeFile(f, getWizard().getTextForBrowser());
		final Control c = Display.getCurrent().getFocusControl();
		if(browser.getUrl() == null || !browser.getUrl().endsWith(f.getName())
				||"mozilla".equals(getBrowserType())) {
			browser.setUrl(sourceURL);
		} else {
			browser.refresh();
		}
		if(c != null) {
			Display.getCurrent().asyncExec(new Runnable() {
				public void run() {
					c.forceFocus();
				}
			});
		}
//		browser.setText(getWizard().getTextForBrowser());
	}

	protected String formatText(String text) {
		int max = 40;
		StringBuilder sb = new StringBuilder();
		boolean inQuota = false;
		boolean inTag = false;
		int column = 0;
		for (int i = 0; i < text.length(); i++) {
			char ch = text.charAt(i);
			sb.append(ch);
			column++;
			if(ch == '"') {
				inQuota = !inQuota;
			} else if(ch == '<' && !inQuota) {
				inTag = true;
			} else if(ch == '>' && !inQuota) {
				inTag = false;
			} else if(ch == '\n') {
				column = 0;
			}
			if(column >= max && !inQuota && ch == ' ') {
				sb.append("\n");
				column = 0;
				if (inTag) {
					sb.append("        ");
				}
			}
		}
		return sb.toString();
	}

	public void dispose() {
		if(sourceFile != null) {
			sourceFile.delete();
			sourceFile = null;
		}
		super.dispose();
	}

}

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
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
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
		Composite panel = new Composite(parent, SWT.NONE);
		GridData d = new GridData(GridData.FILL_BOTH);
		panel.setLayoutData(d);
		GridLayout layout = new GridLayout(3, false);
		panel.setLayout(layout);

		left = new Composite(panel, SWT.BORDER);
		d = new GridData(GridData.FILL_VERTICAL);
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
		showPreviewButton.setText(WizardMessages.hidePreviewButtonText);
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
		d = new GridData(GridData.FILL_BOTH);
		previewPanel.setLayoutData(d);
		previewPanel.setLayout(new GridLayout());
		
		text = new Text(previewPanel, SWT.MULTI | SWT.READ_ONLY | SWT.BORDER | SWT.V_SCROLL);
		text.setLayoutData(new GridData(GridData.FILL_BOTH));
		/**
		 * We set some initial content to the text widget to provide a reasonable default width
		 * for that widget and for browser. We avoid setting width hint or other ways to 
		 * provide the default width, because text widget and browser should be resizable 
		 * and their content will be formatted to the available width. Also, initial width
		 * is to depend on system font size so that initial content serves best to that purpose. 
		 */
		text.setText("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n<html><body>aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa</body></html>");

		Composite browserPanel = createBrowserPanel(previewPanel);
		browser = createBrowser(browserPanel);

		if(browser != null) {
			browser.setLayoutData(new GridData(GridData.FILL_BOTH));
			browser.pack();
		}
		createDisclaimer(browserPanel);

		previewPanel.setWeights(new int[]{4,6});
		
		setControl(panel);
		
		Display.getCurrent().asyncExec(new Runnable() {
			public void run() {
				if(text == null || text.isDisposed()) {
					return;
				}
				updatePreviewPanel(true, true);
				updatePreviewContent();
				runValidation();
				text.addControlListener(new ControlAdapter() {
					public void controlResized(ControlEvent e) {
						if(textLimit < 0 || textLimit == getTextLimit()) return;
						text.setText(formatText(getWizard().getTextForTextView()));
					}
				});
			}
		});
		updatePreviewPanel(true, true);
		Display.getDefault().addFilter(SWT.FocusOut, focusReturn);
		Display.getDefault().addFilter(SWT.MouseDown, focusReturn);
		Display.getDefault().addFilter(SWT.KeyDown, focusReturn);
		Display.getDefault().addFilter(SWT.Modify, focusReturn);
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

	protected Browser createBrowser(Composite browserPanel) {
		/*
		//We can provide webkit in this way
		String property = "org.eclipse.swt.browser.DefaultType";
		String defaultBrowser = System.getProperty(property);
		boolean hasDefaultBrowser = defaultBrowser != null;
		System.getProperties().setProperty(property, "webkit");
		*/
		int browserIndex = getPreferredBrowser();
		try {
			try {
				return new Browser(browserPanel, SWT.READ_ONLY | browserIndex | SWT.NO_SCROLL);
			} catch (SWTError e) {
				try {
					return new Browser(browserPanel, SWT.READ_ONLY | SWT.NONE | SWT.NO_SCROLL);
				} catch (SWTError e1) {
					String message = "Cannot create neither Mozilla nor default browser";
					Exception ex = new HTMLWizardVisualPreviewInitializationException(message, e1);
					WebUiPlugin.getDefault().logError(message, ex);
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
		return null;
	}

	protected static final boolean isMacOS = "Mac OS X".equals(System.getProperty("os.name"));

	protected int getPreferredBrowser() {
		return isMacOS ? SWT.WEBKIT : SWT.MOZILLA;
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
			if(lastHideShellWidth < 0) {
				lastHideShellWidth = previewPanel.getShell().getSize().x - previewPanel.getSize().x;
			}
			previewPanel.setVisible(false);
			flipPreviewButton(WizardMessages.showPreviewButtonText);
			GridData d = new GridData(GridData.FILL_VERTICAL);
			d.widthHint = 0;
			previewPanel.setLayoutData(d);
			left.setLayoutData(new GridData(GridData.FILL_BOTH));
		} else {
			flipPreviewButton(WizardMessages.hidePreviewButtonText);
			left.setLayoutData(new GridData(GridData.FILL_VERTICAL));
			previewPanel.setLayoutData(new GridData(GridData.FILL_BOTH));
			previewPanel.setVisible(true);
		}
		updatePreviewPanel(previewPanel.isVisible(), first);
	}

	private void flipPreviewButton(String text) {
		showPreviewButton.setText(text);
		showPreviewButton.getParent().layout(true);
	}
	
	int lastHideShellWidth = -1;
	int lastShowShellWidth = -1;

	private void updatePreviewPanel(boolean show, boolean first) {
		Shell shell = previewPanel.getShell();
		shell.update();
		shell.layout();

		Rectangle r = shell.getBounds();
		if(show) {
			if(!first) lastHideShellWidth = r.width;
		} else {
			lastShowShellWidth = r.width;
		}
		int width = (first) ? shell.computeSize(-1, -1).x : 
			(show) ? (lastShowShellWidth < 0 ? r.width + 300 : lastShowShellWidth) : 
			(lastHideShellWidth < 0 ? r.width - 300 : lastHideShellWidth);
		if(!show && !first) {
			int dw = shell.computeSize(-1, -1).x;
			if(width < dw) width = dw;
		}
		if(first) {
			int dh =  left.computeSize(-1, -1).y - left.getSize().y;
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

		focusReturn.init();

		if(browser.getUrl() == null || !browser.getUrl().endsWith(f.getName())
				||"mozilla".equals(getBrowserType())) {
			browser.setUrl(sourceURL);
		} else {
			browser.refresh();
		}
	}

	boolean isFocusInBrowser() {
		Control c = Display.getDefault().getFocusControl();
		while(c != null && c != browser) {
			c = c.getParent();
		}
		return c == browser;
	}

	FocusReturn focusReturn = new FocusReturn();

	class FocusReturn implements Listener {
		Control c;
		long t;
		Point s;
		StringBuilder missed = new StringBuilder();
	
		void clear() {
			c = null;
			s = null;
			t = 0;
			missed.setLength(0);
		}

		private Point getSelection() {
			return (c instanceof Combo) ? ((Combo)c).getSelection()
				: (c instanceof Text) ? ((Text)c).getSelection()
				: null;
		}
		
		private String getText() {
			return (c instanceof Combo) ? ((Combo)c).getText()
				: (c instanceof Text) ? ((Text)c).getText()
				: null;
		}

		private void setText(String t) {
			if(c instanceof Combo) {
				((Combo)c).setText(t);
			} else if(c instanceof Text) {
				((Text)c).setText(t);
			}
		}
		
		private void setSelection() {
			if(c instanceof Combo) {
				((Combo)c).setSelection(s);
			} else if(c instanceof Text) {
				((Text)c).setSelection(s);
			}
		}
		
		void init() {
			clear();
			c = Display.getDefault().getFocusControl();
			if(c != null) {
				t = System.currentTimeMillis();
				s = getSelection();
			}
		}
	
		boolean isReady() {
			if(c == null) {
				return false;
			}
			if(System.currentTimeMillis() - t > 2000) {
				clear();
				return false;
			}
			return true;
		}

		void apply() {
			if(!isReady()) {
				return;
			}
			Display.getCurrent().asyncExec(new Runnable() {
				public void run() {
					if(c != null && c != Display.getCurrent().getFocusControl() && isFocusInBrowser()) {
						if(s != null && missed.length() > 0) {
							String t = getText();
							t = t.substring(0, s.x) + missed.toString() + t.substring(s.y);
							s.x += missed.length();
							s.y = s.x;
							setText(t);
						}
						c.forceFocus();
						if(s != null) {
							setSelection();
						}
					}
					clear();
				}
			});
		}

		@Override
		public void handleEvent(Event event) {
			if(event.type == SWT.MouseDown) {
				clear();
			} else if(event.type == SWT.Modify) {
				if(isReady() && c == Display.getDefault().getFocusControl() && c == event.widget) {
					s = getSelection();
				}
			} else if(event.type == SWT.KeyDown) {
				if(isReady() && c == Display.getDefault().getFocusControl()) {
					t = System.currentTimeMillis();
				}
				if(isReady() && event.widget == browser) {
					char ch = event.character;
					if((int)ch >= 32 && (int)ch <= 128) {
						missed.append(ch);
					}
				}
			} else if(event.type == SWT.FocusOut) {
				apply();
			}
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

	protected String formatText(String text) {
		GC gc = new GC(this.text);
		gc.setFont(this.text.getFont());
		int max = textLimit = getTextLimit();
		StringBuilder sb = new StringBuilder();
		boolean inQuota = false;
		boolean inTag = false;
		int offset = 0;
		for (int i = 0; i < text.length(); i++) {
			char ch = text.charAt(i);
			if(ch == '<' && !inQuota) {
				int n = lookUp(text, i);
				int w = gc.stringExtent(sb.substring(offset, sb.length()) + text.substring(i, n)).x;
				if(w > max) {
					sb.append("\n");
					offset = sb.length();
				}
			}
			sb.append(ch);
			if(ch == '"') {
				inQuota = !inQuota;
			} else if(ch == '<' && !inQuota) {
				inTag = true;
			} else if(ch == '>' && !inQuota) {
				inTag = false;
			} else if(ch == '\n') {
				offset = sb.length();
			}
			if(sb.length() > offset && !inQuota && (ch == ' ' || ch == '>')) {
				int l = lookUp(text, i + 1);
				int w = gc.stringExtent(sb.substring(offset, sb.length()) + text.substring(i, l)).x;
				if(l > i + 1 && w > max) {
					sb.append("\n");
					offset = sb.length();
					if (inTag) {
						String indent = "        ";
						sb.append(indent);
					}
				}
			}
		}
		gc.dispose();
		return sb.toString();
	}

	protected int lookUp(String text, int pos) {
		int res = pos;
		boolean inQuota = false;
		for (; res < text.length(); res++) {
			char ch = text.charAt(res);
			if(ch == '\n') return res;
			if(ch == '"') {
				inQuota = !inQuota;
			}
			if(!inQuota) {
				if(ch == ' ' || (res > pos && ch == '<')) return res;
				if(ch == '>') return res + 1;
			}
		}
		return res;
	}

	public void dispose() {
		if(sourceFile != null) {
			sourceFile.delete();
			sourceFile = null;
		}
		Display.getDefault().removeFilter(SWT.FocusOut, focusReturn);
		Display.getDefault().removeFilter(SWT.MouseDown, focusReturn);
		Display.getDefault().removeFilter(SWT.KeyDown, focusReturn);
		Display.getDefault().removeFilter(SWT.Modify, focusReturn);
		super.dispose();
	}

}

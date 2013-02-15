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
import java.util.HashMap;
import java.util.Map;

import org.eclipse.compare.Splitter;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.common.model.ui.editors.dnd.DefaultDropWizardPage;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;

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
		d = new GridData(GridData.FILL_BOTH);
		d.minimumWidth = 400;
		left.setLayoutData(d);
		left.setLayout(new GridLayout(2, false));
		
		Composite fields = new Composite(left, SWT.NONE);
		d = new GridData(GridData.FILL_BOTH);
		d.horizontalSpan = 2;
		fields.setLayoutData(d);
		fields.setLayout(new GridLayout(3, false));
		createFieldPanel(fields);
		
		Label label = new Label(left, SWT.NONE); 
		d = new GridData(GridData.FILL_HORIZONTAL);
		d.minimumWidth = 200;
		label.setLayoutData(d);
		showPreviewButton = new Button(left, SWT.PUSH);
		d = new GridData();
		d.minimumWidth = 100;
		showPreviewButton.setText(WizardMessages.showPreviewButtonText);
		showPreviewButton.setLayoutData(d);
		showPreviewButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				flipPreview();
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
			text.setText("<html><body>aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa</body></html>");

		/*
		//We can provide webkit in this way
		String property = "org.eclipse.swt.browser.DefaultType";
		String defaultBrowser = System.getProperty(property);
		boolean hasDefaultBrowser = defaultBrowser != null;
		System.getProperties().setProperty(property, "webkit");
		*/

		try {
			try {
				browser = new Browser(previewPanel, SWT.READ_ONLY | SWT.BORDER | SWT.MOZILLA | SWT.NO_SCROLL);
			} catch (SWTError e) {
				browser = new Browser(previewPanel, SWT.READ_ONLY | SWT.BORDER | SWT.WEBKIT | SWT.NO_SCROLL);
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
		browser.setLayoutData(gridData);
		browser.pack();
		previewPanel.setWeights(new int[]{4,6});
		
		updatePreviewContent();

		setControl(panel);
		setVisible(true);
		runValidation();
		flipPreview();
//		parent.pack(true);
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

	/**
	 * Utility method expanding combo
	 * @param name
	 * @return
	 */
	protected void expandCombo(IFieldEditor editor) {
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

	void flipPreview() {
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
			updatePreviewPanel(false);
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
			updatePreviewPanel(true);
		}
	}
	
	int lastHideShellWidth = -1;
	int lastShowShellWidth = -1;

	private void updatePreviewPanel(boolean show) {
		previewPanel.update();
		previewPanel.layout();
		Shell shell = previewPanel.getShell();
		Rectangle r = shell.getBounds();
		if(show) {
			lastHideShellWidth = r.width;
		} else {
			lastShowShellWidth = r.width;
		}
		int width = (show) ? (lastShowShellWidth < 0 ? r.width + 300 : lastShowShellWidth) : 
			(lastHideShellWidth < 0 ? r.width - 300 : lastHideShellWidth);
		shell.setBounds(new Rectangle(r.x, r.y, width, r.height));
		shell.update();
		shell.layout();
	}

	protected void updatePreviewContent() {
		text.setText(formatText(getWizard().getTextForTextView()));
		browser.setText(getWizard().getTextForBrowser());
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

}

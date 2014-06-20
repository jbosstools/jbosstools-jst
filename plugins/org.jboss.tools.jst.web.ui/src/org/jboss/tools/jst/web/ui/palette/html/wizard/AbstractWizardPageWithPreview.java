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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.common.model.ui.editors.dnd.DefaultDropWizardPage;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public abstract class AbstractWizardPageWithPreview extends DefaultDropWizardPage implements PropertyChangeListener {
	protected Button showPreviewButton = null;
	protected Composite panel = null;
	protected Composite left = null;
	protected Composite previewPanel = null;
	protected Composite fields = null;

	public AbstractWizardPageWithPreview(String pageName, String title) {
		super(pageName, title);
	}

	public AbstractWizardPageWithPreview(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	@Override
	public void createControl(Composite parent) {
		panel = new Composite(parent, SWT.NONE);
		GridData d = new GridData(GridData.FILL_BOTH);
		panel.setLayoutData(d);
		GridLayout layout = new GridLayout(3, false);
		panel.setLayout(layout);

		left = new Composite(panel, SWT.BORDER) {
			@Override
			public Point computeSize(int wHint, int hHint, boolean changed) {
				Point result = super.computeSize(wHint, hHint, changed);
				if(hHint < 0 && (getWizard() instanceof AbstractNewHTMLWidgetWizard)) {
					AbstractNewHTMLWidgetWizard w = (AbstractNewHTMLWidgetWizard)getWizard();
					if(w.getLeftPanelWidth() < 0) {
						w.setLeftPanelWidth(result.x, false);
					} else {
						result.x = w.getLeftPanelWidth();
					}
				}
				return result;
			}
		};
		d = new GridData(GridData.FILL_VERTICAL);
		left.setLayoutData(d);
		left.setLayout(new GridLayout(2, false));
		
		fields = new Composite(left, SWT.NONE);
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
				for (int i = 0; i < getWizard().getPageCount(); i++) {
					IWizardPage p = getWizard().getPages()[i];
					if(p instanceof AbstractWizardPageWithPreview) {
						((AbstractWizardPageWithPreview)p).flipPreview(false);
					}
				
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});

		createPreview();
	

		setControl(panel);

		startPreview();

		if(getWizard().getPages()[0] == this) {
			updatePreviewPanel(true, true);
		}
		onCreateControl();
	}

	protected void onCreateControl() {
	}

	protected void createPreview() {
	}

	protected void startPreview() {
	}

	/**
	 * override it
	 * @param parent
	 */
	protected void createFieldPanel(Composite parent) {		
	}

	public void createFields() {
		createFieldPanel(null);
	}

	protected IFieldEditor createAddLibsEditor(Composite parent) {
		return null;
	}

	public Composite getLeftPanel() {
		return left;
	}

	public boolean isPreviewClosed() {
		if(previewPanel == null || previewPanel.isDisposed()) return true;
		GridData d = (GridData)previewPanel.getLayoutData();
		return true;//d.widthHint == 0;
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
							break;
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

	protected boolean isPreviewPanelVisible = true;

	void flipPreview(boolean first) {
		if(previewPanel == null || previewPanel.isDisposed()) {
			return;
		}
		if(isPreviewPanelVisible) {
			if(lastHideShellWidth < 0) {
				lastHideShellWidth = previewPanel.getShell().getSize().x - previewPanel.getSize().x + 5;
			}
			previewPanel.setVisible(false);
			isPreviewPanelVisible = false;
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
			isPreviewPanelVisible = true;
		}
		if(isCurrentPage()) {
			updatePreviewPanel(isPreviewPanelVisible, first);
		}
	}

	private void flipPreviewButton(String text) {
		showPreviewButton.setText(text);
		showPreviewButton.getParent().layout(true);
	}
	
	int lastHideShellWidth = -1;
	int lastShowShellWidth = -1;

	protected void updatePreviewPanel(boolean show, boolean first) {
		Shell shell = previewPanel.getShell();
		shell.update();
		shell.layout();

		Rectangle r = shell.getBounds();
		if(show) {
			if(!first) lastHideShellWidth = r.width;
		} else {
			lastShowShellWidth = r.width;
		}
		int defaultWidth = (first) ? shell.computeSize(-1, -1).x : 0;
		if(defaultWidth < 550) defaultWidth = 550;
		int width = (first) ? defaultWidth : 
			(show) ? (lastShowShellWidth < 0 ? r.width + 300 : lastShowShellWidth) : 
			(lastHideShellWidth < 0 ? r.width - 300 : lastHideShellWidth);
		if(!show && !first) {
			int dw = defaultWidth;
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

	public void createSeparator(Composite parent) {
		if(parent == null) return;
		Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData sd = new GridData(GridData.FILL_HORIZONTAL);
		sd.horizontalSpan = 3;
		separator.setLayoutData(sd);
	}

	protected abstract void updatePreviewContent();
}

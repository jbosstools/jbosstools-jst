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
package org.jboss.tools.jst.web.ui.internal.preferences.js;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.common.ui.widget.editor.SwtFieldEditorFactory;
import org.jboss.tools.jst.web.ui.WebUiPlugin;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class JSCSSPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage {
	public static final String ID = "org.jboss.tools.jst.web.ui.JSCSSPreferencePage"; //$NON-NLS-1$

	JSLibModel model;
	TreeViewer tree;

	Button editButton, addJSButton, removeButton;

	public void init(IWorkbench workbench) {
	}

	protected Control createContents(Composite parent) {
		model = JSLibFactory.getInstance().getWorkingCopy();

		Composite libs = new Composite(parent, SWT.NULL);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.grabExcessHorizontalSpace = true;
		libs.setLayoutData(data);

		GridLayout layout = new GridLayout();
		libs.setLayout(layout);
		int heightHint = convertVerticalDLUsToPixels(14/*IDialogConstants.BUTTON_HEIGHT*/);
		int widthHint = convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
		
		Group gd = new Group(libs, 0);
		gd.setText(WizardMessages.descriptionLabel);
		GridData dl = new GridData(GridData.FILL_HORIZONTAL);
		gd.setLayoutData(dl);
		gd.setLayout(new GridLayout());
		Label label = new Label(gd, 0);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		label.setText(WizardMessages.configureJSCSSDescription);

		Group gt = new Group(libs, 0);
		layout = new GridLayout();
		layout.numColumns = 2;
		gt.setLayout(layout);
		gt.setLayoutData(new GridData(GridData.FILL_BOTH));
		gt.setText(WizardMessages.JSCSSReferencesLabel);
		tree = new TreeViewer(gt);
		tree.setAutoExpandLevel(2);
		tree.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		LibsContentProvider contentProvider = new LibsContentProvider();
		tree.setContentProvider(contentProvider);
		LibsLabelProvider labelProvider = new LibsLabelProvider();
		tree.setLabelProvider(labelProvider);
		tree.setInput(model);
		tree.addSelectionChangedListener(new SL());

		Composite buttons = new Composite(gt, 0);
		GridData db = new GridData(GridData.BEGINNING | GridData.FILL_VERTICAL);
		buttons.setLayoutData(db);
		GridLayout l = new GridLayout();
		l.marginTop = 0;
		l.marginHeight= 0;
		buttons.setLayout(l);
		
		Button newButton = new Button(buttons, SWT.PUSH);
		newButton.setText(WizardMessages.newButtonLabel);
		GridData d = new GridData(GridData.BEGINNING);
		d.widthHint = widthHint;
		d.heightHint=heightHint;
		newButton.setLayoutData(d);

		editButton = new Button(buttons, SWT.PUSH);
		editButton.setText(WizardMessages.editButtonLabel);
		d = new GridData(GridData.BEGINNING);
		d.widthHint = widthHint;
		d.heightHint=heightHint;
		editButton.setLayoutData(d);

		addJSButton = new Button(buttons, SWT.PUSH);
		addJSButton.setText(WizardMessages.addJSCSSButtonLabel);
		d = new GridData(GridData.BEGINNING);
		d.widthHint = widthHint;
		d.heightHint=heightHint;
		addJSButton.setLayoutData(d);

		removeButton = new Button(buttons, SWT.PUSH);
		removeButton.setText(WizardMessages.removeActionLabel);
		d = new GridData(GridData.BEGINNING);
		d.widthHint = widthHint;
		d.heightHint=heightHint;
		removeButton.setLayoutData(d);

		newButton.addSelectionListener(new DefaultSelectionListener() {			
			@Override
			public void widgetSelected(SelectionEvent e) {
				NewJSLibDialog dialog = new NewJSLibDialog();
				int result = dialog.open();
				if(result == Dialog.OK) {
					JSLib lib = model.getOrCreateLib(dialog.getName());
					lib.getOrCreateVersion(dialog.getVersion());
					refreshTree();
				}					
			}
		});

		editButton.addSelectionListener(new DefaultSelectionListener() {			
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object s = getSelectedObject();
				if(s instanceof JSLibVersion) {
					JSLibVersion v = (JSLibVersion)s;
					EditJSLibDialog dialog = new EditJSLibDialog(v);
					int result = dialog.open();
					if(result == Dialog.OK) {
						if(v.getLib().getName().equals(dialog.getName())) {
							v.getLib().removeVersion(v);
							v.setVersion(dialog.getVersion());
							v.getLib().addVersion(v);
						} else {
							v.getLib().removeVersion(v);
							JSLib lib = model.getOrCreateLib(dialog.getName());
							lib.addVersion(v);
						}
						refreshTree();
					}
				} else if(s instanceof URLWrapper) {
					URLWrapper w = (URLWrapper)s;
					EditFileRefDialog dialog = new EditFileRefDialog(w.parent, w);
					int result = dialog.open();
					if(result == Dialog.OK) {
						w.parent.getURLs().remove(w.url);
						w.parent.getURLs().add(dialog.getURL());
						refreshTree();
					}
				}
			}
		});

		addJSButton.addSelectionListener(new DefaultSelectionListener() {			
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object s = getSelectedObject();
				JSLibVersion v = null;
				if(s instanceof JSLibVersion) {
					v = (JSLibVersion)s;
				} else if(s instanceof URLWrapper) {
					v = ((URLWrapper)s).parent;
				}
				if(v != null) {
					AddFileRefDialog dialog = new AddFileRefDialog(v);
					int result = dialog.open();
					if(result == Dialog.OK) {
						v.getURLs().add(dialog.getURL());
						refreshTree();
					}					
				}
			}
		});

		removeButton.addSelectionListener(new DefaultSelectionListener() {			
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object s = getSelectedObject();
				if(s instanceof JSLibVersion) {
					JSLibVersion v = (JSLibVersion)s;
					JSLib lib = v.getLib();
					lib.removeVersion(v);
					if(lib.getVersions().isEmpty()) {
						model.removeLib(lib);
					}
					refreshTree();
				} else if(s instanceof URLWrapper) {
					URLWrapper w = (URLWrapper)s;
					w.parent.getURLs().remove(w.url);
					refreshTree();
				}
			}
		});

		updateButtons();

		return libs;
	}

	Object getSelectedObject() {
		ISelection s = tree.getSelection();
		if(s.isEmpty()) return null;
		if(s instanceof IStructuredSelection) {
			return ((IStructuredSelection)s).getFirstElement();
		}
		return null;
	}

	void refreshTree() {
		if(tree.getControl() != null && !tree.getControl().isDisposed()) {
			tree.refresh();
		}
	}

	protected void performDefaults() {
		JSLibFactory.getInstance().applyDefault();
		refreshTree();
	}

	protected void performApply() {
		JSLibFactory.getInstance().applyWorkingCopy();
	}

    public boolean performOk() {
    	JSLibFactory.getInstance().applyWorkingCopy();
    	JSLibFactory.getInstance().disposeWorkingCopy();
    	JSLibFactory.getInstance().savePreferenceModel();
        return true;
    }

    public boolean performCancel() {
    	JSLibFactory.getInstance().disposeWorkingCopy();
        return true;
    }

	public static void expandCombo(IFieldEditor editor) {
		Control c = (Control) (editor.getEditorControls()[1]);
		GridData d = (GridData)c.getLayoutData();
		d.horizontalAlignment = SWT.FILL;
		d.grabExcessHorizontalSpace = true;
		c.setLayoutData(d);
	}

	class SL implements ISelectionChangedListener {

		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			updateButtons();
		}
		
	}

	void updateButtons() {
		if(tree == null || tree.getControl().isDisposed()) {
			return;
		}
		ISelection s = tree.getSelection();
		boolean isEnabled = !s.isEmpty();
		addJSButton.setEnabled(isEnabled);
		editButton.setEnabled(isEnabled);
		removeButton.setEnabled(isEnabled);
	}

}

abstract class DefaultSelectionListener implements SelectionListener {

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		widgetSelected(e);		
	}
	
}

class URLWrapper {
	String url;
	JSLibVersion parent;
	public URLWrapper(String url, JSLibVersion parent) {
		this.url = url;
		this.parent= parent;
	}

	public String toString() {
		return url;
	}
}

class LibsContentProvider implements ITreeContentProvider {
	JSLibModel model;

	Map<String, URLWrapper> urls = new HashMap<String, URLWrapper>();

	public LibsContentProvider() {
	}
	@Override
	public void dispose() {
		urls.clear();
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		model = (JSLibModel)newInput;
		if(viewer != null && model != null && viewer.getControl() != null && !viewer.getControl().isDisposed()) {
			viewer.refresh();
		}
	}

	@Override
	public Object[] getElements(Object inputElement) {
		List<JSLibVersion> versions = new ArrayList<JSLibVersion>();
		if(model != null) {
			for (JSLib lib: model.getSortedLibs()) {
				for (JSLibVersion v: lib.getSortedVersions()) {
					versions.add(v);
				}
			}
		}
		return versions.toArray();
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof JSLibVersion) {
			List<URLWrapper> result = new ArrayList<URLWrapper>();
			JSLibVersion v = (JSLibVersion)parentElement;
			for (String s: v.getSortedUrls()) {
				URLWrapper w = urls.get(s);
				if(w == null) {
					w = new URLWrapper(s, v);
					urls.put(s, w);
				} else {
					w.parent = v;
					w.url = s;
				}
				result.add(w);
			}
			return result.toArray();
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		if(element instanceof URLWrapper) {
			return ((URLWrapper)element).parent;
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return element instanceof JSLibVersion;
	}
	
}

class LibsLabelProvider extends BaseLabelProvider implements ILabelProvider {

	@Override
	public Image getImage(Object element) {
		if(element instanceof JSLibVersion) {
			return WebUiPlugin.getImageDescriptor(WebUiPlugin.JS_LIB_IMAGE_PATH).createImage();
		} else if(element != null) {
			String s = element.toString();
			if(s.endsWith(".css")) {
				return WebUiPlugin.getImageDescriptor(WebUiPlugin.JS_LIB_CSS_IMAGE_PATH).createImage();
			} else {
				return WebUiPlugin.getImageDescriptor(WebUiPlugin.JS_LIB_JS_IMAGE_PATH).createImage();
			}
		}
		return null;
	}

	@Override
	public String getText(Object element) {
		if(element instanceof JSLibVersion) {
			JSLibVersion v = (JSLibVersion)element;
			return v.getFullName();
		} if(element != null) {
			return element.toString();
		}
		return null;
	}
	
}

abstract class TitledDialog extends Dialog implements PropertyChangeListener {
	String title = "";

	public TitledDialog() {
		super(Display.getDefault().getActiveShell());
		setShellStyle(getShellStyle() | SWT.RESIZE);
	}
	
	public Control createContents(Composite parent) {
		Control result = super.createContents(parent);
		validate();
		return result;
	}

	protected void configureShell(final Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(title);
		newShell.addShellListener(new ShellAdapter() {
			public void shellActivated(ShellEvent e) {
				Rectangle r = newShell.getBounds();
				newShell.setBounds(r.x - (500 - r.width) / 2, r.y, 500, r.height);
			}
		});
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		validate();
	}

	protected void validate() {
		
	}
}

class AbstractJSLibDialog extends TitledDialog {
	JSLibVersion current = null;
	IFieldEditor nameEditor;
	IFieldEditor versionEditor;

	public AbstractJSLibDialog() {
	}
	
	public String getName() {
		return nameEditor.getValueAsString();
	}

	public String getVersion() {
		return versionEditor.getValueAsString();
	}

	protected Control createDialogArea(Composite parent) {
		Composite dialogArea = (Composite) super.createDialogArea(parent);

		Composite composite = new Composite(dialogArea, SWT.NONE);
		GridLayout layout = new GridLayout(3, false);		
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		applyDialogFont(composite);
		
		nameEditor = createNameEditor();
		nameEditor.doFillIntoGrid(composite);
		JSCSSPreferencePage.expandCombo(nameEditor);
		nameEditor.addPropertyChangeListener(this);

		versionEditor = createVersionEditor();
		versionEditor.doFillIntoGrid(composite);
		versionEditor.addPropertyChangeListener(this);

		return dialogArea;
	}

	IFieldEditor createNameEditor() {
		List<String> availableNames = getAvailableNames();
		return SwtFieldEditorFactory.INSTANCE.createComboEditor("name", WizardMessages.nameLabel, availableNames, 
				current == null ? "" : current.getLib().getName(), true, "Type a new name or select any existing one");
	}

	IFieldEditor createVersionEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor("version", WizardMessages.jsLibVersionLabel, 
				current == null ? "" : current.getVersion());
	}

	List<String> getAvailableNames() {
		List<String> result = new ArrayList<String>();
		for (JSLib lib: JSLibFactory.getInstance().getWorkingCopy().getLibs()) {
			result.add(lib.getName());
		}		
		return result;
	}

	protected void validate() {
		String v = getVersion();
		String n = getName();
		boolean enabled = v.length() > 0 && n.length() > 0;
		if(enabled) {
			JSLibModel model = JSLibFactory.getInstance().getWorkingCopy();
			if(model.getLib(n) != null && model.getLib(n).getVersion(v) != null) {
				enabled = false;
			}
		}
		getButton(OK).setEnabled(enabled);
	}

}

class NewJSLibDialog extends AbstractJSLibDialog {

	public NewJSLibDialog() {
		title = WizardMessages.newJSCSSLibTitle;
	}

}

class EditJSLibDialog extends AbstractJSLibDialog {

	public EditJSLibDialog(JSLibVersion v) {
		title = WizardMessages.editJSCSSLibTitle;
		current = v;
	}

}

class AbstractFileRefDialog extends TitledDialog {
	JSLibVersion parentVersion;
	URLWrapper current = null;
	IFieldEditor url;

	public AbstractFileRefDialog(JSLibVersion parentVersion) {
		this.parentVersion = parentVersion;
	}

	public String getURL() {
		return url.getValueAsString();
	}

	protected Control createDialogArea(Composite parent) {
		Composite dialogArea = (Composite) super.createDialogArea(parent);

		Composite composite = new Composite(dialogArea, SWT.NONE);
		GridLayout layout = new GridLayout(3, false);		
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		applyDialogFont(composite);

		String label = NLS.bind(WizardMessages.jsFileReferenceLabel, parentVersion.getFullName());
		url = SwtFieldEditorFactory.INSTANCE.createTextEditor("url", label, 
				current == null ? "" : current.url);
		url.doFillIntoGrid(composite);
		url.addPropertyChangeListener(this);
		
		Object[] cs = url.getEditorControls();
		
		for (int i = 0; i < cs.length; i++) {
			Control c = (Control)cs[i];
			GridData d = new GridData(GridData.FILL_HORIZONTAL);
			d.horizontalSpan = 3;
			c.setLayoutData(d);
		}

		return dialogArea;
	}

	protected void validate() {
		String u = getURL();
		boolean enabled = u.length() > 0;
		if(enabled) {
			if(parentVersion.getURLs().contains(u)) {
				enabled = false;
			}
		}
		getButton(OK).setEnabled(enabled);
	}
}

class AddFileRefDialog extends AbstractFileRefDialog {

	public AddFileRefDialog(JSLibVersion parentVersion) {
		super(parentVersion);
		title = WizardMessages.addJSCSSReferenceTitle;
	}
}

class EditFileRefDialog extends AbstractFileRefDialog {

	public EditFileRefDialog(JSLibVersion parentVersion, URLWrapper current) {
		super(parentVersion);
		this.current = current;
		title = WizardMessages.editJSCSSReferenceTitle;
	}
}

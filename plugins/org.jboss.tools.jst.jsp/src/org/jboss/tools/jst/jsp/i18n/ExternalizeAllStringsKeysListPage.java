/*******************************************************************************
 * Copyright (c) 2007-2011 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.jsp.i18n;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.wst.validation.ValidationFramework;
import org.jboss.tools.common.model.ui.ModelUIImages;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.messages.JstUIMessages;
import org.jboss.tools.jst.jsp.util.Constants;

public class ExternalizeAllStringsKeysListPage extends WizardPage {

	private static final String KEY_PROPERTY = "key"; //$NON-NLS-1$
	private static final String VALUE_PROPERTY = "value"; //$NON-NLS-1$
	Color red; 
	Color white;
	private List<KeyValueElement> initKeys;
	private IDocument doc;
	Properties properties;
	TableViewer tv;

	protected ExternalizeAllStringsKeysListPage(String pageName) {
		super(pageName,
				JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_TITLE, 
				ModelUIImages.getImageDescriptor(ModelUIImages.WIZARD_DEFAULT));
		setDescription(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_DESCRIPTION);
		setPageComplete(false);
	}

	@Override
	public void createControl(Composite parent) {
		red = parent.getDisplay().getSystemColor(SWT.COLOR_RED); 
		white = parent.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND);
		initKeys = getAllNonExternalizedStrings();
		Collections.sort(initKeys, Collections.reverseOrder());
		tv = createTable(parent);
		highlightAllDuplicateKeys(tv);
		setControl(tv.getTable());
	}

	private List<KeyValueElement> getAllNonExternalizedStrings() {
		List<KeyValueElement> keys = new ArrayList<KeyValueElement>(); 
		if (getWizard() instanceof ExternalizeAllStringsWizard) {
			ExternalizeAllStringsWizard wiz = (ExternalizeAllStringsWizard) getWizard();
			IEditorInput in = wiz.getEditor().getEditorInput();
			if (in instanceof IFileEditorInput) {
				IFileEditorInput fin = (IFileEditorInput) in;
				IFile file = fin.getFile();
				try {
					ValidationFramework.getDefault().validate(file, new NullProgressMonitor());
					IMarker[] markers = fin.getFile().findMarkers(
							JspEditorPlugin.I18N_VALIDATION_PROBLEM_ID, 
							true, IResource.DEPTH_ZERO);
					doc = wiz.getEditor().getDocumentProvider().getDocument(in);
					/*
					 * Iterate over all the markers
					 */
					String text = null;
					for (IMarker m : markers) {
						Integer offset = (Integer) m.getAttribute("PROBLEM_OFFSET"); //$NON-NLS-1$
						Integer length = (Integer) m.getAttribute("PROBLEM_LENGHT"); //$NON-NLS-1$
						try {
							text = doc.get(offset.intValue(), length.intValue());
							keys.add(new KeyValueElement(
									ExternalizeStringsUtils.generatePropertyKey(text), 
									text, offset.intValue(), length.intValue(), false));
						} catch (BadLocationException e) {
							e.printStackTrace();
						}
					}
				} catch (CoreException e) {
					JspEditorPlugin.getDefault().logError(e);
				}
			}
		}
		markAllDuplicatedKeys(keys);
		return keys;
	}

	private void markAllDuplicatedKeys(List<KeyValueElement> keys) {
		/*
		 * Check for duplicated keys and mark corresponding elements
		 */
			if (properties != null) {
				for (KeyValueElement k : keys) {
					if(isDuplicatedBundleKey(k.key)) {
						k.duplicated = true;
					}
				}
			}
	}
	
	private void markAllDuplicatedKeys(TableViewer tv) {
		for (int i = 0; i < tv.getTable().getItemCount(); i++) {
			KeyValueElement k = (KeyValueElement) tv.getElementAt(i);
			if(isDuplicatedBundleKey(k.key)) {
				k.duplicated = true;
			}
		}
	}
	
	private boolean isDuplicatedBundleKey(String key) {
		boolean duplicated = false;
		if (properties != null) {
			for (Iterator it = properties.keySet().iterator(); it.hasNext();) {
				String original = (String) it.next();
				if (original.equalsIgnoreCase(key)) {
					duplicated = true;
					break;
				}
			}
		}
		return duplicated;
	}
	
	private boolean isDuplicatedStringKey(KeyValueElement element, String newKey) {
		boolean duplicated = false;
		for (KeyValueElement k : initKeys) {
			if (!k.equals(element) && k.key.equalsIgnoreCase(newKey)) {
				duplicated = true;
			}
		}
		return duplicated;
	}
	
	private void highlightAllDuplicateKeys(TableViewer tv) {
		for (int i = 0; i < tv.getTable().getItemCount(); i++) {
			highlightDuplicateKey(tv.getTable().getItem(i),
					((KeyValueElement) tv.getElementAt(i)).duplicated);
		}
	}
	
	private void highlightDuplicateKey(TableItem ti, boolean duplicated) {
		if (duplicated) {
			ti.setBackground(0, red);
		} else {
			ti.setBackground(0, white);
		}
	}
	
	private TableViewer createTable(Composite parent) {
		Table table = new Table(parent, SWT.FULL_SELECTION | SWT.BORDER);
		TableViewer tv = layoutTableInViewer(table);
		attachContentProvider(tv);
		attachLabelProvider(tv);
		attachCellEditors(tv, table);
		addInitialKeysValues(tv);
		return tv;
	}

	private TableViewer layoutTableInViewer(Table table) {
		TableViewer tableViewer = new TableViewer(table);

		TableLayout layout = new TableLayout();
		layout.addColumnData(new ColumnWeightData(200, 200, true));
		layout.addColumnData(new ColumnWeightData(200, 200, true));
		table.setLayout(layout);

		TableColumn nameColumn = new TableColumn(table, SWT.LEFT);
		nameColumn.setText(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_PROPERTY_NAME);
		TableColumn valColumn = new TableColumn(table, SWT.LEFT);
		valColumn.setText(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_PROPERTY_VALUE);

		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		return tableViewer;
	}

	private void attachContentProvider(TableViewer tv) {
		tv.setContentProvider(new IStructuredContentProvider() {
			public Object[] getElements(Object inputElement) {
				return (Object[]) inputElement;
			}
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
			}
			public void dispose() {}
		});
	}

	private void attachLabelProvider(TableViewer tv) {
		tv.setLabelProvider(new ITableLabelProvider() {
			public Image getColumnImage(Object element, int columnIndex) {
				return null;
			}
			public String getColumnText(Object element, int columnIndex) {
				switch (columnIndex) {
				case 0:
					return ((KeyValueElement) element).key;
				case 1:
					return ((KeyValueElement) element).value;
				default:
					return "Invalid column: " + columnIndex; //$NON-NLS-1$
				}
			}
			public void addListener(ILabelProviderListener listener) { }
			public void dispose() { }
			public boolean isLabelProperty(Object element, String property) {
				return false;
			}
			public void removeListener(ILabelProviderListener lpl) { }
		});
	}

	private void attachCellEditors(final TableViewer viewer, Composite parent) {
		viewer.setCellModifier(new ICellModifier() {
			public boolean canModify(Object element, String property) {
				return true;
			}
			public Object getValue(Object element, String property) {
				if (KEY_PROPERTY.equals(property)) {
					return ((KeyValueElement) element).key;
				} else {
					return ((KeyValueElement) element).value;
				}
			}
			public void modify(Object element, String property, Object value) {
				TableItem tableItem = (TableItem) element;
				KeyValueElement kve = (KeyValueElement) tableItem.getData();
				if (KEY_PROPERTY.equals(property)) {
					kve.key = value.toString();
				} else {
					kve.value = value.toString();
				}
				viewer.refresh(kve);
			}
		});
		TextCellEditor keyEditor = new TextCellEditor(parent); 
		/*
		 * TODO: cell validator should be added after bug
		 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=130854
		 * is fixed.
		 */
//		keyEditor.setValidator(new ICellEditorValidator() {
//			@Override
//			public String isValid(Object value) {
//				return null;
//			}
//		});
		((Text)keyEditor.getControl()).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				Text t = (Text) e.getSource();
				Table table = (Table) t.getParent();
				int ind = table.getSelectionIndex();
				KeyValueElement element = (KeyValueElement) viewer.getElementAt(ind);
				TableItem ti = table.getItem(ind);
				String key = t.getText();
				/*
				 * Check duplicate key, set bkg color to red
				 * Setting new key value is called after this modyfy listener,
				 * thus pass new key value manually.
				 */
				if (isDuplicatedBundleKey(key) || isDuplicatedStringKey(element, key)) {
					element.duplicated = true;
					highlightDuplicateKey(ti, true);
					t.setBackground(red);
				} else {
					element.duplicated = false;
					highlightDuplicateKey(ti, false);
					t.setBackground(white);
				}
				updateStatus();
			}
		});
		((Text)keyEditor.getControl()).addVerifyListener(new VerifyListener() {
			  public void verifyText(VerifyEvent e) {
					for (int i = 0; i < ExternalizeStringsUtils.REPLACED_CHARACTERS.length; i++) {
						/*
						 * Entering of the forbidden characters will be prevented.
						 */
						if (e.character == ExternalizeStringsUtils.REPLACED_CHARACTERS[i]) {
							e.doit = false;
							break;
						}
					}
			    }
			});
		viewer.setCellEditors(new CellEditor[] { 
				keyEditor, null });
		viewer.setColumnProperties(new String[] { 
				KEY_PROPERTY, VALUE_PROPERTY });
	}

	private void addInitialKeysValues(TableViewer viewer) {
		for (KeyValueElement k : initKeys) {
			viewer.add(k);
		}
	}

	/**
	 * Gets <code>key=value</code> pair
	 * 
	 * @return a pair <code>key=value</code>
	 */
	public String getKeyValuePairsList() {
		StringBuffer sb = new StringBuffer();
		for (KeyValueElement k : initKeys) {
			/*
			 * Element with duplicated key will be excluded.
			 */
			if (!k.duplicated) {
				sb.append(k.key + Constants.EQUAL + k.value);
				sb.append('\n');
			}
		}
		return sb.toString();
	}
	
	/**
	 * Replaces the text in the current file
	 */
	public void replaceAllStrings(String bundlePrefix) {
		if ((doc == null) && (getWizard() instanceof ExternalizeAllStringsWizard)){
			doc = ((ExternalizeAllStringsWizard) getWizard()).getDocument();
		}
		if (doc != null) {
			/*
			 * Go from the end to the top of the file.
			 */
			Collections.sort(initKeys);
			for (int i = initKeys.size()-1; i >= 0; i--) {
				KeyValueElement k = initKeys.get(i);
				try {
					/*
					 * Element with duplicated key won't be replaced
					 */
					if (!k.duplicated) {
						doc.replace(k.offset, k.length, "#{" +bundlePrefix + Constants.DOT + k.key + "}"); //$NON-NLS-1$ //$NON-NLS-2$
					}
				} catch (BadLocationException e) {
					JspEditorPlugin.getPluginLog().logError(e);
				}
			}
		}
	}
	
	public void updateTable(Properties p) {
		this.properties = p;
		boolean anyDuplicated = false;
		for (int i = 0; i < tv.getTable().getItemCount(); i++) {
			KeyValueElement k = (KeyValueElement) tv.getElementAt(i);
			TableItem ti = tv.getTable().getItem(i);
			if(isDuplicatedBundleKey(k.key) || isDuplicatedStringKey(k, k.key)) {
				k.duplicated = true;
				ti.setBackground(0, red);
				anyDuplicated = true;
			} else {
				k.duplicated = false;
				ti.setBackground(0, white);
			}
		}
		setDuplicateWarning(anyDuplicated);
	}
	
	private void setDuplicateWarning(boolean anyDuplicated) {
		if (anyDuplicated) {
			this.setMessage("Duplicated entries won't be externalized",
					IMessageProvider.WARNING);
			this.setErrorMessage(null);
		} else {
			this.setMessage(null);
			this.setErrorMessage(null);
		}
	}
	
	private void updateStatus() {
		boolean anyDuplicated = false;
		for (KeyValueElement k : initKeys) {
			if (k.duplicated) {
				anyDuplicated = true;
				break;
			}
		}
		setDuplicateWarning(anyDuplicated);
	}
	
	@Override
	public boolean isPageComplete() {
		/*
		 * User always can complete the page
		 *  Duplicate entries simply will be ignored.
		 */
		return true;
	}
	
	class KeyValueElement implements Comparable<KeyValueElement> {
		public String key;
		public String value;
		public int offset;
		public int length;
		public boolean duplicated;
		public KeyValueElement(String k, String v, int o, int l, boolean d) {
			key = k;
			value = v;
			offset = o;
			length = l;
			duplicated = d;
		}
		@Override
		public int compareTo(KeyValueElement aThat) {
			final int BEFORE = -1;
		    final int EQUAL = 0;
		    final int AFTER = 1;
		    if (this == aThat) return EQUAL;
		    if (this.offset < aThat.offset) return BEFORE;
		    if (this.offset > aThat.offset) return AFTER;
		    assert this.equals(aThat) : "compareTo inconsistent with equals."; //$NON-NLS-1$
			return EQUAL;
		}
		@Override
		public int hashCode() {
			int result = 47 + super.hashCode();
			if (key != null) {
				result+= key.hashCode();
			}
			result = result*7;
			if (value != null) {
				result+= value.hashCode();
			}
			result = result*7;
			result+= offset;
			result = result*7;
			result+= length;
		    return result;
		}
		
		@Override
		public boolean equals(Object aThat) {
			if (this == aThat) return true;
			if (!(aThat instanceof KeyValueElement)) return false;
			KeyValueElement that = (KeyValueElement)aThat;
			return ((this.key == that.key) &&
					(this.value == that.value) &&
					(this.offset == that.offset) &&
					(this.length == that.length));
		}
		
		@Override
		public String toString() {
			return this.getClass() + "@" + this.hashCode()  //$NON-NLS-1$
			+ " (key=" + this.key + ", value=" + this.value //$NON-NLS-1$ //$NON-NLS-2$
			+ ", offset=" + this.offset + ", length=" + this.length //$NON-NLS-1$ //$NON-NLS-2$
			+ ", duplicated=" + this.duplicated + ")"; //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
}

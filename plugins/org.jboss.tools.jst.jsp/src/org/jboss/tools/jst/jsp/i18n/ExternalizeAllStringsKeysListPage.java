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
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
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
	private List<KeyValueElement> keys = new ArrayList<KeyValueElement>();
	private IDocument doc = null;

	protected ExternalizeAllStringsKeysListPage(String pageName) {
		super(pageName,
				JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_TITLE, 
				ModelUIImages.getImageDescriptor(ModelUIImages.WIZARD_DEFAULT));
		setDescription(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_DESCRIPTION);
		setPageComplete(false);
	}

	@Override
	public void createControl(Composite parent) {
		keys = getAllStrings();
		Table table = createTable(parent);
		setControl(table);
	}

	private List<KeyValueElement> getAllStrings() {
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
									text, offset.intValue(), length.intValue()));
						} catch (BadLocationException e) {
							e.printStackTrace();
						}
					}
				} catch (CoreException e) {
					JspEditorPlugin.getDefault().logError(e);
				}
			}
		}
		return keys;
	}

	private Table createTable(Composite parent) {
		Table table = new Table(parent, SWT.FULL_SELECTION | SWT.BORDER);
		TableViewer tv = layoutTableInViewer(table);
		attachContentProvider(tv);
		attachLabelProvider(tv);
		attachCellEditors(tv, table);
		addProperties(tv);
		return table;
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
		viewer.setCellEditors(new CellEditor[] { 
				new TextCellEditor(parent), null });
		viewer.setColumnProperties(new String[] { 
				KEY_PROPERTY, VALUE_PROPERTY });
	}

	private void addProperties(TableViewer viewer) {
		for (KeyValueElement k : keys) {
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
		for (KeyValueElement k : keys) {
			sb.append(k.key + Constants.EQUAL + k.value);
			sb.append('\n');
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
			Collections.sort(keys);
			for (int i = keys.size()-1; i >= 0; i--) {
				KeyValueElement k = keys.get(i);
				try {
					doc.replace(k.offset, k.length, "#{" +bundlePrefix + Constants.DOT + k.key + "}"); //$NON-NLS-1$ //$NON-NLS-2$
				} catch (BadLocationException e) {
					JspEditorPlugin.getPluginLog().logError(e);
				}
			}
		}
	}
	
	@Override
	public boolean isPageComplete() {
		return true;
	}
	
	class KeyValueElement implements Comparable<KeyValueElement> {
		public String key;
		public String value;
		public int offset;
		public int length;
		public KeyValueElement(String k, String v, int o, int l) {
			key = k;
			value = v;
			offset = o;
			length = l;
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
			int result = 47;
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
			+ " (key=" + this.key + ", value=" + this.value  //$NON-NLS-1$ //$NON-NLS-2$
			+ ", offset=" + this.offset + ", length=" + this.length + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	}
}

/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.wizards.process;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Properties;
import org.jboss.tools.common.model.ui.attribute.IListContentProvider;
import org.jboss.tools.common.model.ui.wizards.query.AbstractQueryWizardView;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import org.jboss.tools.common.meta.key.WizardKeys;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.web.ui.WebUiPlugin;

public abstract class SelectWebProcessItemWizardView extends AbstractQueryWizardView {
	private Properties p; 
	private XModelObject process;
	private XModelObject[] items = null;
	private XModelObject selected = null;
	protected TableViewer tableViewer;
	protected Text filter;
	protected TContentProvider provider = new TContentProvider();

	public void setObject(Object data) {
		super.setObject(data);
		p = (Properties)data;
		process = (XModelObject)p.get("object"); //$NON-NLS-1$
		items = (XModelObject[])p.get("items"); //$NON-NLS-1$
		sortItems();
		this.setMessage(WizardKeys.getString("SelectItemWizardView.Message")); //$NON-NLS-1$
//		this.setTitle(WizardKeys.getString("SelectItemWizardView.Title"));
//		this.setWindowTitle(WizardKeys.getString("SelectItemWizardView.WindowTitle"));
	}
	
	private void sortItems() {
		if(items == null) return;
		Comparator c = new Comparator() {
			public int compare(Object o1, Object o2) {
				XModelObject oo1 = (XModelObject)o1;
				XModelObject oo2 = (XModelObject)o2;
				String key1 = "" + getKey(oo1); //$NON-NLS-1$
				String key2 = "" + getKey(oo2); //$NON-NLS-1$
				return key1.compareTo(key2);
			}
			
		};
		Arrays.sort(items, c);
	}

	public Control createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 10;
		layout.verticalSpacing = 10;
		layout.marginHeight = 10;
		layout.marginWidth = 10;
		composite.setLayout(layout);
		GridData gd = new GridData(GridData.FILL_BOTH);
		composite.setLayoutData(gd);
		
		
		//Composite c = new Composite(composite, SWT.NONE);
		//c.setLayout(new GridLayout());
		Control c1 = createTableViewer(composite);
		if(c1 != null) {
			c1.setLayoutData(new GridData(GridData.FILL_BOTH));
		}
		Control c2 = createText(composite);
		c2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		getCommandBar().setCommands(new String[]{AbstractQueryWizardView.OK,AbstractQueryWizardView.CANCEL});
		getCommandBar().setEnabled(AbstractQueryWizardView.OK,false);		
		return composite;
	}
	
	protected Control createTableViewer(Composite parent) {
		if(items == null) {
			items = process.getChildren(getItemEntity());
			sortItems();
		}
		provider.setObjects(items);
		provider.setPattern(""); //$NON-NLS-1$
		tableViewer = new TableViewer(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		tableViewer.setColumnProperties(new String[]{"name"}); //$NON-NLS-1$
		tableViewer.setContentProvider(provider);
		tableViewer.setLabelProvider(provider);		
		tableViewer.setInput(this);
		tableViewer.getTable().addMouseListener(new Ms());
			
		return tableViewer.getTable();		
	}
	
	protected Control createText(Composite parent) {
		Composite c = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.horizontalSpacing = 10;
		layout.verticalSpacing = 0;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		c.setLayout(layout);
		
		Label l = new Label(c, SWT.NONE);
		l.setText("Filter: ");
		filter = new Text(c, SWT.BORDER);
		filter.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		filter.addModifyListener(new MA());
		//makeSelection();
		filter.forceFocus();
		return c;
	}
	
	public void stopEditing() {
		readSelection();
	}
	
	public void action(String command) {
		stopEditing();
		if(OK.equalsIgnoreCase(command)) {
			p.put("selected", selected); //$NON-NLS-1$
		}
		super.action(command);			
	}
	
	protected abstract String getItemEntity();
	protected abstract String getKey(XModelObject o);
	
	private void readSelection() {
		int i = tableViewer.getTable().getSelectionIndex();
		selected = (i < 0 || provider == null) ? null : (XModelObject)provider.items.get(i);
	}
	
	private void makeSelection() {
		if(provider == null || provider.items == null) return;			
		if(selected == null || !provider.items.contains(selected)) {
			selected = null;
			if(provider.items.size() > 0) selected = (XModelObject)provider.items.get(0);
		}
		if(selected != null && tableViewer != null && tableViewer.getControl() != null && !tableViewer.getControl().isDisposed()) {
			tableViewer.setSelection(new StructuredSelection(selected));
		}
	}
	
	class MA implements ModifyListener {
		public void modifyText(ModifyEvent e) {
			provider.setPattern(filter.getText());
			tableViewer.refresh();
			readSelection();
			getCommandBar().setEnabled(AbstractQueryWizardView.OK, selected!=null);
		}		
	}
	
	class Ms extends MouseAdapter {
		public void mouseDoubleClick(MouseEvent e) {
			action(OK);
		}
		
        public void mouseDown(MouseEvent e) {
            readSelection();
            getCommandBar().setEnabled(AbstractQueryWizardView.OK, selected!=null);
        }
	}

	class TContentProvider implements IListContentProvider, ITableLabelProvider {
		XModelObject[] l = new XModelObject[0];
		ArrayList<XModelObject> items = new ArrayList<XModelObject>();
		
		public void setObjects(XModelObject[] l) {
			this.l = l;
		}
		
		public void setPattern(String s) {
			s = s == null ? "" : s.toLowerCase(); //$NON-NLS-1$
			items.clear();
			for (int i = 0; i < l.length; i++) {
				String key = getKey(l[i]);
				if(key == null) {
					continue;
				}
				if(s.length() == 0 || key.toLowerCase().indexOf(s) >= 0) items.add(l[i]);
			}
			makeSelection();
		}
		
		public Object[] getElements(Object inputElement) {
			return items.toArray(new Object[0]);
		}

		public void dispose() {}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			try { 
				viewer.refresh(); 
			} catch (Exception e) {
				WebUiPlugin.getPluginLog().logError(e);
			}
		}

		public Image getColumnImage(Object element, int columnIndex) {
			if(!(element instanceof XModelObject)) return null;
			return EclipseResourceUtil.getImage((XModelObject)element);
		}

		public String getColumnText(Object element, int columnIndex) {
			if(!(element instanceof XModelObject)) return ""; //$NON-NLS-1$
			return (columnIndex == 0) ? getKey((XModelObject)element) : "-"; //$NON-NLS-1$
		}

		public void addListener(ILabelProviderListener listener) {			
		}

		public boolean isLabelProperty(Object element, String property) {
			return ("name".equals(property)); //$NON-NLS-1$
		}

		public void removeListener(ILabelProviderListener listener) {
		}
		
	}

}

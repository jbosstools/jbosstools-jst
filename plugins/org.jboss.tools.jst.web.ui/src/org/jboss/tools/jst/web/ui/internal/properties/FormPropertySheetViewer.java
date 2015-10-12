/******************************************************************************* 
 * Copyright (c) 2013-2015 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.internal.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.jboss.tools.jst.web.ui.internal.editor.outline.IFormCategoryDescriptor;
import org.jboss.tools.jst.web.ui.internal.editor.outline.IFormPropertySource;
import org.jboss.tools.jst.web.ui.WebUiPlugin;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class FormPropertySheetViewer extends Viewer implements SelectionListener {
	public static final String ALL_CATEGORY = "All";
	IPropertySheetModel model;

	private Object[] input;

	protected TabFolder tab = null;
	protected Composite control = null;

	EditedDescriptor editedDescriptor = new EditedDescriptor(this);

	IPropertySetViewer selectedViewer;
	Map<String, Class<? extends IPropertySetViewer>> setViewerClassesByCategory = new HashMap<String, Class<? extends IPropertySetViewer>>();
	Map<String, IPropertySetViewer> setViewersByCategory = new HashMap<String, IPropertySetViewer>();

	public FormPropertySheetViewer(Composite parent) {
		control = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		control.setLayout(layout);
		
		tab = new TabFolder(control, SWT.NULL);
		GridData d = new GridData(GridData.FILL_BOTH);
		tab.setLayoutData(d);
		tab.addSelectionListener(this);

	}

	public IPropertySheetModel getModel() {
		return model;
	}

	public void setModel(IPropertySheetModel model) {
		if(this.model != null) {
			//remove model listener
		}
		this.model = model;

		//set model to components
		
		//add model listener
		
		setInput(input);
	}

	@Override
	public Control getControl() {
		return control;
	}

	@Override
	public Object getInput() {
		return input;
	}

	@Override
	public ISelection getSelection() {
		IPropertyDescriptor d = editedDescriptor.getPropertyDescriptor();
		return d == null ? StructuredSelection.EMPTY : new StructuredSelection(d);
	}

	public IEditedDescriptor getEditedDecriptor() {
		return editedDescriptor;
	}

	@Override
	public void refresh() {
		List<IPropertyDescriptor> descriptors = model.getPropertyDescriptors();

		List<String> categories = getCategories(descriptors);
		String defaultCategory = ALL_CATEGORY;
		boolean changed = false;
		Iterator<String> it = setViewersByCategory.keySet().iterator();
		while(it.hasNext()) {
			String c = it.next();
			if(!categories.contains(c)){
				IPropertySetViewer v = setViewersByCategory.get(c);
				v.dispose();
				it.remove();
				changed = true;
			}
		}
		for (String c: categories) {
			if(!setViewersByCategory.containsKey(c)) {
				changed = true;
			}
		}
		if(changed) {
			for (IPropertySetViewer v: setViewersByCategory.values()) {
				v.dispose();
			}
			setViewersByCategory.clear();
		}
		for (String c: categories) {
			if(!setViewersByCategory.containsKey(c)) {
				changed = true;
				IPropertySetViewer v = createSetViewer(c);
				v.setModel(model);
				v.setEditedDescriptorService(editedDescriptor);
				setViewersByCategory.put(c, v);
				Composite all = v.createControl(tab);
				all.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			} else if(selectedViewer != null && selectedViewer == setViewersByCategory.get(c)) {
				defaultCategory = c;
			}
		}

		if(changed) {
			TabItem selection = null;
			TabItem[] is = tab.getItems();
			for (int i = is.length - 1; i >= 0; i--) {
				is[i].dispose();
			}
			for (String c: categories) {
				IPropertySetViewer v = setViewersByCategory.get(c);
				TabItem b1 = new TabItem(tab, SWT.NULL);
				b1.setData(c);
				b1.setText(v.getCategoryDisplayName());
				b1.setControl(v.getControl());
				if(defaultCategory.equals(c)) {
					selection = b1;
				}
				tab.setSelection(b1);
			}
			if(selection != null) {
				tab.setSelection(selection);
			}
		}
		for (String c: categories) {
			IPropertySetViewer v = setViewersByCategory.get(c);
			v.refresh(descriptors);
		}
		fireSelectedDescriptorChanged();
	}

	/**
	 * Create custom viewer objects here.
	 * 
	 * @param category
	 * @return
	 */
	protected IPropertySetViewer createSetViewer(String category) {
		IPropertySetViewer result = null;
		if(category != null && setViewerClassesByCategory.containsKey(category)) {
			try {
				result = (IPropertySetViewer)setViewerClassesByCategory.get(category).newInstance();
			} catch (InstantiationException e) {
				WebUiPlugin.getDefault().logError(e);
			} catch (IllegalAccessException e) {
				WebUiPlugin.getDefault().logError(e);
			}
			if(result == null) {
				result = new DefaultPropertySetViewer();
				setViewerClassesByCategory.put(category, DefaultPropertySetViewer.class);
			}
		} else {
			result = new DefaultPropertySetViewer();
		}
		result.setCategory(category);
		return result;
	}

	private List<String> getCategories(List<IPropertyDescriptor> descriptors) {
		List<String> result = new ArrayList<String>();
		IPropertySource s = model.getPropertySource();
		if(s instanceof IFormPropertySource) {
			IFormCategoryDescriptor[] ds = ((IFormPropertySource)s).getCategoryDescriptors();
			for (int i = 0; i < ds.length; i++) {
				if(!setViewerClassesByCategory.containsKey(ds[i])) {
					setViewerClassesByCategory.put(ds[i].getName(), ds[i].getUIClass());
				}
				result.add(ds[i].getName());
			}
		} else {
			TreeSet<String> categories = new TreeSet<String>();
			for (IPropertyDescriptor d: descriptors) {
				String c = d.getCategory();
				if(c != null) categories.add(c);
			}
			categories.remove(ALL_CATEGORY);
			result.addAll(categories);
		}
		result.add(ALL_CATEGORY);
		return result;
	}

	@Override
	public void setInput(Object input) {
        // need to save any changed value when user clicks elsewhere
		if(selectedViewer != null) {
			selectedViewer.applyEditorValue();
	        // deactivate our cell editor
	       deactivateCellEditor();
		}
       
       {
    	   Object[] newInput = (Object[]) input;
    	   if(this.input == null || newInput == null 
    		|| this.input.length != newInput.length
    		|| newInput.length == 0 || this.input[0] != newInput[0]) {
    		   editedDescriptor.setDescriptor(null);
    	   }
       }
        
        this.input = (Object[]) input;
        if (input == null) {
			this.input = new Object[0];
		}

        model.setInput(this.input);

		IPropertyDescriptor selected = editedDescriptor.getPropertyDescriptor();
		if(selected != null) {
			IPropertyDescriptor d = model.getDescriptor(selected.getId().toString());
			if(d != selected) {
				editedDescriptor.setDescriptor(d);
			}
		}

		refresh();
//        updateStatusLine(null);
	}

	void deactivateCellEditor() {
		if(selectedViewer != null) {
			selectedViewer.stopEditing();
		}
	}

	@Override
	public void setSelection(ISelection selection, boolean reveal) {
	}

	public void setFocus() {
		if(tab != null) {
			tab.setFocus();
		}
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		if(selectedViewer != null) {
			selectedViewer.applyEditorValue();
			editedDescriptor.setDescriptor(null);
			deactivateCellEditor();
			selectedViewer = null;
		}
		int i = tab.getSelectionIndex();
		if(i >= 0) {
			TabItem item = tab.getItem(i);
			if(item != null && item.getData() != null) {
				String category = item.getData().toString();
				selectedViewer = setViewersByCategory.get(category);
				if(selectedViewer instanceof AbstractPropertySetViewer) {
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							if(selectedViewer instanceof AbstractPropertySetViewer) {
								((AbstractPropertySetViewer)selectedViewer).updateUI();
							}
						}
					});
				}
			}
		}		
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
	}

	public void fireSelectedDescriptorChanged() {
		fireSelectionChanged(new SelectionChangedEvent(this, getSelection()));
	}

	/**
	 * Access for test.
	 * @return
	 */
	public List<String> getCategories() {
		List<String> result = new ArrayList<String>();
		if(tab != null && !tab.isDisposed()) {
			for (TabItem i: tab.getItems()) {
				result.add(i.getData().toString());
			}
		}
		return result;
	}

	/**
	 * Access for test.
	 * @return
	 */
	public IPropertySetViewer getViewer(String category) {
		return setViewersByCategory.get(category);
	}
}
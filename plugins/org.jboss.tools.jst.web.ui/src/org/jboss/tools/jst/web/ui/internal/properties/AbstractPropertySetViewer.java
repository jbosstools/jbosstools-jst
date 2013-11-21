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
package org.jboss.tools.jst.web.ui.internal.properties;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public abstract class AbstractPropertySetViewer implements IPropertySetViewer {
	protected String category = null;
	protected IPropertySheetModel model;
	protected IEditedDescriptor editedDescriptor;

	protected IPropertySheetModelListener entryListener;

	public AbstractPropertySetViewer() {
		createEntryListener();
	}

	@Override
	public IPropertySheetModel getModel() {
		return model;
	}

	@Override
	public void setModel(IPropertySheetModel model) {
		if(this.model != null && entryListener != null) {
			this.model.removeListener(entryListener);
		}
		this.model = model;
		if(this.model != null) {
			this.model.addListener(entryListener);
		}
	}

	@Override
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * Default implementation. Subclasses may override.
	 */
	@Override
	public String getCategoryDisplayName() {
		return category;
	}

	@Override
	public void setEditedDescriptorService(IEditedDescriptor editedDescriptor) {
		this.editedDescriptor = editedDescriptor;
	}

	/**
	 * Checks if the property is set in the edited object or default value is assumed.
	 * 
	 * @param d
	 * @return
	 */
	protected boolean isPropertySet(IPropertyDescriptor d) {
		IPropertySource s = model.getPropertySource();
		return s != null && s.isPropertySet(d.getId());
	}

	protected void createEntryListener() {
	}

	@Override
	public void stopEditing() {		
	}

	@Override
	public void dispose() {
		model.removeListener(entryListener);
	}

	@Override
	public void applyEditorValue() {
	}
}
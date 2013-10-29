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
import org.eclipse.wst.sse.ui.views.properties.IPropertySourceExtension;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class EditedDescriptor implements IEditedDescriptor {
	IPropertyDescriptor descriptor;
	FormPropertySheetViewer viewer;

	public EditedDescriptor(FormPropertySheetViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public void setDescriptor(IPropertyDescriptor descriptor) {
		boolean changed = this.descriptor != descriptor;
		this.descriptor = descriptor;
		if(changed && viewer != null) {
			viewer.fireSelectedDescriptorChanged();
		}
	}

	@Override
	public IPropertyDescriptor getPropertyDescriptor() {
		return descriptor;
	}

	@Override
	public Object getValue() {
		if(descriptor != null) {
			return viewer.getModel().getPropertySource().getPropertyValue(descriptor.getId());
		} else {
			return null;
		}
	}

	@Override
	public void applyValue(Object newValue) {
		Object oldValue = getValue();
		boolean changed = (oldValue == null) ? newValue != null : !oldValue.equals(newValue);
		// Set the editor value
		if (changed) {
			viewer.getModel().valueChanged(descriptor, newValue);
			if(viewer != null) {
				viewer.fireSelectedDescriptorChanged();
			}
		}
	}

	@Override
	public void removeProperty() {
		if(descriptor == null) return;
		IPropertySource s = viewer.getModel().getPropertySource();
		if(s instanceof IPropertySourceExtension) {
			IPropertySourceExtension se = (IPropertySourceExtension)s;
			if(se.isPropertyRemovable(descriptor.getId())) {
				se.removeProperty(descriptor.getId());
				if(viewer != null) {
					viewer.fireSelectedDescriptorChanged();
				}
			}
		}
	}

}

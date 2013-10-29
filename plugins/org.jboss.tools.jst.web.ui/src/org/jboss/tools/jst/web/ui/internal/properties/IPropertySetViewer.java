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

import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public interface IPropertySetViewer {

	/**
	 * Hints that this viewer is engaged to display properties for the passed 
	 * category name. The viewer implementation may use the hint at its own 
	 * discretion.
	 * 
	 * @param category
	 */
	public void setCategory(String category);

	/**
	 * Returns a name to be displayed as tab name for the tab 
	 * displaying this viewer.
	 * @return
	 */
	public String getCategoryDisplayName();

	/**
	 * Creates root control for this viewer.
	 * This method is not expected to create all controls for the current 
	 * context. Actually, context may be nat accessible at this moment.
	 * Method refresh() will be later called to build the entire content.
	 *  
	 * @param parent
	 * @return Root control for this viewer
	 */
	public Composite createControl(Composite parent);

	/**
	 * @return Root control for this viewer
	 */
	public Control getControl();

	public IPropertySheetModel getModel();

	public void setModel(IPropertySheetModel model);

	/**
	 * Sets service IEditedDescriptor shared by contributing viewers.
	 * 
	 * @param editedDescriptor
	 */
	public void setEditedDescriptorService(IEditedDescriptor editedDescriptor);

	/**
	 * Refreshes form. May either update field values only, 
	 * or rebuild the entire form depending on the kind of changes 
	 * in the context.
	 * 
	 * @param descriptors Model property descriptors passed for convenience
	 */
	public void refresh(List<IPropertyDescriptor> descriptors);

	/**
	 * If this viewer has an active cell editor, its value must be applied.
	 */
	public void applyEditorValue();

	/**
	 * If this viewer has an active cell editor, it must be deactivated.
	 */
	public void stopEditing();

	/**
	 * Disposes all used resources.
	 */
	public void dispose();
}

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

import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public interface IPropertySheetModel {

	public void setWorkbenchPart(IWorkbenchPart part);

	public IWorkbenchPart getWorkbenchPart();

	public void setPropertySourceProvider(IPropertySourceProvider provider);

	/**
	 * Called by VisualPropertySheetViewer.setInput()
	 * Implementation should use current property source provider 
	 * to receive property descriptors associated with the selection.
	 * Next, VisualPropertySheetViewer will invoke refresh of the view.
	 * 
	 * @param objects
	 */
	public void setInput(Object[] objects);

	/**
	 * 
	 * @return currently selected object
	 */
	public Object getValue();

	/**
	 * 
	 * @return Property source associated with currently selected object
	 */
	public IPropertySource getPropertySource();

	/**
	 * 
	 * @return List of descriptors associated with currently selected object
	 */
	public List<IPropertyDescriptor> getPropertyDescriptors();

	public boolean hasDescriptor(String id);

	public IPropertyDescriptor getDescriptor(String id);

	/**
	 * Retrieves from current property source value for the passed descriptor.
	 * 
	 * @param descriptor
	 * @return
	 */
	public String getValueAsString(IPropertyDescriptor descriptor);

	/**
	 * Callback for IEditedDescriptor.
	 * 
	 * @param d
	 * @param newValue
	 */
	public void valueChanged(IPropertyDescriptor d, Object newValue);

	public void addListener(IPropertySheetModelListener listener);

	public void removeListener(IPropertySheetModelListener listener);
}

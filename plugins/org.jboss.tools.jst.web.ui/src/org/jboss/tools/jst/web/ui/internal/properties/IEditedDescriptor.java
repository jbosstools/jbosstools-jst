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

/**
 * Service object shared by participating property set viewers.
 * For viewers that provide editors only for a selected entry, 
 * this object stores the selected property description, 
 * and by accessing property source provides cell editor and 
 * commit of new value.
 * 
 * For form viewers that build all field editors on their own,
 * only commit of new value to property source is relevant
 * by invoking setDescriptor() and applyEditorValue().
 * 
 * @author Viacheslav Kabanovich
 *
 */
public interface IEditedDescriptor {

	public void setDescriptor(IPropertyDescriptor descriptor);

	public IPropertyDescriptor getPropertyDescriptor();

	public Object getValue();

	public void applyValue(Object newValue);

	public void removeProperty();

}

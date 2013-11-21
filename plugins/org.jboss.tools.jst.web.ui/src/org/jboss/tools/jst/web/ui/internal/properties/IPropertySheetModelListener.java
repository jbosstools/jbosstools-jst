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
 * 
 * @author Viacheslav Kabanovich
 *
 */
public interface IPropertySheetModelListener {

	public void descriptorsChanged();

	public void valueChanged(IPropertyDescriptor descriptor);

}

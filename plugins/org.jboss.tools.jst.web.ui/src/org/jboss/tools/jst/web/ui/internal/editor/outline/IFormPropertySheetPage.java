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
package org.jboss.tools.jst.web.ui.internal.editor.outline;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.wst.sse.ui.views.properties.PropertySheetConfiguration;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public interface IFormPropertySheetPage extends IPropertySheetPage, IAdaptable {

	public void setConfiguration(PropertySheetConfiguration configuration);

	public void refresh();

}

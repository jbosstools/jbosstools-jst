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
package org.jboss.tools.jst.web.ui.internal.properties.advanced;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.ui.internal.properties.FormPropertySheetViewer;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class DefaultAdvancedPropertySetViewer extends AbstractAdvancedPropertySetViewer {

	public DefaultAdvancedPropertySetViewer() {
	}

	@Override
	protected List<IPropertyDescriptor> getFilteredDescriptors(List<IPropertyDescriptor> descriptors) {
		if(category == null || category.equals(FormPropertySheetViewer.ALL_CATEGORY)) {
			return descriptors;
		}
		List<IPropertyDescriptor> result = new ArrayList<IPropertyDescriptor>();
		for (IPropertyDescriptor d: descriptors) {
			if(category.equals(d.getCategory())) {
				result.add(d);
			}
		}
		return result;
	}

	@Override
	protected IFieldEditor createEditor(IPropertyDescriptor d) {
		return createTextEditor(d);
	}

	@Override
	protected void layoutEditors(Composite fields, List<Entry> entries) {
		for (Entry e: entries) {
			layoutEditor(e, fields);
		}		
	}

}

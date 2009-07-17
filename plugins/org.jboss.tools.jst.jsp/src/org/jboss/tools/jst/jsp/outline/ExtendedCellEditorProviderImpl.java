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
package org.jboss.tools.jst.jsp.outline;

import java.util.Properties;
import org.jboss.tools.common.model.ui.objecteditor.AttributeWrapper;
import org.jboss.tools.common.model.ui.objecteditor.ExtendedCellEditorProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class ExtendedCellEditorProviderImpl implements ExtendedCellEditorProvider {

	public CellEditor createCellEditor(Composite parent, Properties context) {
		//ValueHelper valueHelper = new ValueHelper();
		//context.put("valueHelper", valueHelper);
		return new ExtendedJSPDialogCellEditor(parent, context);
	}

}

class ExtendedJSPDialogCellEditor extends JSPDialogCellEditor {
	AttributeWrapper wrapper;
	
	public ExtendedJSPDialogCellEditor(Composite parent, Properties context) {
		super(parent, context);
	}
	
	protected Object doGetValue() {
		if(wrapper != null) {
			wrapper.value = (String)super.doGetValue();
		}
		return wrapper;
	}

	protected void doSetValue(Object value) {
		if(value instanceof String) {
			if(wrapper != null) wrapper.value = value.toString();
			super.doSetValue(value);
			setValueValid(true);
			fireEditorValueChanged(false, true);
		} else if(value instanceof AttributeWrapper) {
			wrapper = (AttributeWrapper)value;
			super.doSetValue(wrapper == null ? "" : wrapper.value); //$NON-NLS-1$
			setValueValid(true);
		}
	}
	
    public void activate() {
    	if(context != null && wrapper != null) {
    		context.setProperty("attributeName", "" + wrapper.name); //$NON-NLS-1$ //$NON-NLS-2$
    	}
    	super.activate();
    	
    }
	protected Object openDialogBox(Control cellEditorWindow) {
		context.setProperty("attributeName", "" + wrapper.name); //$NON-NLS-1$ //$NON-NLS-2$
		if(wrapper != null && wrapper.value != null) context.put("value", wrapper.value); //$NON-NLS-1$
		Object o = super.openDialogBox(cellEditorWindow);
		if(o == null || o.equals(wrapper.value)) return null;
		AttributeWrapper newWrapper = new AttributeWrapper();
		newWrapper.attributes = wrapper.attributes;
		newWrapper.name = wrapper.name;
		newWrapper.value = o.toString();
		return newWrapper;
	}
}

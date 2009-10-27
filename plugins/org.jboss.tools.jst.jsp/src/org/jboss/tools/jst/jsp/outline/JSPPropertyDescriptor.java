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
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.jboss.tools.jst.jsp.outline.JSPPropertySourceAdapter.IQueryFactory;

/**
 * @author Kabanovich
 * Property Descriptor implementation that provides
 * JSPDialogCellEditor as property cell editor.
 */

public class JSPPropertyDescriptor extends PropertyDescriptor {
	Properties context;

	public JSPPropertyDescriptor(Properties context, Object id, String displayName) {
		super(id, displayName);
		this.context = context;
	}

    public CellEditor createPropertyEditor(Composite parent) {
    	CellEditor editor = null;
    	IQueryFactory queryFactory = (IQueryFactory)context.get("queryFactory"); //$NON-NLS-1$
    	String attributeName = (String)context.get("attributeName"); //$NON-NLS-1$
    	if(queryFactory != null && queryFactory.isAvailable(attributeName)) {
    		editor = new JSPDialogCellEditor(parent, context);
    	} else {
    		editor = new TextCellEditor(parent);
    	}
        
        if (getValidator() != null)
            editor.setValidator(getValidator());
        return editor;
    }
}

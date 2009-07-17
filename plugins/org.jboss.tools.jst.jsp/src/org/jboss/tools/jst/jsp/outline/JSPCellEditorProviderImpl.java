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
import org.jboss.tools.common.model.ui.objecteditor.ExtendedCellEditorProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;

public class JSPCellEditorProviderImpl implements ExtendedCellEditorProvider {

	public CellEditor createCellEditor(Composite parent, Properties context) {
		ValueHelper valueHelper = new ValueHelper();
		context.put("valueHelper", valueHelper); //$NON-NLS-1$
		return new JSPDialogCellEditor(parent, context);
	}

}


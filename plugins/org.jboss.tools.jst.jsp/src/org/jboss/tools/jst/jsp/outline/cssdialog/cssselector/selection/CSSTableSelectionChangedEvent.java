/*******************************************************************************
 * Copyright (c) 2007-2010 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.selection;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.model.CSSSelectorTreeModel;

/**
 * 
 * @author yzhishko
 * 
 */

@SuppressWarnings("serial")
public class CSSTableSelectionChangedEvent extends
		CSSClassSelectionChangedEvent {

	public CSSTableSelectionChangedEvent(ISelectionProvider source,
			ISelection selection, CSSSelectorTreeModel model) {
		super(source, selection);
		this.model = model;
	}

	@Override
	public String[] getSelectedClassNames() {
		StructuredSelection structuredSelection = (StructuredSelection) selection;
		Object[] selectedObjects = structuredSelection.toArray();
		String[] selectedNames = new String[selectedObjects.length];
		for (int i = 0; i < selectedObjects.length; i++) {
			selectedNames[i] = selectedObjects[i].toString();
		}
		return selectedNames;
	}

}

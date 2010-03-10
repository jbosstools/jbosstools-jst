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

package org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.viewers;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.model.CSSSelectorTreeModel;

/**
 * 
 * @author yzhishko
 *
 */

public class CSSSelectorTreeViewer extends TreeViewer {

	public final static String CSS_SELECTOR_TREE_VIWER_ID = "css_selector_tree_viwer"; //$NON-NLS-1$
	private CSSSelectorTreeModel model;
	
	public CSSSelectorTreeViewer(Composite parent, int style) {
		super(parent, style);
		setContentProvider(new CSSSelectorTreeContentProvider());
		setLabelProvider(new CSSSelectorTreeLabelProvider());
	}
	
	public void setModel(CSSSelectorTreeModel model){
		if (model != null) {
			setInput(model.getInvisibleRoot());
			this.model = model;
		}
	}
	
	public CSSSelectorTreeModel getModel(){
		return model;
	}

}

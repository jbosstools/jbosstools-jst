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

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.model.CSSTreeNode;

/**
 * 
 * @author yzhishko
 *
 */

public class CSSSelectorTreeContentProvider implements ITreeContentProvider {

	public Object[] getChildren(Object parentElement) {
		return ((CSSTreeNode)parentElement).getChildren().toArray();
	}

	public Object getParent(Object element) {
		return ((CSSTreeNode)element).getParent();
	}

	public boolean hasChildren(Object element) {
		return ((CSSTreeNode)element).hasChildren();
	}

	public Object[] getElements(Object inputElement) {
		return ((CSSTreeNode)inputElement).getChildren().toArray();
	}

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

}

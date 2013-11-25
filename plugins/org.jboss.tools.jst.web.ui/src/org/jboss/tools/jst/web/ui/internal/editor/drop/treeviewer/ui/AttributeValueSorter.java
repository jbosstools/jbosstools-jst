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
package org.jboss.tools.jst.web.ui.internal.editor.drop.treeviewer.ui;

import org.eclipse.jface.viewers.ViewerSorter;

import org.jboss.tools.jst.web.ui.internal.editor.drop.treeviewer.model.JsfVariablesResourceElement;

public class AttributeValueSorter extends ViewerSorter {
	
	/*
	 * @see ViewerSorter#category(Object)
	 */
	public int category(Object element) {
		if(element instanceof JsfVariablesResourceElement) {
			return 2;
		}
		return 1;
	}
}
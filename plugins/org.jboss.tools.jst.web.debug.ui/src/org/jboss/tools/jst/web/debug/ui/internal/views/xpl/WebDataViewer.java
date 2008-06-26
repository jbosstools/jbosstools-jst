/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Exadel, Inc.
 *     Red Hat, Inc. 
 *******************************************************************************/
package org.jboss.tools.jst.web.debug.ui.internal.views.xpl;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Tree;

// TODO-3.3: WTP 2.0
public class WebDataViewer { // extends AsynchronousTreeViewer {

	private Item fNewItem;
	
	/**
	 * Public constructor
	 * @param parent
	 */
	public WebDataViewer(Composite parent) {
//		super(parent);
	}

	/**
	 * Public constructor
	 * @param parent
	 * @param style
	 */
	public WebDataViewer(Composite parent, int style) {
//		super(parent, style);
	}

	/**
	 * Public constructor
	 * @param tree
	 */
	public WebDataViewer(Tree tree) {
//		super(tree);
	}
	
	/**
	 * @see Viewer#refresh()
	 */
	public void refresh() {
//		try {
//		super.refresh();
//		if (getSelection().isEmpty() && getNewItem() != null) {
//			if (!getNewItem().isDisposed()) {
//				// TODO replace with valid code				
//				//showItem(getNewItem());
//			}
//			setNewItem(null);
//		}
//		} catch (Exception x) {
//		}
	}
	
	
	/**
	 * 
	 * @return
	 */
	protected Item getNewItem() {
		return fNewItem;
	}

	/**
	 * 
	 * @param newItem
	 */
	protected void setNewItem(Item newItem) {
		fNewItem = newItem;
	}
}

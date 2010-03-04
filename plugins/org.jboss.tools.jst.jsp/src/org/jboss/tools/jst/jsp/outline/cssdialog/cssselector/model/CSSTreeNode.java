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

package org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author yzhishko
 *
 */

public class CSSTreeNode {

	private String name;
	private List<CSSTreeNode> children = new ArrayList<CSSTreeNode>(0);
	private CSSTreeNode parent;
	private Object cssResource;
	private String styleSheetSource;

	public CSSTreeNode(String n) {
		name = n;
	}

	public Object getParent() {
		return parent;
	}

	public CSSTreeNode addChild(CSSTreeNode child) {
		children.add(child);
		child.parent = this;
		return this;
	}

	public List<CSSTreeNode> getChildren() {
		return children;
	}

	public boolean hasChildren() {
		return children.size() > 0;
	}

	public String toString() {
		return name;
	}

	public void setCssResource(Object cssResource) {
		this.cssResource = cssResource;
	}

	public Object getCssResource() {
		return cssResource;
	}
	
	public void setStyleSheetSource(String source){
		styleSheetSource = source;
	}
	
	public String getStyleSheetSource(){
		return styleSheetSource;
	}
}

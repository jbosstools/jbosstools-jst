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
package org.jboss.tools.jst.jsp.drop.treeviewer.model;

import java.util.List;


/**
 * @author Igels
 */
public class RootElement extends ModelElement implements IAttributeValueContainer {

	private List<AttributeValueResource> children;

	public RootElement(String name, List<AttributeValueResource>  children) {
		super(name, null);
		this.children = children;
	}

	/**
	 * @see IAttributeValueContainer#getChildren()
	 */
	public ModelElement[] getChildren() {
		return children.toArray(new AttributeValueResource[0]);
	}

	/**
	 * @see ModelElement#compareValue(String)
	 */
	public int compareValue(String value) {
		return value.length();
	}
}
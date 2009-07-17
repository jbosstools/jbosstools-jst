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

/**
 * @author Igels
 */
public class ImageFileElement extends ModelElement implements IAttributeValue {

	public ImageFileElement(String name, ModelElement parent) {
		super(name, parent);
	}

	protected String getFullName() {
		return parent.getFullName() + getName();
	}

	public String getValue() {
		String path = getFullName();
		ModelElement p = getParent();
		while(p != null && !p.getFullName().equals("/")) p = p.getParent(); //$NON-NLS-1$
		if(p instanceof ImageFileResourceElement) {
			ImageFileResourceElement r = (ImageFileResourceElement)p;
			path = r.wtpKbResource.encodePath(path, r.getQuery().getLastParentTag(), r.getValueHelper());
		}		
		return path;
	}

	public int compareValue(String value) {
		if(value==null) {
			return NOT_EQUAL_VALUES;
		}
		String thisValue = getValue(); // getComparedValue();
		if(value.equals(thisValue)) {
			return EQUAL_VALUES;
		} else if(value.startsWith(thisValue) || thisValue.startsWith(value)) {
			return value.length() - thisValue.length();
		}
		return NOT_EQUAL_VALUES;
	}
}
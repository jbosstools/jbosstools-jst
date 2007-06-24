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
public class BundleNameElement extends ModelElement implements IAttributeValue {

	public BundleNameElement(BundlesNameResourceElement parent) {
		super(parent);
	}

	public BundleNameElement(String bundleAlias, BundlesNameResourceElement parent) {
		super(bundleAlias, parent);
	}

	public String getValue() {
		return name;
	}

	/**
	 * @see ModelElement#getComparedValue()
	 */
	public int compareValue(String value) {
		if(value==null) {
			return NOT_EQUAL_VALUES;
		}
		String thisValue = getComparedValue();
		if(value.equals(thisValue)) {
			return EQUAL_VALUES;
		} else if(value.startsWith(thisValue) || thisValue.startsWith(value)) {
			return value.length() - thisValue.length();
		}
		return NOT_EQUAL_VALUES;
	}
}
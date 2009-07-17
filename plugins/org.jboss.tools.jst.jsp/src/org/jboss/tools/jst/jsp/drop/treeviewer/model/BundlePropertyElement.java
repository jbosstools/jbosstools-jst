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
public class BundlePropertyElement extends ModelElement implements IAttributeValue {

	public BundlePropertyElement(BundleAliasElement parent) {
		super(parent);
	}

	public BundlePropertyElement(String name, BundleAliasElement parent) {
		super(name, parent);
	}

	/**
	 * @see IAttributeValue#getValue()
	 */
	@SuppressWarnings("nls")
	public String getValue() {
		String propertyName;
		if(name.indexOf('.')!=-1) {
			propertyName = "['" + name + "']";
		} else {
			propertyName = "." + name;
		}
		return "#{" + ((BundleAliasElement)parent).getName() + propertyName + "}";
	}

	/**
	 * @see ModelElement#getComparedValue()
	 */
	protected String getComparedValue() {
		String value = getValue();
		return value.substring(0, value.length()-1);
	}
}
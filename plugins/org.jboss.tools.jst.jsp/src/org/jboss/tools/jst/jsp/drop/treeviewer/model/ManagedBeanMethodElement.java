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
public class ManagedBeanMethodElement extends ManagedBeanForMdElement implements IAttributeValue {

	public ManagedBeanMethodElement(String name, ManagedBeanForMdElement parent) {
		super(name, parent);
	}

	protected String getFullName() {
		return parent.getFullName() + "." + name; //$NON-NLS-1$
	}

	/**
	 * @see IAttributeValue#getValue()
	 */
	public String getValue() {
		return "#{" + getFullName() + "}"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * @see ModelElement#getComparedValue()
	 */
	protected String getComparedValue() {
		return "#{" + getFullName(); //$NON-NLS-1$
	}

	private static Class[] EQUAL_CLASSES_LIST = new Class[] {
		ManagedBeanPropertyElement.class
	};

	protected Class[] getEqualClasses() {
		return EQUAL_CLASSES_LIST;
	}
}
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
public class JsfVariableElement extends ModelElement implements IAttributeValue {

	public JsfVariableElement(ModelElement parent) {
		super(parent);
	}

	public JsfVariableElement(String name, ModelElement parent) {
		super(name, parent);
	}

	/**
	 * @see IAttributeValue#getValue()
	 */
	public String getValue() {
		return "#{" + name + "}"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * @see ModelElement#getComparedValue()
	 */
	protected String getComparedValue() {
		return "#{" + name; //$NON-NLS-1$
	}
}
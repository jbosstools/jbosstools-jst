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

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.jboss.tools.jst.jsp.outline.ValueHelper;

/**
 * 
 * @author Viacheslav Kabanovich	
 */
public class SeamVariableElement extends SeamElement {

	public SeamVariableElement(String name, ModelElement parent) {
		super(name, parent);
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

}

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

import org.jboss.tools.jst.jsp.messages.JstUIMessages;
import org.jboss.tools.jst.jsp.support.kb.WTPKbJsfValuesResource;

/**
 * @author Igels
 */
public class JsfVariablesResourceElement extends AttributeValueResource {

	private JsfVariableElement[] jsfVariableElement;

	public JsfVariablesResourceElement(ModelElement parent) {
		super(parent);
	}

	public JsfVariablesResourceElement(String name, ModelElement parent) {
		super(name, parent);
	}

	/**
	 * @see IAttributeValueContainer#getChildren()
	 */
	public ModelElement[] getChildren() {
		if(jsfVariableElement==null) {
			String[] values = WTPKbJsfValuesResource.getJsfValues();
			jsfVariableElement = new JsfVariableElement[values.length];
			for(int i=0; i<values.length; i++) {
				jsfVariableElement[i] = new JsfVariableElement(values[i], this);
			}
		}
		return jsfVariableElement;
	}

	/**
	 * @see ModelElement#getName()
	 */
	public String getName() {
		return "JSF Variables"; //$NON-NLS-1$
	}
}
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

import org.jboss.tools.jst.web.kb.internal.taglib.CustomProposalType.Param;
import org.jboss.tools.jst.jsp.messages.JstUIMessages;

/**
 * @author Igels
 */
public class EnumerationResourceElement extends AttributeValueResource {

	private EnumerationElement[] enumerationElements; 

	public EnumerationResourceElement(ModelElement parent) {
		super(parent);
	}

	public EnumerationResourceElement(String name, ModelElement parent) {
		super(name, parent);
	}

	/**
	 * @see IAttributeValueContainer#getChildren()
	 */
	public ModelElement[] getChildren() {
		if(enumerationElements==null) {
			Param[] params = getParams();
			enumerationElements = new EnumerationElement[params.length];
			for(int i=0; i<enumerationElements.length; i++) {
				String value = params[i].getValue();
				enumerationElements[i] = new EnumerationElement(value, this);
			}
		}
		return enumerationElements;
	}

	/**
	 * @see ModelElement#getName()
	 */
	public String getName() {
		return "Enumeration"; //$NON-NLS-1$
	}
}
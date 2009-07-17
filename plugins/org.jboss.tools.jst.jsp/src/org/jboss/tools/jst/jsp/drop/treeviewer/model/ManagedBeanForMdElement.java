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
import java.util.Properties;

import org.jboss.tools.jst.web.project.list.IWebPromptingProvider;
import org.jboss.tools.jst.web.project.list.WebPromptingProvider;

/**
 * @author Igels
 */
public class ManagedBeanForMdElement extends ModelElement implements IAttributeValueContainer {

	public static String SUPPORTED_ID = WebPromptingProvider.JSF_BEAN_METHODS;
	private ManagedBeanMethodElement[] managedBeanMethodElements;

	public ManagedBeanForMdElement(String name, ModelElement parent) {
		super(name, parent);
	}

	protected ManagedBeanMethodResourceElement getResource() {
		ModelElement currentParent = parent;
		while(currentParent!=null) {
			if(currentParent instanceof ManagedBeanMethodResourceElement) {
				return (ManagedBeanMethodResourceElement)currentParent;
			}
			currentParent = currentParent.getParent();
		}
		throw new RuntimeException("Can't get ManagedBeanMethodResourceElement for element."); //$NON-NLS-1$
	}

	/**
	 * @see ModelElement#getComparedValue()
	 */
	protected String getComparedValue() {
		return "#{" + getFullName(); //$NON-NLS-1$
	}

	/**
	 * @see IAttributeValueContainer#getChildren()
	 */
	public ModelElement[] getChildren() {
		if(managedBeanMethodElements==null) {
			ManagedBeanMethodResourceElement parentResource = getResource();

			Properties types = new Properties();
			String[] typeValues = parentResource.getParams().getParamsValues("paramType"); //$NON-NLS-1$
			types.put(IWebPromptingProvider.PARAMETER_TYPES, typeValues);
			String[] returnTypes = parentResource.getParams().getParamsValues("returnType"); //$NON-NLS-1$
			if(returnTypes.length>0) {
				types.put(IWebPromptingProvider.RETURN_TYPE, returnTypes[0]);
			} else {
				types.put(IWebPromptingProvider.RETURN_TYPE, "void"); //$NON-NLS-1$
			}

			List properties = parentResource.getProvider().getList(parentResource.getXModel(), SUPPORTED_ID, getFullName(), types);
			managedBeanMethodElements = new ManagedBeanMethodElement[properties.size()];
			for(int i=0; i<properties.size(); i++) {
				String propertyName = (String)properties.get(i);
				managedBeanMethodElements[i] = new ManagedBeanMethodElement(propertyName, this);
			}
		}
		return managedBeanMethodElements;
	}

	private static Class[] EQUAL_CLASSES_LIST = new Class[] {
		ManagedBeanForPropElement.class
	};

	protected Class[] getEqualClasses() {
		return EQUAL_CLASSES_LIST;
	}
}
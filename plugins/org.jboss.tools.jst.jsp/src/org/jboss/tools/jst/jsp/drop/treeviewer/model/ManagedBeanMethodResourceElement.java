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

import org.eclipse.ui.IEditorInput;

import org.jboss.tools.jst.web.project.list.WebPromptingProvider;

/**
 * @author Igels
 */
public class ManagedBeanMethodResourceElement extends XModelAttributeValueResource {

	private ManagedBeanForMdElement[] managedBeanElements;

	public ManagedBeanMethodResourceElement(IEditorInput editorInput, ModelElement parent) {
		super(editorInput, parent);
	}

	public ManagedBeanMethodResourceElement(IEditorInput editorInput, String name, ModelElement parent) {
		super(editorInput, name, parent);
	}

	/**
	 * @see IAttributeValueContainer#getChildren()
	 */
	public ModelElement[] getChildren() {
		if(managedBeanElements!=null) {
			return managedBeanElements;
		}
		if(!isReadyToUse()) {
			return EMPTY_LIST;
		}

		List beanList = provider.getList(xModel, WebPromptingProvider.JSF_MANAGED_BEANS, "", null); //$NON-NLS-1$
		managedBeanElements = new ManagedBeanForMdElement[beanList.size()];
		for(int i=0; i<beanList.size(); i++) {
			String beanName = (String)beanList.get(i);
			managedBeanElements[i] = new ManagedBeanForMdElement(beanName, this);
		}

		return managedBeanElements;
	}

	/**
	 * @see ModelElement#getName()
	 */
	public String getName() {
		return "Managed Beans"; //$NON-NLS-1$
	}

	private static Class[] EQUAL_CLASSES_LIST = new Class[] {
		ManagedBeansPropertiesResourceElement.class
	};

	protected Class[] getEqualClasses() {
		return EQUAL_CLASSES_LIST;
	}
}
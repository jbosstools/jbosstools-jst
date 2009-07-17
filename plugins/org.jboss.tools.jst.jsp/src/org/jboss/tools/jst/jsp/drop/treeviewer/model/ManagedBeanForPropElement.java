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
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.jst.jsp.messages.JstUIMessages;
import org.jboss.tools.jst.web.project.list.IWebPromptingProvider;
import org.jboss.tools.jst.web.project.list.WebPromptingProvider;

/**
 * @author Igels
 */
public class ManagedBeanForPropElement extends ModelElement implements IAttributeValueContainer {

	public static String SUPPORTED_ID = WebPromptingProvider.JSF_BEAN_PROPERTIES;
	private ManagedBeanPropertyElement[] managedBeanPropertyElements;

	public ManagedBeanForPropElement(String beanName, ModelElement parent) {
		super(beanName, parent);
	}

	protected ManagedBeansPropertiesResourceElement getResource() {
		ModelElement currentParent = parent;
		while(currentParent!=null) {
			if(currentParent instanceof ManagedBeansPropertiesResourceElement) {
				return (ManagedBeansPropertiesResourceElement)currentParent;
			}
			currentParent = currentParent.getParent();
		}
		throw new RuntimeException("Can't get ManagedBeansPropertiesResourceElement for element."); //$NON-NLS-1$
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
		if(managedBeanPropertyElements==null) {
			ManagedBeansPropertiesResourceElement parentResource = getResource();
			String typeValue = null;
			String[] typeValues = parentResource.getParamsValues("type"); //$NON-NLS-1$
			if(typeValues.length>0) {
				typeValue = typeValues[0];
			}
			Properties types = new Properties();
			if(typeValue!=null) {
				types.put(IWebPromptingProvider.PROPERTY_TYPE, typeValue);
			}
			List properties = parentResource.getProvider().getList(parentResource.getXModel(), SUPPORTED_ID, getFullName(), types);
			managedBeanPropertyElements = new ManagedBeanPropertyElement[properties.size()];
			for(int i=0; i<properties.size(); i++) {
				String propertyName = (String)properties.get(i);
				managedBeanPropertyElements[i] = new ManagedBeanPropertyElement(propertyName, this);
			}
		}
		return managedBeanPropertyElements;
	}

	private static Class[] EQUAL_CLASSES_LIST = new Class[] {
		ManagedBeanForMdElement.class
	};

	protected Class[] getEqualClasses() {
		return EQUAL_CLASSES_LIST;
	}

	static String ADD_PROPERTY_ACTION = JstUIMessages.ManagedBeanForPropElement_AddProperty;

	public String[] getActions() {
		return new String[]{ADD_PROPERTY_ACTION};
	}
	
	public void action(String name, Properties properties) {
		if(ADD_PROPERTY_ACTION.equals(name)) {
			addProperty(properties);
		}
	}
	
	void addProperty(Properties properties) {
		List list = WebPromptingProvider.getInstance().getList(getResource().getXModel(), IWebPromptingProvider.JSF_BEAN_ADD_PROPERTY, getName(), new Properties());
		if(list == null || list.size() == 0) return;
		XModelObject c = (XModelObject)list.get(0);		
		managedBeanPropertyElements = null;
		getChildren();
		for (int i = 0; i < managedBeanPropertyElements.length; i++) {
			ManagedBeanPropertyElement p = managedBeanPropertyElements[i];
			if(p.getName().equals(c.getAttributeValue("property-name"))) { //$NON-NLS-1$
				properties.put("select", p); //$NON-NLS-1$
			}
		}		
	}

}
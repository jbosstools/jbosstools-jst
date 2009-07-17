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
import java.util.Map;
import java.util.Properties;

import org.jboss.tools.common.meta.action.XActionInvoker;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.jst.jsp.messages.JstUIMessages;
import org.jboss.tools.jst.web.project.list.WebPromptingProvider;

/**
 * @author Igels
 */
public class BundleAliasElement extends ModelElement implements IAttributeValueContainer {

	public static String SUPPORTED_ID = WebPromptingProvider.JSF_BUNDLE_PROPERTIES;
	private BundlePropertyElement[] bundlePropertyElements;

	public BundleAliasElement(BundlesPropertiesResourceElement parent) {
		super(parent);
	}

	public BundleAliasElement(String bundleAlias, BundlesPropertiesResourceElement parent) {
		super(bundleAlias, parent);
	}

	protected BundlesPropertiesResourceElement getResource() {
		return (BundlesPropertiesResourceElement)parent;
	}

	/**
	 * @see IAttributeValueContainer#getChildren()
	 */
	public ModelElement[] getChildren() {
		if(bundlePropertyElements==null) {
			BundlesPropertiesResourceElement parentResource = getResource();
			String bundleAlias = name;
			Map bundles = parentResource.getBundles();

			String basename = bundles.get(bundleAlias).toString();
			List properties = parentResource.getProvider().getList(parentResource.getXModel(), SUPPORTED_ID, basename, null);
			bundlePropertyElements = new BundlePropertyElement[properties.size()];
			for(int i=0; i<properties.size(); i++) {
				String propertyName = (String)properties.get(i);
				bundlePropertyElements[i] = new BundlePropertyElement(propertyName, this);
			}
		}
		return bundlePropertyElements;
	}

	/**
	 * @see ModelElement#getComparedValue()
	 */
	protected String getComparedValue() {
		return "#{" + getFullName(); //$NON-NLS-1$
	}
	
	static String ADD_PROPERTY_ACTION = JstUIMessages.BundleAliasElement_AddProperty;

	public String[] getActions() {
		return new String[]{ADD_PROPERTY_ACTION};
	}
	
	public void action(String name, Properties properties) {
		if(ADD_PROPERTY_ACTION.equals(name)) {
			addProperty(properties);
		}
	}
	
	void addProperty(Properties properties) {
		BundlesPropertiesResourceElement parentResource = getResource();
		String bundleAlias = name;
		Map bundles = parentResource.getBundles();
		String basename = bundles.get(bundleAlias).toString();
		if(basename == null) return;
		XModelObject b = parentResource.getXModel().getByPath("/" + basename.replace('.', '/') + ".properties"); //$NON-NLS-1$ //$NON-NLS-2$
		if(b == null) return;
		properties.setProperty("actionSourceGUIComponentID", "dialog"); //$NON-NLS-1$ //$NON-NLS-2$
		XActionInvoker.invoke("CreateActions.CreateProperty", b, properties); //$NON-NLS-1$
		XModelObject c = (XModelObject)properties.get("created"); //$NON-NLS-1$
		if(c == null) return;
		bundlePropertyElements = null;
		getChildren();
		for (int i = 0; i < bundlePropertyElements.length; i++) {
			BundlePropertyElement p = bundlePropertyElements[i];
			if(p.getName().equals(c.getAttributeValue("name"))) { //$NON-NLS-1$
				properties.put("select", p); //$NON-NLS-1$
			}
		}		
	}

}
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

import org.eclipse.ui.IEditorInput;

import org.jboss.tools.common.meta.action.XActionInvoker;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.util.XModelObjectLoaderUtil;
import org.jboss.tools.jst.jsp.messages.JstUIMessages;
import org.jboss.tools.jst.web.project.list.WebPromptingProvider;

/**
 * @author Igels
 */
public class BundlesNameResourceElement extends XModelAttributeValueResource {

	public static String SUPPORTED_ID = WebPromptingProvider.JSF_BUNDLES;

	private BundleNameElement[] bundleNameElements;

	public BundlesNameResourceElement(IEditorInput editorInput, ModelElement parent) {
		super(editorInput, parent);
	}

	public BundlesNameResourceElement(IEditorInput editorInput, String name, ModelElement parent) {
		super(editorInput, name, parent);
	}

	/**
	 * @see IAttributeValueContainer#getChildren()
	 */
	public ModelElement[] getChildren() {
		if(bundleNameElements!=null) {
			return bundleNameElements;
		}
		if(!isReadyToUse()) {
			return EMPTY_LIST;
		}

		List sourceList = provider.getList(xModel, SUPPORTED_ID, "", null); //$NON-NLS-1$
		bundleNameElements = new BundleNameElement[sourceList.size()];
		for (int i=0; i<sourceList.size(); i++) {
			String bundleName = (String)sourceList.get(i);
			bundleNameElements[i] = new BundleNameElement(bundleName, this);
		}

		return bundleNameElements;
	}

	/**
	 * @see ModelElement#getName()
	 */
	public String getName() {
		return "Resource Bundles"; //$NON-NLS-1$
	}

	static String ADD_BUNDLE_ACTION = JstUIMessages.BundlesNameResourceElement_AddBundle;

	public String[] getActions() {
		return new String[]{ADD_BUNDLE_ACTION};
	}
	
	public void action(String name, Properties properties) {
		if(ADD_BUNDLE_ACTION.equals(name)) {
			addBundle(properties);
		}
	}
	
	void addBundle(Properties properties) {
		XModelObject o = getXModel().getByPath("root:JSFProjects/Resource Bundles"); //$NON-NLS-1$
		if(o == null) return;
		XActionInvoker.invoke("CreateActions.CreateFiles.CreateFileProperties", o, properties); //$NON-NLS-1$
		XModelObject c = (XModelObject)properties.get("created"); //$NON-NLS-1$

		bundleNameElements = null;
		if(c == null) return;

		o.set("invalidate", "true"); //$NON-NLS-1$ //$NON-NLS-2$
		getChildren();
		String path = getBaseName(c);
		if(path == null) return;
		
		for (int i = 0; i < bundleNameElements.length; i++) {
			String name = bundleNameElements[i].getName();
			if(name.equals(path)) {
				properties.put("select", bundleNameElements[i]); //$NON-NLS-1$
			}			
		}
	}
	
	private String getBaseName(XModelObject c) {
		String path = XModelObjectLoaderUtil.getResourcePath(c);
		if(path == null) return null;
		if(path.endsWith(".properties")) path = path.substring(0, path.length() - ".properties".length()); //$NON-NLS-1$ //$NON-NLS-2$
		if(path.startsWith("/")) path = path.substring(1); //$NON-NLS-1$
		return path.replace('/', '.');
	}
}
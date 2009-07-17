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

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.ui.IEditorInput;

import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.IResourceBundle;
import org.jboss.tools.jst.web.project.list.WebPromptingProvider;

/**
 * @author Igels
 */
public class BundlesPropertiesResourceElement extends XModelAttributeValueResource {

	public static String SUPPORTED_ID = WebPromptingProvider.JSF_BUNDLES;

	private BundleAliasElement[] bundleAliasElements;
	private IPageContext pageContext;
	private Map bundles;

	public BundlesPropertiesResourceElement(IEditorInput editorInput, IPageContext pageContext, ModelElement parent) {
		super(editorInput, parent);
		this.pageContext = pageContext;
	}

	public BundlesPropertiesResourceElement(IEditorInput editorInput, IPageContext pageContext, String name, ModelElement parent) {
		super(editorInput, name, parent);
		this.pageContext = pageContext;
	}

	/**
	 * @see IAttributeValueContainer#getChildren()
	 */
	public ModelElement[] getChildren() {
		if(bundleAliasElements!=null) {
			return bundleAliasElements;
		}
		if(!isReadyToUse()) {
			return EMPTY_LIST;
		}
		bundles = new TreeMap();
		List registeredBunbles = provider.getList(xModel, WebPromptingProvider.JSF_REGISTERED_BUNDLES, null, null);
		if(registeredBunbles != null && registeredBunbles.size() > 1 && (registeredBunbles.get(0) instanceof Map)) {
			bundles.putAll((Map)registeredBunbles.get(0));
		}
		IResourceBundle[] bs = pageContext.getResourceBundles();
		for (IResourceBundle b: bs) {
			String alias = b.getVar();
			bundles.put(alias, b.getBasename());
		}
		bundleAliasElements = new BundleAliasElement[bundles.size()];
		
		Iterator itr = bundles.keySet().iterator();
		for (int i=0; itr.hasNext(); i++) {
			String bundleAlias = (String)itr.next();
			bundleAliasElements[i] = new BundleAliasElement(bundleAlias, this);
		}

		return bundleAliasElements;
	}

	/**
	 * @see ModelElement#getName()
	 */
	public String getName() {
		return "Bundle Properies"; //$NON-NLS-1$
	}

	public Map getBundles() {
		return bundles;
	}
}
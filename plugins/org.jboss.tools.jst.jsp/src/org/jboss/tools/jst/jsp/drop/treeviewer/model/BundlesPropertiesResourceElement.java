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

import org.jboss.tools.jst.jsp.support.kb.WTPTextJspKbConnector;
import org.jboss.tools.jst.web.project.list.WebPromptingProvider;

/**
 * @author Igels
 */
public class BundlesPropertiesResourceElement extends XModelAttributeValueResource {

	public static String SUPPORTED_ID = WebPromptingProvider.JSF_BUNDLES;

	private BundleAliasElement[] bundleAliasElements;
	private WTPTextJspKbConnector wtpTextJspKbConnector;
	private Map bundles;

	public BundlesPropertiesResourceElement(IEditorInput editorInput, WTPTextJspKbConnector wtpTextJspKbConnector, ModelElement parent) {
		super(editorInput, parent);
		this.wtpTextJspKbConnector = wtpTextJspKbConnector;
	}

	public BundlesPropertiesResourceElement(IEditorInput editorInput, WTPTextJspKbConnector wtpTextJspKbConnector, String name, ModelElement parent) {
		super(editorInput, name, parent);
		this.wtpTextJspKbConnector = wtpTextJspKbConnector;
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
		Map bundles2 = wtpTextJspKbConnector.getDeclaredBundles();
		Iterator it2 = bundles2.keySet().iterator();
		while(it2.hasNext()) {
			String alias = it2.next().toString();
			WTPTextJspKbConnector.LoadBundleInfo info = (WTPTextJspKbConnector.LoadBundleInfo)bundles2.get(alias);
			bundles.put(alias, info.getBaseName());
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
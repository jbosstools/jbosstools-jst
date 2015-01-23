/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.web.kb.internal;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.jboss.tools.jst.web.kb.IBrowserDataProvider;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.WebKbPlugin;

/**
 * @author Alexey Kazakov
 */
public class BrowserDataProviderManager {

	public static final String ELEMENT_ID_PARAM = "idParamName";
	public static final String ELEMENT_ID_VALUE_PARAM = "idParamValue";
	public static final Object DATA_LOADING = new Object(); // If the browser evaluation result equals to this object then it means that the data is still loading (for example the browser may still be being initialized. 

	private static final BrowserDataProviderManager INSTANCE = new BrowserDataProviderManager();

	private Set<IBrowserDataProvider> providers;

	private BrowserDataProviderManager() {
	}

	public static BrowserDataProviderManager getInstance() {
		return INSTANCE;
	}

	private void init() {
		if(providers==null) {
			providers = new HashSet<IBrowserDataProvider>();
	        IExtensionRegistry registry = Platform.getExtensionRegistry();
			IExtensionPoint extensionPoint = registry.getExtensionPoint("org.jboss.tools.jst.web.kb.browserDataProvider"); //$NON-NLS-1$
			if (extensionPoint != null) { 
				IExtension[] extensions = extensionPoint.getExtensions();
				for(IExtension extension : extensions) {
					IConfigurationElement[] elements = extension.getConfigurationElements();
					for (IConfigurationElement element : elements) {
						String className = element.getAttribute("class"); //$NON-NLS-1$
						if(className!=null) {
							try {
								Object obj = element.createExecutableExtension("class");
								if(obj instanceof IBrowserDataProvider) {
									providers.add((IBrowserDataProvider)obj);
								} else {
									WebKbPlugin.getDefault().logError("Browser Data Provider (class name: " + className + ", contributer: " + element.getContributor().getName() + ") must implement " + IBrowserDataProvider.class.getName());
								}
							} catch (CoreException e) {
								WebKbPlugin.getDefault().logError(e);
							}
						}
					}
				}
			}
		}
	}

	public Collection<Object> evaluate(String js, IPageContext pageContext) {
		init();
		Set<Object> results = new HashSet<Object>();
		for (IBrowserDataProvider provider : providers) {
			Object result = provider.evaluate(js, pageContext);
			results.add(result);
		}
		return results;
	}

	public static String format(String js, Map<String, String> parameters) {
		for (String name : parameters.keySet()) {
			js = js.replaceAll("#\\{" + name + "\\}", parameters.get(name));
		}
		return js;
	}
}
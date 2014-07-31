/******************************************************************************* 
 * Copyright (c) 2014 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.internal.preferences.js;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.jboss.tools.common.model.plugin.ModelPlugin;
import org.jboss.tools.common.model.ui.editors.dnd.IElementGenerator.ElementNode;
import org.jboss.tools.common.util.FileUtil;
import org.jboss.tools.jst.web.kb.internal.taglib.html.jq.JQueryMobileVersion;
import org.jboss.tools.jst.web.ui.WebUiPlugin;
import org.jboss.tools.jst.web.ui.internal.editor.outline.JQueryCategoryFilter;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryConstants;
import org.osgi.framework.Bundle;
import org.osgi.service.prefs.BackingStoreException;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class JSLibFactory {
	static JSLibFactory instance = new JSLibFactory();

	static String JS_CSS_LIBS_PREFERENCE_NAME = "js-css-libs";

	public static JSLibFactory getInstance() {
		return instance;
	}

	private JSLibModel defaultModel;

	private JSLibModel preferenceModel;

	private JSLibModel workingCopy;

	private JSLibFactory() {}

	/**
	 * Returns model instance provided by this class. Preferences model is set to the default model 
	 * until modified.
	 * 
	 * @return model instance provided by this class
	 */
	public JSLibModel getDefaultModel() {
		if(defaultModel == null) {
			createDefaultModel();
		}
		return defaultModel;
	}

	/**
	 * Returns model instance saved in Eclipse preferences.
	 * 
	 * @return
	 */
	public JSLibModel getPreferenceModel() {
		if(preferenceModel == null) {
			loadPreferenceModel();
		}
		return preferenceModel;
	}

	/**
	 * 
	 * @return working copy of preferences model
	 */
	public synchronized JSLibModel getWorkingCopy() {
		if(workingCopy == null) {
			workingCopy = createCopy(getPreferenceModel());
		}
		return workingCopy;
	}

	public synchronized void applyDefault() {
		if(workingCopy != null) {
			workingCopy.applyWorkingCopy(createCopy(getDefaultModel()), false);
		}
	}

	/**
	 * Applies working copy to preferences model.
	 */
	public synchronized void applyWorkingCopy() {
		if(workingCopy != null) {
			preferenceModel.applyWorkingCopy(workingCopy, true);
		}
	}

	/**
	 * Disposes working copy after it is applied to preferences model, or to cancel changes.
	 */
	public synchronized void disposeWorkingCopy() {
		workingCopy = null;
	}

	synchronized void createDefaultModel() {
		if(defaultModel != null) return;
		JSLibModel model = new JSLibModel();

		List<URL> resources = getResources();
		
		for (URL url: resources) {
			try {
				String s = FileUtil.readStream(url.openStream());
				JSLibModel m = JSLibXMLLoader.load(s);
				model.applyWorkingCopy(m, false);
			} catch (IOException e) {
				WebUiPlugin.getDefault().logError(e);
			}
		}

		defaultModel = model;
	}

	synchronized void loadPreferenceModel() {
		if(preferenceModel != null) return;
		IEclipsePreferences node = (IEclipsePreferences)
				Platform.getPreferencesService().getRootNode()
					.node(InstanceScope.SCOPE).node(WebUiPlugin.PLUGIN_ID);
		String s = node.get(JS_CSS_LIBS_PREFERENCE_NAME, null);
		if(s == null) {
			s = JSLibXMLLoader.saveToString(getDefaultModel());
		}
		JSLibModel model = JSLibXMLLoader.load(s);
		if(model == null) {
			model = createCopy(getDefaultModel());
		}
		preferenceModel = model;
	}

	synchronized void savePreferenceModel() {
		if(preferenceModel == null) return;
		IEclipsePreferences node = (IEclipsePreferences)
				Platform.getPreferencesService().getRootNode()
					.node(InstanceScope.SCOPE).node(WebUiPlugin.PLUGIN_ID);
		synchronized (preferenceModel) {
			String s = JSLibXMLLoader.saveToString(preferenceModel);
			node.put(JS_CSS_LIBS_PREFERENCE_NAME, s);
		}
		try {
			node.flush();
		} catch (BackingStoreException e) {
			WebUiPlugin.getDefault().logError(e);
		}
	}

	synchronized JSLibModel createCopy(JSLibModel source) {
		JSLibModel copy = new JSLibModel();
		for (JSLib lib: source.getLibs()) {
			JSLib libCopy = copy.getOrCreateLib(lib.getName());
			for (JSLibVersion version: lib.getVersions()) {
				JSLibVersion versionCopy = libCopy.getOrCreateVersion(version.getVersion());
				versionCopy.getURLs().addAll(version.getURLs());
			}
		}
		return copy;
	}		

	public static List<URL> getResources() {
		List<URL> resources = new ArrayList<URL>();
		IExtensionPoint point = Platform.getExtensionRegistry().getExtensionPoint("org.jboss.tools.jst.web.ui.jscssLibs"); //$NON-NLS-1$
		IExtension[] es = point.getExtensions();
		for (int i = 0; i < es.length; i++) {
			Bundle bundle = Platform.getBundle(es[i].getNamespaceIdentifier());
			IConfigurationElement[] elements = es[i].getConfigurationElements();
			for (int j = 0; j < elements.length; j++) {
				String path = elements[j].getAttribute("path"); //$NON-NLS-1$
				if(path == null) continue;
				try {
					URL url = bundle.getResource(path);
					if(url != null) {
						resources.add(url);
					} else {
						if(WebUiPlugin.isDebugEnabled()) {
							WebUiPlugin.getDefault().logInfo("Warning: meta resource " + path + " not found."); //$NON-NLS-1$ //$NON-NLS-2$
						}
					}
				} catch (IllegalStateException e) {
					ModelPlugin.getPluginLog().logError("MetaResourceLoader warning: meta resource " + path + " not found."); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
		return resources;
	}

}

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

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.jboss.tools.jst.web.kb.internal.taglib.html.jq.JQueryMobileVersion;
import org.jboss.tools.jst.web.ui.WebUiPlugin;
import org.jboss.tools.jst.web.ui.internal.editor.outline.JQueryCategoryFilter;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryConstants;
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

		JSLib jQuery = model.getOrCreateLib(JQueryCategoryFilter.CATEGORY_JQM);
		String[] jQueryVersions = {"1.9.1", "1.10.2", "1.11.1", "2.0.1", "2.0.3", "2.1.0", "2.1.1"};
		for (String s: jQueryVersions) {
			jQuery.getOrCreateVersion(s).getURLs().add("http://code.jquery.com/jquery-" + s + ".min.js");
		}

		JSLib jQueryMobile = model.getOrCreateLib(JQueryConstants.JQM_CATEGORY);
		String[] jQueryMobileVersions = {"1.3.1", JQueryMobileVersion.JQM_1_3.getFullDefaultVersion(), JQueryMobileVersion.JQM_1_4.getFullDefaultVersion()};
		for (String s: jQueryMobileVersions) {
			JSLibVersion v = jQueryMobile.getOrCreateVersion(s);
			v.getURLs().add("http://code.jquery.com/mobile/" + s + "/jquery.mobile-" + s + ".min.js");
			v.getURLs().add("http://code.jquery.com/mobile/" + s + "/jquery.mobile-" + s + ".min.css");
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
	
}

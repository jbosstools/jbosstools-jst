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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.jboss.tools.common.util.FileUtil;
import org.jboss.tools.jst.web.ui.WebUiPlugin;
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
		String s = getPreference(JS_CSS_LIBS_PREFERENCE_NAME);
		JSLibModel prev = getPreviousDefaultModel();
		if(s == null) {
			preferenceModel = createCopy(getDefaultModel());
		} else {
			preferenceModel = JSLibXMLLoader.load(s);
			if(preferenceModel == null) {
				preferenceModel = createCopy(getDefaultModel());
			} else if(prev != null) {
				List<String> diff = new ArrayList<String>();
				mergeDefaultModel(diff, preferenceModel, prev, getDefaultModel());
				if(!diff.isEmpty()) {
					savePreferenceModel();
				}
			}
		}
	}

	private String getPreference(String name) {
		IEclipsePreferences node = (IEclipsePreferences)
				Platform.getPreferencesService().getRootNode()
					.node(InstanceScope.SCOPE).node(WebUiPlugin.PLUGIN_ID);
		return node.get(name, null);
	}

	private void setPreference(String name, String value) {
		IEclipsePreferences node = (IEclipsePreferences)
				Platform.getPreferencesService().getRootNode()
					.node(InstanceScope.SCOPE).node(WebUiPlugin.PLUGIN_ID);
		node.put(name, value);
		try {
			node.flush();
		} catch (BackingStoreException e) {
			WebUiPlugin.getDefault().logError(e);
		}
	}

	private JSLibModel getPreviousDefaultModel() {
		Bundle bundle = Platform.getBundle(WebUiPlugin.PLUGIN_ID);
		File location = Platform.getStateLocation(bundle).toFile();
		File f = new File(location, ".js-css.xml");
		JSLibModel newDefaultModel = getDefaultModel();
		String newText = JSLibXMLLoader.saveToString(getDefaultModel());
		if(!f.exists()) {
			FileUtil.writeFile(f, newText);
			return null;
		}
		JSLibModel oldDefaultModel = JSLibXMLLoader.load(FileUtil.readFile(f));
		if(newDefaultModel.equals(oldDefaultModel)) {
			return null;
		}
		FileUtil.writeFile(f, newText);
		return oldDefaultModel;
	}

	synchronized void savePreferenceModel() {
		if(preferenceModel == null) return;
		setPreference(JS_CSS_LIBS_PREFERENCE_NAME, JSLibXMLLoader.saveToString(preferenceModel));
	}

	synchronized JSLibModel createCopy(JSLibModel source) {
		JSLibModel copy = new JSLibModel();
		for (JSLib lib: source.getLibs()) {
			addLibCopy(copy, lib);
		}
		return copy;
	}		

	synchronized JSLib addLibCopy(JSLibModel copy, JSLib source) {
		JSLib libCopy = copy.getOrCreateLib(source.getName());
		for (JSLibVersion version: source.getVersions()) {
			addVersionCopy(libCopy, version);
		}
		return libCopy;
	}		

	synchronized JSLibVersion addVersionCopy(JSLib copy, JSLibVersion source) {
		JSLibVersion versionCopy = copy.getOrCreateVersion(source.getVersion());
		versionCopy.getURLs().addAll(source.getURLs());
		return versionCopy;
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
							WebUiPlugin.getDefault().logInfo("Warning: js/css lib resource " + path + " not found."); //$NON-NLS-1$ //$NON-NLS-2$
						}
					}
				} catch (IllegalStateException e) {
					WebUiPlugin.getDefault().logError("MetaResourceLoader warning: js/css lib resource " + path + " not found."); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
		return resources;
	}

	public void mergeDefaultModel(List<String> diff, JSLibModel model, JSLibModel oldDefaultModel, JSLibModel newDefaultModel) {
		for (JSLib lib: newDefaultModel.getLibs()) {
			JSLib currentLib = model.getLib(lib.getName());
			JSLib oldDefaultLib = oldDefaultModel.getLib(lib.getName());
			if(currentLib == null && (oldDefaultLib == null || !oldDefaultLib.equals(lib))) {
				addLibCopy(model, lib);
				diff.add("A " + lib.getName());
			} else if(currentLib != null) {
				if(oldDefaultLib == null) {
					currentLib.applyWorkingCopy(lib, false);
					diff.add("U " + lib.getName());
				} else {
					mergeDefaultLib(diff, currentLib, oldDefaultLib, lib);
				}
			}
		}
		for (JSLib lib: oldDefaultModel.getLibs()) {
			JSLib currentLib = model.getLib(lib.getName());
			JSLib newDefaultLib = newDefaultModel.getLib(lib.getName());
			if(newDefaultLib == null && currentLib != null && lib.equals(currentLib)) {
				model.removeLib(currentLib);
				diff.add("R " + currentLib.getName());
			}			
		}
	}

	public void mergeDefaultLib(List<String> diff, JSLib lib, JSLib oldDefaultLib, JSLib newDefaultLib) {
		for (JSLibVersion version: newDefaultLib.getVersions()) {
			JSLibVersion currentVersion = lib.getVersion(version.getVersion());
			JSLibVersion oldDefaultVersion = oldDefaultLib.getVersion(version.getVersion());
			if(currentVersion == null && (oldDefaultVersion == null || !version.equals(oldDefaultVersion))) {
				addVersionCopy(lib, version);
				diff.add("A " + lib.getName() + ":" + version.getVersion());
			} else if(currentVersion != null) {
				if(oldDefaultVersion == null) {
					currentVersion.applyWorkingCopy(version, false);
					diff.add("U " + lib.getName() + ":" + version.getVersion());
				} else {
					mergeDefaultVersion(diff, currentVersion, oldDefaultVersion, version);
				}
			}
		}
		for (JSLibVersion version: oldDefaultLib.getVersions()) {
			JSLibVersion currentVersion = lib.getVersion(version.getVersion());
			JSLibVersion newDefaultVersion = newDefaultLib.getVersion(version.getVersion());
			if(newDefaultVersion == null && currentVersion != null && version.equals(currentVersion)) {
				lib.removeVersion(currentVersion);
				diff.add("R " + lib.getName() + ":" + currentVersion.getVersion());
			}
		}
	}

	public void mergeDefaultVersion(List<String> diff, JSLibVersion version, JSLibVersion oldDefaultVersion, JSLibVersion newDefaultVersion) {
		for (String url: newDefaultVersion.getURLs()) {
			boolean currentHasUrl = version.getURLs().contains(url);
			boolean oldDefaultHasUrl = oldDefaultVersion.getURLs().contains(url);
			if(!currentHasUrl && !oldDefaultHasUrl) {
				version.getURLs().add(url);
				diff.add("A " + version.getLib().getName() + ":" + version.getVersion() + ":" + url);
			}
		}
		for (String url: oldDefaultVersion.getURLs()) {
			boolean currentHasUrl = version.getURLs().contains(url);
			boolean newDefaultHasUrl = newDefaultVersion.getURLs().contains(url);
			if(!newDefaultHasUrl && currentHasUrl) {
				version.getURLs().remove(url);
				diff.add("R " + version.getLib().getName() + ":" + version.getVersion() + ":" + url);
			}
		}
	}
}

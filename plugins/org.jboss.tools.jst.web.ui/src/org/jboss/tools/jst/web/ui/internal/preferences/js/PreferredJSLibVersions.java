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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.jboss.tools.common.xml.XMLUtilities;
import org.jboss.tools.jst.web.html.HTMLConstants;
import org.jboss.tools.jst.web.kb.internal.JQueryMobileRecognizer;
import org.jboss.tools.jst.web.kb.internal.JQueryRecognizer;
import org.jboss.tools.jst.web.kb.internal.JSRecognizer;
import org.jboss.tools.jst.web.kb.internal.taglib.html.jq.JQueryMobileVersion;
import org.jboss.tools.jst.web.ui.WebUiPlugin;
import org.jboss.tools.jst.web.ui.internal.editor.outline.JQueryCategoryFilter;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryConstants;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class PreferredJSLibVersions implements IPreferredJSLibVersion {
	IFile f;
	Object version;

	private Set<String> disabledLibs = new HashSet<String>();
	private Set<String> enabledLibs = new HashSet<String>();

	private Map<String, Boolean> preferredLibs = new HashMap<String, Boolean>();
	private Map<String, String> preferredVersions = new HashMap<String, String>();

	/**
	 * Variable to save/load library version selections in current project.
	 */
	static Map<Object, QualifiedName> PREFERRED_LIBS = new HashMap<Object, QualifiedName>(); 
	
	static QualifiedName getQualifiedName(Object v) {
		QualifiedName result = PREFERRED_LIBS.get(v);
		if(result == null) {
			result = new QualifiedName(WebUiPlugin.PLUGIN_ID, "preferred-js-libs-" + v.toString());
			PREFERRED_LIBS.put(v, result);
		}
		return result;
	}
	abstract static class DefaultJSRecognizer extends JSRecognizer {
		protected static String getJSReferenceVersion(IFile file, String jsLibName) {
			return JSRecognizer.getJSReferenceVersion(file, jsLibName);
		}		
	}

	public PreferredJSLibVersions(IFile file, Object version) {
		f = file;
		this.version = version;
	}

	/**
	 * Returns true, if lib is already implemented in html.
	 * @param libName
	 * @return
	 */
	public boolean isLibDisabled(String libName) {
		return disabledLibs.contains(libName);
	}

	public boolean areAllLibsDisabled() {
		return enabledLibs.isEmpty();
	}

	/**
	 * Returns true if lib is selected.
	 * @param libName
	 * @return
	 */
	public boolean shouldAddLib(String libName) {
		return preferredLibs.containsKey(libName) && preferredLibs.get(libName);
	}

	/**
	 * Returns version selected for the library.
	 * @param libName
	 * @return
	 */
	public String getLibVersion(String libName) {
		return preferredVersions.get(libName);
	}

	/**
	 * For all js libraries in preferences computes
	 * 1. If it is already referenced in html, then it is marked as disabled;
	 * 2. Creates default selection and preferred version.
	 * 3. Loads from project previously saved selection and preferred version, if available.
	 */
	public void updateLibEnablementAndSelection() {
		JSLibModel model = JSLibFactory.getInstance().getPreferenceModel();
		for (String libName: new ArrayList<String>(preferredLibs.keySet())) {
			JSLib lib = model.getLib(libName);
			if(lib == null || lib.getVersions().isEmpty()) {
				enabledLibs.remove(libName);
				disabledLibs.remove(libName);
				preferredLibs.remove(libName);
				preferredVersions.remove(libName);
			}
		}
		for(JSLib lib: model.getLibs()) {
			if(lib.getVersions().isEmpty()) continue;
			String libName = lib.getName();
			boolean enabled = true;
			if(libName.equals(JQueryCategoryFilter.CATEGORY_JQM)) {
				enabled = f == null || !JQueryRecognizer.containsJQueryJSReference(f);
			} else if(libName.equals(JQueryConstants.JQM_CATEGORY)) {
				enabled = f == null || JQueryMobileRecognizer.getVersion(f) == null;
			} else {
				String libNameRoot = getLibNameRoot(lib);
				if(libNameRoot != null) {
					enabled = DefaultJSRecognizer.getJSReferenceVersion(f, libNameRoot) == null;
				}
			}
			if(enabled) {
				enabledLibs.add(libName);
			} else  {
				disabledLibs.add(libName);
			}
		}
		Set<String> availableLibs = new HashSet<String>();
		for(JSLib lib: model.getLibs()) {
			if(lib.getVersions().isEmpty()) continue;
			String libName = lib.getName();
			Boolean current = preferredLibs.get(libName);
			String currentVersion = getLibVersion(libName);
			if(currentVersion != null && lib.getVersion(currentVersion) == null) {
				currentVersion = null;
			}
			String mask = null;
			boolean add = false;
			if(libName.equals(JQueryCategoryFilter.CATEGORY_JQM)) {
				if(version instanceof JQueryMobileVersion) add = true;
				mask = version == JQueryMobileVersion.JQM_1_3 ? "1.9." : "2.0.";
			} else if(libName.equals(JQueryConstants.JQM_CATEGORY)) {
				if(version instanceof JQueryMobileVersion) add = true;
				mask = version == JQueryMobileVersion.JQM_1_3 ? "1.3." : "1.4.";
			}
			if(current == null) {
				preferredLibs.put(libName, add);
			}
			if(currentVersion == null) {
				String lastVersion = getLastVersion(lib, mask);
				if(lastVersion == null) {
					preferredLibs.put(libName, Boolean.FALSE);
					current = null;
					String[] ns = lib.getVersionNames().toArray(new String[0]);
					lastVersion = ns[ns.length - 1];
				}
				preferredVersions.put(libName, lastVersion);
			}
			if(current != null) {
				availableLibs.add(libName);
			}
		}
		String pl = null;
		try {
			if(f != null) {
				pl = f.getProject().getPersistentProperty(getQualifiedName(version));
			}
		} catch (CoreException e) {
			WebUiPlugin.getDefault().logError(e);
		}
		if(pl != null) {
			StringTokenizer st = new StringTokenizer(pl, ";");
			while(st.hasMoreTokens()) {
				String t = st.nextToken();
				StringTokenizer st2 = new StringTokenizer(t, ":");
				if(st2.countTokens() == 3) {
					String name = st2.nextToken();
					if(preferredLibs.containsKey(name) && !availableLibs.contains(name)) {
						boolean add = "true".equals(st2.nextToken());
						String version = st2.nextToken();
						if(model.getLib(name).getVersion(version) != null) {
							preferredVersions.put(name, version);
							preferredLibs.put(name, add);
						} else {
							if(!add) {
								preferredLibs.put(name, add);
							}
						}
					}
				}
			}
		}
	}

	private String getLastVersion(JSLib lib, String mask) {
		String[] ns = lib.getVersionNames().toArray(new String[0]); 
		if(mask != null) {
			for (int i = ns.length - 1; i >= 0; i--) {
				if(ns[i].startsWith(mask)) {
					return ns[i];
				}
			}
		} else {
			return ns[ns.length - 1];
		}
		return null;
	}

	private String getLibNameRoot(JSLib lib) {
		for (JSLibVersion v: lib.getVersions()) {
			for (String u: v.getURLs()) {
				int i = u.lastIndexOf('/') + 1;
				int j = u.indexOf('-', i);
				if(j >= 0) {
					return u.substring(i, j + 1);
				}
			}
		}
		return null;
	}

	/**
	 * Stores library version preferences in maps.
	 */
	public void applyLibPreference(IPreferredJSLibVersion preferredVersions) {
		for(JSLib lib: JSLibFactory.getInstance().getPreferenceModel().getLibs()) {
			if(lib.getVersions().isEmpty()) continue;
			String libName = lib.getName();
			if(!enabledLibs.contains(libName)) continue;
			preferredLibs.put(libName, preferredVersions.shouldAddLib(libName));
			this.preferredVersions.put(libName, preferredVersions.getLibVersion(libName));
		}
	}

	/**
	 * Stores library version preferences in current project.
	 */
	public void saveLibPreference() {
		StringBuffer sb = new StringBuffer();
		for (String libName: preferredLibs.keySet()) {
			String shouldAdd = "" + preferredLibs.get(libName);
			String version = preferredVersions.get(libName);
			sb.append(libName).append(":").append(shouldAdd).append(":").append(version).append(";");
		}
		try {
			f.getProject().setPersistentProperty(getQualifiedName(version), sb.toString());
		} catch (CoreException e) {
			WebUiPlugin.getDefault().logError(e);
		}
	}

	public String[][] getURLs(Node node) {
		Set<String> referencedJS = new TreeSet<String>();
		Set<String> referencedCSS = new TreeSet<String>();
		if(node instanceof Element) {
			Element head = (Element)node;
			for (Element c: XMLUtilities.getChildren(head, HTMLConstants.TAG_LINK)) {
				String href = c.getAttribute(HTMLConstants.ATTR_HREF);
				if(href != null && href.length() > 0) {
					referencedCSS.add(href);
				}
			}
			for (Element c: XMLUtilities.getChildren(head, HTMLConstants.TAG_SCRIPT)) {
				String src = c.getAttribute(HTMLConstants.ATTR_SRC);
				if(src != null && src.length() > 0) {
					referencedJS.add(src);
				}
			}
		}
		JSLibModel model = JSLibFactory.getInstance().getPreferenceModel();
		String[][] result = new String[2][];
		List<String> css = new ArrayList<String>();
		List<String> js = new ArrayList<String>();
		for(JSLib lib: model.getSortedLibs()) {
			String libName = lib.getName();
			if(disabledLibs.contains(libName)) continue;
			if(!preferredLibs.containsKey(libName) || !preferredLibs.get(libName)) continue;
			String preferredVersion = preferredVersions.get(libName);
			JSLibVersion version = lib.getVersion(preferredVersion);
			String[] urls = version.getSortedUrls();
			for (String u: urls) {
				if(version.isCSS(u)) {
					if(referencedCSS.contains(u)) continue;
					//no need to do other checks as the library is enabled.
					css.add(u);
				} else if(version.isJS(u)) {
					if(referencedJS.contains(u)) continue;
					//no need to do other checks as the library is enabled.
					js.add(u);
				}
			}
		}
		result[0] = css.toArray(new String[0]);
		result[1] = js.toArray(new String[0]);
		return result;
	}

}

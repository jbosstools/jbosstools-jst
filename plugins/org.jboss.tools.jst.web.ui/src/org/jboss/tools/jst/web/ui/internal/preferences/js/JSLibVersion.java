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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class JSLibVersion {
	JSLib lib;
	String version;
	List<String> urls = new ArrayList<String>();

	public JSLibVersion(JSLib lib) {
		this.lib = lib;
	}

	/**
	 * Returns the library object that contains this version.
	 * @return
	 */
	public JSLib getLib() {
		return lib;
	}

	/**
	 * 
	 * @return name composed of library name and this version
	 */
	public String getFullName() {
		return getLib().getName() + " " + getVersion();
	}

	/**
	 * 
	 * @return this version name
	 */
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * 
	 * @return list of URL paths
	 */
	public List<String> getURLs() {
		return urls;
	}

	/**
	 * 
	 * @return sorted array of URL paths, first sorted by name .js, then sorted by name .css 
	 */
	public String[] getSortedUrls() {
		if(urls.size() < 2) {
			return urls.toArray(new String[urls.size()]);
		} else {
			Set<String> js = new TreeSet<String>();
			Set<String> css = new TreeSet<String>();
			Set<String> other = new TreeSet<String>();
			for (String u: urls) {
				if(isJS(u)) js.add(u);
				else if(isCSS(u)) css.add(u);
				else other.add(u);
			}
			int jscss = js.size() + css.size();
			String[] result = new String[jscss + other.size()];
			System.arraycopy(js.toArray(new String[js.size()]), 0, result, 0, js.size());
			System.arraycopy(css.toArray(new String[css.size()]), 0, result, js.size(), css.size());
			System.arraycopy(other.toArray(new String[other.size()]), 0, result, jscss, other.size());
			return result; 
		}
	}

	/**
	 * 
	 * @param url
	 * @return if the url is a reference to .js
	 */
	public boolean isJS(String url) {
		return url != null && !isCSS(url);
	}
	
	/**
	 * 
	 * @param url
	 * @return if the url is a reference to .css
	 */
	public boolean isCSS(String url) {
		return url != null && url.endsWith(".css");
	}

	public void applyWorkingCopy(JSLibVersion copy, boolean applyRemoved) {
		version = copy.getVersion();
		if(applyRemoved) {
			urls.clear();
		}
		for (String s: copy.getURLs()) {
			if(!urls.contains(s)) urls.add(s);
		}
	}
	
	public boolean equals(JSLibVersion other) {
		if(!version.equals(other.version) || urls.size() != other.urls.size()) {
			return false;
		}
		HashSet<String> set = new HashSet<String>(urls);
		set.removeAll(other.urls);

		return set.isEmpty();
	}

}

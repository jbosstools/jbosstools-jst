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
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class JSLib {
	String name;
	Map<String,JSLibVersion> versions = new TreeMap<String, JSLibVersion>();

	public JSLib() {
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return name of this JS library
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return collection of versions of this library
	 */
	public Collection<JSLibVersion> getVersions() {
		return versions.values();
	}

	/**
	 * 
	 * @return array of versions of this library sorted by major/minor versions.
	 */
	public JSLibVersion[] getSortedVersions() {
		JSLibVersion[] vs = versions.values().toArray(new JSLibVersion[0]);
		Arrays.sort(vs, versionComparator);
		return vs;
	}

	public Collection<String> getVersionNames() {
		String[] vs = versions.keySet().toArray(new String[0]);
		Arrays.sort(vs, versionStringComparator);
		List<String> result = new ArrayList<String>();
		for (String v: vs) result.add(v);
		return result;
	}

	/**
	 * 
	 * @param version
	 * @return version object for the given name, or null if not available
	 */
	public JSLibVersion getVersion(String version) {
		return versions.get(version);
	}

	/**
	 * Returns version object for the given name, when it is not available, new object is created and added to this library.
	 * 
	 * @param version
	 * @return previously available or new version object for the given name
	 */
	public JSLibVersion getOrCreateVersion(String version) {
		JSLibVersion v = getVersion(version);
		if(version != null) {
			v = new JSLibVersion(this);
			v.setVersion(version);
			addVersion(v);
		}
		return v;
	}

	/**
	 * Adds version object to this library.
	 * @param version
	 */
	public void addVersion(JSLibVersion version) {
		versions.put(version.getVersion(), version);
		version.lib = this;
	}

	/**
	 * Removes version object from this library. Returns true, if the object was actually removed.
	 * @param version
	 * @return
	 */
	public boolean removeVersion(JSLibVersion version) {
		return versions.remove(version.getVersion()) != null;
	}

	/**
	 * Merges differences between this object and its modified copy into this object.
	 * If applyRemoved = false, objects removed from modified copy will not be removed from this library. 
	 * @param copy
	 * @param applyRemoved
	 */
	public void applyWorkingCopy(JSLib copy, boolean applyRemoved) {
		this.name = copy.name;
		if(applyRemoved) {
			Iterator<String> vs = versions.keySet().iterator();
			while(vs.hasNext()) {
				if(copy.getVersion(vs.next()) == null) {
					vs.remove();
				}
			}
		}
		for (JSLibVersion vc: copy.getVersions()) {
			JSLibVersion vo = getVersion(vc.getVersion());
			if(vo == null) {
				addVersion(vc);
			} else {
				vo.applyWorkingCopy(vc, applyRemoved);
			}
		}
	}

	static VersionComparator versionComparator = new VersionComparator();

	static class VersionComparator implements Comparator<JSLibVersion> {

		@Override
		public int compare(JSLibVersion o1, JSLibVersion o2) {
			return versionStringComparator.compare(o1.getVersion(), o2.getVersion());
		}
		
	}

	static VersionStringComparator versionStringComparator = new VersionStringComparator();

	static class VersionStringComparator implements Comparator<String> {

		@Override
		public int compare(String o1, String o2) {
			StringTokenizer s1 = new StringTokenizer(o1, ".");
			StringTokenizer s2 = new StringTokenizer(o2, ".");
			while(s1.hasMoreTokens() && s2.hasMoreTokens()) {
				String t1 = s1.nextToken();
				String t2 = s2.nextToken();
				if(!t1.equals(t2)) {
					if(t1.length() != t2.length()) {
						return t1.length() - t2.length();
					}
					return t1.compareTo(t2);
				}
			}
			return 0;
		}
	
	}
}

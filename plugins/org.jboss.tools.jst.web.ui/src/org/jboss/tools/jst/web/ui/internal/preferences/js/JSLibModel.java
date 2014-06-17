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

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class JSLibModel {
	Map<String, JSLib> libs = new TreeMap<String, JSLib>();

	public JSLibModel() {}

	/**
	 * 
	 * @return collection of JS libraries
	 */
	public Collection<JSLib> getLibs() {
		return libs.values();
	}

	/**
	 * 
	 * @return sorted by name array of JS libraries
	 */
	public JSLib[] getSortedLibs() {
		JSLib[] result = libs.values().toArray(new JSLib[0]);
		Arrays.sort(result, libComparator);
		return result;
	}

	/**
	 * Adds library to the model.
	 * @param lib
	 */
	public void addLib(JSLib lib) {
		libs.put(lib.getName(), lib);
	}

	/**
	 * 
	 * @param name
	 * @return library object for thegiven name, or null if it is not available
	 */
	public JSLib getLib(String name) {
		return libs.get(name);
	}

	/**
	 * Removes library from the model. Returns true if it was really removed.
	 * @param lib
	 * @return
	 */
	public boolean removeLib(JSLib lib) {
		return libs.remove(lib.getName()) != null;
	}

	/**
	 * Returns library object for the given name, when it is not available, new object is created and added to this model.
	 * @param name
	 * @return
	 */
	public JSLib getOrCreateLib(String name) {
		JSLib lib = getLib(name);
		if(lib == null) {
			lib = new JSLib();
			lib.setName(name);
			addLib(lib);
		}
		return lib;
	}

	/**
	 * Merges differences between this object and its modified copy into this object.
	 * If applyRemoved = false, objects removed from modified copy will not be removed from this model. 
	 * @param copy
	 * @param applyRemoved
	 */
	public void applyWorkingCopy(JSLibModel copy, boolean applyRemoved) {
		if(applyRemoved) {
			Iterator<String> vs = libs.keySet().iterator();
			while(vs.hasNext()) {
				if(copy.getLib(vs.next()) == null) {
					vs.remove();
				}
			}
		}
		for (JSLib vc: copy.getLibs()) {
			JSLib vo = getLib(vc.getName());
			if(vo == null) {
				addLib(vc);
			} else {
				vo.applyWorkingCopy(vc, applyRemoved);
			}
		}
	}

	static LibComparator libComparator = new LibComparator();

	static class LibComparator implements Comparator<JSLib> {

		@Override
		public int compare(JSLib o1, JSLib o2) {
			return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
		}
		
	}
}

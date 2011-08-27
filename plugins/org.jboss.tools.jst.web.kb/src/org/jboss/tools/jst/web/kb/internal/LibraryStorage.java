/******************************************************************************* 
 * Copyright (c) 2009 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.internal;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.jboss.tools.jst.web.kb.taglib.ITagLibrary;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class LibraryStorage {

	private static final Set<ITagLibrary> EMPTY_SET = Collections.emptySet();
	private static final ITagLibrary[] EMPTY_LIB_ARRAY = new ITagLibrary[0];
	
	private Set<ITagLibrary> allLibraries = new HashSet<ITagLibrary>();
	private ITagLibrary[] allLibrariesArray = EMPTY_LIB_ARRAY;
	private Map<IPath, Set<ITagLibrary>> librariesBySource = new HashMap<IPath, Set<ITagLibrary>>();
	private Map<String, Set<ITagLibrary>> librariesByUri = new HashMap<String, Set<ITagLibrary>>();
	private Map<String,ITagLibrary[]> librariesByUriArray = new HashMap<String, ITagLibrary[]>();

	public synchronized void clear() {
		allLibraries.clear();
		allLibrariesArray = EMPTY_LIB_ARRAY;
		librariesBySource.clear();
		librariesByUri.clear();
		librariesByUriArray.clear();
	}

	public synchronized ITagLibrary[] getAllLibrariesArray() {
		if(allLibrariesArray.length == 0) {
			allLibrariesArray = allLibraries.toArray(new ITagLibrary[allLibraries.size()]);
		}
		return allLibrariesArray;
	}

	public synchronized ITagLibrary[] getLibrariesArray(String uri) {
		ITagLibrary[] result = librariesByUriArray.get(uri);
		if(result == null) {
			Set<ITagLibrary> libs = librariesByUri.get(uri);
			if(libs!=null) {
				result = libs.toArray(new ITagLibrary[libs.size()]);
			} else {
				result = EMPTY_LIB_ARRAY; 
			}
			librariesByUriArray.put(uri, result);
		}
		return result;
	}

	public synchronized Set<ITagLibrary> getLibrariesBySource(IPath path) {
		Set<ITagLibrary> set = librariesBySource.get(path);
		return set != null ? set : EMPTY_SET;
	}

	public synchronized ITagLibrary[] getLibrariesArray(IPath path) {
		ITagLibrary[] result = EMPTY_LIB_ARRAY;
		Set<ITagLibrary> libs = librariesBySource.get(path);
		if(libs!=null) {
			result = libs.toArray(new ITagLibrary[0]);
		}
		return result;
	}

	public synchronized void addLibrary(ITagLibrary f) {
		allLibraries.add(f);
		allLibrariesArray = EMPTY_LIB_ARRAY;
		IPath path = f.getSourcePath();
		if(path != null) {
			Set<ITagLibrary> fs = librariesBySource.get(path);
			if(fs == null) {
				fs = new HashSet<ITagLibrary>();
				librariesBySource.put(path, fs);
			}
			fs.add(f);
		}
		String uri = f.getURI();
		librariesByUriArray.remove(uri);
		Set<ITagLibrary> ul = librariesByUri.get(uri);
		if (ul == null) {
			ul = new HashSet<ITagLibrary>();
			librariesByUri.put(uri, ul);
		}
		ul.add(f);
	}

	public synchronized void removeLibrary(ITagLibrary f) {
		allLibraries.remove(f);
		allLibrariesArray = EMPTY_LIB_ARRAY;
		IPath path = f.getSourcePath();
		if(path != null) {
			Set<ITagLibrary> fs = librariesBySource.get(path);
			if(fs != null) {
				fs.remove(f);
			}
			if(fs.isEmpty()) {
				librariesBySource.remove(path);
			}
		}
		String uri = f.getURI();
		Set<ITagLibrary> ul = librariesByUri.get(uri);
		librariesByUriArray.remove(uri);
		if (ul != null) {
			ul.remove(f);
			if (ul.isEmpty()) {
				librariesByUri.remove(uri);
			}
		}
	}

	public synchronized Set<ITagLibrary> removePath(IPath path) {
		Set<ITagLibrary> fs = librariesBySource.get(path);
		if(fs == null) {
			fs = EMPTY_SET;
		} else {
			for (ITagLibrary f: fs) {
				allLibraries.remove(f);
				allLibrariesArray = EMPTY_LIB_ARRAY;
				Set<ITagLibrary> s = librariesByUri.get(f.getURI());
				if(s != null) {
					s.remove(f);
					if(s.isEmpty()) {
						librariesByUri.remove(f.getURI());
					}
				}
				librariesByUriArray.remove(f.getURI());
			}
			librariesBySource.remove(path);
		}
		return fs;
	}

}

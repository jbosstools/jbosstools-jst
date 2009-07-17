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
package org.jboss.tools.jst.web.project.helpers;

import java.io.File;
import java.util.*;

import org.eclipse.osgi.util.NLS;

import org.jboss.tools.common.util.FileUtil;
import org.jboss.tools.jst.web.WebModelPlugin;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;
import org.jboss.tools.jst.web.project.handlers.*;

/**
 * @author glory
 */
public class LibrarySets {
	static String[] installBundles = new String[]{
		"org.jboss.tools.jsf", //$NON-NLS-1$
		"org.jboss.tools.struts", //$NON-NLS-1$
		"org.jboss.tools.common.model" //$NON-NLS-1$
	};
		
	static LibrarySets instance = new LibrarySets();
	String libPath;
	Map<String,LibrarySet> librarySets = new TreeMap<String,LibrarySet>();
	
	LibrarySets() {
		loadInstallPath();
		refresh();
	}
	
	public static LibrarySets getInstance() {
		return instance;
	}
	
	public String getLibrarySetsPath() {
		return libPath;
	}
	
	private void loadInstallPath() {
		libPath = WebModelPlugin.getTemplateStateLocation() + "lib"; //$NON-NLS-1$
	}
	
	public void refresh() {
		File[] fs = new File(libPath).listFiles();
		if(fs == null) {
			librarySets.clear();
			return;
		}
		Map<String,String> fm = new HashMap<String,String>();
		for (int i = 0; i < fs.length; i++) {
			if(fs[i].isDirectory() 
				&& !"CVS".equalsIgnoreCase(fs[i].getName()) //$NON-NLS-1$
				&& !".svn".equalsIgnoreCase(fs[i].getName())) { //$NON-NLS-1$
				fm.put(fs[i].getName(), fs[i].getAbsolutePath());
			}
		}
		Iterator<String> it = librarySets.keySet().iterator();
		while(it.hasNext()) {
			String name = it.next();
			if(!fm.containsKey(name)) {
				librarySets.remove(name);
			}
		}
		it = fm.keySet().iterator();
		while(it.hasNext()) {
			String name = it.next();
			LibrarySet s = getLibrarySet(name);
			if(s == null) {
				s = new LibrarySet(name, fm.get(name).toString());
				librarySets.put(name, s);
			}
			s.refresh();
		}		
	}
	
	public String[] getLibrarySetList() {
		return librarySets.keySet().toArray(new String[0]);		
	}
	
	public LibrarySet getLibrarySet(String name) {
		return librarySets.get(name);
	}
	
	public String addLibrarySet() {
		return AddLibrarySetSupport.run();
	}
	
	public void addLibrarySet(String name) {
		if(getLibrarySet(name) != null) return;
		String path = getLibrarySetsPath() + "/" + name; //$NON-NLS-1$
		File d = new File(path);
		d.mkdirs();
		if(d.isDirectory()) {
			LibrarySet s = new LibrarySet(name, path);
			s.refresh();
			librarySets.put(name, s);
		}		
	}
	
	public boolean removeLibrarySet(String name) {
		LibrarySet s = getLibrarySet(name);
		if(s == null) return false;
		if(!AbstractWebProjectTemplate.confirm(NLS.bind(WebUIMessages.YOU_WANT_TO_DELETE_LIBRARYSET, name))) return false; 
		String path = s.getPath();
		FileUtil.remove(new File(path));
		librarySets.remove(name);
		return true;		
	}

}

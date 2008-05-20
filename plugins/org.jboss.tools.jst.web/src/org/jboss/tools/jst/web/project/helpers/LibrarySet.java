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

import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.options.*;
import org.jboss.tools.common.util.FileUtil;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;
import org.jboss.tools.jst.web.project.handlers.AddJarToLibrarySetSupport;

public class LibrarySet {
	String name;
	String path;
	Set<String> jars = new TreeSet<String>();
	
	LibrarySet(String name, String path) {
		this.name = name;
		this.path = path;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPath() {
		return path;
	}
	
	public void refresh() {
		jars.clear();
		File f = new File(path);
		if(!f.isDirectory()) return;
		File[] fs = f.listFiles();
		if(fs == null) return;
		for (int i = 0; i < fs.length; i++) {
			if(fs[i].isFile() && fs[i].getName().endsWith(".jar")) { //$NON-NLS-1$
				jars.add(fs[i].getName());
			}
		}
	}
	
	public String[] getJarList() {
		return (String[])jars.toArray(new String[0]);
	}
	
	public String addJar() {
		return AddJarToLibrarySetSupport.run(this);
	}
	
	public String addJar(String location) {
		ServiceDialog d = PreferenceModelUtilities.getPreferenceModel().getService();
		File f = new File(location);
		if(!f.exists()) {
			d.showDialog(WebUIMessages.ERROR, NLS.bind(WebUIMessages.FILE_DOESNOT_EXIST, location), new String[]{WebUIMessages.CLOSE}, null, ServiceDialog.ERROR); 
		}
		String jarname = f.getName();
		if(jars.contains(jarname)) {
			int q = d.showDialog(WebUIMessages.WARNING, NLS.bind(WebUIMessages.LIBRARYSET_CONTAINS, jarname), new String[]{WebUIMessages.OK, WebUIMessages.CANCEL}, null, ServiceDialog.WARNING);
			if(q != 0) return null;
		}
		File jf = getFile(jarname);
		FileUtil.copyFile(f, jf, true, true);
		if(jf.isFile()) jars.add(jarname);
		return jarname;
	}
	
	public boolean removeJar(String jarname) {
		File jf = getFile(jarname);
		if(jf.isFile()) {
			if(!AbstractWebProjectTemplate.confirm(NLS.bind(WebUIMessages.YOU_WANT_TO_DELETE_FROM_LIBRARYSET,jarname, name))) return false;
			jf.delete();
		}
		if(!jf.exists()) {
			jars.remove(jarname);
			return true;
		}
		return false;
	}
	
	File getFile(String jarname) {
		return new File(path + "/" + jarname); //$NON-NLS-1$
	}	
	
}

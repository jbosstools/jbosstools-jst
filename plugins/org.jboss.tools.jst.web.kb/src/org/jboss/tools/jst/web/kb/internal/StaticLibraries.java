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


import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Path;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.web.kb.internal.scanner.LoadedDeclarations;
import org.jboss.tools.jst.web.kb.internal.scanner.XMLScanner;
import org.jboss.tools.jst.web.kb.taglib.ITagLibrary;
import org.jboss.tools.jst.web.kb.taglib.TagLibraryManager;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class StaticLibraries {
	static StaticLibraries instance = new StaticLibraries();

	LibraryStorage libraries = new LibraryStorage();
	Map<File, XModelObject> loadedFolders = new HashMap<File, XModelObject>();
	Map<File, XModelObject> loadedFiles = new HashMap<File, XModelObject>();
	
	private StaticLibraries() {}

	public ITagLibrary[] getLibraries(String uri) {
		File file = TagLibraryManager.getStaticTLD(uri);
		if(file == null) return new ITagLibrary[0];
		if(loadedFiles.containsKey(file)) {
			return libraries.getLibrariesArray(uri);
		}
		File folder = file.getParentFile();
		if(!loadedFolders.containsKey(folder)) {
			XModelObject o = EclipseResourceUtil.createObjectForLocation(file.getAbsolutePath());
			if(o != null) {
				loadedFolders.put(folder, o.getParent());
			}			
		}
		XModelObject o = loadedFolders.get(folder);
		if(o != null) {
			XModelObject fo = o.getChildByPath(file.getName());
			if (fo != null) {
				loadedFiles.put(file, fo);
				XMLScanner scanner = new XMLScanner();
				LoadedDeclarations ds = scanner.parse(fo, new Path(file.getAbsolutePath()), null);
				List<ITagLibrary> ls = ds.getLibraries();
				for (ITagLibrary l : ls) {
					libraries.addLibrary(l);
				}
			}
		}
		
		return libraries.getLibrariesArray(uri);
	}

}

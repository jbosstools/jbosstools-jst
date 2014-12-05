/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.internal.editor.jspeditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.jboss.tools.common.model.ui.views.palette.PaletteContents;
import org.jboss.tools.jst.web.kb.internal.JQueryMobileRecognizer;
import org.jboss.tools.jst.web.kb.taglib.IHTMLLibraryVersion;
import org.jboss.tools.jst.web.kb.taglib.ITagLibRecognizer;
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteGroup;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.PaletteModelImpl;
import org.jboss.tools.jst.web.ui.palette.model.PaletteModel;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class PagePaletteContents extends PaletteContents {
	IEditorPart editorPart;
	Map<String, CategoryVersion> categoryVersions = new HashMap<String, CategoryVersion>();
	Set<String> recognizedCategories = new HashSet<String>();

	public PagePaletteContents(IEditorPart editorPart) {
		super(editorPart);
		this.editorPart = editorPart;

		categoryVersions.put("jQuery Mobile", new CategoryVersion(new JQueryVersionComputer()));

		computeVersions();
		computeRecognized();
	}

	public IEditorPart getEditorPart() {
		return editorPart;
	}

	public IFile getFile() {
		if(editorPart != null) {
			IEditorInput input = editorPart.getEditorInput();
			if (input instanceof IFileEditorInput) {
				return ((IFileEditorInput)input).getFile();
			}
		}
		return null;
	}

	public boolean update() {
		boolean r = super.update();
		if(computeVersions()) {
			computeRecognized();
			r = true;
		}		
		return r;
	}

	public void dispose() {
		PaletteModel.disposeInstance(this);
		editorPart = null;
	}

	boolean computeVersions() {
		boolean result = false;
		IFile file = getFile();
		String[] types = getNatureTypes();
		if(types.length > 0 && PaletteModel.TYPE_HTML5.equals(types[0])) {
			if(file != null) {
				for (String categoryName: categoryVersions.keySet()) {
					CategoryVersion v = categoryVersions.get(categoryName);
					if(v.computeVersion()) {
						result = true;
					}
				}
			}
		}
		
		return result;
	}

	boolean computeRecognized() {
		boolean result = false;
		IFile file = getFile();
		String[] types = getNatureTypes();
		if(types.length > 0 && PaletteModel.TYPE_HTML5.equals(types[0])) {
			if(file != null) {
				Set<String> rc = new HashSet<String>();
				Map<String, ITagLibRecognizer> rs = getTaglibRecognizers();
				for (Map.Entry<String, ITagLibRecognizer> e: rs.entrySet()) {
					String name = e.getKey();
					ITagLibRecognizer r = e.getValue();
					if(r.isUsed(null, file)) {
						rc.add(name);
					}
				}
				if(!setsAreEqual(recognizedCategories, rc)) {
					recognizedCategories = rc;
					result = true;
				}
			}
		}
		
		return result;
	}

	boolean mapsAreEqual(Map<String, String> map1, Map<String, String> map2) {
		if(map1.size() != map2.size()) {
			return false;
		}
		for (String k: map2.keySet()) {
			if(!map1.containsKey(k)) {
				return false;
			}
			if(!map1.get(k).equals(map2.get(k))) {
				return false;
			}
		}
		return true;
	}

	boolean setsAreEqual(Set<String> map1, Set<String> map2) {
		if(map1.size() != map2.size()) {
			return false;
		}
		for (String k: map2) {
			if(!map1.contains(k)) {
				return false;
			}
		}
		return true;
	}

	public IHTMLLibraryVersion getVersion(String category) {
		CategoryVersion v = categoryVersions.get(category);
		return v == null ? null : v.getVersion();
	}

	public boolean isRecognized(String category) {
		return recognizedCategories.contains(category);
	}

	public void setPreferredVersion(String category, IHTMLLibraryVersion version) {
		CategoryVersion v = categoryVersions.get(category);
		if(v != null && v.preferredVersion != version) {
			v.preferredVersion = version;
		}
	}

	class CategoryVersion {
		VersionComputer versionComputer;
		IHTMLLibraryVersion detectedVersion = null;
		IHTMLLibraryVersion preferredVersion = null;

		public CategoryVersion(VersionComputer versionComputer) {
			this.versionComputer = versionComputer;
		}

		public IHTMLLibraryVersion getVersion() {
			return(preferredVersion != null) ? preferredVersion : detectedVersion;
		}

		public boolean computeVersion() {
			IHTMLLibraryVersion newVersion = versionComputer.computeVersion(getFile());
			boolean isNew = !isSame(newVersion);
			if(isNew) {
				preferredVersion = null;
				detectedVersion = newVersion;
			}
			return isNew;
		}

		private boolean isSame(IHTMLLibraryVersion newVersion) {
			return (detectedVersion == null) ? newVersion == null : detectedVersion.equals(newVersion);
		}		 
	}
	static Map<String, ITagLibRecognizer> taglibRecognizers = null;

	static Map<String, ITagLibRecognizer> getTaglibRecognizers() {
		if(taglibRecognizers == null) {
			ArrayList<IPaletteGroup> paletteGroups = PaletteModelImpl.loadPaletteGroups();
			Map<String, ITagLibRecognizer> tr = new HashMap<String, ITagLibRecognizer>();
			for(IPaletteGroup group : paletteGroups){
				ITagLibRecognizer recognizer = group.getRecognizer();
				if(recognizer != null){
					tr.put(group.getName(), recognizer);
				}
			}
		    taglibRecognizers = tr;
		}
		return taglibRecognizers;
	}
}


interface VersionComputer {
	public IHTMLLibraryVersion computeVersion(IFile file);
}

class JQueryVersionComputer implements VersionComputer {
	static String prefix = "jquery.mobile-";

	@Override
	public IHTMLLibraryVersion computeVersion(IFile file) {
		return JQueryMobileRecognizer.getVersion(file);
	}
}
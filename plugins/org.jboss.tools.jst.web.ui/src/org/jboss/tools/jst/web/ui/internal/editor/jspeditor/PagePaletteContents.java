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
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.jboss.tools.common.model.ui.views.palette.PaletteContents;
import org.jboss.tools.jst.web.kb.PageContextFactory;
import org.jboss.tools.jst.web.kb.taglib.IHTMLLibraryVersion;
import org.jboss.tools.jst.web.kb.taglib.ITagLibRecognizer;
import org.jboss.tools.jst.web.kb.taglib.ITagLibVersionRecognizer;
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

	public PagePaletteContents(IEditorPart editorPart) {
		super(editorPart);
		this.editorPart = editorPart;

		getTaglibRecognizers();
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
				for (CategoryVersion v: getVersionObjects()) {
					if(v.computeVersion(file)) {
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
				for (CategoryVersion v: getVersionObjects()) {
					if(v.computeRecognized(file)) {
						result = true;
					}
				}
			}
		}
		
		return result;
	}

	public IHTMLLibraryVersion getVersion(String category) {
		CategoryVersion v = getVersionObject(category);
		return v == null ? null : v.getVersion();
	}

	public boolean isRecognized(String category) {
		CategoryVersion v = getVersionObject(category);
		return v != null && v.recognized;
	}

	private synchronized CategoryVersion[] getVersionObjects() {
		return categoryVersions.values().toArray(new CategoryVersion[0]);
	}

	private CategoryVersion getVersionObject(String category) {
		CategoryVersion v = null;
		synchronized(this) {
			v = categoryVersions.get(category);
		}
		if(v == null) {
			ITagLibRecognizer r = taglibRecognizers.get(category);
			if(r instanceof ITagLibVersionRecognizer) {
				v = new CategoryVersion((ITagLibVersionRecognizer)r);
				synchronized(this) {
					categoryVersions.put(category, v);
				}
				IFile file = getFile();
				if(file != null) {
					v.computeVersion(file);
					v.computeRecognized(file);
				}
			}
		}
		return v;
	}

	public void setPreferredVersion(String category, IHTMLLibraryVersion version) {
		CategoryVersion v = categoryVersions.get(category);
		if(v != null && v.preferredVersion != version) {
			v.preferredVersion = version;
		}
	}

	class CategoryVersion {
		ITagLibVersionRecognizer recognizer;
		IHTMLLibraryVersion detectedVersion = null;
		IHTMLLibraryVersion preferredVersion = null;
		boolean recognized = false;

		public CategoryVersion(ITagLibVersionRecognizer recognizer) {
			this.recognizer = recognizer;
		}

		public IHTMLLibraryVersion getVersion() {
			return(preferredVersion != null) ? preferredVersion : detectedVersion;
		}

		public boolean computeVersion(IFile file) {
			IHTMLLibraryVersion newVersion = recognizer.getVersion(PageContextFactory.createPageContext(file));
			boolean isNew = !isSame(newVersion);
			if(isNew) {
				preferredVersion = null;
				detectedVersion = newVersion;
			}
			return isNew;
		}
	
		public boolean computeRecognized(IFile file) {
			if(recognized != recognizer.isUsed(file)) {
				recognized = !recognized;
				return true;
			}
			return false;
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

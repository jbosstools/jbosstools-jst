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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.jboss.tools.common.model.ui.views.palette.PaletteContents;
import org.jboss.tools.jst.web.kb.internal.JQueryMobileRecognizer;
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

		categoryVersions.put("jQuery Mobile", new CategoryVersion(new JQueryVersionComputer()));

		computeVersions();
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
		if(types.length > 0 && TYPE_MOBILE.equals(types[0])) {
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

	public String getVersion(String category) {
		CategoryVersion v = categoryVersions.get(category);
		return v == null ? null : v.getVersion();
	}

	public void setPreferredVersion(String category, String version) {
		CategoryVersion v = categoryVersions.get(category);
		if(v != null && v.preferredVersion != version) {
			v.preferredVersion = version;
		}
	}

	class CategoryVersion {
		VersionComputer versionComputer;
		String detectedVersion = null;
		String preferredVersion = null;

		public CategoryVersion(VersionComputer versionComputer) {
			this.versionComputer = versionComputer;
		}

		public String getVersion() {
			return(preferredVersion != null) ? preferredVersion : detectedVersion;
		}

		public boolean computeVersion() {
			String newVersion = versionComputer.computeVersion(getFile());
			boolean isNew = !isSame(newVersion);
			if(isNew) {
				preferredVersion = null;
				detectedVersion = newVersion;
			}
			return isNew;
		}

		private boolean isSame(String newVersion) {
			return (detectedVersion == null) ? newVersion == null : detectedVersion.equals(newVersion);
		}		 
	}

}

interface VersionComputer {
	public String computeVersion(IFile file);
}

class JQueryVersionComputer implements VersionComputer {
	static String prefix = "jquery.mobile-";

	@Override
	public String computeVersion(IFile file) {
		return JQueryMobileRecognizer.getVersion(file);
//		String content = FileUtil.getContentFromEditorOrFile(file);
//		int i = 0;
//		while(i >= 0) {
//			int j = content.indexOf(prefix, i);
//			if(j < 0 || content.length() < j + prefix.length() + 3) {
//				return null;
//			}
//			String delta = content.substring(i, j);
//			int k = delta.lastIndexOf("<");
//			if(k >= 0 && k + 6 < delta.length() && delta.substring(k, k + 7).toLowerCase().equals("<script")) {
//				int u = delta.indexOf(">", k);
//				if(u < 0) {
//					i = j + prefix.length();
//					return content.substring(i, i + 3);
//				}
//			}
//			
//			i = j + prefix.length();
//		}
//		
//		return null;
	}
}
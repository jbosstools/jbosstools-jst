package org.jboss.tools.jst.web.kb.internal.taglib.html;

import org.eclipse.core.resources.IFile;

/**
n * List of HTML versions supported by Palette.
 * When adding a new version, we should create palette subcategory 
 * under "HTML" with name of that version, register in plugin.xml wizards 
 *    for its items, etc.

 * @author slava
 *
 */
public enum HTMLVersion implements IHTMLLibraryVersion {
	HTML_5_0("5.0");

	String version;

	HTMLVersion(String version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return version;
	}

	@Override
	public boolean isPreferredJSLib(IFile file, String libName) {
		return false;
	}

	@Override
	public boolean isReferencingJSLib(IFile file, String libName) {
		return false;
	}

}

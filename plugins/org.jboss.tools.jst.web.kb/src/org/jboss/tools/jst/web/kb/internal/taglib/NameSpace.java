/*******************************************************************************
 * Copyright (c) 2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.jst.web.kb.internal.taglib;

import org.jboss.tools.jst.web.kb.taglib.INameSpace;
import org.jboss.tools.jst.web.kb.taglib.INameSpaceExtended;
import org.jboss.tools.jst.web.kb.taglib.ITagLibrary;

/**
 * The Namespace and TagLibrary holder object
 * 
 * @author Victor Rubezhny
 *
 */
public class NameSpace implements INameSpaceExtended {
	private String fPrefix;
	private String fUri;
	private ITagLibrary[] fTagLibraries;
	
	/**
	 * Constructs NameSpace object 
	 * 
	 * @param uri
	 * @param prefix
	 */
	public NameSpace(String uri, String prefix) {
		this(uri, prefix, null);
	}

	/**
	 * Constructs NameSpace object 
	 * 
	 * @param uri
	 * @param prefix
	 */
	public NameSpace(String uri, String prefix, ITagLibrary[] libraries) {
		this.fUri = uri;
		this.fPrefix = prefix;
		this.fTagLibraries = libraries;
	}
	
	/**
	 * Returns prefix for the Namespace object
	 */
	public String getPrefix() {
		return fPrefix;
	}

	/**
	 * Returns URI for the Namespace object
	 */
	public String getURI() {
		return fUri;
	}

	/**
	 * Returns TagLibrary for the Namespace object
	 * 
	 * @return
	 */
	public ITagLibrary[] getTagLibraries() {
		return fTagLibraries;
	}
	
	/**
	 * Compares the NameSpace objects
	 * The method doesn't take in account the library
	 *
	 * @return
	 */
	public boolean equals(Object obj) {
		if (obj instanceof INameSpace) {
			INameSpace objNs = (INameSpace)obj;
			
			boolean result = (fPrefix == null && objNs.getPrefix() == null) ||
				(fPrefix != null && fPrefix.equals(objNs.getPrefix()));
			
			if (!result)
				return false;
			
			return (fUri == null && objNs.getURI() == null) || 
				(fUri != null && fUri.equals(objNs.getURI()));
		}
		return false;
	}
}
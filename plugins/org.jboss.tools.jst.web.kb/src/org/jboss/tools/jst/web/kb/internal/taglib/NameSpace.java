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

/**
 * The Namespace holder object
 * 
 * @author Victor Rubezhny
 *
 */
public class NameSpace implements INameSpace {
	private String fPrefix;
	private String fUri;
	
	/**
	 * Constructs NameSpace object 
	 * 
	 * @param uri
	 * @param prefix
	 */
	public NameSpace(String uri, String prefix) {
		this.fUri = uri;
		this.fPrefix = prefix;
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
	 * Compares the NameSpace objects
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

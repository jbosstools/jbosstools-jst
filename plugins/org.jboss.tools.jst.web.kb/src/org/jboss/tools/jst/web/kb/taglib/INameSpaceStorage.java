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
package org.jboss.tools.jst.web.kb.taglib;

import java.util.Set;

/**
 * 
 * Keeps data from xmlns:* attributes.
 * 
 * @author Viacheslav Kabanovich
 *
 */
public interface INameSpaceStorage {
	
	/**
	 * Adds to the storage data from xmlns:%prefix%="%url%" attribute on a page.
	 * 
	 * @param prefix
	 * @param url
	 */
	public void add(String prefix, String uri);
	
	/**
	 * Returns all uris declared with given prefix on pages in the current project.
	 * 
	 * @param prefix
	 * @return
	 */
	public Set<String> getURIs(String prefix);

	/**
	 * Returns all prefixes, starting with prefixMask, 
	 * declared on pages in the current project.
	 * 
	 * @param prefixMask
	 * @return
	 */
	public Set<String> getPrefixes(String prefixMask);
}

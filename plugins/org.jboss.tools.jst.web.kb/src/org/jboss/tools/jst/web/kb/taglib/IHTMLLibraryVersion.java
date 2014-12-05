/******************************************************************************* 
 * Copyright (c) 2014 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.taglib;

import org.eclipse.core.resources.IFile;

/**
 * 
 * @author Viacheslav Kabanocich
 *
 */
public interface IHTMLLibraryVersion {

	/**
	 * Preferred library is by default is selected in Tag Insert wizard
	 * for components of this library version.
	 * 
	 * @param category
	 * @return
	 */
	public boolean isPreferredJSLib(IFile file, String name);

	/**
	 * Returns true if file is already referencing this library.
	 * 
	 * @param file
	 * @param name
	 * @return
	 */
	public boolean isReferencingJSLib(IFile file, String name);

}

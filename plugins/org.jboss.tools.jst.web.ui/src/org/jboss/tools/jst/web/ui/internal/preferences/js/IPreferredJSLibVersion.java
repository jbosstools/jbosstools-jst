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
package org.jboss.tools.jst.web.ui.internal.preferences.js;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public interface IPreferredJSLibVersion {

	/**
	 * Returns true if lib is selected.
	 * @param libName
	 * @return
	 */
	public boolean shouldAddLib(String libName);

	/**
	 * Returns version selected for the library.
	 * @param libName
	 * @return
	 */
	public String getLibVersion(String libName);
}

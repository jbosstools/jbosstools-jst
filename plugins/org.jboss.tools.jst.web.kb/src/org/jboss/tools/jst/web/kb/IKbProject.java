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
package org.jboss.tools.jst.web.kb;

import org.eclipse.core.resources.IProjectNature;
import org.jboss.tools.jst.web.kb.taglib.ITagLibrary;
import org.jboss.tools.jst.web.kb.validation.IValidationContext;

/**
 * 
 * @author V.Kabanovich
 *
 */
public interface IKbProject extends IProjectNature {

	public static String NATURE_ID = WebKbPlugin.PLUGIN_ID + ".kbnature"; //$NON-NLS-1$

	/**
	 * Returns all available tag libraries.
	 * @return
	 */
	ITagLibrary[] getTagLibraries();

	/**
	 * Returns tag libraries by URI
	 * @param uri
	 * @return
	 */
	ITagLibrary[] getTagLibraries(String uri);

	void resolve();

	/**
	 * @return validation context which is associated with this KB project.
	 */
	IValidationContext getValidationContext();
}
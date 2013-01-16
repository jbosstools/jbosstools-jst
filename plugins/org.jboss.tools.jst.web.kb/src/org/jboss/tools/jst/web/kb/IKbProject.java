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

import java.util.List;

import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.IPath;
import org.jboss.tools.common.validation.IProjectValidationContext;
import org.jboss.tools.jst.web.kb.include.IIncludeModel;
import org.jboss.tools.jst.web.kb.taglib.INameSpaceStorage;
import org.jboss.tools.jst.web.kb.taglib.ITagLibrary;

/**
 * 
 * @author V.Kabanovich
 *
 */
public interface IKbProject extends IProjectNature {

	public static String NATURE_ID = WebKbPlugin.PLUGIN_ID + ".kbnature"; //$NON-NLS-1$

	/**
	 * Returns all available tag libraries of the project.
	 * Doesn't returns libraries from XML Catalog.
	 * The same as getTagLibraries(false);
	 * @return
	 */
	ITagLibrary[] getProjectTagLibraries();

	/**
	 * Returns all available tag libraries including static libraries from XML Catalog.
	 * @return
	 */
	List<ITagLibrary> getAllTagLibraries();

	/**
	 * Returns tag libraries by URI
	 * @param uri
	 * @return
	 */
	ITagLibrary[] getTagLibraries(String uri);

	/**
	 * Returns tag libraries by resource
	 * @param uri
	 * @return
	 */
	ITagLibrary[] getTagLibraries(IPath path);

	void resolve();

	/**
	 * @return validation context which is associated with this KB project.
	 */
	IProjectValidationContext getValidationContext();

	/**
	 * @return model object that collects <ui:include> elements from pages
	 */
	IIncludeModel getIncludeModel();
	
	/**
	 * 
	 * @return model object that collects data from xmlns:* attributes of pages.
	 */
	INameSpaceStorage getNameSpaceStorage();
}
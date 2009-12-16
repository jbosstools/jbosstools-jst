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
package org.jboss.tools.jst.web.kb.taglib;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.IProposalProcessor;
import org.jboss.tools.jst.web.kb.KbQuery;

/**
 * Represents a tag library.
 * @author Alexey Kazakov
 */
public interface ITagLibrary extends IProposalProcessor {

	/**
	 * @return source path
	 */
	public IPath getSourcePath();

	/**
	 * @return name space
	 */
	INameSpace getDefaultNameSpace();

	/**
	 * @return URI of the tag lib.
	 */
	String getURI();

	/**
	 * @return version of the tag lib.
	 */
	String getVersion();

	/**
	 * @return resource of this tag lib.
	 */
	IResource getResource();

	/**
	 * @return all tags
	 */
	IComponent[] getComponents();

	/**
	 * @param nameTemplate
	 * @return tags with names which start with given template
	 */
	IComponent[] getComponents(String nameTemplate);

	/**
	 * @param name
	 * @return tag by name
	 */
	IComponent getComponent(String name);

	/**
	 * @param type
	 * @return component by type
	 */
	IComponent getComponentByType(String type);

	/**
	 * @param query
	 * @param context
	 * @return components
	 */
	public IComponent[] getComponents(KbQuery query, IPageContext context);

	/**
	 * Clone the lib
	 * @return
	 * @throws CloneNotSupportedException
	 */
	public ITagLibrary clone() throws CloneNotSupportedException;
}
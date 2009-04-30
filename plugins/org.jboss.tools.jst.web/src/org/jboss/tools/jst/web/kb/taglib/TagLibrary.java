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

/**
 * Represents a tag library.
 * @author Alexey Kazakov
 */
public interface TagLibrary {

	/**
	 * @return URI of the tag lib.
	 */
	String getURI();

	/**
	 * @return resource of this tag lib.
	 */
	IResource getResource();

	/**
	 * @return all tags
	 */
	Component[] getAllComponents();

	/**
	 * @param nameTemplate
	 * @return tags with names which start with given template
	 */
	Component[] getComponents(String nameTemplate);

	/**
	 * @param name
	 * @return tag by name
	 */
	Component getComponent(String name);
}
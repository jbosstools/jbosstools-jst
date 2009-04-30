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

/**
 * @author Alexey Kazakov
 */
public interface Component {

	/**
	 * @return name space
	 */
	NameSpace getNameSpace();

	/**
	 * @return component name
	 */
	String getName();

	/**
	 * @return description
	 */
	String getDesription();

	/**
	 * @return true if the tag can have a body
	 */
	boolean canHaveBody();

	/**
	 * @return the component type
	 */
	String getComponentType();

	/**
	 * @return the component class name
	 */
	String getComponentClass();

	/**
	 * @return all attributes of this component
	 */
	Attribute[] getAttributes();

	/**
	 * @param nameTemplate
	 * @return attributes with names which start with given template.
	 */
	Attribute[] getAttributes(String nameTemplate);

	/**
	 * @param name
	 * @return attribute by name
	 */
	Attribute getAttribute(String name);
}
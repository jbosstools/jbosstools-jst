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

import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.IProposalProcessor;
import org.jboss.tools.jst.web.kb.KbQuery;

/**
 * @author Alexey Kazakov
 */
public interface IComponent extends IProposalProcessor {

	/**
	 * @return component name
	 */
	String getName();

	/**
	 * @return description
	 */
	String getDescription();

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
	IAttribute[] getAttributes();

	/**
	 * @param nameTemplate
	 * @return attributes with names which start with given template.
	 */
	IAttribute[] getAttributes(String nameTemplate);

	/**
	 * @return all required attributes of this component
	 */
	IAttribute[] getRequiredAttributes();

	/**
	 * @return all preferable attributes of this component
	 */
	IAttribute[] getPreferableAttributes();

	/**
	 * @param name
	 * @return attribute by name
	 */
	IAttribute getAttribute(String name);

	/**
	 * Return attributes
	 * @param query
	 * @param context
	 * @return
	 */
	public IAttribute[] getAttributes(KbQuery query, IPageContext context);
}
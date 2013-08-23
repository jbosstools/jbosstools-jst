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

import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.KbQuery;

/**
 * @author Alexey Kazakov
 */
public interface IContextComponent extends IComponent {

	/**
	 * Returns all attributes with the given name declared 
	 * for the component in current tag library.
	 * Attribute extensions are not excluded.
	 * Default implementation returns same result as getAttributes().
	 * For custom custom tag library that defines attribute 
	 * providers, they are queried, while getAttributes() 
	 * in that case may return empty list.
	 * @param query
	 * @param name
	 * @return attributes by name
	 */
	IAttribute[] getAttributes(IPageContext context, KbQuery query, String name);

	/**
	 * getAttributes(context, query, false);
	 */
	IAttribute[] getAttributes(IPageContext context, KbQuery query);

	/**
	 * Returns all attributes declared for the component
	 * in current tag library. Attribute extensions are not excluded.
	 * Default implementation returns same result as getAttributes().
	 * For custom custom tag library that defines attribute 
	 * providers, they are queried, while getAttributes() 
	 * in that case may return empty list.
	 * @param query
	 * @return
	 */
	IAttribute[] getAttributes(IPageContext context, KbQuery query, boolean includeExtensions);
}
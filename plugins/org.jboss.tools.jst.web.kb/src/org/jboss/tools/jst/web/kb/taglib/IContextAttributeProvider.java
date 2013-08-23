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
public interface IContextAttributeProvider extends IAttributeProvider {

	/**
	 * @return all the available attributes for the query
	 */
	IAttribute[] getAttributes(IPageContext context, KbQuery query);

	/**
	 * Returns attributes of the component
	 * @param query
	 * @param name
	 * @return
	 */
	IAttribute getAttribute(IPageContext context, KbQuery query, String name);
}
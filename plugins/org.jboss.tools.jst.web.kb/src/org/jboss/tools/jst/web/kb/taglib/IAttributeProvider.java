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

import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.internal.taglib.CustomTagLibComponent;

/**
 * @author Alexey Kazakov
 */
public interface IAttributeProvider {

	/**
	 * @deprecated Use IContextAttributeProvider.getAttributes(IPageContext context, KbQuery query) instead
	 * @return all the available attributes for the query
	 */
	@Deprecated
	IAttribute[] getAttributes(KbQuery query);

	/**
	 * Sets the parent component
	 * @param parentComponent
	 */
	void setComponent(CustomTagLibComponent parentComponent);

	/**
	 * Gets the parent component
	 * @return
	 */
	CustomTagLibComponent getComponent();

	/**
	 * Returns attributes of the component
	 * @deprecated Use IContextAttributeProvider.getAttribute(IPageContext context, KbQuery query, String name) instead
	 * @param query
	 * @param name
	 * @return
	 */
	@Deprecated
	IAttribute getAttribute(KbQuery query, String name);
}
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
import java.util.Map;
import java.util.Set;

import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.jst.web.kb.taglib.INameSpace;

/**
 * 
 * @author Victor Rubezhny
 * 
 */
public interface IXmlContext extends ELContext {

	/**
	 * Returns all the URIs which are used in the page.
	 * @return
	 */
	Set<String> getURIs();

	/**
	 * Returns mapping of URIs to name space objects for the root element.
	 * 
	 * @return
	 */
	Map<String, List<INameSpace>> getRootNameSpaces();
}
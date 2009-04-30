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

import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.common.el.core.resolver.ELResolver;
import org.jboss.tools.jst.web.kb.taglib.TagLibrary;

/**
 * Page context
 * @author Alexey Kazakov
 */
public interface PageContext extends ELContext {

	/**
	 * Returns libraries which should be used in this context  
	 * @return
	 */
	TagLibrary[] getLibraries();

	/**
	 * Returns EL Resolvers which are declared for this page
	 * @return
	 */
	ELResolver[] getElResolvers();

	/**
	 * Returns resource bundles
	 * @return
	 */
	ResourceBundle[] getResourceBundles();
}
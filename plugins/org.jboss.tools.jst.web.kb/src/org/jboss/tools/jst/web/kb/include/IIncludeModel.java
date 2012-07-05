/******************************************************************************* 
 * Copyright (c) 2012 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.include;

import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.jboss.tools.common.el.core.resolver.Var;

/**
 * Keeps data from <ui:include> elements.
 * 
 * @author Viacheslav Kabanovich
 *
 */
public interface IIncludeModel {

	/**
	 * Returns list of parameters defined by <ui:param> nodes to be used at the page with the given path.
	 * 
	 * @param page
	 * @return
	 */
	public List<Var> getVars(IPath path);

	/**
	 * Cleans data collected for the path.
	 * 
	 * @param path
	 */
	public void clean(IPath path);

	/**
	 * Adds to the model data defined by <ui:include> at path.
	 * 
	 * @param parent
	 * @param include
	 */
	public void addInclude(IPath path, PageInclude include);

}

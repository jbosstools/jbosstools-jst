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

import java.util.Map;

/**
 * @author Alexey Kazakov
 */
public interface IFaceletPageContext extends IPageContext {

	/**
	 * Returns parent page context. For example if some this facelet page is used in a template then
	 * this method will return a page context for that template.
	 * May return null. 
	 * @return
	 */
	IFaceletPageContext getParentContext();

	/**
	 * Returns parameters which are declared in the parent context and are available within this page.
	 * Key - name of Parameter.
	 * Value - value of Parameter. 
	 * @return
	 */
	Map<String, String> getParams();
}
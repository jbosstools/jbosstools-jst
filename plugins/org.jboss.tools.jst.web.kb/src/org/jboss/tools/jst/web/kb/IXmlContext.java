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

import org.jboss.tools.common.el.core.ELReference;
import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.common.el.core.resolver.Var;

/**
 * 
 * @author Victor Rubezhny
 * 
 */
public interface IXmlContext extends ELContext {

	/**
	 * Returns "var" attributes which are available in particular offset.
	 * 
	 * @param offset
	 * @return
	 */
	Var[] getVars(int offset);

	/**
	 * Returns all EL references of the file of this context.
	 * 
	 * @return
	 */
	ELReference[] getELReferences();
}
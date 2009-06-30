/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.jst.css.properties;

import org.eclipse.ui.views.properties.tabbed.ITypeMapper;
import org.eclipse.wst.css.core.internal.provisional.document.ICSSNode;
import org.eclipse.wst.css.core.internal.provisional.document.ICSSStyleRule;

/**
 * @author Sergey Dzmitrovich
 * 
 */
public class TypeMapper implements ITypeMapper {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.properties.tabbed.ITypeMapper#mapType(java.lang.
	 * Object)
	 */
	public Class mapType(Object object) {

		while (object instanceof ICSSNode) {
			if ((object instanceof ICSSStyleRule)
					|| (((ICSSNode) object).getParentNode() == null)) {
				break;
			} else {
				object = ((ICSSNode) object).getParentNode();
			}
		}
		return object.getClass();
	}

}

/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.jst.web.validation;

import org.jboss.tools.common.model.*;
import org.jboss.tools.common.validation.ValidationErrorManager;
import org.jboss.tools.jst.web.model.helpers.WebAppHelper;

/**
 * @author Viacheslav Kabanovich
 */
public class CheckFilterMappingName extends Check {
	static String ATTR = "filter-name"; //$NON-NLS-1$

	public CheckFilterMappingName(ValidationErrorManager manager, String preference) {
		super(manager, preference, ATTR);
	}

	public void check(XModelObject object) {
		String filterName = object.getAttributeValue(ATTR);
		if(filterName == null) return;
		if(filterName.length() == 0) {
			fireMessage(object, WebXMLValidatorMessages.EMPTY, ATTR);
		} else if(findFilter(object, filterName) == null) {
			fireMessage(object, WebXMLValidatorMessages.FILTER_NOT_EXISTS, ATTR, filterName);
		}
	}
	
	XModelObject findFilter(XModelObject mapping, String name) {
		XModelObject webxml = WebAppHelper.getParentFile(mapping);
		XModelObject[] cs = WebAppHelper.getFilters(webxml);
		if(cs != null) for (int i = 0; i < cs.length; i++) {
			if(name.equals(cs[i].getAttributeValue(ATTR))) return cs[i];
		}
		return null;
	}

}

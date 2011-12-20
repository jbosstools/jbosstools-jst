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
package org.jboss.tools.jst.web.kb.internal.validation;

import org.jboss.tools.common.validation.IWarningNameMap;
import org.jboss.tools.jst.web.kb.preferences.ELSeverityPreferences;

/**
 * @author Alexey Kazakov
 */
public class ELSuppressWarningsMap implements IWarningNameMap {

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.common.validation.IWarningNameMap#getWarningNames(java.lang.String)
	 */
	@Override
	public String[] getWarningNames(String preferenceID) {
		return ELSeverityPreferences.getInstance().getWarningNames(preferenceID);
	}
}
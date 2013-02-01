/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.palette;

import java.lang.reflect.Field;

import org.jboss.tools.jst.web.ui.WebUiPlugin;

/**
 * @deprecated use the fields of PaletteUIMessages instead
 */
public class Messages {
	private Messages() {
	}
	
    /**
	 * Gets a resource string by field name. This is useful when the field name
	 * is constructed ad hoc.
	 * 
	 * @param fieldName
	 * @return
	 */
	public static String getString(String fieldName) {
		Class c = PaletteUIMessages.class;
		try {
			Field field = c.getDeclaredField(fieldName);
			return (String) field.get(null);
		} catch (NoSuchFieldException e) {
			WebUiPlugin.getPluginLog().logError(e);
			return "!" + fieldName + "!"; //$NON-NLS-1$ //$NON-NLS-2$
		} catch (IllegalArgumentException e) {
			WebUiPlugin.getPluginLog().logError(e);
			return "!" + fieldName + "!"; //$NON-NLS-1$ //$NON-NLS-2$
		} catch (IllegalAccessException e) {
			WebUiPlugin.getPluginLog().logError(e);
			return "!" + fieldName + "!"; //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
}

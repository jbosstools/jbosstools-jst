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
package org.jboss.tools.jst.jsp.preferences;

import org.eclipse.jst.jsp.ui.internal.JSPUIPlugin;
import org.jboss.tools.jst.jsp.preferences.xpl.XMLOccurrencesPreferencePage;

/**
 * @author Jeremy
 */
public class JSPOccurrencesPreferencePage extends XMLOccurrencesPreferencePage {

	public JSPOccurrencesPreferencePage() {
		super(JSPUIPlugin.ID, JSPUIPlugin.getDefault().getPreferenceStore()); // add tau 02.02.2005
	}
}
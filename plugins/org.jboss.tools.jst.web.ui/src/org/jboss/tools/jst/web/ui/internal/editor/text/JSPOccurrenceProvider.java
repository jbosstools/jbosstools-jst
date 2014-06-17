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
package org.jboss.tools.jst.web.ui.internal.editor.text;

import org.eclipse.jst.jsp.ui.internal.JSPUIPlugin;

import org.jboss.tools.jst.web.ui.internal.editor.preferences.JSPOccurrencePreferenceConstants;
import org.jboss.tools.jst.jsp.text.xpl.DefaultStructuredTextOccurrenceStructureProvider;

/**
 * @author Jeremy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class JSPOccurrenceProvider extends DefaultStructuredTextOccurrenceStructureProvider {
	
	public JSPOccurrenceProvider () {
		super (JSPUIPlugin.ID, JSPUIPlugin.getDefault().getPreferenceStore());
	}

	public boolean affectsPreferences(String property) {
		return JSPOccurrencePreferenceConstants.affectsPreferences(property);
	}

}

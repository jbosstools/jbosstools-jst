/******************************************************************************* 
 * Copyright (c) 2014 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.palette.html.jquery.wizard;

import org.jboss.tools.jst.web.ui.internal.preferences.js.IPreferredJSLibVersion;
import org.jboss.tools.jst.web.ui.internal.preferences.js.PreferredJSLibVersions;
import org.jboss.tools.jst.web.ui.palette.html.wizard.VersionPage;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class JQueryVersionPage extends VersionPage implements IPreferredJSLibVersion, JQueryConstants {
	protected JQueryVersionPage(String pageName, String title) {
		super(pageName, title);
	}

	@Override
    public NewJQueryWidgetWizard<?> getWizard() {
        return (NewJQueryWidgetWizard<?>)super.getWizard();
    }

	@Override
	protected PreferredJSLibVersions getPreferredVersions() {
		return getWizard().getPreferredVersions();
	}

}

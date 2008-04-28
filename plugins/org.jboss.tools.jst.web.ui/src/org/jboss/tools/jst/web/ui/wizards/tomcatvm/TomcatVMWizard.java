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
package org.jboss.tools.jst.web.ui.wizards.tomcatvm;

import org.jboss.tools.common.model.ui.wizards.query.AbstractQueryWizard;

import org.jboss.tools.common.model.options.PreferenceModelUtilities;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;

public class TomcatVMWizard extends AbstractQueryWizard {
	public TomcatVMWizard() {
		setView(new TomcatVMWizardView());
	}
	
	public void setObject(Object object) {
		getView().setModel(PreferenceModelUtilities.getPreferenceModel());
		getView().setObject(object);
		getView().setWindowTitle(WebUIMessages.START_TOMCAT_SERVER);
		getView().setTitle(WebUIMessages.CHECK_JVM);
	}
}

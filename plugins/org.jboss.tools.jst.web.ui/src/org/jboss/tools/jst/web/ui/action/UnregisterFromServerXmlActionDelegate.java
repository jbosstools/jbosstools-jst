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
package org.jboss.tools.jst.web.ui.action;

import org.eclipse.core.resources.IProject;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.web.ui.action.ServerXmlActionDelegate;

public class UnregisterFromServerXmlActionDelegate extends ServerXmlActionDelegate {
	
	protected String getActionPath() {
		return "Registration.UnregisterInServerXML"; //$NON-NLS-1$
	}

	protected boolean isRelevantProject(IProject project) {
		return EclipseResourceUtil.getModelNature(project) != null;
	}

}

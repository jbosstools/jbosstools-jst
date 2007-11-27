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
package org.jboss.tools.jst.web.ui.editors.webapp.form;

import org.jboss.tools.common.model.ui.forms.*;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;

public class WebAppWelcomeFormLayoutData {
	static String WELCOME_LIST_ENTITY = "WebAppWelcomFileList"; //$NON-NLS-1$
	static String WELCOME_FILE_ENTITY = "WebAppWelcomFile"; //$NON-NLS-1$

	static IFormData WELCOME_FILE_LIST_DEFINITION = new FormData(
		WebUIMessages.WELCOME_FILES,
		WebAppJspConfigFormLayoutData.EMPTY_DESCRIPTION,
		new FormAttributeData[]{new FormAttributeData("file name", 100)}, //$NON-NLS-1$
		new String[]{WELCOME_FILE_ENTITY},
		FormLayoutDataUtil.createDefaultFormActionData("CreateActions.CreateWelcomFile") //$NON-NLS-1$
	);

	private final static IFormData[] WELCOME_LIST_DEFINITIONS = new IFormData[] {
		WELCOME_FILE_LIST_DEFINITION,
	};

	final static IFormData WELCOME_LIST_FORM_DEFINITION = new FormData(
		WELCOME_LIST_ENTITY, new String[]{null}, WELCOME_LIST_DEFINITIONS);
}

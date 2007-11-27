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

public class WebAppAuthConstraintFormLayoutData {
	static String AUTH_CONSTRAINT_ENTITY = "WebAppAuthConstraint"; //$NON-NLS-1$
	static String ROLE_NAME_ENTITY = "WebAppRoleName"; //$NON-NLS-1$

	static IFormData ROLE_LIST_DEFINITION = new FormData(
		WebUIMessages.SECURITY_ROLES,
		WebAppJspConfigFormLayoutData.EMPTY_DESCRIPTION,
		new FormAttributeData[]{new FormAttributeData("role-name", 100)}, //$NON-NLS-1$
		new String[]{ROLE_NAME_ENTITY},
		FormLayoutDataUtil.createDefaultFormActionData("CreateActions.CreateRole") //$NON-NLS-1$
	);

	private final static IFormData[] AUTH_CONSTRAINT_DEFINITIONS = new IFormData[] {
		new FormData(
			WebUIMessages.AUTH_CONSTRAINT,
			WebAppJspConfigFormLayoutData.EMPTY_DESCRIPTION,
			FormLayoutDataUtil.createGeneralFormAttributeData(AUTH_CONSTRAINT_ENTITY)
		),
		ROLE_LIST_DEFINITION,
	};

	final static IFormData AUTH_CONSTRAINT_FORM_DEFINITION = new FormData(
		AUTH_CONSTRAINT_ENTITY, new String[]{null}, AUTH_CONSTRAINT_DEFINITIONS);
}

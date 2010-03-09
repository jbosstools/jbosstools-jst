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
import org.jboss.tools.jst.web.model.helpers.WebAppHelper;

public class WebAppServletFormLayoutData {
	static String SERVLET_ENTITY = WebAppHelper.SERVLET_ENTITY;
	static String SECURITY_ROLE_REF_ENTITY = "WebAppSecurityRoleRef"; //$NON-NLS-1$

	static IFormData SECURITY_ROLE_REF_LIST_DEFINITION = new FormData(
		WebUIMessages.SECURITY_ROLES,
		WebAppJspConfigFormLayoutData.EMPTY_DESCRIPTION,
		new FormAttributeData[]{new FormAttributeData("role-name", 50), new FormAttributeData("role-link", 50)}, //$NON-NLS-1$ //$NON-NLS-2$
		new String[]{SECURITY_ROLE_REF_ENTITY},
		FormLayoutDataUtil.createDefaultFormActionData("CreateActions.CreateSecurityRoleRef") //$NON-NLS-1$
	);

	private final static IFormData[] creareServletDefinitions(String entity) {
		return new IFormData[] {
		new FormData(
			WebUIMessages.SERVLET,
			WebAppJspConfigFormLayoutData.EMPTY_DESCRIPTION,
			FormLayoutDataUtil.createGeneralFormAttributeData(entity)
		),
		WebAppFilterFormLayoutData.INIT_PARAM_LIST_DEFINITION,
		SECURITY_ROLE_REF_LIST_DEFINITION,
		new FormData(
			WebUIMessages.ADVANCED,
			WebAppJspConfigFormLayoutData.EMPTY_DESCRIPTION,
			FormLayoutDataUtil.createAdvancedFormAttributeData(entity)
		),
		};
	}

	final static IFormData SERVLET_FORM_DEFINITION = new FormData(
		SERVLET_ENTITY, new String[]{null}, creareServletDefinitions(SERVLET_ENTITY));

	final static IFormData SERVLET_30_FORM_DEFINITION = new FormData(
		WebAppHelper.SERVLET_30_ENTITY, new String[]{null}, creareServletDefinitions(WebAppHelper.SERVLET_30_ENTITY));
}

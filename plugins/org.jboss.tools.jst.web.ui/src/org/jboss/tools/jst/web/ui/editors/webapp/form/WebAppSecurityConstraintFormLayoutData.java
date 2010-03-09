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

public class WebAppSecurityConstraintFormLayoutData {
	static String SECURITY_CONSTRAINT_ENTITY = "WebAppSecurityConstraint"; //$NON-NLS-1$
	static String SECURITY_CONSTRAINT_30_ENTITY = "WebAppSecurityConstraint30"; //$NON-NLS-1$
	static String RESOURCE_COLLECTION_ENTITY = "WebAppResourceCollection"; //$NON-NLS-1$
	static String RESOURCE_COLLECTION_30_ENTITY = "WebAppResourceCollection30"; //$NON-NLS-1$

	static IFormData RESOURCE_COLLECTION_LIST_DEFINITION = new FormData(
		WebUIMessages.RESOURCE_COLLECTIONS,
		WebAppJspConfigFormLayoutData.EMPTY_DESCRIPTION,
		new FormAttributeData[]{new FormAttributeData("web-resource-name", 100)}, //$NON-NLS-1$
		new String[]{RESOURCE_COLLECTION_ENTITY, RESOURCE_COLLECTION_30_ENTITY},
		FormLayoutDataUtil.createDefaultFormActionData("CreateActions.CreateResourceCollection") //$NON-NLS-1$
	);

	private final static IFormData[] SECURITY_CONSTRAINT_DEFINITIONS = new IFormData[] {
		new FormData(
			WebUIMessages.SECURITY_CONSTRAINT,
			WebAppJspConfigFormLayoutData.EMPTY_DESCRIPTION,
			FormLayoutDataUtil.createGeneralFormAttributeData(SECURITY_CONSTRAINT_ENTITY)
		),
		RESOURCE_COLLECTION_LIST_DEFINITION,
	};

	final static IFormData SECURITY_CONSTRAINT_FORM_DEFINITION = new FormData(
		SECURITY_CONSTRAINT_ENTITY, new String[]{null}, SECURITY_CONSTRAINT_DEFINITIONS);

	final static IFormData SECURITY_CONSTRAINT_30_FORM_DEFINITION = new FormData(
		SECURITY_CONSTRAINT_30_ENTITY, new String[]{null}, SECURITY_CONSTRAINT_DEFINITIONS);
}

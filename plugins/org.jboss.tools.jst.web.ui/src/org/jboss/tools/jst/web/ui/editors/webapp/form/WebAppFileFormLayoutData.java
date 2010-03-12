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
import org.jboss.tools.jst.web.ui.Messages;

public class WebAppFileFormLayoutData {
	static String FILE_23_ENTITY = "FileWebApp"; //$NON-NLS-1$
	static String FILE_24_ENTITY = "FileWebApp24"; //$NON-NLS-1$
	static String FILE_25_ENTITY = "FileWebApp25"; //$NON-NLS-1$
	static String FILE_30_ENTITY = "FileWebApp30"; //$NON-NLS-1$
	
	static IFormData CONTEXT_PARAM_FOLDER_DEFINITION = new FormData(
		Messages.WebAppFileFormLayoutData_ContextParams,
		WebAppJspConfigFormLayoutData.EMPTY_DESCRIPTION,
		"Context Params", //$NON-NLS-1$
		new FormAttributeData[]{new FormAttributeData("param-name", 40), new FormAttributeData("param-value", 60)}, //$NON-NLS-1$ //$NON-NLS-2$
		new String[]{WebAppHelper.CONTEXT_PARAM_ENTITY},
		FormLayoutDataUtil.createDefaultFormActionData("CreateActions.CreateContextParam") //$NON-NLS-1$
	);

	private final static IFormData[] createFileDefinitions(String name, String entity) {
		return new IFormData[] {
			new FormData(
				name,
				WebAppJspConfigFormLayoutData.EMPTY_DESCRIPTION,
				FormLayoutDataUtil.createGeneralFormAttributeData(FILE_23_ENTITY)
			),
			CONTEXT_PARAM_FOLDER_DEFINITION,
			new FormData(
				WebUIMessages.ADVANCED,
				WebAppJspConfigFormLayoutData.EMPTY_DESCRIPTION,
				FormLayoutDataUtil.createAdvancedFormAttributeData(entity)
			)
		};
	}
	
	public static IFormData FILE_WEB_APP_23_DEFINITION = new FormData(
		FILE_23_ENTITY, new String[]{null}, createFileDefinitions(Messages.WebAppFileFormLayoutData_WebDesc23, FILE_23_ENTITY)
	);

	public static IFormData FILE_WEB_APP_24_DEFINITION = new FormData(
		FILE_24_ENTITY, new String[]{null}, createFileDefinitions(Messages.WebAppFileFormLayoutData_WebDesc24, FILE_24_ENTITY)
	);

	public static IFormData FILE_WEB_APP_25_DEFINITION = new FormData(
		FILE_25_ENTITY, new String[]{null}, createFileDefinitions(Messages.WebAppFileFormLayoutData_WebDesc25, FILE_25_ENTITY)
	);

}

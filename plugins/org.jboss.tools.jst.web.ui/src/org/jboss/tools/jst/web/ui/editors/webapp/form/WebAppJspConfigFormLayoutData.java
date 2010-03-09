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

public class WebAppJspConfigFormLayoutData {
	static String EMPTY_DESCRIPTION = ""; //$NON-NLS-1$
	static String JSP_CONFIG_ENTITY = "WebAppJspConfig"; //$NON-NLS-1$
	static String JSP_CONFIG_30_ENTITY = "WebAppJspConfig30"; //$NON-NLS-1$
	static String TAGLIB_ENTITY = WebAppHelper.TAGLIB_ENTITY;
	static String PROPERTY_GROUP_ENTITY = "WebAppJSPPropertyGroup"; //$NON-NLS-1$
	static String PROPERTY_GROUP_30_ENTITY = "WebAppJSPPropertyGroup30"; //$NON-NLS-1$

	static IFormData TAGLIB_LIST_DEFINITION = new FormData(
		WebUIMessages.TAGLIBS,
		EMPTY_DESCRIPTION,
		new FormAttributeData[]{new FormAttributeData("taglib-uri", 100)}, //$NON-NLS-1$
		new String[]{TAGLIB_ENTITY},
		FormLayoutDataUtil.createDefaultFormActionData("CreateActions.CreateTaglib") //$NON-NLS-1$
	);

	static IFormData PROPERTY_GROUP_LIST_DEFINITION = new FormData(
		WebUIMessages.PROPERTY_GROUPS,
		EMPTY_DESCRIPTION,
		new FormAttributeData[]{new FormAttributeData("url-pattern", 100)}, //$NON-NLS-1$
		new String[]{PROPERTY_GROUP_ENTITY, PROPERTY_GROUP_30_ENTITY},
		FormLayoutDataUtil.createDefaultFormActionData("CreateActions.CreatePropertyGroup") //$NON-NLS-1$
	);

	private final static IFormData[] JSP_CONFIG_DEFINITIONS = new IFormData[] {
		TAGLIB_LIST_DEFINITION,
		PROPERTY_GROUP_LIST_DEFINITION,
	};

	private final static IFormData[] PROPERTY_GROUP_DEFINITIONS = new IFormData[] {
		new FormData(
			WebUIMessages.PROPERTY_GROUP,
			EMPTY_DESCRIPTION,
			FormLayoutDataUtil.createGeneralFormAttributeData(PROPERTY_GROUP_ENTITY)
		),
		new FormData(
			WebUIMessages.ADVANCED,
			EMPTY_DESCRIPTION,
			FormLayoutDataUtil.createAdvancedFormAttributeData(PROPERTY_GROUP_ENTITY)
		),
	};

	private final static IFormData[] PROPERTY_GROUP_30_DEFINITIONS = new IFormData[] {
		new FormData(
			WebUIMessages.PROPERTY_GROUP,
			EMPTY_DESCRIPTION,
			FormLayoutDataUtil.createGeneralFormAttributeData(PROPERTY_GROUP_30_ENTITY)
		),
		new FormData(
			WebUIMessages.ADVANCED,
			EMPTY_DESCRIPTION,
			FormLayoutDataUtil.createAdvancedFormAttributeData(PROPERTY_GROUP_30_ENTITY)
		),
	};

	final static IFormData JSP_CONFIG_FORM_DEFINITION = new FormData(
		JSP_CONFIG_ENTITY, new String[]{null}, JSP_CONFIG_DEFINITIONS);

	final static IFormData JSP_CONFIG_30_FORM_DEFINITION = new FormData(
		JSP_CONFIG_30_ENTITY, new String[]{null}, JSP_CONFIG_DEFINITIONS);

	final static IFormData PROPERTY_GROUP_FORM_DEFINITION = new FormData(
		PROPERTY_GROUP_ENTITY, new String[]{null}, PROPERTY_GROUP_DEFINITIONS);

	final static IFormData PROPERTY_GROUP_30_FORM_DEFINITION = new FormData(
		PROPERTY_GROUP_30_ENTITY, new String[]{null}, PROPERTY_GROUP_30_DEFINITIONS);

}

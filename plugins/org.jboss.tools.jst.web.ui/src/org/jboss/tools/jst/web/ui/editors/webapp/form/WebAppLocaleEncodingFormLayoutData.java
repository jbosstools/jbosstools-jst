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

public class WebAppLocaleEncodingFormLayoutData {
	static String LOCALE_ENC_MAPPING_LIST_ENTITY = "WebAppLocaleEncodingMappingList"; //$NON-NLS-1$
	static String LOCALE_ENC_MAPPING_ENTITY = "WebAppLocaleEncodingMapping"; //$NON-NLS-1$

	static IFormData LOCALE_ENC_MAPPING_LIST_DEFINITION = new FormData(
		WebUIMessages.LOCALE_ENCODING_MAPPINGS,
		WebAppJspConfigFormLayoutData.EMPTY_DESCRIPTION,
		new FormAttributeData[]{new FormAttributeData("locale", 50), new FormAttributeData("encoding", 50)}, //$NON-NLS-1$ //$NON-NLS-2$
		new String[]{LOCALE_ENC_MAPPING_ENTITY},
		FormLayoutDataUtil.createDefaultFormActionData("CreateActions.CreateLocaleEncodingMapping") //$NON-NLS-1$
	);

	private final static IFormData[] LOCALE_ENC_MAPPING_DEFINITIONS = new IFormData[] {
		LOCALE_ENC_MAPPING_LIST_DEFINITION,
	};

	final static IFormData LOCALE_ENC_MAPPING_FORM_DEFINITION = new FormData(
		LOCALE_ENC_MAPPING_LIST_ENTITY, new String[]{null}, LOCALE_ENC_MAPPING_DEFINITIONS);
}

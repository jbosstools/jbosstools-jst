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

import java.util.*;
import org.jboss.tools.common.model.util.ClassLoaderUtil;
import org.jboss.tools.common.model.ui.forms.*;

public class WebAppFormLayoutData implements IFormLayoutData {
	static {
		ClassLoaderUtil.init();
	}

	private final static IFormData[] FORM_LAYOUT_DEFINITIONS = new IFormData[] {
		WebAppFilterFormLayoutData.FILTER_FORM_DEFINITION,
		WebAppServletFormLayoutData.SERVLET_FORM_DEFINITION,
		WebAppWelcomeFormLayoutData.WELCOME_LIST_FORM_DEFINITION,
		WebAppAuthConstraintFormLayoutData.AUTH_CONSTRAINT_FORM_DEFINITION,
		WebAppSecurityConstraintFormLayoutData.SECURITY_CONSTRAINT_FORM_DEFINITION,
		WebAppLocaleEncodingFormLayoutData.LOCALE_ENC_MAPPING_FORM_DEFINITION,
		WebAppJspConfigFormLayoutData.JSP_CONFIG_FORM_DEFINITION,
		WebAppJspConfigFormLayoutData.PROPERTY_GROUP_FORM_DEFINITION,
		WebAppFoldersFormLayoutData.CONTEXT_PARAM_FOLDER_DEFINITION,
		WebAppFoldersFormLayoutData.FILTER_FOLDER_DEFINITION,
		WebAppFoldersFormLayoutData.FILTER_FOLDER_24_DEFINITION,
		WebAppFoldersFormLayoutData.LISTENER_FOLDER_DEFINITION,
		WebAppFoldersFormLayoutData.LISTENER_FOLDER_24_DEFINITION,
		WebAppFoldersFormLayoutData.LIFECYCLE_FOLDER_24_DEFINITION,
		WebAppFoldersFormLayoutData.PERSISTENCE_FOLDER_24_DEFINITION,
		WebAppFoldersFormLayoutData.SERVLET_FOLDER_DEFINITION,
		WebAppFoldersFormLayoutData.MIME_FOLDER_DEFINITION,
		WebAppFoldersFormLayoutData.ERROR_FOLDER_DEFINITION,
		WebAppFoldersFormLayoutData.TAGLIB_FOLDER_DEFINITION,
		WebAppFoldersFormLayoutData.RESOURCE_FOLDER_DEFINITION,
		WebAppFoldersFormLayoutData.SECURITY_FOLDER_DEFINITION,
		WebAppFoldersFormLayoutData.ROLE_FOLDER_DEFINITION,
		WebAppFoldersFormLayoutData.ENV_FOLDER_DEFINITION,
		WebAppFoldersFormLayoutData.EJB_FOLDER_DEFINITION,
		WebAppFoldersFormLayoutData.SERVICE_FOLDER_DEFINITION,
		WebAppFoldersFormLayoutData.MESSAGE_FOLDER_DEFINITION,
		
		WebAppFileFormLayoutData.FILE_WEB_APP_23_DEFINITION,
		WebAppFileFormLayoutData.FILE_WEB_APP_24_DEFINITION,
		WebAppFileFormLayoutData.FILE_WEB_APP_25_DEFINITION,
	};

	private static Map FORM_LAYOUT_DEFINITION_MAP = Collections.unmodifiableMap(new ArrayToMap(FORM_LAYOUT_DEFINITIONS));
	
	private static WebAppFormLayoutData INSTANCE = new WebAppFormLayoutData();
	
	public static IFormLayoutData getInstance() {
		return INSTANCE;
	}
	
	private WebAppFormLayoutData() {}

	public IFormData getFormData(String entityName) {
		return (IFormData)FORM_LAYOUT_DEFINITION_MAP.get(entityName);
	}

}

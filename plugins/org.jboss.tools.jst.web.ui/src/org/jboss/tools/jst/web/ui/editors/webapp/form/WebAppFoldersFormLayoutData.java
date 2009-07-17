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
import org.jboss.tools.jst.web.model.helpers.WebAppHelper;
import org.jboss.tools.jst.web.ui.Messages;

public class WebAppFoldersFormLayoutData {
	static String EMPTY_DESCRIPTION = ""; //$NON-NLS-1$
	static String CONTEXT_PARAM_FOLDER_ENTITY = "WebAppFolderContextParams"; //$NON-NLS-1$
	static String CONTEXT_PARAM_ENTITY = WebAppHelper.CONTEXT_PARAM_ENTITY;
	static String FILTER_FOLDER_ENTITY = "WebAppFolderFilters"; //$NON-NLS-1$
	static String FILTER_FOLDER_24_ENTITY = "WebAppFolderFilters24"; //$NON-NLS-1$
	static String LISTENER_FOLDER_ENTITY = "WebAppFolderListeners"; //$NON-NLS-1$
	static String LISTENER_FOLDER_24_ENTITY = "WebAppFolderListeners24"; //$NON-NLS-1$
	static String SERVLET_FOLDER_ENTITY = "WebAppFolderServlets"; //$NON-NLS-1$
	static String MIME_FOLDER_ENTITY = "WebAppFolderMimeMappings"; //$NON-NLS-1$
	static String ERROR_FOLDER_ENTITY = "WebAppFolderErrorPages"; //$NON-NLS-1$
	static String TAGLIB_FOLDER_ENTITY = "WebAppFolderTaglibs"; //$NON-NLS-1$
	static String RESOURCE_FOLDER_ENTITY = "WebAppFolderResources"; //$NON-NLS-1$
	static String SECURITY_FOLDER_ENTITY = "WebAppFolderSecurityConstraints"; //$NON-NLS-1$
	static String ROLE_FOLDER_ENTITY = "WebAppFolderSecurityRoles"; //$NON-NLS-1$
	static String ENV_FOLDER_ENTITY = "WebAppFolderEnvEntries"; //$NON-NLS-1$
	static String EJB_FOLDER_ENTITY = "WebAppFolderEJB"; //$NON-NLS-1$
	static String SERVICE_FOLDER_ENTITY = "WebAppFolderServices"; //$NON-NLS-1$	
	static String MESSAGE_FOLDER_ENTITY = "WebAppFolderMessageDestinations"; //$NON-NLS-1$
	
	
	static IFormData CONTEXT_PARAM_LIST_DEFINITION = new FormData(
		Messages.WebAppFoldersFormLayoutData_ContextParams,
		EMPTY_DESCRIPTION,
		new FormAttributeData[]{new FormAttributeData("param-name", 40), new FormAttributeData("param-value", 60)}, //$NON-NLS-1$ //$NON-NLS-2$
		new String[]{CONTEXT_PARAM_ENTITY},
		FormLayoutDataUtil.createDefaultFormActionData("CreateActions.CreateContextParam") //$NON-NLS-1$
	);

	static IFormData[] CONTEXT_PARAM_FOLDER_DEFINITIONS = new IFormData[]{
		CONTEXT_PARAM_LIST_DEFINITION
	};

	static IFormData FILTER_LIST_DEFINITION = new FormData(
		Messages.WebAppFoldersFormLayoutData_Filters,
		EMPTY_DESCRIPTION,
		new FormAttributeData[]{new FormAttributeData("filter-name", 100)}, //$NON-NLS-1$
		new String[]{WebAppHelper.FILTER_ENTITY},
		FormLayoutDataUtil.createDefaultFormActionData("CreateActions.CreateFilter") //$NON-NLS-1$
	);

	static IFormData FILTER_MAPPING_LIST_DEFINITION = new FormData(
		Messages.WebAppFoldersFormLayoutData_FilterMappings,
		EMPTY_DESCRIPTION,
		new FormAttributeData[]{new FormAttributeData("filter-name", 50), new FormAttributeData("url-pattern", 50)}, //$NON-NLS-1$ //$NON-NLS-2$
		new String[]{WebAppHelper.FILTER_MAPPING_ENTITY},
		FormLayoutDataUtil.createDefaultFormActionData("CreateActions.CreateFilterMapping") //$NON-NLS-1$
	);

	static IFormData FILTER_MAPPING_24_LIST_DEFINITION = new FormData(
		Messages.WebAppFoldersFormLayoutData_FilterMappings,
		EMPTY_DESCRIPTION,
		new FormAttributeData[]{new FormAttributeData("filter-name", 50), new FormAttributeData("url-pattern", 50)}, //$NON-NLS-1$ //$NON-NLS-2$
		new String[]{WebAppHelper.FILTER_MAPPING_24_ENTITY},
		FormLayoutDataUtil.createDefaultFormActionData("CreateActions.CreateFilterMapping") //$NON-NLS-1$
	);

	static IFormData[] FILTER_FOLDER_DEFINITIONS = new IFormData[]{
		FILTER_LIST_DEFINITION,
		FILTER_MAPPING_LIST_DEFINITION
	};

	static IFormData[] FILTER_FOLDER_24_DEFINITIONS = new IFormData[]{
		FILTER_LIST_DEFINITION,
		FILTER_MAPPING_24_LIST_DEFINITION
	};

	static IFormData LISTENER_LIST_DEFINITION = new FormData(
		Messages.WebAppFoldersFormLayoutData_Listeners,
		EMPTY_DESCRIPTION,
		new FormAttributeData[]{new FormAttributeData("listener-class", 100)}, //$NON-NLS-1$
		new String[]{WebAppHelper.LISTENER_ENTITY},
		FormLayoutDataUtil.createDefaultFormActionData("CreateActions.CreateListener") //$NON-NLS-1$
	);

	static IFormData[] LISTENER_FOLDER_DEFINITIONS = new IFormData[]{
		LISTENER_LIST_DEFINITION
	};

	static IFormData LISTENER_24_LIST_DEFINITION = new FormData(
		Messages.WebAppFoldersFormLayoutData_Listeners,
		EMPTY_DESCRIPTION,
		new FormAttributeData[]{new FormAttributeData("listener-class", 100)}, //$NON-NLS-1$
		new String[]{WebAppHelper.LISTENER_24_ENTITY},
		FormLayoutDataUtil.createDefaultFormActionData("CreateActions.CreateListener") //$NON-NLS-1$
	);

	static IFormData[] LISTENER_FOLDER_24_DEFINITIONS = new IFormData[]{
		LISTENER_24_LIST_DEFINITION
	};

	static IFormData SERVLET_LIST_DEFINITION = new FormData(
		Messages.WebAppFoldersFormLayoutData_Servlets,
		EMPTY_DESCRIPTION,
		new FormAttributeData[]{new FormAttributeData("servlet-name", 30), new FormAttributeData("servlet-class", 70)}, //$NON-NLS-1$ //$NON-NLS-2$
		new String[]{WebAppHelper.SERVLET_ENTITY},
		FormLayoutDataUtil.createDefaultFormActionData("CreateActions.CreateServlet") //$NON-NLS-1$
	);

	static IFormData SERVLET_MAPPING_LIST_DEFINITION = new FormData(
		Messages.WebAppFoldersFormLayoutData_ServletMappings,
		EMPTY_DESCRIPTION,
		new FormAttributeData[]{new FormAttributeData("servlet-name", 50), new FormAttributeData("url-pattern", 50)}, //$NON-NLS-1$ //$NON-NLS-2$
		new String[]{WebAppHelper.SERVLET_MAPPING_ENTITY},
		FormLayoutDataUtil.createDefaultFormActionData("CreateActions.CreateServletMapping") //$NON-NLS-1$
	);

	static IFormData[] SERVLET_FOLDER_DEFINITIONS = new IFormData[]{
		SERVLET_LIST_DEFINITION,
		SERVLET_MAPPING_LIST_DEFINITION
	};

	static IFormData MIME_LIST_DEFINITION = new FormData(
		Messages.WebAppFoldersFormLayoutData_MimeMappings,
		EMPTY_DESCRIPTION,
		new FormAttributeData[]{new FormAttributeData("extension", 50), new FormAttributeData("mime-type", 50)}, //$NON-NLS-1$ //$NON-NLS-2$
		new String[]{Messages.WebAppFoldersFormLayoutData_WebAppMimeMapping},
		FormLayoutDataUtil.createDefaultFormActionData("CreateActions.CreateMimeMapping") //$NON-NLS-1$
	);

	static IFormData[] MIME_FOLDER_DEFINITIONS = new IFormData[]{
		MIME_LIST_DEFINITION
	};

	static IFormData ERROR_LIST_DEFINITION = new FormData(
		Messages.WebAppFoldersFormLayoutData_ErrorPages,
		EMPTY_DESCRIPTION,
		new FormAttributeData[]{new FormAttributeData("location", 70), new FormAttributeData("error-code", 30)}, //$NON-NLS-1$ //$NON-NLS-2$
		new String[]{Messages.WebAppFoldersFormLayoutData_WebAppErrorPage},
		FormLayoutDataUtil.createDefaultFormActionData("CreateActions.CreateErrorPage") //$NON-NLS-1$
	);

	static IFormData[] ERROR_FOLDER_DEFINITIONS = new IFormData[]{
		ERROR_LIST_DEFINITION
	};

	static IFormData[] TAGLIB_FOLDER_DEFINITIONS = new IFormData[]{
		WebAppJspConfigFormLayoutData.TAGLIB_LIST_DEFINITION
	};


	static IFormData RESOURCE_ENV_REF_LIST_DEFINITION = new FormData(
		Messages.WebAppFoldersFormLayoutData_ResourceEnvRefs,
		EMPTY_DESCRIPTION,
		new FormAttributeData[]{new FormAttributeData("resource-env-ref-name", 30), new FormAttributeData("resource-env-ref-type", 70)}, //$NON-NLS-1$ //$NON-NLS-2$
		new String[]{"WebAppResourceEnvRef"}, //$NON-NLS-1$
		FormLayoutDataUtil.createDefaultFormActionData("CreateActions.CreateResourceEnvRef") //$NON-NLS-1$
	);

	static IFormData RESOURCE_REF_LIST_DEFINITION = new FormData(
		Messages.WebAppFoldersFormLayoutData_ResourceRefs,
		EMPTY_DESCRIPTION,
		new FormAttributeData[]{new FormAttributeData("res-ref-name", 30), new FormAttributeData("res-type", 70)}, //$NON-NLS-1$ //$NON-NLS-2$
		new String[]{"WebAppResourceRef"}, //$NON-NLS-1$
		FormLayoutDataUtil.createDefaultFormActionData("CreateActions.CreateResourceRef") //$NON-NLS-1$
	);

	static IFormData[] RESOURCE_FOLDER_DEFINITIONS = new IFormData[]{
		RESOURCE_ENV_REF_LIST_DEFINITION,
		RESOURCE_REF_LIST_DEFINITION
	};
		
	static IFormData SECURITY_LIST_DEFINITION = new FormData(
		Messages.WebAppFoldersFormLayoutData_SecurityConstraints,
		EMPTY_DESCRIPTION,
		new FormAttributeData[]{new FormAttributeData("display-name", 100)}, //$NON-NLS-1$
		new String[]{"WebAppSecurityConstraint"}, //$NON-NLS-1$
		FormLayoutDataUtil.createDefaultFormActionData("CreateActions.CreateSecurityConstraint") //$NON-NLS-1$
	);

	static IFormData[] SECURITY_FOLDER_DEFINITIONS = new IFormData[]{
		SECURITY_LIST_DEFINITION
	};

	static IFormData ROLE_LIST_DEFINITION = new FormData(
		Messages.WebAppFoldersFormLayoutData_SecurityRoles,
		EMPTY_DESCRIPTION,
		new FormAttributeData[]{new FormAttributeData("role-name", 100)}, //$NON-NLS-1$
		new String[]{WebAppHelper.ROLE_ENTITY},
		FormLayoutDataUtil.createDefaultFormActionData("CreateActions.CreateSecurityRole") //$NON-NLS-1$
	);

	static IFormData[] ROLE_FOLDER_DEFINITIONS = new IFormData[]{
		ROLE_LIST_DEFINITION
	};

	static IFormData ENV_LIST_DEFINITION = new FormData(
		Messages.WebAppFoldersFormLayoutData_EnvEntries,
		EMPTY_DESCRIPTION,
		new FormAttributeData[]{new FormAttributeData("env-entry-name", 50), new FormAttributeData("env-entry-type", 50)}, //$NON-NLS-1$ //$NON-NLS-2$
		new String[]{"WebAppEnvEntry"}, //$NON-NLS-1$
		FormLayoutDataUtil.createDefaultFormActionData("CreateActions.CreateEnvEntry") //$NON-NLS-1$
	);

	static IFormData[] ENV_FOLDER_DEFINITIONS = new IFormData[]{
		ENV_LIST_DEFINITION
	};

	static IFormData EJB_REF_LIST_DEFINITION = new FormData(
		Messages.WebAppFoldersFormLayoutData_EjbRefList,
		EMPTY_DESCRIPTION,
		new FormAttributeData[]{new FormAttributeData("ejb-ref-name", 50), new FormAttributeData("ejb-ref-type", 50)}, //$NON-NLS-1$ //$NON-NLS-2$
		new String[]{"WebAppEjbRef"}, //$NON-NLS-1$
			FormLayoutDataUtil.createDefaultFormActionData("CreateActions.CreateEjbRef") //$NON-NLS-1$
	);

	static IFormData EJB_LOCAL_REF_LIST_DEFINITION = new FormData(
		Messages.WebAppFoldersFormLayoutData_EjbLocalRefList,
		EMPTY_DESCRIPTION,
		new FormAttributeData[]{new FormAttributeData("ejb-ref-name", 50), new FormAttributeData("ejb-ref-type", 50)}, //$NON-NLS-1$ //$NON-NLS-2$
		new String[]{"WebAppEjbLocalRef"}, //$NON-NLS-1$
		FormLayoutDataUtil.createDefaultFormActionData("CreateActions.CreateEjbLocalRef") //$NON-NLS-1$
	);

	static IFormData[] EJB_FOLDER_DEFINITIONS = new IFormData[]{
		EJB_REF_LIST_DEFINITION,
		EJB_LOCAL_REF_LIST_DEFINITION
	};

	static IFormData SERVICE_LIST_DEFINITION = new FormData(
		Messages.WebAppFoldersFormLayoutData_Services,
		EMPTY_DESCRIPTION,
		new FormAttributeData[]{new FormAttributeData("service-ref-name", 40), new FormAttributeData("service-interface", 60)}, //$NON-NLS-1$ //$NON-NLS-2$
		new String[]{"WebAppServiceRef"}, //$NON-NLS-1$
		FormLayoutDataUtil.createDefaultFormActionData("CreateActions.CreateServiceRef") //$NON-NLS-1$
	);

	static IFormData[] SERVICE_FOLDER_DEFINITIONS = new IFormData[]{
		SERVICE_LIST_DEFINITION
	};

	static IFormData MESSAGE_REF_LIST_DEFINITION = new FormData(
		Messages.WebAppFoldersFormLayoutData_MessageDestinationRefList,
		EMPTY_DESCRIPTION,
		new FormAttributeData[]{new FormAttributeData("message-destination-ref-name", 100)}, //$NON-NLS-1$
		new String[]{"WebAppMessageDestinationRef"}, //$NON-NLS-1$
		FormLayoutDataUtil.createDefaultFormActionData("CreateActions.CreateServiceMessageDestinationRef") //$NON-NLS-1$
	);

	static IFormData MESSAGE_LIST_DEFINITION = new FormData(
		Messages.WebAppFoldersFormLayoutData_MessageDestinationList,
		EMPTY_DESCRIPTION,
		new FormAttributeData[]{new FormAttributeData("message-destination-name", 100)}, //$NON-NLS-1$
		new String[]{"WebAppMessageDestination"}, //$NON-NLS-1$
		FormLayoutDataUtil.createDefaultFormActionData("CreateActions.CreateServiceMessageDestination") //$NON-NLS-1$
	);

	static IFormData[] MESSAGE_FOLDER_DEFINITIONS = new IFormData[]{
		MESSAGE_REF_LIST_DEFINITION,
		MESSAGE_LIST_DEFINITION
	};

	final static IFormData CONTEXT_PARAM_FOLDER_DEFINITION = new FormData(
		CONTEXT_PARAM_FOLDER_ENTITY, new String[]{null}, CONTEXT_PARAM_FOLDER_DEFINITIONS);
	
	final static IFormData FILTER_FOLDER_DEFINITION = new FormData(
		FILTER_FOLDER_ENTITY, new String[]{null}, FILTER_FOLDER_DEFINITIONS);

	final static IFormData FILTER_FOLDER_24_DEFINITION = new FormData(
		FILTER_FOLDER_24_ENTITY, new String[]{null}, FILTER_FOLDER_24_DEFINITIONS);

	final static IFormData LISTENER_FOLDER_DEFINITION = new FormData(
		LISTENER_FOLDER_ENTITY, new String[]{null}, LISTENER_FOLDER_DEFINITIONS);

	final static IFormData LISTENER_FOLDER_24_DEFINITION = new FormData(
		LISTENER_FOLDER_24_ENTITY, new String[]{null}, LISTENER_FOLDER_24_DEFINITIONS);

	final static IFormData SERVLET_FOLDER_DEFINITION = new FormData(
		SERVLET_FOLDER_ENTITY, new String[]{null}, SERVLET_FOLDER_DEFINITIONS);

	final static IFormData MIME_FOLDER_DEFINITION = new FormData(
		MIME_FOLDER_ENTITY, new String[]{null}, MIME_FOLDER_DEFINITIONS);

	final static IFormData ERROR_FOLDER_DEFINITION = new FormData(
		ERROR_FOLDER_ENTITY, new String[]{null}, ERROR_FOLDER_DEFINITIONS);

	final static IFormData TAGLIB_FOLDER_DEFINITION = new FormData(
		TAGLIB_FOLDER_ENTITY, new String[]{null}, TAGLIB_FOLDER_DEFINITIONS);

	final static IFormData RESOURCE_FOLDER_DEFINITION = new FormData(
		RESOURCE_FOLDER_ENTITY, new String[]{null}, RESOURCE_FOLDER_DEFINITIONS);

	final static IFormData SECURITY_FOLDER_DEFINITION = new FormData(
		SECURITY_FOLDER_ENTITY, new String[]{null}, SECURITY_FOLDER_DEFINITIONS);

	final static IFormData ROLE_FOLDER_DEFINITION = new FormData(
		ROLE_FOLDER_ENTITY, new String[]{null}, ROLE_FOLDER_DEFINITIONS);

	final static IFormData ENV_FOLDER_DEFINITION = new FormData(
		ENV_FOLDER_ENTITY, new String[]{null}, ENV_FOLDER_DEFINITIONS);

	final static IFormData EJB_FOLDER_DEFINITION = new FormData(
		EJB_FOLDER_ENTITY, new String[]{null}, EJB_FOLDER_DEFINITIONS);

	final static IFormData SERVICE_FOLDER_DEFINITION = new FormData(
		SERVICE_FOLDER_ENTITY, new String[]{null}, SERVICE_FOLDER_DEFINITIONS);

	final static IFormData MESSAGE_FOLDER_DEFINITION = new FormData(
		MESSAGE_FOLDER_ENTITY, new String[]{null}, MESSAGE_FOLDER_DEFINITIONS);

}

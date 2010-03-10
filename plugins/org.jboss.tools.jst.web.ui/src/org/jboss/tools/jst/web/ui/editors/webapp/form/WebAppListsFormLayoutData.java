/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.web.ui.editors.webapp.form;

import java.util.HashMap;
import java.util.Map;

import org.jboss.tools.common.model.ui.forms.FormAttributeData;
import org.jboss.tools.common.model.ui.forms.FormData;
import org.jboss.tools.common.model.ui.forms.FormLayoutDataUtil;
import org.jboss.tools.common.model.ui.forms.IFormData;
import org.jboss.tools.common.model.ui.forms.ModelFormLayoutData;

/**
 * @author Viacheslav Kabanovich
 */
public class WebAppListsFormLayoutData {

	static Map<String, IFormData> singleChildLists = new HashMap<String, IFormData>();
	
	static IFormData createOneAttributeSingleChildList(String header, String attrName, String childEntity, String actionPath) {
		IFormData result = new FormData(
			header,
			ModelFormLayoutData.EMPTY_DESCRIPTION,
			new FormAttributeData[]{new FormAttributeData(attrName, 100)},
			new String[]{childEntity},
			FormLayoutDataUtil.createDefaultFormActionData(actionPath)
		);
		singleChildLists.put(childEntity, result);
		return result;
	}
	
	static IFormData createTwoAttributeSingleChildList(String header, String attrName, int width, String attr2Name, String childEntity, String actionPath) {
		IFormData result = new FormData(
			header,
			ModelFormLayoutData.EMPTY_DESCRIPTION,
			new FormAttributeData[]{new FormAttributeData(attrName, width), new FormAttributeData(attr2Name, 100 - width)},
			new String[]{childEntity},
			FormLayoutDataUtil.createDefaultFormActionData(actionPath)
		);
		singleChildLists.put(childEntity, result);
		return result;
	}
	
	static {
		createOneAttributeSingleChildList(
			"Properties", "name", "WebAppProperty30", "CreateActions.CreateProperty" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		);
		createOneAttributeSingleChildList(
			"Data Sources", "name", "WebAppDataSource30", "CreateActions.CreateDataSource" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		);
		createTwoAttributeSingleChildList(
			"Services", "service-ref-name", 40, "service-interface", "WebAppServiceRef30", "CreateActions.CreateServiceRef" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		);
		createTwoAttributeSingleChildList(
			"Init Params", "param-name", 40, "param-value", "WebAppInitParam", "CreateActions.CreateInitParam" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		);
		createOneAttributeSingleChildList(
			"SOAP Headers", "header", "WebAppSoapHeader30", "CreateActions.CreateSoapHeader" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		);
		createOneAttributeSingleChildList(
			"SOAP Roles", "role", "WebAppSoapRole30", "CreateActions.CreateSoapRole" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		);
		createOneAttributeSingleChildList(
			"Port Names", "name", "WebAppPortName30", "CreateActions.CreatePortName" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		);

		createOneAttributeSingleChildList(
			"Port Component Refs", "service-endpoint-interface", "WebAppPortComponentRef30", "CreateActions.CreatePortComponentRef" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		);
		createTwoAttributeSingleChildList(
			"Handlers", "handler-name", 50, "handler-class", "WebAppHandler30", "CreateActions.CreatePortComponentRef" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		);

		createOneAttributeSingleChildList(
			"Cookie Configs", "name", "WebAppCookieConfig", "CreateActions.CreateCookieConfig" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		);
	}


}

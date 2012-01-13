/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.jst.web.webapp.model.handlers;

import java.util.Properties;

import org.jboss.tools.common.meta.action.impl.AbstractHandler;
import org.jboss.tools.common.meta.action.impl.SpecialWizardSupport;
import org.jboss.tools.common.model.ServiceDialog;
import org.jboss.tools.common.model.XModelException;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.filesystems.FileSystemsHelper;
import org.jboss.tools.common.model.plugin.ModelMessages;
import org.jboss.tools.common.model.util.FindObjectHelper;
import org.jboss.tools.jst.web.model.helpers.WebAppHelper;
import org.jboss.tools.jst.web.webapp.model.WebAppConstants;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class OpenServletHandler extends AbstractHandler implements WebAppConstants {

	public OpenServletHandler() {}

	public boolean isEnabled(XModelObject object) {
		return object != null
				&& object.getAttributeValue(SERVLET_NAME) != null 
				&& object.getAttributeValue(SERVLET_NAME).length() > 0; 
	}

	public void executeHandler(XModelObject object, Properties p) throws XModelException {
		if(!isEnabled(object)) return;
		XModelObject webxml = FileSystemsHelper.getFile(object);
		if(webxml != null) {
			String servletName = object.getAttributeValue(SERVLET_NAME);
			XModelObject servlet = WebAppHelper.findServlet(webxml, null, servletName);
			if(servlet != null) {
				FindObjectHelper.findModelObject(servlet, FindObjectHelper.IN_EDITOR_ONLY);
			} else {
				String message = "Cannot find servlet " + servletName;
				object.getModel().getService().showDialog(ModelMessages.WARNING, message, new String[]{SpecialWizardSupport.CLOSE}, null, ServiceDialog.WARNING);
			}
		}
	}

}

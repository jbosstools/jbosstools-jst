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
package org.jboss.tools.jst.web.model.handlers;

import java.util.Properties;
import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.filesystems.impl.*;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;
import org.jboss.tools.jst.web.model.helpers.TLDRegistrationHelper;
import org.jboss.tools.jst.web.project.WebProject;

public class DeleteTLDHandler extends DeleteFileHandler {

	public void executeHandler(XModelObject object, Properties p) throws Exception {
		boolean unregister = false;
		if(getHelper(object).isRegistered(object.getModel(), object)) {
			String checkboxMessage = WebUIMessages.DELETE_REFERENCE_FROM_WEBDESCRIPTOR;
			ServiceDialog d = object.getModel().getService();
			Properties pd = new Properties();
			String message = WebUIMessages.DELETE + FileAnyImpl.toFileName(object);
			pd.setProperty(ServiceDialog.DIALOG_MESSAGE, message);
			pd.setProperty(ServiceDialog.CHECKBOX_MESSAGE, checkboxMessage);
			pd.put(ServiceDialog.CHECKED, new Boolean(true));
			if(!d.openConfirm(pd)) return;
			Boolean b = (Boolean)pd.get(ServiceDialog.CHECKED);
			unregister = b.booleanValue();
		}
    	String oldPath = WebProject.getInstance(object.getModel()).getPathInWebRoot(object);
		super.executeHandler(object, p);
		if(object.isActive()) return;
		if(unregister) {
			getHelper(object).unregister(object.getModel(), oldPath);
		}
	}
	
	public boolean getSignificantFlag(XModelObject object) {
		return !getHelper(object).isRegistered(object.getModel(), object);
	}
	
	private TLDRegistrationHelper getHelper(XModelObject object) {
		return new TLDRegistrationHelper();
	}

}

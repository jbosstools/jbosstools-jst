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
package org.jboss.tools.jst.web.tiles.model.handlers;

import java.util.Properties;
import org.jboss.tools.common.model.ServiceDialog;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.filesystems.impl.DeleteFileHandler;
import org.jboss.tools.common.model.filesystems.impl.FileAnyImpl;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;
import org.jboss.tools.jst.web.tiles.model.helpers.TilesRegistrationHelper;

public class DeleteTilesHandler extends DeleteFileHandler {

	public void executeHandler(XModelObject object, Properties p) throws Exception {
		boolean unregister = false;
		if(TilesRegistrationHelper.isRegistered(object.getModel(), object)) {
			String nature = TilesRegistrationHelper.getRegistratorNature(object.getModel());
			String checkboxMessage = null;
			if(nature != null && nature.indexOf("jsf") >= 0) { 
				checkboxMessage = WebUIMessages.DELETE_REFERENCE_FROM_WEBDESCRIPTOR;
			} else {
				checkboxMessage = WebUIMessages.DELETE_REFERENCE_FROM_STRUTS_CONFIGURATION_FILE;
			}
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
    	String oldPath = ((FileAnyImpl)object).getAbsolutePath();
		super.executeHandler(object, p);
		if(object.isActive()) return;
		if(unregister) {
			TilesRegistrationHelper.unregister(object.getModel(), oldPath);
		}
	}
	
	public boolean getSignificantFlag(XModelObject object) {
		return !TilesRegistrationHelper.isRegistered(object.getModel(), object);
	}

}

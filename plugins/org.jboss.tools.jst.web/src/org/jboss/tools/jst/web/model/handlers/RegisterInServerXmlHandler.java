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

import java.util.*;
import org.jboss.tools.common.meta.action.*;
import org.jboss.tools.common.meta.action.impl.*;
import org.jboss.tools.common.model.*;

public class RegisterInServerXmlHandler extends AbstractHandler {
	String textTemplate = null;

	public boolean isEnabled(XModelObject object) {
		if(textTemplate == null) {
			textTemplate = action.getDisplayName();
		}
		if(textTemplate != null) {
			String t = textTemplate;
			int i = t.indexOf("server.xml");
			if(i >= 0) {
				t = t.substring(0, i) + "Server"/*ServerXmlHelper.getDefaultServer(2)*/ + t.substring(i + "server.xml".length());
				((XActionImpl)action).setDisplayName(t);
			}
		}
		return true;
	}

    public boolean isEnabled(XModelObject object, XModelObject[] objects) {
        if(object != null && (objects == null || objects.length == 1)) return isEnabled(object);
        return false;
    }

	public void executeHandler(XModelObject object, Properties p) throws Exception {
		SpecialWizard wizard = SpecialWizardFactory.createSpecialWizard("org.jboss.tools.jst.web.ui.wizards.appregister.AppRegisterWizard");
		if(p == null) p = new Properties();
		p.setProperty("title", action.getDisplayName());
		p.setProperty("wtp", "true");
		p.put("object", object);
		String nature = object.getModel().getProperties().getProperty("nature");
		String natureIndex = (nature == null) ? null
		  : (nature.indexOf("jsf") >= 0) ? "jsf" 
          : (nature.indexOf("struts") >= 0) ? "struts" : null;
		if(natureIndex != null) {
			p.setProperty("natureIndex", natureIndex);
		}
		wizard.setObject(p);
		wizard.execute();
	}

}

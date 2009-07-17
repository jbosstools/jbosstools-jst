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
import org.jboss.tools.common.meta.key.WizardKeys;
import org.jboss.tools.common.model.*;

public class RegisterInServerXmlHandler extends AbstractHandler {
	String textTemplate = null;

	public boolean isEnabled(XModelObject object) {
		if(textTemplate == null) {
			textTemplate = action.getDisplayName();
		}
		if(textTemplate != null) {
			String t = textTemplate;
			int i = t.indexOf("server.xml"); //$NON-NLS-1$
			if(i >= 0) {
				t = t.substring(0, i) + "Server"/*ServerXmlHelper.getDefaultServer(2)*/ + t.substring(i + "server.xml".length()); //$NON-NLS-1$ //$NON-NLS-2$
				((XActionImpl)action).setDisplayName(t);
			}
		}
		return true;
	}

    public boolean isEnabled(XModelObject object, XModelObject[] objects) {
        if(object != null && (objects == null || objects.length == 1)) return isEnabled(object);
        return false;
    }

	public void executeHandler(XModelObject object, Properties p) throws XModelException {
		SpecialWizard wizard = SpecialWizardFactory.createSpecialWizard("org.jboss.tools.jst.web.ui.wizards.appregister.AppRegisterWizard"); //$NON-NLS-1$
		if(p == null) p = new Properties();
		String displayName = WizardKeys.getMenuItemDisplayName(action, object == null ? null : object.getModelEntity());
		p.setProperty("title", displayName); //$NON-NLS-1$
		p.setProperty("wtp", "true"); //$NON-NLS-1$ //$NON-NLS-2$
		p.put("object", object); //$NON-NLS-1$
		String nature = object.getModel().getProperties().getProperty("nature"); //$NON-NLS-1$
		String natureIndex = (nature == null) ? null
		  : (nature.indexOf("jsf") >= 0) ? "jsf"  //$NON-NLS-1$ //$NON-NLS-2$
          : (nature.indexOf("struts") >= 0) ? "struts" : null; //$NON-NLS-1$ //$NON-NLS-2$
		if(natureIndex != null) {
			p.setProperty("natureIndex", natureIndex); //$NON-NLS-1$
		}
		wizard.setObject(p);
		wizard.execute();
	}

}

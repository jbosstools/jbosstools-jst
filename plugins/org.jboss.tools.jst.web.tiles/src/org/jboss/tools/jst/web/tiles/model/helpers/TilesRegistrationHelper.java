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
package org.jboss.tools.jst.web.tiles.model.helpers;

import java.util.Properties;

import org.jboss.tools.common.meta.action.*;
import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.filesystems.impl.FileAnyImpl;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.web.project.WebProject;

public class TilesRegistrationHelper {
	static Registrator[] registrators = new Registrator[]{
		new Registrator(WebProject.STRUTS_NATURE_ID, "org.jboss.tools.struts.plugins.model.handlers.TilesFileRegistration"), //$NON-NLS-1$
		new Registrator(WebProject.JSF_NATURE_ID, "org.jboss.tools.jsf.model.handlers.tile.TilesFileRegistration") //$NON-NLS-1$
	};
	
	public static boolean isEnabled(XModel model) {
		for (int i = 0; i < registrators.length; i++) {
			if(registrators[i].canRegister(model)) return true;
		}
		return false;
	}
	
	public static String getRegistratorNature(XModel model) {
		for (int i = 0; i < registrators.length; i++) {
			if(registrators[i].canRegister(model)) return registrators[i].nature;
		}
		return null;
	}
	
	public static void register(XModel model, XModelObject file) {
    	for (int i = 0; i < registrators.length; i++) {
    		if(registrators[i].canRegister(model)) {
    			registrators[i].register(model, file, null, false);
    		}
    	}
	}

	public static boolean isRegistered(XModel model, XModelObject file) {
    	for (int i = 0; i < registrators.length; i++) {
    		if(registrators[i].canRegister(model)) {
    			if(registrators[i].register(model, file, null, true)) return true;
    		}
    	}
    	return false;
	}
	

	public static void update(XModel model, XModelObject file, String oldPath) {
    	for (int i = 0; i < registrators.length; i++) {
    		if(registrators[i].canRegister(model)) {
    			registrators[i].register(model, file, oldPath, false);
    		}
    	}
	}

	public static void unregister(XModel model, String oldPath) {
    	for (int i = 0; i < registrators.length; i++) {
    		if(registrators[i].canRegister(model)) {
    			registrators[i].register(model, null, oldPath, false);
    		}
    	}
	}

}

class Registrator {
	String nature;
	String className;
	SpecialWizard wizard;
	
	public Registrator(String nature, String className) {
		this.nature = nature;
		this.className = className;
	}
	
	public boolean canRegister(XModel model) {
		if(!EclipseResourceUtil.hasNature(model, nature)) return false;
		if(wizard == null) {
			if(className != null) {
				wizard = SpecialWizardFactory.createSpecialWizard(className);
				className = null;
			}
		}
		return wizard != null;
	}
	
	public boolean register(XModel model, XModelObject file, String oldPath, boolean test) {
    	Properties p = new Properties();
    	p.put("model", model); //$NON-NLS-1$
    	String webRoot = WebProject.getInstance(model).getWebRootLocation().replace('\\', '/');
    	if(!webRoot.endsWith("/")) webRoot += "/"; //$NON-NLS-1$ //$NON-NLS-2$
    	if(file != null && file.isActive()) {
        	String path = ((FileAnyImpl)file).getAbsolutePath();
        	if(!path.toLowerCase().startsWith(webRoot.toLowerCase())) return false;
        	path = path.substring(webRoot.length() - 1);
        	p.setProperty("path", path); //$NON-NLS-1$
    	}
    	if(oldPath != null) {
    		oldPath = oldPath.substring(webRoot.length() - 1);
    		p.setProperty("oldPath", oldPath);    		 //$NON-NLS-1$
    	}
    	if(test) p.setProperty("test", "true"); //$NON-NLS-1$ //$NON-NLS-2$
    	wizard.setObject(p);
    	return wizard.execute() == 0;
	}

}

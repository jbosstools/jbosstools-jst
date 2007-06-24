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
package org.jboss.tools.jst.web.project.handlers;

import java.io.File;
import java.util.Properties;

import org.eclipse.osgi.util.NLS;

import org.jboss.tools.common.meta.action.XActionInvoker;
import org.jboss.tools.common.meta.action.impl.*;
import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.options.PreferenceModelUtilities;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;
import org.jboss.tools.jst.web.project.helpers.*;

public class AddJarToLibrarySetSupport extends SpecialWizardSupport {
	
	public static String run(LibrarySet librarySet) {
		XModel model = PreferenceModelUtilities.getPreferenceModel();
		String entity = "LibrarySet"; //$NON-NLS-1$
		String actionPath = "CreateActions.AddJar"; //$NON-NLS-1$
		Properties p = new Properties();
		p.put("librarySet", librarySet); //$NON-NLS-1$
		XActionInvoker.invoke(entity, actionPath, model.getRoot(), p);
		return p.getProperty("jarname"); //$NON-NLS-1$
	}
	
	LibrarySet librarySet;
	
	public void reset() {
		librarySet = (LibrarySet)getProperties().get("librarySet"); //$NON-NLS-1$
	}

	public void action(String name) throws Exception {
		if(OK.equals(name)) {
			execute();
			setFinished(true);
		} else if(CANCEL.equals(name)) {
			setFinished(true);
		}
	}
	
	protected void execute() throws Exception {
		String location = extractStepData(0).getProperty("location"); //$NON-NLS-1$
		String jarname = librarySet.addJar(location);
		getProperties().setProperty("jarname", jarname); //$NON-NLS-1$
	}

    protected DefaultWizardDataValidator validator = new JarValidator();
    
    public WizardDataValidator getValidator(int step) {
    	validator.setSupport(this, step);
		return validator;    	
    }
    
    class JarValidator extends DefaultWizardDataValidator {
    	public void validate(Properties data) {
    		message = null;
    		super.validate(data);
    		if(message != null) return;
    		String location = data.getProperty("location").replace('\\', '/'); //$NON-NLS-1$
    		if(!new File(location).isFile()) {
    			message = NLS.bind(WebUIMessages.FILE_DOESNOT_EXIST,location);
    		}
    	}
    }

}

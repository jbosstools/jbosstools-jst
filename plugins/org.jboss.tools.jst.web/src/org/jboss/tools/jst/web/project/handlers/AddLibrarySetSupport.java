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

import java.util.Properties;

import org.eclipse.osgi.util.NLS;

import org.jboss.tools.common.meta.action.XActionInvoker;
import org.jboss.tools.common.meta.action.impl.*;
import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.options.PreferenceModelUtilities;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;
import org.jboss.tools.jst.web.project.helpers.LibrarySets;

public class AddLibrarySetSupport extends SpecialWizardSupport {
	
	public static String run() {
		XModel model = PreferenceModelUtilities.getPreferenceModel();
		String entity = "LibrarySet"; //$NON-NLS-1$
		String actionPath = "CreateActions.CreateLibrarySet"; //$NON-NLS-1$
		Properties p = new Properties();
		XActionInvoker.invoke(entity, actionPath, model.getRoot(), p);
		return p.getProperty("name"); //$NON-NLS-1$
	}

	public void action(String name) throws XModelException {
		if(OK.equals(name) || FINISH.equals(name)) {
			execute();
			setFinished(true);
		} else if(CANCEL.equals(name)) {
			setFinished(true);
		}
	}
	
	protected void execute() throws XModelException {
		String name = extractStepData(0).getProperty("name"); //$NON-NLS-1$
		LibrarySets.getInstance().addLibrarySet(name);
		getProperties().setProperty("name", name); //$NON-NLS-1$
	}

    protected DefaultWizardDataValidator validator = new LibrarySetValidator();
    
    public WizardDataValidator getValidator(int step) {
    	validator.setSupport(this, step);
		return validator;    	
    }
    
	String FORBIDDEN_INDICES = "\"\n\t*\\/:<>?|"; //$NON-NLS-1$
    class LibrarySetValidator extends DefaultWizardDataValidator {
    	public void validate(Properties data) {
    		message = null;
    		super.validate(data);
    		if(message != null) return;
    		String name = data.getProperty("name"); //$NON-NLS-1$
    		if(LibrarySets.getInstance().getLibrarySet(name) != null) {
    			message = NLS.bind(WebUIMessages.LIBRARY_SET_ALREADY_EXISTS, name);
    		} else if(name.equals(" .")) { //$NON-NLS-1$
    			message = WebUIMessages.LIBRARY_SET_NAME_MUST_BE_A_VALID_FOLDERNAME;
    		} else if(name.endsWith(".") && name.indexOf('.') != name.lastIndexOf('.')) { //$NON-NLS-1$
    			message = WebUIMessages.LIBRARY_SET_NAME_MUST_BE_A_VALID_FOLDER_NAME;
    		} else {
    			for (int i = 0; i < FORBIDDEN_INDICES.length(); i++) {
    				if(name.indexOf(FORBIDDEN_INDICES.charAt(i)) >= 0) {
    	    			message = NLS.bind(WebUIMessages.MUST_NOT_CONTAIN_CHARACTER, ""+FORBIDDEN_INDICES.charAt(i)); //$NON-NLS-1$
    					return;
    				}
    			}				
    		}    			
    	}    	
    }

}

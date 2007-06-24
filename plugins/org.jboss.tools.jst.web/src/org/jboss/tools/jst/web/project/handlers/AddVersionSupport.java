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

import java.util.*;

import org.eclipse.osgi.util.NLS;

import org.jboss.tools.common.meta.action.XActionInvoker;
import org.jboss.tools.common.meta.action.impl.*;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.options.PreferenceModelUtilities;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;
import org.jboss.tools.jst.web.project.helpers.LibrarySets;
import org.jboss.tools.jst.web.project.version.ProjectVersions;

public class AddVersionSupport extends SpecialWizardSupport {
	
	public static String run(ProjectVersions versions, String entity) {
		XModel model = PreferenceModelUtilities.getPreferenceModel();
		String actionPath = "CreateActions.CreateProjectVersion"; //$NON-NLS-1$
		Properties p = new Properties();
		p.put("versions", versions); //$NON-NLS-1$
		XActionInvoker.invoke(entity, actionPath, model.getRoot(), p);
		return p.getProperty("name"); //$NON-NLS-1$
	}
	
	ProjectVersions versions;
	String defaultLocation;
	
	public void reset() {
		versions = (ProjectVersions)getProperties().get("versions"); //$NON-NLS-1$
		setLists();
		setDefaultLocation();
	}
	
	void setLists() {
		String[] s = LibrarySets.getInstance().getLibrarySetList();
		setValueList(0, "core library", s); //$NON-NLS-1$
// hide "common library"
	}
	
	void setDefaultLocation() {
		defaultLocation = versions.getPath();
		String location = defaultLocation;
		setAttributeValue(0, "templates location", location); //$NON-NLS-1$
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
		String name = extractStepData(0).getProperty("name"); //$NON-NLS-1$
		versions.addVersion(extractStepData(0));
		getProperties().setProperty("name", name); //$NON-NLS-1$
	}

    protected DefaultWizardDataValidator validator = new ProjectVersionValidator();
    
    public WizardDataValidator getValidator(int step) {
    	validator.setSupport(this, step);
		return validator;    	
    }
    
	String FORBIDDEN_INDICES = "\"\n\t*\\/:<>?|"; //$NON-NLS-1$
    class ProjectVersionValidator extends DefaultWizardDataValidator {
    	public void validate(Properties data) {
    		message = null;
    		super.validate(data);
    		if(message != null) return;
    		String name = data.getProperty("name"); //$NON-NLS-1$
    		if(versions.getVersion(name) != null) {
    			message = NLS.bind(WebUIMessages.IMPLEMENTATION_ALREADY_EXISTS,name);
    			return;
    		}
    		//TODO validate location
    	}
    	
    }

}

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
import org.jboss.tools.jst.web.project.helpers.*;
import org.jboss.tools.jst.web.project.version.ProjectVersion;

public class AddLibraryToVersionSupport extends SpecialWizardSupport {
	
	public static String run(ProjectVersion version) {
		XModel model = PreferenceModelUtilities.getPreferenceModel();
		String entity = "LibraryReference"; //$NON-NLS-1$
		String actionPath = "CreateActions.CreateLibraryReference"; //$NON-NLS-1$
		Properties p = new Properties();
		p.put("version", version); //$NON-NLS-1$
		XActionInvoker.invoke(entity, actionPath, model.getRoot(), p);
		return p.getProperty("name"); //$NON-NLS-1$
	}

	ProjectVersion version;
	Set<String> names;
	Set<String> usedNames;

	public void reset() {
		version = (ProjectVersion)(getProperties().get("version")); //$NON-NLS-1$
		if(version == null) {
			setFinished(true);
			return;
		}
		setLibraryList();
	}
	
	void setLibraryList() {
		names = new TreeSet<String>();
		usedNames = new HashSet<String>();
		String[] all = LibrarySets.getInstance().getLibrarySetList();
		for (int i = 0; i < all.length; i++) {
			names.add(all[i]);
		}
		String[] exclude = version.getLibraryNames();
		for (int i = 0; i < exclude.length; i++) {
			names.remove(exclude[i]);
			usedNames.add(exclude[i]);
		}		
		String[] pl = names.toArray(new String[0]);
		setValueList(0, "name", pl); //$NON-NLS-1$
		if(pl.length > 0) setAttributeValue(0, "name", pl[0]); //$NON-NLS-1$
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
		version.addLibrary(name);
		getProperties().setProperty("name", name); //$NON-NLS-1$
	}

    protected DefaultWizardDataValidator validator = new LibraryValidator();
    
    public WizardDataValidator getValidator(int step) {
    	validator.setSupport(this, step);
		return validator;    	
    }
    
    class LibraryValidator extends DefaultWizardDataValidator {
    	public void validate(Properties data) {
    		message = null;
    		super.validate(data);
    		if(message != null) return;
    		String name = data.getProperty("name"); //$NON-NLS-1$
    		if(usedNames.contains(name)) {
    			message = NLS.bind(WebUIMessages.LIBRARY_SET_IS_ALREADY_ADDED, name);
    		} else if(!names.contains(name)) {
    			message = NLS.bind(WebUIMessages.LIBRARY_SET_ISNOT_FOUND, name);
    		} 
    	}
    	
    }

}

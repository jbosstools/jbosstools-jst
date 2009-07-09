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

import org.jboss.tools.common.meta.action.impl.*;
import org.jboss.tools.common.model.XModelException;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;
import org.jboss.tools.jst.web.project.helpers.*;

public class EditProjectTemplateSupport extends MultistepWizardSupport {

	public static String run(AbstractWebProjectTemplate template, String entity, String version, String name) {
		String actionPath = "EditActions.EditProjectTemplate"; //$NON-NLS-1$
		Properties p = new Properties();
		p.put("version", version); //$NON-NLS-1$
		p.put("name", name); //$NON-NLS-1$
		return AddProjectTemplateSupport.run(template, entity, actionPath, p);
	}
	
	AbstractWebProjectTemplate template;
	EditProjectTemplateStep step;
	
	ProjectTemplate projectTemplate;
	Set<String> templates;
	
	protected MultistepWizardStep[] createSteps() {
		return new MultistepWizardStep[]{
			step = new EditProjectTemplateStep()
		};		
	}

	public void reset() {
		initSteps();
		template = (AbstractWebProjectTemplate)getProperties().get("template"); //$NON-NLS-1$
		String versionName = getProperties().getProperty("version"); //$NON-NLS-1$
		String name = getProperties().getProperty("name"); //$NON-NLS-1$
		projectTemplate = template.getProjectTemplate(versionName, name);
		getProperties().put("projectTemplate", projectTemplate); //$NON-NLS-1$
		templates = new HashSet<String>();
		if(versionName != null) {
			String[] ts = template.getTemplateList(versionName);
			for (int i = 0; i < ts.length; i++) templates.add(ts[i]);
		}
	}
	
	public void action(String name) throws XModelException {
		if(WebUIMessages.EditProjectTemplateSupport_Save.equals(name)) {
			name = FINISH;
		}
		super.action(name);
	}	

	public String[] getActionNames(int stepId) {
		return new String[]{WebUIMessages.EditProjectTemplateSupport_Save, CANCEL};
	}

	protected void execute() throws XModelException {
		projectTemplate.commit();
		getProperties().setProperty("name", projectTemplate.getName()); //$NON-NLS-1$
	}

    protected DefaultWizardDataValidator validator = new ProjectTemplateValidator();
    
    public WizardDataValidator getValidator(int step) {
    	if(step == 0) {
    		validator.setSupport(this, step);
    		return validator;
    	} else {
    		return super.getValidator(step);
    	}
    }
    
    class ProjectTemplateValidator extends DefaultWizardDataValidator {
    	public void validate(Properties data) {
    		message = null;
    		super.validate(data);
    		if(message != null) return;
    		String name = data.getProperty("name"); //$NON-NLS-1$
    		if(projectTemplate.isNameModified() && templates.contains(name)) {
    			message = NLS.bind(WebUIMessages.PROJECT_TEMPLATE_ALREADY_EXISTS, name);
    			return;
    		}
    	}
    }
    
}

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

import java.util.Map;
import java.util.Properties;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Path;
import org.eclipse.osgi.util.NLS;

import org.jboss.tools.common.meta.action.XActionInvoker;
import org.jboss.tools.common.meta.action.impl.DefaultWizardDataValidator;
import org.jboss.tools.common.meta.action.impl.SpecialWizardSupport;
import org.jboss.tools.common.meta.action.impl.WizardDataValidator;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.options.PreferenceModelUtilities;
import org.jboss.tools.common.model.plugin.ModelPlugin;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;
import org.jboss.tools.jst.web.project.helpers.AbstractWebProjectTemplate;

public class AddPageTemplateSupport extends SpecialWizardSupport {
	
	public static String run(AbstractWebProjectTemplate template, String entity, XModelObject selection) {
		XModel model = PreferenceModelUtilities.getPreferenceModel();
		String actionPath = "CreateActions.CreatePageTemplate"; //$NON-NLS-1$
		Properties p = new Properties();
		p.put("template", template); //$NON-NLS-1$
		if(selection != null) p.put("selection", selection); //$NON-NLS-1$
		XActionInvoker.invoke(entity, actionPath, model.getRoot(), p);
		return p.getProperty("name"); //$NON-NLS-1$
	}
	
	AbstractWebProjectTemplate template;
	Map pageTemplates;
	
	public void reset() {
		template = (AbstractWebProjectTemplate)getProperties().get("template"); //$NON-NLS-1$
		pageTemplates = template.getPageTemplates();
		initSelection();
	}
	
	private void initSelection() {
		XModelObject selection = (XModelObject)getProperties().get("selection"); //$NON-NLS-1$
		if(selection == null) return;
		IFile f = (IFile)EclipseResourceUtil.getResource(selection);
		if(f == null) return;
		String path = f.getFullPath().toString();
		setAttributeValue(0, "page path", path); //$NON-NLS-1$
		setAttributeValue(0, "name", selection.getAttributeValue("name")); //$NON-NLS-1$ //$NON-NLS-2$
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
		Properties p0 = extractStepData(0);
		String name = p0.getProperty("name"); //$NON-NLS-1$
		String path = p0.getProperty("page path"); //$NON-NLS-1$
		IFile r = getSelectedResource(path);
		if(r != null) template.addPageTemplate(getFinalTemplateName(r, name), r);
		getProperties().setProperty("name", name); //$NON-NLS-1$
	}

    protected DefaultWizardDataValidator validator = new PageTemplateValidator();
    
    public WizardDataValidator getValidator(int step) {
    	validator.setSupport(this, step);
		return validator;    	
    }
    
    class PageTemplateValidator extends DefaultWizardDataValidator {
    	public void validate(Properties data) {
    		message = null;
    		super.validate(data);
    		if(message != null) return;
    		String name = data.getProperty("name"); //$NON-NLS-1$
    		if(pageTemplates.containsKey(name)) {
    			message = NLS.bind(WebUIMessages.TEMPLATE_PAGE_ALREADY_EXISTS, name);
    			return;
    		}
    		IFile r = getSelectedResource(data.getProperty("page path")); //$NON-NLS-1$
    		if(r == null || !r.exists()) {
    			message = WebUIMessages.PAGE_PATH_ISNOT_CORRECT;
    			return;
    		}
    		String n = getFinalTemplateName(r, name);
    		if(!name.equals(n) && pageTemplates.containsKey(n)) {
    			message = NLS.bind(WebUIMessages.TEMPLATE_PAGE_ALREADY_EXISTS, n);
    			return;
    		}
    	}
    }
    
    String getFinalTemplateName(IFile f, String name) {
		String n = f.getName();
		if(n.indexOf(".jsp") >= 0) return name; //$NON-NLS-1$
		int q = n.lastIndexOf('.');
		if(q < 0) return name;
		String ext = n.substring(q);
		if(name.endsWith(ext)) return name;
		return name + ext;
    }
    
    IFile selectedFile;
    String pagePath;
    
    IFile getSelectedResource(String pagePath) {
    	if(pagePath == null) return null;
    	if(pagePath.equals(this.pagePath)) return selectedFile;
    	this.pagePath = pagePath;
    	try {
    		selectedFile = ModelPlugin.getWorkspace().getRoot().getFile(new Path(pagePath)); 
    	} catch (Exception e) {}
    	return selectedFile;
    }

}

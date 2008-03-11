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
import org.eclipse.core.resources.IProject;
import org.jboss.tools.common.meta.action.impl.AbstractHandler;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.web.project.helpers.AbstractWebProjectTemplate;
import org.jboss.tools.jst.web.project.helpers.WebProjectTemplateFactory;

public class CreateTemplateFromProjectHandler extends AbstractHandler {

    public boolean isEnabled(XModelObject object) {
    	if(object == null) return false;
        return EclipseResourceUtil.getModelNature(EclipseResourceUtil.getProject(object)) != null;
    }

    public boolean isEnabled(XModelObject object, XModelObject[] objects) {
        if(object != null && (objects == null || objects.length == 1)) return isEnabled(object);
        return false;
    }

    public void executeHandler(XModelObject object, Properties p) throws Exception {
    	AbstractWebProjectTemplate template = WebProjectTemplateFactory.getTemplate(object);
    	IProject project =  EclipseResourceUtil.getProject(object);
    	if(template != null && project != null) {
    		template.addProjectTemplate(project);
    	}
    }

}

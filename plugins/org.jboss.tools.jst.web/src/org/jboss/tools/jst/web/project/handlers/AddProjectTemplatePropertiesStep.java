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
import org.jboss.tools.common.meta.action.impl.MultistepWizardStep;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.loaders.impl.PropertiesLoader;
import org.jboss.tools.common.util.FileUtil;
import org.jboss.tools.jst.web.project.helpers.ProjectTemplate;

public class AddProjectTemplatePropertiesStep extends MultistepWizardStep {
	XModelObject properties;

	public String getStepImplementingClass() {
		return "org.jboss.tools.jst.web.ui.wizards.project.AddProjectTemplatePropertiesView"; //$NON-NLS-1$
	}
	
	public void reset() {
		properties = getSupport().getTarget().getModel().createModelObject("FilePROPERTIES", null); //$NON-NLS-1$
		getSupport().getProperties().put("properties", properties); //$NON-NLS-1$
	}

	void createPropertiesFile(File target) {
		if(properties == null || properties.getChildren().length == 0) return;
		File f = new File(target, ProjectTemplate.PREPROCESSING_PROPERTIES);
		PropertiesLoader loader = new PropertiesLoader();
		String body = loader.getBody(properties);
		FileUtil.writeFile(f, body.toString());
	}

}

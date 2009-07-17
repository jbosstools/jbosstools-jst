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
package org.jboss.tools.jst.web.project.helpers;

import java.lang.reflect.Method;

import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.common.model.util.ModelFeatureFactory;
import org.jboss.tools.jst.web.WebModelPlugin;
import org.jboss.tools.jst.web.project.WebProject;

public class WebProjectTemplateFactory {
	
	public static AbstractWebProjectTemplate getTemplate(XModelObject object) {
		Class cls = null;
		if(EclipseResourceUtil.hasNature(object.getModel(), WebProject.JSF_NATURE_ID)) {
			cls = ModelFeatureFactory.getInstance().getFeatureClass("org.jboss.tools.jsf.web.JSFTemplate"); //$NON-NLS-1$
		} else if(EclipseResourceUtil.hasNature(object.getModel(), WebProject.STRUTS_NATURE_ID)) {
			cls = ModelFeatureFactory.getInstance().getFeatureClass("org.jboss.tools.struts.StrutsUtils"); //$NON-NLS-1$
		}
		if(cls == null) return null;
		try {
			Method m = cls.getDeclaredMethod("getInstance", new Class[0]); //$NON-NLS-1$
			m.setAccessible(true);
			return (AbstractWebProjectTemplate)m.invoke(null, new Object[0]);
		} catch (Exception e) {
			WebModelPlugin.getPluginLog().logError(e);
			return null;
		}
	}

}

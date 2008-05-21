/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Exadel, Inc.
 *     Red Hat, Inc. 
 *******************************************************************************/
package org.jboss.tools.jst.web.debug.xpl;

import java.util.HashMap;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugException;

import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.project.IModelNature;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.web.WebUtils;
import org.jboss.tools.jst.web.debug.internal.ClassNameBuilderFacrtory;
import org.jboss.tools.jst.web.debug.internal.JspLineBreakpoint;

public class JspDebugUtils {

	public static String getGeneratedJavaClassName(String jspPath, String configName) {
		return ClassNameBuilderFacrtory.getInstance().getClassNameBuilder(configName).getJavaClassName(jspPath);
	}

	public static JspLineBreakpoint createJspLineBreakpoint(IResource resource, int lineNumber, int charStart, int charEnd) throws DebugException {
		String relativeJspPath = null;

		IPath webRootPath = new Path(WebUtils.getWebRootPath(resource.getProject()));
		IPath jspPath = resource.getLocation();
		if (!jspPath.isPrefixOf(webRootPath)) {
			jspPath = jspPath.removeFirstSegments(webRootPath.segmentCount());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < jspPath.segmentCount(); i++) {
				sb.append('/').append(jspPath.segment(i));
			}
			relativeJspPath = sb.toString();
		}

		return new JspLineBreakpoint(resource, relativeJspPath, lineNumber, charStart, charEnd, true, new HashMap(10)); 
	}
	
    private static IPath getWebRoot(IProject project) {
		if(project != null && project.isOpen()) {
			IModelNature modelNature = EclipseResourceUtil.getModelNature(project);
			XModel model = (modelNature == null) ? null : modelNature.getModel();
			XModelObject webRoot = (model == null) ? null : model.getByPath("FileSystems/WEB-ROOT");
			IResource webRootResource = (webRoot == null) ? null : EclipseResourceUtil.getResource(webRoot);
			return webRootResource.getFullPath();
		}
	    return null;
	}	
}
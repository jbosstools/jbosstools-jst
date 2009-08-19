/******************************************************************************* 
 * Copyright (c) 2009 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.jst.jsp.contentassist.actions;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IMarkerResolution;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.contentassist.AbstractXMLContentAssistProcessor;
import org.jboss.tools.jst.jsp.messages.JstUIMessages;
import org.jboss.tools.jst.web.kb.IKbProject;

/**
 * The Marker Resolution that enables the KB Nature on the project
 * 
 * @author Victor Rubezhny
 *
 */
public class EnableJSFCAOnProject implements IMarkerResolution{

	public String getLabel() {
		return JstUIMessages.ENABLE_KB;
	}

	public void run(IMarker marker) {
		IResource resource = marker.getResource();
		IProject project = resource instanceof IProject ? (IProject)resource : 
			resource != null ? resource.getProject() : null;
		
		if (project == null)
			return;
		
		try {
			EclipseResourceUtil.addNatureToProject(project, IKbProject.NATURE_ID);
			// Find existing KBNATURE problem marker and kill it if exists
			project.deleteMarkers(AbstractXMLContentAssistProcessor.KB_PROBLEM_MARKER_TYPE, true, IResource.DEPTH_ONE);
		} catch (CoreException e) {
			JspEditorPlugin.getPluginLog().logError(e);
		}
	}

}

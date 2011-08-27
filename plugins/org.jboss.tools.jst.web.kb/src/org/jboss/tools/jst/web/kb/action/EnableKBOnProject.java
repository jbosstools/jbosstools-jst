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
package org.jboss.tools.jst.web.kb.action;

import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ui.IMarkerResolution;
import org.jboss.tools.jst.web.kb.WebKbPlugin;
import org.jboss.tools.jst.web.kb.internal.KbBuilderMarker;
import org.jboss.tools.jst.web.kb.internal.scanner.UsedJavaProjectCheck;

/**
 * The Marker Resolution that enables the KB Nature on the project
 * 
 * @author Victor Rubezhny
 *
 */
public class EnableKBOnProject implements IMarkerResolution {
	String label;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void run(IMarker marker) {
		IResource resource = marker.getResource();
		IProject project = resource instanceof IProject ? (IProject)resource : resource != null ? resource.getProject() : null;
		
		if (project == null)
			return;
		
		try {
			WebKbPlugin.enableKB(project, new NullProgressMonitor());

			List<IProject> ps = new UsedJavaProjectCheck().getNonKbJavaProjects(project);
			for (IProject p: ps) {
				WebKbPlugin.enableKB(p, new NullProgressMonitor());
				p.deleteMarkers(KbBuilderMarker.KB_BUILDER_PROBLEM_MARKER_TYPE, true, IResource.DEPTH_ONE);
			}
			// Find existing KBNATURE problem marker and kill it if exists
			project.deleteMarkers(KbBuilderMarker.KB_BUILDER_PROBLEM_MARKER_TYPE, true, IResource.DEPTH_ONE);
		} catch (CoreException e) {
			WebKbPlugin.getDefault().logError(e);
		}
	}
}
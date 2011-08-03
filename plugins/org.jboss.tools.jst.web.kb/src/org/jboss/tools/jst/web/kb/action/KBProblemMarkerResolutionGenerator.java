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
import org.eclipse.core.runtime.CoreException;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator;
import org.jboss.tools.jst.web.kb.KbMessages;
import org.jboss.tools.jst.web.kb.WebKbPlugin;
import org.jboss.tools.jst.web.kb.internal.KbBuilderMarker;
import org.jboss.tools.jst.web.kb.internal.scanner.UsedJavaProjectCheck;

/**
 * Shows the Marker Resolutions for KB Problem Marker
 * 
 * @author Victor Rubezhny
 *
 */
public class KBProblemMarkerResolutionGenerator implements IMarkerResolutionGenerator {
	public IMarkerResolution[] getResolutions(IMarker marker) {
		try {
			if(!KbBuilderMarker.KB_BUILDER_PROBLEM_MARKER_TYPE.equals(marker.getType())) {
				return new IMarkerResolution[0];
			}
		} catch (CoreException e) {
			WebKbPlugin.getDefault().logError(e);
		}
		EnableKBOnProject resolution = new EnableKBOnProject();
		if(marker.getAttribute(KbBuilderMarker.ATTR_KIND, 1) == KbBuilderMarker.KIND_DEPENDS_ON_NON_KB_POJECTS) {
			try {
				UsedJavaProjectCheck check = new UsedJavaProjectCheck();
				List<IProject> list = check.getNonKbJavaProjects(marker.getResource().getProject());
				String messageId = list.size() == 1 ? KbMessages.ENABLE_KB_ON_SINGLE : KbMessages.ENABLE_KB_ON_MANY;
				String projectList = check.asText(list);
				String label = NLS.bind(messageId, projectList);
				resolution.setLabel(label);
			} catch (CoreException e) {
				WebKbPlugin.getDefault().logError(e);
			}
		} else {
			resolution.setLabel(KbMessages.ENABLE_KB);
		}
		return new IMarkerResolution[] {
			resolution
		};
	}
}
/*************************************************************************************
 * Copyright (c) 2011 JBoss by Red Hat and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     JBoss by Red Hat - Initial implementation.
 ************************************************************************************/
package org.jboss.tools.jst.web.kb.test.validation;

import junit.framework.TestCase;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.ide.IDE;
import org.eclipse.wst.validation.internal.plugin.ValidationPlugin;
import org.jboss.tools.jst.web.kb.internal.validation.ValidatorManager;
import org.jboss.tools.test.util.JobUtils;

public class BuilderOrderMarkerResolutionTest extends TestCase {

	IProject project = null;
	
	public void setUp() throws Exception {
		project = ResourcesPlugin.getWorkspace().getRoot().getProject("TestBrokenBuilderOrder");
		assertNotNull("Can't load TestBrokenBuilderOrder", project); //$NON-NLS-1$
	}

	private void checkResolution(IProject project, String markerType, String resolutionClassName) throws CoreException {
		try{
			IMarker[] markers = project.findMarkers(ValidationPlugin.VALIDATION_BUILDER_ID, true, IResource.DEPTH_ZERO);
			for (int i = 0; i < markers.length; i++) {
				IMarker marker = markers[i];
				IMarkerResolution[] resolutions = IDE.getMarkerHelpRegistry().getResolutions(marker);
				for (int j = 0; j < resolutions.length; j++) {
					IMarkerResolution resolution = resolutions[j];
					if (resolution.getClass().getName().equals(resolutionClassName)) {
						resolution.run(marker);
						JobUtils.waitForIdle();
						IMarker[] newMarkers = project.findMarkers(markerType, true,	IResource.DEPTH_INFINITE);
						assertTrue("Marker resolution did not decrease number of problems. was: "+markers.length+" now: "+newMarkers.length, newMarkers.length < markers.length);
						return;
					}
					fail("Marker resolution: "+resolutionClassName+" not found");
				}
			}
		} finally {
			JobUtils.waitForIdle();
//			TestUtil.waitForValidation(project);
		}
	}

	public void testBuilderOrderResolution() throws CoreException {
		checkResolution(project,
				ValidatorManager.ORDER_PROBLEM_MARKER_TYPE,
				"org.jboss.tools.jst.web.kb.internal.validation.BuilderOrderResolution");
	}

}

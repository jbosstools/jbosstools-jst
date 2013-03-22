/*************************************************************************************
 * Copyright (c) 2011 Red Hat, Inc. and others.
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
import org.jboss.tools.common.base.test.validation.TestUtil;
import org.jboss.tools.jst.web.kb.internal.validation.KBValidator;
import org.jboss.tools.test.util.JobUtils;

public class BuilderOrderMarkerResolutionTest extends TestCase {

	IProject project = null;

	@Override
	public void setUp() throws Exception {
		project = ResourcesPlugin.getWorkspace().getRoot().getProject("TestBrokenBuilderOrder");
		assertTrue("Can't load TestBrokenBuilderOrder", project.exists()); //$NON-NLS-1$
	}

	private void checkResolution(IProject project, String markerType, String resolutionClassName) throws CoreException {
		try{
			IMarker[] markers = getBuilderOrderMarkers();
			assertTrue(markers.length > 0);
			for (int i = 0; i < markers.length; i++) {
				IMarker marker = markers[i];
				IMarkerResolution[] resolutions = IDE.getMarkerHelpRegistry().getResolutions(marker);
				for (int j = 0; j < resolutions.length; j++) {
					IMarkerResolution resolution = resolutions[j];
					if (resolution.getClass().getName().equals(resolutionClassName)) {
						resolution.run(marker);
						TestUtil._waitForValidation(project);
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

	private IMarker[] getBuilderOrderMarkers() throws CoreException {
		return project.findMarkers(KBValidator.ORDER_PROBLEM_MARKER_TYPE, true, IResource.DEPTH_ZERO);
	}

	public void testBuilderOrderResolution() throws CoreException {
		checkResolution(project,
				KBValidator.ORDER_PROBLEM_MARKER_TYPE,
				"org.jboss.tools.jst.web.kb.internal.validation.BuilderOrderResolution");
	}
}
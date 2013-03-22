/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.test.validation;

import junit.framework.TestCase;

import org.eclipse.core.internal.preferences.EclipsePreferences;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.jboss.tools.common.base.test.validation.TestUtil;
import org.jboss.tools.common.preferences.SeverityPreferences;
import org.jboss.tools.jst.web.kb.internal.validation.KBValidator;
import org.jboss.tools.jst.web.kb.preferences.ELSeverityPreferences;

/**
 * @author Alexey Kazakov
 */
public class BuilderOrderValidationTest extends TestCase {

	IProject project = null;

	@Override
	public void setUp() throws Exception {
		project = ResourcesPlugin.getWorkspace().getRoot().getProject("TestBrokenBuilderOrder");
		assertTrue("Can't load TestBrokenBuilderOrder", project.exists()); //$NON-NLS-1$
	}

	private IMarker[] getBuilderOrderMarkers() throws CoreException {
		return project.findMarkers(KBValidator.ORDER_PROBLEM_MARKER_TYPE, true, IResource.DEPTH_ZERO);
	}

	public void testWrongBuildOrderPreference() throws CoreException {
		IMarker[] markers = getBuilderOrderMarkers();
		assertEquals(1, markers.length);
		assertEquals(IMarker.SEVERITY_ERROR, markers[0].getAttribute(IMarker.SEVERITY, -1));

		modifyPreference(SeverityPreferences.IGNORE);
		markers = getBuilderOrderMarkers();
		assertEquals(0, markers.length);

		modifyPreference(SeverityPreferences.WARNING);
		markers = getBuilderOrderMarkers();
		assertEquals(1, markers.length);
		assertEquals(IMarker.SEVERITY_WARNING, markers[0].getAttribute(IMarker.SEVERITY, -1));

		modifyPreference(SeverityPreferences.ERROR);
		markers = getBuilderOrderMarkers();
		assertEquals(1, markers.length);
		assertEquals(IMarker.SEVERITY_ERROR, markers[0].getAttribute(IMarker.SEVERITY, -1));
	}

	void modifyPreference(String value) throws CoreException {
		EclipsePreferences ps = (EclipsePreferences)ELSeverityPreferences.getInstance().getProjectPreferences(project);
		ps.put(ELSeverityPreferences.WRONG_BUILDER_ORDER_PREFERENCE_NAME, value);
		TestUtil._waitForValidation(project);
	}
}
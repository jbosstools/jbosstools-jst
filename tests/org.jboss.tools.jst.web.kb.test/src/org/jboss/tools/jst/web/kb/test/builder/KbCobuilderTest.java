/*************************************************************************************
 * Copyright (c) 2015 Red Hat, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     JBoss by Red Hat - Initial implementation.
 ************************************************************************************/
package org.jboss.tools.jst.web.kb.test.builder;

import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.jboss.tools.jst.web.kb.KbProjectFactory;
import org.jboss.tools.jst.web.kb.internal.KbProject;
import org.jboss.tools.jst.web.kb.taglib.ITagLibrary;
import org.jboss.tools.test.util.JobUtils;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class KbCobuilderTest extends TestCase {
	IProject project = null;

	public KbCobuilderTest() {
		super("Kb co-builder Test");
	}

	public void setUp() throws Exception {
		project = ResourcesPlugin.getWorkspace().getRoot().getProject("TestKbModel2");
		assertNotNull("Can't load TestKbModel2", project); //$NON-NLS-1$
	}

	public void testCobuilder() throws Exception {
		KbProject kb = (KbProject)KbProjectFactory.getKbProject(project, true);
		assertNotNull(kb);
		assertEquals(KbCobuilder.TEST_MODEL_OBJECT, kb.getExtensionModel(KbCobuilder.TEST_MODEL_ID));
	}

}

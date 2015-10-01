/******************************************************************************* 
 * Copyright (c) 2015 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.jst.js.node.test;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;

import junit.framework.TestCase;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */

public class NodeUITestCase extends TestCase {
	private IProject testProject;
	private static SWTWorkbenchBot bot = new SWTWorkbenchBot();

	@Override
	protected void setUp() {
		this.testProject = ResourcesPlugin.getWorkspace().getRoot().getProject("TestProject"); //$NON-NLS-1$
	}

	public void testProject() {
		assertNotNull("Can't load TestProject", this.testProject); //$NON-NLS-1$
		assertTrue(this.testProject.exists());
	}
	
	public static SWTBotView getProjectExplorer() {
		SWTBotView view = bot.viewByTitle("Project Explorer"); //$NON-NLS-1$
		return view;
	}
	
}
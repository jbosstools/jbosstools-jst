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
package org.jboss.tools.js.test;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.ContextMenuHelper;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;

import junit.framework.TestCase;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */

public class BowerUITestCase extends TestCase {
	private IProject testProject;
	private static SWTWorkbenchBot bot = new SWTWorkbenchBot();

	@Override
	protected void setUp() {
		this.testProject = ResourcesPlugin.getWorkspace().getRoot().getProject("TestBowerProject"); //$NON-NLS-1$
	}

	public void testProject() {
		assertNotNull("Can't load TestBowerProject", this.testProject); //$NON-NLS-1$
		assertTrue(this.testProject.exists());
	}

	public void testBowerUpdateShortcutAvailability() {
		SWTBotView packageExplorer = getProjectExplorer();
		SWTBotTree tree = packageExplorer.bot().tree();
		packageExplorer.show();
		String testProjectName = this.testProject.getName();
		assertTrue("Project does not exist", isProjectCreated(testProjectName)); //$NON-NLS-1$
		tree.select(testProjectName);
		SWTBotMenu bowerUpdate = new SWTBotMenu(ContextMenuHelper.contextMenu(tree, "Run As", "Run Configurations...")); //$NON-NLS-1$ //$NON-NLS-2$
//		SWTBotMenu bowerUpdate = new SWTBotMenu(ContextMenuHelper.contextMenu(tree, "Run As", "Bower Update")); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(bowerUpdate.isVisible());
	}
	
	private static SWTBotView getProjectExplorer() {
		SWTBotView view = bot.viewByTitle("Project Explorer"); //$NON-NLS-1$
		return view;
	}

	private boolean isProjectCreated(String name) {
		try {
			SWTBotView packageExplorer = getProjectExplorer();
			SWTBotTree tree = packageExplorer.bot().tree();
			tree.getTreeItem(name);
			return true;
		} catch (WidgetNotFoundException e) {
			return false;
		}
	}

}
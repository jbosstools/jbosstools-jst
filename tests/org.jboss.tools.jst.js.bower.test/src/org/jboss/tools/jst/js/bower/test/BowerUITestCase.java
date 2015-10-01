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
package org.jboss.tools.jst.js.bower.test;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
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
	
	public void testbowerInitWizard() {
		SWTBotView packageExplorer = getProjectExplorer();
		SWTBotTree tree = packageExplorer.bot().tree();
		packageExplorer.show();
		String testProjectName = this.testProject.getName();
		assertTrue("Project does not exist", isProjectCreated(testProjectName)); //$NON-NLS-1$
		tree.select(testProjectName);
				
		bot.menu("File").menu("New").menu("Other...").click();  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		
		SWTBotShell shell = bot.shell("New"); //$NON-NLS-1$
		shell.activate();
		
		bot.text().setText("Bower"); //$NON-NLS-1$
		bot.tree().expandNode("JavaScript").select("Bower Init"); //$NON-NLS-1$ //$NON-NLS-2$
		bot.button("Next >").click(); //$NON-NLS-1$
		
		String name = bot.textWithLabel("Name:").getText(); //$NON-NLS-1$
		assertEquals(testProjectName, name);
		
		String version = bot.textWithLabel("Version:").getText(); //$NON-NLS-1$
		assertEquals("0.0.0", version); //$NON-NLS-1$
		
		// bower.json already exists -> Finish must be disabled
		assertFalse(bot.button("Finish").isEnabled()); //$NON-NLS-1$
		bot.button("Cancel").click(); //$NON-NLS-1$
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
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
package org.jboss.tools.jst.js.npm.test;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.waits.Conditions;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;

import junit.framework.TestCase;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */

public class NpmUITestCase extends TestCase {
	private IProject testProject;
	private static SWTWorkbenchBot bot = new SWTWorkbenchBot();

	@Override
	protected void setUp() {
		this.testProject = ResourcesPlugin.getWorkspace().getRoot().getProject("TestNpmProject"); //$NON-NLS-1$
	}

	public void testProject() {
		assertNotNull("Can't load TestNpmProject", this.testProject); //$NON-NLS-1$
		assertTrue(this.testProject.exists());
	}
	
	public static SWTBotView getProjectExplorer() {
		SWTBotView view = bot.viewByTitle("Project Explorer"); //$NON-NLS-1$
		return view;
	}
	
	public void testNpmInitWizard() {
		SWTBotView packageExplorer = getProjectExplorer();
		packageExplorer.show();
		SWTBotTree tree = packageExplorer.bot().tree();
		final String testProjectName = this.testProject.getName();
		assertTrue("Project does not exist", isProjectCreated(testProjectName)); //$NON-NLS-1$
		tree.expandNode(testProjectName, "WebContent").getNode("pages").select(); //$NON-NLS-1$ //$NON-NLS-2$

		bot.menu("File").menu("New").menu("Other...").click();  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		
		bot.waitUntil(Conditions.shellIsActive("New"), NpmAllTests.DEFAULT_TIMEOUT); //$NON-NLS-1$
		
		bot.text().setText("npm"); //$NON-NLS-1$
		bot.tree().expandNode("JavaScript").select("npm Init"); //$NON-NLS-1$ //$NON-NLS-2$
		bot.button("Next >").click(); //$NON-NLS-1$
		bot.waitUntil(new DefaultCondition() {
			@Override
			public boolean test() throws Exception {
				return bot.textWithLabel("Name:").getText() != testProjectName; //$NON-NLS-1$
			}
			@Override
			public String getFailureMessage() {
				return "Field content was not updated to " + testProjectName; //$NON-NLS-1$
			}
		},NpmAllTests.DEFAULT_TIMEOUT);

		String version = bot.textWithLabel("Version:").getText(); //$NON-NLS-1$
		assertEquals("0.0.0", version); //$NON-NLS-1$
		
		// package.json already exists -> Finish must be disabled
		assertFalse(bot.button("Finish").isEnabled()); //$NON-NLS-1$
		bot.button("Cancel").click(); //$NON-NLS-1$
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
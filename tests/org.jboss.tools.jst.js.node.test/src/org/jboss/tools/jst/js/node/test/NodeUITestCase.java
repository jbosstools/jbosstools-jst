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

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.waits.Conditions;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.jboss.tools.common.util.PlatformUtil;

import junit.framework.TestCase;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */

public class NodeUITestCase extends TestCase {
	private IProject testProject;
	private static SWTWorkbenchBot bot = new SWTWorkbenchBot();

	@Override
	protected void setUp() {
		this.testProject = ResourcesPlugin.getWorkspace().getRoot().getProject("TestNodeProject"); //$NON-NLS-1$
	}

	public void testProject() {
		assertNotNull("Can't load TestNodeProject", this.testProject); //$NON-NLS-1$
		assertTrue(this.testProject.exists());
	}
	
	public static SWTBotView getProjectExplorer() {
		SWTBotView view = bot.viewByTitle("Project Explorer"); //$NON-NLS-1$
		return view;
	}
	
	// JBIDE-20989 Preference validation fails on windows if node executable called node64.exe
	public void testNode64ExePreferenceValidation() {
		getProjectExplorer().show();
		bot.menu("Window").menu("Preferences").click(); //$NON-NLS-1$ //$NON-NLS-2$
		bot.waitUntil(Conditions.shellIsActive("Preferences"), NodeAllTests.DEFAULT_TIMEOUT); //$NON-NLS-1$
		bot.tree().expandNode("JavaScript Tools").select("Node"); //$NON-NLS-1$ //$NON-NLS-2$
		
		IFolder nodeInstallationFolder = testProject.getFolder(getNodeInstallationFolder());
		assertTrue("JBIDE-20989 folder does not exist", nodeInstallationFolder.exists()); //$NON-NLS-1$
		
		SWTBotText locationInput = bot.textWithLabel("Node Location"); //$NON-NLS-1$
		bot.waitUntil(Conditions.widgetIsEnabled(locationInput), NodeAllTests.DEFAULT_TIMEOUT);
		locationInput.setFocus();
				
		locationInput.setText("").typeText(nodeInstallationFolder.getLocation().toOSString(), 20); //$NON-NLS-1$
		SWTBotButton ok = bot.button("OK"); //$NON-NLS-1$
		
		bot.waitUntil(Conditions.widgetIsEnabled(ok), NodeAllTests.DEFAULT_TIMEOUT);
		assertTrue("Valid node home folder - 'OK' must be enabled", ok.isEnabled()); //$NON-NLS-1$ 
		
		locationInput.setText("").typeText(testProject.getLocation().toOSString(), 20); //$NON-NLS-1$ 
		assertFalse("Not valid node home folder - 'OK' must be disabled", ok.isEnabled()); //$NON-NLS-1$
	}
	
	private String getNodeInstallationFolder() {
		String folder = null;
		switch(PlatformUtil.getOs()) {
			case WINDOWS:
				folder = "JBIDE-20989/Windows";	 //$NON-NLS-1$
				break;
				
			case MACOS:
				folder = "JBIDE-20989/Mac"; //$NON-NLS-1$
				break;
				
			case LINUX:
				folder = "JBIDE-20989/Linux"; //$NON-NLS-1$
				break;

			case OTHER: 
				// 'node' executable name is default (Mac case) 
				folder = "JBIDE-20989/Mac"; //$NON-NLS-1$
		}
		return folder;
	}
	
}
/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.test;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

/**
 * @author Alexey Kazakov
 */
public class TestNature implements IProjectNature {

	IProject project;
	static String ID = "org.jboss.tools.jst.web.test.testnature";
	static boolean APPEND_BUILDER = true;

	public void configure() throws CoreException {
		addToBuildSpec(TestBuilder.ID);
	}

	public void deconfigure() throws CoreException {
	}

	public IProject getProject() {
		return project;
	}

	public void setProject(IProject project) {
		this.project = project;
	}

	protected void addToBuildSpec(String builderID) throws CoreException {
		IProjectDescription description = getProject().getDescription();
		ICommand command = null;
		ICommand commands[] = description.getBuildSpec();
		for (int i = 0; i < commands.length && command == null; ++i) {
			if (commands[i].getBuilderName().equals(builderID)) 
				command = commands[i];
		}
		if (command == null) {
			command = description.newCommand();
			command.setBuilderName(builderID);
			ICommand[] oldCommands = description.getBuildSpec();
			ICommand[] newCommands = new ICommand[oldCommands.length + 1];

			if(APPEND_BUILDER) {
				System.arraycopy(oldCommands, 0, newCommands, 0, oldCommands.length);
				newCommands[oldCommands.length] = command;
			} else {
				System.arraycopy(oldCommands, 0, newCommands, 1, oldCommands.length);
				newCommands[0] = command;
			}
			description.setBuildSpec(newCommands);
			getProject().setDescription(description, null);
		}
	}
}
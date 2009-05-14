/******************************************************************************* 
 * Copyright (c) 2009 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.internal;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.jboss.tools.jst.web.kb.IKbProject;
import org.jboss.tools.jst.web.kb.taglib.ITagLibrary;

/**
 * 
 * @author V.Kabanovich
 *
 */
public class KbProject implements IKbProject {
	IProject project;

	ITagLibrary[] tagLibraries = new ITagLibrary[0];

	public ITagLibrary[] getTagLibraries() {
		return tagLibraries;
	}

	public void configure() throws CoreException {
		addToBuildSpec(KbBuilder.BUILDER_ID);
	}

	public void deconfigure() throws CoreException {
		removeFromBuildSpec(KbBuilder.BUILDER_ID);
	}

	public IProject getProject() {
		return project;
	}

	public void setProject(IProject project) {
		this.project = project;
	}

	/**
	 * 
	 * @param builderID
	 * @throws CoreException
	 */
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
			System.arraycopy(oldCommands, 0, newCommands, 0, oldCommands.length);
			newCommands[oldCommands.length] = command;
			description.setBuildSpec(newCommands);
			getProject().setDescription(description, null);
		}
	}

	static String EXTERNAL_TOOL_BUILDER = "org.eclipse.ui.externaltools.ExternalToolBuilder";
	static final String LAUNCH_CONFIG_HANDLE = "LaunchConfigHandle";
	
	/**
	 * 
	 * @param builderID
	 * @throws CoreException
	 */
	protected void removeFromBuildSpec(String builderID) throws CoreException {
		IProjectDescription description = getProject().getDescription();
		ICommand[] commands = description.getBuildSpec();
		for (int i = 0; i < commands.length; ++i) {
			String builderName = commands[i].getBuilderName();
			if (!builderName.equals(builderID)) {
				if(!builderName.equals(EXTERNAL_TOOL_BUILDER)) continue;
				Object handle = commands[i].getArguments().get(LAUNCH_CONFIG_HANDLE);
				if(handle == null || handle.toString().indexOf(builderID) < 0) continue;
			}
			ICommand[] newCommands = new ICommand[commands.length - 1];
			System.arraycopy(commands, 0, newCommands, 0, i);
			System.arraycopy(commands, i + 1, newCommands, i, commands.length - i - 1);
			description.setBuildSpec(newCommands);
			getProject().setDescription(description, null);
			return;
		}
	}

}

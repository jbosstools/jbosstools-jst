/*******************************************************************************
 * Copyright (c) 2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *       Red Hat, Inc. - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.jst.jsdt.utils;

import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import tern.eclipse.ide.core.IDETernProject;
import tern.server.TernPlugin;

/**
 * 
 * @author Victor Rubezhny
 *
 */
public class ConfigUtils {
	
	/**
	 * Configures the CordovaJS plug-in, so it could be shown in Content Assistant
	 *  
	 * @throws IOException 
	 * @throws CoreException 
	 * 
	 * @param project
	 */
	public static void enableCordovaJSPlugin(IProject project) throws CoreException, IOException {
		IDETernProject ternProject = IDETernProject.getTernProject(project);
		if (!ternProject.hasPlugin(TernPlugin.cordovajs)) {
			ternProject.addPlugin(TernPlugin.cordovajs);
		}
		ternProject.save();
	}
}

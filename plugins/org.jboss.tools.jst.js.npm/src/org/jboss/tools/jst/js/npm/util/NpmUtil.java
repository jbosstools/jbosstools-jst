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
package org.jboss.tools.jst.js.npm.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.jboss.tools.jst.js.node.util.PlatformUtil;
import org.jboss.tools.jst.js.node.util.WorkbenchResourceUtil;
import org.jboss.tools.jst.js.npm.PackageJson;
import org.jboss.tools.jst.js.npm.internal.NpmConstants;
import org.jboss.tools.jst.js.npm.internal.preference.NpmPreferenceHolder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public final class NpmUtil {

	private NpmUtil() {
	}

	public static boolean isPackageJsonExist(final IProject project) throws CoreException {
		IFile packageJson = null;
		if (project != null && project.exists()) {
			packageJson = WorkbenchResourceUtil.findFileRecursively(project, NpmConstants.PACKAGE_JSON);
		}
		return (packageJson != null && packageJson.exists());
	}

	public static boolean hasPackageJson(final IFolder folder) throws CoreException {
		IResource packageJson = folder.findMember(NpmConstants.PACKAGE_JSON);
		return (packageJson != null && packageJson.exists());
	}

	public static boolean isPackageJson(final IResource resource) {
		return (resource != null && NpmConstants.PACKAGE_JSON.equals(resource.getName()) && resource.exists());
	}

	public static String generateJson(PackageJson packageJson) {
		Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		return gson.toJson(packageJson);
	}
	
	
	/**
	 * @return absolute path to directory in which native npm call must be performed. Basically, the method scans 
	 * project for package.json file and returns it's parent, ignoring "node_modules"
	 * @throws CoreException
	 */
	public static String getNpmWorkingDir(IProject project, final String... ignores) throws CoreException {
		String workingDir = null;
		final List<IFile> foundFiles = new ArrayList<>();
		if (project != null && project.exists()) {
			project.accept(new IResourceVisitor() {

				@Override
				public boolean visit(IResource resource) throws CoreException {
					if (!foundFiles.isEmpty()) {
						return false;
					} else if (resource.getType() == IResource.FOLDER && ignores != null) {
						for (String ignore : ignores) {
							if (resource.getName().equals(ignore)) {
								return false;
							}
						}
					} else if (resource.getType() == IResource.FILE
							&& NpmConstants.PACKAGE_JSON.equals(resource.getName())) {
						foundFiles.add((IFile) resource);
					}
					return true;
				}
			});
		}
		if (!foundFiles.isEmpty()) {
			workingDir = foundFiles.get(0).getParent().getFullPath().toOSString();
		}
		return workingDir;
	}
		
	public static String getNpmExecutableLocation() {
		String npmExecutableLocation = null;
		File npmExecutable = new File(NpmPreferenceHolder.getNpmLocation(), NpmUtil.getNpmExecutableName());
		if (npmExecutable != null && npmExecutable.exists()) {
			npmExecutableLocation = npmExecutable.getAbsolutePath();
		}
		return npmExecutableLocation;
	}
	
	public static String getNpmExecutableName() {
		String name = null;
		switch(PlatformUtil.getOs()) {
			case WINDOWS:
				name = NpmConstants.NPM_CLI_JS;	
				break;
				
			case MACOS:
				name = NpmConstants.NPM;
				break;
				
			case LINUX:
				name = NpmConstants.NPM;
				break;
			
			case OTHER:
				name = NpmConstants.NPM;
				break;
		}
		return name;
	}

}
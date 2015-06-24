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
package org.jboss.tools.jst.js.bower.internal.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.jboss.tools.jst.js.bower.BowerJson;
import org.jboss.tools.jst.js.bower.internal.BowerConstants;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Ilya Buziuk (ibuziuk)
 */
public final class BowerUtil {

	private BowerUtil() {
	}

	public static boolean isBowerJsonExist(final IProject project) throws CoreException {
		final List<IFile> foundFiles = new ArrayList<>();
		if (project != null) {
			project.accept(new IResourceVisitor() {
				@Override
				public boolean visit(IResource resource) throws CoreException {
					if (resource.getType() == IResource.FILE && BowerConstants.BOWER_JSON.equals(resource.getName())) {
						foundFiles.add((IFile) resource);
						return false;
					}
					return true;
				}
			});
		}
		return !foundFiles.isEmpty();
	}

	public static boolean hasBowerJson(final IFolder folder) throws CoreException {
		IResource bowerJson = folder.findMember(BowerConstants.BOWER_JSON);
		return (bowerJson != null && bowerJson.exists());
	}

	public static boolean isBowerJson(final IResource resource) {
		return (resource != null && BowerConstants.BOWER_JSON.equals(resource.getName()) && resource.exists());
	}

	public static String generateJson(BowerJson bowerJson) {
		Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		return gson.toJson(bowerJson);
	}

}
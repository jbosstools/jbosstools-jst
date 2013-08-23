/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.internal.taglib.html;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.jboss.tools.common.EclipseUtil;
import org.jboss.tools.jst.web.kb.WebKbPlugin;
import org.jboss.tools.jst.web.kb.internal.taglib.CustomTagLibAttribute;

/**
 * @author Alexey Kazakov
 */
public abstract class FileNameAttributeProvider extends DynamicAttributeValueProvider {

	@Override
	protected CustomTagLibAttribute getAttribute() {
		IFile file = context.getResource();
		IProject project = file.getProject();
		Set<IResource> ignore = new HashSet<IResource>();
		IResource[] sourceRoots = EclipseUtil.getJavaSourceRoots(project);
		if(sourceRoots!=null && sourceRoots.length>0) {
			for (IResource resource : sourceRoots) {
				ignore.add(resource);
			}
			IJavaProject javaProject = EclipseUtil.getJavaProject(project);
			try {
				IPath path = javaProject.getOutputLocation();
				IResource outputResource = project.getWorkspace().getRoot().findMember(path);
				if(outputResource!=null) {
					ignore.add(outputResource);
				}
			} catch (JavaModelException e) {
				WebKbPlugin.getDefault().logError(e);
			}
		}

		List<String> result = new ArrayList<String>();
		IContainer parent = file.getParent();
		if(parent!=null) {
			try {
				addParent(file, "", result, ignore);
			} catch (CoreException e) {
				WebKbPlugin.getDefault().logError(e);
			}
		}
		FileNameAttribute attribute = null;
		if(!result.isEmpty()) {
			String[] paths = result.toArray(new String[result.size()]);
			attribute = new FileNameAttribute(getAttributeName(), "", paths);
		}

		return attribute;
	}

	private void addChildren(IContainer root, String pathToRoot, List<String> result, Set<IResource> ignore) throws CoreException {
		IResource[] members = root.members(IContainer.EXCLUDE_DERIVED);
		for (IResource resource : members) {
			if(!ignore.contains(resource)) {
				if(resource.getType() == IResource.FOLDER) {
					ignore.add(resource);
					addChildren((IContainer)resource, pathToRoot + resource.getName() + "/", result, ignore);
				} else if(match(resource)) {
					result.add(pathToRoot + resource.getName());
				}
			}
		}
	}

	private void addParent(IResource child, String pathToRoot, List<String> result, Set<IResource> ignore) throws CoreException {
		IContainer parent = child.getParent();
		if(parent!=null && (parent.getType()==IResource.FOLDER || parent.getType()==IResource.PROJECT) && !ignore.contains(parent)) {
			ignore.add(parent);
			addChildren(parent, pathToRoot, result, ignore);
			addParent(parent, pathToRoot + "../", result, ignore);
		}
	}

	protected abstract String getAttributeName();

	protected abstract boolean match(IResource resource);
}
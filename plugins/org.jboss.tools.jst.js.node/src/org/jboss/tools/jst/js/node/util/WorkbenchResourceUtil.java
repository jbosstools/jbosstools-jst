/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *       Red Hat, Inc. - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.jst.js.node.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.ide.ResourceUtil;
import org.eclipse.ui.part.FileEditorInput;
import org.jboss.tools.jst.js.node.Activator;

/**
 * @author Ilya Buziuk (ibuziuk)
 */
public final class WorkbenchResourceUtil {

	private WorkbenchResourceUtil() {
	}

	public static void openInEditor(final IFile file, String editorID) throws PartInitException {
		IEditorRegistry editorRegistry = PlatformUI.getWorkbench().getEditorRegistry();
		if (editorID == null || editorRegistry.findEditor(editorID) == null) {
			editorID = PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(file.getFullPath().toString()).getId();
		}

		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		page.openEditor(new FileEditorInput(file), editorID, true, IWorkbenchPage.MATCH_ID);
	}
	
	

	public static void createFile(final IFile file, final String content) throws CoreException {
		if (!file.exists()) {
			InputStream source = new ByteArrayInputStream(content.getBytes());
			try {
				file.create(source, IResource.NONE, null);
			} catch (CoreException e) {
				Activator.logError(e);
			}
		}
	}
	
	public static void updateFile(IFile file, String content) throws CoreException {
		if (file.exists()) {
			InputStream source = new ByteArrayInputStream(content.getBytes());
			file.setContents(source, true, true, new NullProgressMonitor());
		}
	}

	public static IProject getSelectedProject() {
		IWorkbenchWindow workbenchWindow = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow();
		if (workbenchWindow != null) {
			IWorkbenchPage activePage = workbenchWindow.getActivePage();
			if (activePage != null) {
				ISelection selection = activePage.getSelection();

				if (selection instanceof StructuredSelection) {
					StructuredSelection structuredSelection = (StructuredSelection) selection;
					Object firstElement = structuredSelection.getFirstElement();
					IResource resource = ResourceUtil.getResource(firstElement);
					if (resource != null) {
						return resource.getProject();
					}
				}
			}
		}
		return null;
	}

	public static IProject getProject(String projectString) {
		if (projectString != null) {
			try {
				IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectString);
				if (project != null && project.exists()) {
					return project;
				}
			} catch (IllegalArgumentException e) {
			}
		}

		return null;
	}
	
	public static IContainer getContainerFromSelection(IStructuredSelection selection) {
		IContainer container = null;
		if (selection != null && !selection.isEmpty()) {
			Object selectedObject = selection.getFirstElement();
			if (selectedObject instanceof IContainer) {
				container = (IContainer) selectedObject;
			} else if (selectedObject instanceof IFile) {
				container = ((IFile) selectedObject).getParent();
			}
		}
		return container;
	}
	
	public static String getAbsolutePath(IResource resource) {
		IPath path = null;
		String absoluteLocation = null;
		if (resource != null) {
			path = resource.getRawLocation();
			path = (path != null) ? path : resource.getLocation();
			if (path != null) {
				absoluteLocation = path.toOSString();
			}
		}
		return absoluteLocation;
	}

	public static void showConsoleView() throws PartInitException {
		IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (activeWorkbenchWindow != null) {
			IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
			if (activePage != null) {
				activePage.showView(IConsoleConstants.ID_CONSOLE_VIEW);
			}
		}
	}
	
	public static IFile findFileRecursively(IContainer container, String name) throws CoreException {
		for (IResource r : container.members()) {
			if (r instanceof IContainer) {
				IFile file = findFileRecursively((IContainer) r, name);
				if (file != null && file.exists()) {
					return file;
				}
			} else if (r instanceof IFile && r.getName().equals(name) && r.exists()) {
				return (IFile) r;
			}
		}
		return null;
	}
	
}

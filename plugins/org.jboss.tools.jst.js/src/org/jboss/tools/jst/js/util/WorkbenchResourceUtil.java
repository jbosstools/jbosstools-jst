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
package org.jboss.tools.jst.js.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.ResourceUtil;
import org.eclipse.ui.part.FileEditorInput;
import org.jboss.tools.jst.js.internal.Activator;

/**
 * @author Ilya Buziuk (ibuziuk)
 */
public final class WorkbenchResourceUtil {

	private WorkbenchResourceUtil() {
	}

	public static void openInEditor(final IFile file) throws PartInitException {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				IEditorDescriptor desc = PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(file.getName());
				try {
					page.openEditor(new FileEditorInput(file), desc.getId());
				} catch (PartInitException e) {
					Activator.logError(e);
				}
			}
		});

	}

	public static void createFile(IFile file, String content) throws CoreException {
		if (!file.exists()) {
			InputStream source = new ByteArrayInputStream(content.getBytes());
			file.create(source, IResource.NONE, null);
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

}

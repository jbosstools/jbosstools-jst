/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.jsp.outline.cssdialog.common;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.jboss.tools.jst.jsp.JspEditorPlugin;

/**
 * Class for creates filter by file extensions
 * 
 * @author dsakovich
 */
public class FileExtensionFilter extends ViewerFilter {

	private String[] fTargetExtension;

	public FileExtensionFilter(String[] targetExtension) {
	    fTargetExtension = targetExtension;
	}

	public boolean select(Viewer viewer, Object parent, Object element) {
	    if (element instanceof IFile) {
		for (int i = 0; i < fTargetExtension.length; i++) {
		    if (((IFile) element).getName().toLowerCase().endsWith(
			    "." + fTargetExtension[i])) { //$NON-NLS-1$
			return true;
		    }
		}
		return false;
	    } if (element instanceof IProject && ((IProject) element).isOpen())
		return true;
	    if (element instanceof IContainer) { // i.e. IProject, IFolder
		try {
		    IResource[] resources = ((IContainer) element).members();
		    for (int i = 0; i < resources.length; i++) {
			if (select(viewer, parent, resources[i]))
			    return true;
		    }
		} catch (CoreException e) {
		    JspEditorPlugin.getPluginLog().logError(e);
		}
	    }
	    return false;
	}
}
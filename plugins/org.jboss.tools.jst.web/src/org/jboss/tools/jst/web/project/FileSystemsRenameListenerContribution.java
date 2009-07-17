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
package org.jboss.tools.jst.web.project;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.swt.widgets.Display;
import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.filesystems.impl.FileSystemsRenameListener;
import org.jboss.tools.jst.web.WebModelPlugin;

public class FileSystemsRenameListenerContribution implements FileSystemsRenameListener.Contribution {

	//probably this contribution is no more needed at all
	public void pathRenamed(final FileSystemsRenameListener listener, String oldPath, String newPath) {
//		final IProject project = (IProject)listener.getFileSystems().getModel().getProperties().get("project");
//		if(project == null) return;
//		String webroot = getWebRootWTP(listener.getFileSystems().getModel());

//		if(webroot != null && webroot.toLowerCase().equals(oldPath.toLowerCase())) {
//			final String webrootname = newPath.substring(newPath.lastIndexOf('/') + 1);
//			Display.getDefault().asyncExec(new Runnable() {
//				public void run() {
//					try {
//						updateWebContentNamePropertiesOnly(project, webrootname, null);
//					} catch (Exception e) {
//						WebModelPlugin.getPluginLog().logError(e);
//					}
//				}
//			});
//		}
	}
	
	private String getWebRootWTP(XModel model) {
		throw new RuntimeException("FileSystemsRenameListenerContribution.getWebRootWTP(XModel model) migration to WTP I-build is needed."); //$NON-NLS-1$
	}
	
	/*
	 * only for comparison
	 */
	public static void updateWebContentNamePropertiesOnly(IProject project, String webContentName,IProgressMonitor progressMonitor) throws CoreException {
		throw new RuntimeException("FileSystemsRenameListenerContribution.updateWebContentNamePropertiesOnly(IProject project, String webContentName,IProgressMonitor progressMonitor) migration to WTP I-build is needed."); //$NON-NLS-1$
	}

}

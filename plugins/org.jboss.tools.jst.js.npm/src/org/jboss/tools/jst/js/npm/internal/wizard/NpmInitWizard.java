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
package org.jboss.tools.jst.js.npm.internal.wizard;

import org.eclipse.core.internal.resources.Container;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.jboss.tools.jst.js.npm.PackageJson;
import org.jboss.tools.jst.js.npm.NpmPlugin;
import org.jboss.tools.jst.js.npm.internal.NpmConstants;
import org.jboss.tools.jst.js.npm.internal.Messages;
import org.jboss.tools.jst.js.npm.util.NpmUtil;
import org.jboss.tools.jst.js.node.util.WorkbenchResourceUtil;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
@SuppressWarnings("restriction")
public class NpmInitWizard extends Wizard implements INewWizard {
	private IStructuredSelection selection;
	private NpmInitPage npmInitPage;

	public NpmInitWizard() {
		super();
		setDefaultPageImageDescriptor(NpmPlugin.getImageDescriptor("/icons/npm_75.png")); //$NON-NLS-1$
		setWindowTitle(Messages.NpmInitWizard_WindowTitle);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
	
	@Override
	public void addPages() {
		super.addPages();
		npmInitPage = new NpmInitPage(selection);
		addPage(npmInitPage);
	}

	@Override
	public boolean performFinish() {
		PackageJson packageJson = npmInitPage.getModel();
		String dir = npmInitPage.getExecutionDir();
		try {
			IContainer root = ResourcesPlugin.getWorkspace().getRoot().getContainerForLocation(new Path(dir));
			if (root != null && root.exists()) {
				IFile file = ((Container) root).getFile(NpmConstants.PACKAGE_JSON);
				if (!file.exists()) {
					String json = NpmUtil.generateJson(packageJson);
					WorkbenchResourceUtil.createFile(file, json);
					WorkbenchResourceUtil.openInEditor(file, null);
				}
			}
		} catch (CoreException e) {
			NpmPlugin.logError(e);
			return false;
		}
		return true;
	}

}

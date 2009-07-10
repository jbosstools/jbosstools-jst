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
package org.jboss.tools.jst.web.ui.wizards.project;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.actions.WorkspaceModifyDelegatingOperation;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

import org.jboss.tools.common.reporting.ProblemReportingHelper;
import org.jboss.tools.common.meta.key.WizardKeys;
import org.jboss.tools.common.model.ui.ModelUIPlugin;
import org.jboss.tools.jst.web.context.ImportWebWarContext;
import org.jboss.tools.jst.web.ui.WebUiPlugin;

public abstract class ImportWebWarWizard extends Wizard implements IImportWizard {
	protected IWorkbench workbench; 
	protected IStructuredSelection selection;
	protected ImportWebWarWizardPage mainPage;
	protected ImportWebWarContext context;
	
	public ImportWebWarWizard() {
		setHelpAvailable(false);
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
	}

	protected void installMainPage() {
		mainPage = new ImportWebWarWizardPage(context);
		String mainPageTitle = WizardKeys.getString("" + getClass().getName() + ".mainPageTitle"); //$NON-NLS-1$ //$NON-NLS-2$
		mainPage.setTitle(mainPageTitle);
		String mainPageDescr = WizardKeys.getString("" + ImportWebWarWizard.class.getName() + ".mainPagePrompt"); //$NON-NLS-1$ //$NON-NLS-2$
		mainPage.setDescription(mainPageDescr);
		addPage(mainPage);
	}
	
	public boolean canFinish() {
		if(mainPage == null) return false;
		return mainPage.isPageComplete() && context.canFinish();
	}

	public boolean performFinish() {
		boolean result = true;		
			mainPage.commit();
			
			context.setServletVersion("2.4"); //$NON-NLS-1$
			if(!context.isServletVersionConsistentToWebXML()) {
				context.setServletVersion("2.3"); //$NON-NLS-1$
			}
			
			IRunnableWithProgress op =  new WorkspaceModifyDelegatingOperation(createOperation());
			try {
				getContainer().run(false, true, op);
			} catch (InvocationTargetException e) {
				WebUiPlugin.getPluginLog().logError(e);
			} catch (InterruptedException e) {
				WebUiPlugin.getPluginLog().logError(e);
			}
			updatePerspective();
			BasicNewResourceWizard.selectAndReveal(context.getProjectHandle(), ModelUIPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow());
		return result;
	}

	protected abstract IRunnableWithProgress createOperation();

	protected void updatePerspective() {
		BasicNewProjectResourceWizard.updatePerspective(new ConfigurationElementInternal());
	}

	protected String getFinalPerspective() {
		return "org.jboss.tools.jst.web.ui.WebDevelopmentPerspective"; //$NON-NLS-1$
	}
	
	private class ConfigurationElementInternal implements IConfigurationElement {
		public Object getParent() {
			return null;
		}
		public String getAttribute(String name) {
			return "finalPerspective".equals(name) ? getFinalPerspective() : null; //$NON-NLS-1$
		}

		public Object createExecutableExtension(String propertyName) throws CoreException {
			throw new UnsupportedOperationException("Not implemented."); //$NON-NLS-1$
		}
		public String getAttributeAsIs(String name) {
			throw new UnsupportedOperationException("Not implemented."); //$NON-NLS-1$
		}
		public String[] getAttributeNames() {
			throw new UnsupportedOperationException("Not implemented."); //$NON-NLS-1$
		}
		public IConfigurationElement[] getChildren() {
			throw new UnsupportedOperationException("Not implemented."); //$NON-NLS-1$
		}
		public IConfigurationElement[] getChildren(String name) {
			throw new UnsupportedOperationException("Not implemented."); //$NON-NLS-1$
		}
		public IExtension getDeclaringExtension() {
			throw new UnsupportedOperationException("Not implemented."); //$NON-NLS-1$
		}
		public String getName() {
			throw new UnsupportedOperationException("Not implemented."); //$NON-NLS-1$
		}
		public String getValue() {
			throw new UnsupportedOperationException("Not implemented."); //$NON-NLS-1$
		}
		public String getValueAsIs() {
			throw new UnsupportedOperationException("Not implemented."); //$NON-NLS-1$
		}
		public Class loadExtensionClass(String propertyName) throws CoreException {
			return null;
		}
		public String getNamespace() throws InvalidRegistryObjectException {
			return null;
		}
		public boolean isValid() {
			return true;
		}
		public IContributor getContributor() throws InvalidRegistryObjectException {
			throw new UnsupportedOperationException("Not implemented."); //$NON-NLS-1$
		}
		public String getNamespaceIdentifier() throws InvalidRegistryObjectException {
			throw new UnsupportedOperationException("Not implemented."); //$NON-NLS-1$
		}
	}

}

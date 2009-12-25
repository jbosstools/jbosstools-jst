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
import java.util.Properties;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.actions.WorkspaceModifyDelegatingOperation;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

import org.jboss.tools.common.meta.key.WizardKeys;
import org.jboss.tools.common.model.ServiceDialog;
import org.jboss.tools.common.model.XModelException;
import org.jboss.tools.common.model.options.PreferenceModelUtilities;
import org.jboss.tools.common.reporting.ProblemReportingHelper;
import org.jboss.tools.common.model.ui.ModelUIPlugin;
import org.jboss.tools.jst.web.WebModelPlugin;
import org.jboss.tools.jst.web.context.ImportWebDirProjectContext;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;
import org.jboss.tools.jst.web.ui.WebUiPlugin;

public abstract class ImportWebProjectWizard extends Wizard implements IImportWizard {
	public static final String IMPORT_WEB_PROJECT_WIZARD_PROJECT_LOCATION = "IMPORT_WEB_PROJECT_WIZARD_PROJECT_LOCATION"; //$NON-NLS-1$
	public static final String IMPORT_WEB_PROJECT_SELECT_WEB_XML = "IMPORT_WEB_PROJECT_SELECT_WEB_XML"; //$NON-NLS-1$
	public static final String IMPORT_WEB_PROJECT_WIZARD_PROJECT_FOLDERS = "IMPORT_WEB_PROJECT_WIZARD_PROJECT_FOLDERS"; //$NON-NLS-1$
	public static final String IMPORT_WEB_PROJECT_SELECT_PROJECT_FOLDERS = "IMPORT_WEB_PROJECT_SELECT_PROJECT_FOLDERS"; //$NON-NLS-1$

	protected IWorkbench workbench; 
	protected IStructuredSelection selection;
	protected ImportWebDirProjectContext context;
	protected String initialName = null;
	protected String initialLocation = null;
	protected ImportWebProjectWizardPage mainPage;
	
	public ImportWebProjectWizard() {
		setNeedsProgressMonitor(true);
		setHelpAvailable(false);
	}
	
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
	}
	
	protected void installMainPage() {
		mainPage = new ImportWebProjectWizardPage(context); 
		mainPage.setTitle(WizardKeys.getString(IMPORT_WEB_PROJECT_WIZARD_PROJECT_LOCATION)); 
		mainPage.setDescription(WizardKeys.getString(IMPORT_WEB_PROJECT_SELECT_WEB_XML));
		addPage(mainPage);
	}
	
	protected void installFoldersPage(WizardPage page) {
		page.setTitle(WizardKeys.getString(IMPORT_WEB_PROJECT_WIZARD_PROJECT_FOLDERS)); 
		page.setDescription(WizardKeys.getString(IMPORT_WEB_PROJECT_SELECT_PROJECT_FOLDERS));
		addPage(page);
	}
	
	public boolean canFinish() {
		if(mainPage==null) return false;
		return mainPage.isPageComplete() && context.canFinish();
	}

	public boolean performFinish() {
		if(!checkOldVersion()) return false;
		boolean result = false;		
		try	{
			if(!checkServletVersion()) return false;
			context.commitSupportDelta();
			IRunnableWithProgress op =  new WorkspaceModifyDelegatingOperation(createOperation());
			getContainer().run(false, true, op);
			updatePerspective();
			BasicNewResourceWizard.selectAndReveal(context.getProjectHandle(), ModelUIPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow());
			result = true;
		} catch (XModelException ex) {
			WebUiPlugin.getPluginLog().logError(ex);
		} catch (CoreException ex) {
			WebUiPlugin.getPluginLog().logError(ex);
		} catch (InvocationTargetException ex) {
			WebUiPlugin.getPluginLog().logError(ex);
		} catch (InterruptedException ex) {
			WebUiPlugin.getPluginLog().logError(ex);
		}		
		return result;
	}
	
	protected abstract IRunnableWithProgress createOperation();
	
	public boolean performCancel() {
		if(context==null)return true;
		context.rollbackSupportDelta();
		return true;
	}
	
	protected boolean checkOldVersion() {
		return true;
	}

	public void setInitialName(String s) {
		initialName = s;
	}
	
	public void setInitialLocation(String s) {
		initialLocation = s;
	}

	protected void updatePerspective() throws CoreException {
		BasicNewProjectResourceWizard.updatePerspective(new ConfigurationElementInternal());
	}

	protected String getFinalPerspective() {
		return "org.jboss.tools.jst.web.ui.WebDevelopmentPerspective"; //$NON-NLS-1$
	}
	
	private boolean checkServletVersion() throws XModelException {
		if(context.isServletVersionConsistentToWebXML()) return true;
		ServiceDialog d = PreferenceModelUtilities.getPreferenceModel().getService();
		String servletVersion = context.getServletVersion();
		String message = NLS.bind(WebUIMessages.SERVLET_VERSION_ISNOT_CONSISTENT_WITH_WEBXML_VERSION, servletVersion);
		Properties p = new Properties();
		p.setProperty("title", WebUIMessages.WARNING); //$NON-NLS-1$
		p.setProperty(ServiceDialog.DIALOG_MESSAGE, message);
		String checkBoxMessage = NLS.bind(WebUIMessages.SAVE_OLD_SERVLET, context.getWebXMLVersion());
		p.setProperty(ServiceDialog.CHECKBOX_MESSAGE, checkBoxMessage);
		p.put(ServiceDialog.CHECKED, Boolean.TRUE);
		p.put(ServiceDialog.BUTTONS, new String[]{WebUIMessages.YES, WebUIMessages.NO, WebUIMessages.CANCEL});
		boolean b = d.openConfirm(p);
		int q = (p.containsKey(ServiceDialog.RETURN_CODE)) ? ((Integer)p.get(ServiceDialog.RETURN_CODE)).intValue() : b ? 0 : -1;
		boolean backup = ((Boolean)p.get(ServiceDialog.CHECKED)).booleanValue();
		if(q == 0) context.convertWebXML(backup);
		return q == 0 || q == 1;
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

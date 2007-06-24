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
package org.jboss.tools.jst.web.ui.action.server;

import java.util.*;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.*;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.internal.Runtime;
import org.eclipse.wst.server.core.internal.RuntimeWorkingCopy;

import org.jboss.tools.common.meta.action.SpecialWizard;
import org.jboss.tools.common.meta.action.SpecialWizardFactory;
import org.jboss.tools.common.model.ServiceDialog;
import org.jboss.tools.common.model.options.PreferenceModelUtilities;
import org.jboss.tools.jst.web.WebModelPlugin;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;
import org.jboss.tools.jst.web.server.*;
import org.jboss.tools.jst.web.tomcat.TomcatVMHelper;

public class RunServerActionDelegate extends AbstractServerActionDelegate {
	static Set startingServers = new HashSet();

	public void init(IWorkbenchWindow window) {
		super.init(window);
		update();
	}
	
	protected void doRun() {
		IServer server = ServerManager.getInstance().getSelectedServer();
		if(server == null || startingServers.contains(server.getName())) return;
		ServiceDialog d = PreferenceModelUtilities.getPreferenceModel().getService();
		
		IRuntime runtime = server.getRuntime();
		if(runtime == null) {
			String message = WebUIMessages.RUNTIME_REFERENCED_HAS_BEEN_REMOVED;
			d.showDialog(WebUIMessages.RUN_SERVER, message, new String[]{WebUIMessages.CLOSE}, null, ServiceDialog.WARNING);
			return;
		}
		IStatus status = runtime.validate(new NullProgressMonitor());
		
		if(status.getSeverity() == IStatus.ERROR || status.getSeverity() == IStatus.WARNING) {
			if(!checkToolsJar(runtime)) {
				if(!createJVM(runtime)) return;
			} else {
				int i = d.showDialog(WebUIMessages.RUN_SERVER, status.getMessage(), new String[]{WebUIMessages.RUN, WebUIMessages.CANCEL}, null, ServiceDialog.WARNING);
				if(i != 0) return;
			}
		}
		startingServers.add(server.getName());		
		RunServerJob job = new RunServerJob(this, server, getLaunchMode());
		job.schedule();
		updateAll();
	}
	
	protected String getLaunchMode() {
		return ILaunchManager.RUN_MODE;
	}

	protected boolean isActionEnabled() {
		IServer selected = ServerManager.getInstance().getSelectedServer();
		return (selected != null 
				&& selected.getServerState() != IServer.STATE_STARTED 
				&& selected.getServerState() != IServer.STATE_STARTING) 
				&& !startingServers.contains(selected.getName());
	}

	protected String computeToolTip() {
		IServer selected = ServerManager.getInstance().getSelectedServer();
		String name = selected == null ? "" : selected.getName(); //$NON-NLS-1$
		return WebUIMessages.START + name;
	}	
	
	private boolean checkToolsJar(IRuntime runtime) {
		String id = getVMInstallTypeId(runtime);
		if(id == null) return false;
		IVMInstall vmInstall = TomcatVMHelper.getJVMInstallById(id);
		if(vmInstall == null) return false;
		String jvm = (vmInstall == null) ? null : vmInstall.getInstallLocation().getPath();
		String toolsPath = TomcatVMHelper.findToolsJarInVM(jvm);
		return (toolsPath != null);
	}
	
	String getVMInstallTypeId(IRuntime runtime) {
		try {
			return ((Runtime)runtime).getAttribute("vm-install-id", (String)null); //$NON-NLS-1$
		} catch (Exception e) {
			WebModelPlugin.log(e);
		}
		return null;
	}
	void setVMInstallTypeId(IRuntime runtime, String id, String typeId) {
		try {
			RuntimeWorkingCopy copy = (RuntimeWorkingCopy)runtime.createWorkingCopy();
			copy.setAttribute("vm-install-id", id); //$NON-NLS-1$
			copy.setAttribute("vm-install-type-id", typeId); //$NON-NLS-1$
			copy.save(true, new NullProgressMonitor());
		} catch (Exception e) {
			WebModelPlugin.log(e);
		}
	}

	boolean createJVM(IRuntime runtime) {
		runtime = runtime.createWorkingCopy();
		SpecialWizard wizard = SpecialWizardFactory.createSpecialWizard("org.jboss.tools.jst.web.ui.wizards.tomcatvm.TomcatVMWizard"); //$NON-NLS-1$
		Properties p = new Properties();
		wizard.setObject(p);
		int ii = wizard.execute();
		if(ii != 0) return false;
		String jvm = p.getProperty("vm"); //$NON-NLS-1$
		if(jvm != null) jvm = jvm.trim();
		String name = TomcatVMHelper.createVM(jvm);
		IVMInstall vm = TomcatVMHelper.getJVMInstall(name);
		String id = vm.getId();
		String typeId = vm.getVMInstallType().getId();
		setVMInstallTypeId(runtime, id, typeId);
		return true;
	}

}

class RunServerJob extends Job {
	IServer server;
	String launchMode;
	RunServerActionDelegate action;
	
	public RunServerJob(RunServerActionDelegate action, IServer server, String launchMode) {
		super(WebUIMessages.RUN_SERVER+ " " + server.getName());
		this.launchMode = launchMode;
		this.server = server;
		this.action = action;
	}

	protected IStatus run(IProgressMonitor monitor) {
		try {
			boolean publishBefore = RegistrationHelper.canPublish(server);
			if(publishBefore) {
				server.publish(IServer.PUBLISH_INCREMENTAL, new NullProgressMonitor());
			}
			server.start(launchMode, new NullProgressMonitor());
			if(!publishBefore) {
				server.publish(IServer.PUBLISH_INCREMENTAL, new NullProgressMonitor());
			}
		} catch (Exception e) {
			return new Status(IStatus.ERROR, "org.jboss.tools.jst.web", 0, WebUIMessages.CANNOT_START_SERVER + e.getMessage(), e); //$NON-NLS-1$
		} finally {
			RunServerActionDelegate.startingServers.remove(server.getName());
			RunServerActionDelegate.updateAll();
		}
		return Status.OK_STATUS;
	}
	
}

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

import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.ui.internal.view.servers.StartAction;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;
import org.jboss.tools.jst.web.server.ServerManager;

public class RunServerActionDelegate extends AbstractServerActionDelegate {

	private StartAction delegate;
	public void init(IWorkbenchWindow window) {
		super.init(window);
		update();
	}
	
	protected void doRun() {
		IServer server = ServerManager.getInstance().getSelectedServer();
		if(server == null || server.getServerState() != IServer.STATE_STOPPED) 
			return;
		delegate = new StartAction(window.getShell(), getSelectionProvider(), getLaunchMode());
		if( delegate.accept(server))
			delegate.perform(server);
		updateAll();
	}
	
	protected String getLaunchMode() {
		return ILaunchManager.RUN_MODE;
	}

	protected boolean isActionEnabled() {
		IServer selected = ServerManager.getInstance().getSelectedServer();
		return (selected != null 
				&& selected.getServerState() != IServer.STATE_STARTED 
				&& selected.getServerState() != IServer.STATE_STARTING);
	}

	protected String computeToolTip() {
		IServer selected = ServerManager.getInstance().getSelectedServer();
		String name = selected == null ? "" : selected.getName(); //$NON-NLS-1$
		return WebUIMessages.START + name;
	}	
	
	
	
	
	
	/*
	 * 	This code is very Tomcat specific 
	 *  and arguably unnecessary.
	 *  Switching implementation to use WTP API's.
	 *  
	 *  - rob.stryker
	 */
	
//	private boolean checkToolsJar(IRuntime runtime) {
//		String id = getVMInstallTypeId(runtime);
//		if(id == null) return false;
//		IVMInstall vmInstall = TomcatVMHelper.getJVMInstallById(id);
//		if(vmInstall == null) return false;
//		String jvm = (vmInstall == null) ? null : vmInstall.getInstallLocation().getPath();
//		String toolsPath = TomcatVMHelper.findToolsJarInVM(jvm);
//		return (toolsPath != null);
//	}
//	
//	String getVMInstallTypeId(IRuntime runtime) {
//		try {
//			return ((Runtime)runtime).getAttribute("vm-install-id", (String)null); //$NON-NLS-1$
//		} catch (Exception e) {
//			WebModelPlugin.getPluginLog().logError(e);
//		}
//		return null;
//	}
//	void setVMInstallTypeId(IRuntime runtime, String id, String typeId) {
//		try {
//			RuntimeWorkingCopy copy = (RuntimeWorkingCopy)runtime.createWorkingCopy();
//			copy.setAttribute("vm-install-id", id); //$NON-NLS-1$
//			copy.setAttribute("vm-install-type-id", typeId); //$NON-NLS-1$
//			copy.save(true, new NullProgressMonitor());
//		} catch (Exception e) {
//			WebUiPlugin.getPluginLog().logError(e);
//		}
//	}
//
//	boolean createJVM(IRuntime runtime) {
//		runtime = runtime.createWorkingCopy();
//		SpecialWizard wizard = SpecialWizardFactory.createSpecialWizard("org.jboss.tools.jst.web.ui.wizards.tomcatvm.TomcatVMWizard"); //$NON-NLS-1$
//		Properties p = new Properties();
//		wizard.setObject(p);
//		int ii = wizard.execute();
//		if(ii != 0) return false;
//		String jvm = p.getProperty("vm"); //$NON-NLS-1$
//		if(jvm != null) jvm = jvm.trim();
//		String name = TomcatVMHelper.createVM(jvm);
//		IVMInstall vm = TomcatVMHelper.getJVMInstall(name);
//		String id = vm.getId();
//		String typeId = vm.getVMInstallType().getId();
//		setVMInstallTypeId(runtime, id, typeId);
//		return true;
//	}
//


}


/*
 *  This class is an inefficient (and less functional)
 *  duplicate of StartAction (inside WTP).
 *  
 *  - rob.stryker
 */ 

//class RunServerJob extends Job {
//	IServer server;
//	String launchMode;
//	RunServerActionDelegate action;
//	
//	public RunServerJob(RunServerActionDelegate action, IServer server, String launchMode) {
//		super(WebUIMessages.RUN_SERVER+ " " + server.getName());
//		this.launchMode = launchMode;
//		this.server = server;
//		this.action = action;
//	}
//
//	protected IStatus run(IProgressMonitor monitor) {
//		try {
//			boolean publishBefore = RegistrationHelper.canPublish(server);
//			if(publishBefore) {
//				server.publish(IServer.PUBLISH_INCREMENTAL, new NullProgressMonitor());
//			}
//			server.start(launchMode, new NullProgressMonitor());
//			if(!publishBefore) {
//				server.publish(IServer.PUBLISH_INCREMENTAL, new NullProgressMonitor());
//			}
//		} catch (Exception e) {
//			return new Status(IStatus.ERROR, "org.jboss.tools.jst.web", 0, WebUIMessages.CANNOT_START_SERVER + e.getMessage(), e); //$NON-NLS-1$
//		} finally {
//			RunServerActionDelegate.startingServers.remove(server.getName());
//			RunServerActionDelegate.updateAll();
//		}
//		return Status.OK_STATUS;
//	}
//	
//}

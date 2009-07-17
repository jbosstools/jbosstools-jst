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
package org.jboss.tools.jst.web.browser.wtp;

import java.util.*;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.wst.server.core.*;
import org.eclipse.wst.server.core.internal.*;
import org.eclipse.wst.server.core.util.*;

import org.jboss.tools.common.model.ServiceDialog;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelException;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.options.PreferenceModelUtilities;
import org.jboss.tools.common.model.plugin.ModelPlugin;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.common.model.util.ModelFeatureFactory;
import org.jboss.tools.jst.web.WebModelPlugin;
import org.jboss.tools.jst.web.browser.AbstractBrowserContext;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;
import org.jboss.tools.jst.web.server.ServerManager;

public class RunOnServerContext extends AbstractBrowserContext {
	static RunOnServerContext instance = new RunOnServerContext();
	
	@SuppressWarnings("nls")
	static String[][] pathSources = new String[][]{
		{"org.jboss.tools.jst.web.model.handlers.RunOnServerHandler", "org.jboss.tools.jst.web"},
		{"org.jboss.tools.struts.model.handlers.page.RunOnServerHandler", "org.jboss.tools.struts"},
		{"org.jboss.tools.jsf.model.handlers.run.RunOnServerHandler", "org.jboss.tools.jsf"}
	};

	public void init() {
		if(inited) return;
		inited = true;
		//causes delegating IPathSource to RunOnServerContext
		//this implementation imitates extension point
		for (int i = 0; i < pathSources.length; i++) {
			String plugin = pathSources[i][1];
			if(Platform.getBundle(plugin) == null) {
				continue;
			}
			String classname = pathSources[i][0];
			ModelFeatureFactory.getInstance().createFeatureInstance(classname);
		}
	}

	public static RunOnServerContext getInstance() {
		return instance;
	}
	
	public String getModelActionPath() {
		return "RunActions.RunPageOnServer"; //$NON-NLS-1$
	}

	public String getBrowserPrefix(XModel model) {
//		String appname = model.getByPath("FileSystems").getAttributeValue("application name");
		IServer server = ServerManager.getInstance().getSelectedServer();
		if(server == null ) return "%server%" + WebUIMessages.CANNOT_RUN_SELECTION_WITHOUT_AVAILABLE_SERVER; //$NON-NLS-1$
		IProject p = EclipseResourceUtil.getProject(model.getRoot());
		if(p != null && (!p.exists() || !p.isOpen())) p = null;
		if(p == null) return null;
		IModule ms = null;
		try {
			// TODO Ear project can contain several Web modules, so when ear is selected several options can be shown in
			// in menu now it throws exception Invalid Artifact Edit access
			ms = ServerUtil.getModule(p);
		} catch (Exception e) {
			WebModelPlugin.getPluginLog().logError(e);
		}
		WebResource wr = (ms == null) ? null : new WebResource(ms, new Path("")); //$NON-NLS-1$

		HttpLaunchable launchable = getLaunchable(server, wr);
		if(launchable != null) {
			URL url = ((HttpLaunchable)launchable).getURL();
			if(url != null) return url.toString();
		}
		return "%server%" + NLS.bind(WebUIMessages.APPLICATION_ISNOT_REGISTERED_IN_SELECTED_SERVER, (p == null ? "" : p.getName())); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	private HttpLaunchable getLaunchable(IServer server, WebResource resource) {
		if(resource == null) return null;
		ILaunchableAdapter[] as = ServerPlugin.getLaunchableAdapters();
		for (int i = 0; i < as.length; i++) {
			try {
				Object launchable = as[i].getLaunchable(server, resource);
				if(launchable instanceof HttpLaunchable) return (HttpLaunchable)launchable;
			} catch (CoreException e) {
				WebModelPlugin.getPluginLog().logError(e);
			}
		}
		return null;
	}

	protected void doExecute(String lastRunUrl) throws XModelException {
		ServiceDialog d = PreferenceModelUtilities.getPreferenceModel().getService();
		IServer server = ServerManager.getInstance().getSelectedServer();
		if(server == null) {
			if(lastRunUrl != null && lastRunUrl.startsWith("%server%")) { //$NON-NLS-1$
				String message = WebUIMessages.PLEASE_CREATE_A_SERVER_AND_SELECT_IT_ON_TOOLBAR;
				d.showDialog(WebUIMessages.WARNING, message, new String[]{WebUIMessages.CLOSE}, null, ServiceDialog.WARNING);
				return;
			}
			runJustUrl();
			return;
		}
		if(!checkUrl()) return;

		server.getModules();
		
		String launchMode = ILaunchManager.DEBUG_MODE.equals(server.getMode()) ? ILaunchManager.DEBUG_MODE : ILaunchManager.RUN_MODE; 
		try {
			Object launchable = new HttpLaunchable(new URL(lastRunUrl));
			IClient[] clients = getClients(server, launchable, launchMode);
			IClient client = clients[0];
			client.launch(server, launchable, launchMode, server.getLaunch());
		} catch (MalformedURLException e) {
			WebModelPlugin.getPluginLog().logError(e);
			runJustUrl();
		}
	}

	public static IClient[] getClients(IServer server, Object launchable, String launchMode) {
		ArrayList<Object> list = new ArrayList<Object>();
		IClient[] clients = ServerPlugin.getClients();
		if (clients != null) {
			int size = clients.length;
			for (int i = 0; i < size; i++) {
				if (clients[i].supports(server, launchable, launchMode))
					list.add(clients[i]);
			}
		}
		
		IClient[] clients2 = new IClient[list.size()];
		list.toArray(clients2);
		return clients2;
	}

	public void runJustUrl() {
		IWorkbenchBrowserSupport browserSupport = ModelPlugin.getDefault().getWorkbench().getBrowserSupport();
		try {
			IWebBrowser browser = browserSupport.createBrowser(IWorkbenchBrowserSupport.LOCATION_BAR | IWorkbenchBrowserSupport.NAVIGATION_BAR, null, null, null);
			if(!checkUrl()) return;
			browser.openURL(new URL(lastRunUrl));
		} catch (MalformedURLException mue) {
			ServiceDialog d = PreferenceModelUtilities.getPreferenceModel().getService();
			d.showDialog(WebUIMessages.ERROR, NLS.bind(WebUIMessages.INCORRECT_URL, mue.getMessage()), new String[]{WebUIMessages.OK}, null, ServiceDialog.ERROR);
		} catch (PartInitException e) {
			WebModelPlugin.getPluginLog().logError(e);
		}
	}
	
	private boolean checkUrl() {
		if(lastRunUrl == null || lastRunUrl.length() == 0) return false;
		if(!lastRunUrl.startsWith("%server%")) return true; //$NON-NLS-1$
		if(lastRunObject == null) return false;
		ServiceDialog d = PreferenceModelUtilities.getPreferenceModel().getService();
		XModelObject fss = lastRunObject.getModel().getByPath("FileSystems"); //$NON-NLS-1$
		if(fss == null) return false;
		String appname = fss.getAttributeValue("application name"); //$NON-NLS-1$
		String message = NLS.bind(WebUIMessages.APPLICATION_ISNOT_REGISTERED_IN_SELECTED_SERVER, appname);
		d.showDialog(WebUIMessages.WARNING, message, new String[]{WebUIMessages.CLOSE}, null, ServiceDialog.WARNING);
		return false;
	}

}

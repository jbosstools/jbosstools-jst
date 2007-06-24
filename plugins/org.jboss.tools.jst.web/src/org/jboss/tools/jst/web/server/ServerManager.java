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
package org.jboss.tools.jst.web.server;

import java.util.*;

import org.eclipse.wst.server.core.*;
import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.options.PreferenceModelUtilities;
import org.jboss.tools.jst.web.WebPreference;

public class ServerManager {
	private static ServerManager instance;
	
	public static synchronized ServerManager getInstance() {
		if(instance == null) {
			instance = new ServerManager();
		}
		return instance;
	}
	
	private XModel preferenceModel = PreferenceModelUtilities.getPreferenceModel();
	private XModelObject runningObject = preferenceModel.getByPath(WebPreference.OPTIONS_RUNNING_PATH);
	private List<ServerManagerListener> listeners = new ArrayList<ServerManagerListener>();
	private IServerListener serverListener;
	protected IServer[] servers = new IServer[0];

	IServer selected = null;

	public ServerManager() {
		serverListener = new ServerListenerImpl();
		load();
	}
	
	void load() {
		servers = (IServer[])ServerCore.getServers().clone();
		loadSelectedServer();
		ServerResourceListenerImpl listener = new ServerResourceListenerImpl();
		ServerCore.addRuntimeLifecycleListener(listener);
		ServerCore.addServerLifecycleListener(listener);
	}
	
	private void loadSelectedServer() {
		String ds = WebPreference.DEFAULT_WTP_SERVER.getValue();
		IServer s = getServer(ds);
		if(s == null && servers.length > 0) {
			s = servers[0];
			WebPreference.DEFAULT_WTP_SERVER.setValue(servers[0].getId());
		}
		setSelectedServerInternal(s);
	}
	
	public IServer[] getServers() {
		return servers;
	}
	
	public IServer getServer(String serverId) {
		for (int i = 0; i < servers.length; i++) {
			if(servers[i].getId().equals(serverId)) {
				return servers[i];
			}
		}
		return null;
	}
	
	public void addListener(ServerManagerListener listener) {
		if(!listeners.contains(listener)) listeners.add(listener);
	}
	
	public void removeListener(ServerManagerListener listener) {
		listeners.remove(listener);
	}
	
	void fire() {
		ServerManagerListener[] ls = (ServerManagerListener[])listeners.toArray(new ServerManagerListener[0]);
		for (int i = 0; i < ls.length; i++) {
			ls[i].serverManagerChanged();
		}
	}
	
	public void setSelectedServer(String id) {
		IServer server = getServer(id);
		if(server == selected) return;
		setSelectedServerInternal(server);
		preferenceModel.changeObjectAttribute(runningObject, "Default WTP Server", id);
		preferenceModel.saveOptions();
		fire();
	}
	
	private void setSelectedServerInternal(IServer server) {
		if(selected == server) return;
		if(selected != null) selected.removeServerListener(serverListener);
		selected = server;
		if(selected != null) selected.addServerListener(serverListener);
	}
	
	public String getSelectedServerId() {
		return WebPreference.DEFAULT_WTP_SERVER.getValue();
	}

	public IServer getSelectedServer() {
		return selected;
	}

	class ServerResourceListenerImpl implements IRuntimeLifecycleListener, IServerLifecycleListener {
		public void serverAdded(IServer server) {
			IServer[] ss = new IServer[servers.length + 1];
			System.arraycopy(servers, 0, ss, 0, servers.length);
			ss[servers.length] = server;
			servers = ss;
			loadSelectedServer();
			fire();
		}
		public void serverChanged(IServer server) {
			fire();
		}
		public void serverRemoved(IServer server) {
			List<IServer> l = new ArrayList<IServer>();
			for (int i = 0; i < servers.length; i++) {
				if(servers[i] != server) l.add(servers[i]);
			}
			if(l.size() == servers.length) return;
			servers = l.toArray(new IServer[0]);
			loadSelectedServer();
			fire();
		}
		public void runtimeAdded(IRuntime runtime) {
			fire();
		}
		public void runtimeChanged(IRuntime runtime) {
			fire();
		}
		public void runtimeRemoved(IRuntime runtime) {
			fire();
		}
	}
	
	class ServerListenerImpl implements IServerListener {
		public void serverChanged(ServerEvent arg0) {
			fire();
		}
	}
	
}

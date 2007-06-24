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
import org.eclipse.core.runtime.*;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.*;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.TaskModel;
import org.eclipse.wst.server.ui.internal.wizard.ClosableWizardDialog;
import org.eclipse.wst.server.ui.internal.wizard.NewServerWizard;
import org.jboss.tools.common.model.ui.ModelUIImages;
import org.jboss.tools.jst.web.server.*;


public class SelectServerActionDelegate extends AbstractServerActionDelegate implements IWorkbenchWindowPulldownDelegate {
	
	protected void safeSelectionChanged(IAction action, ISelection selection) {
		this.action = action;
		update();
	}
	
	protected void update() {
		if (action == null)	return;
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				update0();
			}
		});
	}
	private void update0() {
		if(action == null) return;
		IServer server = ServerManager.getInstance().getSelectedServer();
		ImageDescriptor d = (server != null) ? getImageDescriptor(server)
		    : ModelUIImages.getImageDescriptor("wizards/new_server.gif");
		action.setImageDescriptor(null);
		action.setText("   ");
		action.setImageDescriptor(d);
		action.setText(null);
	}
	
	private ImageDescriptor getImageDescriptor(IServer server) {
		if(imageDescriptors == null) {
			initializeImageRegistry();
		}
// TODO-3.3: why double null check needed for 3.3?
		String key = server == null || server.getServerType() == null ? null : server.getServerType().getId();
		ImageDescriptor d = (ImageDescriptor)imageDescriptors.get(key);
		if(d == null) {
			d = ImageDescriptor.getMissingImageDescriptor();
			imageDescriptors.put(key, d);
		}
		return d;
	}
	
	protected void doRun() {
		getMenu(window.getShell()).setVisible(true);
	}
	
	public void dispose() {
		action = null;
	}
	
	public Menu getMenu(Control parent) {
		Menu menu = new Menu(parent);
		IServer[] servers = ServerManager.getInstance().getServers();
		for (int i = 0; i < servers.length; i++) {
			createServerItem(menu, servers[i]);
		}
		new MenuItem(menu, SWT.SEPARATOR);
		createNewServerItem(menu);
		return menu;
	}
	
	private void createServerItem(Menu menu, final IServer server) {
		MenuItem item = new MenuItem(menu, SWT.RADIO);
		if(server == ServerManager.getInstance().getSelectedServer()) {
			item.setSelection(true);
		}
		
		item.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				ServerManager.getInstance().setSelectedServer(server.getId());
				update();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
		item.setText(server.getName());
		ImageDescriptor d = getImageDescriptor(server);
		item.setImage(d.createImage());		
	}
	
	private void createNewServerItem(Menu menu) {
		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText("New server...");
		item.setImage(ModelUIImages.getImage("wizards/new_server.gif"));
		item.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				newServer();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				newServer();
				widgetSelected(e);
			}
		});
	}
	
	private void newServer() {
		NewServerWizard wizard = new NewServerWizard();
		ClosableWizardDialog dialog = new ClosableWizardDialog(window.getShell(), wizard);
		if (dialog.open() == Window.CANCEL) {
			return;
		}
		IServer server = (IServer)wizard.getRootFragment().getTaskModel().getObject(TaskModel.TASK_SERVER);
		if(server == null) return;
		ServerManager.getInstance().setSelectedServer(server.getId());
		update();		
	}
	
// server images
	private static ImageRegistry imageRegistry;
	private static Map<String,ImageDescriptor> imageDescriptors;
	
	public static Image getImage(String key) {
		if (imageRegistry == null)
			initializeImageRegistry();
		Image image = imageRegistry.get(key);
		if (image == null) {
			imageRegistry.put(key, ImageDescriptor.getMissingImageDescriptor());
			image = imageRegistry.get(key);
		}
		return image;
	}
	protected static void initializeImageRegistry() {
		imageRegistry = new ImageRegistry();
		imageDescriptors = new HashMap<String,ImageDescriptor>();
		loadServerImages();
	}

	private static void loadServerImages() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] cf = registry.getConfigurationElementsFor(/*ServerUICore.PLUGIN_ID*/"org.eclipse.wst.server.ui", "serverImages");

		int size = cf.length;
		for (int i = 0; i < size; i++) {
			try {
				String pluginId = cf[i].getDeclaringExtension().getNamespaceIdentifier();
				String iconPath = cf[i].getAttribute("icon");
				ImageDescriptor imageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, iconPath);
				if (imageDescriptor == null)
					ImageDescriptor.getMissingImageDescriptor();
				
				String typeId = cf[i].getAttribute("typeIds");
				if (typeId == null)
					typeId = cf[i].getAttribute("moduleId");
				imageRegistry.put(typeId, imageDescriptor);		
				imageDescriptors.put(typeId, imageDescriptor);
			} catch (Exception t) {
			}
		}
	}

}

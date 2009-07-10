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
package org.jboss.tools.jst.web.ui.action;

import java.text.MessageFormat;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowPulldownDelegate;
import org.jboss.tools.common.meta.action.XActionInvoker;
import org.jboss.tools.common.meta.action.XEntityData;
import org.jboss.tools.common.meta.action.impl.SpecialWizardSupport;
import org.jboss.tools.common.meta.action.impl.XEntityDataImpl;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelException;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.event.XModelTreeEvent;
import org.jboss.tools.common.model.event.XModelTreeListener;
import org.jboss.tools.common.model.options.PreferenceModelUtilities;
import org.jboss.tools.common.model.plugin.ModelPlugin;
import org.jboss.tools.common.model.project.IModelNature;
import org.jboss.tools.common.model.ui.ModelUIPlugin;
import org.jboss.tools.common.model.ui.action.AbstractModelActionDelegate;
import org.jboss.tools.common.model.ui.dnd.DnDUtil;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.common.model.util.XModelTreeListenerSWTASync;
import org.jboss.tools.jst.web.browser.AbstractBrowserContext;
import org.jboss.tools.jst.web.browser.wtp.RunOnServerContext;
import org.jboss.tools.jst.web.server.ServerManager;
import org.jboss.tools.jst.web.server.ServerManagerListener;
import org.jboss.tools.jst.web.ui.Messages;
import org.jboss.tools.jst.web.ui.WebUiPlugin;

public class RunPageActionDelegate extends AbstractModelActionDelegate implements IWorkbenchWindowPulldownDelegate {
	static AbstractBrowserContext context = RunOnServerContext.getInstance();
	IAction action;
	XModel selectedModel = null;
	XModelTreeListenerSWTASync syncListener = new XModelTreeListenerSWTASync(new XModelTreeListenerImpl());
	ServerManagerListener sml = new SML();
	
	public RunPageActionDelegate() {
		ServerManager.getInstance().addListener(sml);
	}
	
	class SML implements ServerManagerListener {
		public void serverManagerChanged() {
			Display.getDefault().asyncExec(new R2());
		}		
	}
	
	public void init(IWorkbenchWindow window) {
		super.init(window);
		new Thread(new R(), "Update Run Page Action").start(); //$NON-NLS-1$
	}

	protected void safeSelectionChanged(IAction action, ISelection selection) {
		this.action = action;
		XModelObject adapter = getAdapter(selection);
		if(adapter == null) {
			IProject p = getSelectedProject(selection);
			if(p != null) {
				adapter = EclipseResourceUtil.createObjectForResource(p);
			} else {
				if(context.lastRunObject != null) {
					p = (IProject)context.lastRunObject.getModel().getProperties().get("project"); //$NON-NLS-1$
					if(p == null || !p.isOpen() || !p.exists()) {
						context.setLastRunObject(null);
						update();
					}
				}
				return;
			}
		}
		XModelObject o = adapter;
		if(object == null || (o != null && o.getModel() != object.getModel())) {
			if(o != null) object = o.getModel().getRoot();
			if(object == null)
				context.setLastRunObject(null);
			else
				context.activateModel(object.getModel());
		} else if(o != null && o != context.lastRunObject) {
			context.activateModel(object.getModel());
		} else {
			context.setLastRunObject(context.lastRunObject);
		}
		update();
	}
	
	private IProject getSelectedProject(ISelection selection) {
		if(!(selection instanceof IStructuredSelection)) return null;
		Object o = ((IStructuredSelection)selection).getFirstElement();
		IProject p = null;
		if(o instanceof IResource) {
			p = ((IResource)o).getProject();
		} else if(o instanceof IJavaElement) {
			p = ((IJavaElement)o).getJavaProject().getProject();
		}
		return p;
	}
	
	static String defaultToolTip = null; 
	
	protected void update() {
		object = context.lastRunObject;
		if(object != null && !object.isActive() && !context.isJustUrl(context.getLastRunURL())) {
			object = object.getModel().getRoot();
			context.setLastRunObject(object);
		}
		if(action != null) {
			if(defaultToolTip == null) defaultToolTip = action.getToolTipText();
			action.setEnabled(computeEnabled());
			String tip = defaultToolTip;
			if(object != null) {
				String lastRunURL = context.getLastRunURL();
				if(lastRunURL != null && !lastRunURL.startsWith("%server%")) { //$NON-NLS-1$
					tip = MessageFormat.format(Messages.RunPageActionDelegate_RunURL, lastRunURL);
				}
			}
			action.setToolTipText(tip);
		}
		updateModel();	
	}
	
	private void updateModel() {
		XModel m = (object == null) ? null : object.getModel();
		if(m == selectedModel) return;
		if(selectedModel != null) selectedModel.removeModelTreeListener(syncListener);
		selectedModel = m;
		if(selectedModel != null) selectedModel.addModelTreeListener(syncListener);
	}
	
	protected boolean computeEnabled() {
		object = context.lastRunObject;
		return true;
	}
	
	String getModelActionPath() {
		return context.getModelActionPath();
	}

	protected void doRun() {
		if(context.isJustUrl(context.getLastRunURL())) {
			if(!saveAllEditors()) return;
			context.runJustUrl();
		} else if(object != null && DnDUtil.getEnabledAction(object, null, getModelActionPath()) != null) {
			if(!saveAllEditors()) return;
			Properties p = new Properties();
			p.put("shell", window.getShell()); //$NON-NLS-1$
			XActionInvoker.invoke(getModelActionPath(), object, p);
		} else {
			getMenu(window.getShell()).setVisible(true);
			///runSelector();		
		}
	}
	
	private boolean stopped = false;
	class R implements Runnable {
		public void run() {
			while(!stopped) {
				synchronized (context.monitor) {
					try { 
						context.monitor.wait(); 
					} catch (Exception e) {
			        	WebUiPlugin.getPluginLog().logError(e);
					}					
				}
				if(!stopped) update();
			}
		}
	}
	class R2 implements Runnable {
		public void run() {
			context.setLastRunObject(context.lastRunObject);
		}
	}
	
	public void dispose() {
		stopped = true;
		action = null;
		synchronized (context.monitor) {
			try { 
				context.monitor.notifyAll(); 
			} catch (Exception e) {
	        	WebUiPlugin.getPluginLog().logError(e);
			}					
		}
		if(sml != null) {
			ServerManager.getInstance().removeListener(sml);
			sml = null;
		}
	}
	
	class XModelTreeListenerImpl implements XModelTreeListener {
		public void nodeChanged(XModelTreeEvent event) {
			if("FileSystems".equals(event.getModelObject().getPath())) { //$NON-NLS-1$
				context.setLastRunObject(context.lastRunObject);
			}
		}
		public void structureChanged(XModelTreeEvent event) {}
	}

	public Menu getMenu(Control parent) {
		Menu menu = new Menu(parent);
		MenuItem item = null;
		String lastRunURL = context.getLastRunURL();
		if(lastRunURL == null) {
			//do nothing
		} else if(!lastRunURL.startsWith("%server%")) { //$NON-NLS-1$
			item = new MenuItem(menu, SWT.PUSH);
			item.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent e) {
					run(action);
				}

				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				}
			});
			item.setText("" + context.getLastRunURL()); //$NON-NLS-1$
		} else {
			item = new MenuItem(menu, SWT.PUSH);
			String message = lastRunURL.substring("%server%".length()); //$NON-NLS-1$
			int i = message.indexOf("/"); //$NON-NLS-1$
			if(i >= 0) message = message.substring(0, i);
			item.setText(message);
		}
		String[] items = context.getHistory();
		if(items.length > 0) {
			item = new MenuItem(menu, SWT.SEPARATOR);
			for (int i = 0; i < items.length; i++) {
				final String url = items[i];
				item = new MenuItem(menu, SWT.PUSH);
				item.setText(url);
				item.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						if(context.isJustUrl(url)) {							
							context.activateJustUrl(url);
						} else {
							XModelObject o = context.activateItem(url);
							if(o == null) return;
						}
						update();
						run(action);
					}
				});
			}
		}
		if(item != null) {
			item = new MenuItem(menu, SWT.SEPARATOR);
		}
		item = new MenuItem(menu, SWT.PUSH);
		item.setText(Messages.RunPageActionDelegate_RunMenuItem);
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				runSelector();
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
		return menu;
	}
	
	private void runSelector() {
		if(!saveAllEditors()) return;
		XEntityData data = XEntityDataImpl.create(new String[][]{{"RunPageHelper"}, {"url", "yes"}}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		XModelObject dummy = PreferenceModelUtilities.getPreferenceModel().createModelObject("RunPageHelper", null); //$NON-NLS-1$
		int i = RunSelectorSupport.run(dummy, data, null);
		if(i != 0) return;
		String url = data.getValue("url"); //$NON-NLS-1$
		context.activateJustUrl(url);
		update();
		run(action);
	}

	public static boolean saveAllEditors() {
		return ModelUIPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().saveAllEditors(true);
	}

}

class RunSelectorSupport extends SpecialWizardSupport {
	public static int run(XModelObject object, XEntityData data, Properties p) {
		return run(object, data, p, new RunSelectorSupport());
	}

	public static int run(XModelObject object, XEntityData data, Properties p, RunSelectorSupport support) {
		support.setActionData(null, new XEntityData[]{data}, object, p);
		object.getModel().getService().showDialog(support);
		return support.getReturnCode();
	}
	protected int returnCode = -1;
	
	public void reset() {
		returnCode = -1;
		Set<String> set = new TreeSet<String>();
		IProject[] ps = ModelPlugin.getWorkspace().getRoot().getProjects();
		for (int i = 0; i < ps.length; i++) {
			IModelNature mn = EclipseResourceUtil.getModelNature(ps[i]);
			if(mn == null) continue;
			String url = RunPageActionDelegate.context.computeURL(mn.getModel().getRoot());
			if(url != null && !url.startsWith("%server%")) { //$NON-NLS-1$
				set.add(url);
			}
		}
		String lastRunUrl = RunPageActionDelegate.context.getLastRunURL();
		if(lastRunUrl != null && !lastRunUrl.startsWith("%server%")) { //$NON-NLS-1$
			set.add(lastRunUrl);
		}
		String[] urls = RunPageActionDelegate.context.getHistory();
		for (int i = 0; i < urls.length; i++) set.add(urls[i]);
		urls = (String[])set.toArray(new String[0]);
		setValueList(0, "url", urls); //$NON-NLS-1$
		setAttributeValue(0, "url", ""); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public String getTitle() {
		return Messages.RunPageActionDelegate_RunTitle;
	}

	public String getMessage(int stepId) {
		return Messages.RunPageActionDelegate_PleaseEnterURL;
	}

	public String[] getActionNames(int stepId) {
		return new String[]{FINISH, CANCEL};
	}

	public void action(String name) throws XModelException {
		if(name.equals(FINISH)) {
			returnCode = 0;
			setFinished(true);
		} else if(name.equals(CANCEL)) {
			returnCode = 1;
			setFinished(true);
		}
	}
	
	public int getReturnCode() {
		return returnCode;
	}
	
}

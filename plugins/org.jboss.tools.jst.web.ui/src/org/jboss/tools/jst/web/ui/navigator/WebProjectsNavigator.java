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
package org.jboss.tools.jst.web.ui.navigator;

import org.jboss.tools.common.model.ui.navigator.*;
import org.eclipse.jface.viewers.*;
import org.jboss.tools.common.meta.action.*;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.options.PreferenceModelUtilities;
import org.jboss.tools.common.model.ui.views.navigator.*;

public class WebProjectsNavigator extends NavigatorViewPart {
	public static String VIEW_ID = "org.jboss.tools.jst.web.ui.navigator.WebProjectsView"; //$NON-NLS-1$
	private WebProjectsContentProvider c = null;

	public void dispose() {
		super.dispose();
		if (c!=null) c.dispose();
		c = null;
	}

	protected void initLabelProvider(TreeViewer viewer)	{
		viewer.setLabelProvider(LabelDecoratorImpl.decorateLabelProvider(new WebProjectsLabelProvider()));			
	}
	
	protected void initContentProvider(TreeViewer viewer) {
		if(true) {
			c = new WebProjectsContentProvider();
			TreeViewerModelListenerImpl listener = new WebProjectsTreeListener();
			listener.setViewer(viewer);
			c.setListener(listener);
			contentProvider = c;
			viewer.setContentProvider(contentProvider);
		} else {
			viewer.setContentProvider(
				new ITreeContentProvider() {
					public Object[] getChildren(Object parentElement) {
						return new Object[]{};
					}
					public Object getParent(Object element) {
						return null;	
					}
					public boolean hasChildren(Object element) {
						return false;				
					}
					public Object[] getElements(Object o) {
						return new Object[]{""}; //$NON-NLS-1$
					}
					public void inputChanged(Viewer v, Object o1,Object o2) {
					
					}
					public void dispose() {
					}
				}
			);
		}
	}

	protected String[] getActionClasses() {
		String[] actions = new String[]{
			"org.jboss.tools.jsf.ui.action.CreateProjectAction", //$NON-NLS-1$
			"org.jboss.tools.jsf.ui.action.ImportProjectAction", //$NON-NLS-1$
			"org.jboss.tools.struts.ui.internal.action.CreateProjectAction", //$NON-NLS-1$
			"org.jboss.tools.struts.ui.internal.action.ImportProjectAction"        			 //$NON-NLS-1$
		};
		return actions;
	}
	
	protected TreeViewerMenuInvoker createMenuInvoker() {
		return new JSFNavigatorMenuInvoker();
	}

	public static void main(String[] args) {
	}

}

class JSFNavigatorMenuInvoker extends NavigatorMenuInvoker {
	private static XModelObject webWorkspace = PreferenceModelUtilities.getPreferenceModel().createModelObject("WebWorkspace", null); //$NON-NLS-1$
	
	protected XModelObject getWorkspaceObject() {
		return webWorkspace;
	}

	protected XActionList getActionList(XModelObject o) {
		XActionList l = o.getModelEntity().getActionList();
		if(o.getModelEntity().getName().equals("FileSystemFolder")) { //$NON-NLS-1$
			l = getWebContextActionList(l);
		} else {
			l = (XActionList)l.copy(acceptor);
		}
		return l;
	}
	
	static XActionList webContextActionList = null;
	
	private  XActionList getWebContextActionList(XActionList l) {
		if(webContextActionList == null) {
			webContextActionList = (XActionList)l.copy(new FileSystemFolder());
		}
		return webContextActionList;
	}
	
	class FileSystemFolder implements XActionItem.Acceptor {
		public boolean accepts(XActionItem item) {
			if("Help".equals(item.getName())) return false; //$NON-NLS-1$
			String path = item.getPath();
			if(path == null) return true;
			int q = path.indexOf('/');
			if(q > 0) return true;
			String s = "." + path + "."; //$NON-NLS-1$ //$NON-NLS-2$
			return ".CreateActions.CopyActions.Properties.".indexOf(s) >= 0; //$NON-NLS-1$
		}
	}

	AcceptorImpl acceptor = new AcceptorImpl();
	static String HIDDEN_ACTIONS = ".Help.Mount.Unmount."; //$NON-NLS-1$

	class AcceptorImpl implements XActionItem.Acceptor {
		public boolean accepts(XActionItem item) {
			if(HIDDEN_ACTIONS.indexOf("." + item.getName() + ".") >= 0) return false; //$NON-NLS-1$ //$NON-NLS-2$
			return true;
		}
	}
}

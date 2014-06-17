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

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.common.model.ui.navigator.*;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.*;
import org.jboss.tools.common.meta.action.*;
import org.jboss.tools.common.model.XModelFactory;
import org.jboss.tools.common.model.XModelObject;
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
		c = new WebProjectsContentProvider();
		TreeViewerModelListenerImpl listener = new WebProjectsTreeListener();
		listener.setViewer(viewer);
		c.setListener(listener);
		contentProvider = c;
		viewer.setContentProvider(contentProvider);
	}

	protected String[] getActionClasses() {
		List<String> actions = new ArrayList<String>();
		actions.add("org.jboss.tools.jsf.ui.action.CreateProjectAction"); //$NON-NLS-1$
		actions.add("org.jboss.tools.jsf.ui.action.ImportProjectAction"); //$NON-NLS-1$
		return actions.toArray(new String[0]);
	}
	
	protected TreeViewerMenuInvoker createMenuInvoker() {
		return new JSFNavigatorMenuInvoker();
	}

	public static void main(String[] args) {
	}

}

class JSFNavigatorMenuInvoker extends NavigatorMenuInvoker {
	private static XModelObject webWorkspace;

	protected XModelObject getWorkspaceObject() {
		if(webWorkspace == null) {
			webWorkspace = XModelFactory.getDefaultInstance().createModelObject("WebWorkspace", null); //$NON-NLS-1$
		}
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

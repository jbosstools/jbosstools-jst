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

import java.util.*;
import org.jboss.tools.common.model.ui.navigator.TreeViewerModelListenerImpl;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.TreeViewer;
import org.jboss.tools.common.model.event.XModelTreeEvent;
import org.jboss.tools.common.model.util.ModelFeatureFactory;
import org.jboss.tools.jst.web.project.WebProject;
import org.jboss.tools.jst.web.ui.WebUiPlugin;

public class WebProjectsTreeListener extends TreeViewerModelListenerImpl {
	static String[][] LISTENERS = new String[][]{
		{WebProject.JSF_NATURE_ID, "org.jboss.tools.jsf.ui.navigator.JsfProjectsTreeListener", "org.jboss.tools.jsf.ui"}, //$NON-NLS-1$ //$NON-NLS-2$
		{WebProject.STRUTS_NATURE_ID, "org.jboss.tools.struts.ui.navigator.StrutsProjectsTreeListener", "org.jboss.tools.struts.ui"} //$NON-NLS-1$ //$NON-NLS-2$
	};
	Map<String,TreeViewerModelListenerImpl> listeners = new HashMap<String,TreeViewerModelListenerImpl>();
	
	public WebProjectsTreeListener() {
		for (int i = 0; i < LISTENERS.length; i++) {
			String nature = LISTENERS[i][0];
			String classname = LISTENERS[i][1];
			String plugin = LISTENERS[i][2];
			if(Platform.getBundle(plugin) == null) {
				continue;
			}
			try {
				TreeViewerModelListenerImpl impl = (TreeViewerModelListenerImpl)ModelFeatureFactory.getInstance().createFeatureInstance(classname);
				if(impl == null) continue;
				listeners.put(nature, impl);
			} catch (ClassCastException e) {
				WebUiPlugin.getPluginLog().logError(e);
			}
		}
	}

	public void setViewer(TreeViewer viewer) {
		super.setViewer(viewer);
		TreeViewerModelListenerImpl[] impls = (TreeViewerModelListenerImpl[])listeners.values().toArray(new TreeViewerModelListenerImpl[0]);
		for (int i = 0; i < impls.length; i++) impls[i].setViewer(viewer);
	}
	
	private TreeViewerModelListenerImpl getListenerImpl(XModelTreeEvent event) {
		String nature = event.getModelObject().getModel().getProperties().getProperty("nature"); //$NON-NLS-1$
		return (nature == null) ? null : (TreeViewerModelListenerImpl)listeners.get(nature);
	}

	public void nodeChanged(XModelTreeEvent event) {
		TreeViewerModelListenerImpl impl = getListenerImpl(event);
		if(impl != null) impl.nodeChanged(event);
	}

	public void structureChanged(XModelTreeEvent event) {
		TreeViewerModelListenerImpl impl = getListenerImpl(event);
		if(impl != null) impl.structureChanged(event);
	}

}

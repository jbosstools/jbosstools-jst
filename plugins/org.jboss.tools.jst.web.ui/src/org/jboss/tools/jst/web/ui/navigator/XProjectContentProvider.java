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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaProject;
import org.jboss.tools.common.model.XFilteredTree;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.util.EclipseResourceUtil;

public class XProjectContentProvider extends XContentProvider {

	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof XModelObject) {
			XModelObject o = (XModelObject)parentElement;
			XFilteredTree filteredTree = getFilteredTree(o);
			if(filteredTree != null) {
				return filteredTree.getChildren(o);
			}
			return o.getChildrenForSave();
		} else if(parentElement instanceof IProject || parentElement instanceof IJavaProject) {
			if(parentElement instanceof IJavaProject) {
				parentElement = ((IJavaProject)parentElement).getProject();
			}
			IProject f = (IProject)parentElement;
			XModelObject o = EclipseResourceUtil.getObjectByResource(f);
			XFilteredTree filteredTree = getFilteredTree(o);
			if(filteredTree == null) {
				filteredTree = getFilteredTree(o);
			}
			if(filteredTree != null) {
				return new Object[]{filteredTree.getRoot()};
			}
			return null;
		}
		return null;
	}

	public Object getParent(Object element) {
		if(element instanceof XModelObject) {
			XModelObject o = (XModelObject)element;
			XFilteredTree filteredTree = getFilteredTree(o);
			if(o == filteredTree.getRoot()) {
				return EclipseResourceUtil.getProject(o);
			}
			return o.getParent();
		} else if(element instanceof IProject) {
			IResource r = (IResource)element;
			return r.getParent();
		}
		return null;
	}

}

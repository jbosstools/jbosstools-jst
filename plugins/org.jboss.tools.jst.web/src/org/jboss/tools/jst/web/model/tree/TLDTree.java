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
package org.jboss.tools.jst.web.model.tree;

import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.util.XModelObjectLoaderUtil;
import org.jboss.tools.jst.web.project.WebProject;

public class TLDTree implements XFilteredTree {
	XModel model;
    XModelObject root;
	XModelObject[] children = null;

	public void setModel(XModel model) {
		this.model = model;
		root = model.getByPath("FileSystems/WEB-ROOT"); //$NON-NLS-1$
	}

	public XModelObject getRoot() {
		return root;
	}

	public boolean hasChildren(XModelObject object) {
		return object == root || (root != null && object == root.getParent());
	}

	public XModelObject[] getChildren(XModelObject object) {
		if(object == root) {
			if(children == null) {
				XModelObject[] os = WebProject.getInstance(model).getTaglibMapping().getTaglibObjects().values().toArray(new XModelObject[0]);
				children = os;
			}
			return children;
		}
		return new XModelObject[0];
	}

	public XModelObject getChildAt(XModelObject object, int i) {
		if(object == root) {
			getChildren(object);
			if(children != null) return children[i];
		}
		return null;
	}

	public boolean isSelectable(XModelObject object) {
		return object != root;
	}

	public String getValue(XModelObject object) {
		if(object == root) return ""; //$NON-NLS-1$
		return getPath(object);
	}

    public String getPath(XModelObject object) {
        String p = object.getPath();
        if(p == null || !p.startsWith("FileSystems/")) return p; //$NON-NLS-1$
        return XModelObjectLoaderUtil.getResourcePath(object);
    }

	public void setConstraint(Object object) {
	}

    public XModelObject find(String value) {
    	if(value == null || value.length() == 0) return root;
        return model.getByPath(value);
    }

	public XModelObject getParent(XModelObject object) {
		return (object == root) ? null : root;
	}

	public void dispose() {
	}

}

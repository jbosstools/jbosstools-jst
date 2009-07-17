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

import java.util.*;
import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.util.XModelObjectLoaderUtil;
import org.jboss.tools.common.model.filesystems.FileSystemsHelper;
import org.jboss.tools.common.model.filesystems.XFileObject;
import org.jboss.tools.common.model.impl.trees.*;

public class WebPagesTree extends FileSystemResourceTree {
    protected XModelObject fsr;

    public WebPagesTree() {}

	public XModelObject getRoot() {
		XModelObject r = FileSystemsHelper.getWebRoot(model);
		return (r != null) ? r : super.getRoot(); 
	}

    public XModelObject[] getChildren(XModelObject object) {
        if(!hasChildren(object)) return new XModelObject[0];
        List<XModelObject> l = new ArrayList<XModelObject>();
        XModelObject[] cs = object.getChildren();
        for (int i = 0; i < cs.length; i++) if(accept(cs[i])) l.add(cs[i]);
        return l.toArray(new XModelObject[0]);
    }

	public XModelObject getParent(XModelObject object) {
		return (object == getRoot()) ? null : object.getParent();
	}

    private boolean accept(XModelObject c) {
        if(c.getFileType() == XFileObject.FOLDER) {
            String overlapped = c.get("overlapped"); //$NON-NLS-1$
            if(overlapped != null && overlapped.length() > 0) {
            	String overlappedSystem = c.get("overlappedSystem"); //$NON-NLS-1$
            	if(!"FileSystems/WEB-INF".equals(overlappedSystem)) return false; //$NON-NLS-1$
            } 
        } else if(c.getFileType() == XFileObject.FILE) {
        	String nm = c.getAttributeValue("name"); //$NON-NLS-1$
        	if(nm.length() == 0) return false;
        	if(!accepts0(c)) return false;
        }
        return true;
    }

	public boolean isSelectable(XModelObject object) {
		return (object != null && (object.getFileType() == XFileObject.FILE || object.getFileType() == XFileObject.FOLDER));
	}

    public String getPath(XModelObject o) {
        if(o == fsr) return ""; //$NON-NLS-1$
        String s = XModelObjectLoaderUtil.getResourcePath(o);
        String p = o.getPath();
        if(p == null) return ""; //$NON-NLS-1$
        int b = "FileSystems/".length(), e = p.length() - s.length(); //$NON-NLS-1$
        if(e < b) return ""; //$NON-NLS-1$
        p = p.substring(b, e);
           if(o.getFileType() == XFileObject.FOLDER) s += "/"; //$NON-NLS-1$
        return s;
    }

}


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
package org.jboss.tools.jst.web.tiles.model.handlers;
import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.filesystems.*;
import org.jboss.tools.common.model.util.*;
import org.jboss.tools.common.meta.*;
import org.jboss.tools.jst.web.project.WebModuleConstants;

public class TilesDefinitionAdopt implements XAdoptManager {

    public boolean isAdoptable(XModelObject target, XModelObject object) {
        String entity = object.getModelEntity().getName();
        return target != null && target.isObjectEditable() &&
               (entity.startsWith("StrutsAction") ||
               (entity.equals("FileJSP") || entity.startsWith("FileHTML")));
    }

    public void adopt(XModelObject target, XModelObject object, java.util.Properties p) {
        String entity = object.getModelEntity().getName();
        if(entity.startsWith("StrutsAction")) adoptAction(target, object);
        if(entity.equals("FileJSP") || entity.startsWith("FileHTML")) adoptPage(target, object);
    }

    private void adoptAction(XModelObject target, XModelObject object) {
        String path = object.getAttributeValue("path");
        XModelObject cg = object;
        while(cg != null && !cg.getModelEntity().getName().startsWith("StrutsConfig")) cg = cg.getParent();
        path = getModuleForConfig(cg) + path;
        target.getModel().changeObjectAttribute(target, "controllerUrl", path);
    }

    private void adoptPage(XModelObject target, XModelObject object) {
        String path = XModelObjectLoaderUtil.getResourcePath(object);
        if(path == null) return;
        XModelObject fs = object;
        while(fs != null && fs.getFileType() != XFileObject.SYSTEM) fs = fs.getParent();
        path = getModuleForFileSystem(fs) + path;
        target.getModel().changeObjectAttribute(target, "path", path);
    }

    private String getModuleForConfig(XModelObject cg) {
        if(cg == null) return "";
        String mp = XModelObjectLoaderUtil.getResourcePath(cg);
        XModelObject[] ms = cg.getModel().getByPath("Web").getChildren();
        for (int i = 0; i < ms.length; i++)
          if(ms[i].getAttributeValue("model path").equals(mp)) return ms[i].getAttributeValue("name");
        return "";
    }

    private String getModuleForFileSystem(XModelObject fs) {
///		return (fs == null) ? "" : WebModulesHelper.getInstance(fs.getModel()).getModuleForFileSystem(fs);
    	if(fs == null) return "";
		XModelObject web = fs.getModel().getByPath("Web");
		XModelObject[] ms = web.getChildren(WebModuleConstants.ENTITY_WEB_MODULE);
		String n = fs.getAttributeValue("name");
		for (int i = 0; i < ms.length; i++) {
			if(n.equals(ms[i].getAttributeValue(WebModuleConstants.ATTR_ROOT_FS))) return ms[i].getAttributeValue("name");
		}
		return "";
    }

}


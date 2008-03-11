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
package org.jboss.tools.jst.web.model.handlers;

import java.util.*;
import org.jboss.tools.common.model.*;
import org.jboss.tools.common.meta.action.impl.*;

public class ShowHideCommentsHandler extends AbstractHandler {

    public ShowHideCommentsHandler() {}

    public boolean isEnabled(XModelObject object) {
        if(object == null || !object.isObjectEditable()) return false;
        String commentEntity = action.getProperty("commentEntity");
        if(commentEntity == null) return false;
        if(object.getChildren(commentEntity).length == 0) return false;
        String attr = object.getAttributeValue("hide comments");
        String displayName = ("no".equals(attr)) ? "Hide Comments" : "Show Comments";
        ((XActionImpl)action).setDisplayName(displayName);
        return true;
    }

    public void executeHandler(XModelObject object, Properties p) throws Exception {
        if(!isEnabled(object)) return;
        String attr = object.getAttributeValue("hide comments");
        attr = ("no".equals(attr)) ? "yes" : "no";
        object.getModel().changeObjectAttribute(object, "hide comments", attr);
    }
    
}

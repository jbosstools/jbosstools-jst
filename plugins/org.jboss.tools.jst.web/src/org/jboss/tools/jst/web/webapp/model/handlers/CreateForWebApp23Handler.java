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
package org.jboss.tools.jst.web.webapp.model.handlers;

import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.filesystems.XFileObject;
import org.jboss.tools.common.meta.action.impl.handlers.*;

public class CreateForWebApp23Handler extends DefaultCreateHandler {

    public boolean isEnabled(XModelObject object) {
        if(!super.isEnabled(object)) return false;
        XModelObject f = object;
        while(f != null && f.getFileType() != XFileObject.FILE) f = f.getParent();
        if(f == null) return false;
        String publicId = "" + f.get("publicId"); //$NON-NLS-1$ //$NON-NLS-2$
        return (publicId.indexOf("2.2") < 0); //$NON-NLS-1$
    }

}


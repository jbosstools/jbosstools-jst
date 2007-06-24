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
package org.jboss.tools.jst.web.tomcat;

import org.eclipse.swt.graphics.Image;
import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.icons.impl.*;
import org.jboss.tools.jst.web.WebModelPlugin;

public class TomcatStateIcon implements ImageComponent {

    public TomcatStateIcon() {}

    public int getHash(XModelObject obj) {
        boolean b = false;//TomcatProcess.getInstance().isRunning();
        return (b) ? 72618 : 37156;
    }

    public Image getImage(XModelObject obj) {
        try {
            boolean b = false;//TomcatProcess.getInstance().isRunning();
                String s = (b) ? "struts.tomcat.running" : "struts.tomcat.stopped";
                return obj.getModelEntity().getMetaModel().getIconList().getImage(s, "default.unknown");
            } catch (Exception e) {
    			WebModelPlugin.log(e);
                return null;
            }
    }

}

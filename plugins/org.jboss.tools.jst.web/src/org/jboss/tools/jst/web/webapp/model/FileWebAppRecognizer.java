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
package org.jboss.tools.jst.web.webapp.model;

import org.eclipse.core.runtime.FileLocator;
import org.jboss.tools.common.model.loaders.*;
import org.jboss.tools.common.xml.XMLEntityResolver;

public class FileWebAppRecognizer implements EntityRecognizer {
    static {
        try {
            XMLEntityResolver.registerPublicEntity(WebAppConstants.DOC_PUBLICID, FileLocator.resolve(FileWebAppRecognizer.class.getResource("/meta/web-app_2_3.dtd")).toString());
            XMLEntityResolver.registerPublicEntity(WebAppConstants.DOC_PUBLICID_2_3, FileLocator.resolve(FileWebAppRecognizer.class.getResource("/meta/web-app_2_3.dtd")).toString());
        } catch (Exception e) {}
    }

    public String getEntityName(String ext, String body) {
        return (body == null || !"xml".equals(ext)) ? null :                 
               (body.indexOf(WebAppConstants.DOC_PUBLICID) >= 0 ||
                body.indexOf(WebAppConstants.DOC_PUBLICID_2_3) >= 0) ? "FileWebApp" :
		       (body.indexOf("<web-app") >= 0 
		        && body.indexOf("version=\"2.4\"") > 0
		        && body.indexOf("xmlns=\"http://java.sun.com/xml/ns/j2ee\"") > 0) ? "FileWebApp24" :
		       (body.indexOf("<web-app") >= 0 
				        && body.indexOf("version=\"2.5\"") > 0
				        && body.indexOf("xmlns=\"http://java.sun.com/xml/ns/javaee\"") > 0) ? "FileWebApp25" :
               null;
    }

}


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

import java.io.IOException;

import org.jboss.tools.common.model.loaders.*;
import org.jboss.tools.common.xml.XMLEntityResolver;
import org.jboss.tools.jst.web.WebModelPlugin;

public class FileWebAppRecognizer implements EntityRecognizer {
    static {
        try {
            XMLEntityResolver.registerPublicEntity(WebAppConstants.DOC_PUBLICID, FileWebAppRecognizer.class, "/meta/web-app_2_3.dtd"); //$NON-NLS-1$
            XMLEntityResolver.registerPublicEntity(WebAppConstants.DOC_PUBLICID_2_3, FileWebAppRecognizer.class, "/meta/web-app_2_3.dtd"); //$NON-NLS-1$
        } catch (IOException e) {
        	WebModelPlugin.getPluginLog().logError(e);
        }
    }

    public String getEntityName(EntityRecognizerContext context) {
    	String body = context.getBody();
    	if((body == null || !"xml".equals(context.getExtension()))) return null; //$NON-NLS-1$
		XMLRecognizerContext xml = context.getXMLContext();
		if(xml.isDTD()) {
			String publicId = xml.getPublicId();
        	if(!"web-app".equals(xml.getRootName())) return null; //$NON-NLS-1$
        	if(WebAppConstants.DOC_PUBLICID.equals(publicId)) return "FileWebApp"; //$NON-NLS-1$
        	if(WebAppConstants.DOC_PUBLICID_2_3.equals(publicId)) return "FileWebApp"; //$NON-NLS-1$
        	if(xml.getSystemId() != null && xml.getSystemId().indexOf("web-app_2_3.dtd") >= 0) return "FileWebApp"; //$NON-NLS-1$ //$NON-NLS-2$
        	return null;
        }

    	return (body.indexOf("<web-app") >= 0  //$NON-NLS-1$
		        && body.indexOf("version=\"2.4\"") > 0 //$NON-NLS-1$
		        && body.indexOf("xmlns=\"http://java.sun.com/xml/ns/j2ee\"") > 0) ? "FileWebApp24" : //$NON-NLS-1$ //$NON-NLS-2$
		       (body.indexOf("<web-app") >= 0  //$NON-NLS-1$
				        && body.indexOf("version=\"2.5\"") > 0 //$NON-NLS-1$
				        && body.indexOf("xmlns=\"http://java.sun.com/xml/ns/javaee\"") > 0) ? "FileWebApp25" : //$NON-NLS-1$ //$NON-NLS-2$
               null;
    }

}


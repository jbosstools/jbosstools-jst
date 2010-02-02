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
package org.jboss.tools.jst.web.tiles.model;

import java.io.IOException;

import org.jboss.tools.common.model.loaders.EntityRecognizer;
import org.jboss.tools.common.model.loaders.EntityRecognizerContext;
import org.jboss.tools.common.model.loaders.XMLRecognizerContext;
import org.jboss.tools.common.model.plugin.ModelPlugin;
import org.jboss.tools.common.xml.XMLEntityResolver;

public class FileTilesRecognizer implements EntityRecognizer {
    static {
        try {
            XMLEntityResolver.registerPublicEntity(TilesConstants.DOC_PUBLICID, FileTilesRecognizer.class, "/meta/tiles_config_1_1.dtd"); //$NON-NLS-1$
        } catch (IOException e) {
        	ModelPlugin.getPluginLog().logError(e);
        }
    }

    public String getEntityName(EntityRecognizerContext context) {
    	String body = context.getBody();
        if(body == null || !"xml".equals(context.getExtension())) return null; //$NON-NLS-1$
		XMLRecognizerContext xml = context.getXMLContext();
		if(xml.isDTD()) {
			String publicId = xml.getPublicId();
			if(TilesConstants.DOC_PUBLICID.equals(publicId)) return "FileTiles"; //$NON-NLS-1$
		}
        return null;
    }

}


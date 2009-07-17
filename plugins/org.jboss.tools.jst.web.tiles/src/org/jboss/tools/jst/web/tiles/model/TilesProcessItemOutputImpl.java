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

public class TilesProcessItemOutputImpl extends TilesProcessItemImpl {

    public String getPresentationString() {
        String title = getAttributeValue("path"); //$NON-NLS-1$
        if(title == null) title = getAttributeValue(ATT_NAME);
        return "" + title; //$NON-NLS-1$
    }

}

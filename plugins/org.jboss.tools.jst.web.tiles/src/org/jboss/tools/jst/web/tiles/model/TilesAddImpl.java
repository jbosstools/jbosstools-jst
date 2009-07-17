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

import org.jboss.tools.common.model.impl.*;

public class TilesAddImpl extends RegularObjectImpl {

    public TilesAddImpl() {}

    public String name() {
        return "" + System.identityHashCode(this); //$NON-NLS-1$
    }

    public String getPresentationString() {
        String value = "" + getAttributeValue("value"); //$NON-NLS-1$ //$NON-NLS-2$
        if(value.length() > 33) value = value.substring(30) + "..."; //$NON-NLS-1$
        value = value.replace('\n', ' ');
        return "add:" + value; //$NON-NLS-1$
    }

}

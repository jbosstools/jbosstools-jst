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
package org.jboss.tools.jst.web.tld.model;

import org.jboss.tools.common.meta.*;
import org.jboss.tools.common.model.loaders.impl.SimpleWebFileLoader;
import org.jboss.tools.common.model.util.*;

public class FileTLDLoader extends SimpleWebFileLoader implements TLDConstants {

    public FileTLDLoader() {}

    protected XModelObjectLoaderUtil createUtil() {
        return new TLDLoaderUtil();
    }

}

class TLDLoaderUtil extends XModelObjectLoaderUtil {
    static String required = "!name!tagclass!tlibversion!shortname!"; //$NON-NLS-1$

    protected boolean isSaveable(XModelEntity entity, String n, String v, String dv) {
        if(v == null) return false;
        if(v.length() == 0) return (required.indexOf("!" + n + "!") >= 0); //$NON-NLS-1$ //$NON-NLS-2$
        return super.isSaveable(entity, n, v, dv);
    }

}


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

import org.jboss.tools.common.meta.action.impl.handlers.DefaultRemoveHandler;
import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.impl.*;

public class TLDValidatorImpl extends OrderedObjectImpl {
	private static final long serialVersionUID = 1L;
	
    public String name() {
        return "Validator"; //$NON-NLS-1$
    }
    
    public boolean isEmpty() {
    	if(getChildren().length > 0) return false;
    	if(getAttributeValue("validator-class").length() > 0) return false;  //$NON-NLS-1$
		if(getAttributeValue("description").length() > 0) return false; //$NON-NLS-1$
		return true; 
    }
    
    public void delete() throws XModelException {
    	XModelObject[] cs = getChildren();
    	for (int i = 0; i < cs.length; i++) {
    		DefaultRemoveHandler.removeFromParent(cs[i]);
    	}
		getModel().changeObjectAttribute(this, "validator-class", ""); //$NON-NLS-1$ //$NON-NLS-2$
		getModel().changeObjectAttribute(this, "description", ""); //$NON-NLS-1$ //$NON-NLS-2$
    }

}


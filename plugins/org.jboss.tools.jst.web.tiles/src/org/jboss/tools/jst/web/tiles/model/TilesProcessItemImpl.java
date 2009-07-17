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

import org.eclipse.swt.graphics.Image;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.impl.OrderedObjectImpl;
import org.jboss.tools.jst.web.model.ReferenceObject;

public class TilesProcessItemImpl extends OrderedObjectImpl implements ReferenceObject, TilesConstants {
	private static final long serialVersionUID = 1L;
	protected XModelObject reference;
    protected long referenceTimeStamp = -1;

    public TilesProcessItemImpl() {}

    public XModelObject getReference() {
        return reference;
    }

    public void setReference(XModelObject reference) {
    	if(this.reference != reference) {
			referenceTimeStamp = -1;
    	}
        this.reference = reference;
        if(reference != null) {
            String shape = get("SHAPE"); //$NON-NLS-1$
            if(shape != null && shape.length() > 0) reference.set("_shape", shape); //$NON-NLS-1$
        }
    }
    
    public boolean isUpToDate() {
    	return reference == null || reference.getTimeStamp() == referenceTimeStamp;
    }
    
    public void notifyUpdate() {
		referenceTimeStamp = (reference == null) ? -1 : reference.getTimeStamp();
    }

    public String getPresentationString() {
        return getAttributeValue(ATT_NAME);
    }

    public Image getIcon() {
        return (reference != null) ? reference.getImage() : super.getImage();
    }

    public void set(String name, String value) {
        if("SHAPE".equals(name) && reference != null) { //$NON-NLS-1$
            reference.set("_shape", value); //$NON-NLS-1$
        }
        super.set(name, value);
    }

}

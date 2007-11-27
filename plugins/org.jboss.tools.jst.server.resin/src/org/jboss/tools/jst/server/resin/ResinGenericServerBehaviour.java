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
package org.jboss.tools.jst.server.resin;

import org.eclipse.jst.server.generic.core.internal.GenericServerBehaviour;

/**
 * Server behaviour delegate implementation for Resin server.
 *
 * @author Eric
 */
public class ResinGenericServerBehaviour extends GenericServerBehaviour {

	/* (non-Javadoc)
     * @see org.eclipse.wst.server.core.model.ServerBehaviourDelegate#stop(boolean)
     */
    public void stop(boolean force) {
    	// Allways terminate Resin.
    	terminate();
    }  
}
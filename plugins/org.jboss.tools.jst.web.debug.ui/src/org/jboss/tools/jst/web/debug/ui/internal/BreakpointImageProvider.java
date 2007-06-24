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
package org.jboss.tools.jst.web.debug.ui.internal;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.jdt.debug.core.IJavaBreakpoint;
import org.eclipse.ui.internal.ide.IMarkerImageProvider;

/**
 * @author Jeremy
 */
public class BreakpointImageProvider implements IMarkerImageProvider { 
    private static final String iconPath = "images/xstudio/debug/xpl/";//$NON-NLS-1$       

    public BreakpointImageProvider() {
    }
    
    /**
     * Returns the relative path for the image
     * to be used for displaying an marker in the workbench.
     * This path is relative to the plugin location
     *
     * Returns <code>null</code> if there is no appropriate image.
     *
     * @param marker The marker to get an image path for.
     *
     * @see org.eclipse.ui.internal.IMarkerImageProvider#getImagePath(org.eclipse.core.resources.IMarker)
     */
    public String getImagePath(IMarker marker) {
        IBreakpoint bp = getBreakpoint(marker);
        if (bp != null && bp instanceof IJavaBreakpoint) {
            IJavaBreakpoint jlbp = (IJavaBreakpoint)bp;
            try {
                boolean installed = jlbp.isInstalled();
                boolean enabled = jlbp.isEnabled();
                if (enabled) {
                    if (installed) return iconPath + "brkpi_obj.gif"; //$NON-NLS-1$
                    return iconPath + "brkp_obj.gif";//$NON-NLS-1$
                }
                if (installed) return iconPath + "brkpid_obj.gif"; //$NON-NLS-1$
                return iconPath + "brkpd_obj.gif";//$NON-NLS-1$
            } catch (CoreException e) {
            	//ignore
            }
        }
        return null;
    }

    
    protected IBreakpoint getBreakpoint(IMarker marker) {
        return DebugPlugin.getDefault().getBreakpointManager().getBreakpoint(marker);
    }
}

/*
 * CreateWebPrjHandler.java
 *
 * Created on March 7, 2003, 3:40 PM
 */

package org.jboss.tools.jst.web.project.handlers;

import org.jboss.tools.common.meta.action.impl.AbstractHandler;
import org.jboss.tools.common.model.*;
import org.jboss.tools.jst.web.project.helpers.NewWebProjectHelper;

import java.util.*;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;

/**
 * @author  valera
 */
public class CreateWebPrjHandler extends AbstractHandler {
    
    protected static NewWebProjectHelper helper = new NewWebProjectHelper();

    public CreateWebPrjHandler() {}

    public void executeHandler(XModelObject object, Properties p) throws XModelException {
        if (p == null) p = new Properties();
        
        helper.createProject(object, p);
		IProject project = (IProject)object.getModel().getProperties().get("project"); //$NON-NLS-1$
		try {
			project.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		} catch (CoreException e) {
			throw new XModelException(e);
		}
    }

}

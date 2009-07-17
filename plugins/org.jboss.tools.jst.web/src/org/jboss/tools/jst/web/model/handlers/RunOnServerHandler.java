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
package org.jboss.tools.jst.web.model.handlers;

import java.util.Properties;
import org.jboss.tools.common.meta.action.impl.AbstractHandler;
import org.jboss.tools.common.model.*;
import org.jboss.tools.jst.web.browser.IPathSource;
import org.jboss.tools.jst.web.browser.wtp.RunOnServerContext;

public class RunOnServerHandler extends AbstractHandler {
	static RunOnServerContext context = RunOnServerContext.getInstance();
	
	static {
		context.addPathSource(new IPathSourceImpl());
	}
	
    public RunOnServerHandler() {}

    public boolean isEnabled(XModelObject object) {
        if(object == null || !object.isActive()) return false;
        if(object == object.getModel().getRoot()) return true;
        return false;
    }
    
    static class IPathSourceImpl implements IPathSource {
		public String computeURL(XModelObject object) {
			XModel model = object.getModel();
			boolean isRoot = model.getRoot() == object;
			if(isRoot) {
				String result = context.getBrowserPrefix(object.getModel());
				if(result != null && !result.endsWith("/")) result += "/"; //$NON-NLS-1$ //$NON-NLS-2$
				return result;
			}
			return null;
		}
    }
    
    public void executeHandler(XModelObject object, Properties p) throws XModelException {
        if(!isEnabled(object)) return;
        context.execute(object);
    }

}

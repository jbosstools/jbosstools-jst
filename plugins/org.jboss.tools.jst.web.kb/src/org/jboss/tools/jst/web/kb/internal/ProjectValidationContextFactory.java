/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.internal;

import org.eclipse.core.resources.IProject;
import org.jboss.tools.common.validation.IProjectValidationContext;
import org.jboss.tools.common.validation.IProjectValidationContextFactory;
import org.jboss.tools.jst.web.kb.IKbProject;
import org.jboss.tools.jst.web.kb.KbProjectFactory;

public class ProjectValidationContextFactory implements	IProjectValidationContextFactory {

	public IProjectValidationContext getContext(IProject project) {
		IKbProject kb = KbProjectFactory.getKbProject(project, true);
		return kb == null ? null : kb.getValidationContext();
	}

}

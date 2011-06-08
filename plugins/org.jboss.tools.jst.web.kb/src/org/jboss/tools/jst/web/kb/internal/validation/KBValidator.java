/******************************************************************************* 
 * Copyright (c) 2010 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.jst.web.kb.internal.validation;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.jboss.tools.jst.web.kb.IKbProject;
import org.jboss.tools.jst.web.kb.KbProjectFactory;
import org.jboss.tools.jst.web.kb.validation.IProjectValidationContext;
import org.jboss.tools.jst.web.kb.validation.IValidatingProjectSet;
import org.jboss.tools.jst.web.kb.validation.IValidatingProjectTree;
import org.jboss.tools.jst.web.kb.validation.IValidator;

/**
 * @author Alexey Kazakov
 */
public abstract class KBValidator extends ValidationErrorManager implements IValidator {

	protected boolean notValidatedYet(IResource resource) {
		IProject pr = resource.getProject();
		return coreHelper==null || !coreHelper.getValidationContextManager().projectHasBeenValidated(this, pr);
	}

	/**
	 * Creates a simple validating project tree for the project.
	 * 
	 * @param project
	 * @return
	 */
	public static IValidatingProjectTree createSimpleValidatingProjectTree(IProject project) {
		Set<IProject> projects = new HashSet<IProject>();
		projects.add(project);
		IKbProject kbProject = KbProjectFactory.getKbProject(project, false);
		if(kbProject!=null) {
			IProjectValidationContext rootContext = kbProject.getValidationContext();
			IValidatingProjectSet projectSet = new ValidatingProjectSet(project, projects, rootContext);
			return new SimpleValidatingProjectTree(projectSet);
		}
		return new SimpleValidatingProjectTree(project);
	}
}
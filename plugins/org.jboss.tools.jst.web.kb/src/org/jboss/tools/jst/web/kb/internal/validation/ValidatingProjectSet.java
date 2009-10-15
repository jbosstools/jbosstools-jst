/******************************************************************************* 
 * Copyright (c) 2009 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.internal.validation;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.jboss.tools.jst.web.kb.validation.IValidatingProjectSet;
import org.jboss.tools.jst.web.kb.validation.IValidationContext;

/**
 * @author Alexey Kazakov
 */
public class ValidatingProjectSet implements IValidatingProjectSet {

	protected IProject rootProject;
	protected List<IProject> allProjects;
	protected IValidationContext rootContext;

	/**
	 * @param rootProject
	 * @param allProjects
	 * @param rootContext
	 */
	public ValidatingProjectSet(IProject rootProject, List<IProject> allProjects, IValidationContext rootContext) {
		this.rootProject = rootProject;
		this.allProjects = allProjects;
		this.rootContext = rootContext;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidatingProjectSet#getAllProjests()
	 */
	public List<IProject> getAllProjests() {
		return allProjects;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidatingProjectSet#getRootContext()
	 */
	public IValidationContext getRootContext() {
		return rootContext;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidatingProjectSet#getRootProject()
	 */
	public IProject getRootProject() {
		return rootProject;
	}
}
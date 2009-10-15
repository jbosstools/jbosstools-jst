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
package org.jboss.tools.jst.web.kb.validation;

import java.util.List;

import org.eclipse.core.resources.IProject;

/**
 * Represents a set of projects which are being validated.  
 * @author Alexey Kazakov
 */
public interface IValidatingProjectSet {

	/**
	 * @return the root project which holds a link to validating context for this project set.
	 */
	IProject getRootProject();

	/**
	 * @return all projects of the set.
	 */
	List<IProject> getAllProjests();

	/**
	 * @return Root validating context which is associated with the root project.
	 */
	IValidationContext getRootContext();
}
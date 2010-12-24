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
package org.jboss.tools.jst.web.kb.validation;

import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;

/**
 * @author Alexey Kazakov
 */
public interface IValidationContextManager {

	IValidatingProjectTree getValidatingProjectTree(IValidator validator);

	void addProject(IProject project);

	void clearAll();

	void clearRegisteredFiles();

	void clearAllResourceLinks();

	Set<IFile> getRemovedFiles();

	void addRemovedFile(IFile file);

	Set<IFile> getRegisteredFiles();

	void registerFile(IFile file);

	/**
	 * @return a list of validators which are associated with this context.
	 */
	List<IValidator> getValidators();

	Set<IProject> getRootProjects();

	void addValidatedProject(IValidator validator, IProject project);

	boolean projectHasBeenValidated(IValidator validator, IProject project);
}
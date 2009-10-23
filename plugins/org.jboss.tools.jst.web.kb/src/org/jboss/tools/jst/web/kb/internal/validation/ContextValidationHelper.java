/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.internal.validation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;
import org.eclipse.wst.validation.internal.operations.WorkbenchContext;
import org.jboss.tools.jst.web.kb.validation.IValidatingProjectSet;
import org.jboss.tools.jst.web.kb.validation.IValidationContext;
import org.jboss.tools.jst.web.kb.validation.IValidator;

/**
 * Helper for Validators that use Validator Context. 
 * @author Alexey Kazakov
 */
public class ContextValidationHelper extends WorkbenchContext {

	protected IValidationContext validationContext;
	protected TextFileDocumentProvider documentProvider = new TextFileDocumentProvider();
	protected Map<IProject, IValidationContext> contexts = new HashMap<IProject, IValidationContext>();

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.wst.validation.internal.operations.WorkbenchContext#registerResource(org.eclipse.core.resources.IResource)
	 */
	@Override
	public void registerResource(IResource resource) {
		if(resource instanceof IFile) {
			IFile file = (IFile)resource;
			IValidationContext context = contexts.get(file.getProject());
			if(context==null) {
				context = new ValidationContext(file.getProject());
				contexts.put(file.getProject(), context);
			}
			if(!file.exists()) {
				context.addRemovedFile(file);
			} else {
				context.registerFile(file);
			}
		}
	}

	/**
	 * @return Set of changed resources
	 */
	public Set<IFile> getChangedFiles() {
		List<IValidator> validators = getValidationContext().getValidators();
		Set<IProject> projects = new HashSet<IProject>();
		for (IValidator validator : validators) {
			IValidatingProjectSet set = validator.getValidatingProjects(getProject());
			projects.addAll(set.getAllProjests());
		}
		Set<IFile> result = new HashSet<IFile>();
		String[] uris = getURIs();
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		for (int i = 0; i < uris.length; i++) {
			IFile currentFile = root.getFile(new Path(uris[i]));
			if(projects.contains(currentFile.getProject())) {
				result.add(currentFile);
			}
		}
		Set<IFile> removedFiles = getValidationContext().getRemovedFiles();
		for (IFile file : removedFiles) {
			if(projects.contains(file.getProject())) {
				result.add(file);
			}
		}
		return result;
	}

	public Set<IFile> getProjectSetRegisteredFiles() {
		Set<IFile> result = new HashSet<IFile>();
		List<IValidator> validators = getValidationContext().getValidators();
		Set<IProject> projects = new HashSet<IProject>();
		for (IValidator validator : validators) {
			IValidatingProjectSet set = validator.getValidatingProjects(getProject());
			projects.addAll(set.getAllProjests());
		}
		Set<IFile> files = validationContext.getRegisteredFiles();
		for (IFile file : files) {
			if(projects.contains(file.getProject())) {
				result.add(file);
			}
		}
		return result;
	}

	public IValidationContext getValidationContext() {
		if(validationContext==null) {
			validationContext = new ValidationContext(getProject());
		}
		return validationContext;
	}

	public void setValidationContext(IValidationContext context) {
		validationContext = context;
	}

	public TextFileDocumentProvider getDocumentProvider() {
		return documentProvider;
	}
}
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.jboss.tools.common.el.core.ELReference;
import org.jboss.tools.jst.web.kb.WebKbPlugin;
import org.jboss.tools.jst.web.kb.validation.IValidatingProjectSet;
import org.jboss.tools.jst.web.kb.validation.IValidationContext;
import org.jboss.tools.jst.web.kb.validation.IValidator;
import org.w3c.dom.Element;

/**
 * @author Alexey Kazakov
 */
public class ValidationContext implements IValidationContext {

	private IValidationContext rootContext;
	private IProject rootProject;
	private static List<IValidator> ALL_VALIDATORS;
	private List<IValidator> validators = new ArrayList<IValidator>();

	public ValidationContext(IProject project) {
		if(ALL_VALIDATORS == null) {
			// Load all validators
			ALL_VALIDATORS = new ArrayList<IValidator>();
	        IExtensionRegistry registry = Platform.getExtensionRegistry();
			IExtensionPoint extensionPoint = registry.getExtensionPoint(IValidator.EXTENSION_POINT_ID);
			if (extensionPoint != null) { 
				Map<String, IValidator> extendsIds = new HashMap<String, IValidator>();
				IExtension[] extensions = extensionPoint.getExtensions();
				for (int i=0; i<extensions.length; i++) {
					IExtension extension = extensions[i];
					IConfigurationElement[] elements = extension.getConfigurationElements();
					for(int j=0; j<elements.length; j++) {
						try {
							IValidator validator = (IValidator)elements[j].createExecutableExtension("class"); //$NON-NLS-1$
							String extendsId = elements[j].getAttribute("extends"); //$NON-NLS-1$
							if(extendsId!=null) {
								IValidator[] tempArray = ALL_VALIDATORS.toArray(new IValidator[0]);
								for (IValidator vld : tempArray) {
									if(extendsId.equals(vld.getId())) {
										ALL_VALIDATORS.remove(vld);
									}
								}
								extendsIds.put(extendsId, validator);
							}
							if(!extendsIds.containsKey(validator.getId())) {
								ALL_VALIDATORS.add(validator);
							}
						} catch (CoreException e) {
							WebKbPlugin.getDefault().logError(e);
						}
					}
				}
			}
		}

		// Init context for given project.
		for (IValidator validator : ALL_VALIDATORS) {
			if(validator.shouldValidate(project)) {
				validators.add(validator);
			}
		}
		for (IValidator validator : validators) {
			IValidatingProjectSet prSet = validator.getValidatingProjects(project);
			IProject vProject = prSet.getRootProject();
			if(vProject!=project) {
				rootProject = vProject;
				rootContext = prSet.getRootContext();
			} else if(rootContext==null) {
				rootContext = prSet.getRootContext();
			}
		}
		if(rootProject==null) {
			rootProject = project;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#getValidators()
	 */
	public List<IValidator> getValidators() {
		return validators;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.seam.internal.core.validation.ISeamValidationContext#addLinkedCoreResource(java.lang.String, org.eclipse.core.runtime.IPath, boolean)
	 */
	public void addLinkedCoreResource(String variableName, IPath linkedResourcePath, boolean declaration) {
		rootContext.addLinkedCoreResource(variableName, linkedResourcePath, declaration);	
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.seam.internal.core.validation.ISeamValidationContext#addLinkedEl(java.lang.String, org.jboss.tools.seam.internal.core.validation.ELReference)
	 */
	public void addLinkedEl(String variableName, ELReference el) {
		rootContext.addLinkedEl(variableName, el);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.seam.internal.core.validation.ISeamValidationContext#addRemovedFile(org.eclipse.core.resources.IFile)
	 */
	public void addRemovedFile(IFile file) {
		if(rootContext!=null) {
			rootContext.addRemovedFile(file);
		}
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.seam.internal.core.validation.ISeamValidationContext#addUnnamedCoreResource(org.eclipse.core.runtime.IPath)
	 */
	public void addUnnamedCoreResource(IPath fullPath) {
		rootContext.addUnnamedCoreResource(fullPath);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.seam.internal.core.validation.ISeamValidationContext#addUnnamedElResource(org.eclipse.core.runtime.IPath)
	 */
	public void addUnnamedElResource(IPath fullPath) {
		rootContext.addUnnamedElResource(fullPath);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.seam.internal.core.validation.ISeamValidationContext#addVariableNameForELValidation(java.lang.String)
	 */
	public void addVariableNameForELValidation(String name) {
		rootContext.addVariableNameForELValidation(name);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.seam.internal.core.validation.ISeamValidationContext#clearAll()
	 */
	public void clearAll() {
		rootContext.clearAll();
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.seam.internal.core.validation.ISeamValidationContext#clearAllResourceLinks()
	 */
	public void clearAllResourceLinks() {
		rootContext.clearAllResourceLinks();
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.seam.internal.core.validation.ISeamValidationContext#clearElResourceLinks()
	 */
	public void clearElResourceLinks() {
		rootContext.clearElResourceLinks();
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.seam.internal.core.validation.ISeamValidationContext#clearOldVariableNameForElValidation()
	 */
	public void clearOldVariableNameForElValidation() {
		rootContext.clearOldVariableNameForElValidation();
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.seam.internal.core.validation.ISeamValidationContext#clearRegisteredFiles()
	 */
	public void clearRegisteredFiles() {
		if(rootContext!=null) {
			rootContext.clearRegisteredFiles();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.seam.internal.core.validation.ISeamValidationContext#getCoreResourcesByVariableName(java.lang.String, boolean)
	 */
	public Set<IPath> getCoreResourcesByVariableName(String variableName, boolean declaration) {
		return rootContext.getCoreResourcesByVariableName(variableName, declaration);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.seam.internal.core.validation.ISeamValidationContext#getElsByVariableName(java.lang.String)
	 */
	public Set<ELReference> getElsByVariableName(String variableName) {
		return rootContext.getElsByVariableName(variableName);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.seam.internal.core.validation.ISeamValidationContext#getElsForValidation(java.util.Set, boolean)
	 */
	public Set<ELReference> getElsForValidation(Set<IFile> changedFiles, boolean onlyChangedVariables) {
		return rootContext.getElsForValidation(changedFiles, onlyChangedVariables);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.seam.internal.core.validation.ISeamValidationContext#getRegisteredFiles()
	 */
	public Set<IFile> getRegisteredFiles() {
		if(rootContext==null) {
			return Collections.emptySet();
		}
		return rootContext.getRegisteredFiles();
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.seam.internal.core.validation.ISeamValidationContext#getRemovedFiles()
	 */
	public Set<IFile> getRemovedFiles() {
		if(rootContext==null) {
			return Collections.emptySet();
		}
		return rootContext.getRemovedFiles();
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.seam.internal.core.validation.ISeamValidationContext#getUnnamedCoreResources()
	 */
	public Set<IPath> getUnnamedCoreResources() {
		return rootContext.getUnnamedCoreResources();
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.seam.internal.core.validation.ISeamValidationContext#getUnnamedElResources()
	 */
	public Set<IPath> getUnnamedElResources() {
		return rootContext.getUnnamedElResources();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.seam.internal.core.validation.ISeamValidationContext#getVariableNamesByCoreResource(org.eclipse.core.runtime.IPath, boolean)
	 */
	public Set<String> getVariableNamesByCoreResource(IPath fullPath, boolean declaration) {
		return rootContext.getVariableNamesByCoreResource(fullPath, declaration);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.seam.internal.core.validation.ISeamValidationContext#load(org.w3c.dom.Element)
	 */
	public void load(Element root) {
		rootContext.load(root);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.seam.internal.core.validation.ISeamValidationContext#registerFile(org.eclipse.core.resources.IFile)
	 */
	public void registerFile(IFile file) {
		if(rootContext!=null) {
			rootContext.registerFile(file);
		}
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.seam.internal.core.validation.ISeamValidationContext#removeLinkedCoreResource(org.eclipse.core.runtime.IPath)
	 */
	public void removeLinkedCoreResource(IPath resource) {
		rootContext.removeLinkedCoreResource(resource);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.seam.internal.core.validation.ISeamValidationContext#removeLinkedCoreResource(java.lang.String, org.eclipse.core.runtime.IPath)
	 */
	public void removeLinkedCoreResource(String name, IPath linkedResourcePath) {
		rootContext.removeLinkedCoreResource(name, linkedResourcePath);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.seam.internal.core.validation.ISeamValidationContext#removeLinkedCoreResources(java.util.Set)
	 */
	public void removeLinkedCoreResources(Set<IPath> resources) {
		rootContext.removeLinkedCoreResources(resources);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.seam.internal.core.validation.ISeamValidationContext#removeLinkedEl(java.lang.String, org.jboss.tools.seam.internal.core.validation.ELReference)
	 */
	public void removeLinkedEl(String name, ELReference el) {
		rootContext.removeLinkedEl(name, el);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.seam.internal.core.validation.ISeamValidationContext#removeLinkedEls(java.util.Set)
	 */
	public void removeLinkedEls(Set<IFile> resorces) {
		rootContext.removeLinkedEls(resorces);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.seam.internal.core.validation.ISeamValidationContext#removeUnnamedCoreResource(org.eclipse.core.runtime.IPath)
	 */
	public void removeUnnamedCoreResource(IPath fullPath) {
		rootContext.removeUnnamedCoreResource(fullPath);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.seam.internal.core.validation.ISeamValidationContext#removeUnnamedElResource(org.eclipse.core.runtime.IPath)
	 */
	public void removeUnnamedElResource(IPath fullPath) {
		rootContext.removeUnnamedElResource(fullPath);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.seam.internal.core.validation.ISeamValidationContext#store(org.w3c.dom.Element)
	 */
	public void store(Element root) {
		rootContext.store(root);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#getRootProject()
	 */
	public IProject getRootProject() {
		return rootProject;
	}
}
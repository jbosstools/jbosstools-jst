 /*******************************************************************************
  * Copyright (c) 2007 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributors:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.jst.web.kb.internal.validation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.jboss.tools.common.xml.XMLUtilities;
import org.jboss.tools.jst.web.kb.validation.ELReference;
import org.jboss.tools.jst.web.kb.validation.IValidationContext;
import org.jboss.tools.jst.web.kb.validation.IValidator;
import org.w3c.dom.Element;

/**
 * Contains information for validators that must be saved between
 * validation invoking.
 * @author Alexey Kazakov
 */
public class ProjectValidationContext implements IValidationContext {

	// We should load/save these collections between eclipse sessions.
	private LinkCollection coreLinks = new LinkCollection();
	private ELValidatorContext elLinks = new ELValidatorContext();

	private Set<IFile> removedFiles = new HashSet<IFile>();
	private Set<IFile> registeredResources = new HashSet<IFile>();
	private Set<String> oldVariableNamesForELValidation = new HashSet<String>();

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#addLinkedCoreResource(java.lang.String, org.eclipse.core.runtime.IPath, boolean)
	 */
	public void addLinkedCoreResource(String variableName, IPath linkedResourcePath, boolean declaration) {
		coreLinks.addLinkedResource(variableName, linkedResourcePath, declaration);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#removeLinkedCoreResource(java.lang.String, org.eclipse.core.runtime.IPath)
	 */
	public void removeLinkedCoreResource(String name, IPath linkedResourcePath) {
		coreLinks.removeLinkedResource(name, linkedResourcePath);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#removeLinkedCoreResources(java.util.Set)
	 */
	public void removeLinkedCoreResources(Set<IPath> resources) {
		coreLinks.removeLinkedResources(resources);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#removeLinkedCoreResource(org.eclipse.core.runtime.IPath)
	 */
	public void removeLinkedCoreResource(IPath resource) {
		coreLinks.removeLinkedResource(resource);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#getCoreResourcesByVariableName(java.lang.String, boolean)
	 */
	public Set<IPath> getCoreResourcesByVariableName(String variableName, boolean declaration) {
		return coreLinks.getResourcesByVariableName(variableName, declaration);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#getVariableNamesByCoreResource(org.eclipse.core.runtime.IPath, boolean)
	 */
	public Set<String> getVariableNamesByCoreResource(IPath fullPath, boolean declaration) {
		return coreLinks.getVariableNamesByResource(fullPath, declaration);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#addUnnamedCoreResource(org.eclipse.core.runtime.IPath)
	 */
	public void addUnnamedCoreResource(IPath fullPath) {
		coreLinks.addUnnamedResource(fullPath);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#getUnnamedCoreResources()
	 */
	public Set<IPath> getUnnamedCoreResources() {
		return coreLinks.getUnnamedResources();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#removeUnnamedCoreResource(org.eclipse.core.runtime.IPath)
	 */
	public void removeUnnamedCoreResource(IPath fullPath) {
		coreLinks.removeUnnamedResource(fullPath);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#addUnnamedElResource(org.eclipse.core.runtime.IPath)
	 */
	public void addUnnamedElResource(IPath fullPath) {
		elLinks.addUnnamedResource(fullPath);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#getUnnamedElResources()
	 */
	public Set<IPath> getUnnamedElResources() {
		return elLinks.getUnnamedResources();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#removeUnnamedElResource(org.eclipse.core.runtime.IPath)
	 */
	public void removeUnnamedElResource(IPath fullPath) {
		elLinks.removeUnnamedResource(fullPath);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#addVariableNameForELValidation(java.lang.String)
	 */
	public void addVariableNameForELValidation(String name) {
		oldVariableNamesForELValidation.add(name);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#removeLinkedEls(java.util.Set)
	 */
	public void removeLinkedEls(Set<IFile> resorces) {
		elLinks.removeLinkedEls(resorces);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#getElsForValidation(java.util.Set, boolean)
	 */
	public Set<ELReference> getElsForValidation(Set<IFile> changedFiles, boolean onlyChangedVariables) {
		Set<ELReference> result = new HashSet<ELReference>();
		// Collect all ELs which use new variables names
		for(IResource resource : changedFiles) {
			Set<String> newNames = getVariableNamesByCoreResource(resource.getFullPath(), true);
			if(newNames!=null) {
				for (String newName : newNames) {
					if(!onlyChangedVariables || !oldVariableNamesForELValidation.contains(newName)) {
						Set<ELReference> els = elLinks.getElsByVariableName(newName);
						if(els!=null) {
							result.addAll(els);
						}
					}
				}
			}
			for (String oldName : oldVariableNamesForELValidation) {
				if(!onlyChangedVariables || newNames==null || !newNames.contains(oldName)) {
					Set<ELReference> els = elLinks.getElsByVariableName(oldName);
					if(els!=null) {
						result.addAll(els);
					}
				}
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#clearAll()
	 */
	public void clearAll() {
		removedFiles.clear();
		synchronized (registeredResources) {
			registeredResources.clear();
		}
		oldVariableNamesForELValidation.clear();
		coreLinks.clearAll();
		elLinks.clearAll();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#clearAllResourceLinks()
	 */
	public void clearAllResourceLinks() {
		oldVariableNamesForELValidation.clear();
		coreLinks.clearAll();
		elLinks.clearAll();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#clearRegisteredFiles()
	 */
	public void clearRegisteredFiles() {
		removedFiles.clear();
		synchronized (registeredResources) {
			registeredResources.clear();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#clearElResourceLinks()
	 */
	public void clearElResourceLinks() {
		oldVariableNamesForELValidation.clear();
		elLinks.clearAll();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#clearOldVariableNameForElValidation()
	 */
	public void clearOldVariableNameForElValidation() {
		oldVariableNamesForELValidation.clear();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#addLinkedEl(java.lang.String, org.jboss.tools.jst.web.kb.validation.ELReference)
	 */
	public void addLinkedEl(String variableName, ELReference el) {
		elLinks.addLinkedEl(variableName, el);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#removeLinkedEl(java.lang.String, org.jboss.tools.jst.web.kb.validation.ELReference)
	 */
	public void removeLinkedEl(String name, ELReference el) {
		elLinks.removeLinkedEl(name, el);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#getElsByVariableName(java.lang.String)
	 */
	public Set<ELReference> getElsByVariableName(String variableName) {
		return elLinks.getElsByVariableName(variableName);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#store(org.w3c.dom.Element)
	 */
	public void store(Element root) {
		Element validation = XMLUtilities.createElement(root, "validation"); //$NON-NLS-1$
		Element core = XMLUtilities.createElement(validation, "core"); //$NON-NLS-1$
		coreLinks.store(core);
		Element el = XMLUtilities.createElement(validation, "el"); //$NON-NLS-1$
		elLinks.store(el);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#load(org.w3c.dom.Element)
	 */
	public void load(Element root) {
		Element validation = XMLUtilities.getUniqueChild(root, "validation"); //$NON-NLS-1$
		if(validation == null) return;
		Element core = XMLUtilities.getUniqueChild(validation, "core"); //$NON-NLS-1$
		if(core != null) {
			coreLinks.load(core);
		}
		Element el = XMLUtilities.getUniqueChild(validation, "el"); //$NON-NLS-1$
		if(el != null) {
			elLinks.load(el);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#getRemovedFiles()
	 */
	public Set<IFile> getRemovedFiles() {
		return removedFiles;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#addRemovedFile(org.eclipse.core.resources.IFile)
	 */
	public void addRemovedFile(IFile file) {
		removedFiles.add(file);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#getRegisteredFiles()
	 */
	public Set<IFile> getRegisteredFiles() {
		Set<IFile> copy = new HashSet<IFile>();
		synchronized (registeredResources) {
			copy.addAll(registeredResources);
		}
		return copy;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#registerFile(org.eclipse.core.resources.IFile)
	 */
	public void registerFile(IFile file) {
		synchronized (registeredResources) {
			registeredResources.add(file);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#getRootProject()
	 */
	public IProject getRootProject() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#getValidators()
	 */
	public List<IValidator> getValidators() {
		return null;
	}
}
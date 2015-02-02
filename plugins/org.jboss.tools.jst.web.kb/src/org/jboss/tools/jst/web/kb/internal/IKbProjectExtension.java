/*************************************************************************************
 * Copyright (c) 2015 Red Hat, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     JBoss by Red Hat - Initial implementation.
 ************************************************************************************/
package org.jboss.tools.jst.web.kb.internal;

import java.util.Set;

import org.eclipse.core.resources.IProject;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public interface IKbProjectExtension {

	/**
	 * Returns Eclipse project.
	 * @return
	 */
	public IProject getProject();

	/**
	 * Returns true, if model is fully loaded.
	 * 
	 * @return
	 */
	public boolean isStorageResolved();

	/**
	 * Fully loads model if was not loaded yet.
	 */
	public void resolve();

	/**
	 * Updates model by loaded definitions.
	 * @param updateDependent
	 */
	public void update(boolean updateDependent);

	public Set<IKbProjectExtension> getUsedProjects();

	public void addUsedProject(IKbProjectExtension project);

	public void addDependentProject(IKbProjectExtension project);

	public void removeUsedProject(IKbProjectExtension project);

}

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
package org.jboss.tools.jst.web.kb.test.builder;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.jboss.tools.jst.web.kb.KbProjectFactory;
import org.jboss.tools.jst.web.kb.internal.IIncrementalProjectBuilderExtension;
import org.jboss.tools.jst.web.kb.internal.KbProject;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class KbCobuilder extends IncrementalProjectBuilder implements IIncrementalProjectBuilderExtension {
	static String TEST_MODEL_ID = "KbTest";
	static String TEST_MODEL_OBJECT = "KbTestModel";

	@Override
	public IProject[] build(int kind, Map<String, String> args,
			IProgressMonitor monitor) throws CoreException {
		KbProject p = (KbProject)KbProjectFactory.getKbProject(getProject(), false);
		if(p != null) {
			p.setExtensionModel(TEST_MODEL_ID, TEST_MODEL_OBJECT);
		}
		return null;
	}

}

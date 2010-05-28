/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.web.test;

import java.util.List;
import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.project.Watcher;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.web.project.list.WebPromptingProvider;
import org.jboss.tools.test.util.JobUtils;
import org.jboss.tools.test.util.TestProjectProvider;
/**
 * Tests were moved to jsf.test plugin to fix tycho junit test errors
 * @author eskimo
 *
 */
public class WebContentAssistProviderTest extends TestCase {

	public void testJstComponentRelatedFunctionality() {
		
	}

}
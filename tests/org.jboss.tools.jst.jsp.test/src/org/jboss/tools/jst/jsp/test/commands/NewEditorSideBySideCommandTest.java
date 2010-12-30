/*******************************************************************************
 * Copyright (c) 2007-2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.jsp.test.commands;

import junit.framework.TestCase;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.resources.IProject;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.jboss.tools.test.util.TestProjectProvider;
import org.jboss.tools.test.util.WorkbenchUtils;

/**
 * 
 * Junit test for https://jira.jboss.org/browse/JBIDE-6685 command
 * @author mareshkau
 *
 */
public class NewEditorSideBySideCommandTest extends TestCase {
	
	public static final String NEW_EDITOR_SIDE_BY_SIDE_COMMAND_ID="org.jboss.tools.sidebyside.newEditor"; //$NON-NLS-1$
	protected IProject project = null;
    private TestProjectProvider provider = null;
    private Command newEditorSideBySideCmd;
    public void setUp() throws Exception {
        provider = new TestProjectProvider("org.jboss.tools.jst.jsp.test", null, "JsfJbide1791Test",false);  //$NON-NLS-1$ //$NON-NLS-2$
        project = provider.getProject();
		ICommandService commandService =
			(ICommandService) PlatformUI.getWorkbench()
				.getService(ICommandService.class);
		newEditorSideBySideCmd = commandService.getCommand(
		NEW_EDITOR_SIDE_BY_SIDE_COMMAND_ID);
    }

    protected void tearDown() throws Exception {
    	if(provider != null) {
            provider.dispose();
        }
    }
    /**
     * Test Side by Side command
     * @throws NotDefinedException 
     * @throws NotHandledException 
     * @throws NotEnabledException 
     * @throws ExecutionException 
     */
    public void testNewEditorSideBySideCommand() throws Exception {
    	assertEquals(newEditorSideBySideCmd.getName()+ " should be disabled without opened editor",false,newEditorSideBySideCmd.isEnabled());  //$NON-NLS-1$
    	WorkbenchUtils.openEditor(project.getName()+"/WebContent/pages/newSideBySideEditorTest.txt");  //$NON-NLS-1$
		assertEquals(newEditorSideBySideCmd.getName()+ " should be enabled when editor opened",true,newEditorSideBySideCmd.isEnabled());  //$NON-NLS-1$
    }
}

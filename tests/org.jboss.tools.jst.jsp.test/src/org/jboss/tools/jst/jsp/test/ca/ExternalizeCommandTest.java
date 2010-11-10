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
package org.jboss.tools.jst.jsp.test.ca;

import junit.framework.TestCase;

import org.eclipse.core.commands.Command;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.wst.sse.ui.internal.StructuredTextViewer;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.jsp.test.TestUtil;
import org.jboss.tools.test.util.TestProjectProvider;
import org.jboss.tools.test.util.WorkbenchUtils;

/**
 * Junit for JBIDE-7060
 * 
 * @author mareshkau
 *
 */
public class ExternalizeCommandTest extends TestCase {
	protected IProject project = null;
    private TestProjectProvider provider = null;
    private Command externalizeCommand;
    
    public void setUp() throws Exception {
        provider = new TestProjectProvider("org.jboss.tools.jst.jsp.test", null, "JsfJbide1791Test",false);  //$NON-NLS-1$ //$NON-NLS-2$
        project = provider.getProject();
		ICommandService commandService =
			(ICommandService) PlatformUI.getWorkbench()
				.getService(ICommandService.class);
		externalizeCommand = commandService.getCommand(
		"org.jboss.tools.jst.jsp.commands.i18"); //$NON-NLS-1$
    }

    protected void tearDown() throws Exception {
        if(provider != null) {
            provider.dispose();
        }
    }
    /**
     * Test behaviour of externalize string command
     */
    public void testExternalizeCommand(){
		IEditorPart editorPart = WorkbenchUtils.openEditor(project.getName()+"/WebContent/pages/extCommandTest.xhtml");  //$NON-NLS-1$
		
		assertTrue("Should be opened JSPMultiPage Editor", editorPart instanceof JSPMultiPageEditor); //$NON-NLS-1$

		JSPMultiPageEditor jspMultiPageEditor= (JSPMultiPageEditor) editorPart;
		StructuredTextViewer textViewer = jspMultiPageEditor.getSourceEditor().getTextViewer();
		
		textViewer.setSelectedRange(0, 0);
		checkExternalizeCommand(false);
		textViewer.setSelectedRange(2, 0);
		checkExternalizeCommand(false);
		textViewer.setSelectedRange(15, 0);
		checkExternalizeCommand(true);
		textViewer.setSelectedRange(2, 0);
		checkExternalizeCommand(false);
		
		TestUtil.closeAllEditors();
		checkExternalizeCommand(false);
    }
    
    private void checkExternalizeCommand(boolean requiredState) {
    	TestUtil.waitForIdle(TestUtil.MAX_IDLE);
		assertEquals("Externalize Command has incorrect enabled state", //$NON-NLS-1$
				requiredState,externalizeCommand.isEnabled());
    }
}

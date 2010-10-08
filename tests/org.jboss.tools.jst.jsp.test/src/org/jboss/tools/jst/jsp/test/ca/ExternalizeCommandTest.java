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
		if(editorPart instanceof JSPMultiPageEditor){
			JSPMultiPageEditor jspMultiPageEditor= (JSPMultiPageEditor) editorPart;
			StyledText textWidget = 	jspMultiPageEditor.getSourceEditor().getTextViewer().getTextWidget();
			textWidget.setCaretOffset(0);
			assertEquals("Ext command should be disabled with current selection",false,externalizeCommand.isEnabled()); //$NON-NLS-1$
			textWidget.setCaretOffset(2);
			assertEquals("Ext command should be disabled with current selection",false,externalizeCommand.isEnabled()); //$NON-NLS-1$
			textWidget.setCaretOffset(15);
			assertEquals("Ext command should be enabled with current selection",true,externalizeCommand.isEnabled()); //$NON-NLS-1$
			textWidget.setCaretOffset(2);
			assertEquals("Ext command should be disabled with current selection",false,externalizeCommand.isEnabled()); //$NON-NLS-1$
			TestUtil.closeAllEditors();
			assertEquals("Ext command should be disabled without opened editor",false,externalizeCommand.isEnabled()); //$NON-NLS-1$
		}else{
			fail("Should be opened JSPMultiPage Editor"); //$NON-NLS-1$
		}
		
    }
}

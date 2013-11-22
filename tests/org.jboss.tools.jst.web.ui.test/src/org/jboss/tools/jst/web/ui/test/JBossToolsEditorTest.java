/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.test;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.jboss.tools.common.model.ui.editor.IModelObjectEditorInput;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.test.util.WorkbenchUtils;

import junit.framework.TestCase;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class JBossToolsEditorTest extends TestCase {
	protected IProject project = null;
	IEditorPart editor;

	public JBossToolsEditorTest() {
	}

	public void setUp() {
		project = ResourcesPlugin.getWorkspace().getRoot().getProject("SimpleProject");
	}

	public void testEditor() {
		editor = openEditor("a.html");
		IEditorInput input = editor.getEditorInput();
		assertTrue(input instanceof IModelObjectEditorInput);
		assertTrue(input instanceof IFileEditorInput);
		IEditorInput fileInput = new FileEditorInput(((IFileEditorInput)input).getFile());
		
		//JBIDE-14095
		assertEquals(input.getToolTipText(), fileInput.getToolTipText());
	}	

	public IEditorPart openEditor(String fileName) {
		IFile testfile = project.getFile(fileName);
		assertTrue("Test file doesn't exist: " + project.getName() + "/" + fileName, 
				(testfile.exists() && testfile.isAccessible()));

		IEditorPart editorPart = WorkbenchUtils.openEditor(project.getName()+"/"+ fileName); //$NON-NLS-1$
		assertTrue(editorPart instanceof JSPMultiPageEditor);
		return editorPart;
	}

	protected void tearDown() throws Exception {
		if(editor != null) {
			editor.getSite().getPage().closeEditor(editor, false);
			editor = null;
		}
		super.tearDown();
	}

}

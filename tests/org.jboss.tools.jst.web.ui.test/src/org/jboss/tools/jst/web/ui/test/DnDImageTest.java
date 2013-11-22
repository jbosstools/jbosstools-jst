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

import java.net.MalformedURLException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.common.model.ui.editors.dnd.DropCommandFactory;
import org.jboss.tools.common.model.ui.editors.dnd.IDropCommand;
import org.jboss.tools.common.model.ui.editors.dnd.TagAttributesWizardPage;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.JSPMultiPageEditor;

/**
 * @author Alexey Kazakov
 */
public class DnDImageTest extends AbstractPaletteEntryTest {

	JSPMultiPageEditor editor = null;
	IFile file = null;

	@Override
	public void setUp() {
		project = ResourcesPlugin.getWorkspace().getRoot().getProject("WebProject");
		editor = (JSPMultiPageEditor)openEditor("WebContent/index.html");
		file = project.getFile("WebContent/images/test.gif");
	}

	@Override
	protected void tearDown() throws Exception {
		if(currentDialog != null) {
			currentDialog.close();
		}
		if(editor != null) {
			editor.getSite().getPage().closeEditor(editor, false);
			editor = null;
		}
		super.tearDown();
	}

	public void testNewImgWizard() throws MalformedURLException {
		System.setProperty(IDropCommand.TEST_FLAG, "true");
		editor.runDropCommand(DropCommandFactory.kFileMime, file.getLocationURI().toURL().toString());
		System.getProperties().remove(IDropCommand.TEST_FLAG);
		Shell shell = findShell();
		Object data = shell.getData();
		assertTrue(data instanceof WizardDialog);
		currentDialog = (WizardDialog)data;
		IWizardPage page = ((WizardDialog)data).getCurrentPage();
		assertTrue(page instanceof TagAttributesWizardPage);
	}
}
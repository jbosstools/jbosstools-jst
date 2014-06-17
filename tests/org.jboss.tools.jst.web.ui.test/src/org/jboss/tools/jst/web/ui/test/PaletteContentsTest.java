package org.jboss.tools.jst.web.ui.test;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ui.IEditorPart;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.PagePaletteContents;
import org.jboss.tools.test.util.WorkbenchUtils;

import junit.framework.TestCase;

public class PaletteContentsTest extends TestCase {
	protected IProject project = null;
	protected IEditorPart editor = null;

	public PaletteContentsTest() {}

	public void setUp() {
		project = ResourcesPlugin.getWorkspace().getRoot().getProject("SimpleProject");
	}

	protected void tearDown() throws Exception {
		if(editor != null) {
			editor.getSite().getPage().closeEditor(editor, false);
			editor = null;
		}
		super.tearDown();
	}

	public void testMobilePalette() {
		openEditor("a.html");
		PagePaletteContents p = new PagePaletteContents(editor);
		String[] x = p.getNatureTypes();
		assertEquals(1, x.length);
		assertEquals(PagePaletteContents.TYPE_MOBILE, x[0]);
	}

	public void testJSFPalette() {
		openEditor("c.html");
		PagePaletteContents p = new PagePaletteContents(editor);
		String[] x = p.getNatureTypes();
		assertEquals(1, x.length);
		assertEquals(PagePaletteContents.TYPE_JSF, x[0]);
	}

	public void testFirstPaletteOpening() {
		JSPMultiPageEditor.resetPaletteOpened();
		assertFalse(JSPMultiPageEditor.wasPaletteOpened());
		openEditor("a.html");
		assertTrue(JSPMultiPageEditor.wasPaletteOpened());		
	}

	public IEditorPart openEditor(String fileName) {
		IFile testfile = project.getFile(fileName);
		assertTrue("Test file doesn't exist: " + project.getName() + "/" + fileName, 
				(testfile.exists() && testfile.isAccessible()));

		return editor = WorkbenchUtils.openEditor(project.getName()+"/"+ fileName); //$NON-NLS-1$
	}

}

/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.css.test.jbide;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.jboss.tools.jst.css.common.StyleContainer;
import org.jboss.tools.jst.css.test.AbstractCSSViewTest;
import org.jboss.tools.jst.css.view.CSSEditorView;
import org.jboss.tools.test.util.JobUtils;
import org.w3c.dom.Element;

/**
 * @author Sergey Dzmitrovich
 * 
 */
public class ExtendingCSSViewTest_JBIDE4850 extends AbstractCSSViewTest {

	public static final String TEST_PAGE_NAME = "JBIDE/4850/cssViewTest.jsp"; //$NON-NLS-1$

	/** jsp editor id */
	public static final String JSP_EDITOR_ID = "org.eclipse.jst.jsp.core.jspsource.source";//"org.jboss.tools.jst.jsp.jspeditor.JSPTextEditor"; //$NON-NLS-1$

	public static final String TESTED_ELEMENT_ID = "cssViewTest"; //$NON-NLS-1$

	public static final String TESTED_STYLE_ID = "styleID"; //$NON-NLS-1$

	public void testInlineStyleEditing() throws CoreException,
			SecurityException, IllegalArgumentException, NoSuchFieldException,
			IllegalAccessException {

		IFile pageFile = getComponentPath(TEST_PAGE_NAME, getProjectName());

		assertNotNull(pageFile);

		StructuredTextEditor editor = (StructuredTextEditor) openEditor(
				pageFile, JSP_EDITOR_ID);

		JobUtils.waitForIdle();

		assertNotNull(editor);

		CSSEditorView view = (CSSEditorView) openView(CSS_EDITOR_VIEW);

		assertNotNull(view);

		IDOMModel model = (IDOMModel) getStructuredModel(pageFile);

		assertNotNull(model);

		IDOMDocument document = model.getDocument();

		assertNotNull(document);

		Element element = document.getElementById(TESTED_ELEMENT_ID);

		assertNotNull(element);

		int offset = ((IndexedRegion) element).getStartOffset();

		setSelection(editor, offset, 0);

		Object selectedObject = getSelectedObject(view);

		assertTrue(selectedObject instanceof StyleContainer);

		assertSame(element, ((StyleContainer) selectedObject).getStyleObject());

	}

	public void testStyleTagEditing() throws CoreException, SecurityException,
			IllegalArgumentException, NoSuchFieldException,
			IllegalAccessException {

		IFile pageFile = getComponentPath(TEST_PAGE_NAME, getProjectName());

		assertNotNull(pageFile);

		StructuredTextEditor editor = (StructuredTextEditor) openEditor(
				pageFile, JSP_EDITOR_ID);

		JobUtils.waitForIdle();

		assertNotNull(editor);

		CSSEditorView view = (CSSEditorView) openView(CSS_EDITOR_VIEW);

		assertNotNull(view);

		IDOMModel model = (IDOMModel) getStructuredModel(pageFile);

		assertNotNull(model);

		IDOMDocument document = model.getDocument();

		assertNotNull(document);

		Element element = document.getElementById(TESTED_STYLE_ID);

		assertNotNull(element);

		int offset = ((IDOMElement) element).getStartEndOffset();

		setSelection(editor, offset, 0);

		Object selectedObject = getSelectedObject(view);

		assertTrue(selectedObject instanceof StyleContainer);

	}
}

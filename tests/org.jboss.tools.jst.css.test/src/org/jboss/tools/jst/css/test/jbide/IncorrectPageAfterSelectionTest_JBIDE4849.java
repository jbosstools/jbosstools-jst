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
import org.eclipse.ui.part.IPage;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jboss.tools.jst.css.properties.CSSPropertyPage;
import org.jboss.tools.jst.css.test.AbstractCSSViewTest;
import org.jboss.tools.jst.css.view.CSSEditorView;
import org.jboss.tools.test.util.JobUtils;

/**
 * @author Sergey Dzmitrovich
 * 
 */
public class IncorrectPageAfterSelectionTest_JBIDE4849 extends
		AbstractCSSViewTest {

	public static final String TEST_PAGE_NAME = "JBIDE/4849/incorrectPageTest.css"; //$NON-NLS-1$

	public void testInlineStyleEditing() throws CoreException,
			SecurityException, IllegalArgumentException, NoSuchFieldException,
			IllegalAccessException {

		IFile pageFile = getComponentPath(TEST_PAGE_NAME, getProjectName());

		assertNotNull(pageFile);

		StructuredTextEditor editor = (StructuredTextEditor) openEditor(
				pageFile, CSS_EDITOR_ID);

		JobUtils.waitForIdle();

		assertNotNull(editor);

		CSSEditorView view = (CSSEditorView) openView(CSS_EDITOR_VIEW);

		assertNotNull(view);

		IPage page = view.getDefaultPage();

		assertNotNull(page);

		assertTrue(page instanceof CSSPropertyPage);

	}

}

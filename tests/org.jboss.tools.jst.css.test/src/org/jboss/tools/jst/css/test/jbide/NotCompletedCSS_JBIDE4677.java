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
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.part.IPage;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jboss.tools.jst.web.ui.internal.css.properties.CSSPropertyPage;
import org.jboss.tools.jst.css.test.AbstractCSSViewTest;
import org.jboss.tools.jst.web.ui.internal.css.view.CSSEditorView;
import org.jboss.tools.test.util.JobUtils;

/**
 * @author Sergey Dzmitrovich
 * 
 */
public class NotCompletedCSS_JBIDE4677 extends AbstractCSSViewTest {

	public static final String TEST_PAGE_NAME = "JBIDE/4677/notCompletedCss.css"; //$NON-NLS-1$

	public void testNotCompletedCSS() throws CoreException,
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

		IPage page = view.getCurrentPage();

		assertNotNull(page);

		StructuredSelection selection = (StructuredSelection) ((CSSPropertyPage) page)
				.getCurrentSelection();

		assertNotNull(selection);
		
		assertNull(selection.getFirstElement());

	}

}

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
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.wst.css.core.internal.document.CSSStructuredDocumentRegionContainer;
import org.eclipse.wst.css.core.internal.provisional.document.ICSSModel;
import org.eclipse.wst.css.core.internal.provisional.document.ICSSStyleSheet;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jboss.tools.jst.css.test.AbstractCSSViewTest;
import org.jboss.tools.jst.css.view.CSSEditorView;
import org.jboss.tools.test.util.JobUtils;
import org.w3c.dom.css.CSSRule;

/**
 * @author Sergey Dzmitrovich
 * 
 *         https://jira.jboss.org/jira/browse/JBIDE-4791
 * 
 */
public class SelectionLosingByPropertySheet_JBIDE4791 extends
		AbstractCSSViewTest {

	public static final String TEST_PAGE_NAME = "JBIDE/4791/propertyViewTest.css"; //$NON-NLS-1$

	public static final String PROPERTY_SHEET_VIEW_ID = "org.eclipse.ui.views.PropertySheet"; //$NON-NLS-1$

	public static final String SELECTION_FIELD_NAME = "currentSelection"; //$NON-NLS-1$

	public void testSelectionLosingByPropertySheet() throws CoreException,
			SecurityException, IllegalArgumentException, NoSuchFieldException,
			IllegalAccessException {

		IFile pageFile = getComponentPath(TEST_PAGE_NAME, getProjectName());

		assertNotNull(pageFile);

		PropertySheet propertySheet = (PropertySheet) openView(PROPERTY_SHEET_VIEW_ID);

		assertNotNull(propertySheet);

		StructuredTextEditor editor = (StructuredTextEditor) openEditor(
				pageFile, CSS_EDITOR_ID);

		assertNotNull(editor);

		ICSSModel model = (ICSSModel) getStructuredModel(pageFile);

		assertNotNull(model);

		ICSSStyleSheet document = (ICSSStyleSheet) model.getDocument();

		assertNotNull(document);

		CSSRule cssRule = document.getCssRules().item(0);

		assertNotNull(cssRule);

		int offset = ((CSSStructuredDocumentRegionContainer) cssRule)
				.getStartOffset();

		setSelection(editor, offset, 0);

		ISelection selection = (ISelection) getFieldValue(propertySheet,
				SELECTION_FIELD_NAME);

		assertTrue(selection instanceof IStructuredSelection);

		assertEquals(cssRule, ((IStructuredSelection) selection)
				.getFirstElement());

		CSSEditorView view = (CSSEditorView) openView(CSS_EDITOR_VIEW);

		JobUtils.delay(1000);

		selection = (ISelection) getFieldValue(propertySheet,
				SELECTION_FIELD_NAME);

		assertTrue(selection instanceof IStructuredSelection);

		assertEquals(cssRule, ((IStructuredSelection) selection)
				.getFirstElement());

	}

}

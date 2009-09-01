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

package org.jboss.tools.jst.css.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.internal.views.properties.tabbed.view.TabbedPropertyRegistry;
import org.eclipse.ui.internal.views.properties.tabbed.view.TabbedPropertyViewer;
import org.eclipse.ui.views.properties.tabbed.ITabDescriptor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.eclipse.wst.css.core.internal.document.CSSStructuredDocumentRegionContainer;
import org.eclipse.wst.css.core.internal.provisional.document.ICSSModel;
import org.eclipse.wst.css.core.internal.provisional.document.ICSSStyleSheet;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jboss.tools.jst.css.properties.CSSPropertyPage;
import org.jboss.tools.jst.css.view.CSSEditorView;
import org.jboss.tools.jst.css.view.CSSPreview;
import org.jboss.tools.test.util.JobUtils;
import org.w3c.dom.css.CSSRule;

/**
 * 
 * @author Sergey Dzmitrovich
 * 
 */
public class CSSViewTest extends AbstractCSSViewTest {


	public static final String TEST_PAGE_NAME = "test.css"; //$NON-NLS-1$

	public static final int COUNT_TABS = 5;


	/**
	 * 
	 * @throws PartInitException
	 * @throws CoreException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public void testEditorViewSelection() throws PartInitException,
			CoreException, SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {

		IFile pageFile = getComponentPath(TEST_PAGE_NAME, getProjectName());

		assertNotNull(pageFile);

		StructuredTextEditor editor = (StructuredTextEditor) openEditor(
				pageFile, CSS_EDITOR_ID);

		assertNotNull(editor);

		CSSEditorView view = (CSSEditorView) openView(CSS_EDITOR_VIEW);

		assertNotNull(view);

		CSSPropertyPage page = (CSSPropertyPage) view.getCurrentPage();

		assertNotNull(page);

		ICSSModel model = (ICSSModel) getStructuredModel(pageFile);

		assertNotNull(model);

		ICSSStyleSheet document = (ICSSStyleSheet) model.getDocument();

		assertNotNull(document);

		CSSRule cssRule = document.getCssRules().item(0);

		assertNotNull(cssRule);

		int offset = ((CSSStructuredDocumentRegionContainer) cssRule)
				.getStartOffset();

		setSelection(editor, offset, 0);

		JobUtils.delay(1000);

		Object selectedObject = getFieldValue(page, "selectedObject"); //$NON-NLS-1$

		setSelection(editor, 0, 0);

		JobUtils.delay(1000);

		selectedObject = getFieldValue(page, "selectedObject"); //$NON-NLS-1$

		assertNotSame(cssRule, selectedObject);

	}

	public void testEditorViewTabs() throws CoreException, SecurityException,
			IllegalArgumentException, NoSuchFieldException,
			IllegalAccessException, NoSuchMethodException,
			InvocationTargetException {

		IFile pageFile = getComponentPath(TEST_PAGE_NAME, getProjectName());

		assertNotNull(pageFile);

		StructuredTextEditor editor = (StructuredTextEditor) openEditor(
				pageFile, CSS_EDITOR_ID);

		assertNotNull(editor);

		CSSEditorView view = (CSSEditorView) openView(CSS_EDITOR_VIEW);

		assertNotNull(view);

		CSSPropertyPage page = (CSSPropertyPage) view.getCurrentPage();

		assertNotNull(page);

		ICSSModel model = (ICSSModel) getStructuredModel(pageFile);

		assertNotNull(model);

		ICSSStyleSheet document = (ICSSStyleSheet) model.getDocument();

		assertNotNull(document);

		CSSRule cssRule = document.getCssRules().item(0);

		assertNotNull(cssRule);

		int offset = ((CSSStructuredDocumentRegionContainer) cssRule)
				.getStartOffset();

		setSelection(editor, offset, 0);

		JobUtils.delay(1000);

		TabbedPropertyRegistry registry = (TabbedPropertyRegistry) getFieldValue(
				page, TabbedPropertySheetPage.class, "registry");//$NON-NLS-1$

		ITabDescriptor[] descriptors = (ITabDescriptor[]) getFieldValue(
				registry, "tabDescriptors");//$NON-NLS-1$

		TabbedPropertyViewer tabbedPropertyViewer = (TabbedPropertyViewer) getFieldValue(
				page, TabbedPropertySheetPage.class, "tabbedPropertyViewer");//$NON-NLS-1$

		Method method = Viewer.class.getDeclaredMethod("fireSelectionChanged",
				SelectionChangedEvent.class);
		method.setAccessible(true);

		for (int i = 0; i < descriptors.length; i++) {

			method.invoke(tabbedPropertyViewer, new SelectionChangedEvent(
					tabbedPropertyViewer, new StructuredSelection(
							descriptors[i])));
			JobUtils.delay(2000);

		}

	}

	/**
	 * 
	 * @throws PartInitException
	 * @throws CoreException
	 */
	public void testPreviewView() throws PartInitException, CoreException {

		IFile pageFile = getComponentPath(TEST_PAGE_NAME, getProjectName());

		assertNotNull(pageFile);

		StructuredTextEditor editor = (StructuredTextEditor) openEditor(
				pageFile, CSS_EDITOR_ID);

		assertNotNull(editor);

		CSSPreview view = (CSSPreview) openView(CSS_PREVIEW_VIEW);

		String browserPage = view.generateBrowserPage();

		assertNotNull(view);

		ICSSModel model = (ICSSModel) getStructuredModel(pageFile);

		assertNotNull(model);

		ICSSStyleSheet document = (ICSSStyleSheet) model.getDocument();

		assertNotNull(document);

		CSSRule cssRule = document.getCssRules().item(0);

		assertNotNull(cssRule);

		int offset = ((CSSStructuredDocumentRegionContainer) cssRule)
				.getStartOffset();

		setSelection(editor, offset, 0);

		JobUtils.delay(1000);

		assertFalse(browserPage.equals(view.generateBrowserPage()));

	}

	/**
	 * @throws WorkbenchException
	 * 
	 */
	public void testPerspective() throws WorkbenchException {
		PlatformUI.getWorkbench().showPerspective(CSS_PERSPECTIVE,
				PlatformUI.getWorkbench().getActiveWorkbenchWindow());
	}
}

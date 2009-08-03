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

import java.lang.reflect.Field;

import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.ui.StructuredTextEditor;

/**
 * @author Sergey Dzmitrovich
 * 
 */
public abstract class AbstractCSSViewTest extends TestCase {

	public static final String COMPONENTS_PATH = "WebContent/pages"; //$NON-NLS-1$
	public static final String CSS_EDITOR_VIEW = "org.jboss.tools.jst.css.view.editor"; //$NON-NLS-1$
	public static final String CSS_PREVIEW_VIEW = "org.jboss.tools.jst.css.view.preview"; //$NON-NLS-1$
	public static final String CSS_EDITOR_ID = "org.eclipse.wst.css.core.csssource.source"; //$NON-NLS-1$

	/**
	 * 
	 * @param componentPage
	 * @param projectName
	 * @return
	 * @throws CoreException
	 */
	public static IFile getComponentPath(String componentPage,
			String projectName) throws CoreException {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(
				projectName);
		if (project != null) {
			return (IFile) project.getFolder(COMPONENTS_PATH).findMember(
					componentPage);
		}

		return null;
	}

	/**
	 * 
	 * @param input
	 * @param editorId
	 * @return
	 * @throws PartInitException
	 */
	public IEditorPart openEditor(IFile file, String editorId)
			throws PartInitException {

		return PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().openEditor(new FileEditorInput(file),
						editorId, true);

	}

	/**
	 * 
	 * @param textEditor
	 * @param offset
	 * @param length
	 */
	public void setSelection(StructuredTextEditor textEditor, int offset,
			int length) {

		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.activate(textEditor);

		textEditor.selectAndReveal(offset, length);

	}

	/**
	 * 
	 */
	public void closeAllEditors() {
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.closeAllEditors(false);
	}

	/**
	 * 
	 * @param viewId
	 * @return
	 * @throws PartInitException
	 */
	public IViewPart openView(String viewId) throws PartInitException {

		return PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().showView(viewId);

	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public IStructuredModel getStructuredModel(IFile file) {

		return StructuredModelManager.getModelManager()
				.getExistingModelForRead(file);
	}

	/**
	 * 
	 * @param object
	 * @param fieldName
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public Object getFieldValue(Object object, String fieldName)
			throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		Field selectedObjectField = object.getClass().getDeclaredField(
				fieldName);
		selectedObjectField.setAccessible(true);
		return getFieldValue(object, object.getClass(), fieldName);
	}

	/**
	 * 
	 * @param object
	 * @param className
	 * @param fieldName
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public Object getFieldValue(Object object, Class className, String fieldName)
			throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		Field selectedObjectField = className.getDeclaredField(fieldName);
		selectedObjectField.setAccessible(true);
		return selectedObjectField.get(object);
	}

	@Override
	protected void tearDown() throws Exception {
		closeAllEditors();
		super.tearDown();
	}

	/**
	 * 
	 * @return
	 */
	public abstract String getProjectName();

}

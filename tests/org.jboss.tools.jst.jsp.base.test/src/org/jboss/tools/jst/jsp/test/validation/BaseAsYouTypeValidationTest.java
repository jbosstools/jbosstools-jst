/*******************************************************************************
 * Copyright (c) 2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.jsp.test.validation;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.sse.ui.internal.reconcile.TemporaryAnnotation;
import org.jboss.tools.common.base.test.validation.AbstractAsYouTypeValidationTest;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;

/**
 * 
 * @author Victor V. Rubezhny
 *
 */
@SuppressWarnings("restriction")
public class BaseAsYouTypeValidationTest extends AbstractAsYouTypeValidationTest {
	public static final String MARKER_TYPE = "org.jboss.tools.common.validation.asyoutype"; //$NON-NLS-1$

	public BaseAsYouTypeValidationTest(IProject project) {
		this.project = project;
	}
	public BaseAsYouTypeValidationTest() {
	}
	
	@Override
	protected void obtainEditor(IEditorPart editorPart) {
		JSPMultiPageEditor jspEditor = null;

		if (editorPart instanceof JSPMultiPageEditor)
			jspEditor = (JSPMultiPageEditor) editorPart;

		assertNotNull("Cannot get the JSP Text Editor instance for page \"" //$NON-NLS-1$
						+ fileName + "\"", jspEditor);
		
		// clean deffered events 
		while (Display.getCurrent().readAndDispatch());

		textEditor = jspEditor.getJspEditor();

		assertNotNull("Cannot get the JSP Text Editor instance for page \"" //$NON-NLS-1$
				+ fileName + "\"", textEditor);

		file = ((IFileEditorInput) textEditor.getEditorInput()).getFile();
		assertNotNull("Cannot get the JSP Text Editor instance for page \"" //$NON-NLS-1$
				+ fileName + "\"", file);
	}

	protected ISourceViewer getTextViewer() {
		return ((StructuredTextEditor)textEditor).getTextViewer();
	}

	@Override
	protected boolean isAnnotationAcceptable(Annotation annotation) {
		if (!(annotation instanceof TemporaryAnnotation))
			return false;

		TemporaryAnnotation temporaryAnnotation = (TemporaryAnnotation) annotation;

		if (temporaryAnnotation.getAttributes() == null && temporaryAnnotation.getAttributes().isEmpty())
			return false;
		
		Object value = temporaryAnnotation.getAttributes().get(MARKER_TYPE);
		
		if (Boolean.TRUE != value)
			return false;

		return true;
	}
}

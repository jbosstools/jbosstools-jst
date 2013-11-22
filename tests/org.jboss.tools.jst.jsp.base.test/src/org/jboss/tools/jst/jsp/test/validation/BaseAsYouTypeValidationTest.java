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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.texteditor.MarkerAnnotation;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.sse.ui.internal.reconcile.TemporaryAnnotation;
import org.jboss.tools.common.base.test.validation.AbstractAsYouTypeValidationTest;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.tests.AbstractResourceMarkerTest;

/**
 * 
 * @author Victor V. Rubezhny
 *
 */
@SuppressWarnings("restriction")
public class BaseAsYouTypeValidationTest extends AbstractAsYouTypeValidationTest {
	public static final String MARKER_TYPE = "org.jboss.tools.common.validation.asyoutype"; //$NON-NLS-1$
	public static final String RESOURCE_MARKER_TYPE = "org.jboss.tools.jst.web.kb.elproblem"; //$NON-NLS-1$

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

		if (temporaryAnnotation.getAttributes() == null || temporaryAnnotation.getAttributes().isEmpty())
			return false;
		
		Object value = temporaryAnnotation.getAttributes().get(MARKER_TYPE);
		
		if (Boolean.TRUE != value)
			return false;

		return true;
	}
	
	@Override
	protected boolean isMarkerAnnotationAcceptable(Annotation annotation) {
		if (!(annotation instanceof MarkerAnnotation))
			return false;

		MarkerAnnotation markerAnnotation = (MarkerAnnotation) annotation;

		IMarker marker = markerAnnotation.getMarker();
		String type;
		try {
			type = marker.getType();
			return RESOURCE_MARKER_TYPE.equals(type);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	protected void assertResourceMarkerIsCreated(IFile file, String errorMessage, int line) throws CoreException {
		IMarker[] markers = AbstractResourceMarkerTest.findMarkers(
				file, RESOURCE_MARKER_TYPE, errorMessage, true);

		assertNotNull("Resource Marker not found for type: " + RESOURCE_MARKER_TYPE + ", message: [" + errorMessage + "] at line: " + line, markers);
		assertFalse("Resource Marker not found for type: " + RESOURCE_MARKER_TYPE + ", message: [" + errorMessage + "] at line: " + line, markers.length == 0);

		for (IMarker m : markers) {
			Integer l = m.getAttribute(IMarker.LINE_NUMBER, -1);
			if (l != null && line == l.intValue()) {
				return;
			}
		}
	
		fail("Resource Marker not found for type: " + RESOURCE_MARKER_TYPE + ", message: [" + errorMessage + "] at line: " + line);
	}
}

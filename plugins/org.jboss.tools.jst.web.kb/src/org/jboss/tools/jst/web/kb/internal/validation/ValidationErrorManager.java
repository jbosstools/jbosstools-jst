/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.web.kb.internal.validation;

import java.util.Set;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;
import org.eclipse.wst.validation.internal.TaskListUtility;
import org.eclipse.wst.validation.internal.operations.WorkbenchReporter;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;
import org.eclipse.wst.validation.internal.provisional.core.IValidator;
import org.jboss.tools.common.model.project.ext.ITextSourceReference;
import org.jboss.tools.common.preferences.SeverityPreferences;
import org.jboss.tools.jst.web.kb.WebKbPlugin;
import org.jboss.tools.jst.web.kb.validation.IValidationContext;
import org.jboss.tools.jst.web.kb.validation.IValidationErrorManager;

/**
 * @author Alexey Kazakov
 */
public abstract class ValidationErrorManager implements IValidationErrorManager {

	protected IStatus OK_STATUS = new Status(IStatus.OK,
			"org.eclipse.wst.validation", 0, "OK", null); //$NON-NLS-1$ //$NON-NLS-2$

	protected IValidator validationManager;
	protected ContextValidationHelper coreHelper;
	protected IReporter reporter;
	protected IProject rootProject;
	protected String markerId;
	protected IValidationContext validationContext;
	protected TextFileDocumentProvider documentProvider;

	/**
	 * Constructor
	 */
	public ValidationErrorManager() {
	}

	public void init(IProject project, ContextValidationHelper validationHelper, IValidator manager, IReporter reporter) {
		setRootProject(project);
		setCoreHelper(validationHelper);
		setValidationManager(manager);
		setReporter(reporter);
		setValidationContext(validationHelper.getValidationContext());
		setMarkerId(org.jboss.tools.jst.web.kb.validation.IValidator.MARKED_RESOURCE_MESSAGE_GROUP);
	}

	/**
	 * @param validationManager the validationManager to set
	 */
	public void setValidationManager(IValidator validationManager) {
		this.validationManager = validationManager;
	}

	/**
	 * @param coreHelper the coreHelper to set
	 */
	public void setCoreHelper(ContextValidationHelper coreHelper) {
		this.coreHelper = coreHelper;
	}

	/**
	 * @param reporter the reporter to set
	 */
	public void setReporter(IReporter reporter) {
		this.reporter = reporter;
	}

	/**
	 * @param rootProject the rootProject to set
	 */
	public void setRootProject(IProject rootProject) {
		this.rootProject = rootProject;
	}

	/**
	 * @param markerId the markerId to set
	 */
	public void setMarkerId(String markerId) {
		this.markerId = markerId;
	}

	/**
	 * @param validationContext the validationContext to set
	 */
	public void setValidationContext(IValidationContext validationContext) {
		this.validationContext = validationContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.seam.internal.core.validation.IValidationErrorManager#addError(java.lang.String,
	 *      java.lang.String, java.lang.String[],
	 *      org.jboss.tools.seam.core.ISeamTextSourceReference,
	 *      org.eclipse.core.resources.IResource)
	 */
	public IMarker addError(String message, String preferenceKey,
			String[] messageArguments, ITextSourceReference location,
			IResource target) {
		return addError(message, preferenceKey, messageArguments, location
				.getLength(), location.getStartPosition(), target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.seam.internal.core.validation.IValidationErrorManager#addError(java.lang.String,
	 *      java.lang.String,
	 *      org.jboss.tools.seam.core.ISeamTextSourceReference,
	 *      org.eclipse.core.resources.IResource)
	 */
	public IMarker addError(String message, String preferenceKey,
			ITextSourceReference location, IResource target) {
		return addError(message, preferenceKey, new String[0], location, target);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.seam.internal.core.validation.IValidationErrorManager#addError(java.lang.String, java.lang.String, java.lang.String[], org.eclipse.core.resources.IResource)
	 */
	public IMarker addError(String message, String preferenceKey,
			String[] messageArguments, IResource target) {
		return addError(message, preferenceKey, messageArguments, 0, 0, target);
	}

	private String getMarkerId() {
		return markerId;
	}

	/**
	 * @param project
	 * @param preferenceKey
	 * @return
	 */
	protected abstract String getPreference(IProject project, String preferenceKey);

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.seam.internal.core.validation.IValidationErrorManager#addError(java.lang.String, java.lang.String, java.lang.String[], int, int, org.eclipse.core.resources.IResource)
	 */
	public IMarker addError(String message, String preferenceKey,
			String[] messageArguments, int length, int offset, IResource target) {
		String preferenceValue = getPreference(target.getProject(), preferenceKey);
		boolean ignore = false;
		int messageSeverity = IMessage.HIGH_SEVERITY;
		if (SeverityPreferences.WARNING.equals(preferenceValue)) {
			messageSeverity = IMessage.NORMAL_SEVERITY;
		} else if (SeverityPreferences.IGNORE.equals(preferenceValue)) {
			ignore = true;
		}

		if (ignore) {
			return null;
		}

		IMessage problemMessage = new ProblemMessage(message, messageSeverity, messageArguments, target, getMarkerId());
		problemMessage.setLength(length);
		problemMessage.setOffset(offset);
		try {
			if(getDocumentProvider()!=null) {
				getDocumentProvider().connect(target);
				IDocument document = getDocumentProvider().getDocument(target);
				if(document!=null) {
					problemMessage.setLineNo(document.getLineOfOffset(offset) + 1);
				}
			}
		} catch (BadLocationException e) {
			WebKbPlugin.getDefault().logError(
					"Exception occurred during error line number calculation",
					e);
			return null;
		} catch (CoreException e) {
			WebKbPlugin.getDefault().logError(
					"Exception occurred during error line number calculation",
					e);
			return null;
		} finally {
			if(getDocumentProvider()!=null) {
				getDocumentProvider().disconnect(target);
			}
		}

		int severity = problemMessage.getSeverity();
		try {
			return TaskListUtility.addTask(getMarkerOwner().getName().intern(), target, "" + problemMessage.getLineNumber(), problemMessage.getText(), 
					problemMessage.getText(), severity, null, problemMessage.getGroupName(), problemMessage.getOffset(), problemMessage.getLength());
		} catch (CoreException e) {
			WebKbPlugin.getDefault().logError(e);
		}
		return null;
	}

	protected TextFileDocumentProvider getDocumentProvider() {
		if(documentProvider==null) {
			if(coreHelper!=null) {
				documentProvider = coreHelper.getDocumentProvider();
			} else {
				documentProvider = new TextFileDocumentProvider();
			}
		}
		return documentProvider;
	}

	protected Class getMarkerOwner() {
		return this.getClass();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.seam.internal.core.validation.IValidationErrorManager#addError(java.lang.String, int, java.lang.String[], int, int, org.eclipse.core.resources.IResource)
	 */
	public IMarker addError(String message, int severity, String[] messageArguments, int length, int offset, IResource target) {
		return addError(message, severity, messageArguments, length, offset, target, getDocumentProvider(), getMarkerId(), getMarkerOwner());
	}

	/**
	 * 
	 * @param message
	 * @param severity
	 * @param messageArguments
	 * @param length
	 * @param offset
	 * @param target
	 * @param documentProvider
	 * @param markerId
	 * @param markerOwner
	 * @return
	 */
	public static IMarker addError(String message, int severity, String[] messageArguments, int length, int offset, IResource target, TextFileDocumentProvider documentProvider, String markerId, Class markerOwner) {
		IMessage problemMessage = new ProblemMessage(message, severity, messageArguments, target, markerId);
		problemMessage.setLength(length);
		problemMessage.setOffset(offset);
		try {
			if (documentProvider != null) {
				documentProvider.connect(target);
				problemMessage.setLineNo(documentProvider.getDocument(
						target).getLineOfOffset(offset) + 1);
			}
		} catch (BadLocationException e) {
			WebKbPlugin.getDefault().logError(
					"Exception occurred during error line number calculation",
					e);
			return null;
		} catch (CoreException e) {
			WebKbPlugin.getDefault().logError(
					"Exception occurred during error line number calculation",
					e);
			return null;
		} finally {
			documentProvider.disconnect(target);
		}

		try {
			return TaskListUtility.addTask(markerOwner.getName().intern(), target, "" + problemMessage.getLineNumber(), problemMessage.getText(), 
					problemMessage.getText(), severity, null, problemMessage.getGroupName(), problemMessage.getOffset(), problemMessage.getLength());
		} catch (CoreException e) {
			WebKbPlugin.getDefault().logError(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.seam.internal.core.validation.IValidationErrorManager#displaySubtask(java.lang.String)
	 */
	public void displaySubtask(String messageId) {
		displaySubtask(messageId, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.seam.internal.core.validation.IValidationErrorManager#displaySubtask(java.lang.String,
	 *      java.lang.String[])
	 */
	public void displaySubtask(String message, String[] messageArguments) {
		IMessage problemMessage = new ProblemMessage(message, IMessage.NORMAL_SEVERITY, messageArguments);
		reporter.displaySubtask(validationManager, problemMessage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.seam.internal.core.validation.IValidationErrorManager#removeMessagesFromResources(java.util.Set)
	 */
	public void removeMessagesFromResources(Set<IResource> resources) {
		for (IResource r : resources) {
			WorkbenchReporter.removeAllMessages(r, new String[]{getMarkerOwner().getName()}, null);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.seam.internal.core.validation.IValidationErrorManager#removeAllMessagesFromResource(org.eclipse.core.resources.IResource)
	 */
	public void removeAllMessagesFromResource(IResource resource) {
//		reporter.removeAllMessages(validationManager, resource);
		WorkbenchReporter.removeAllMessages(resource, new String[]{getMarkerOwner().getName()}, null);
	}
}
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
import org.eclipse.wst.validation.internal.TaskListUtility;
import org.eclipse.wst.validation.internal.core.Message;
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
	protected String baseName;
	protected IValidationContext validationContext;

	/**
	 * Constructor
	 */
	public ValidationErrorManager() {
	}

	protected void init(IProject project, ContextValidationHelper validationHelper, ValidatorManager manager, IReporter reporter, IValidationContext validationContext) {
		setRootProject(project);
		setCoreHelper(validationHelper);
		setValidationManager(manager);
		setReporter(reporter);
		setValidationContext(validationContext);
	}

	/**
	 * @param baseName the baseName to set
	 */
	public void setBaseName(String baseName) {
		this.baseName = baseName;
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

	protected String getBaseName() {
		return baseName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.seam.internal.core.validation.IValidationErrorManager#addError(java.lang.String,
	 *      java.lang.String, java.lang.String[],
	 *      org.jboss.tools.seam.core.ISeamTextSourceReference,
	 *      org.eclipse.core.resources.IResource)
	 */
	public IMarker addError(String messageId, String preferenceKey,
			String[] messageArguments, ITextSourceReference location,
			IResource target) {
		return addError(messageId, preferenceKey, messageArguments, location
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
	public IMarker addError(String messageId, String preferenceKey,
			ITextSourceReference location, IResource target) {
		return addError(messageId, preferenceKey, new String[0], location, target);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.seam.internal.core.validation.IValidationErrorManager#addError(java.lang.String, java.lang.String, java.lang.String[], org.eclipse.core.resources.IResource)
	 */
	public IMarker addError(String messageId, String preferenceKey,
			String[] messageArguments, IResource target) {
		return addError(messageId, preferenceKey, messageArguments, 0, 0, target);
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
	public IMarker addError(String messageId, String preferenceKey,
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

		IMessage message = new Message(getBaseName(), messageSeverity,
				messageId, messageArguments, target,
				getMarkerId());
		message.setLength(length);
		message.setOffset(offset);
		try {
			if (coreHelper != null) {
				coreHelper.getDocumentProvider().connect(target);
				message.setLineNo(coreHelper.getDocumentProvider().getDocument(
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
			if(coreHelper!=null) {
				coreHelper.getDocumentProvider().disconnect(target);
			}
		}

		int severity = message.getSeverity();
		try {
			return TaskListUtility.addTask(this.getClass().getName().intern(), target, ""+message.getLineNumber(), message.getId(), 
				message.getText(this.getClass().getClassLoader()), severity, null, message.getGroupName(), 	message.getOffset(), message.getLength());
		} catch (CoreException e) {
			WebKbPlugin.getDefault().logError(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.seam.internal.core.validation.IValidationErrorManager#addError(java.lang.String, int, java.lang.String[], int, int, org.eclipse.core.resources.IResource)
	 */
	public IMarker addError(String messageId, int severity, String[] messageArguments, int length, int offset, IResource target) {
		IMessage message = new Message(getBaseName(), severity,
				messageId, messageArguments, target,
				getMarkerId());
		message.setLength(length);
		message.setOffset(offset);
		try {
			if (coreHelper != null) {
				coreHelper.getDocumentProvider().connect(target);
				message.setLineNo(coreHelper.getDocumentProvider().getDocument(
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
		}

		try {
			return TaskListUtility.addTask(this.getClass().getName().intern(), target, ""+message.getLineNumber(), message.getId(), 
				message.getText(this.getClass().getClassLoader()), severity, null, message.getGroupName(), 	message.getOffset(), message.getLength());
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
	public void displaySubtask(String messageId, String[] messageArguments) {
		IMessage message = new Message(getBaseName(), IMessage.NORMAL_SEVERITY,
				messageId, messageArguments);
		reporter.displaySubtask(validationManager, message);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.seam.internal.core.validation.IValidationErrorManager#removeMessagesFromResources(java.util.Set)
	 */
	public void removeMessagesFromResources(Set<IResource> resources) {
		for (IResource r : resources) {
			WorkbenchReporter.removeAllMessages(r, new String[]{this.getClass().getName()}, null);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.seam.internal.core.validation.IValidationErrorManager#removeAllMessagesFromResource(org.eclipse.core.resources.IResource)
	 */
	public void removeAllMessagesFromResource(IResource resource) {
//		reporter.removeAllMessages(validationManager, resource);
		WorkbenchReporter.removeAllMessages(resource, new String[]{this.getClass().getName()}, null);
	}
}
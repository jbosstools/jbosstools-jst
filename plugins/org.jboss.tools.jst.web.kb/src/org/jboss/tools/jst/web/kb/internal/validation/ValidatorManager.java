 /*******************************************************************************
  * Copyright (c) 2007 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.jst.web.kb.internal.validation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.validation.internal.core.ValidationException;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;
import org.eclipse.wst.validation.internal.provisional.core.IValidationContext;
import org.eclipse.wst.validation.internal.provisional.core.IValidatorJob;
import org.jboss.tools.common.preferences.SeverityPreferences;
import org.jboss.tools.jst.web.kb.KbMessages;
import org.jboss.tools.jst.web.kb.WebKbPlugin;
import org.jboss.tools.jst.web.kb.validation.IValidatingProjectSet;
import org.jboss.tools.jst.web.kb.validation.IValidationContextManager;
import org.jboss.tools.jst.web.kb.validation.IValidator;

/**
 * This Manager invokes all dependent validators that should be invoked in one job.
 * We need this one because wst validation framework does not let us invoke
 * dependent validators in the same job.
 * @author Alexey Kazakov
 */
public class ValidatorManager implements IValidatorJob {

	private static Set<IProject> validatingProjects = new HashSet<IProject>();
	public static final String SLEEPING = "Sleeping"; //$NON-NLS-1$
	public static final String RUNNING = "Running"; //$NON-NLS-1$
	private static String STATUS = SLEEPING;

	public ValidatorManager() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.validation.internal.provisional.core.IValidatorJob#getSchedulingRule(org.eclipse.wst.validation.internal.provisional.core.IValidationContext)
	 */
	public ISchedulingRule getSchedulingRule(IValidationContext helper) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.wst.validation.internal.provisional.core.IValidatorJob#validateInJob(org.eclipse.wst.validation.internal.provisional.core.IValidationContext, org.eclipse.wst.validation.internal.provisional.core.IReporter)
	 */
	public IStatus validateInJob(IValidationContext helper, IReporter reporter)	throws ValidationException {
		STATUS = RUNNING;
		try {
			ContextValidationHelper validationHelper = (ContextValidationHelper)helper;
			IProject project = validationHelper.getProject();
			if(project==null) {
				return OK_STATUS;
			}
			IValidationContextManager validationContextManager = validationHelper.getValidationContextManager();
			Set<IProject> rootProjects = validationContextManager.getRootProjects();
			IStatus status = OK_STATUS;
			synchronized (validatingProjects) {
				for (IProject rootProject : rootProjects) {
					if(validatingProjects.contains(rootProject)) {
						return OK_STATUS;
					}
					validatingProjects.add(rootProject);
				}
			}
			synchronized (validatingProjects) {
				try {
					validationContextManager.clearValidatedProjectsList();
					Set<IFile> changedFiles = validationHelper.getChangedFiles();
					if(!changedFiles.isEmpty()) {
						status = validate(changedFiles, validationHelper, reporter);
					} else if(!validationContextManager.getRegisteredFiles().isEmpty()) {
						validationContextManager.clearAllResourceLinks();
						status = validateAll(validationHelper, reporter);
					}
				} finally {
					if(validationContextManager!=null) {
						validationContextManager.clearRegisteredFiles();
					}
					validationHelper.cleanup(); // See https://issues.jboss.org/browse/JBIDE-8726
					for (IProject rootProject : rootProjects) {
						validatingProjects.remove(rootProject);
					}
				}
			}
			return status;
		} finally {
			STATUS = SLEEPING;
		}
	}

	static String WTP_VALIDATOR_ID = "org.eclipse.wst.validation.validationbuilder"; //$NON-NLS-1$
	static String ORDER_PROBLEM_MARKER_TYPE = "org.jboss.tools.jst.web.kb.builderOrderProblem"; //$NON-NLS-1$
	private static String ATTR_VALIDATOR = "validator"; //$NON-NLS-1$

	public static boolean isCorrectOrder(IProject project, String builderId) {
		try {
		ICommand[] cs = project.getDescription().getBuildSpec();
		boolean validationFound = false;
			for (ICommand c: cs) {
				String name = c.getBuilderName();
				if(WTP_VALIDATOR_ID.equals(name)) {
					validationFound = true;
				}
				if(builderId.equals(name)) {
					return !validationFound;
				}
			}		
		} catch (CoreException e) {
			WebKbPlugin.getDefault().logError(e);
		}
		return true;
	}

	public static IMarker findBuilderOrderMarker(IProject project, String validator) {
		IMarker result = null;
		try {
			IMarker[] ms = project.findMarkers(ORDER_PROBLEM_MARKER_TYPE, false, IResource.DEPTH_ZERO);
			for (IMarker m: ms) {
				if(validator.equals(m.getAttribute(ATTR_VALIDATOR, null))) {
					result = m;
				}
			}
		} catch (CoreException e) {
			WebKbPlugin.getDefault().logError(e);
		}
		return result;
	}

	public static boolean validateBuilderOrder(IProject project, String builderId, String validator, SeverityPreferences preferences) throws CoreException {
		String severityPreferenceValue = preferences.getBuilderOrderPreference(project);
		int severity = getSeverity(severityPreferenceValue);
		boolean isCorrect = isCorrectOrder(project, builderId);
		IMarker marker = findBuilderOrderMarker(project, validator);
		if(isCorrect || severity <= IMarker.SEVERITY_INFO) {
			if(marker != null) {
				ResourcesPlugin.getWorkspace().deleteMarkers(new IMarker[]{marker});
			}
		} else {
			if(marker != null) {
				if(marker.getAttribute(IMarker.SEVERITY, -1) != severity) {
					marker.setAttribute(IMarker.SEVERITY, severity);
				}
			} else {
				marker = project.createMarker(ORDER_PROBLEM_MARKER_TYPE);
				marker.setAttribute(ATTR_VALIDATOR, validator);
				marker.setAttribute(IMarker.SEVERITY, severity);
				String message = NLS.bind(KbMessages.WRONG_BUILDER_ORDER, project.getName(), builderId);
				marker.setAttribute(IMarker.MESSAGE, message);
				//Temporary to debug
				WebKbPlugin.getDefault().logError(message);
			}
		}		
		return isCorrect || severity <= IMarker.SEVERITY_INFO;
	}

	public static int getSeverity(String severityPreferenceValue) {
		return (SeverityPreferences.IGNORE.equals(severityPreferenceValue))
				? IMarker.SEVERITY_INFO
				: SeverityPreferences.WARNING.equals(severityPreferenceValue)
				? IMarker.SEVERITY_WARNING
				: IMarker.SEVERITY_ERROR;
	}

	private IStatus validate(Set<IFile> changedFiles, ContextValidationHelper validationHelper, IReporter reporter) throws ValidationException {
		IValidationContextManager validationContextManager = validationHelper.getValidationContextManager();
		List<IValidator> validators = validationContextManager.getValidators();
		Set<IProject> rootProjects = validationContextManager.getRootProjects();
		removeMarkers(changedFiles);
		for (IValidator validator : validators) {
			for (IProject rootProject : rootProjects) {
				IValidatingProjectSet projectBrunch = validationHelper.getValidationContextManager().getValidatingProjectTree(validator).getBrunches().get(rootProject);
				if(projectBrunch!=null) {
					validator.validate(changedFiles, rootProject, validationHelper, projectBrunch.getRootContext(), this, reporter);
				}
			}
		}
		return OK_STATUS;
	}

	private IStatus validateAll(ContextValidationHelper validationHelper, IReporter reporter) throws ValidationException {
		IValidationContextManager validationContextManager = validationHelper.getValidationContextManager();
		List<IValidator> validators = validationContextManager.getValidators();
		Set<IProject> rootProjects = validationContextManager.getRootProjects();
		removeMarkers(validationHelper.getProjectSetRegisteredFiles());
		for (IValidator validator : validators) {
			for (IProject rootProject : rootProjects) {
				IValidatingProjectSet projectBrunch = validationHelper.getValidationContextManager().getValidatingProjectTree(validator).getBrunches().get(rootProject);
				if(projectBrunch!=null) {
					validator.validateAll(rootProject, validationHelper, projectBrunch.getRootContext(), this, reporter);
				}
			}
		}
		return OK_STATUS;
	}

	private void removeMarkers(Set<IFile> files) {
		try {
			for (IFile file : files) {
				if(file.isAccessible()) {
					file.deleteMarkers(IValidator.KB_PROBLEM_MARKER_TYPE, true, IResource.DEPTH_ZERO);
				}
			}
		} catch (CoreException e) {
			WebKbPlugin.getDefault().logError(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.wst.validation.internal.provisional.core.IValidator#cleanup(org.eclipse.wst.validation.internal.provisional.core.IReporter)
	 */
	public void cleanup(IReporter reporter) {
		reporter = null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.wst.validation.internal.provisional.core.IValidator#validate(org.eclipse.wst.validation.internal.provisional.core.IValidationContext, org.eclipse.wst.validation.internal.provisional.core.IReporter)
	 */
	public void validate(IValidationContext helper, IReporter reporter)	throws ValidationException {
		validateInJob(helper, reporter);
	}

	/**
	 * This method returns a string with status message of the validator. This method is supposed to be used in unit tests.
	 * @return
	 */
	public static String getStatus() {
		return STATUS;
	}

	/**
	 * This method is supposed to be used in unit tests.
	 * @param status
	 */
	public static void setStatus(String status) {
		STATUS = status;
	}
}
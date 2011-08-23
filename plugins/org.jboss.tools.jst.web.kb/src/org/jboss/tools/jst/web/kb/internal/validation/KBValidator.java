/******************************************************************************* 
 * Copyright (c) 2010 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.jst.web.kb.internal.validation;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.validation.internal.plugin.ValidationPlugin;
import org.jboss.tools.common.preferences.SeverityPreferences;
import org.jboss.tools.common.validation.IProjectValidationContext;
import org.jboss.tools.common.validation.IValidatingProjectSet;
import org.jboss.tools.common.validation.IValidatingProjectTree;
import org.jboss.tools.common.validation.IValidator;
import org.jboss.tools.common.validation.ValidationErrorManager;
import org.jboss.tools.common.validation.internal.SimpleValidatingProjectTree;
import org.jboss.tools.common.validation.internal.ValidatingProjectSet;
import org.jboss.tools.jst.web.kb.IKbProject;
import org.jboss.tools.jst.web.kb.KbMessages;
import org.jboss.tools.jst.web.kb.KbProjectFactory;

/**
 * @author Alexey Kazakov
 */
public abstract class KBValidator extends ValidationErrorManager implements IValidator {

	protected boolean notValidatedYet(IResource resource) {
		IProject pr = resource.getProject();
		return coreHelper==null || !coreHelper.getValidationContextManager().projectHasBeenValidated(this, pr);
	}

	/**
	 * Creates a simple validating project tree for the project.
	 * 
	 * @param project
	 * @return
	 */
	public static IValidatingProjectTree createSimpleValidatingProjectTree(IProject project) {
		Set<IProject> projects = new HashSet<IProject>();
		projects.add(project);
		IKbProject kbProject = KbProjectFactory.getKbProject(project, false);
		if(kbProject!=null) {
			IProjectValidationContext rootContext = kbProject.getValidationContext();
			IValidatingProjectSet projectSet = new ValidatingProjectSet(project, projects, rootContext);
			return new SimpleValidatingProjectTree(projectSet);
		}
		return new SimpleValidatingProjectTree(project);
	}

	public static final String ORDER_PROBLEM_MARKER_TYPE = "org.jboss.tools.jst.web.kb.builderOrderProblem"; //$NON-NLS-1$
	private static String ATTR_BUILDER = "builder"; //$NON-NLS-1$
	private static String ATTR_VALIDATOR = "validator"; //$NON-NLS-1$

	/**
	 * Helper method to be called by IValidator implementations. 
	 * It implements common logic:
	 * 1. Checks if builderId follows Validation Builder.
	 * 2. Checks severity preference associated with this builder.
	 * 3. Creates, updates or deletes error/warning marker on project 
	 *    taking into account builders order and severity preference.
	 * 
	 * @param project
	 * @param builderId
	 * @param validatorId
	 * @param preferences
	 * @return
	 * @throws CoreException
	 */
	public static boolean validateBuilderOrder(IProject project, String builderId, String validatorId, SeverityPreferences preferences) throws CoreException {
		int severity = getSeverity(preferences.getBuilderOrderPreference(project));
		boolean isCorrect = isCorrectOrder(project, builderId);
		IMarker marker = findBuilderOrderMarker(project, builderId, validatorId);
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
				marker.setAttribute(ATTR_BUILDER, builderId);
				marker.setAttribute(IMarker.SEVERITY, severity);
				String message = NLS.bind(KbMessages.WRONG_BUILDER_ORDER, new String[]{project.getName(), findValidatorName(validatorId), findBuilderName(builderId)});
				marker.setAttribute(IMarker.MESSAGE, message);
				marker.setAttribute(ATTR_VALIDATOR, validatorId);
			}
		}		
		return isCorrect || severity <= IMarker.SEVERITY_INFO;
	}

	private static boolean isCorrectOrder(IProject project, String builderId) throws CoreException {
		ICommand[] cs = project.getDescription().getBuildSpec();
		boolean validationFound = false;
		for (ICommand c: cs) {
			String name = c.getBuilderName();
			if(ValidationPlugin.VALIDATION_BUILDER_ID.equals(name)) {
				validationFound = true;
			} else if(builderId.equals(name)) {
				return !validationFound;
			}
		}		
		return true;
	}

	private static IMarker findBuilderOrderMarker(IProject project, String builderId, String validatorId) throws CoreException {
		IMarker result = null;
		IMarker[] ms = project.findMarkers(ORDER_PROBLEM_MARKER_TYPE, false, IResource.DEPTH_ZERO);
		for (IMarker m: ms) {
			if(builderId.equals(m.getAttribute(ATTR_BUILDER, null))
				&& validatorId.equals(m.getAttribute(ATTR_VALIDATOR))) {
				result = m;
			}
		}
		return result;
	}

	private static String findBuilderName(String builderId) {
		IExtension ext = Platform.getExtensionRegistry().getExtension(builderId);
		return (ext != null && ext.getLabel() != null) ? ext.getLabel() : builderId;
	}

	private static String findValidatorName(String validatorId) {
		IExtension ext = Platform.getExtensionRegistry().getExtension(validatorId);
		if(ext != null) {
			IConfigurationElement[] es = ext.getConfigurationElements();
			if(es.length > 0) {
				String name = es[0].getAttribute("name"); //$NON-NLS-1$
				if(name != null) {
					return name;
				}
			}
		}
		return validatorId;
	}

	private static int getSeverity(String severityPreferenceValue) {
		return (SeverityPreferences.IGNORE.equals(severityPreferenceValue))
				? IMarker.SEVERITY_INFO
				: (SeverityPreferences.WARNING.equals(severityPreferenceValue))
				? IMarker.SEVERITY_WARNING
				: IMarker.SEVERITY_ERROR;
	}
}
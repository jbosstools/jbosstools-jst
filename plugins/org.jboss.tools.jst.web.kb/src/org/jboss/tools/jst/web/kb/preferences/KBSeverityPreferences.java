/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.preferences;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.jboss.tools.common.preferences.SeverityPreferences;
import org.jboss.tools.jst.web.kb.WebKbPlugin;

/**
 * @author Viacheslav Kabanovich
 */
public class KBSeverityPreferences extends SeverityPreferences {

	public static final Set<String> SEVERITY_OPTION_NAMES = new HashSet<String>();

	private static KBSeverityPreferences INSTANCE = new KBSeverityPreferences();

	// Expression Language

	// Mark EL Variable name which we can't resolve.
	public static final String REQUIRED_KB_CAPABILITIES_ARE_MISSING = INSTANCE.createSeverityOption("requiredKbCapabilitiesAreMissing"); //$NON-NLS-1$
	// Check "var" attributes.
	public static final String KB_CAPABILITIES_ARE_NOT_ENABLED_IN_JAVA_MODULE = INSTANCE.createSeverityOption("kbCapabilitiesAreNotEnabledInJavaModule"); //$NON-NLS-1$

	/**
	 * @return the only instance of KBSeverityPreferences
	 */
	public static KBSeverityPreferences getInstance() {
		return INSTANCE;
	}

	private KBSeverityPreferences() {
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.common.preferences.SeverityPreferences#createSeverityOption(java.lang.String)
	 */
	@Override
	protected String createSeverityOption(String shortName) {
		String name = getPluginId() + ".validator.problem." + shortName; //$NON-NLS-1$
		SEVERITY_OPTION_NAMES.add(name);
		return name;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.common.preferences.SeverityPreferences#getPluginId()
	 */
	@Override
	protected String getPluginId() {
		return WebKbPlugin.PLUGIN_ID;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.common.preferences.SeverityPreferences#getSeverityOptionNames()
	 */
	@Override
	protected Set<String> getSeverityOptionNames() {
		return SEVERITY_OPTION_NAMES;
	}

	public static boolean isValidationEnabled(IProject project) {
		return INSTANCE.isEnabled(project);
	}

}
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
package org.jboss.tools.jst.web.kb.require;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.jboss.tools.common.text.ext.util.xpl.RegistryReader;
import org.jboss.tools.jst.web.kb.WebKbPlugin;
import org.jboss.tools.jst.web.kb.include.IncludeContextBuilder;

/**
 * KB Require Builder is used to read and store the information from the
 * KbRequire schema.
 * 
 * @author Victor Rubezhny
 *
 */
public class KbRequireBuilder extends RegistryReader {

	// extension point ID
	public static final String PL_KB_REQUIRE = "KbRequire"; //$NON-NLS-1$

	public static final String TAG_REQUIRE = "require"; //$NON-NLS-1$

	public static final String ATT_ID = "id"; //$NON-NLS-1$
	public static final String ATT_FOR_NATURE = "forNature"; //$NON-NLS-1$
	public static final String ATT_DESCRIPTION = "description"; //$NON-NLS-1$

	private static KbRequireBuilder fInstance;

	protected String fTargetContributionElement;

	List<KbRequireDefinition> fDefinitions = null;
	
	/**
	 * returns singleton instance of KbRequireBuilder
	 * 
	 * @return {@link IncludeContextBuilder}
	 */
	public synchronized static KbRequireBuilder getInstance() {
		if (fInstance == null) {
			fInstance = new KbRequireBuilder();
		}
		return fInstance;
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.sse.editor.internal.extension.RegistryReader#readElement(org.eclipse.core.runtime.IConfigurationElement)
	 */
	@Override
	protected boolean readElement(IConfigurationElement element) {
		String tag = element.getName();

		if (tag.equals(TAG_REQUIRE)) {
			processRequireElement(element);
		}

		return false;
	}
	
	/**
	 * Reads the Tag element and stores the specific data 
	 * 
	 * @param element
	 * @return
	 */
	private boolean processRequireElement(IConfigurationElement element) {
		String forNature = element.getAttribute(ATT_FOR_NATURE);
		String description = element.getAttribute(ATT_DESCRIPTION);

		if (forNature != null && forNature.trim().length() > 0) {
			if (fDefinitions == null)
				fDefinitions = new ArrayList<KbRequireDefinition>();
			
			fDefinitions.add(new KbRequireDefinition(forNature, description));
			
			return true;
		}
		
		return false;
	}

	private void initCache() {
		if (fDefinitions == null) {
			readContributions(TAG_REQUIRE, PL_KB_REQUIRE);
		}
	}

	/**
	 * Reads the contributions defined in the extension point
	 * 
	 * @param element
	 * @param extensionPoint
	 */
	protected void readContributions(String element, String extensionPoint) {
		fTargetContributionElement = element;
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		readRegistry(registry, WebKbPlugin.PLUGIN_ID, extensionPoint);
	}

	/**
	 * Returns all the open on definition objects
	 * 
	 * @return
	 */
	public List<KbRequireDefinition> getKbRequireDefinitions() {
		initCache();
		return fDefinitions;
	}

}

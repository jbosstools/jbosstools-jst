/******************************************************************************* 
 * Copyright (c) 2009 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.include;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.internal.registry.RegistryReader;
import org.jboss.tools.jst.web.kb.WebKbPlugin;

/**
 * Include Context Builder is used to read and store the information from the
 * Include schema.
 * 
 * @author Victor Rubezhny
 *
 */
public class IncludeContextBuilder extends RegistryReader {
	// extension point ID
	public static final String PL_INCLUDE = "KbIncludeContext"; //$NON-NLS-1$

	public static final String TAG_INCLUDE = "include"; //$NON-NLS-1$
	public static final String TAG_TAG = "tag"; //$NON-NLS-1$
	public static final String TAG_ATTRIBUTE = "attribute"; //$NON-NLS-1$
	public static final String TAG_CONTEXTTYPE = "contexttype"; //$NON-NLS-1$
	public static final String TAG_CONTENTTYPE = "contenttype"; //$NON-NLS-1$
	public static final String TAG_CSSHOLDER = "cssholder"; //$NON-NLS-1$
	
	public static final String ATT_ID = "id"; //$NON-NLS-1$
	public static final String ATT_URI = "uri"; //$NON-NLS-1$
	public static final String ATT_NAME = "name"; //$NON-NLS-1$

	protected String fTargetContributionElement;

	private static IncludeContextBuilder fInstance;

	private List<IncludeContextDefinition> fIncludeContextDefs = null;
	private IncludeContextDefinition fCurrentIncludeDefinition = null;

	/**
	 * returns singleton instance of IncludeContextBuilder
	 * 
	 * @return {@link IncludeContextBuilder}
	 */
	public synchronized static IncludeContextBuilder getInstance() {
		if (fInstance == null) {
			fInstance = new IncludeContextBuilder();
		}
		return fInstance;
	}

	/**
	 * Returns the name of the part ID attribute that is expected
	 * in the target extension.
	 * 
	 * @param element
	 * @return String
	 */
	public static String getId(IConfigurationElement element) {
		String value = element.getAttribute(ATT_ID);
		return value;
	}

	/**
	 * Returns the URI of the part ID attribute that is expected
	 * in the target extension.
	 * 
	 * @param element
	 * @return String
	 */
	public static String getUri(IConfigurationElement element) {
		String value = element.getAttribute(ATT_URI);
		return value;
	}

	/**
	 * Returns the name of the part ID attribute that is expected
	 * in the target extension.
	 * 
	 * @param element
	 * @return String
	 */
	public static String getName(IConfigurationElement element) {
		String value = element.getAttribute(ATT_NAME);
		return value;
	}

	/**
	 * Processes element which should be a configuration element specifying an
	 * open on object.  Creates a new open on definition object and adds it to the
	 * list of open on definition objects
	 * 
	 * @param element configuration element
	 */
	private void processIncludeContextElement(IConfigurationElement element) {
		String theUri = getUri(element);

		theUri = theUri == null ? "" : theUri; //$NON-NLS-1$
		
		// create a new list of open on definitions if it hasn't been created yet
		if (fIncludeContextDefs == null) {
			fIncludeContextDefs = new ArrayList<IncludeContextDefinition>();
		}
		
		fCurrentIncludeDefinition = getIncludeContextDefinition(theUri);
		if (fCurrentIncludeDefinition == null) {
			// start building new IncludeDefinition
			fCurrentIncludeDefinition = new IncludeContextDefinition(theUri);
			fIncludeContextDefs.add(fCurrentIncludeDefinition);
		}		
	}

	/**
	 * Returns the IncludeContextDefinition by specified URI
	 * 
	 * @param uri
	 * @return
	 */
	private IncludeContextDefinition getIncludeContextDefinition(String uri) {
		if (fIncludeContextDefs == null || uri == null)
			return null;
		
		for (IncludeContextDefinition def : fIncludeContextDefs) {
			if (uri.equals(def.getUri())) {
				return def;
			}
		}
		
		return null;
	}
	
	/**
	 * Reads the Tag element and stores the specific data 
	 * 
	 * @param element
	 * @return
	 */
	private boolean processTagElement(IConfigurationElement element) {
		String theName = getName(element);

		if (fCurrentIncludeDefinition != null && theName != null) {
			return fCurrentIncludeDefinition.addTag(theName, element);
		}
		
		return false;
	}

	/**
	 * Reads the Tag Attribute element and stores the specific data 
	 * 
	 * @param element
	 * @return
	 */
	private void processAttributeElement(IConfigurationElement element) {
		String theName = getName(element);

		if (fCurrentIncludeDefinition != null && theName != null) {
			fCurrentIncludeDefinition.addTagAttribute(theName, element);
		}
	}
	
	/**
	 * Reads the ContextType element and stores the specific data 
	 * 
	 * @param element
	 * @return
	 */
	private void processContextTypeElement(IConfigurationElement element) {
		String theId = getId(element);

		if (fCurrentIncludeDefinition != null && theId != null) {
			fCurrentIncludeDefinition.addContextType(theId, element);
		}
	}
	
	/**
	 * Reads the ContentType element and stores the specific data 
	 * 
	 * @param element
	 * @return
	 */
	private boolean processContentTypeElement(IConfigurationElement element) {
		String theId = getId(element);

		if (fCurrentIncludeDefinition != null && theId != null) {
			return fCurrentIncludeDefinition.addContentType(theId, element);
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.sse.editor.internal.extension.RegistryReader#readElement(org.eclipse.core.runtime.IConfigurationElement)
	 */
	protected boolean readElement(IConfigurationElement element) {
		String tag = element.getName();

		if (tag.equals(TAG_INCLUDE) || tag.equals(TAG_CSSHOLDER)) {
			processIncludeContextElement(element);
			
			// make sure processing of current open on tag resulted in a current definition
			// before continue reading the children
			if (fCurrentIncludeDefinition != null) {
				readElementChildren(element);
			}
			return true;
		}
		else if (tag.equals(TAG_TAG)) {
			processTagElement(element);

			// make sure processing of current open on tag resulted in a current definition
			// before continue reading the children
			if (fCurrentIncludeDefinition != null) {
				readElementChildren(element);
			}
			return true;
		}
		else if (tag.equals(TAG_ATTRIBUTE)) {
			processAttributeElement(element);
			return true;
		}
		else if (tag.equals(TAG_CONTEXTTYPE)) {
			processContextTypeElement(element);

			// make sure processing of current open on tag resulted in a current definition
			// before continue reading the children
			if (fCurrentIncludeDefinition != null) {
				readElementChildren(element);
			}
			return true;
		}
		else if (tag.equals(TAG_CONTENTTYPE)) {
			return processContentTypeElement(element);
		}

		return false;
	}

	private void initCache() {
		if (fIncludeContextDefs == null) {
			readContributions(TAG_INCLUDE, PL_INCLUDE);
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
	public List<IncludeContextDefinition> getIncludeContextDefinitions() {
		initCache();
		return fIncludeContextDefs;
	}
	
	/**
	 * Returns the attributes for the specified include tag
	 * 
	 * @param uri
	 * @param tagName
	 * @return
	 */
	public static String[] getIncludeAttributes(String uri, String tagName) {
		if (uri == null)
			return null;
		
		List<IncludeContextDefinition> defs = IncludeContextBuilder.getInstance().getIncludeContextDefinitions();
		if (defs == null)
			return null;
		
		List<String> attrs = new ArrayList<String>();
		
		for (IncludeContextDefinition def : defs) {
			if (uri.equals(def.getUri())) {
				String[] defAttrs = def.getIncludeTagAttributes(tagName);
				if (defAttrs != null) {
					for (String attr : defAttrs) attrs.add(attr);
				}
			}
		}
		
		return attrs.size() == 0 ? null : attrs.toArray(new String[attrs.size()]);
	}
	
	/**
	 * Returns the Content Type for the specified Content Type
	 * 
	 * @param contentType
	 * @return
	 */
	public static String getContextType(String contentType) {
		if (contentType == null)
			return null;
		
		List<IncludeContextDefinition> defs = IncludeContextBuilder.getInstance().getIncludeContextDefinitions();
		if (defs == null)
			return null;
		
		for (IncludeContextDefinition def : defs) {
			String contextType = def.getContextType(contentType);
			if (contextType != null)
				return contextType;
		}
		
		return null;
	}

	/**
	 * Checks if the specified tag is a CSS Style Sheet container
	 * 
	 * @param uri
	 * @param tagName
	 * @return
	 */
	public static boolean isCSSStyleSheetContainer(String uri, String tagName) {
		if (uri == null)
			return false;
		
		List<IncludeContextDefinition> defs = IncludeContextBuilder.getInstance().getIncludeContextDefinitions();
		if (defs == null)
			return false;
		
		boolean isHolder = false;
		for (IncludeContextDefinition def : defs) {
			if (uri.equals(def.getUri())) {
				String[] defTags = def.getCSSTags();
				if (defTags != null) {
					for (String tag : defTags) {
						if (tagName.equals(tag) || ("".equals(uri) && tagName.equalsIgnoreCase(tag))) { //$NON-NLS-1$
							isHolder = true;
							// Check that the tag have no attributes defined
							// If so - the tag itself is used to define the CSS
							// But if the tag has at least one attribute defined - it's not the holder 
							String[] attrs = def.getCSSTagAttributes(tagName);
							isHolder ^= (attrs != null && attrs.length > 0);  
						}
					}
				}
			}
		}
		
		return isHolder;
	}

	/**
	 * Returns the CSS Style Sheet attributes that represent a CSS Style Sheet container
	 * 
	 * @param uri
	 * @param tagName
	 * @return
	 */
	public static String[] getCSSStyleSheetAttributes(String uri, String tagName) {
		if (uri == null)
			return null;
		
		List<IncludeContextDefinition> defs = IncludeContextBuilder.getInstance().getIncludeContextDefinitions();
		if (defs == null)
			return null;
		
		List<String> attrs = new ArrayList<String>();
		
		for (IncludeContextDefinition def : defs) {
			if (uri.equals(def.getUri())) {
				String[] defAttrs = def.getCSSTagAttributes(tagName);
				if (defAttrs != null) {
					for (String attr : defAttrs) attrs.add(attr);
				}
			}
		}
		
		return attrs.size() == 0 ? null : attrs.toArray(new String[attrs.size()]);

	}
}

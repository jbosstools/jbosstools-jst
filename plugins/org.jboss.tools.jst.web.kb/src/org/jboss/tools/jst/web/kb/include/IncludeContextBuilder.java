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

	public static final String[] EMPTY = new String[0];

	// extension point ID
	public static final String PL_INCLUDE = "KbIncludeContext"; //$NON-NLS-1$

	public static final String TAG_INCLUDE = "include"; //$NON-NLS-1$
	public static final String TAG_TAG = "tag"; //$NON-NLS-1$
	public static final String TAG_ATTRIBUTE = "attribute"; //$NON-NLS-1$
	public static final String TAG_CONTEXTTYPE = "contexttype"; //$NON-NLS-1$
	public static final String TAG_CONTENTTYPE = "contenttype"; //$NON-NLS-1$
	public static final String TAG_CSSHOLDER = "cssholder"; //$NON-NLS-1$
	public static final String TAG_JSF2CSSHOLDER = "jsf2cssholder"; //$NON-NLS-1$

	public static final String ATT_ID = "id"; //$NON-NLS-1$
	public static final String ATT_URI = "uri"; //$NON-NLS-1$
	public static final String ATT_NAME = "name"; //$NON-NLS-1$

	private final List<IncludeContextDefinition> fIncludeContextDefs = new ArrayList<IncludeContextDefinition>();;
	private IncludeContextDefinition fCurrentIncludeDefinition;

	private static final IncludeContextBuilder fInstance = new IncludeContextBuilder();

	private IncludeContextBuilder() {
		// Reads the contributions defined in the extension point
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		readRegistry(registry, WebKbPlugin.PLUGIN_ID, PL_INCLUDE);
	}

	/**
	 * returns singleton instance of IncludeContextBuilder
	 * 
	 * @return {@link IncludeContextBuilder}
	 */
	public static IncludeContextBuilder getInstance() {
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
		return element.getAttribute(ATT_ID);
	}

	/**
	 * Returns the URI of the part ID attribute that is expected
	 * in the target extension.
	 * 
	 * @param element
	 * @return String
	 */
	private static String getUri(IConfigurationElement element) {
		return element.getAttribute(ATT_URI);
	}

	/**
	 * Returns the name of the part ID attribute that is expected
	 * in the target extension.
	 * 
	 * @param element
	 * @return String
	 */
	public static String getName(IConfigurationElement element) {
		return element.getAttribute(ATT_NAME);
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

		if (tag.equals(TAG_INCLUDE) || tag.equals(TAG_CSSHOLDER) || tag.equals(TAG_JSF2CSSHOLDER)) {
			processIncludeContextElement(element);
			
			// make sure processing of current open on tag resulted in a current definition
			// before continue reading the children
			if (fCurrentIncludeDefinition != null) {
				readElementChildren(element);
			}
		} else if (tag.equals(TAG_TAG)) {
			processTagElement(element);

			// make sure processing of current open on tag resulted in a current definition
			// before continue reading the children
			if (fCurrentIncludeDefinition != null) {
				readElementChildren(element);
			}
		} else if (tag.equals(TAG_ATTRIBUTE)) {
			processAttributeElement(element);
		} else if (tag.equals(TAG_CONTEXTTYPE)) {
			processContextTypeElement(element);

			// make sure processing of current open on tag resulted in a current definition
			// before continue reading the children
			if (fCurrentIncludeDefinition != null) {
				readElementChildren(element);
			}
		} else if (tag.equals(TAG_CONTENTTYPE)) {
			return processContentTypeElement(element);
		} else {
			return false;
		}

		return true;
	}

	/**
	 * Returns all the definitions
	 * 
	 * @return
	 */
	private List<IncludeContextDefinition> getIncludeContextDefinitions() {
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
		List<IncludeContextDefinition> defs = getInstance().getIncludeContextDefinitions();
		String[] result = EMPTY;
		if(!defs.isEmpty()) {
			List<String> attrs = new ArrayList<String>();

			for (IncludeContextDefinition def : defs) {
				if (uri.equals(def.getUri())) {
					String[] defAttrs = def.getIncludeTagAttributes(tagName);
					for (String attr : defAttrs) {
						attrs.add(attr);
					}
				}
			}

			if(!attrs.isEmpty()) {
				result = attrs.toArray(new String[attrs.size()]);
			}
		}
		return result;
	}

	/**
	 * Returns the context type for the specified Content Type
	 * 
	 * @param contentType
	 * @return
	 */
	public static String getContextType(String contentType) {
		if (contentType != null) {
			List<IncludeContextDefinition> defs = getInstance().getIncludeContextDefinitions();
			for (IncludeContextDefinition def : defs) {
				String contextType = def.getContextType(contentType);
				if (contextType != null) {
					return contextType;
				}
			}
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
		boolean isHolder = false;
		List<IncludeContextDefinition> defs = getInstance().getIncludeContextDefinitions();

		for (IncludeContextDefinition def : defs) {
			if (uri.equals(def.getUri())) {
				String[] defTags = def.getCSSTags();
				for (String tag : defTags) {
					if (tagName.equals(tag) || (uri.length()==0 && tagName.equalsIgnoreCase(tag))) {
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
		return isHolder;
	}

	/**
	 * Checks if the specified tag is a JSF2 CSS Style Sheet container
	 * 
	 * @param uri
	 * @param tagName
	 * @return
	 */
	public static boolean isJSF2CSSStyleSheetContainer(String uri, String tagName) {
		boolean isHolder = false;
		List<IncludeContextDefinition> defs = getInstance().getIncludeContextDefinitions();

		for (IncludeContextDefinition def : defs) {
			if (uri.equals(def.getUri())) {
				String[] defTags = def.getJSF2CSSTags();
				for (String tag : defTags) {
					if (tagName.equals(tag) || (uri.length()==0 && tagName.equalsIgnoreCase(tag))) {
						isHolder = true;
						// Check that the tag have no attributes defined
						// If so - the tag itself is used to define the CSS
						// But if the tag has at least one attribute defined - it's not the holder 
						String[] attrs = def.getJSF2CSSTagAttributes(tagName);
						isHolder ^= (attrs != null && attrs.length > 0);  
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
		String[] result = EMPTY;
		List<IncludeContextDefinition> defs = getInstance().getIncludeContextDefinitions();
		if(!defs.isEmpty()) {
			List<String> attrs = new ArrayList<String>();
			for (IncludeContextDefinition def : defs) {
				if (uri.equals(def.getUri())) {
					String[] defAttrs = def.getCSSTagAttributes(tagName);
					for (String attr : defAttrs) {
						attrs.add(attr);
					}
				}
			}
			if(!attrs.isEmpty()) {
				result =  attrs.toArray(new String[attrs.size()]);
			}
		}
		return result;
	}

	/**
	 * Returns the JSF2 CSS Style Sheet attributes that represent a CSS Style Sheet container
	 * 
	 * @param uri
	 * @param tagName
	 * @return
	 */
	public static String[] getJSF2CSSStyleSheetAttributes(String uri, String tagName) {
		String[] result = EMPTY;
		if (uri != null) {
			List<IncludeContextDefinition> defs = getInstance().getIncludeContextDefinitions();
			List<String> attrs = new ArrayList<String>();
			for (IncludeContextDefinition def : defs) {
				if (uri.equals(def.getUri())) {
					String[] defAttrs = def.getJSF2CSSTagAttributes(tagName);
					if (defAttrs != null) {
						for (String attr : defAttrs) attrs.add(attr);
					}
				}
			}
			result = attrs.toArray(new String[attrs.size()]);
		}
		return result;
	}
}
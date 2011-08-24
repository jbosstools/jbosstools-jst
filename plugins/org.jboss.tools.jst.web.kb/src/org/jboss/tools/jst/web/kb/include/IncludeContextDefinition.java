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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;

/**
 * IncludeContextDefinition is used to store the definitions read from the Include Schema
 * 
 * @author Victor Rubezhny
 *
 */
public class IncludeContextDefinition {
	private String fUri;
	private Map<String, Set<String>> fIncludeTags = new HashMap<String, Set<String>>(); // Map<TagName, Set<AttributeName>>
	private Map<String, Set<String>> fCSSTags = new HashMap<String, Set<String>>(); // Map<TagName, Set<AttributeName>>
	private Map<String, Set<String>> fJSF2CSSTags = new HashMap<String, Set<String>>(); // Map<TagName, Set<AttributeName>>
	private Map<String, Set<String>> fContexts = new HashMap<String, Set<String>>(); // Map<ContextType, Set<ContentType>>

	/**
	 * Created the IncludeContextDefinition object for the specified URI
	 * 
	 * @param uri
	 */
	public IncludeContextDefinition(String uri) {
		this.fUri = uri;
	}

	/**
	 * Returns the URI for this IncludeContextDefinition object
	 * 
	 * @return
	 */
	public String getUri() {
		return fUri;
	}

	/**
	 * Sets up the specified URI for this IncludeContextDefinition object
	 * 
	 * @param uri
	 */
	public void setUri(String uri) {
		this.fUri = uri;
	}

	/**
	 * Adds a tag to the definition
	 * 
	 * @param tagName
	 * @param element
	 * @return
	 */
	public boolean addTag(String tagName, IConfigurationElement element) {
		if ("".equals(fUri)) //$NON-NLS-1$
			tagName = tagName.toLowerCase();
		if (isInParentElements(element, IncludeContextBuilder.TAG_INCLUDE)) {
			addIncludeTag(tagName, element);
			return true;
		} else if (isInParentElements(element, IncludeContextBuilder.TAG_CSSHOLDER)) {
			addCSSTag(tagName, element);
			return true;
		} else if (isInParentElements(element, IncludeContextBuilder.TAG_JSF2CSSHOLDER)) {
			addJSF2CSSTag(tagName, element);
			return true;
		}
		return false;
	}
	
	/**
	 * Adds an Include Tag to the Definition
	 * 
	 * @param tagName
	 * @param element
	 */
	private void addIncludeTag(String tagName, IConfigurationElement element) {
		Set<String> tagSet = fIncludeTags.get(tagName);
		if (tagSet == null) {
			tagSet = new HashSet<String>();
			fIncludeTags.put(tagName, tagSet);
		}
	}

	/**
	 * Adds a CSS Style Sheet holder to the Definition
	 * 
	 * @param tagName
	 * @param element
	 */
	private void addCSSTag(String tagName, IConfigurationElement element) {
		Set<String> tagSet = fCSSTags.get(tagName);
		if (tagSet == null) {
			tagSet = new HashSet<String>();
			fCSSTags.put(tagName, tagSet);
		}
	}

	/**
	 * Adds a CSS Style Sheet holder to the Definition
	 * 
	 * @param tagName
	 * @param element
	 */
	private void addJSF2CSSTag(String tagName, IConfigurationElement element) {
		Set<String> tagSet = fJSF2CSSTags.get(tagName);
		if (tagSet == null) {
			tagSet = new HashSet<String>();
			fJSF2CSSTags.put(tagName, tagSet);
		}
	}
	
	/**
	 * Adds a Tag Attribute to the Definition
	 * 
	 * @param attributeName
	 * @param element
	 */
	public void addTagAttribute(String attributeName, IConfigurationElement element) {
		IConfigurationElement parentTagElement = null;
		if (element.getParent() instanceof IConfigurationElement) {
			IConfigurationElement parentElement = (IConfigurationElement)element.getParent();
			if (IncludeContextBuilder.TAG_TAG.equals(parentElement.getName())) {
				parentTagElement = parentElement;
			}
		}
		
		String parentTagName = null;
		if (parentTagElement != null) {
			parentTagName = IncludeContextBuilder.getName(parentTagElement);
		}
		parentTagName = parentTagName == null ? "" : parentTagName; //$NON-NLS-1$
		if ("".equals(fUri)) //$NON-NLS-1$
			parentTagName = parentTagName.toLowerCase();
		
		if (isInParentElements(element, IncludeContextBuilder.TAG_INCLUDE)) {
			if (fIncludeTags.get(parentTagName) == null) {
				addIncludeTag(parentTagName, parentTagElement);
			}
			
			fIncludeTags.get(parentTagName).add(attributeName);
		} else if (isInParentElements(element, IncludeContextBuilder.TAG_CSSHOLDER)) {
			if (fCSSTags.get(parentTagName) == null) {
				addCSSTag(parentTagName, parentTagElement);
			}
			
			fCSSTags.get(parentTagName).add(attributeName);
		} else if (isInParentElements(element, IncludeContextBuilder.TAG_JSF2CSSHOLDER)) {
			if (fJSF2CSSTags.get(parentTagName) == null) {
				addJSF2CSSTag(parentTagName, parentTagElement);
			}
			
			fJSF2CSSTags.get(parentTagName).add(attributeName);
		}
	}
	
	/**
	 * Checks if the configuration element with the name specified exists in parents of the 
	 * specified configuration element
	 * 
	 * @param element
	 * @param elementName
	 * @return
	 */
	private boolean isInParentElements(IConfigurationElement element, String elementName) {
		Object parent = element.getParent();
		while (parent instanceof IConfigurationElement) {
			IConfigurationElement parentElement = (IConfigurationElement)parent;
			if (elementName.equals(parentElement.getName())) {
				return true;
			}
			parent = parentElement.getParent();
		}
		return false;
	}
	
	/**
	 * Adds a Context Type to the Definition
	 * 
	 * @param id
	 * @param element
	 */
	public void addContextType(String id, IConfigurationElement element) {
		Set<String> contextSet = fContexts.get(id);
		if (contextSet == null) {
			contextSet = new HashSet<String>();
			fContexts.put(id, contextSet);
		}
		
	}

	/**
	 * Adds a Content Type to the Definition
	 * 
	 * @param id
	 * @param element
	 * @return
	 */
	public boolean addContentType(String id, IConfigurationElement element) {
		IConfigurationElement parentContextElement = null;
		if (element.getParent() instanceof IConfigurationElement) {
			IConfigurationElement parentElement = (IConfigurationElement)element.getParent();
			if (IncludeContextBuilder.TAG_CONTEXTTYPE.equals(parentElement.getName())) {
				parentContextElement = parentElement;
			}
		}
		
		String parentContextId = null;
		if (parentContextElement != null) {
			parentContextId = IncludeContextBuilder.getId(parentContextElement);
		}
		
		if (fContexts.get(parentContextId) == null) 
			return false;
		
		fContexts.get(parentContextId).add(id);
		return true;
	}

	private static final String[] EMPTY_CHILDREN = new String[0];
	
	/**
	 * Returns the Include Tags stored in the Definition
	 * 
	 * @return
	 */
	public String[] getIncludeTags() {
		return fIncludeTags.isEmpty() ? EMPTY_CHILDREN :
			(String[])fIncludeTags.keySet().toArray(new String[fIncludeTags.size()]);
	}	
	
	/**
	 * Returns the CSS Style Sheet holder Tags stored in the Definition
	 * 
	 * @return
	 */
	public String[] getCSSTags() {
		return fCSSTags.isEmpty() ? EMPTY_CHILDREN :
			(String[])fCSSTags.keySet().toArray(new String[fCSSTags.size()]);
	}
	/**
	 * Returns the JSF2 CSS Style Sheet holder Tags stored in the Definition
	 * 
	 * @return
	 */
	public String[] getJSF2CSSTags() {
		return fJSF2CSSTags.isEmpty() ? EMPTY_CHILDREN :
			(String[])fJSF2CSSTags.keySet().toArray(new String[fJSF2CSSTags.size()]);
	}
	
	/**
	 * Returns the Attributes for the Include Tag with the specified Name
	 * 
	 * @param tagName
	 * @return
	 */
	public String[] getIncludeTagAttributes(String tagName) {
		return getTagAttributes(fIncludeTags, tagName);
	}

	/**
	 * Returns the Attributes for the CSS Style Sheet Holder Tag with the specified Name 
	 * 
	 * @param tagName
	 * @return
	 */
	public String[] getCSSTagAttributes(String tagName) {
		return getTagAttributes(fCSSTags, tagName);
	}

	/**
	 * Returns the Attributes for the CSS Style Sheet Holder Tag with the specified Name 
	 * 
	 * @param tagName
	 * @return
	 */
	public String[] getJSF2CSSTagAttributes(String tagName) {
		return getTagAttributes(fJSF2CSSTags, tagName);
	}

	private String[] getTagAttributes (Map<String, Set<String>> tags, String tagName) {
		if ("".equals(fUri)) //$NON-NLS-1$
			tagName = tagName.toLowerCase();

		Set<String> attrSet = tags == null ? null : tags.get(tagName);
		
		return attrSet == null ? EMPTY_CHILDREN :
			(String[])attrSet.toArray(new String[attrSet.size()]);
	}
	
	/** 
	 * Returns the ContextType for the specified Content Type
	 * 
	 * @param contentType
	 * @return
	 */
	public String getContextType(String contentType) {
		for (String contextType : fContexts.keySet()) {
			if (fContexts.get(contextType).contains(contentType))
				return contextType;
		}
		return null;
	}
}
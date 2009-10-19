package org.jboss.tools.jst.web.kb.include;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.ui.internal.EarlyStartupRunnable;

public class IncludeContextDefinition {
	private String fUri;
	private Map<String, Set<String>> fIncludeTags; // Map<TagName, Set<AttributeName>>
	private Map<String, Set<String>> fCSSTags; // Map<TagName, Set<AttributeName>>
	private Map<String, Set<String>> fContexts; // Map<ContextType, Set<ContentType>>
	
	public IncludeContextDefinition(String uri) {
		this.fUri = uri;
	}

	public String getUri() {
		return fUri;
	}

	public void setUri(String uri) {
		this.fUri = uri;
	}

	public boolean addTag(String tagName, IConfigurationElement element) {
		if ("".equals(fUri)) //$NON-NLS-1$
			tagName = tagName.toLowerCase();
		if (isInParentElements(element, IncludeContextBuilder.TAG_INCLUDE)) {
			addIncludeTag(tagName, element);
			return true;
		} else if (isInParentElements(element, IncludeContextBuilder.TAG_CSSHOLDER)) {
			addCSSTag(tagName, element);
			return true;
		}
		return false;
	}
	
	public void addIncludeTag(String tagName, IConfigurationElement element) {
		if (fIncludeTags == null) {
			fIncludeTags = new HashMap<String, Set<String>>();
		}
		Set<String> tagSet = fIncludeTags.get(tagName);
		if (tagSet == null) {
			tagSet = new HashSet<String>();
			fIncludeTags.put(tagName, tagSet);
		}
	}

	public void addCSSTag(String tagName, IConfigurationElement element) {
		if (fCSSTags == null) {
			fCSSTags = new HashMap<String, Set<String>>();
		}
		Set<String> tagSet = fCSSTags.get(tagName);
		if (tagSet == null) {
			tagSet = new HashSet<String>();
			fCSSTags.put(tagName, tagSet);
		}
	}
	
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
		}
	}
	
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
	
	public void addContextType(String id, IConfigurationElement element) {
		if (fContexts == null) {
			fContexts = new HashMap<String, Set<String>>();
		}
		Set<String> contextSet = fContexts.get(id);
		if (contextSet == null) {
			contextSet = new HashSet<String>();
			fContexts.put(id, contextSet);
		}
		
	}

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
	
	
	public String[] getIncludeTags() {
		return fIncludeTags == null ? EMPTY_CHILDREN :
			(String[])fIncludeTags.keySet().toArray(new String[fIncludeTags.size()]);
	}	
	
	public String[] getCSSTags() {
		return fCSSTags == null ? EMPTY_CHILDREN :
			(String[])fCSSTags.keySet().toArray(new String[fCSSTags.size()]);
	}
	
	public String[] getIncludeTagAttributes(String tagName) {
		if ("".equals(fUri)) //$NON-NLS-1$
			tagName = tagName.toLowerCase();

		Set<String> attrSet = fIncludeTags == null ? null : fIncludeTags.get(tagName);
		
		return attrSet == null ? EMPTY_CHILDREN :
			(String[])attrSet.toArray(new String[attrSet.size()]);
	}

	public String[] getCSSTagAttributes(String tagName) {
		if ("".equals(fUri)) //$NON-NLS-1$
			tagName = tagName.toLowerCase();

		Set<String> attrSet = fCSSTags == null ? null : fCSSTags.get(tagName);
		
		return attrSet == null ? EMPTY_CHILDREN :
			(String[])attrSet.toArray(new String[attrSet.size()]);
	}
	public String getContextType(String contentType) {
		if (fContexts == null)
			return null;
		
		for (String contextType : fContexts.keySet()) {
			if (fContexts.get(contextType).contains(contentType))
				return contextType;
		}
		return null;
	}

}

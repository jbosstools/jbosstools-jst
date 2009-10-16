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
	private IConfigurationElement fElement;
	private String fUri;
	private Map<String, Set<String>> fTags; // Map<TagName, Set<AttributeName>>
	private Map<IConfigurationElement, String> fTagConfigurationElements; 
	private Map<String, Set<String>> fContexts; // Map<ContextType, Set<ContentType>>
	private Map<IConfigurationElement, String> fContextConfigurationElements; 
	
	public IncludeContextDefinition(IConfigurationElement element) {
		this.fElement = element;
	}
	
	public IncludeContextDefinition(String uri, IConfigurationElement element) {
		this.fUri = uri;
		this.fElement = element;
	}

	public String getUri() {
		return fUri;
	}

	public void setUri(String uri) {
		this.fUri = uri;
	}

	public void addTag(String tagName, IConfigurationElement element) {
		if (fTags == null) {
			fTags = new HashMap<String, Set<String>>();
		}
		Set<String> tagSet = fTags.get(tagName);
		if (tagSet == null) {
			tagSet = new HashSet<String>();
			fTags.put(tagName, tagSet);
		}
		
		if (fTagConfigurationElements == null) {
			fTagConfigurationElements = new HashMap<IConfigurationElement, String>();
		}
		
		fTagConfigurationElements.put(element, tagName);
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
		
		if (fTags.get(parentTagName) == null) {
			addTag(parentTagName, parentTagElement);
		}
		
		fTags.get(parentTagName).add(attributeName);
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
		
		if (fContextConfigurationElements == null) {
			fContextConfigurationElements = new HashMap<IConfigurationElement, String>();
		}
		
		fContextConfigurationElements.put(element, id);
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
	
	
	public String[] getTags() {
		return fTags == null ? EMPTY_CHILDREN :
			(String[])fTags.keySet().toArray(new String[fTags.size()]);
	}
	
	public String[] getTagAttributes(String tagName) {
		Set<String> attrSet = fTags.get(tagName);
		
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

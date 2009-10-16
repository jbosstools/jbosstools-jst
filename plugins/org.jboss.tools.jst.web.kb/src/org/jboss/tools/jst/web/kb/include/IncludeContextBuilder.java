package org.jboss.tools.jst.web.kb.include;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.internal.registry.RegistryReader;

import org.jboss.tools.jst.web.kb.WebKbPlugin;

public class IncludeContextBuilder extends RegistryReader {
	// extension point ID
	public static final String PL_INCLUDE = "KbIncludeContext"; //$NON-NLS-1$

	public static final String TAG_INCLUDE = "include"; //$NON-NLS-1$
	public static final String TAG_TAG = "tag"; //$NON-NLS-1$
	public static final String TAG_ATTRIBUTE = "attribute"; //$NON-NLS-1$
	public static final String TAG_CONTEXTTYPE = "contexttype"; //$NON-NLS-1$
	public static final String TAG_CONTENTTYPE = "contenttype"; //$NON-NLS-1$

	public static final String ATT_ID = "id"; //$NON-NLS-1$
	public static final String ATT_URI = "uri"; //$NON-NLS-1$
	public static final String ATT_NAME = "name"; //$NON-NLS-1$

	protected String fTargetContributionElement;

	private static IncludeContextBuilder fInstance;

	private List<IncludeContextDefinition> fIncludeContextDefs = null;
	private IncludeContextDefinition fCurrentIncludeDefinition = null;

	/**
	 * returns singleton instance of HyperlinkBuilder
	 * 
	 * @return HyperlinkBuilder
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

	public static String getUri(IConfigurationElement element) {
		String value = element.getAttribute(ATT_URI);
		return value;
	}

	public static String getName(IConfigurationElement element) {
		String value = element.getAttribute(ATT_NAME);
		return value;
	}

	/**
	 * Processes element which should be a configuration element specifying an
	 * open on object.  Creates a new open on definition object and adds it to the
	 * list of open on definition objects
	 * 
	 * @param element hyperlink configuration element
	 */
	private void processIncludeElement(IConfigurationElement element) {
		String theUri = getUri(element);
//		String theResolver = getIncludeResolver(element);

		theUri = theUri == null ? "" : theUri; //$NON-NLS-1$
		
		// start building new HyperlinkDefinition
		fCurrentIncludeDefinition = new IncludeContextDefinition(theUri, element);
		
		// create a new list of open on definitions if it hasnt been created yet
		if (fIncludeContextDefs == null) {
			fIncludeContextDefs = new ArrayList<IncludeContextDefinition>();
		}
		fIncludeContextDefs.add(fCurrentIncludeDefinition);
	}

	private void processTagElement(IConfigurationElement element) {
		String theName = getName(element);

		if (fCurrentIncludeDefinition != null && theName != null) {
			fCurrentIncludeDefinition.addTag(theName, element);
		}
	}

	private void processAttributeElement(IConfigurationElement element) {
		String theName = getName(element);

		if (fCurrentIncludeDefinition != null && theName != null) {
			fCurrentIncludeDefinition.addTagAttribute(theName, element);
		}
	}
	
	private void processContextTypeElement(IConfigurationElement element) {
		String theId = getId(element);

		if (fCurrentIncludeDefinition != null && theId != null) {
			fCurrentIncludeDefinition.addContextType(theId, element);
		}
	}
	
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

		if (tag.equals(fTargetContributionElement)) {
			processIncludeElement(element);

			// make sure processing of current open on tag resulted in a current open on definition
			// before continue reading the children
			if (fCurrentIncludeDefinition != null) {
				readElementChildren(element);
			}
			return true;
		}
		else if (tag.equals(TAG_TAG)) {
			processTagElement(element);

			// make sure processing of current open on tag resulted in a current open on definition
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

			// make sure processing of current open on tag resulted in a current open on definition
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


	protected void readContributions(String element, String extensionPoint) {
		fTargetContributionElement = element;
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		readRegistry(registry, WebKbPlugin.PLUGIN_ID, extensionPoint);
	}

	/**
	 * Returns all the open on definition objects
	 * @return
	 */
	public List<IncludeContextDefinition> getIncludeContextDefinitions() {
		initCache();
		return fIncludeContextDefs;
	}
	
	public static String[] getIncludeAttributes(String uri, String tag) {
		if (uri == null)
			return null;
		
		List<IncludeContextDefinition> defs = IncludeContextBuilder.getInstance().getIncludeContextDefinitions();
		if (defs == null)
			return null;
		
		for (IncludeContextDefinition def : defs) {
			if (uri.equals(def.getUri())) {
				return def.getTagAttributes(tag);
			}
		}
		
		return null;
	}
	
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
}

/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Exadel, Inc.
 *     Red Hat, Inc.
 *******************************************************************************/
package org.jboss.tools.jst.web.debug.ui.internal.views.properties.xpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

/**
 * @author Jeremy
 *
 */
public class WebDataProperties implements IPropertyChangeListener {

	public static final String FILTER_POSTFIX = ".Filter";
	public static final String VALUE_POSTFIX = ".Value";
	
	// Web Properties
	public static final String SHOW_WEB_VARIABLES_PROPERTY = "showWebVariables";
	public static final String SHOW_HTTP_SERVLET_REQUEST_PROPERTY = "showHttpServletRequest";
	public static final String SHOW_HTTP_SERVLET_RESPONSE_PROPERTY = "showHttpServletResponse";
	public static final String SHOW_HTTP_SESSION_PROPERTY = "showHttpSession";
	public static final String SHOW_PAGE_CONTEXT_PROPERTY = "showPageContext";
	public static final String SHOW_SERVLET_CONTEXT_PROPERTY = "showServletContext";

	public static final String SHOW_WEB_PAGECONTEXT_FILTER = "WebDataProperties.Web.PageContext.Filter";
	public static final String SHOW_WEB_PAGECONTEXT_ATTRIBUTES = "WebDataProperties.Web.PageContext.Attributes";
	public static final String SHOW_WEB_SERVLETCONTEXT_FILTER = "WebDataProperties.Web.ServletContext.Filter";
	public static final String SHOW_WEB_SERVLETCONTEXT_INITPARAMETERS = "WebDataProperties.Web.ServletContext.InitParameters";
	public static final String SHOW_WEB_SERVLETCONTEXT_ATTRIBUTES = "WebDataProperties.Web.ServletContext.Attributes";
	public static final String SHOW_WEB_HTTPSESSION_FILTER = "WebDataProperties.Web.HttpSession.Filter";
	public static final String SHOW_WEB_HTTPSESSION_ATTRIBUTES = "WebDataProperties.Web.HttpSession.Attributes";
	public static final String SHOW_WEB_HTTPSERVLETRESPONSE_FILTER = "WebDataProperties.Web.HttpServletResponse.Filter";
	public static final String SHOW_WEB_HTTPSERVLETREQUEST_FILTER = "WebDataProperties.Web.HttpServletRequest.Filter";
	public static final String SHOW_WEB_HTTPSERVLETREQUEST_PARAMETERS = "WebDataProperties.Web.HttpServletRequest.Parameters";
	public static final String SHOW_WEB_HTTPSERVLETREQUEST_ATTRIBUTES = "WebDataProperties.Web.HttpServletRequest.Attributes";
	public static final String SHOW_WEB_HTTPSERVLETREQUEST_SESSIONPROPERTY = "WebDataProperties.Web.HttpServletRequest.SessionProperty";
	public static final String SHOW_WEB_HTTPSERVLETREQUEST_HEADERS = "WebDataProperties.Web.HttpServletRequest.Headers";


	// Struts properties
	public static final String SHOW_STRUTS_VARIABLES_PROPERTY = "showStrutsVariables";
	public static final String SHOW_DYNA_ACTION_FORM_AND_SUBCLASSES_PROPERTY = "showDynaActionFormSubclasses";
	public static final String SHOW_ACTION_MESSAGES_AND_ERRORS_PROPERTY = "showActionMessagesAndErrors";
	public static final String SHOW_ACTION_MAPPING_PROPERTY = "showActionMapping";
	public static final String SHOW_ACTION_FORWARD_PROPERTY = "showActionForward";
	public static final String SHOW_ACTION_PROPERTY = "showAction";

	public static final String SHOW_ACTIONPROPERTY_FILTER = "WebDataProperties.ActionProperty.Filter";
	public static final String SHOW_STRUTS_ACTIONFORWARD_FILTER = "WebDataProperties.Struts.ActionForward.Filter";
	public static final String SHOW_STRUTS_ACTIONMAPPING_FILTER = "WebDataProperties.Struts.ActionMapping.Filter";
	public static final String SHOW_STRUTS_ACTIONMESSAGESANDERRORS_FILTER = "WebDataProperties.Struts.ActionMessagesAndErrors.Filter";
	public static final String SHOW_STRUTS_DYNAACTIONFORM_FILTER = "WebDataProperties.Struts.DynaActionForm.Filter";

	private static Map enablements = new HashMap(12);
	private static Map filters = new HashMap(2);
	
	private IPreferenceStore fStore;

	public WebDataProperties (IPreferenceStore store) {
		this.fStore = store;
		try {
			fStore.addPropertyChangeListener(this);
		} catch (Exception x) {
			//ignore
		}

	}
	
	public boolean isEnabledFilter(String filter) {
		try {
			Boolean value = (Boolean)enablements.get(filter);
			if (value == null && fStore != null) {
				value = new Boolean(fStore.getBoolean(filter));
				synchronized (this) {
					enablements.put(filter, value);
				}
			}
			return (value == null ? false : value.booleanValue());
		} catch (Exception x) {
			//ignore
			return false;
		}
	}

	public String getFilterMask(String filter) {
		try {
			String value = (String)filters.get(filter);
			if (value == null && fStore != null) {
				value = fStore.getString(filter);
				synchronized (this) {
					enablements.put(filter, value);
				}
			}
			return (value == null ? "" : value);
		} catch (Exception x) {
			//ignore
			return "";
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent event) {
		String p= event.getProperty();
		try {
			synchronized (this) {
				if (enablements.containsKey(p)) {
					boolean newValue = false;
					Object value= event.getNewValue();
					if (value instanceof Boolean)
						newValue = ((Boolean) value).booleanValue();
					else if (value instanceof String) {
						String s= (String) value;
						if (IPreferenceStore.TRUE.equals(s))
							newValue= true;
						else if (IPreferenceStore.FALSE.equals(s))
							newValue= false;
					}
					
					enablements.put(p, new Boolean(newValue));
					fireWebDataPropertyChanged(p);
				}
				if (filters.containsKey(p)) {
					String newValue = "";
					Object value= event.getNewValue();
					if (value instanceof String) {
						newValue = (String)value;
					}
					
					filters.put(p, newValue);
					fireWebDataPropertyChanged(p);
				}
			}
		} catch (Exception x) {
			//ignore
		}
	}

	/**
	 * Initializes the given preference store with the default values.
	 *
	 * @param store the preference store to be initialized
	 *
	 * @since 2.1
	 */
	
	public static void initializeDefaultValues(final IPreferenceStore store) {
		store.setDefault(SHOW_WEB_VARIABLES_PROPERTY, true);

		store.setDefault(SHOW_HTTP_SERVLET_REQUEST_PROPERTY, true);
		store.setDefault(SHOW_HTTP_SERVLET_RESPONSE_PROPERTY, true);
		store.setDefault(SHOW_HTTP_SESSION_PROPERTY, true);
		store.setDefault(SHOW_PAGE_CONTEXT_PROPERTY, true);
		store.setDefault(SHOW_SERVLET_CONTEXT_PROPERTY, true);

		store.setDefault(SHOW_WEB_PAGECONTEXT_FILTER, true);
		store.setDefault(SHOW_WEB_SERVLETCONTEXT_FILTER, true);
		store.setDefault(SHOW_WEB_HTTPSESSION_FILTER, true);
		store.setDefault(SHOW_WEB_HTTPSERVLETRESPONSE_FILTER, true);
		store.setDefault(SHOW_WEB_HTTPSERVLETREQUEST_FILTER, true);
		
		store.setDefault(SHOW_WEB_PAGECONTEXT_FILTER+VALUE_POSTFIX, "*");
		store.setDefault(SHOW_WEB_SERVLETCONTEXT_FILTER+VALUE_POSTFIX, "*");
		store.setDefault(SHOW_WEB_HTTPSESSION_FILTER+VALUE_POSTFIX, "*");
		store.setDefault(SHOW_WEB_HTTPSERVLETRESPONSE_FILTER+VALUE_POSTFIX, "*");
		store.setDefault(SHOW_WEB_HTTPSERVLETREQUEST_FILTER+VALUE_POSTFIX, "*");
		
		store.setDefault(SHOW_WEB_PAGECONTEXT_ATTRIBUTES, true);
		store.setDefault(SHOW_WEB_SERVLETCONTEXT_INITPARAMETERS, true);
		store.setDefault(SHOW_WEB_SERVLETCONTEXT_ATTRIBUTES, true);
		store.setDefault(SHOW_WEB_HTTPSESSION_ATTRIBUTES, true);
		store.setDefault(SHOW_WEB_HTTPSERVLETREQUEST_PARAMETERS, true);
		store.setDefault(SHOW_WEB_HTTPSERVLETREQUEST_ATTRIBUTES, true);
		store.setDefault(SHOW_WEB_HTTPSERVLETREQUEST_SESSIONPROPERTY, true);
		store.setDefault(SHOW_WEB_HTTPSERVLETREQUEST_HEADERS, true);

		store.setDefault(SHOW_STRUTS_VARIABLES_PROPERTY, true);

		store.setDefault(SHOW_DYNA_ACTION_FORM_AND_SUBCLASSES_PROPERTY, true);
		store.setDefault(SHOW_ACTION_MESSAGES_AND_ERRORS_PROPERTY, true);
		store.setDefault(SHOW_ACTION_MAPPING_PROPERTY, true);
		store.setDefault(SHOW_ACTION_FORWARD_PROPERTY, true);
		store.setDefault(SHOW_ACTION_PROPERTY, true);

		store.setDefault(SHOW_ACTIONPROPERTY_FILTER, true);
		store.setDefault(SHOW_STRUTS_ACTIONFORWARD_FILTER, true);
		store.setDefault(SHOW_STRUTS_ACTIONMAPPING_FILTER, true);
		store.setDefault(SHOW_STRUTS_ACTIONMESSAGESANDERRORS_FILTER, true);
		store.setDefault(SHOW_STRUTS_DYNAACTIONFORM_FILTER, true);

		store.setDefault(SHOW_ACTIONPROPERTY_FILTER+VALUE_POSTFIX, "*");
		store.setDefault(SHOW_STRUTS_ACTIONFORWARD_FILTER+VALUE_POSTFIX, "*");
		store.setDefault(SHOW_STRUTS_ACTIONMAPPING_FILTER+VALUE_POSTFIX, "*");
		store.setDefault(SHOW_STRUTS_ACTIONMESSAGESANDERRORS_FILTER+VALUE_POSTFIX, "*");
		store.setDefault(SHOW_STRUTS_DYNAACTIONFORM_FILTER+VALUE_POSTFIX, "*");

	}
	private List fListeners = new ArrayList();
	public void addWebDataPropertyChangeListener (IWebDataPropertyChangeListener listener) {
		if (!fListeners.contains(listener)) fListeners.add(listener);
	}
	public void removeWebDataPropertyChangeListener (IWebDataPropertyChangeListener listener) {
		if (fListeners.contains(listener)) fListeners.remove(listener);
	}
	private void fireWebDataPropertyChanged(String property) {
		IWebDataPropertyChangeListener[] listeners = (IWebDataPropertyChangeListener[])fListeners.toArray(new IWebDataPropertyChangeListener[fListeners.size()]);
		for (int i = 0; listeners != null && i < listeners.length; i++) {
			try {
				listeners[i].propertyChanged(property);
			} catch (Exception x) {
				//ignore
			}
		}
	}

}

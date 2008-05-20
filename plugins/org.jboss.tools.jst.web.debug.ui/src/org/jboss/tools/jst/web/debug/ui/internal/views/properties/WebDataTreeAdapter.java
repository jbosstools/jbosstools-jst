/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.web.debug.ui.internal.views.properties;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.jboss.tools.common.meta.action.XEntityData;
import org.jboss.tools.common.meta.action.impl.XEntityDataImpl;
import org.jboss.tools.common.model.ServiceDialog;
import org.jboss.tools.common.model.options.PreferenceModelUtilities;
import org.jboss.tools.common.model.ui.attribute.adapter.CheckTreeAdapter;
import org.jboss.tools.common.model.ui.attribute.adapter.CheckTreeAdapter.CheckItem;
import org.jboss.tools.common.model.ui.attribute.adapter.CheckTreeAdapter.CheckTree;
import org.jboss.tools.common.model.ui.viewers.xpl.CheckStateChangedEvent;
import org.jboss.tools.common.model.ui.viewers.xpl.ICheckStateListener;
import org.jboss.tools.common.model.ui.viewers.xpl.ICheckable;
import org.jboss.tools.jst.web.debug.ui.internal.views.properties.WebDataPreferencePage.ButtonController;
import org.jboss.tools.jst.web.debug.ui.internal.views.properties.xpl.WebDataProperties;
import org.jboss.tools.jst.web.debug.ui.xpl.WebDebugUIPlugin;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;


/**
 * @author au
 */
public class WebDataTreeAdapter extends CheckTreeAdapter implements ICheckStateListener {
	
	public static final String FILTER_PREFIX = "WebDataProperties.FilterPrefix"; //$NON-NLS-1$
	
	private IPreferenceStore store;
	private ButtonController buttonController;
	private String selectedKey;
	private HashMap values = new HashMap();
	
	public WebDataTreeAdapter() {
		labelProvider = new WebDataLabelProvider();
		store = WebDebugUIPlugin.getDefault().getPreferenceStore();
		
		tree = new CheckTree();
		// Web
		CheckItem webRoot = createCheckItem(WebDataProperties.SHOW_WEB_VARIABLES_PROPERTY);
		// HttpServletRequest
		//   Headers
		//   Session property
		//   Attributes
		//   Parameters
		//   Filter
		CheckItem itemHttpServletRequest = createCheckItem(WebDataProperties.SHOW_HTTP_SERVLET_REQUEST_PROPERTY); 
		webRoot.addChild(itemHttpServletRequest);
		itemHttpServletRequest.addChild(createCheckItem(WebDataProperties.SHOW_WEB_HTTPSERVLETREQUEST_HEADERS));
		itemHttpServletRequest.addChild(createCheckItem(WebDataProperties.SHOW_WEB_HTTPSERVLETREQUEST_SESSIONPROPERTY));
		itemHttpServletRequest.addChild(createCheckItem(WebDataProperties.SHOW_WEB_HTTPSERVLETREQUEST_ATTRIBUTES));
		itemHttpServletRequest.addChild(createCheckItem(WebDataProperties.SHOW_WEB_HTTPSERVLETREQUEST_PARAMETERS));
		itemHttpServletRequest.addChild(createCheckItem(WebDataProperties.SHOW_WEB_HTTPSERVLETREQUEST_FILTER));

		// HttpServletResponse 
		//   Filter
		CheckItem itemHttpServletResponse = createCheckItem(WebDataProperties.SHOW_HTTP_SERVLET_RESPONSE_PROPERTY);
		webRoot.addChild(itemHttpServletResponse);
		itemHttpServletResponse.addChild(createCheckItem(WebDataProperties.SHOW_WEB_HTTPSERVLETRESPONSE_FILTER));
		
		// HttpSession
		//   Attributes
		//   Filter
		CheckItem itemHttpSession = createCheckItem(WebDataProperties.SHOW_HTTP_SESSION_PROPERTY);
		webRoot.addChild(itemHttpSession);
		itemHttpSession.addChild(createCheckItem(WebDataProperties.SHOW_WEB_HTTPSESSION_ATTRIBUTES));
		itemHttpSession.addChild(createCheckItem(WebDataProperties.SHOW_WEB_HTTPSESSION_FILTER));

		// ServletContext
		//   Attributes
		//   InitParameters
		//   Filter
		CheckItem itemServletContext = createCheckItem(WebDataProperties.SHOW_SERVLET_CONTEXT_PROPERTY);
		webRoot.addChild(itemServletContext);
		itemServletContext.addChild(createCheckItem(WebDataProperties.SHOW_WEB_SERVLETCONTEXT_ATTRIBUTES));
		itemServletContext.addChild(createCheckItem(WebDataProperties.SHOW_WEB_SERVLETCONTEXT_INITPARAMETERS));
		itemServletContext.addChild(createCheckItem(WebDataProperties.SHOW_WEB_SERVLETCONTEXT_FILTER));

		// PageContext
		//   Attributes
		//   Filter
		CheckItem itemPageContext = createCheckItem(WebDataProperties.SHOW_PAGE_CONTEXT_PROPERTY);
		webRoot.addChild(itemPageContext);
		itemPageContext.addChild(createCheckItem(WebDataProperties.SHOW_WEB_PAGECONTEXT_ATTRIBUTES));
		itemPageContext.addChild(createCheckItem(WebDataProperties.SHOW_WEB_PAGECONTEXT_FILTER));

		//webRoot.addChild(createCheckItem(WebDataProperties.SHOW_WEB_VARIABLES_BY_FILTER_MASK_PROPERTY));
		

		
		// Struts
		CheckItem strutsRoot = createCheckItem(WebDataProperties.SHOW_STRUTS_VARIABLES_PROPERTY);

		// DynaActionForm
		//   Filter
		CheckItem itemDynaActionForm = createCheckItem(WebDataProperties.SHOW_DYNA_ACTION_FORM_AND_SUBCLASSES_PROPERTY);
		strutsRoot.addChild(itemDynaActionForm);
		itemDynaActionForm.addChild(createCheckItem(WebDataProperties.SHOW_STRUTS_DYNAACTIONFORM_FILTER));
		
		// ActionMessagesAndErrors
		//   Filter
		CheckItem itemActionMessagesAndErrors = createCheckItem(WebDataProperties.SHOW_ACTION_MESSAGES_AND_ERRORS_PROPERTY);
		strutsRoot.addChild(itemActionMessagesAndErrors);
		itemActionMessagesAndErrors.addChild(createCheckItem(WebDataProperties.SHOW_STRUTS_ACTIONMESSAGESANDERRORS_FILTER));
		
		// ActionMapping
		//   Filter
		CheckItem itemActionMapping = createCheckItem(WebDataProperties.SHOW_ACTION_MAPPING_PROPERTY);
		strutsRoot.addChild(itemActionMapping);
		itemActionMapping.addChild(createCheckItem(WebDataProperties.SHOW_STRUTS_ACTIONMAPPING_FILTER));
		
		// ActionForward
		//   Filter
		CheckItem itemActionForward = createCheckItem(WebDataProperties.SHOW_ACTION_FORWARD_PROPERTY);
		strutsRoot.addChild(itemActionForward);
		itemActionForward.addChild(createCheckItem(WebDataProperties.SHOW_STRUTS_ACTIONFORWARD_FILTER));
		
		// ActionProperty SHOW_ACTION_PROPERTY
		//   Filter
		CheckItem itemActionProperty = createCheckItem(WebDataProperties.SHOW_ACTION_PROPERTY);
		strutsRoot.addChild(itemActionProperty);
		itemActionProperty.addChild(createCheckItem(WebDataProperties.SHOW_ACTIONPROPERTY_FILTER));
		
		//strutsRoot.addChild(createCheckItem(WebDataProperties.SHOW_STRUTS_VARIABLES_BY_FILTER_MASK_PROPERTY));

		
		tree.addRoot(webRoot);
		tree.addRoot(strutsRoot);
		
		tree.addCheckStateListener((ICheckStateListener)this);
	}

	public void dispose() {
		super.dispose();
		if (values!=null) values.clear();
		values = null;
		if (buttonController!=null) buttonController.dispose();
		buttonController = null;
	}

	private CheckItem createCheckItem(String key) {
		return new CheckItem(
				WebDebugUIPlugin.getResourceString(key), 
				store.getBoolean(key)?ICheckable.STATE_CHECK:ICheckable.STATE_UNCHECK, 
				key);	
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.common.model.ui.viewers.ICheckStateListener#checkStateChanged(org.jboss.tools.common.model.ui.viewers.CheckStateChangedEvent)
	 */
	public void checkStateChanged(CheckStateChangedEvent event) {
		CheckItem item = (CheckItem)event.getElement();
		String preferenceString = (String)item.getData();
		if (preferenceString!=null && isAutoStore()) store.setValue(preferenceString, (item.getState() == 1));
	}
	
	class WebDataLabelProvider extends CheckTreeAdapter.CheckLabelProvider {
		public String getText(Object element) {
			CheckItem item = (CheckItem)element;
			String key = (String)item.getData(); 
			if (key!=null && key.endsWith(WebDataProperties.FILTER_POSTFIX)) {
				key = key+WebDataProperties.VALUE_POSTFIX;
				String filterText = (String)values.get(key);
				if (filterText==null) filterText = store.getString(key);
				return WebDebugUIPlugin.getResourceString(FILTER_PREFIX)+filterText;
			}
			return super.getText(element);
		}
	}
	
	class SelectionChangedListener implements ISelectionChangedListener {
		public void selectionChanged(SelectionChangedEvent event) {
			CheckItem item = (CheckItem)((StructuredSelection)event.getSelection()).getFirstElement();
			if (item == null) return;
			String key = (String)item.getData();
			if (key!=null && key.endsWith(WebDataProperties.FILTER_POSTFIX)) {
				// enable button
				selectedKey = key+WebDataProperties.VALUE_POSTFIX;
				buttonController.setEnabled(Boolean.TRUE.booleanValue());
			} else {
				// disable button
				selectedKey = null;
				buttonController.setEnabled(Boolean.FALSE.booleanValue());
			}
		}
	}
	
	class ButtonSelectionListener implements SelectionListener {
		public void widgetDefaultSelected(SelectionEvent e) {
		}
		public void widgetSelected(SelectionEvent e) {
			doButtonPressed();
		}
	}
	
	protected void doButtonPressed() {
		if (selectedKey!=null) {
			String value = (String)values.get(selectedKey);
			if (value==null) value = store.getString(selectedKey);

			ServiceDialog d = PreferenceModelUtilities.getPreferenceModel().getService();
			XEntityData data = XEntityDataImpl.create(new String[][]{
				{"WebData_EditFilterWizard"}, {"filter"} //$NON-NLS-1$ //$NON-NLS-2$
			});
			data.setValue("filter", value); //$NON-NLS-1$
			String title = WebUIMessages.EDIT_FILTER;
			String message = WebUIMessages.ENTER_NEW_FILTER;
			int result = d.showDialog(title, message, new String[]{WebUIMessages.OK, WebUIMessages.CANCEL}, data, ServiceDialog.QUESTION);
			if(result != 0) return;
			value = data.getValue("filter"); //$NON-NLS-1$

			if (isAutoStore()) store.setValue(selectedKey, value);
			values.put(selectedKey, value);
			
			if(viewer != null) viewer.refresh();
		}
	}
	
	public Object getAdapter(Class adapter) {
		if (adapter == ISelectionChangedListener.class) {
			if (selectionChangedListener==null) selectionChangedListener = new SelectionChangedListener();
			return selectionChangedListener;
		}
		return super.getAdapter(adapter);
	}

	/**
	 * @param buttonController
	 */
	public void setButtonController(ButtonController buttonController) {
		this.buttonController = buttonController;
		buttonController.setListener(new ButtonSelectionListener());
	}
	
	Viewer viewer;

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = viewer;
	}

	private void storeItems(CheckItem[] items) {
		for (int i=0;i<items.length;++i) {
			if (items[i].getData()!=null) store.setValue((String)items[i].getData(), items[i].getState()>ICheckable.STATE_UNCHECK);
			if (items[i].hasChildren()) storeItems(items[i].getChildrens());
		}
	}
	
	public void store() {
		// store tree
		// 1. store checks
		storeItems(tree.getRoot());
		// 2. store values
		Set keys = values.keySet();
		Iterator i = keys.iterator();
		String key;
		while (i.hasNext()) {
			key = (String)i.next();
			store.setValue(key, (String)values.get(key));
		}
	}
	
	private void loadDefaultsForItems (CheckItem[] items) {
		for (int i=0;i<items.length;++i) {
			if (items[i].getData()!=null) {
				boolean value = store.getDefaultBoolean((String)items[i].getData());
				items[i].setState(value ? ICheckable.STATE_CHECK : ICheckable.STATE_UNCHECK);
			}
			if (items[i].hasChildren()) loadDefaultsForItems(items[i].getChildrens());
		}
	}
	
	public void loadDefaults () {
		// store tree
		// 1. load default checks
		loadDefaultsForItems(tree.getRoot());
		// 2. load default values
		Set keys = values.keySet();
		Iterator i = keys.iterator();
		String key;
		while (i.hasNext()) {
			key = (String)i.next();
			values.put(key, store.getDefaultString(key));
		}
	}
}

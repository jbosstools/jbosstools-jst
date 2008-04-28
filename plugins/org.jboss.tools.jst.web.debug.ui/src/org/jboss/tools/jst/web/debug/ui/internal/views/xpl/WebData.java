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
package org.jboss.tools.jst.web.debug.ui.internal.views.xpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;
import org.jboss.tools.jst.web.debug.ui.internal.views.properties.xpl.WebDataProperties;
import org.jboss.tools.jst.web.debug.ui.xpl.WebDebugUIPlugin;

/**
 * @author Jeremy
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class WebData implements IVariable, IValue {

	private boolean fHasValueChanged = false;
	
	private IVariable[] fVariables;

	private WebDataProperties wdp;
	private Map fVariablesM = new HashMap();
	private static String[] PROPERTY_NAMES = new String[] {
		WebDataProperties.SHOW_SERVLET_CONTEXT_PROPERTY,
		WebDataProperties.SHOW_HTTP_SESSION_PROPERTY,
		WebDataProperties.SHOW_HTTP_SERVLET_REQUEST_PROPERTY,
		WebDataProperties.SHOW_HTTP_SERVLET_RESPONSE_PROPERTY,
		WebDataProperties.SHOW_PAGE_CONTEXT_PROPERTY
	};

	private long fChangeCount;
	long getChangeCount() {
		return fChangeCount;
	}
	void setChangeCount(long count) {
		fChangeCount = count;
	}

	private void createVariables() {

		fVariablesM.put(WebDataProperties.SHOW_SERVLET_CONTEXT_PROPERTY, 
			new ServletContextVariableProxy(parent, (IVariable)null, "servletContext", "javax.servlet.ServletContext"));

		fVariablesM.put(WebDataProperties.SHOW_HTTP_SESSION_PROPERTY, 
			new HttpSessionVariableProxy(parent, (IVariable)null, "session", "javax.servlet.http.HttpSession"));

		fVariablesM.put(WebDataProperties.SHOW_HTTP_SERVLET_REQUEST_PROPERTY, 
			new HttpServletRequestVariableProxy(parent, (IVariable)null, "request", "javax.servlet.http.HttpServletRequest"));

		fVariablesM.put(WebDataProperties.SHOW_HTTP_SERVLET_RESPONSE_PROPERTY, 
			new HttpServletResponseVariableProxy(parent, (IVariable)null, "response", "javax.servlet.http.HttpServletResponse"));

		fVariablesM.put(WebDataProperties.SHOW_PAGE_CONTEXT_PROPERTY, 
			new PageContextVariableProxy(parent, (IVariable)null, "pageContext", "javax.servlet.jsp.PageContext"));

		updateVariables();
	}

	void updateVariables() {
		List vars = new ArrayList();

		// Add standard vars
		for (int i = 0; i < PROPERTY_NAMES.length; i++) {
			String name = PROPERTY_NAMES[i];
			if (wdp.isEnabledFilter(name)) {
				// Check presence - if not present do add
				vars.add(fVariablesM.get(name));					 
			}
		}
		
		synchronized (this) {
			clearCurrentVariables();
			
			fVariables = new IVariable[vars.size()];
			for (int i = 0; i < vars.size(); i++) {
				fVariables[i] = (IVariable)vars.get(i);
			}
		}
	}
	
	private void clearCurrentVariables() {
		for (int i = 0; fVariables != null && i < fVariables.length; i++) {
			fVariables[i] = null;
		}
		fVariables = null;
	}
	
	/**
	 * @see org.eclipse.debug.core.model.IValue#getValueString()
	 */
	public String getValueString() throws DebugException {
		return null;
	}

	/**
	 * @see org.eclipse.debug.core.model.IValue#isAllocated()
	 */
	public boolean isAllocated() throws DebugException {

		return false;
	}

	/**
	 * @see org.eclipse.debug.core.model.IValue#hasVariables()
	 */
	public boolean hasVariables() throws DebugException {
		return true;
	}

	StackFrameWrapper parent;
		
	public WebData (StackFrameWrapper parent) {
		this.parent = parent;
		wdp = new WebDataProperties(WebDebugUIPlugin.getDefault().getPreferenceStore());
		createVariables();
	}

	public IVariable[] getVariables() {
		if (fHasValueChanged) updateVariables();
				
		return fVariables;
	}

	/**
	 * @see org.eclipse.debug.core.model.IVariable#getValue()
	 */
	public IValue getValue() throws DebugException {
		if (hasValueChanged() ||
				getChangeCount() != parent.getChangeCount()) {
			updateVariables();
		}
		fHasValueChanged = false;
		setChangeCount(parent.getChangeCount());
		return this;
	}

	/**
	 * @see org.eclipse.debug.core.model.IVariable#getName()
	 */
	public String getName() throws DebugException {
		return "Web";
	}

	/**
	 * @see org.eclipse.debug.core.model.IVariable#getReferenceTypeName()
	 */
	public String getReferenceTypeName() throws DebugException {
		return null;
	}

	/**
	 * @see org.eclipse.debug.core.model.IVariable#hasValueChanged()
	 */
	public boolean hasValueChanged() throws DebugException {

		return false;
	}

	/**
	 * @see org.eclipse.debug.core.model.IDebugElement#getModelIdentifier()
	 */
	public String getModelIdentifier() {
		return parent.getModelIdentifier();
	}

	/**
	 * @see org.eclipse.debug.core.model.IDebugElement#getDebugTarget()
	 */
	public IDebugTarget getDebugTarget() {
		return parent.getDebugTarget();
	}

	/**
	 * @see org.eclipse.debug.core.model.IDebugElement#getLaunch()
	 */
	public ILaunch getLaunch() {
		return parent.getLaunch();
	}

	/**
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		return parent.getAdapter(adapter);
	}

	/**
	 * @see org.eclipse.debug.core.model.IValueModification#setValue(java.lang.String)
	 */
	public void setValue(String expression) throws DebugException {
	}

	/**
	 * @see org.eclipse.debug.core.model.IValueModification#setValue(org.eclipse.debug.core.model.IValue)
	 */
	public void setValue(IValue value) throws DebugException {
	}

	/**
	 * @see org.eclipse.debug.core.model.IValueModification#supportsValueModification()
	 */
	public boolean supportsValueModification() {
		return false;
	}

	/**
	 * @see org.eclipse.debug.core.model.IValueModification#verifyValue(java.lang.String)
	 */
	public boolean verifyValue(String expression) throws DebugException {
		return false;
	}

	/**
	 * @see org.eclipse.debug.core.model.IValueModification#verifyValue(org.eclipse.debug.core.model.IValue)
	 */
	public boolean verifyValue(IValue value) throws DebugException {
		return false;
	}
}

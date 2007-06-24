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
import java.util.List;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.jdt.debug.core.IJavaModifiers;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.eclipse.jdt.internal.debug.core.model.JDINullValue;
import org.jboss.tools.jst.web.debug.ui.internal.views.properties.xpl.WebDataProperties;
import org.jboss.tools.jst.web.debug.ui.xpl.WebDebugUIPlugin;

/**
 * @author Jeremy
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ValueProxy implements IValue {
	static IVariable[] EMPTY_VARIABLES = new IVariable[0];
	protected StackFrameWrapper fStackFrameWrapper;
	protected IValue fOrigin;
	protected IVariable[] fVariables = null;
	protected WebDataProperties wdp;

	ValueProxy(StackFrameWrapper frameWrapper, IValue origin) {
		fStackFrameWrapper = frameWrapper;
		wdp = new WebDataProperties(WebDebugUIPlugin.getDefault().getPreferenceStore());
		setOrigin(origin);
	}
	
	protected void computeVariables() {
		fVariables = EMPTY_VARIABLES;
		try {
			if (fOrigin == null) return;
			if (!fOrigin.hasVariables()) return;
			if (TypeSupport.isSimpleTypeOrWrapper(((IJavaValue)fOrigin).getJavaType())) return;
			
			IVariable[] vars =  fOrigin.getVariables();
			List list = new ArrayList();
			for (int i = vars.length - 1 ; i >= 0; i-- ) {
				if(isStaticVariable(vars[i])) continue;
				IValue value = vars[i].getValue();
				if (value != null && !(value instanceof JDINullValue)) {
					IVariable variable = VariableProxyFactory.createVariable(fStackFrameWrapper, vars[i]);
					list.add(variable);
				}
			}
			if (list.size() > 0) {
				fVariables = (IVariable[])list.toArray(new IVariable[list.size()]);
			}
		} catch (Exception e) {
		}
	}
	
	public static boolean isStaticVariable(IVariable variable) {
		if(variable instanceof VariableProxy) {
			variable = ((VariableProxy)variable).getOrigin();
		}
		if(!(variable instanceof IJavaModifiers)) {
			return false;
		} 
		try {
			IJavaModifiers jm = ((IJavaModifiers)variable);
			return jm.isStatic();
		} catch (Exception e) {
			return false;
		}
	}
	
	public void setOrigin(IValue origin) {
		this.fOrigin = origin;
		fVariables = null;
	}
	
	public IValue getOrigin() {
		return this.fOrigin;	
	}
	
	public IStackFrame getStackFrame() {
		return fStackFrameWrapper.getStackFrame();
	}
	
	/**
	 * @see org.eclipse.debug.core.model.IValue#getReferenceTypeName()
	 */
	public String getReferenceTypeName() throws DebugException {
		return (fOrigin == null ? null : fOrigin.getReferenceTypeName());
	}

	/**
	 * @see org.eclipse.debug.core.model.IValue#getValueString()
	 */
	public String getValueString() throws DebugException {
		if (fOrigin == null) return null;
		if (TypeSupport.isSimpleTypeOrWrapper(((IJavaValue)fOrigin).getJavaType())) {
			return fOrigin.getValueString();
		}
		return fOrigin.getReferenceTypeName();
	}

	/**
	 * @see org.eclipse.debug.core.model.IValue#isAllocated()
	 */
	public boolean isAllocated() throws DebugException {
		return (fOrigin == null ? false : fOrigin.isAllocated());
	}

	/**
	 * @see org.eclipse.debug.core.model.IValue#getVariables()
	 */
	public IVariable[] getVariables() throws DebugException {
		if(fVariables == null) {
			computeVariables();
		}
		return fVariables;
	}
	
	/**
	 * @see org.eclipse.debug.core.model.IValue#hasVariables()
	 */
	public boolean hasVariables() throws DebugException {
		try {
			if (!TypeSupport.isSimpleTypeOrWrapper(((IJavaValue)fOrigin).getJavaType())) {
				return fOrigin.hasVariables();
			}
		} catch (Exception e) { }
		return false;
	}

	/**
	 * @see org.eclipse.debug.core.model.IDebugElement#getModelIdentifier()
	 */
	public String getModelIdentifier() {
		return (fOrigin == null ? null : fOrigin.getModelIdentifier());
	}

	/**
	 * @see org.eclipse.debug.core.model.IDebugElement#getDebugTarget()
	 */
	public IDebugTarget getDebugTarget() {
		return (fOrigin == null ? null : fOrigin.getDebugTarget());
	}

	/**
	 * @see org.eclipse.debug.core.model.IDebugElement#getLaunch()
	 */
	public ILaunch getLaunch() {
		return (fOrigin == null ? null : fOrigin.getLaunch());
	}

	/**
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		return (fOrigin == null ? null : fOrigin.getAdapter(adapter));
	}

	public String toString() {
		return (fOrigin == null ? null : fOrigin.toString());
	}
}

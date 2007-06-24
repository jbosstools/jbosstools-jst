/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc.
 *     Red Hat, Inc. 
 *******************************************************************************/
package org.jboss.tools.jst.web.debug.ui.internal.views.xpl;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;
import org.jboss.tools.jst.web.debug.xpl.EvaluationSupport;

/**
 * @author Jeremy
 */
public class ActionForwardValueProxy extends ValueProxy {
	
	/**
	 * @param origin
	 */
	ActionForwardValueProxy(StackFrameWrapper frameWrapper, IValue origin) {
		super(frameWrapper, origin);
	}
	
	protected void computeVariables() {
		if (fOrigin == null) return;
		
		IVariable[] vars = null;
		fVariables = EMPTY_VARIABLES;
		try {
			if (!fOrigin.hasVariables()) return;
			vars = fOrigin.getVariables(); 
			IVariable name = EvaluationSupport.findVariableForReferenceTypeName(
								vars, "java.lang.String", "name");
			IVariable path = EvaluationSupport.findVariableForReferenceTypeName(
								vars, "java.lang.String", "path");
			IVariable redirect = EvaluationSupport.findVariableForReferenceTypeName(
								vars, "boolean", "redirect");
			IVariable contextRelative = EvaluationSupport.findVariableForReferenceTypeName(
								vars, "boolean", "contextRelative");
			fVariables = new IVariable[4];
			fVariables[0] = VariableProxyFactory.createVariable(fStackFrameWrapper, name, VariableProxy.class);
			fVariables[1] = VariableProxyFactory.createVariable(fStackFrameWrapper, path, VariableProxy.class);
			fVariables[2] = VariableProxyFactory.createVariable(fStackFrameWrapper, redirect, VariableProxy.class);
			fVariables[3] = VariableProxyFactory.createVariable(fStackFrameWrapper, contextRelative, VariableProxy.class);
		} catch (Exception e) {
		}
	}

	/**
	 * @see org.eclipse.debug.core.model.IValue#getValueString()
	 */
	public String getValueString() throws DebugException {
		if (fOrigin == null) return null;
		StringBuffer text = new StringBuffer(getReferenceTypeName());
		return text.toString();
	}

	/**
	 * @see org.eclipse.debug.core.model.IValue#hasVariables()
	 */
	public boolean hasVariables() throws DebugException {
		return (fVariables == null || fVariables.length > 0);
	}

}

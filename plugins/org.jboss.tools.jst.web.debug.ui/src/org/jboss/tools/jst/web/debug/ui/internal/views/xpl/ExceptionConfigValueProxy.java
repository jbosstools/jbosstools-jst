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
public class ExceptionConfigValueProxy extends ValueProxy {
	/**
	 * @param origin
	 */
	ExceptionConfigValueProxy(StackFrameWrapper frameWrapper, IValue origin) {
		super(frameWrapper, origin);
	}
	
	protected void computeVariables() {
		fVariables = EMPTY_VARIABLES;
		if (fOrigin == null) return;
		
		IVariable[] vars = null;
		try {
			if (!fOrigin.hasVariables()) return;
			vars = fOrigin.getVariables(); 
			IVariable type = EvaluationSupport.findVariableForReferenceTypeName(
								vars, "java.lang.String", "type");
			IVariable key = EvaluationSupport.findVariableForReferenceTypeName(
								vars, "java.lang.String", "key");
			IVariable path = EvaluationSupport.findVariableForReferenceTypeName(
								vars, "java.lang.String", "path");
			IVariable scope = EvaluationSupport.findVariableForReferenceTypeName(
								vars, "java.lang.String", "scope");
			fVariables = new IVariable[4];
			fVariables[0] = new VariableProxy(fStackFrameWrapper, type);
			fVariables[1] = new VariableProxy(fStackFrameWrapper, key);
			fVariables[2] = new VariableProxy(fStackFrameWrapper, path);
			fVariables[3] = new VariableProxy(fStackFrameWrapper, scope);
		} catch (Exception e) {
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IValue#getValueString()
	 */
	public String getValueString() throws DebugException {
		if (fOrigin == null) return null;
		StringBuffer text = new StringBuffer(getReferenceTypeName());
/*		try {
			String name = fVariables[0].getValue().getValueString();
			String path = fVariables[1].getValue().getValueString();
			String redirect = fVariables[2].getValue().getValueString();
			String contextRelative = fVariables[3].getValue().getValueString();
			text = text.append('<').append(key).append("> : <").append(value).append('>');
		} catch (Exception e) {
		}
*/
		return text.toString();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IValue#hasVariables()
	 */
	public boolean hasVariables() throws DebugException {
		return (fVariables == null || fVariables.length > 0);
	}

}

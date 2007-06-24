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
public class HashMapEntryValueProxy extends ValueProxy {

	/**
	 * @param origin
	 */
	HashMapEntryValueProxy(StackFrameWrapper frameWrapper, IValue origin) {
		super(frameWrapper, origin);
	}
	
	protected void computeVariables() {
//		if (fVariables == null) fVariables = new ArrayList();
		fVariables = EMPTY_VARIABLES;
		try {
			if (fOrigin == null) return;
		
			IVariable[] vars = null;

			if (!fOrigin.hasVariables()) return;
			vars = fOrigin.getVariables(); 
			IVariable key = EvaluationSupport.findVariableForName(vars, "key");
			IVariable value = EvaluationSupport.findVariableForName(vars, "value");
			fVariables = new IVariable[2];	
			fVariables[0] = VariableProxyFactory.createVariable(fStackFrameWrapper, key);
			((VariableProxy)fVariables[0]).setOrigin(key);
			fVariables[1] = VariableProxyFactory.createVariable(fStackFrameWrapper, value);
			((VariableProxy)fVariables[1]).setOrigin(value);
		} catch (Exception e) {
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IValue#getValueString()
	 */
	public String getValueString() throws DebugException {
		if (fOrigin == null) return null;
		StringBuffer text = new StringBuffer();
		try {
			getVariables();
			if(!hasVariables()) return "";
			String key = fVariables[0].getValue().getValueString();
			String value = fVariables[1].getValue().getValueString();
			text = text.append('<').append(key).append("> : <").append(value).append('>');
		} catch (Exception e) {
		}
		return text.toString();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IValue#hasVariables()
	 */
	public boolean hasVariables() throws DebugException {
		return (fVariables == null || fVariables.length > 0);
	}
}

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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;

/**
 * @author Jeremy
 */
public class EnumerationValueProxy extends ValueProxy {
	protected int fSize = -1;

	/**
	 * @param origin
	 */
	EnumerationValueProxy(StackFrameWrapper frameWrapper, IValue origin) {
		super(frameWrapper, origin);
	}

	public void setOrigin(IValue origin) {
		super.setOrigin(origin);
		fSize = -1;
	}
	
	public IVariable[] getVariables() throws DebugException {
		if (fVariables == null) computeVariables();
		return fVariables;
	}

	protected void computeVariables() {
		fVariables = EMPTY_VARIABLES;
		if (fOrigin == null) return;
		IVariable[] vars = null;
		try {
			if (!fOrigin.hasVariables()) return;
			vars = fOrigin.getVariables();
			fSize = (vars == null ? 0 : vars.length);
			List list = new ArrayList();
			
			for (int i = 0; vars != null && i < vars.length; i++) {
				VariableProxy p = VariableProxyFactory.createVariable(fStackFrameWrapper, vars[i], EnumerationEntryVariableProxy.class);
//				VariableProxy p = VariableProxyFactory.createVariable(fStackFrameWrapper, vars[i], VariableProxy.class);
				p.setAlias("[" + i + "]");
				list.add(i, p);
			}
			if (list.size() > 0) {
				fVariables = (IVariable[])list.toArray(new IVariable[list.size()]);
			}
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
			String type = "java.util.Enumeration";
			IVariable[] vars = fOrigin.getVariables();
			int size = (vars == null ? 0 : vars.length);
			text = text.append(type).append('(').append(size).append(')');
		} catch (Exception e) {
		}
		return text.toString();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IValue#hasVariables()
	 */
	public boolean hasVariables() throws DebugException {
		IVariable[] vars = fOrigin.getVariables();
		int size = (vars == null ? 0 : vars.length);
		return (size > 0);
	}
}

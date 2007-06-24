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
import org.eclipse.jdt.internal.debug.core.model.JDINullValue;
import org.jboss.tools.jst.web.debug.xpl.EvaluationSupport;

/**
 * @author Jeremy
 */
public class ArrayListValueProxy extends ValueProxy {
	private int fSize = 0;
	 
	/**
	 * @param origin
	 */
	ArrayListValueProxy(StackFrameWrapper frameWrapper, IValue origin) {
		super(frameWrapper, origin);
	}
	
	protected void computeVariables() {
		fVariables = EMPTY_VARIABLES;
		if (fOrigin == null) return;
		IVariable[] vars = null;
		try {
			if (!fOrigin.hasVariables()) return;
			vars = fOrigin.getVariables(); 
			IVariable sizeVar = EvaluationSupport.findVariableForName(vars, "size");
			try { fSize = Integer.parseInt(sizeVar.getValue().getValueString()); } 
			catch (Exception ex) { fSize = 0; }
			
			IVariable table = EvaluationSupport.findVariableForName(vars, "elementData");

			if (table.getValue().hasVariables() && fSize > 0) {
				IVariable[] tvars = table.getValue().getVariables();
				List list = new ArrayList();
				int count = 0;
				for (int i = fSize - 1  ; i >= 0; i-- ) {
					IValue value = tvars[i].getValue();
					if (value != null && !(value instanceof JDINullValue)) {
						VariableProxy p = VariableProxyFactory.createVariable(fStackFrameWrapper, tvars[i], ArrayElementVariableProxy.class);
						p.setAlias("[" + count + "]");
						list.add(count, p);
						count++;
					}
				}
				if (list.size() > 0) {
					fVariables = (IVariable[])list.toArray(new IVariable[list.size()]);
				}
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
			String type = fOrigin.getReferenceTypeName();
			getVariables();
			if(!hasVariables()) return "";
			int size = (fVariables == null ? 0 : fVariables.length);
			text = text.append(type).append('(').append(size).append(')');
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

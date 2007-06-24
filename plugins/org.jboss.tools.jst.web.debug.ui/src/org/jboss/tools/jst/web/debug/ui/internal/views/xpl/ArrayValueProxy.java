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
import org.eclipse.jdt.debug.core.IJavaVariable;
import org.eclipse.jdt.internal.debug.core.model.JDIValue;

/**
 * @author Jeremy
 */
public class ArrayValueProxy extends ValueProxy {
	
	/**
	 * @param frame
	 * @param origin
	 */
	ArrayValueProxy(StackFrameWrapper frameWrapper, IValue origin) {
		super(frameWrapper, origin);
		computeVariables();
	}

	protected void computeVariables() {
		List variables = new ArrayList();
		fVariables = EMPTY_VARIABLES;
		if (fOrigin == null) return;
		try {
			if (!fOrigin.hasVariables()) return;
			
			IVariable[] vars = fOrigin.getVariables();
			for (int i = 0; vars != null && i < vars.length; i++){
				if (TypeSupport.isArrayType(((IJavaVariable)vars[i]).getJavaType()))
					variables.add(new ArrayPartitionVariableProxy(fStackFrameWrapper, vars[i]));
				else 
					variables.add(new ArrayElementVariableProxy(fStackFrameWrapper, vars[i]));
			}
			fVariables = (IVariable[])variables.toArray(new IVariable[variables.size()]);
		} catch (Exception e) {
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IValue#getVariables()
	 */
	public IVariable[] getVariables() throws DebugException {
		if (fOrigin == null) return null;
		return super.getVariables();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IValue#hasVariables()
	 */
	public boolean hasVariables() throws DebugException {
		return (fVariables == null || fVariables.length > 0);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IValue#getValueString()
	 */
	public String getValueString() throws DebugException {
		if (fOrigin == null) return null;
		StringBuffer text = new StringBuffer();
		try {
			String type = fOrigin.getReferenceTypeName();

			int size = ((JDIValue)fOrigin).getArrayLength();
						
			if (type.indexOf('[') != -1) {
				text = text.append(type.substring(0, type.indexOf('[') + 1));
				text = text.append(size);
				text = text.append(type.substring(type.indexOf(']')));
			}
			
			
//			text = text.append(type).append('(').append(fVariables.size()).append(')');
		} catch (Exception e) {
		}
		return text.toString();
	}

}

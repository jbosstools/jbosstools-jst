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
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.jdt.debug.eval.IEvaluationResult;
import org.eclipse.jdt.internal.debug.core.model.JDINullValue;

/**
 * @author Jeremy
 */
public class ActionVariableProxy extends VariableProxy {

	ActionVariableProxy(StackFrameWrapper frameWrapper, IVariable origin) {
		super(frameWrapper, origin);
	}

	ActionVariableProxy(StackFrameWrapper frameWrapper, IVariable origin, String alias) {
		super(frameWrapper, origin, alias);
	}

	ActionVariableProxy(StackFrameWrapper frameWrapper, IVariable origin, String alias, String type) {
		super(frameWrapper, origin, alias, type);
	}

	ActionVariableProxy(StackFrameWrapper frameWrapper, IEvaluationResult result, String alias, String type) {
		super(frameWrapper, result, alias, type);
	}
	
	private void initValue (IValue value) {
		fValue = (value == null || value instanceof JDINullValue ? null 
			: ValueProxyFactory.createValueProxy(fStackFrameWrapper, value, ActionValueProxy.class));		
	}

	
	/**
	 * @see org.eclipse.debug.core.model.IVariable#getValue()
	 */
	public IValue getValue() throws DebugException {
		IStackFrame thisFrame = getStackFrame();
		if(thisFrame == null || thisFrame.isTerminated()) return null;

		if (fValue == null || 
				hasValueChanged() ||
				getChangeCount() != fStackFrameWrapper.getChangeCount()) {
			initValue((fOrigin == null? null : fOrigin.getValue()));
		}
		fHasValueChanged = false;
		setChangeCount(fStackFrameWrapper.getChangeCount());
 		return fValue;
	}

}

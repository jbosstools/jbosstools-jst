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
public class EnumerationVariableProxy extends VariableProxy {
	
	/**
	 * @param origin
	 */
	EnumerationVariableProxy(StackFrameWrapper frameWrapper, IVariable origin, String alias, String type) {
		super(frameWrapper, origin, alias, type);
	}

	private void initValue (IValue value) {
		fValue = (value == null || value instanceof JDINullValue ? null 
				: ValueProxyFactory.createValueProxy(fStackFrameWrapper, value, EnumerationValueProxy.class));		
		}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IVariable#getValue()
	 */
	public IValue getValue() throws DebugException {
		IStackFrame thisFrame = getStackFrame();
		if(thisFrame == null || thisFrame.isTerminated()) return null;
		if (fValue == null || 
				hasValueChanged() ||
				getChangeCount() != fStackFrameWrapper.getChangeCount()) {
			Object resultValue = evaluateEnumerationVariable();
			if (resultValue instanceof IVariable) {
				initValue(((IVariable)resultValue).getValue());
			} else {
				if (resultValue instanceof IEvaluationResult){
					initValue(((IEvaluationResult)resultValue).getValue());
				} else {
					return null;
				}
			}
		}
		fHasValueChanged = false;
		setChangeCount(fStackFrameWrapper.getChangeCount());
		return fValue;
	}

	protected IEvaluationResult evaluateEnumerationVariable() {
		return null;
	}


}

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

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.jdt.debug.eval.IEvaluationResult;
import org.jboss.tools.jst.web.debug.xpl.EvaluationSupport;


/**
 * @author Jeremy
 *
 */
public class EvaluatedVariable implements IVariable {
	private StackFrameWrapper fStackFrameWrapper;
	private String fReferencedTypeName;
	private String fName;
	private String fExpression;
	private IEvaluationResult fResult;
	
	private IValue fValue;

	/**
	 * 
	 * @param frameWrapper
	 * @param referenceTypeName
	 * @param name
	 * @param expression
	 */
	public EvaluatedVariable (StackFrameWrapper frameWrapper, String referenceTypeName, String name, String expression) {
		this.fStackFrameWrapper = frameWrapper;
		this.fReferencedTypeName = referenceTypeName;
		this.fName = name;
		this.fExpression = expression;
		doEvaluate();
	}

	/**
	 * 
	 *
	 */
	public void doEvaluate() {
		try {
			fResult = EvaluationSupport.evaluateExpression(
										fStackFrameWrapper.getStackFrame(), 
										fExpression);
			if (fResult != null && !fResult.hasErrors() && fResult.getValue().isAllocated()) {
				fValue = ValueProxyFactory.createValueProxy(fStackFrameWrapper, fResult.getValue());
			} 
			
		} catch (Exception e) {
			fResult = null;
			fValue = null;
		}
	}

	/**
	 * 
	 * @return
	 */
	public IEvaluationResult getResult() {
		return fResult;
	}
	
	/**
	 * @see org.eclipse.debug.core.model.IVariable#getValue()
	 */
	public IValue getValue() throws DebugException {
		return fValue;
	}

	/**
	 * @see org.eclipse.debug.core.model.IVariable#getName()
	 */
	public String getName() throws DebugException {
		return this.fName;
	}

	/**
	 * @see org.eclipse.debug.core.model.IVariable#getReferenceTypeName()
	 */
	public String getReferenceTypeName() throws DebugException {
		return this.fReferencedTypeName;
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
		return fStackFrameWrapper.getModelIdentifier();
	}

	/**
	 * @see org.eclipse.debug.core.model.IDebugElement#getDebugTarget()
	 */
	public IDebugTarget getDebugTarget() {
		return fStackFrameWrapper.getDebugTarget();
	}

	/**
	 * @see org.eclipse.debug.core.model.IDebugElement#getLaunch()
	 */
	public ILaunch getLaunch() {
		return fStackFrameWrapper.getLaunch();
	}

	/**
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		return fStackFrameWrapper.getAdapter(adapter);
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

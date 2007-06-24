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
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.jdt.debug.eval.IEvaluationResult;
import org.eclipse.jdt.internal.debug.core.model.JDINullValue;
import org.jboss.tools.jst.web.debug.ui.internal.views.properties.xpl.WebDataProperties;
import org.jboss.tools.jst.web.debug.ui.xpl.WebDebugUIPlugin;

/**
 * @author Jeremy
 */
public class VariableProxy implements IVariable {
	protected StackFrameWrapper fStackFrameWrapper;

	protected IVariable fOrigin;

	protected IEvaluationResult fResult;

	protected IValue fValue;

	private WebDataProperties wdp;

	private long fChangeCount;

	protected boolean fHasValueChanged = false;

	protected String fAlias = null;

	protected String fType = null;

	VariableProxy(StackFrameWrapper frameWrapper, IVariable origin) {
		this.fStackFrameWrapper = frameWrapper;
		this.fOrigin = origin;
		this.fChangeCount = (frameWrapper == null ? 0 : frameWrapper
				.getChangeCount());
		wdp = new WebDataProperties(WebDebugUIPlugin.getDefault()
				.getPreferenceStore());
	}

	VariableProxy(StackFrameWrapper frameWrapper, IVariable origin, String alias) {
		this(frameWrapper, origin);
		setAlias(alias);
	}

	VariableProxy(StackFrameWrapper frameWrapper, IVariable origin,
			String alias, String type) {
		this(frameWrapper, origin, alias);
		this.fType = type;
	}

	VariableProxy(StackFrameWrapper frameWrapper, IEvaluationResult result,
			String alias, String type) {
		this.fStackFrameWrapper = frameWrapper;
		this.fResult = result;
		this.fChangeCount = (frameWrapper == null ? 0 : frameWrapper
				.getChangeCount());
		setAlias(alias);
		this.fType = type;
		wdp = new WebDataProperties(WebDebugUIPlugin.getDefault()
				.getPreferenceStore());
	}

	public void setAlias(String alias) {
		fAlias = alias;
	}

	public IVariable getOrigin() {
		return fOrigin;
	}

	public void setOrigin(IVariable origin) {
		if (fOrigin == origin)
			return;
		if (fOrigin != null && fOrigin.equals(origin))
			return;
		fOrigin = origin;
		fHasValueChanged = true;

	}

	long getChangeCount() {
		return fChangeCount;
	}

	void setChangeCount(long count) {
		fChangeCount = count;
	}

	void setResult(IEvaluationResult value) {
		if (fResult == value)
			return;
		if (fResult != null && fResult.equals(value))
			return;
		setOrigin(null);
		fResult = value;
		fHasValueChanged = true;
	}

	public IStackFrame getStackFrame() {
		return fStackFrameWrapper.getStackFrame();
	}

	/**
	 * @see org.eclipse.debug.core.model.IVariable#getValue()
	 */
	public IValue getValue() throws DebugException {
		try {
			IValue value = null;
			if (fOrigin != null) {
				if (fValue == null || hasValueChanged()
						|| fChangeCount != fStackFrameWrapper.getChangeCount()) {
					value = getOriginValue();
					fValue = (value == null ? null : ValueProxyFactory
							.createValueProxy(fStackFrameWrapper, value,
									getReferenceTypeName()));
				}
			} else if (fValue == null || hasValueChanged()
					|| fChangeCount != fStackFrameWrapper.getChangeCount()) {
				value = getResultValue();
				fValue = (value == null ? null : ValueProxyFactory
						.createValueProxy(fStackFrameWrapper, value,
								getReferenceTypeName()));
			}
		} catch (Exception e) {
		}
		fHasValueChanged = false;
		setChangeCount(fStackFrameWrapper.getChangeCount());
		return fValue;
	}

	protected IValue getResultValue() {
		try {
			return fResult.getValue();
		} catch (Exception e) {
		}
		return null;
	}

	protected IValue getOriginValue() {
		try {
			return fOrigin.getValue();
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * @see org.eclipse.debug.core.model.IVariable#getName()
	 */
	public String getName() throws DebugException {
		if (fOrigin != null)
			return (fAlias == null ? fOrigin.getName() : fAlias);
		return fAlias;
	}

	/**
	 * @see org.eclipse.debug.core.model.IVariable#getReferenceTypeName()
	 */
	public String getReferenceTypeName() throws DebugException {
		if (fOrigin != null)
			return (fType == null ? fOrigin.getReferenceTypeName() : fType);
		return fType;
	}

	/**
	 * @see org.eclipse.debug.core.model.IVariable#hasValueChanged()
	 */
	public boolean hasValueChanged() throws DebugException {
		if (fHasValueChanged)
			return true;
		if (fOrigin != null)
			return fOrigin.hasValueChanged();
		return false;
	}

	/**
	 * @see org.eclipse.debug.core.model.IDebugElement#getModelIdentifier()
	 */
	public String getModelIdentifier() {
		if (fOrigin != null)
			return fOrigin.getModelIdentifier();
		return null;
	}

	/**
	 * @see org.eclipse.debug.core.model.IDebugElement#getDebugTarget()
	 */
	public IDebugTarget getDebugTarget() {
		if (fOrigin != null)
			return fOrigin.getDebugTarget();
		return null;
	}

	/**
	 * @see org.eclipse.debug.core.model.IDebugElement#getLaunch()
	 */
	public ILaunch getLaunch() {
		if (fOrigin != null)
			return fOrigin.getLaunch();
		return null;
	}

	/**
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		if (fOrigin != null)
			return fOrigin.getAdapter(adapter);
		return null;
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

	static IVariable substByInternalVariable(IVariable variable, String name,
			String type) {
		try {
			IValue value = variable.getValue();
			if (value == null || (value instanceof JDINullValue))
				return variable;
			if (type.equals(value.getReferenceTypeName()))
				return variable;

			IVariable[] vars = value.getVariables();
			for (int i = 0; vars != null && i < vars.length; i++) {
				IVariable v = vars[i];
				if (name.equals(v.getName())
						&& type.equals(v.getReferenceTypeName()))
					return v;
			}
		} catch (Exception e) {
		}
		return variable;
	}

}

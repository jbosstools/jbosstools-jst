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

import java.util.List;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.jdt.debug.eval.IEvaluationResult;
import org.eclipse.jdt.internal.debug.core.model.JDINullValue;
import org.eclipse.jdt.internal.debug.core.model.JDIThread;
import org.jboss.tools.jst.web.debug.xpl.EvaluationSupport;

/**
 * @author Jeremy
 */
public class HttpServletRequestVariableProxy extends VariableProxy {
				  
	HttpServletRequestVariableProxy(StackFrameWrapper frameWrapper, IVariable origin) {
		super(frameWrapper, origin);
		IValue value = null;
		try { value = origin.getValue(); } catch (Exception e) { }
		initValue(value);
	}

	HttpServletRequestVariableProxy(StackFrameWrapper frameWrapper, IVariable origin, String alias) {
		super(frameWrapper, origin, alias);
		IValue value = null;
		try { value = origin.getValue(); } catch (Exception e) { }
		initValue(value);
	}

	HttpServletRequestVariableProxy(StackFrameWrapper frameWrapper, IVariable origin, String alias, String type) {
		super(frameWrapper, origin, alias, type);
		IValue value = null;
		try { value = origin.getValue(); } catch (Exception e) { }
		initValue(value);
	}

	HttpServletRequestVariableProxy(StackFrameWrapper frameWrapper, IEvaluationResult result, String alias, String type) {
		super(frameWrapper, result, alias, type);
		IValue value = null;
		if (result != null && !result.hasErrors()) {
			try { value = result.getValue(); } catch (Exception e) { }
		}
		initValue(value);
	}

	private void initValue (IValue value) {
		fValue = (value == null || value instanceof JDINullValue ? null 
			: ValueProxyFactory.createValueProxy(fStackFrameWrapper, value, HttpServletRequestValueProxy.class));		
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
			Object resultValue = getRequestVariable(fStackFrameWrapper);
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

	static IVariable getRequestVariable(StackFrameWrapper stackFrameWrapper) {
		IStackFrame thisFrame = stackFrameWrapper.getStackFrame();
		if(thisFrame == null || thisFrame.isTerminated()) return null;
		IVariable[] stackVars = null;
		try { stackVars = stackFrameWrapper.getFrameVariables(); }
		catch (Exception e) {}
		
		IVariable variable = EvaluationSupport.findVariableForName(stackVars, "request");
		if (variable != null) {
			return substByInternalVariable(variable, "request", "javax.servlet.ServletRequest");
		}
		
		JDIThread thread = (JDIThread)thisFrame.getThread();
		List frames = null;
		try { frames = thread.computeNewStackFrames(); } 
		catch (Exception x) { return null; }
		
		for (int i = 0; frames != null && i < frames.size(); i++) {
			IStackFrame currentFrame = (IStackFrame)frames.get(i);
			if (!thisFrame.equals(currentFrame)) {
				try { stackVars = stackFrameWrapper.getFrameVariables(); }
				catch (Exception e) {}
				variable = EvaluationSupport.findVariableForName(stackVars, "request");
				if (variable != null) return substByInternalVariable(variable, "request", "javax.servlet.ServletRequest");
			}
		}
		return null;
	}

}
/*
class HttpServletRequestValueProxy extends ValueProxy {
	
	HttpServletRequestValueProxy(StackFrameWrapper frameWrapper, IValue origin) {
		super(frameWrapper, origin);
	}
	
	protected void computeVariables() {
		fVariables = EMPTY_VARIABLES;
		if (fOrigin == null) return;
		
		IVariable[] vars = null;
		try {
			if (!fOrigin.hasVariables()) return;
			vars = fOrigin.getVariables();
			if (vars == null || vars.length == 0) return;
			List list = new ArrayList();
			for (int i = 0; vars != null && i < vars.length; i++) {
				if(isStaticVariable(vars[i])) continue;
				String name = vars[i].getName();
				if("inputStream".equals(name) || "reader".equals(name)) {
					continue;
				}
				list.add(VariableProxyFactory.createVariable(fStackFrameWrapper, vars[i]));
			}
			if(list.size() > 0) {
				fVariables = (IVariable[])list.toArray(new IVariable[list.size()]);
			}
		} catch (Exception e) {
		}
	}


	public String getValueString() throws DebugException {
		if (fOrigin == null) return null;
		StringBuffer text = new StringBuffer(getReferenceTypeName());
		return text.toString();
	}

	public boolean hasVariables() throws DebugException {
		return (fVariables == null || fVariables.length > 0);
	}

}
*/

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
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.eclipse.jdt.debug.eval.IEvaluationResult;
import org.eclipse.jdt.internal.debug.core.model.JDINullValue;
import org.eclipse.jdt.internal.debug.core.model.JDIThread;
import org.jboss.tools.jst.web.debug.ui.internal.views.properties.xpl.WebDataProperties;
import org.jboss.tools.jst.web.debug.xpl.EvaluationSupport;

/**
 * @author Jeremy
 */
public class HttpServletResponseVariableProxy extends VariableProxy {

	HttpServletResponseVariableProxy(StackFrameWrapper frameWrapper, IVariable origin) {
		super(frameWrapper, origin);
		IValue value = null;
		try { value = origin.getValue(); } catch (Exception e) { }
		initValue(value);
	}

	HttpServletResponseVariableProxy(StackFrameWrapper frameWrapper, IVariable origin, String alias) {
		super(frameWrapper, origin, alias);
		IValue value = null;
		try { value = origin.getValue(); } catch (Exception e) { }
		initValue(value);
	}

	HttpServletResponseVariableProxy(StackFrameWrapper frameWrapper, IVariable origin, String alias, String type) {
		super(frameWrapper, origin, alias, type);
		IValue value = null;
		try { value = origin.getValue(); } catch (Exception e) { }
		initValue(value);
	}

	HttpServletResponseVariableProxy(StackFrameWrapper frameWrapper, IEvaluationResult result, String alias, String type) {
		super(frameWrapper, result, alias, type);
		IValue value = null;
		if (result != null && !result.hasErrors()) {
			try { value = result.getValue(); } catch (Exception e) { }
		}
		initValue(value);
	}

	private void initValue (IValue value) {
		fValue = (value == null || value instanceof JDINullValue ? null 
			: ValueProxyFactory.createValueProxy(fStackFrameWrapper, value, HttpServletResponseValueProxy.class));		
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
			Object resultValue = getResponseVariable();
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

	private Object getResponseVariable() {
		IStackFrame thisFrame = getStackFrame();
		if(thisFrame == null || thisFrame.isTerminated()) return null;
		IVariable[] stackVars = null;
		try { stackVars = fStackFrameWrapper.getFrameVariables(); }
		catch (Exception e) {  }
		
		IVariable variable = EvaluationSupport.findVariableForName(stackVars, "response");
		if (variable != null) {
			return substByInternalVariable(variable, "response", "javax.servlet.ServletResponse");
		}

		JDIThread thread = (JDIThread)thisFrame.getThread();
		List frames = null;
		try { frames = thread.computeNewStackFrames(); } 
		catch (Exception x) { return null; }
		
		for (int i = 0; frames != null && i < frames.size(); i++) {
			IStackFrame currentFrame = (IStackFrame)frames.get(i);
			if (!thisFrame.equals(currentFrame)) {
				try { stackVars = fStackFrameWrapper.getFrameVariables(); }
				catch (Exception e) { }
				variable = EvaluationSupport.findVariableForName(stackVars, "response");
				if (variable != null) return substByInternalVariable(variable, "response", "javax.servlet.ServletResponse");
			}
		}
		return null;
	}

}

class HttpServletResponseValueProxy extends ValueProxy {
	
	HttpServletResponseValueProxy(StackFrameWrapper frameWrapper, IValue origin) {
		super(frameWrapper, origin);
	}
	

	public String getValueString() throws DebugException {
		if (fOrigin == null) return null;
		StringBuffer text = new StringBuffer(getReferenceTypeName());
		return text.toString();
	}

	protected void computeVariables() {
		fVariables = EMPTY_VARIABLES;
		try {
			if (fOrigin == null) return;
			if (!fOrigin.hasVariables()) return;
			if (TypeSupport.isSimpleTypeOrWrapper(((IJavaValue)fOrigin).getJavaType())) return;
			
			FilteredVariablesEnumeration filtered = 
				new FilteredVariablesEnumeration(
						fOrigin, 
						WebDataProperties.SHOW_WEB_HTTPSERVLETRESPONSE_FILTER,
						WebDataProperties.SHOW_WEB_HTTPSERVLETRESPONSE_FILTER + WebDataProperties.VALUE_POSTFIX, 
						"outputStream writer");
			
			
			List list = new ArrayList();
			while (filtered.hasMoreElements()) {
				IVariable var = (IVariable)filtered.nextElement();
				if(isStaticVariable(var)) continue;
				IValue value = var.getValue();
				if (value != null && !(value instanceof JDINullValue)) {
					IVariable variable = VariableProxyFactory.createVariable(fStackFrameWrapper, var);
					list.add(variable);
				}
			}
			if (list.size() > 0) {
				fVariables = (IVariable[])list.toArray(new IVariable[list.size()]);
			}
		} catch (Exception e) {
		}
	}
}

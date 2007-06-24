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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.eclipse.jdt.debug.eval.IEvaluationResult;
import org.eclipse.jdt.internal.debug.core.model.JDINullValue;
import org.jboss.tools.jst.web.debug.ui.internal.views.properties.xpl.WebDataProperties;
import org.jboss.tools.jst.web.debug.xpl.EvaluationSupport;

/**
 * @author Jeremy
 *
 */
public class ServletContextVariableProxy extends VariableProxy {


	ServletContextVariableProxy(StackFrameWrapper frameWrapper, IVariable origin) {
		super(frameWrapper, origin);
		IValue value = null;
		try { value = origin.getValue(); } catch (Exception e) { }
		initValue(value);
	}

	ServletContextVariableProxy(StackFrameWrapper frameWrapper, IVariable origin, String alias) {
		super(frameWrapper, origin, alias);
		IValue value = null;
		try { value = origin.getValue(); } catch (Exception e) { }
		initValue(value);
	}

	ServletContextVariableProxy(StackFrameWrapper frameWrapper, IVariable origin, String alias, String type) {
		super(frameWrapper, origin, alias, type);
		IValue value = null;
		try { value = origin.getValue(); } catch (Exception e) { }
		initValue(value);
	}

	ServletContextVariableProxy(StackFrameWrapper frameWrapper, IEvaluationResult result, String alias, String type) {
		super(frameWrapper, result, alias, type);
		IValue value = null;
		if (result != null && !result.hasErrors()) {
			try { value = result.getValue(); } catch (Exception e) { }
		}
		initValue(value);
	}

	private void initValue (IValue value) {
		fValue = (value == null || value instanceof JDINullValue ? null : new ServletContextValueProxy(fStackFrameWrapper, value));		
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
			Object resultValue = evaluateServletContextVariable();
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

	private IEvaluationResult evaluateServletContextVariable() {
		try {
			IEvaluationResult result = EvaluationSupport.evaluateExpression(
										getStackFrame(),
										"request.getSession(false).getServletContext()");
			if (result == null || result.hasErrors()) return null;
			return result;
		} catch (Exception x) {
		}
		return null;
	}
}

class ServletContextValueProxy extends ValueProxy {
	private Map fVariablesM = new HashMap();
	private static String[] PROPERTY_NAMES = new String[] {
		WebDataProperties.SHOW_WEB_SERVLETCONTEXT_ATTRIBUTES,
		WebDataProperties.SHOW_WEB_SERVLETCONTEXT_INITPARAMETERS
	};

	/**
	 * @param origin
	 */
	ServletContextValueProxy(StackFrameWrapper frameWrapper, IValue origin) {
		super(frameWrapper, origin);
	}

	protected void computeVariables() {
		fVariables = EMPTY_VARIABLES;
		try {
			if (fOrigin == null) return;
			if (!fOrigin.hasVariables()) return;
			if (TypeSupport.isSimpleTypeOrWrapper(((IJavaValue)fOrigin).getJavaType())) return;
			
			IVariable[] vars =  fOrigin.getVariables();
			List list = new ArrayList();
			// Add standard vars
			for (int i = 0; i < PROPERTY_NAMES.length; i++) {
				String name = PROPERTY_NAMES[i];
				if (!wdp.isEnabledFilter(name)) continue;

				if (WebDataProperties.SHOW_WEB_SERVLETCONTEXT_ATTRIBUTES.equals(name)) {
					ServletContextAttributesVariableProxy variable = new ServletContextAttributesVariableProxy(fStackFrameWrapper, null);
					list.add(variable);
				} else if (WebDataProperties.SHOW_WEB_SERVLETCONTEXT_INITPARAMETERS.equals(name)) {
					ServletContextInitParametersVariableProxy variable = new ServletContextInitParametersVariableProxy(fStackFrameWrapper, null);
					list.add(variable);
				} 
			}

			String stopWords = "";
			for (int i = 0; list != null && i < list.size(); i++) {
				try {
					IVariable var = (IVariable)list.get(i);
					stopWords += " " + var.getName();
				} catch (Exception x) {}
			}
			
			FilteredVariablesEnumeration filtered = 
				new FilteredVariablesEnumeration(
						fOrigin, 
						WebDataProperties.SHOW_WEB_SERVLETCONTEXT_FILTER,
						WebDataProperties.SHOW_WEB_SERVLETCONTEXT_FILTER + WebDataProperties.VALUE_POSTFIX, 
						stopWords);
			
			
			while (filtered.hasMoreElements()) {
				IVariable var = (IVariable)filtered.nextElement();
				if(isStaticVariable(var)) continue;
				IValue value = var.getValue();
				if (value != null && !(value instanceof JDINullValue)) {
					IVariable variable = VariableProxyFactory.createVariable(fStackFrameWrapper, var);
					list.add(variable);
				}
			}

			synchronized (this) {
				clearCurrentVariables();
				
				fVariables = new IVariable[list.size()];
				for (int i = 0; i < list.size(); i++) {
					fVariables[i] = (IVariable)list.get(i);
				}
			}
			
		} catch (Exception e) {
		}
	}

	private void clearCurrentVariables() {
		for (int i = 0; fVariables != null && i < fVariables.length; i++) {
			fVariables[i] = null;
		}
		fVariables = null;
	}


	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IValue#getValueString()
	 */
	public String getValueString() throws DebugException {
		if (fOrigin == null) return null;
		StringBuffer text = new StringBuffer(getReferenceTypeName());

		return text.toString();
	}

}


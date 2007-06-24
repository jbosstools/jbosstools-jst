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

import org.eclipse.debug.core.model.IVariable;
import org.eclipse.jdt.debug.eval.IEvaluationResult;
import org.jboss.tools.jst.web.debug.xpl.EvaluationSupport;

/**
 * @author Jeremy
 */
public class HttpServletRequestParametersVariableProxy extends EnumerationVariableProxy {

	/**
	 * @param frameWrapper
	 * @param origin
	 * @param alias
	 */
	HttpServletRequestParametersVariableProxy(StackFrameWrapper frameWrapper, IVariable origin) {
		super(frameWrapper, origin, "parameters", "java.util.Enumeration");
	}

	protected IEvaluationResult evaluateEnumerationVariable() {
		try {
			IEvaluationResult result = EvaluationSupport.evaluateExpression(
										fStackFrameWrapper.getStackFrame(),
										"java.util.ArrayList HttpServletRequestParametersVariableProxy_list=new java.util.ArrayList();" +
										"java.util.Enumeration HttpServletRequestParametersVariableProxy_e=request.getParameterNames();" +
										"for(;HttpServletRequestParametersVariableProxy_e!=null&&HttpServletRequestParametersVariableProxy_e.hasMoreElements();){" +
										"java.lang.String HttpServletRequestParametersVariableProxy_name=(java.lang.String)HttpServletRequestParametersVariableProxy_e.nextElement();" +
										"java.util.Hashtable HttpServletRequestParametersVariableProxy_entry = new java.util.Hashtable(1, 1.0f);" +
										"HttpServletRequestParametersVariableProxy_entry.put(HttpServletRequestParametersVariableProxy_name,request.getParameterValues(HttpServletRequestParametersVariableProxy_name));" +
										"HttpServletRequestParametersVariableProxy_list.add(HttpServletRequestParametersVariableProxy_entry);}" +
										"return (java.util.Hashtable[])HttpServletRequestParametersVariableProxy_list.toArray(new java.util.Hashtable[HttpServletRequestParametersVariableProxy_list.size()]);");
			if (result == null || result.hasErrors()) {
				return null;
			}
			return result;
		} catch (Exception x) {
		}
		return null;
	}

}

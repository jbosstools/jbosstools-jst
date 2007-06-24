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
public class HttpServletRequestAttributesVariableProxy extends EnumerationVariableProxy {
	/**
	 * @param origin
	 */
	HttpServletRequestAttributesVariableProxy(StackFrameWrapper frameWrapper, IVariable origin) {
		super(frameWrapper, origin, "attributes", "java.util.Enumeration");
	}

	protected IEvaluationResult evaluateEnumerationVariable() {
		try {
//			IEvaluationResult result = EvaluationSupport.evaluateExpression(
//					fStackFrameWrapper.getStackFrame(),
//					"request.getHeaderNames();");
			IEvaluationResult result = EvaluationSupport.evaluateExpression(
										fStackFrameWrapper.getStackFrame(),
										"java.util.ArrayList HttpServletRequestAttributesVariableProxy_list=new java.util.ArrayList();" +
										"java.util.Enumeration HttpServletRequestAttributesVariableProxy_e=request.getAttributeNames();" +
										"for(;HttpServletRequestAttributesVariableProxy_e!=null&&HttpServletRequestAttributesVariableProxy_e.hasMoreElements();){" +
										"java.lang.String HttpServletRequestAttributesVariableProxy_name=(java.lang.String)HttpServletRequestAttributesVariableProxy_e.nextElement();" +
										"java.lang.Object HttpServletRequestAttributesVariableProxy_attr=request.getAttribute(HttpServletRequestAttributesVariableProxy_name);" +
										"java.util.Hashtable HttpServletRequestAttributesVariableProxy_entry = new java.util.Hashtable(1, 1.0f);" +
										"HttpServletRequestAttributesVariableProxy_entry.put(HttpServletRequestAttributesVariableProxy_name,HttpServletRequestAttributesVariableProxy_attr);" +
										"HttpServletRequestAttributesVariableProxy_list.add(HttpServletRequestAttributesVariableProxy_entry);}" +
										"return (java.util.Hashtable[])HttpServletRequestAttributesVariableProxy_list.toArray(new java.util.Hashtable[HttpServletRequestAttributesVariableProxy_list.size()]);");
			if (result == null || result.hasErrors()) {
				return null;
			}
			return result;
		} catch (Exception x) {
		}
		return null;
	}
	
}

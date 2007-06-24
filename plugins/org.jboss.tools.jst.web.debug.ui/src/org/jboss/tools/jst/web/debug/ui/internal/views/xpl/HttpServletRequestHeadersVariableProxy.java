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
public class HttpServletRequestHeadersVariableProxy extends EnumerationVariableProxy {
	
	/**
	 * @param origin
	 */
	HttpServletRequestHeadersVariableProxy(StackFrameWrapper frameWrapper, IVariable origin) {
		super(frameWrapper, origin, "headers", "java.util.Enumeration");
	}

	protected IEvaluationResult evaluateEnumerationVariable() {
		try {
//			IEvaluationResult result = EvaluationSupport.evaluateExpression(
//					fStackFrameWrapper.getStackFrame(),
//					"request.getHeaderNames();");
			IEvaluationResult result = EvaluationSupport.evaluateExpression(
										fStackFrameWrapper.getStackFrame(),
										"java.util.ArrayList HttpServletRequestHeadersVariableProxy_list=new java.util.ArrayList();" +
										"java.util.Enumeration HttpServletRequestHeadersVariableProxy_e=request.getHeaderNames();" +
										"for(;HttpServletRequestHeadersVariableProxy_e!=null&&HttpServletRequestHeadersVariableProxy_e.hasMoreElements();){" +
										 "java.lang.String HttpServletRequestHeadersVariableProxy_name=(java.lang.String)HttpServletRequestHeadersVariableProxy_e.nextElement();" +
										 "java.util.Enumeration HttpServletRequestHeadersVariableProxy_h=request.getHeaders(HttpServletRequestHeadersVariableProxy_name);" +
										 "java.util.ArrayList HttpServletRequestHeadersVariableProxy_hl=new java.util.ArrayList();" +
										 "for(;HttpServletRequestHeadersVariableProxy_h!=null&&HttpServletRequestHeadersVariableProxy_h.hasMoreElements();){" +
										  "java.lang.Object HttpServletRequestHeadersVariableProxy_header = (java.lang.Object)HttpServletRequestHeadersVariableProxy_h.nextElement();" +
										  "HttpServletRequestHeadersVariableProxy_hl.add((HttpServletRequestHeadersVariableProxy_header==null?\"\":HttpServletRequestHeadersVariableProxy_header.toString()));}" +
										 "java.util.Hashtable HttpServletRequestHeadersVariableProxy_entry = new java.util.Hashtable(1, 1.0f);" +
										 "HttpServletRequestHeadersVariableProxy_entry.put(HttpServletRequestHeadersVariableProxy_name,(java.lang.String[])HttpServletRequestHeadersVariableProxy_hl.toArray(new java.lang.String[HttpServletRequestHeadersVariableProxy_hl.size()]));" +
										 "HttpServletRequestHeadersVariableProxy_list.add(HttpServletRequestHeadersVariableProxy_entry);}" +
										"return (java.util.Hashtable[])HttpServletRequestHeadersVariableProxy_list.toArray(new java.util.Hashtable[HttpServletRequestHeadersVariableProxy_list.size()]);");
			if (result == null || result.hasErrors()) {
				return null;
			}
			return result;
		} catch (Exception x) {
		}
		return null;
	}


}

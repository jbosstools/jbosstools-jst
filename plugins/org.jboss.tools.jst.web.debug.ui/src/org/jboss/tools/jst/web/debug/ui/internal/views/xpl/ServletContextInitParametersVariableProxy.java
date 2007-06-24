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
public class ServletContextInitParametersVariableProxy extends EnumerationVariableProxy {
	/**
	 * @param origin
	 */
	ServletContextInitParametersVariableProxy(StackFrameWrapper frameWrapper, IVariable origin) {
		super(frameWrapper, origin, "initParameters", "java.util.Enumeration");
	}

	protected IEvaluationResult evaluateEnumerationVariable() {
		try {
			IEvaluationResult result = EvaluationSupport.evaluateExpression(
										fStackFrameWrapper.getStackFrame(),
										"java.util.ArrayList ServletContextInitParametersVariableProxy_list=new java.util.ArrayList();" +
										"javax.servlet.http.HttpSession ServletContextInitParametersVariableProxy_s=request.getSession(false);" +
										"if(ServletContextInitParametersVariableProxy_s==null)return null;" +
										"javax.servlet.ServletContext ServletContextInitParametersVariableProxy_c=ServletContextInitParametersVariableProxy_s.getServletContext();" +
										"if(ServletContextInitParametersVariableProxy_c==null)return null;" +
										"java.util.Enumeration ServletContextInitParametersVariableProxy_e=ServletContextInitParametersVariableProxy_c.getInitParameterNames();" +
										"for(;ServletContextInitParametersVariableProxy_e!=null&&ServletContextInitParametersVariableProxy_e.hasMoreElements();){" +
										"java.lang.String ServletContextInitParametersVariableProxy_name=(java.lang.String)ServletContextInitParametersVariableProxy_e.nextElement();" +
										"java.lang.Object ServletContextInitParametersVariableProxy_param=ServletContextInitParametersVariableProxy_c.getInitParameter(ServletContextInitParametersVariableProxy_name);" +
										"java.util.Hashtable ServletContextInitParametersVariableProxy_entry = new java.util.Hashtable(1, 1.0f);" +
										"ServletContextInitParametersVariableProxy_entry.put(ServletContextInitParametersVariableProxy_name,ServletContextInitParametersVariableProxy_param);" +
										"ServletContextInitParametersVariableProxy_list.add(ServletContextInitParametersVariableProxy_entry);}" +
										"return (java.util.Hashtable[])ServletContextInitParametersVariableProxy_list.toArray(new java.util.Hashtable[ServletContextInitParametersVariableProxy_list.size()]);");
			if (result == null || result.hasErrors()) {
				return null;
			}
			return result;
		} catch (Exception x) {
		}
		return null;
	}

}

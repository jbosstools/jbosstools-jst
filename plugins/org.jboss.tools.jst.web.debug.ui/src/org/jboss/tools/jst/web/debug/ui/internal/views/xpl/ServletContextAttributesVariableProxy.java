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
public class ServletContextAttributesVariableProxy extends EnumerationVariableProxy {
	/**
	 * @param origin
	 */
	ServletContextAttributesVariableProxy(StackFrameWrapper frameWrapper, IVariable origin) {
		super(frameWrapper, origin, "attributes", "java.util.Enumeration");
	}

	protected IEvaluationResult evaluateEnumerationVariable() {
		try {
			IEvaluationResult result = EvaluationSupport.evaluateExpression(
										fStackFrameWrapper.getStackFrame(),
										"java.util.ArrayList ServletContextAttributesVariableProxy_list=new java.util.ArrayList();" +
										"javax.servlet.http.HttpSession ServletContextAttributesVariableProxy_s=request.getSession(false);" +
										"if(ServletContextAttributesVariableProxy_s==null)return null;" +
										"javax.servlet.ServletContext ServletContextAttributesVariableProxy_c=ServletContextAttributesVariableProxy_s.getServletContext();" +
										"if(ServletContextAttributesVariableProxy_c==null)return null;" +
										"java.util.Enumeration ServletContextAttributesVariableProxy_e=ServletContextAttributesVariableProxy_c.getAttributeNames();" +
										"for(;ServletContextAttributesVariableProxy_e!=null&&ServletContextAttributesVariableProxy_e.hasMoreElements();){" +
										"java.lang.String ServletContextAttributesVariableProxy_name=(java.lang.String)ServletContextAttributesVariableProxy_e.nextElement();" +
										"java.lang.Object ServletContextAttributesVariableProxy_attr=ServletContextAttributesVariableProxy_c.getAttribute(ServletContextAttributesVariableProxy_name);" +
										"java.util.Hashtable ServletContextAttributesVariableProxy_entry = new java.util.Hashtable(1, 1.0f);" +
										"ServletContextAttributesVariableProxy_entry.put(ServletContextAttributesVariableProxy_name,ServletContextAttributesVariableProxy_attr);" +
										"ServletContextAttributesVariableProxy_list.add(ServletContextAttributesVariableProxy_entry);}" +
										"return (java.util.Hashtable[])ServletContextAttributesVariableProxy_list.toArray(new java.util.Hashtable[ServletContextAttributesVariableProxy_list.size()]);");
			if (result == null || result.hasErrors()) {
				return null;
			}
			return result;
		} catch (Exception x) {
		}
		return null;
	}

}

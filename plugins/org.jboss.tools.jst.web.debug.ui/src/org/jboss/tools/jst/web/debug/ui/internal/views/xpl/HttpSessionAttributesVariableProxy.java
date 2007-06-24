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
public class HttpSessionAttributesVariableProxy  extends EnumerationVariableProxy {
	/**
	 * @param origin
	 */
	HttpSessionAttributesVariableProxy(StackFrameWrapper frameWrapper, IVariable origin) {
		super(frameWrapper, origin, "attributes", "java.util.Enumeration");
	}

	protected IEvaluationResult evaluateEnumerationVariable() {
		try {
			IEvaluationResult result = EvaluationSupport.evaluateExpression(
										fStackFrameWrapper.getStackFrame(),
										"java.util.ArrayList HttpSessionAttributesVariableProxy_list=new java.util.ArrayList();" +
										"if (request == null) return null;" +
										"if (request.getSession(false) == null) return null;" +
										"javax.servlet.http.HttpSession HttpSessionAttributesVariableProxy_session = request.getSession(false);" +
										"java.util.Enumeration HttpSessionAttributesVariableProxy_e=HttpSessionAttributesVariableProxy_session.getAttributeNames();" +
										"for(;HttpSessionAttributesVariableProxy_e!=null&&HttpSessionAttributesVariableProxy_e.hasMoreElements();){" +
										"java.lang.String HttpSessionAttributesVariableProxy_name=(java.lang.String)HttpSessionAttributesVariableProxy_e.nextElement();" +
										"java.lang.Object HttpSessionAttributesVariableProxy_attr=HttpSessionAttributesVariableProxy_session.getAttribute(HttpSessionAttributesVariableProxy_name);" +
										"java.util.Hashtable HttpSessionAttributesVariableProxy_entry = new java.util.Hashtable(1, 1.0f);" +
										"HttpSessionAttributesVariableProxy_entry.put(HttpSessionAttributesVariableProxy_name,HttpSessionAttributesVariableProxy_attr);" +
										"HttpSessionAttributesVariableProxy_list.add(HttpSessionAttributesVariableProxy_entry);}" +
										"return (java.util.Hashtable[])HttpSessionAttributesVariableProxy_list.toArray(new java.util.Hashtable[HttpSessionAttributesVariableProxy_list.size()]);");
			if (result == null || result.hasErrors()) {
				return null;
			}
			return result;
		} catch (Exception x) {
		}
		return null;
	}
	
}

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
public class PageContextAttributesVariableProxy extends EnumerationVariableProxy {
	/**
	 * @param origin
	 */
	PageContextAttributesVariableProxy(StackFrameWrapper frameWrapper, IVariable origin) {
		super(frameWrapper, origin, "attributes", "java.util.Enumeration");
	}

	protected IEvaluationResult evaluateEnumerationVariable() {
		try {
			IEvaluationResult result = EvaluationSupport.evaluateExpression(
										fStackFrameWrapper.getStackFrame(),
										"java.util.ArrayList PageContextAttributesVariableProxy_list=new java.util.ArrayList();" +
										"java.util.Enumeration PageContextAttributesVariableProxy_e=pageContext.getAttributeNamesInScope(javax.servlet.jsp.PageContext.PAGE_SCOPE);" +
										"for(;PageContextAttributesVariableProxy_e!=null&&PageContextAttributesVariableProxy_e.hasMoreElements();){" +
										"java.lang.String PageContextAttributesVariableProxy_name=(java.lang.String)PageContextAttributesVariableProxy_e.nextElement();" +
										"java.lang.Object PageContextAttributesVariableProxy_attr=pageContext.getAttribute(PageContextAttributesVariableProxy_name, javax.servlet.jsp.PageContext.PAGE_SCOPE);" +
										"java.util.Hashtable PageContextAttributesVariableProxy_entry = new java.util.Hashtable(1, 1.0f);" +
										"PageContextAttributesVariableProxy_entry.put(PageContextAttributesVariableProxy_name,PageContextAttributesVariableProxy_attr);" +
										"PageContextAttributesVariableProxy_list.add(PageContextAttributesVariableProxy_entry);}" +
										"return(java.util.Hashtable[])PageContextAttributesVariableProxy_list.toArray(new java.util.Hashtable[PageContextAttributesVariableProxy_list.size()]);");
			if (result == null || result.hasErrors()) {
				return null;
			}
			return result;
		} catch (Exception x) {
		}
		return null;
	}

}

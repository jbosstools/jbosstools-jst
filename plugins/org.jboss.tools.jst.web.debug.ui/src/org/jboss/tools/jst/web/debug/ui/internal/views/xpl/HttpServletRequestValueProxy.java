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

import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.eclipse.jdt.debug.eval.IEvaluationResult;
import org.eclipse.jdt.internal.debug.core.model.JDINullValue;
import org.jboss.tools.jst.web.debug.ui.internal.views.properties.xpl.WebDataProperties;
import org.jboss.tools.jst.web.debug.xpl.EvaluationSupport;

/**
 * @author Jeremy
 */
public class HttpServletRequestValueProxy extends ValueProxy {

	private Map fVariablesM = new HashMap();
	private static String[] PROPERTY_NAMES = new String[] {
		WebDataProperties.SHOW_WEB_HTTPSERVLETREQUEST_HEADERS,
		WebDataProperties.SHOW_WEB_HTTPSERVLETREQUEST_SESSIONPROPERTY,
		WebDataProperties.SHOW_WEB_HTTPSERVLETREQUEST_ATTRIBUTES,
		WebDataProperties.SHOW_WEB_HTTPSERVLETREQUEST_PARAMETERS
	};

	/**
	 * @param frameWrapper
	 * @param origin
	 */
	HttpServletRequestValueProxy(StackFrameWrapper frameWrapper, IValue origin) {
		super(frameWrapper, origin);
	}

	protected void computeVariables() {
		fVariables = EMPTY_VARIABLES;
		try {
			if (fOrigin == null) return;
			if (TypeSupport.isSimpleTypeOrWrapper(((IJavaValue)fOrigin).getJavaType())) return;
			
			IVariable[] vars =  fOrigin.getVariables();
			List list = new ArrayList();
			
			// Add standard vars
			for (int i = 0; i < PROPERTY_NAMES.length; i++) {
				String name = PROPERTY_NAMES[i];
				if (!wdp.isEnabledFilter(name)) continue;

				if (WebDataProperties.SHOW_WEB_HTTPSERVLETREQUEST_ATTRIBUTES.equals(name)) {
					EnumerationVariableProxy variable = new HttpServletRequestAttributesVariableProxy(fStackFrameWrapper, HttpServletRequestVariableProxy.getRequestVariable(fStackFrameWrapper));
					list.add(variable);
				} else if (WebDataProperties.SHOW_WEB_HTTPSERVLETREQUEST_HEADERS.equals(name)) {
					EnumerationVariableProxy variable = new HttpServletRequestHeadersVariableProxy(fStackFrameWrapper, HttpServletRequestVariableProxy.getRequestVariable(fStackFrameWrapper));
					list.add(variable);
				} else if (WebDataProperties.SHOW_WEB_HTTPSERVLETREQUEST_PARAMETERS.equals(name)) {
//					IVariable var = EvaluationSupport.findVariableForName(vars, "parameterMap");
//					if (var != null) {
//						IVariable variable = VariableProxyFactory.createVariable(fStackFrameWrapper, var);
//						list.add(variable);
//					}
					EnumerationVariableProxy variable = new HttpServletRequestParametersVariableProxy(fStackFrameWrapper, HttpServletRequestVariableProxy.getRequestVariable(fStackFrameWrapper));
					list.add(variable);
				} else if (WebDataProperties.SHOW_WEB_HTTPSERVLETREQUEST_SESSIONPROPERTY.equals(name)) {
					try {
						IEvaluationResult resultValue = evaluateSessionVariable();
						HttpSessionVariableProxy variable = new HttpSessionVariableProxy(fStackFrameWrapper, resultValue, "session", "javax.servlet.http.HttpSession");
						list.add(variable);
					} catch (Exception x) {}
				}
			}
			String stopWords = "inputStream reader";
			for (int i = 0; list != null && i < list.size(); i++) {
				try {
					IVariable var = (IVariable)list.get(i);
					stopWords += " " + var.getName();
				} catch (Exception x) {}
			}
			
			FilteredVariablesEnumeration filtered = 
				new FilteredVariablesEnumeration(
						fOrigin, 
						WebDataProperties.SHOW_WEB_HTTPSERVLETREQUEST_FILTER,
						WebDataProperties.SHOW_WEB_HTTPSERVLETREQUEST_FILTER + WebDataProperties.VALUE_POSTFIX, 
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

	private IEvaluationResult evaluateSessionVariable() {
		try {
			IEvaluationResult result = EvaluationSupport.evaluateExpression(
										fStackFrameWrapper.getStackFrame(),
										"request.getSession(false)");
			if (result == null || result.hasErrors()) return null;
			return result;
		} catch (Exception x) {
		}
		return null;
	}

	private IEvaluationResult evaluateGetHeaderNamesVariable() {
		try {
			IEvaluationResult result = EvaluationSupport.evaluateExpression(
					fStackFrameWrapper.getStackFrame(),
					"request.getHeaderNames();");
//			IEvaluationResult result = EvaluationSupport.evaluateExpression(
//										fStackFrameWrapper.getStackFrame(),
//										"java.lang.String[] headers = null; {java.util.ArrayList l = new java.util.ArrayList();" +
//										"java.util.Enumeration e = request.getHeaderNames();" +
//										"for (;e != null && e.hasMoreElements();) l.add(e.nextElement()); " +
//										"headers = (java.lang.String[])l.toArray(new java.lang.String[l.size()]);}");
			if (result == null || result.hasErrors()) return null;
			return result;
		} catch (Exception x) {
		}
		return null;
	}

}

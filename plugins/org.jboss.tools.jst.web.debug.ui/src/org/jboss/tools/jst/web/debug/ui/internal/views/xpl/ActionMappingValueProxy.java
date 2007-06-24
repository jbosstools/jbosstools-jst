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
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.jdt.internal.debug.core.model.JDINullValue;
import org.jboss.tools.jst.web.debug.xpl.EvaluationSupport;

/**
 * @author Jeremy
 */
public class ActionMappingValueProxy extends ValueProxy {
/**
 *	path=/greeting,name=GetNameForm,scope=request,type=demo.GreetingAction
 */
	
	ActionMappingValueProxy(StackFrameWrapper frameWrapper, IValue origin) {
		super(frameWrapper, origin);
	}
	
	protected void computeVariables() {
		fVariables = null;
		if (fOrigin == null || (fOrigin instanceof JDINullValue)) return;
		IVariable[] vars = null;
		try {
			if (!fOrigin.hasVariables()) return;
			vars = fOrigin.getVariables(); 

			List<IVariable> list = new ArrayList<IVariable>();
			
			IVariable variable = EvaluationSupport.findVariableForName(vars, "path");
			if (variable != null && !(variable instanceof JDINullValue)) {
				VariableProxy v = new VariableProxy(fStackFrameWrapper, (IVariable)null, "name", "java.lang.String");
				v.setOrigin(variable);
				list.add(v);
			}
			variable = EvaluationSupport.findVariableForName(vars, "name");
			if (variable != null && !(variable instanceof JDINullValue)) {
				VariableProxy v = new VariableProxy(fStackFrameWrapper, (IVariable)null, "name", "java.lang.String");
				v.setOrigin(variable);
				list.add(v);
			}
			variable = EvaluationSupport.findVariableForName(vars, "scope");
			if (variable != null && !(variable instanceof JDINullValue)) {
				VariableProxy v = new VariableProxy(fStackFrameWrapper, (IVariable)null, "scope", "java.lang.String");
				v.setOrigin(variable);
				list.add(v);
			}
	
			variable = EvaluationSupport.findVariableForName(vars, "type");
			if (variable != null && !(variable instanceof JDINullValue)) {
				VariableProxy v = new VariableProxy(fStackFrameWrapper, (IVariable)null, "type", "java.lang.String");
				v.setOrigin(variable);
				list.add(v);
			}
	
			variable = EvaluationSupport.findVariableForName(vars, "forwards");
			if (variable != null && !(variable instanceof JDINullValue)) {
				VariableProxy v = new HashMapVariableProxy(fStackFrameWrapper, (IVariable)null, "forwards");
				v.setOrigin(variable);
				list.add(v);
			}
	
			variable = findGlobalForwards(vars);
			if (variable != null && !(variable instanceof JDINullValue)) {
				VariableProxy v = new HashMapVariableProxy(fStackFrameWrapper, (IVariable)null, "__GlobalForwards");
				v.setOrigin(variable);
				list.add(v);
			}
	
			variable = EvaluationSupport.findVariableForName(vars, "exceptions");
			if (variable != null && !(variable instanceof JDINullValue)) {
				VariableProxy v = new HashMapVariableProxy(fStackFrameWrapper, (IVariable)null, "exceptions");
				v.setOrigin(variable);
				list.add(v);
			}
	
			variable = findGlobalExceptions(vars);
			if (variable != null && !(variable instanceof JDINullValue)) {
				VariableProxy v = new HashMapVariableProxy(fStackFrameWrapper, (IVariable)null, "__GlobalExceptions");
				v.setOrigin(variable);
				list.add(v);
			}
	
			if (list.size() > 0) 
				fVariables = (IVariable[])list.toArray(new IVariable[list.size()]);
	
		} catch (Exception e) {
		}
	}
	
	private IVariable findGlobalForwards (IVariable[] vars) {
		IVariable globalForwards = null;
		try {
			IVariable moduleConfig = EvaluationSupport.findVariableForName(vars, "moduleConfig");
			if (moduleConfig == null) return null;
			if (moduleConfig.getValue() == null || !moduleConfig.getValue().hasVariables()) return null;
			
			IVariable[] mcVars = moduleConfig.getValue().getVariables();
	
			globalForwards = EvaluationSupport.findVariableForName(mcVars, "forwards");
		} catch (Exception e) {
		}
		return globalForwards;
	}

	private IVariable findGlobalExceptions (IVariable[] vars) {
		IVariable globalExceptions = null;
		try {
			IVariable moduleConfig = EvaluationSupport.findVariableForName(vars, "moduleConfig");
			if (moduleConfig == null) return null;
			if (moduleConfig.getValue() == null || !moduleConfig.getValue().hasVariables()) return null;
			
			IVariable[] mcVars = moduleConfig.getValue().getVariables();
			
			globalExceptions = EvaluationSupport.findVariableForName(mcVars, "exceptions");
		} catch (Exception e) {
		}
		return globalExceptions;
	}

	/**
	 * @see org.eclipse.debug.core.model.IValue#getValueString()
	 */
	public String getValueString() throws DebugException {
		computeVariables();
		if (fOrigin == null) return null;
		return getReferenceTypeName();
	}

	/**
	 * @see org.eclipse.debug.core.model.IValue#hasVariables()
	 */
	public boolean hasVariables() throws DebugException {
		computeVariables();
		return (fVariables != null && fVariables.length > 0);
	}
}

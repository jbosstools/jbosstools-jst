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
import java.util.Iterator;
import java.util.List;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.jdi.internal.MethodImpl;
import org.eclipse.jdt.debug.eval.IEvaluationResult;
import org.eclipse.jdt.internal.debug.core.model.JDIDebugTarget;
import org.eclipse.jdt.internal.debug.core.model.JDIValue;
import org.jboss.tools.jst.web.debug.xpl.EvaluationSupport;

import com.sun.jdi.ClassType;
import com.sun.jdi.VirtualMachine;

/**
 * @author Jeremy
 */
public class ActionFormValueProxy extends ValueProxy {
	
	ActionFormValueProxy(StackFrameWrapper frameWrapper, IValue origin) {
		super(frameWrapper, origin);
	}
	
	protected void computeVariables() {
		fVariables = EMPTY_VARIABLES;
		List variables = new ArrayList();
		
		if (fOrigin == null) return;
		try {
			JDIDebugTarget target = (JDIDebugTarget)fOrigin.getDebugTarget();
			VirtualMachine vm = target.getVM();
			List allClasses = vm.allClasses();
			Iterator iter = allClasses.iterator();
			ClassType originClass = null;
			while (iter.hasNext()) {
				Object obj = iter.next();
				if (obj instanceof ClassType) {
					ClassType clazz = (ClassType)obj;
					if (getReferenceTypeName().equals(clazz.name())) {
						originClass = clazz;
						break;
					}
				}
			}
			if (originClass == null) return;
			
			List allMethods = originClass.allMethods();
			List getMethods = new ArrayList();
			for (int i = 0; allMethods != null && i < allMethods.size(); i++) {
				Object mthd = allMethods.get(i);
				if (mthd instanceof MethodImpl){
					MethodImpl method = (MethodImpl)mthd;
					if (method.isNative() || 
					method.isAbstract() ||
					method.isConstructor() ||
					method.isPackagePrivate() ||
					method.isPrivate() || 
					method.isProtected() ||
					method.isStatic() ||
					method.isStaticInitializer() ||
					method.isSynthetic()) continue;
					
					if (method.name().startsWith("get")) {
						List args = method.arguments();
						List argTypeNames = method.argumentTypeNames();

						if (argTypeNames == null || argTypeNames.size() == 0) {
							String propName = new StringBuffer(String.valueOf(method.name().charAt(3)).toLowerCase()).append(method.name().substring(4)).toString();
							String refTypeName = method.returnTypeName();
							String type = fOrigin.getReferenceTypeName();
							String expr = "((" + type + ")form)." + method.name() + "()";

							IEvaluationResult result = null;
							try {
								result = EvaluationSupport.evaluateExpression(
															getStackFrame(), 
															expr);
								if (result != null && !result.hasErrors() && result.getValue().isAllocated()) {
									JDIValue resultValue = (JDIValue)result.getValue();
									if (resultValue.getArrayLength() != -1) {
										variables.add(new ArrayVariableProxy(fStackFrameWrapper, result, propName, refTypeName));
									} else {
										variables.add(new VariableProxy(fStackFrameWrapper, result, propName, refTypeName));
									}
									
								} 
			
							} catch (Exception e) {
								//ignore
							}
						}
					}
				}
			}
			
			fVariables = (IVariable[])variables.toArray(new IVariable[variables.size()]);
		} catch (Exception e) {
		}
	}

	/*
	 * @see org.eclipse.debug.core.model.IValue#getValueString()
	 */
	public String getValueString() throws DebugException {
		if (fOrigin == null) return null;
		return getReferenceTypeName();
	}

	/*
	 * @see org.eclipse.debug.core.model.IValue#getVariables()
	 */
	public IVariable[] getVariables() throws DebugException {
		if (fOrigin == null) return null;
		return super.getVariables();
	}

	/*
	 * @see org.eclipse.debug.core.model.IValue#hasVariables()
	 */
	public boolean hasVariables() throws DebugException {
		return (fVariables == null || fVariables.length > 0);
	}

}

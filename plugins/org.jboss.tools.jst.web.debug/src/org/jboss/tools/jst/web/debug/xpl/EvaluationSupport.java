/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Exadel, Inc.
 *     Red Hat, Inc. 
 *******************************************************************************/
package org.jboss.tools.jst.web.debug.xpl;

import java.util.HashMap;

import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.debug.core.IJavaDebugTarget;
import org.eclipse.jdt.debug.core.IJavaStackFrame;
import org.eclipse.jdt.debug.eval.EvaluationManager;
import org.eclipse.jdt.debug.eval.IAstEvaluationEngine;
import org.eclipse.jdt.debug.eval.ICompiledExpression;
import org.eclipse.jdt.debug.eval.IEvaluationListener;
import org.eclipse.jdt.debug.eval.IEvaluationResult;
import org.eclipse.jdt.internal.debug.core.model.JDIStackFrame;

/**
 * @author Jeremy
 *
 */

public class EvaluationSupport {
	
	private static HashMap fEngines = new HashMap();
	
	private EvaluationSupport() {
	}
	
	public static void clearCache() {
		 fEngines.clear();
	}
	
	public static void evaluateExpression (IStackFrame frame, String expression, IEvaluationListener evaluationListener) {
		try {
			IAstEvaluationEngine engine = getEvaluationEngine((JDIStackFrame)frame);
			ICompiledExpression compiledExpression = engine.getCompiledExpression(expression, (JDIStackFrame)frame);
			engine.evaluateExpression(compiledExpression, (IJavaStackFrame)frame, evaluationListener, DebugEvent.EVALUATION_IMPLICIT, false);
		} catch (Exception x) {
			//ignore
		}
	}

	private static class EvaluationListener implements IEvaluationListener {
		private IEvaluationResult fResult;
		private boolean fWaitingForResult;
		
		public void reset () {
			synchronized (this) {
				this.fResult = null;
				this.fWaitingForResult = true;
			}
		}
		
		private boolean IsWaiting() {
			boolean result = false;
			synchronized (this) {
				result = fWaitingForResult;
			}
			return result;
		}
		
		public IEvaluationResult getResult() {
			IEvaluationResult result = null;
			while (IsWaiting()) {
				Thread.yield();
				try {Thread.sleep(100);} catch (Exception x) {}
			}
			
			synchronized (this) {
				result = this.fResult;
			}
			return result;
		}
		
		EvaluationListener() {
			reset();	
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.jdt.debug.eval.IEvaluationListener#evaluationComplete(org.eclipse.jdt.debug.eval.IEvaluationResult)
		 */
		public void evaluationComplete(IEvaluationResult result) {
			synchronized (this) {
				this.fResult = result;
				this.fWaitingForResult = false;
			}
		}
	}

	public static synchronized IEvaluationResult evaluateExpression (IStackFrame frame, String expression) {
		IEvaluationResult result = null;
		try {
			IAstEvaluationEngine engine = getEvaluationEngine((JDIStackFrame)frame);
			ICompiledExpression compiledExpression = engine.getCompiledExpression(expression, (JDIStackFrame)frame);

			EvaluationListener evaluationListener = new EvaluationSupport.EvaluationListener();
			engine.evaluateExpression(compiledExpression, 
										(IJavaStackFrame)frame, 
										evaluationListener, 
										DebugEvent.EVALUATION_IMPLICIT, false);
			result = evaluationListener.getResult();
		} catch (Exception x) {
			//ignore
		}
		return result;
	}

	/**
	 * Returns an evaluation engine for evaluating this breakpoint's condition
	 * in the given target and project context.
	 */
	private static IAstEvaluationEngine getEvaluationEngine(JDIStackFrame frame)   {
		if (frame == null) return null;
		if (fEngines.containsKey(frame)) return (IAstEvaluationEngine)fEngines.get(frame);

		IJavaDebugTarget target = (IJavaDebugTarget)frame.getDebugTarget();
		IJavaProject project = DebugSupport.getJavaProject(frame);

		IAstEvaluationEngine engine = EvaluationManager.newAstEvaluationEngine(project, target);
		if (engine != null) fEngines.put(frame, engine);
		return engine;
	}

	public static IVariable findVariableForReferenceTypeName(IVariable[] variables, String referenceTypeName, String name) {
		if (variables == null || variables.length == 0) return null;

		try {
			for (int i = 0; i < variables.length; i++) {
				if (variables[i] != null) {
					if (referenceTypeName.equals(variables[i].getReferenceTypeName()) &&
							name.equals(variables[i].getName())) 
						return variables[i];
				}
			}
		} catch (Exception e) {
		}
		return null;
	}

	public static IVariable findVariableForName(IVariable[] variables, String name) {
		if (variables == null || variables.length == 0) return null;

		try {
			for (int i = 0; i < variables.length; i++) {
				if (variables[i] != null) {
					if (name.equals(variables[i].getName())) 
						return variables[i];
				}
			}
		} catch (Exception e) {
		}
		return null;
	}

}

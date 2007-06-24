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
package org.jboss.tools.jst.web.debug.ui.internal.views.xpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.eclipse.jdt.debug.eval.IEvaluationResult;
import org.eclipse.jdt.internal.debug.core.model.JDIThread;
import org.jboss.tools.jst.web.debug.ui.internal.views.properties.xpl.WebDataProperties;
import org.jboss.tools.jst.web.debug.ui.xpl.WebDebugUIPlugin;
import org.jboss.tools.jst.web.debug.xpl.EvaluationSupport;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;

/**
 * 
 * @author Jeremy
 *
 */
public class StrutsData implements IVariable, IValue {
	
	/*
	 * 
	 */
	private boolean fHasValueChanged = false;
	
	/*
	 * 
	 */
	private IVariable[] fVariables = new IVariable[5] ;

	/*
	 * 
	 */
	private WebDataProperties wdp;
	
	/*
	 * 
	 */
	private Map fVariablesM = new HashMap();

	/*
	 * 
	 */
	private static String[] PROPERTY_NAMES = new String[] {
		WebDataProperties.SHOW_ACTION_MAPPING_PROPERTY,
		WebDataProperties.SHOW_DYNA_ACTION_FORM_AND_SUBCLASSES_PROPERTY,
		WebDataProperties.SHOW_ACTION_MESSAGES_AND_ERRORS_PROPERTY,
		WebDataProperties.SHOW_ACTION_PROPERTY,
		WebDataProperties.SHOW_ACTION_FORWARD_PROPERTY
	};
	
	/*
	 * 
	 */
	private long fChangeCount;
	
	/*
	 * 
	 */
	long getChangeCount() {
		return fChangeCount;
	}
	
	/*
	 * 
	 */
	void setChangeCount(long count) {
		fChangeCount = count;
	}

	/**
	 * 
	 */
	void updateVariables() {
		IStackFrame thisFrame = parent.getStackFrame();
		if(thisFrame == null || thisFrame.isTerminated()) return;
		if(!thisFrame.isSuspended()) return;

		Object resultValue;
		List vars = new ArrayList();
		
		// Add standard vars
		for (int i = 0; i < PROPERTY_NAMES.length; i++) {
			String name = PROPERTY_NAMES[i];
			if (!wdp.isEnabledFilter(name)) continue;

			if (WebDataProperties.SHOW_ACTION_MAPPING_PROPERTY.equals(name)) {
				ActionMappingVariableProxy var = (ActionMappingVariableProxy)fVariablesM.get(WebDataProperties.SHOW_ACTION_MAPPING_PROPERTY);
				
				resultValue = getActionMappingVariable();
				if (resultValue instanceof IVariable) {
					var.setOrigin((IVariable)resultValue);
					try { var.fAlias = ((IVariable)resultValue).getName(); }
					catch (Exception e) {  } 
				} else {
					var.fAlias = "mapping"; //$NON-NLS-1$
					var.fType = "org.apache.struts.action.ActionMapping"; //$NON-NLS-1$
					if (resultValue instanceof IEvaluationResult)
						var.setResult((IEvaluationResult)resultValue);
					else var.setOrigin((IVariable)null);
				}
				
				vars.add(var);
			} else if (WebDataProperties.SHOW_DYNA_ACTION_FORM_AND_SUBCLASSES_PROPERTY.equals(name)) {
				ActionFormVariableProxy var = (ActionFormVariableProxy)fVariablesM.get(WebDataProperties.SHOW_DYNA_ACTION_FORM_AND_SUBCLASSES_PROPERTY);

				resultValue = getActionFormVariable();
				if (resultValue instanceof IVariable)
					var.setOrigin((IVariable)resultValue);
				else if (resultValue instanceof IEvaluationResult)
					var.setResult((IEvaluationResult)resultValue);
				else var.setOrigin((IVariable)null);
				
				vars.add(var);
			} else if (WebDataProperties.SHOW_ACTION_MESSAGES_AND_ERRORS_PROPERTY.equals(name)) {
				VariableProxy var = (VariableProxy)fVariablesM.get(WebDataProperties.SHOW_ACTION_MESSAGES_AND_ERRORS_PROPERTY);
				
				resultValue = evaluateActionErrorsVariable();
				if (resultValue instanceof IVariable)
					var.setOrigin((IVariable)resultValue);
				else if (resultValue instanceof IEvaluationResult)
					var.setResult((IEvaluationResult)resultValue);
				else var.setOrigin((IVariable)null);
				
				vars.add(var);
			} else if (WebDataProperties.SHOW_ACTION_PROPERTY.equals(name)) {
				ActionVariableProxy var = (ActionVariableProxy)fVariablesM.get(WebDataProperties.SHOW_ACTION_PROPERTY);
				
				resultValue = getActionVariable();
				if (resultValue instanceof IVariable)
					var.setOrigin((IVariable)resultValue);
				else if (resultValue instanceof IEvaluationResult)
					var.setResult((IEvaluationResult)resultValue);
				else var.setOrigin((IVariable)null);
				
				vars.add(var);
			} else if (WebDataProperties.SHOW_ACTION_FORWARD_PROPERTY.equals(name)) {
				ActionForwardVariableProxy var = (ActionForwardVariableProxy)fVariablesM.get(WebDataProperties.SHOW_ACTION_FORWARD_PROPERTY);
			
				resultValue = getActionForwardVariable();
				if (resultValue instanceof IVariable)
					var.setOrigin((IVariable)resultValue);
				else if (resultValue instanceof IEvaluationResult)
					var.setResult((IEvaluationResult)resultValue);
				else var.setOrigin((IVariable)null);
			
				vars.add(var);
			}
		}

		synchronized (this) {
			clearCurrentVariables();
				
			fVariables = new IVariable[vars.size()];
			for (int i = 0; i < vars.size(); i++) {
				fVariables[i] = (IVariable)vars.get(i);
			}
		}
		fHasValueChanged = false;
	}
	
	/*
	 * 
	 */
	private void clearCurrentVariables() {
		for (int i = 0; fVariables != null && i < fVariables.length; i++) {
			fVariables[i] = null;
		}
		fVariables = null;
	}
	
	/*
	 * 
	 */
	private boolean isActionFormVariableValid() {
		try {
			IValue v = ((ActionFormVariableProxy)fVariables[1]).getValue();
			if(v == null) return false;
			v.getVariables();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/*
	 * 
	 */
	private void createVariables() {
		fVariablesM.put(WebDataProperties.SHOW_ACTION_MAPPING_PROPERTY, 
			new ActionMappingVariableProxy(parent, null, "mapping", "org.apache.struts.action.ActionMapping")); //$NON-NLS-1$ //$NON-NLS-2$
		fVariablesM.put(WebDataProperties.SHOW_DYNA_ACTION_FORM_AND_SUBCLASSES_PROPERTY,
			new ActionFormVariableProxy(parent, null, "form", "org.apache.struts.action.ActionForm")); //$NON-NLS-1$ //$NON-NLS-2$
		fVariablesM.put(WebDataProperties.SHOW_ACTION_MESSAGES_AND_ERRORS_PROPERTY, 
			new ActionMessagesVariableProxy(parent, (IVariable)null, "errors", "org.apache.struts.action.ActionErrors")); //$NON-NLS-1$ //$NON-NLS-2$
		fVariablesM.put(WebDataProperties.SHOW_ACTION_PROPERTY, 
			new ActionVariableProxy(parent, (IVariable)null, "action", "org.apache.struts.action.Action")); //$NON-NLS-1$ //$NON-NLS-2$
		fVariablesM.put(WebDataProperties.SHOW_ACTION_FORWARD_PROPERTY, 
			new ActionForwardVariableProxy(parent, (IVariable)null, "forward", "org.apache.struts.action.ActionForward")); //$NON-NLS-1$ //$NON-NLS-2$

		updateVariables();
	}


	/**
	 * @see org.eclipse.debug.core.model.IValue#getValueString()
	 */
	public String getValueString() throws DebugException {
		return null;
	}

	/**
	 * @see org.eclipse.debug.core.model.IValue#isAllocated()
	 */
	public boolean isAllocated() throws DebugException {
		return true;
	}

	/**
	 * @see org.eclipse.debug.core.model.IValue#hasVariables()
	 */
	public boolean hasVariables() throws DebugException {
		return true;
	}


	StackFrameWrapper parent;
		
	public StrutsData (StackFrameWrapper parent) {
		this.parent = parent;
		wdp = new WebDataProperties(WebDebugUIPlugin.getDefault().getPreferenceStore());
		createVariables();
	}
/*
  (ActionMapping) this.request.getAttribute(Globals.MAPPING_KEY)
*/
	private IEvaluationResult evaluateActionMappingVariable() {
		try {
			IEvaluationResult result = EvaluationSupport.evaluateExpression(
										parent.getStackFrame(),
										"request.getAttribute(org.apache.struts.Globals.MAPPING_KEY)"); //$NON-NLS-1$
			if (result == null || result.hasErrors()) return null;
			return result;
		} catch (Exception x) {
		}
		return null;
	}


	private Object getActionMappingVariable () {
		IStackFrame thisFrame = parent.getStackFrame();
		if(thisFrame == null || thisFrame.isTerminated()) return null;
		IVariable[] stackVars = null;
		try { stackVars = parent.getFrameVariables(); }
		catch (Exception e) { }
		
		IVariable variable = EvaluationSupport.findVariableForName(stackVars, "mapping"); //$NON-NLS-1$
		if (variable != null) return variable;
		variable = EvaluationSupport.findVariableForName(stackVars, "actionMapping"); //$NON-NLS-1$
		if (variable != null) return variable;
		
		IEvaluationResult result = evaluateActionMappingVariable();
		if (result != null) return result;

		JDIThread thread = (JDIThread)thisFrame.getThread();
		List frames = null;
		try { frames = thread.computeNewStackFrames(); } 
		catch (Exception x) { return null; }
		
		for (int i = 0; frames != null && i < frames.size(); i++) {
			IStackFrame currentFrame = (IStackFrame)frames.get(i);
			if (!thisFrame.equals(currentFrame)) {
				try { stackVars = parent.getFrameVariables(); }
				catch (Exception e) { }
				variable = EvaluationSupport.findVariableForName(stackVars, "mapping"); //$NON-NLS-1$
				if (variable != null) return variable;
				variable = EvaluationSupport.findVariableForName(stackVars, "actionMapping"); //$NON-NLS-1$
				if (variable != null) return variable;
			}
		}
		return null;
	}


	private Object getActionFormVariable () {
		IStackFrame thisFrame = parent.getStackFrame();
		if(thisFrame == null || thisFrame.isTerminated()) return null;
		IVariable[] stackVars = null;
		try { stackVars = parent.getFrameVariables(); }
		catch (Exception e) { }

		IVariable variable = EvaluationSupport.findVariableForName(stackVars, "form"); //$NON-NLS-1$
		if (variable != null) return variable;
		
		try {
			IVariable thisVariable = EvaluationSupport.findVariableForName(stackVars, "this"); //$NON-NLS-1$
			if(thisVariable == null) return null;
			IJavaValue thisValue = (IJavaValue)thisVariable.getValue(); 
			if (TypeSupport.isInstanceOf(thisValue.getJavaType(), "org.apache.struts.action.ActionForm")) { //$NON-NLS-1$
				return thisVariable;
			}
		} catch (Exception e) {
		}
		
		JDIThread thread = (JDIThread)thisFrame.getThread();
		List frames = null;
		try { frames = thread.computeNewStackFrames(); } 
		catch (Exception x) { return null; }
		
		for (int i = 0; frames != null && i < frames.size(); i++) {
			IStackFrame currentFrame = (IStackFrame)frames.get(i);
			if (!thisFrame.equals(currentFrame)) {
				try { stackVars = parent.getFrameVariables(); }
				catch (Exception e) { }
				variable = EvaluationSupport.findVariableForName(stackVars, "form"); //$NON-NLS-1$
				if (variable != null) return variable;
			}
		}
		return null;
	}

	private IEvaluationResult evaluateActionErrorsVariable() {
		IEvaluationResult result = null;
		try {
			result = EvaluationSupport.evaluateExpression(
										parent.getStackFrame(), 
										"request.getAttribute(org.apache.struts.Globals.ERROR_KEY)"); //$NON-NLS-1$
			if (result == null || result.hasErrors() || !result.getValue().isAllocated()) return null;
			return result;
		} catch (Exception e) {
		}
		return null;
	}

	private Object getActionVariable() {
		try {
			IStackFrame thisFrame = parent.getStackFrame();
			if(thisFrame == null || thisFrame.isTerminated()) return null;
			IVariable[] stackVars = null;
			try { stackVars = parent.getFrameVariables(); }
			catch (Exception e) { }

			IVariable thisVariable = EvaluationSupport.findVariableForName(stackVars, "this"); //$NON-NLS-1$
			if(thisVariable == null) return null;
			IJavaValue thisValue = (IJavaValue)thisVariable.getValue(); 
			if (TypeSupport.isInstanceOf(thisValue.getJavaType(), "org.apache.struts.action.Action")) { //$NON-NLS-1$
				return thisVariable;
			}
		} catch (Exception e) {
		}
		return null;
	}

	private Object getActionForwardVariable() {
		IStackFrame thisFrame = parent.getStackFrame();
		if(thisFrame == null || thisFrame.isTerminated()) return null;
		IVariable[] stackVars = null;
		try { stackVars = parent.getFrameVariables(); }
		catch (Exception e) {  }
		
		IVariable variable = EvaluationSupport.findVariableForName(stackVars, "forward"); //$NON-NLS-1$
		if (variable != null) return variable;

		JDIThread thread = (JDIThread)thisFrame.getThread();
		List frames = null;
		try { frames = thread.computeNewStackFrames(); } 
		catch (Exception x) { return null; }
		
		for (int i = 0; frames != null && i < frames.size(); i++) {
			IStackFrame currentFrame = (IStackFrame)frames.get(i);
			if (!thisFrame.equals(currentFrame)) {
				try { stackVars = parent.getFrameVariables(); }
				catch (Exception e) { }
				variable = EvaluationSupport.findVariableForName(stackVars, "forward"); //$NON-NLS-1$
				if (variable != null) return variable;
			}
		}
		return null;
	}


	public IVariable[] getVariables() {
		if (fHasValueChanged) updateVariables();
				
		return fVariables;
	}

	/**
	 * @see org.eclipse.debug.core.model.IVariable#getValue()
	 */
	public IValue getValue() throws DebugException {
		if (hasValueChanged() ||
				getChangeCount() != parent.getChangeCount()) {
			updateVariables();
		}
		fHasValueChanged = false;
		setChangeCount(parent.getChangeCount());
		return this;
	}

	/**
	 * @see org.eclipse.debug.core.model.IVariable#getName()
	 */
	public String getName() throws DebugException {
		return WebUIMessages.STRUTS;
	}

	/**
	 * @see org.eclipse.debug.core.model.IVariable#getReferenceTypeName()
	 */
	public String getReferenceTypeName() throws DebugException {
		return null;
	}

	/**
	 * @see org.eclipse.debug.core.model.IVariable#hasValueChanged()
	 */
	public boolean hasValueChanged() throws DebugException {
		return false;
	}

	/**
	 * @see org.eclipse.debug.core.model.IDebugElement#getModelIdentifier()
	 */
	public String getModelIdentifier() {
		return parent.getModelIdentifier();
	}

	/**
	 * @see org.eclipse.debug.core.model.IDebugElement#getDebugTarget()
	 */
	public IDebugTarget getDebugTarget() {
		return parent.getDebugTarget();
	}

	/**
	 * @see org.eclipse.debug.core.model.IDebugElement#getLaunch()
	 */
	public ILaunch getLaunch() {
		return parent.getLaunch();
	}

	/**
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		return parent.getAdapter(adapter);
	}

	/**
	 * @see org.eclipse.debug.core.model.IValueModification#setValue(java.lang.String)
	 */
	public void setValue(String expression) throws DebugException {
	}

	/**
	 * @see org.eclipse.debug.core.model.IValueModification#setValue(org.eclipse.debug.core.model.IValue)
	 */
	public void setValue(IValue value) throws DebugException {
	}

	/**
	 * @see org.eclipse.debug.core.model.IValueModification#supportsValueModification()
	 */
	public boolean supportsValueModification() {
		return false;
	}

	/**
	 * @see org.eclipse.debug.core.model.IValueModification#verifyValue(java.lang.String)
	 */
	public boolean verifyValue(String expression) throws DebugException {
		return false;
	}

	/**
	 * @see org.eclipse.debug.core.model.IValueModification#verifyValue(org.eclipse.debug.core.model.IValue)
	 */
	public boolean verifyValue(IValue value) throws DebugException {
		return false;
	}

}

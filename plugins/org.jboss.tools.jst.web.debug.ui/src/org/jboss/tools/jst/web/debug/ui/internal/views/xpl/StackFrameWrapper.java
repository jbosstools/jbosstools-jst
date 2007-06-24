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

import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IDebugElement;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IVariable;
import org.jboss.tools.jst.web.debug.ui.internal.views.properties.xpl.WebDataProperties;
import org.jboss.tools.jst.web.debug.ui.xpl.WebDebugUIPlugin;

/**
 * @author Jeremy
 */

public class StackFrameWrapper implements IDebugElement {
	IStackFrame fFrame;
	IVariable[] fVariables;
	private boolean fHasValueChanged = false;
	IVariable[] frameVariables;
	private WebData fWebData;
	private StrutsData fStrutsData;
	private WebDataProperties wdp;
	private long fChangeCount;

	public StackFrameWrapper ( IStackFrame stack) {
		this.fFrame = stack;
		this.fChangeCount = 0;
		wdp = new WebDataProperties(WebDebugUIPlugin.getDefault().getPreferenceStore());
		fWebData = new WebData(this);
		fStrutsData = new StrutsData(this);
	}
		
	IStackFrame getStackFrame() {
		return fFrame;
	}
	
	long getChangeCount() {
		return fChangeCount;
	}
	void doChange() {
		fChangeCount++;
	}
	
	public IVariable[] getFrameVariables() {
		if(fFrame == null || fFrame.isTerminated()) return null;
		if(fFrame.isSuspended()) {
			try {
				return frameVariables = fFrame.getVariables();
			} catch (Exception e) {
				return null;
			}
		} else if(frameVariables == null) {
			return frameVariables;
		} else {
			return frameVariables;
		}
	}
	
	void setStackFrame(IStackFrame frame) {
		if (fFrame == frame) return;
		if (fFrame != null && fFrame.equals(frame)) 
			return;
		if (frame != null) {
			fFrame = frame;
			fChangeCount++;
		}
		fHasValueChanged = true;
	}
	
	public boolean hasVariables () {
		int count = 0;
		if (wdp.isEnabledFilter(WebDataProperties.SHOW_WEB_VARIABLES_PROPERTY)) count++;
		if (wdp.isEnabledFilter(WebDataProperties.SHOW_STRUTS_VARIABLES_PROPERTY)) count++;
		return (count > 0);
	}

	void updateVariables() {
		int count = 0;
		IVariable[] vars = new IVariable[2];
		
		if (wdp.isEnabledFilter(WebDataProperties.SHOW_WEB_VARIABLES_PROPERTY)) {
			try {
				if (fHasValueChanged) fWebData.updateVariables();
				vars[count] = fWebData;
				count++;
			} catch (Exception e) {  }
		}
		if (wdp.isEnabledFilter(WebDataProperties.SHOW_STRUTS_VARIABLES_PROPERTY)) {
			try { 
				if (fHasValueChanged) fStrutsData.updateVariables();
				vars[count] = fStrutsData;
				count++;
			} catch (Exception e) {  }
		}
		IVariable[] newVars = new IVariable[count];
		for (int i = 0; i < count; i++) newVars[i] = vars[i];
		
		fHasValueChanged = false;
		fVariables = newVars;
	}
		
	IVariable[] getVariables() {
		updateVariables();
		return fVariables;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IDebugElement#getModelIdentifier()
	 */
	public String getModelIdentifier() {
		if (this.fFrame == null)
			return null;
		return this.fFrame.getModelIdentifier();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IDebugElement#getDebugTarget()
	 */
	public IDebugTarget getDebugTarget() {
		if (this.fFrame == null)
			return null;
		return this.fFrame.getDebugTarget();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IDebugElement#getLaunch()
	 */
	public ILaunch getLaunch() {
		if (this.fFrame == null)
			return null;
		return this.fFrame.getLaunch();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		if (this.fFrame == null)
			return null;
		return this.fFrame.getAdapter(adapter);
	}
}

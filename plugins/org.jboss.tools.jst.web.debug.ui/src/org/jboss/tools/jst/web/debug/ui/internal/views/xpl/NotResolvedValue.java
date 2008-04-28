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

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;

/**
 * @author Jeremy
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class NotResolvedValue implements IValue {

	/**
	 * @see org.eclipse.debug.core.model.IValue#getReferenceTypeName()
	 */
	public String getReferenceTypeName() throws DebugException {
		return null;
	}

	/**
	 * @see org.eclipse.debug.core.model.IValue#getValueString()
	 */
	public String getValueString() throws DebugException {
		return WebUIMessages.NOT_RESOLVED;
	}

	/**
	 * @see org.eclipse.debug.core.model.IValue#isAllocated()
	 */
	public boolean isAllocated() throws DebugException {
		return false;
	}

	/**
	 * @see org.eclipse.debug.core.model.IValue#getVariables()
	 */
	public IVariable[] getVariables() throws DebugException {
		return null;
	}

	/**
	 * @see org.eclipse.debug.core.model.IValue#hasVariables()
	 */
	public boolean hasVariables() throws DebugException {
		return false;
	}

	/**
	 * @see org.eclipse.debug.core.model.IDebugElement#getModelIdentifier()
	 */
	public String getModelIdentifier() {
		return null;
	}

	/**
	 * @see org.eclipse.debug.core.model.IDebugElement#getDebugTarget()
	 */
	public IDebugTarget getDebugTarget() {
		return null;
	}

	/**
	 * @see org.eclipse.debug.core.model.IDebugElement#getLaunch()
	 */
	public ILaunch getLaunch() {
		return null;
	}

	/**
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		return null;
	}

}

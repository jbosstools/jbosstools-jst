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

/**
 * @author Jeremy
 */
public class ActionForwardVariableProxy extends VariableProxy {
	
	ActionForwardVariableProxy (StackFrameWrapper frameWrapper, IVariable origin) {
		super(frameWrapper, origin);
	}

	ActionForwardVariableProxy (StackFrameWrapper frameWrapper, IVariable origin, String name) {
		super(frameWrapper, origin, name);
	}

	ActionForwardVariableProxy (StackFrameWrapper frameWrapper, IVariable origin, String alias, String type) {
		super(frameWrapper, origin, alias, type);
	}
	ActionForwardVariableProxy(StackFrameWrapper frameWrapper, IEvaluationResult result, String alias, String type) {
		super(frameWrapper, result, alias, type);
	}

}

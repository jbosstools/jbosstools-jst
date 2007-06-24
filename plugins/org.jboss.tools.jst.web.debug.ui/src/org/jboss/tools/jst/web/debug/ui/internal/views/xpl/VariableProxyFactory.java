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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.debug.core.model.IVariable;
import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaVariable;

/**
 * @author Jeremy
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class VariableProxyFactory {
	
	static Map variableProxies = new HashMap();

	public static VariableProxy createVariable(StackFrameWrapper frameWrapper, IVariable origin) {
		VariableProxy vp = (origin == null) ? null : (VariableProxy)variableProxies.get(origin);
		if(vp == null) {
			vp = createVariable0(frameWrapper, origin);
			if(origin != null) variableProxies.put(origin, vp);
		}
		return vp;
	}

	public static VariableProxy createVariable(StackFrameWrapper frameWrapper, IVariable origin, Class cls) {
		VariableProxy vp = (origin == null) ? null : (VariableProxy)variableProxies.get(origin);
		if(vp == null) {
			try {
				vp = (VariableProxy)cls.getDeclaredConstructor(new Class[]{StackFrameWrapper.class, IVariable.class}).newInstance(new Object[]{frameWrapper, origin});
			} catch (Exception e) {
			}
			if(origin != null && vp != null) variableProxies.put(origin, vp);
		}
		return vp;
	}
	
	static VariableProxy createVariable0(StackFrameWrapper frameWrapper, IVariable origin) {
		
		try {
			IJavaType type = ((IJavaVariable)origin).getJavaType();
			if (TypeSupport.isArrayType(type)) {
				return new ArrayVariableProxy(frameWrapper, origin);
			} else if (TypeSupport.isInstanceOf(type, "java.util.HashMap")) {
				return new HashMapVariableProxy(frameWrapper, origin);
			} else if (TypeSupport.isInstanceOf(type, "java.util.Hashtable")) {
				return new HashtableVariableProxy(frameWrapper, origin);
			} else if (TypeSupport.isInstanceOf(type, "java.util.ArrayList")) {
				return new ArrayListVariableProxy(frameWrapper, origin);
			} else if (TypeSupport.isInstanceOf(type, "org.apache.struts.action.ActionForm")) {
				return new ActionFormVariableProxy(frameWrapper, origin);
			} else if (TypeSupport.isInstanceOf(type, "org.apache.struts.action.Action")) {
				return new ActionVariableProxy(frameWrapper, origin);	
			} else if (TypeSupport.isInstanceOf(type, "org.apache.struts.action.ActionMapping")) {
				return new ActionMappingVariableProxy(frameWrapper, origin);	
			} else if (TypeSupport.isInstanceOf(type, "org.apache.struts.action.ActionForward")) {
				return new ActionForwardVariableProxy(frameWrapper, origin);	
			} else if (TypeSupport.isInstanceOf(type, "org.apache.struts.config.ExceptionConfig")) {
				return new ExceptionConfigVariableProxy(frameWrapper, origin);	
			} else if (TypeSupport.isInstanceOf(type, "org.apache.struts.action.ActionMessages")) {
				return new ActionMessagesVariableProxy(frameWrapper, origin);	
			}
		} catch (Exception ex) {
		}
		return new VariableProxy(frameWrapper, origin);
	}
}

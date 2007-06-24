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

import org.eclipse.debug.core.model.IValue;
import org.eclipse.jdt.internal.debug.core.model.JDIValue;

/**
 * @author Jeremy
 */
public class ValueProxyFactory {
	static final String PROXY_TYPE_STRING = "java.lang.String";
	static final String PROXY_TYPE_HASHMAP = "java.util.HashMap";
	static final String PROXY_TYPE_HASHMAP_ENTRY = "java.util.HashMap$Entry";
	static final String PROXY_TYPE_ACTION_MAPPING = "org.apache.struts.action.ActionMapping";
	static final String PROXY_TYPE_ACTION_FORWARD = "org.apache.struts.action.ActionForward";
	static final String PROXY_TYPE_EXCEPTION_CONFIG = "org.apache.struts.config.ExceptionConfig";
	static final String PROXY_TYPE_ACTION_FORM = "org.apache.struts.action.ActionForm";
	static final String PROXY_TYPE_ACTION = "org.apache.struts.action.Action";
	static final String PROXY_TYPE_ACTION_MESSAGES = "org.apache.struts.action.ActionMessages";
	static final String PROXY_TYPE_ACTION_ERRORS = "org.apache.struts.action.ActionErrors";
//	ActionMessagesValueProxy
	private ValueProxyFactory() {
		
	}

	public static IValue createValueProxy(StackFrameWrapper frameWrapper, IValue value, Class cls) {
		IValue vp  = null;
//		IValue vp = (value == null) ? null : (IValue)valueProxies.get(value);
		if(vp == null) {
			try {
				vp = (IValue)cls.getDeclaredConstructor(new Class[]{StackFrameWrapper.class, IValue.class}).newInstance(new Object[]{frameWrapper, value});
			} catch (Exception e) {
			}
//			if(value != null && vp != null) valueProxies.put(value, vp);
		}
		return vp;
	}
	
	public static IValue createValueProxy(StackFrameWrapper frameWrapper, IValue value) {
		IValue vp  = null;
//		IValue vp = (value == null) ? null : (IValue)valueProxies.get(value);
		if(vp == null) {
			vp = createValueProxy0(frameWrapper, value);
			if(value != null) {
//				valueProxies.put(value, vp);
			}
		}
		return vp;
	}
	
	static IValue createValueProxy0(StackFrameWrapper frameWrapper, IValue value) {
		IValue proxy = value;
		int arrayLength = -1;
		
		try { arrayLength = ((JDIValue) value).getArrayLength(); } catch (Exception e) { arrayLength = -1; }
		
		try {
			String type = value.getReferenceTypeName();
			
			if (arrayLength != -1) return new ArrayValueProxy(frameWrapper, value);
			
			if (PROXY_TYPE_HASHMAP.equals(type)) {
				return new HashMapValueProxy(frameWrapper, value);
			} else if (PROXY_TYPE_HASHMAP_ENTRY.equals(type)) {
				return new HashMapEntryValueProxy(frameWrapper, value);
			} else if ("java.util.Hashtable".equals(type)) {
				return new HashtableValueProxy(frameWrapper, value);
			} else if ("java.util.Hashtable$Entry".equals(type)) {
				return new HashMapEntryValueProxy(frameWrapper, value);
			} else if ("java.util.ArrayList".equals(type)) {
				return new ArrayListValueProxy(frameWrapper, value);
			} else if (PROXY_TYPE_ACTION_MAPPING.equals(type)) {
				return new ActionMappingValueProxy(frameWrapper, value);
			} else if (PROXY_TYPE_ACTION_FORWARD.equals(type)) {
				return new ActionForwardValueProxy(frameWrapper, value);
			} else if (PROXY_TYPE_EXCEPTION_CONFIG.equals(type)) {
				return new ExceptionConfigValueProxy(frameWrapper, value);
			} else if (PROXY_TYPE_ACTION_FORM.equals(type)) {
				return new ActionFormValueProxy(frameWrapper, value);
			} else if (PROXY_TYPE_ACTION.equals(type)) {
				return new ActionValueProxy(frameWrapper, value);
			} else if (PROXY_TYPE_ACTION_MESSAGES.equals(type) || 
					PROXY_TYPE_ACTION_ERRORS.equals(type)) {
				return new ActionMessagesVariableProxy.ActionMessagesValueProxy(frameWrapper, value);
			} else if (PROXY_TYPE_STRING.equals(type)) {
				return new ValueProxy(frameWrapper, value);
			}
		} catch (Exception e) {
		}
		
		return new ValueProxy(frameWrapper, proxy);
	}
	
	static java.util.Map valueProxies = new java.util.HashMap();

	public static IValue createValueProxy(StackFrameWrapper frameWrapper, IValue value, String referenceTypeName) {
		IValue vp = (value == null) ? null : (IValue)valueProxies.get(value);
		if(vp == null) {
			vp = createValueProxy0(frameWrapper, value, referenceTypeName);
			if(value != null) {
				valueProxies.put(value, vp);
			}
		}
		return vp;
	}
	
	static IValue createValueProxy0(StackFrameWrapper frameWrapper, IValue value, String referenceTypeName) {
		IValue proxy = value;
		int arrayLength = -1;
		
		try { arrayLength = ((JDIValue) value).getArrayLength(); } catch (Exception e) { arrayLength = -1; }
	
		try {
			String type = value.getReferenceTypeName();

			if (arrayLength != -1) return new ArrayValueProxy(frameWrapper, value);
			
			if (PROXY_TYPE_HASHMAP.equals(type) || 
				PROXY_TYPE_HASHMAP.equals(referenceTypeName)) {
				return new HashMapValueProxy(frameWrapper, value);
			} else if (PROXY_TYPE_HASHMAP_ENTRY.equals(type) ||
				PROXY_TYPE_HASHMAP_ENTRY.equals(referenceTypeName)) {
				return new HashMapEntryValueProxy(frameWrapper, value);
			} else if ("java.util.Hashtable".equals(type) ||
				"java.util.Hashtable".equals(referenceTypeName)) {
				return new HashtableValueProxy(frameWrapper, value);
			} else if ("java.util.Hashtable$Entry".equals(type) ||
				"java.util.Hashtable$Entry".equals(referenceTypeName)) {
				return new HashMapEntryValueProxy(frameWrapper, value);
			} else if ("java.util.ArrayList".equals(type) || 
				"java.util.ArrayList".equals(referenceTypeName)) {
				return new ArrayListValueProxy(frameWrapper, value);
			} else if (PROXY_TYPE_ACTION_MAPPING.equals(type) || 
				PROXY_TYPE_ACTION_MAPPING.equals(referenceTypeName)) {
				return new ActionMappingValueProxy(frameWrapper, value);
			} else if (PROXY_TYPE_ACTION_FORWARD.equals(type) ||
				PROXY_TYPE_ACTION_FORWARD.equals(referenceTypeName)) {
				return new ActionForwardValueProxy(frameWrapper, value);
			} else if (PROXY_TYPE_EXCEPTION_CONFIG.equals(type) ||
				PROXY_TYPE_EXCEPTION_CONFIG.equals(referenceTypeName)) {
				return new ExceptionConfigValueProxy(frameWrapper, value);
			} else if (PROXY_TYPE_ACTION_FORM.equals(type) ||
				PROXY_TYPE_ACTION_FORM.equals(referenceTypeName)) {
				return new ActionFormValueProxy(frameWrapper, value);
			} else if (PROXY_TYPE_ACTION.equals(type) ||
				PROXY_TYPE_ACTION.equals(referenceTypeName)) {
				return new ActionValueProxy(frameWrapper, value);
			} else if (PROXY_TYPE_ACTION_MESSAGES.equals(type) || 
					PROXY_TYPE_ACTION_MESSAGES.equals(referenceTypeName) ||
					PROXY_TYPE_ACTION_ERRORS.equals(type) ||
					PROXY_TYPE_ACTION_ERRORS.equals(referenceTypeName)) {
				return new ActionMessagesVariableProxy.ActionMessagesValueProxy(frameWrapper, value);
			} else if (PROXY_TYPE_STRING.equals(type) || 
				PROXY_TYPE_STRING.equals(referenceTypeName)) {
				return new ValueProxy(frameWrapper, value);
			}
		} catch (Exception e) {
		}
		
		return new ValueProxy(frameWrapper, proxy);
	}

}

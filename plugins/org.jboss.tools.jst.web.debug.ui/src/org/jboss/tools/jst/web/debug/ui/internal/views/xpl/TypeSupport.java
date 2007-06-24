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

import java.util.TreeSet;

import org.eclipse.jdt.debug.core.IJavaArrayType;
import org.eclipse.jdt.debug.core.IJavaClassType;
import org.eclipse.jdt.debug.core.IJavaInterfaceType;
import org.eclipse.jdt.debug.core.IJavaType;

/**
 * @author Jeremy
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class TypeSupport {
	public static boolean isClassType(IJavaType javaType) {
		return (javaType instanceof IJavaClassType);
	}
	public static boolean isInterfaceType(IJavaType javaType) {
		return (javaType instanceof IJavaInterfaceType);
	}
	public static boolean isArrayType(IJavaType javaType) {
		return (javaType instanceof IJavaArrayType);
	}
	public static boolean isSimpleType(IJavaType javaType) {
		if (isClassType(javaType)) return false;
		if (isInterfaceType(javaType)) return false;
		if (isArrayType(javaType)) return false;
		try {
			return SIMPLE_TYPES.contains(javaType.getName());
		} catch (Exception e) {
		}
		return false;
	}
	
	public static boolean isSimpleTypeOrWrapper (IJavaType javaType) {
		try {
			if (isSimpleType(javaType)) return true;
			if (isClassType(javaType)) {
				return SIMPLE_TYPE_WRAPPERS.contains(javaType.getName());
			}
		} catch (Exception x) { }
		return false;
	}
	
	private static class SimpleTypes extends TreeSet {
		SimpleTypes() {
			super(String.CASE_INSENSITIVE_ORDER);
			add("boolean"); //$NON-NLS-1$
			add("byte"); //$NON-NLS-1$
			add("char"); //$NON-NLS-1$
			add("short"); //$NON-NLS-1$
			add("int"); //$NON-NLS-1$
			add("long"); //$NON-NLS-1$
			add("float"); //$NON-NLS-1$
			add("double"); //$NON-NLS-1$
		}
	};
	private static class SimpleTypeWrappers extends TreeSet {
		SimpleTypeWrappers() {
			super(String.CASE_INSENSITIVE_ORDER);
			add("java.lang.String"); //$NON-NLS-1$
//			add("java.lang.Boolean"); //$NON-NLS-1$
//			add("java.lang.Byte"); //$NON-NLS-1$
//			add("java.lang.Character"); //$NON-NLS-1$
//			add("java.lang.Short"); //$NON-NLS-1$
//			add("java.lang.Integer"); //$NON-NLS-1$
//			add("java.lang.Long"); //$NON-NLS-1$
//			add("java.lang.Float"); //$NON-NLS-1$
//			add("lava.lang.Double"); //$NON-NLS-1$
		}
	}
	private static final SimpleTypes SIMPLE_TYPES = new TypeSupport.SimpleTypes();
	private static final SimpleTypeWrappers SIMPLE_TYPE_WRAPPERS = new TypeSupport.SimpleTypeWrappers();
		
	public static boolean isInstanceOf(IJavaType javaType, String type) {
		if (javaType == null || type == null) return false;
		try {
			if (isSimpleType(javaType)) {
				IJavaType simpleType = (IJavaType)javaType;
				if (type.equals(simpleType.getName())){
					return true; 
				}
			} 
			if (isClassType(javaType)) {
				IJavaClassType classType = (IJavaClassType)javaType;
				if (type.equals(classType.getName())) {
					return true;
				} 
				IJavaClassType superClass = classType.getSuperclass();
				if (isInstanceOf(superClass, type)) {
					return true;
				} 
			}
			if (isInterfaceType(javaType)) {
				IJavaInterfaceType interfaceType = (IJavaInterfaceType)javaType;
				if (type.equals(interfaceType.getName())) {
					return true;
				} 
			}
			if (isArrayType(javaType)) {
				IJavaArrayType arrayType = (IJavaArrayType)javaType;
				if (type.equals(arrayType.getName())) {
					return true;
				} 
			}
		} catch (Exception e) {
		}
		return false;
	}

}

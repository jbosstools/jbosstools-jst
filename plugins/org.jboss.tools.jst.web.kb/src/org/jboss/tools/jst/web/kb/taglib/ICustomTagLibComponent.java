/******************************************************************************* 
 * Copyright (c) 2009 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.taglib;

/**
 * @author Alexey Kazakov
 */
public interface ICustomTagLibComponent extends IComponent {

	/**
	 * Returns the custom proposal providers for the attributes of this component.
	 * May be null.
	 * @return
	 */
	IAttributeProvider[] getProviders();
}
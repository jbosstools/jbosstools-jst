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
package org.jboss.tools.jst.web.kb.internal.taglib;

/**
 * @author Alexey Kazakov
 */
public class CustomComponentExtension extends CustomTagLibComponent {

	public CustomComponentExtension() {
		setName("*"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IComponent#isExtended()
	 */
	@Override
	public boolean isExtended() {
		return true;
	}

	/**
	 * @param attributes
	 */
	public void addAttributes(CustomTagLibAttribute[] attributes) {
		for (int i = 0; i < attributes.length; i++) {
			addAttribute(attributes[i]);
		}
	}
}
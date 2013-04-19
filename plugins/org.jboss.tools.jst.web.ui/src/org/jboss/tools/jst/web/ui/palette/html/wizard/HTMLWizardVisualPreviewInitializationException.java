/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.palette.html.wizard;

/**
 * @author Alexey Kazakov
 */
public class HTMLWizardVisualPreviewInitializationException extends Exception {

	private static final long serialVersionUID = 1977744483393866114L;

	public HTMLWizardVisualPreviewInitializationException() {
	}

	/**
	 * @param message
	 */
	public HTMLWizardVisualPreviewInitializationException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public HTMLWizardVisualPreviewInitializationException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public HTMLWizardVisualPreviewInitializationException(String message,
			Throwable cause) {
		super(message, cause);
	}
}
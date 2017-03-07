/******************************************************************************* 
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.reddeer.wst.html.ui.wizard;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;

/**
 * Wizard dialog for creating a HTML File.
 * @author vpakan
 */
public class NewHTMLFileWizardDialog extends NewWizardDialog {
	/**
	 * Constructs the wizard with Web > HTML File.
	 */
	public NewHTMLFileWizardDialog() {
		super("Web", "HTML File");
	}
	
}
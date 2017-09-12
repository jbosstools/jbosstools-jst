/*******************************************************************************
 * Copyright (c) 2007-2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
/**
 * Represents JSF Project Import wizard page
 * @author vlado pakan
 */
package org.jboss.tools.jst.reddeer.web.ui.wizards.project;

import org.eclipse.reddeer.core.reference.ReferencedComposite;
import org.eclipse.reddeer.jface.wizard.WizardPage;
import org.eclipse.reddeer.swt.impl.text.DefaultText;

public class ImportWebProjectWizardPage extends WizardPage{
	
	
	public ImportWebProjectWizardPage(ReferencedComposite referencedComposite) {
		super(referencedComposite);
	}

	public void setWebXmlLocation(String location){
		new DefaultText(referencedComposite, 1).setText(location);
	}
}

/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.wizards.newfile;

import org.jboss.tools.common.model.ui.wizard.newfile.*;
import org.jboss.tools.jst.web.ui.JSTWebUIImages;

public class NewWebFileWizard extends NewFileWizardEx {

	public NewWebFileWizard(){
		super();
		setDefaultPageImageDescriptor(JSTWebUIImages.getInstance().getOrCreateImageDescriptor(JSTWebUIImages.WEB_DESCRIPTOR_IMAGE));
	}
	
	protected NewFileContextEx createNewFileContext() {
		return new NewWebFileContext();
	}
	
	class NewWebFileContext extends NewFileContextEx {
		protected String getActionPath() {
			return "CreateActions.CreateFiles.Web.CreateWebApp"; //$NON-NLS-1$
		}
	}

}

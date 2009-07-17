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
package org.jboss.tools.jst.web.tiles.ui.wizard.newfile;

import org.jboss.tools.common.meta.action.impl.SpecialWizardSupport;
import org.jboss.tools.common.model.ui.wizard.newfile.*;
import org.jboss.tools.jst.web.tiles.model.handlers.CreateTilesSupport;

public class NewTilesFileWizard extends NewFileWizardEx {

	protected NewFileContextEx createNewFileContext() {
		return new NewTilesFileContext();
	}
	
	class NewTilesFileContext extends NewFileContextEx {
		protected SpecialWizardSupport createSupport() {
			CreateTilesSupport support = new CreateTilesSupport();
			return support;
		}
		protected String getActionPath() {
			return "CreateActions.CreateFiles.Struts.CreateTiles"; //$NON-NLS-1$
		}
	}

}

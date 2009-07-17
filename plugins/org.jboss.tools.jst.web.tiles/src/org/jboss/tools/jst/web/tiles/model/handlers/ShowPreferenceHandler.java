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
package org.jboss.tools.jst.web.tiles.model.handlers;

import java.util.Properties;
import org.jboss.tools.common.meta.action.*;
import org.jboss.tools.common.meta.action.impl.AbstractHandler;
import org.jboss.tools.common.model.XModelException;
import org.jboss.tools.common.model.XModelObject;

public class ShowPreferenceHandler extends AbstractHandler {
	SpecialWizard wizard = SpecialWizardFactory.createSpecialWizard("org.jboss.tools.jst.web.tiles.ui.preferences.OpenTilesEditorPreferencePage"); //$NON-NLS-1$

    public boolean isEnabled(XModelObject object) {
        return wizard != null;
    }

    public void executeHandler(XModelObject object, Properties p) throws XModelException {
    	if(wizard != null) wizard.execute();
    }

}

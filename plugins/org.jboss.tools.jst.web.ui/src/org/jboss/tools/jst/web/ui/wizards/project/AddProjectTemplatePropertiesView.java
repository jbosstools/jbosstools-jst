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
package org.jboss.tools.jst.web.ui.wizards.project;

import org.jboss.tools.common.propertieseditor.PropertiesEditor;
import org.jboss.tools.common.model.ui.wizards.special.AbstractSpecialWizardStep;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.jboss.tools.common.model.XModelObject;

public class AddProjectTemplatePropertiesView extends AbstractSpecialWizardStep {
	PropertiesEditor propertiesEditor = new PropertiesEditor();

	public Control createControl(Composite parent) {
		XModelObject properties = (XModelObject)support.getProperties().get("properties"); //$NON-NLS-1$
		propertiesEditor.setObject(properties);
		return propertiesEditor.createControl(parent);
	}

}

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

import java.beans.PropertyChangeEvent;

import org.jboss.tools.common.model.ui.attribute.adapter.IModelPropertyEditorAdapter;
import org.jboss.tools.common.model.ui.wizards.special.*;

import org.jboss.tools.common.meta.action.impl.SpecialWizardSupport;

public class AddProjectTemplateNameView extends SpecialWizardStep {
	boolean nameEqualsProjectName = true;

	public void setSupport(SpecialWizardSupport support, int i) {
		super.setSupport(support, i);
		nameEqualsProjectName = true;
	}

	public void propertyChange(PropertyChangeEvent arg0) {
		syncNameAndProject(arg0);
		super.propertyChange(arg0);
	}
	
	private void syncNameAndProject(PropertyChangeEvent arg0) {
		IModelPropertyEditorAdapter n = attributes.getPropertyEditorAdapterByName("name"); //$NON-NLS-1$
		IModelPropertyEditorAdapter p = attributes.getPropertyEditorAdapterByName("project"); //$NON-NLS-1$
		if(n == null || p == null) return;
		if(arg0.getSource() == p) {
			if(nameEqualsProjectName) n.setValue(p.getStringValue(true));
			else nameEqualsProjectName = p.getStringValue(true).equals(n.getStringValue(true));
		} else if(arg0.getSource() == n) {
			nameEqualsProjectName = p.getStringValue(true).equals(n.getStringValue(true));			
		}
	}

}

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

import java.util.ArrayList;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.jboss.tools.common.model.ui.attribute.adapter.IModelPropertyEditorAdapter;
import org.jboss.tools.common.model.ui.attribute.adapter.StructuredListAdapter;
import org.jboss.tools.common.model.ui.wizards.special.SpecialWizardStep;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.dialogs.ResourceSelectionDialog;
import org.jboss.tools.jst.web.project.handlers.AddProjectTemplateSupport;
import org.jboss.tools.jst.web.ui.Messages;

public class AddProjectTemplateVelocityView extends SpecialWizardStep {

	public Control createControl(Composite parent) {
		IModelPropertyEditorAdapter adapter = attributes.getPropertyEditorAdapterByName("velocity templates"); //$NON-NLS-1$
		if(adapter instanceof StructuredListAdapter) {
			StructuredListAdapter s = (StructuredListAdapter)adapter;
			NewResourceProvider provider = new NewResourceProvider();
			s.setNewValueProvider(provider);
		}
		return super.createControl(parent);
	}

	class NewResourceProvider implements StructuredListAdapter.INewValueProvider {
		IResource root;
		
		public void setContext(IResource root) {
			this.root = root;
		}

		public Object getValue() {
			IProject root = ((AddProjectTemplateSupport)support).getSelectedProject();
			ResourceSelectionDialog d = new ResourceSelectionDialog(stepControl.getShell(), root, Messages.AddProjectTemplateVelocityView_SelectMappings);
			int ii = d.open();
			if(ii != ResourceSelectionDialog.OK) return null;
			Object[] ss = d.getResult();
			ArrayList result = new ArrayList();
			for (int i = 0; i < ss.length; i++) {
				if(!(ss[i] instanceof IResource)) continue;
				if(ss[i] instanceof IFile) {
					IFile fi = (IFile)ss[i];
					String path = fi.getFullPath().toString();
					result.add(path);
				}
			}
			return result.size() == 0 ? null : (String[])result.toArray(new String[0]);
		}

	}
}

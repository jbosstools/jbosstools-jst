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
package org.jboss.tools.jst.web.ui.wizards.palette;

import java.beans.PropertyChangeEvent;

import org.jboss.tools.common.model.ui.attribute.adapter.DefaultValueAdapter;
import org.jboss.tools.common.model.ui.wizards.special.SpecialWizardStep;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import org.jboss.tools.common.meta.action.impl.SpecialWizardSupport;
import org.jboss.tools.jst.web.tld.URIConstants;
import org.jboss.tools.jst.web.tld.model.handlers.ImportTLDToPaletteSupport;

public class ImportTLDPage extends SpecialWizardStep {
	protected ParentGroupComponent parentGroupComponent = new ParentGroupComponent();
	protected Composite stepControl2;
	ImportTLDToPaletteSupport importSupport;
	
	public ImportTLDPage() {
		parentGroupComponent.setListener(this);
	}
	
	public void setSupport(SpecialWizardSupport support, int i) {
		super.setSupport(support, i);
		importSupport = (ImportTLDToPaletteSupport)support;
		parentGroupComponent.setItems((String[])support.getProperties().get("groups")); //$NON-NLS-1$
		parentGroupComponent.setInitialItem(support.getProperties().getProperty("parent group")); //$NON-NLS-1$
	}

	public Control createControl(Composite parent) {
		stepControl2 = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		stepControl2.setLayout(layout);
		stepControl2.setLayoutData(new GridData(GridData.FILL_BOTH));

		stepControl = attributes.createControl(stepControl2);
		stepControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		String focusAttr = support.getFocusAttribute(id);
		if(focusAttr != null && attributes.getFieldEditorByName(focusAttr) != null) {
			attributes.getFieldEditorByName(focusAttr).setFocus();
		}
		
		Control pgc = parentGroupComponent.createControl(stepControl2);
		pgc.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		updateFieldEnablement();

		return stepControl2;
	}

	public void updateFieldEnablement() {
		super.updateFieldEnablement();
		parentGroupComponent.updateFieldEnablement();
	}
	
	boolean isAdjusting = false;
	
	public void propertyChange(PropertyChangeEvent event) {
		if(isAdjusting) return;
		isAdjusting = true;
		isDataChanged = true;
		attributes.store();
		parentGroupComponent.store(support.getProperties());
		validate();
		if(event != null && event.getSource() == attributes.getPropertyEditorAdapterByName("tld")) { //$NON-NLS-1$
//			String path = (String)event.getNewValue();
			boolean locked = ((DefaultValueAdapter)attributes.getPropertyEditorAdapterByName("tld")).isStoreLocked(); //$NON-NLS-1$
			if(!locked) {
				importSupport.onPathModified();
				String[] as = {"name", URIConstants.DEFAULT_PREFIX, URIConstants.LIBRARY_URI}; //$NON-NLS-1$
				for (int i = 0; i < as.length; i++)
				attributes.getPropertyEditorAdapterByName(as[i]).setValue(support.getAttributeValue(0, as[i]));
				validate();
			}
		}
		updateFieldEnablement();
		isAdjusting = false;
	}

}

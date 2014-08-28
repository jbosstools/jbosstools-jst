/******************************************************************************* 
 * Copyright (c) 2014 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard;

import java.beans.PropertyChangeEvent;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.jboss.tools.jst.web.ui.internal.properties.advanced.LayoutUtil;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewNavigationWizardPage extends NewIonicWidgetWizardPage {

	public NewNavigationWizardPage() {
		super("newNavigation", IonicWizardMessages.newNavigationWizardTitle);
		setDescription(IonicWizardMessages.newNavigationWizardDescription);
	}

	@Override
	protected void createFieldPanel(Composite parent) {
		
		Group navbar = parent == null ? null : LayoutUtil.createGroup(parent, "Navigation Bar");
		addEditor(IonicFieldEditorFactory.createDelegateHandleEditor(), navbar);
		addEditor(IonicFieldEditorFactory.createAddBackButtonEditor(), navbar);
		addEditor(IonicFieldEditorFactory.createAlignTitleEditor(), navbar);
		addEditor(IonicFieldEditorFactory.createNoTapScrollEditor(), navbar);
		addEditor(IonicFieldEditorFactory.createAnimationEditor(EDITOR_ID_NAV_BAR_ANIMATION), navbar);
		addEditor(IonicFieldEditorFactory.createBarColorEditor(EDITOR_ID_BAR_COLOR), navbar);

		Group navview = parent == null ? null : LayoutUtil.createGroup(parent, "Navigation View");
		addEditor(IonicFieldEditorFactory.createNavViewNameEditor(), navview);
		addEditor(IonicFieldEditorFactory.createAnimationEditor(EDITOR_ID_NAV_VIEW_ANIMATION), navview);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
	}

}
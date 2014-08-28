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

import org.eclipse.swt.widgets.Composite;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewRefresherWizardPage extends NewIonicWidgetWizardPage {

	public NewRefresherWizardPage() {
		super("newRefresher", IonicWizardMessages.newRefresherWizardTitle);
		setDescription(IonicWizardMessages.newRefresherWizardDescription);
	}

	@Override
	protected void createFieldPanel(Composite parent) {
//		createIDEditor(parent, true);
//		addID.setValue(Boolean.FALSE);

		addEditor(IonicFieldEditorFactory.createOnPullingEditor(), parent);
		addEditor(IonicFieldEditorFactory.createPullingIconEditor(), parent);
		addEditor(IonicFieldEditorFactory.createPullingTextEditor(), parent);

		createSeparator(parent);

		addEditor(IonicFieldEditorFactory.createOnRefreshEditor(IonicWizardMessages.refresherOnrefreshDescription), parent);
		addEditor(IonicFieldEditorFactory.createRefreshingIconEditor(), parent);
		addEditor(IonicFieldEditorFactory.createRefreshingTextEditor(), parent);

	}

}
/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.internal.properties;

import org.eclipse.jface.action.Action;
import org.eclipse.wst.sse.ui.internal.editor.EditorPluginImageHelper;
import org.eclipse.wst.sse.ui.internal.editor.EditorPluginImages;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

class RemoveAction extends Action {
	private FormPropertySheetPage page;

	public RemoveAction(FormPropertySheetPage page) {
		super();
		this.page = page;
		setText(WizardMessages.removeActionLabel);
		setToolTipText(getText());
		setImageDescriptor(EditorPluginImageHelper.getInstance().getImageDescriptor(EditorPluginImages.IMG_ELCL_DELETE));
		setDisabledImageDescriptor(EditorPluginImageHelper.getInstance().getImageDescriptor(EditorPluginImages.IMG_DLCL_DELETE));
	}

	public void run() {
		page.getViewer().deactivateCellEditor();
		page.getViewer().getEditedDecriptor().removeProperty();
	}
}

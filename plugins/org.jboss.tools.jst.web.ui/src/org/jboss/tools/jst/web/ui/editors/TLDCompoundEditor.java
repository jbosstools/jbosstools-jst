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
package org.jboss.tools.jst.web.ui.editors;

import org.jboss.tools.common.editor.TreeFormPage;
import org.jboss.tools.common.model.ui.editor.EditorDescriptor;
import org.jboss.tools.common.model.ui.editors.multipage.DefaultMultipageEditor;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;
import org.jboss.tools.jst.web.tld.model.EditorTreeConstraint;

public class TLDCompoundEditor extends DefaultMultipageEditor {

	protected void doCreatePages() {
		// create
		if(isAppropriateNature()) {
			treeFormPage = createTreeFormPage();
			treeFormPage.setTitle(WebUIMessages.REDHAT_TAG_LIBRARY_EDITOR);
			((TreeFormPage)treeFormPage).addFilter(new EditorTreeConstraint());
			treeFormPage.initialize(object);
			addFormPage(treeFormPage);
		}
		createTextPage();
		initEditors();
		if(treeFormPage != null) selectionProvider.addHost("treeEditor", treeFormPage.getSelectionProvider()); //$NON-NLS-1$
		if(textEditor != null) selectionProvider.addHost("textEditor", getTextSelectionProvider()); //$NON-NLS-1$
	}

	public Object getAdapter(Class adapter) {
		if (adapter == EditorDescriptor.class)
			return new EditorDescriptor("tld"); //$NON-NLS-1$

		return super.getAdapter(adapter);
	}
}
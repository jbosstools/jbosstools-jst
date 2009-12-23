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
import org.jboss.tools.common.meta.key.WizardKeys;
import org.jboss.tools.common.model.XFilteredTreeConstraint;
import org.jboss.tools.common.model.ui.editor.EditorDescriptor;
import org.jboss.tools.common.model.ui.editors.multipage.DefaultMultipageEditor;
import org.jboss.tools.common.model.util.ModelFeatureFactory;
import org.jboss.tools.jst.web.tld.model.EditorTreeConstraint;
import org.jboss.tools.jst.web.ui.Messages;

public class WebCompoundEditor extends DefaultMultipageEditor {
	public static String EDITOR_TREE_CONSTRAINT_ID = "editorTreeConstraint"; //$NON-NLS-1$
			
	protected void doCreatePages() {
		if(isAppropriateNature()) {
			treeFormPage = createTreeFormPage();
			String title = Messages.WebCompoundEditor_WebXMLEditor; 
			if(object != null) {
				String key = object.getModelEntity().getName() + ".editorTitle"; //$NON-NLS-1$
				String s = WizardKeys.getString(key);
				if(s != null) title = s;
			}
			treeFormPage.setTitle(title);
			addTreeConstraint();
			treeFormPage.initialize(object);
			addFormPage(treeFormPage);
		}
		createTextPage();
		initEditors();
		if(treeFormPage != null) selectionProvider.addHost("treeEditor", treeFormPage.getSelectionProvider()); //$NON-NLS-1$
		if(textEditor != null) selectionProvider.addHost("textEditor", getTextSelectionProvider()); //$NON-NLS-1$
	}

	protected void addTreeConstraint() {
		XFilteredTreeConstraint constraint = null;
		String editorTreeConstraintId = object.getModelEntity().getProperty(EDITOR_TREE_CONSTRAINT_ID);
		if(editorTreeConstraintId != null) {
			constraint = (XFilteredTreeConstraint)ModelFeatureFactory.getInstance().createFeatureInstance(editorTreeConstraintId);
		}
		if(constraint == null) {
			constraint = new EditorTreeConstraint();
		}
		((TreeFormPage)treeFormPage).addFilter(constraint);
	}

	public Object getAdapter(Class adapter) {
		if (adapter == EditorDescriptor.class)
			return new EditorDescriptor("web.xml"); //$NON-NLS-1$

		return super.getAdapter(adapter);
	}

	protected String[] getSupportedNatures() {
		return new String[0];
	}

}
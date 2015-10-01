/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *       Red Hat, Inc. - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.jst.js.node.preference;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.jboss.tools.jst.js.node.Messages;
import org.jboss.tools.jst.js.node.preference.editor.NodeHomeFieldEditor;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class NodePreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	public static final String PAGE_ID = "org.jboss.tools.jst.js.node.preference.NodePreferencesPage"; //$NON-NLS-1$

	public NodePreferencePage() {
		super(GRID);
	}

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(NodePreferenceHolder.getStore());
	}

	@Override
	protected void createFieldEditors() {
		NodeHomeFieldEditor nodeHomeEditor = new NodeHomeFieldEditor(NodePreferenceHolder.PREF_NODE_LOCATION,
				Messages.NodePreferencePage_NodeLocationLabel, getFieldEditorParent());
		addField(nodeHomeEditor);
	}
	
}

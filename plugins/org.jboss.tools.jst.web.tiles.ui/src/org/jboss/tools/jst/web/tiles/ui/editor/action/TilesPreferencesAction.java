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
package org.jboss.tools.jst.web.tiles.ui.editor.action;

import org.eclipse.gef.ui.actions.WorkbenchPartAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPart;

import org.jboss.tools.jst.web.tiles.ui.editor.TilesEditor;
import org.jboss.tools.jst.web.tiles.ui.preferences.TilesEditorTabbedPreferencesPage;

public class TilesPreferencesAction extends WorkbenchPartAction  {

	/**
	 * Constructor for PrintAction.
	 * @param part The workbench part associated with this PrintAction
	 */
	public TilesPreferencesAction(IWorkbenchPart part) {
		super(part);
	}

	/**
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		return true;
	}

	/**
	 * @see org.eclipse.gef.ui.actions.EditorPartAction#init()
	 */
	protected void init() {
		setId("Preferences"); //$NON-NLS-1$
		setImageDescriptor(ImageDescriptor.createFromFile(TilesEditor.class, "icons/preference.gif")); //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		TilesEditorTabbedPreferencesPage.openPreferenceDialog();
	}

	}
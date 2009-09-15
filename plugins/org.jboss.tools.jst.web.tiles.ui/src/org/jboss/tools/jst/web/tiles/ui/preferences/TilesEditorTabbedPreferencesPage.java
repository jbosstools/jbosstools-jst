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
package org.jboss.tools.jst.web.tiles.ui.preferences;
import org.jboss.tools.common.model.ui.preferences.TabbedPreferencesPage;
import org.jboss.tools.common.model.ui.preferences.XMOBasedPreferencesPage;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.options.Preference;
import org.jboss.tools.common.model.plugin.ModelPlugin;

public class TilesEditorTabbedPreferencesPage extends TabbedPreferencesPage implements IWorkbenchPreferencePage {
	public static String TILES_EDITOR_PATH = Preference.EDITOR_PATH + "/Tiles Diagram"; //$NON-NLS-1$
	
	public TilesEditorTabbedPreferencesPage() {
		XModel model = getPreferenceModel();		
		XModelObject tilesEditor = model.getByPath(TILES_EDITOR_PATH);
		addPreferencePage(new Tab(tilesEditor));
	}

	public void init(IWorkbench workbench)  {
	}
	
	class Tab extends XMOBasedPreferencesPage {
		public Tab(XModelObject xmo) {
			super(xmo);
		}
		public String getTitle() {
			return TilesEditorTabbedPreferencesPage.this.getTitle();
		}		
	}
	
	public static void openPreferenceDialog() {
		openPreferenceDialog(true);
	}
	
	public static void openPreferenceDialog(boolean block) {
		TilesEditorPreferenceDialog dialog = new TilesEditorPreferenceDialog();
		dialog.setBlockOnOpen(block);
		dialog.open();
	}

	private static class TilesEditorPreferenceDialog extends PreferenceDialog {
		public TilesEditorPreferenceDialog() {
			super(ModelPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(), 
			ModelPlugin.getDefault().getWorkbench().getPreferenceManager());
			setSelectedNodePreference("org.jboss.tools.common.xstudio.editors.tilesdiagram"); //$NON-NLS-1$
		}
	}
}

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

import org.eclipse.ui.actions.RetargetAction;


import org.eclipse.jface.resource.ImageDescriptor;

import org.jboss.tools.jst.web.tiles.ui.Messages;
import org.jboss.tools.jst.web.tiles.ui.editor.TilesEditor;

public class TilesPreferencesRetargetAction extends RetargetAction{

	public TilesPreferencesRetargetAction() {
		super("Preferences",Messages.TilesPreferencesRetargetAction_Text);  //$NON-NLS-1$
		setToolTipText(Messages.TilesPreferencesRetargetAction_Tooltip);
		setImageDescriptor(ImageDescriptor.createFromFile(TilesEditor.class, "icons/preference.gif")); //$NON-NLS-1$
	}

}



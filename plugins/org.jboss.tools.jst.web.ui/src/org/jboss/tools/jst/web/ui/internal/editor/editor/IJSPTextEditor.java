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
package org.jboss.tools.jst.web.ui.internal.editor.editor;

import org.eclipse.wst.sse.ui.internal.StructuredTextViewer;
import org.jboss.tools.common.model.ui.editors.dnd.context.IDNDTextEditor;

public interface IJSPTextEditor extends IDNDTextEditor {
	public String[] getConfigurationPoints();
	public StructuredTextViewer getTextViewer();
	public void runDropCommand(String flavor, String data);
	public void setVPEController(IVisualController c);	
	public IVisualController getVPEController();
}

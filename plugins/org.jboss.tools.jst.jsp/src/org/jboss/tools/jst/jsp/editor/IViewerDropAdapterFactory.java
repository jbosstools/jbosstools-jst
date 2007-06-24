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
package org.jboss.tools.jst.jsp.editor;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.wst.common.ui.internal.dnd.*;
import org.jboss.tools.common.model.ui.editors.dnd.context.DropContext;

public interface IViewerDropAdapterFactory {

	public ViewerDropAdapter createDropAdapter(Transfer sourceTransfer, Viewer viewer, IJSPTextEditor editor, Transfer transfer, DropContext dropContext);

}

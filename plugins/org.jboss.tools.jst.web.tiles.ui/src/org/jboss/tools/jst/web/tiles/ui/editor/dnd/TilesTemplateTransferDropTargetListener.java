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
package org.jboss.tools.jst.web.tiles.ui.editor.dnd;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.requests.CreationFactory;

public class TilesTemplateTransferDropTargetListener
	extends TemplateTransferDropTargetListener {

	public TilesTemplateTransferDropTargetListener(EditPartViewer viewer) {
		super(viewer);
	}

	protected CreationFactory getFactory(Object template) {
		return new TilesTemplateFactory();
	}

	class TilesTemplateFactory implements CreationFactory {
		public Object getNewObject() {
			return "definition"; //$NON-NLS-1$
		}

		public Object getObjectType() {
			return String.class;
		}
	}

}

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
package org.jboss.tools.jst.web.tiles.ui.editor.edit;

import java.util.List;

import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.swt.accessibility.AccessibleEvent;

import org.jboss.tools.jst.web.tiles.ui.editor.model.ITilesModel;

/**
 * Container for EditParts
 */
abstract public class TilesContainerEditPart extends TilesEditPart {
	protected AccessibleEditPart createAccessible() {
		return new AccessibleGraphicalEditPart() {
			public void getName(AccessibleEvent event) {
				event.result = getTilesModel().toString();
			}
		};
	}

	/**
	 * 
	 */
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.CONTAINER_ROLE,
				new TilesContainerEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, 
				new TilesXYLayoutEditPolicy());
	}

	/**
	 * 
	 * @return
	 */
	protected ITilesModel getTilesModel() {
		return (ITilesModel) getModel();
	}

	/**
	 * 
	 * @return
	 */
	protected List getModelChildren() {
		return getTilesModel().getDefinitionList().getElements();
	}

}

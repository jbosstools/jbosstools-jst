/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Exadel, Inc.
 *     Red Hat, Inc. 
 *******************************************************************************/
package org.jboss.tools.jst.web.tiles.ui.editor.edit.xpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

public class DefaultGraphicalEditPart extends AbstractGraphicalEditPart {

	@Override
	protected IFigure createFigure() {
		return null;
	}

	@Override
	protected void createEditPolicies() {
	}
	
	protected void refreshSourceConnections() {
		int i;
		ConnectionEditPart editPart;
		Object model;

		Map modelToEditPart = new HashMap();
		List editParts = getSourceConnections();

		for (i = 0; i < editParts.size(); i++) {
			editPart = (ConnectionEditPart) editParts.get(i);
			modelToEditPart.put(editPart.getModel(), editPart);
		}

		List modelObjects = getModelSourceConnections();
		if (modelObjects == null)
			modelObjects = new ArrayList();

		for (i = 0; i < modelObjects.size(); i++) {
			model = modelObjects.get(i);

			if (i < editParts.size()) {
				editPart = (ConnectionEditPart) editParts.get(i);
				if (editPart.getModel() == model) {
					if (editPart.getSource() != this)
						editPart.setSource(this);
					continue;
				}
			}

			editPart = (ConnectionEditPart) modelToEditPart.get(model);
			if (editPart != null)
				reorderSourceConnection(editPart, i);
			else {
				editPart = createOrFindConnection(model);
				addSourceConnection(editPart, i);
			}
		}

		List<Object> trash = new ArrayList<Object>();
		for (; i < editParts.size(); i++)
			trash.add(editParts.get(i));
		for (i = 0; i < trash.size(); i++)
			removeSourceConnection((ConnectionEditPart) trash.get(i));
	}

	protected void refreshTargetConnections() {
		int i;
		ConnectionEditPart editPart;
		Object model;

		Map mapModelToEditPart = new HashMap();
		List connections = getTargetConnections();

		for (i = 0; i < connections.size(); i++) {
			editPart = (ConnectionEditPart) connections.get(i);
			mapModelToEditPart.put(editPart.getModel(), editPart);
		}

		List modelObjects = getModelTargetConnections();
		if (modelObjects == null)
			modelObjects = new ArrayList();

		for (i = 0; i < modelObjects.size(); i++) {
			model = modelObjects.get(i);

			if (i < connections.size()) {
				editPart = (ConnectionEditPart) connections.get(i);
				if (editPart.getModel() == model) {
					if (editPart.getTarget() != this)
						editPart.setTarget(this);
					continue;
				}
			}

			editPart = (ConnectionEditPart) mapModelToEditPart.get(model);
			if (editPart != null)
				reorderTargetConnection(editPart, i);
			else {
				editPart = createOrFindConnection(model);
				addTargetConnection(editPart, i);
			}
		}

		List trash = new ArrayList();
		for (; i < connections.size(); i++)
			trash.add(connections.get(i));
		for (i = 0; i < trash.size(); i++)
			removeTargetConnection((ConnectionEditPart) trash.get(i));
	}	

}

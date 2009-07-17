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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.*;
import org.jboss.tools.common.model.plugin.ModelPlugin;
import org.jboss.tools.common.model.ui.dnd.DnDUtil;
import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartListener;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.swt.accessibility.AccessibleEvent;

import org.jboss.tools.common.meta.action.XAction;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.gef.edit.GEFRootEditPart;
import org.jboss.tools.jst.web.tiles.ui.Messages;
import org.jboss.tools.jst.web.tiles.ui.TilesUIPlugin;
import org.jboss.tools.jst.web.tiles.ui.editor.figures.ConnectionFigure;
import org.jboss.tools.jst.web.tiles.ui.editor.figures.FigureFactory;
import org.jboss.tools.jst.web.tiles.ui.editor.model.ILink;
import org.jboss.tools.jst.web.tiles.ui.editor.model.ILinkListener;

public class LinkEditPart extends AbstractConnectionEditPart
	implements PropertyChangeListener, ILinkListener, EditPartListener {

	AccessibleEditPart acc;

	public void activate() {
		super.activate();
		getLink().addPropertyChangeListener(this);
		addEditPartListener(this);
	}

	public void activateFigure() {
		super.activateFigure();
		getFigure().addPropertyChangeListener(
				Connection.PROPERTY_CONNECTION_ROUTER, this);
	}

	public void doDoubleClick(boolean cf) {
		try {
			XAction action = DnDUtil.getEnabledAction(
					(XModelObject) getLinkModel().getSource(), null,
					"Properties.Properties"); //$NON-NLS-1$
			if (action != null)
				action.executeHandler(
						(XModelObject) getLinkModel().getSource(), null);
		} catch (Exception e) {
			TilesUIPlugin.getPluginLog().logError(e);
		}
	}

	public void doMouseDown(boolean cf) {

	}

	public void doMouseUp(boolean cf) {
	}

	/**
	 * Adds extra EditPolicies as required.
	 */
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE,
				new LinkEndpointEditPolicy());
		refreshBendpointEditPolicy();
		installEditPolicy(EditPolicy.CONNECTION_ROLE, new LinkEditPolicy());
	}

	protected IFigure createFigure() {
		if (getLink() == null)
			return null;
		ConnectionFigure conn = FigureFactory.createNewBendableWire(this,
				getLink());
		return conn;
	}

	public ILink getLinkModel() {
		return (ILink) getModel();
	}

	public void deactivate() {
		removeEditPartListener(this);
		getLink().removePropertyChangeListener(this);
		super.deactivate();
	}

	public void deactivateFigure() {
		getFigure().removePropertyChangeListener(
				Connection.PROPERTY_CONNECTION_ROUTER, this);
		super.deactivateFigure();
	}

	public AccessibleEditPart getAccessibleEditPart() {
		if (acc == null)
			acc = new AccessibleGraphicalEditPart() {
				public void getName(AccessibleEvent e) {
					e.result = Messages.LinkEditPart_Link;
				}
			};
		return acc;
	}

	protected ILink getLink() {
		return (ILink) getModel();
	}

	protected ConnectionFigure getLinkFigure() {
		return (ConnectionFigure) getFigure();
	}

	public void propertyChange(PropertyChangeEvent event) {
		String property = event.getPropertyName();
		if (Connection.PROPERTY_CONNECTION_ROUTER.equals(property)) {
			refreshBendpoints();
			refreshBendpointEditPolicy();
		}
		if ("value".equals(property)) //$NON-NLS-1$
			refreshVisuals();
		if ("bendpoint".equals(property)) //$NON-NLS-1$
			refreshBendpoints();
	}

	protected void refreshBendpoints() {
	}

	private void refreshBendpointEditPolicy() {
	}

	/**
	 * Refreshes the visual aspects of this, based upon the model (Wire). It
	 * changes the wire color depending on the state of Wire.
	 * 
	 */
	protected void refreshVisuals() {
	}

	public void setModel(Object model) {
		super.setModel(model);
		((ILink) model).addLinkListener(this);
	}

	public boolean isLinkListenerEnable() {
		return true;
	}

	public void linkChange(ILink source) {
		refresh();
	}

	public void linkRemove(ILink source) {
		getLink().removeLinkListener(this);
	}

	public void childAdded(EditPart child, int index) {
	}

	public void partActivated(EditPart editpart) {
	}

	public void partDeactivated(EditPart editpart) {
	}

	public void removingChild(EditPart child, int index) {
	}

	public void selectedStateChanged(EditPart editpart) {
		if (this.getSelected() == EditPart.SELECTED_PRIMARY) {
			((GEFRootEditPart) getParent()).setToFront(this);

		}
	}

}

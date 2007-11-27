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

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.gef.*;

import org.jboss.tools.jst.web.tiles.ui.editor.edit.xpl.DefaultGraphicalEditPart;
import org.jboss.tools.jst.web.tiles.ui.editor.figures.NodeFigure;

abstract public class TilesEditPart extends DefaultGraphicalEditPart implements
		NodeEditPart, PropertyChangeListener {

	private AccessibleEditPart acc;

	public void activate() {
		if (isActive())
			return;
		super.activate();
	}

	public void doDoubleClick(boolean cf) {
	}

	public void doMouseUp(boolean cf) {
	}

	public void doMouseDown(boolean cf) {
	}

	public void doMouseHover(boolean cf) {
	}

	public void doControlUp() {
	}

	public void doControlDown() {
	}

	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new TilesElementEditPolicy());
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,
				new TilesNodeEditPolicy());
	}

	abstract protected AccessibleEditPart createAccessible();

	/**
	 * Makes the EditPart insensible to changes in the model by removing itself
	 * from the model's list of listeners.
	 */
	public void deactivate() {
		if (!isActive())
			return;
		super.deactivate();
	}

	protected AccessibleEditPart getAccessibleEditPart() {
		if (acc == null)
			acc = createAccessible();
		return acc;
	}

	protected NodeFigure getNodeFigure() {
		return (NodeFigure) getFigure();
	}

	public ConnectionAnchor getSourceConnectionAnchor(
			ConnectionEditPart connEditPart) {
		return null;
	}

	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return null;
	}

	public ConnectionAnchor getTargetConnectionAnchor(
			ConnectionEditPart connEditPart) {
		return null;
	}

	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return null;
	}

	final protected String mapConnectionAnchorToTerminal(ConnectionAnchor c) {
		return getNodeFigure().getConnectionAnchorName(c);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		refreshVisuals();
	}

	protected void refreshVisuals() {
	}

	protected void removeSourceConnection(ConnectionEditPart connection) {
		if (connection.getSource() != this)
			return;
		fireRemovingSourceConnection(connection, getSourceConnections()
				.indexOf(connection));
		connection.deactivate();
		connection.setSource(null);
		primRemoveSourceConnection(connection);
	}

	protected void removeTargetConnection(ConnectionEditPart connection) {
		if (connection.getTarget() != this)
			return;
		fireRemovingTargetConnection(connection, getTargetConnections()
				.indexOf(connection));
		connection.setTarget(null);
		primRemoveTargetConnection(connection);
	}

}

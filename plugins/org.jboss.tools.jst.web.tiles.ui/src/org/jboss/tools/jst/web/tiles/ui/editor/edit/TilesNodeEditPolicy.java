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

import org.eclipse.draw2d.*;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;

import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import org.jboss.tools.jst.web.tiles.ui.editor.figures.FigureFactory;
import org.jboss.tools.jst.web.tiles.ui.editor.figures.NodeFigure;
import org.jboss.tools.jst.web.tiles.ui.editor.model.commands.ConnectionCommand;

public class TilesNodeEditPolicy extends
		org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy {

	protected Connection createDummyConnection(Request req) {
		PolylineConnection conn = FigureFactory.createNewLink(null);
		return conn;
	}

	protected Command getConnectionCompleteCommand(
			CreateConnectionRequest request) {
		ConnectionCommand command = (ConnectionCommand) request
				.getStartCommand();
		command.setTarget((TilesEditPart) getTilesEditPart());
		ConnectionAnchor ctor = getTilesEditPart().getTargetConnectionAnchor(
				request);
		if (ctor == null)
			return null;
		command.setTargetTerminal(getTilesEditPart()
				.mapConnectionAnchorToTerminal(ctor));
		return command;
	}

	protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
		if (getTilesEditPart() instanceof DefinitionEditPart) {
			if (((DefinitionEditPart) getTilesEditPart()).getDefinitionModel()
					.isAnotherTiles()
					|| !((DefinitionEditPart) getTilesEditPart())
							.getDefinitionModel().isConfirmed())
				return null;
		}
		ConnectionCommand command = new ConnectionCommand();
		command.setLink(null);
		command.setSource((TilesEditPart) getTilesEditPart());
		ConnectionAnchor ctor = getTilesEditPart().getSourceConnectionAnchor(
				request);
		command.setSourceTerminal(getTilesEditPart()
				.mapConnectionAnchorToTerminal(ctor));
		request.setStartCommand(command);
		return command;
	}

	protected TilesEditPart getTilesEditPart() {
		return (TilesEditPart) getHost();
	}

	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		return null;
	}

	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		return null;
	}

	protected NodeFigure getNodeFigure() {
		return (NodeFigure) ((GraphicalEditPart) getHost()).getFigure();
	}

}
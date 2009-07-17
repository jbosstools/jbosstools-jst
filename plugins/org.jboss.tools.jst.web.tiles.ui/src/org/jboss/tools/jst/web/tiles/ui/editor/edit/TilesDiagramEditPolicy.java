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

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

import org.jboss.tools.common.meta.action.XActionInvoker;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.jst.web.tiles.ui.editor.model.impl.TilesModel;

public class TilesDiagramEditPolicy extends RootComponentEditPolicy{

	public TilesDiagramEditPolicy() {
		super();
	}
	
	public TilesDiagramEditPart getDiagramEditPart(){
		return (TilesDiagramEditPart)getHost();
	}
	
	public Command getCommand(Request request) {
		if (RequestConstants.REQ_CREATE.equals(request.getType()))
			return getCreateCommand((CreateRequest)request);
		
		return super.getCommand(request);
	}
	
	public Command getCreateCommand(CreateRequest request){
		if(!request.getNewObjectType().equals(String.class)) return null;
		CreateViewCommand comm = new CreateViewCommand();
		comm.setLocation(request.getLocation());
		return comm;
	}
	
	class CreateViewCommand extends org.eclipse.gef.commands.Command{
		Point location;
	
		public void setLocation(Point point){
			TilesDiagramEditPolicy.this.getDiagramEditPart().getFigure().translateToRelative(point);
			location = point;
		}
	
	
		public CreateViewCommand(){
			super("CreateViewCommand"); //$NON-NLS-1$
		}
	
		public boolean canExecute(){
			return true;
		}
	
		public void execute(){
			XActionInvoker.invoke("CreateActions.CreateDefinition", (XModelObject)((TilesModel)getDiagramEditPart().getModel()).getSource(),null); //$NON-NLS-1$
		}
	
		public boolean canUndo() {
			return false;
		}
	}

}

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


import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.Polyline;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Transposer;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;

import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.DropRequest;


public class TilesFlowEditPolicy
	extends org.eclipse.gef.editpolicies.FlowLayoutEditPolicy
{

protected Command createAddCommand(EditPart child, EditPart after) {
	return null;
}

protected Command createMoveChildCommand(EditPart child, EditPart after) {
	return null;
}

protected Command getCreateCommand(CreateRequest request) {
	return null;
}

protected Command getDeleteDependantCommand(Request request) {
	return null;
}

protected Command getOrphanChildrenCommand(Request request) {
	return null;
}
protected boolean isHorizontal() {
	return false;
}

protected EditPolicy createChildEditPolicy(EditPart child){
	return new JSFNonResizableEditPolicy();
}

protected void showLayoutTargetFeedback(Request request) {
	if (getHost().getChildren().size() == 0)
		return;
	Polyline fb = getLineFeedback();
	Transposer transposer = new Transposer();
	transposer.setEnabled(!isHorizontal());
	
	boolean before = true;
	int epIndex = getFeedbackIndexFor(request);
	Rectangle r = null;
	if (epIndex == -1) {
		before = false;
		epIndex = getHost().getChildren().size() - 1;
		EditPart editPart = (EditPart) getHost().getChildren().get(epIndex);
		r = transposer.t(getAbsoluteBounds((GraphicalEditPart)editPart));
	} else {
		EditPart editPart = (EditPart) getHost().getChildren().get(epIndex);
		r = transposer.t(getAbsoluteBounds((GraphicalEditPart)editPart));
		Point p = transposer.t(getLocationFromRequest(request));
		if (p.x <= r.x + (r.width / 2))
			before = true;
		else {
			before = false;
			epIndex--;
			editPart = (EditPart) getHost().getChildren().get(epIndex);
			r = transposer.t(getAbsoluteBounds((GraphicalEditPart)editPart));
		}
	}
	int x = Integer.MIN_VALUE;
	if (before) {
		if (epIndex > 0) {
			// Need to determine if a line break.
			Rectangle boxPrev = transposer.t(
				getAbsoluteBounds(
					(GraphicalEditPart) getHost().getChildren().get(epIndex - 1)));
			int prevRight = boxPrev.right();
			if (prevRight < r.x) {
				// Not a line break
				x = prevRight + (r.x - prevRight) / 2;
			} else if (prevRight == r.x) {
				x = prevRight + 1;
			}
		}
		if (x == Integer.MIN_VALUE) {
			// It is a line break.
			Rectangle parentBox = transposer.t(
				getAbsoluteBounds((GraphicalEditPart)getHost()));
			x = r.x - 5;
			if (x < parentBox.x)
				x = parentBox.x + (r.x - parentBox.x) / 2;
		}
	} else {
		Rectangle parentBox = transposer.t(
			getAbsoluteBounds((GraphicalEditPart)getHost()));
		int rRight = r.x + r.width;
		int pRight = parentBox.x + parentBox.width;
		x = rRight + 5;
		if (x > pRight)
			x = rRight + (pRight - rRight) / 2;
	}
	Point p1 = new Point(x, r.y - 4);
	p1 = transposer.t(p1);
	fb.translateToRelative(p1);
	Point p2 = new Point(x, r.y + r.height + 4);
	p2 = transposer.t(p2);
	fb.translateToRelative(p2);
	fb.setPoint(p1, 0);
	fb.setPoint(p2, 1);
}

private Point getLocationFromRequest(Request request) {
	return ((DropRequest)request).getLocation();
}

private Rectangle getAbsoluteBounds(GraphicalEditPart ep) {
	Rectangle bounds = ep.getFigure().getBounds().getCopy();
	ep.getFigure().translateToAbsolute(bounds);
	return bounds;
}

class JSFNonResizableEditPolicy extends NonResizableEditPolicy{
	public JSFNonResizableEditPolicy(){
		super();
	}
	
	public List createSelectionHandles(){
		return Collections.EMPTY_LIST;
	}

}

}
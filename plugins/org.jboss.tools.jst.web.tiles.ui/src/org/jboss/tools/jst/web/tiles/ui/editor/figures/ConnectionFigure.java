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
package org.jboss.tools.jst.web.tiles.ui.editor.figures;

import org.eclipse.draw2d.AnchorListener;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;

import org.jboss.tools.jst.web.tiles.ui.editor.edit.LinkEditPart;
import org.jboss.tools.jst.web.tiles.ui.editor.model.ILink;

public class ConnectionFigure extends PolylineConnection implements Connection, AnchorListener{
	private boolean manual = false;

	private ILink link = null;

	private int oldStartPointX = 0, oldStartPointY = 0, oldEndPointX = 0,
			oldEndPointY = 0;

	public ConnectionFigure(LinkEditPart part) {
		super();
		link = part.getLinkModel();
	}

	public ILink getLinkModel() {
		return link;
	}

	public ConnectionFigure() {
		super();
	}

	public void setOldPoints(Point start, Point end) {
		oldStartPointX = start.x;
		oldStartPointY = start.y;
		oldEndPointX = end.x;
		oldEndPointY = end.y;
	}

	public Point getOldStartPoint() {
		return new Point(oldStartPointX, oldStartPointY);
	}

	public Point getOldEndPoint() {
		return new Point(oldEndPointX, oldEndPointY);
	}

	public void setManual(boolean flag) {
		manual = flag;
	}

	public boolean isManual() {
		return manual;
	}

	PointList points;

	Point point;

	Point beg = new Point(0, 0), end = new Point(0, 0);

	Point corner = new Point(0, 0);

	boolean upFlag, rightFlag;

	boolean horiz;

	protected void outlineShape(Graphics g) {
		points = getPoints();
		point = points.getPoint(0);
		beg.x = point.x;
		beg.y = point.y;

		if (points.getFirstPoint().y == points.getLastPoint().y) {
			super.outlineShape(g);
			return;
		}

		for (int i = 1; i < points.size(); i++) {
			point = points.getPoint(i);
			end.x = point.x;
			end.y = point.y;

			if (beg.y == end.y)
				horiz = true;
			else
				horiz = false;

			if (i != 1) {
				if (horiz) {
					if (end.x > beg.x) {
						corner.x = beg.x + 1;
						beg.x += 4;
						rightFlag = true;
					} else {
						corner.x = beg.x - 1;
						beg.x -= 4;
						rightFlag = false;
					}
				} else {
					if (end.y > beg.y) {
						corner.y = beg.y + 1;
						beg.y += 4;
						upFlag = false;
					} else {
						corner.y = beg.y - 1;
						beg.y -= 4;
						upFlag = true;
					}
				}
			}
			if (corner.x != 0) {
				if (upFlag) {
					g.drawLine(corner.x, corner.y - 1, corner.x, corner.y - 2);
				} else {
					g.drawLine(corner.x, corner.y + 1, corner.x, corner.y + 2);
				}
				if (rightFlag) {
					g.drawLine(corner.x + 1, corner.y, corner.x + 2, corner.y);
				} else {
					g.drawLine(corner.x - 1, corner.y, corner.x - 2, corner.y);
				}
			}
			corner.x = 0;

			if (i != points.size() - 1) {
				if (horiz) {
					if (end.x > beg.x) {
						corner.x = end.x - 1;
						end.x -= 4;
						rightFlag = false;

					} else {
						corner.x = end.x + 1;
						end.x += 4;
						rightFlag = true;
					}
				} else {
					if (end.y > beg.y) {
						corner.y = end.y - 1;
						end.y -= 4;
						upFlag = true;
					} else {
						corner.y = end.y + 1;
						end.y += 4;
						upFlag = false;
					}
				}
			}

			g.drawLine(beg, end);
			point = points.getPoint(i);
			beg.x = point.x;
			beg.y = point.y;
		}
	}

}

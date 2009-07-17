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
import java.util.*;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;
import org.jboss.tools.common.model.ui.dnd.DnDUtil;
import org.eclipse.gef.*;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.handles.RelativeHandleLocator;
import org.eclipse.gef.handles.SquareHandle;
import org.eclipse.gef.requests.DropRequest;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.graphics.Cursor;

import org.jboss.tools.common.meta.action.XAction;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.jst.web.tiles.ui.TilesUIPlugin;
import org.jboss.tools.jst.web.tiles.ui.editor.figures.DefinitionFigure;
import org.jboss.tools.jst.web.tiles.ui.editor.model.IDefinition;
import org.jboss.tools.jst.web.tiles.ui.editor.model.IDefinitionListener;
import org.jboss.tools.jst.web.tiles.ui.editor.model.ILink;


public class DefinitionEditPart
	extends TilesEditPart implements PropertyChangeListener, IDefinitionListener, EditPartListener {
	/**
	 * @see org.eclipse.gef.EditPart#deactivate()
	 */
	public void doDoubleClick(boolean cf) {
		try {
			XAction action = DnDUtil.getEnabledAction(
					(XModelObject) getDefinitionModel().getSource(), null,
					"Open"); //$NON-NLS-1$
			if (action != null)
				action.executeHandler((XModelObject) getDefinitionModel()
						.getSource(), null);
		} catch (Exception e) {
			TilesUIPlugin.getPluginLog().logError(e);
		}
	}

	public void deactivate() {
		getDefinitionModel().removePropertyChangeListener(this);
		getDefinitionModel().removeDefinitionListener(this);
		super.deactivate();
	}

	public void definitionChange() {
		refresh();
		fig.refreshFont();
		fig.repaint();
	}

	public boolean isDefinitionListenerEnable() {
		return true;
	}

	private DefinitionFigure fig = null;

	public void doControlUp() {

	}

	public void doControlDown() {

	}

	public void doMouseHover(boolean cf) {
	}

	public void setModel(Object model) {
		super.setModel(model);
		((IDefinition) model).addPropertyChangeListener(this);
		((IDefinition) model).addDefinitionListener(this);
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
	}

	public void propertyChange(PropertyChangeEvent evt) {
	}

	public void linkAdd(ILink link) {
		refreshTargetLink(link);
		refresh();
		List editParts = getSourceConnections();
		for (int i = 0; i < editParts.size(); i++) {
			((ConnectionEditPart) editParts.get(i)).refresh();
		}
		getFigure().getParent().getLayoutManager().layout(
				getFigure().getParent());
	}

	private void refreshTargetLink(ILink link) {
		if (link == null)
			return;
		DefinitionEditPart gep = (DefinitionEditPart) getViewer()
				.getEditPartRegistry().get(link.getToDefinition());
		if (gep == null)
			return;

		gep.refreshTargetConnections();
		gep.figure.repaint();
	}

	public void linkRemove(ILink link) {
		refresh();

		refreshTargetLink(link);
		refresh();
		getFigure().getParent().getLayoutManager().layout(
				getFigure().getParent());
	}

	public void linkChange(ILink link, PropertyChangeEvent evet) {
	}

	protected AccessibleEditPart createAccessible() {
		return new AccessibleGraphicalEditPart() {

			public void getName(AccessibleEvent e) {
				e.result = "EditPart"; //$NON-NLS-1$
			}

			public void getValue(AccessibleControlEvent e) {
			}

		};
	}

	protected List getModelTargetConnections() {
		return getDefinitionModel().getVisibleInputLinks();
	}

	protected List getModelSourceConnections() {
		if (getDefinitionModel().getLink() == null)
			return Collections.EMPTY_LIST;
		return Collections.singletonList(getDefinitionModel().getLink());// getDefinitionModel().getListOutputLinks();
	}

	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.NODE_ROLE, null);
		TilesNonResizableEditPolicy p = new TilesNonResizableEditPolicy();
		p.setDragAllowed(false);
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, p);
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new DefinitionEditPolicy());
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE,
				new DefinitionEditPolicy());
	}

	/**
	 * Returns a newly created Figure to represent this.
	 * 
	 * @return Figure of this.
	 */

	// protected ChopboxAnchor ca;
	protected IFigure createFigure() {
		fig = new DefinitionFigure(getDefinitionModel());
		((DefinitionFigure) fig).setGroupEditPart(this);
		return fig;
	}

	public DefinitionFigure getDefinitionFigure() {
		return (DefinitionFigure) getFigure();
	}

	/**
	 * Returns the model of this as a LED.
	 * 
	 * @return Model of this as an LED.
	 */
	public IDefinition getDefinitionModel() {
		return (IDefinition) getModel();
	}

	public ConnectionAnchor getTargetConnectionAnchor(
			ConnectionEditPart connEditPart) {
		ConnectionAnchor anc = getNodeFigure().getConnectionAnchor("1_IN"); //$NON-NLS-1$
		return anc;
	}

	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		Point pt = new Point(((DropRequest) request).getLocation());
		return getNodeFigure().getTargetConnectionAnchorAt(pt);
	}

	public ConnectionAnchor getSourceConnectionAnchor(
			ConnectionEditPart connEditPart) {
		return getNodeFigure().getConnectionAnchor("1_OUT"); //$NON-NLS-1$
	}

	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		Point pt = new Point(((DropRequest) request).getLocation());
		return getNodeFigure().getSourceConnectionAnchorAt(pt);
	}

	protected List getModelChildren() {
		return Collections.EMPTY_LIST;
	}

	class TilesNonResizableEditPolicy extends NonResizableEditPolicy {
		public TilesNonResizableEditPolicy() {
			super();
		}

		protected List createSelectionHandles() {
			List<Handle> list = new ArrayList<Handle>();
			list.add(createHandle((GraphicalEditPart) getHost(),
					PositionConstants.SOUTH_EAST));
			list.add(createHandle((GraphicalEditPart) getHost(),
					PositionConstants.SOUTH_WEST));
			list.add(createHandle((GraphicalEditPart) getHost(),
					PositionConstants.NORTH_WEST));
			list.add(createHandle((GraphicalEditPart) getHost(),
					PositionConstants.NORTH_EAST));

			return list;
		}

		Handle createHandle(GraphicalEditPart owner, int direction) {
			SelectHandle handle = new SelectHandle(owner, direction);
			return handle;
		}

	}

	public class SelectHandle extends SquareHandle {
		public SelectHandle(GraphicalEditPart owner, int direction) {
			setOwner(owner);
			setLocator(new RelativeHandleLocator(owner.getFigure(), direction));
			setCursor(SharedCursors.NO);
		}

		public SelectHandle(GraphicalEditPart owner, Locator loc, Cursor c) {
			super(owner, loc, c);
		}

		protected DragTracker createDragTracker() {
			return null;
		}
	}

}

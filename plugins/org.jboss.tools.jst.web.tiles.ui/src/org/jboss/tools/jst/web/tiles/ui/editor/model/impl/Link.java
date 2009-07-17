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
package org.jboss.tools.jst.web.tiles.ui.editor.model.impl;

import java.util.*;
import java.util.List;

import org.eclipse.swt.widgets.*;

import org.jboss.tools.common.model.*;
import org.jboss.tools.jst.web.tiles.ui.editor.model.IDefinition;
import org.jboss.tools.jst.web.tiles.ui.editor.model.ILink;
import org.jboss.tools.jst.web.tiles.ui.editor.model.ILinkListener;
import org.jboss.tools.jst.web.tiles.ui.editor.model.ITilesElement;

public class Link extends TilesElement implements ILink {

	public static final String PATH_PROPERTY = "link shape"; //$NON-NLS-1$
	public static final String HIDDEN_PROPERTY = "hidden"; //$NON-NLS-1$
	List<ILinkListener> linkListeners = new Vector<ILinkListener>();
	XModelObject target = null;
	IDefinition toDefinition;

	public void dispose() {
		super.dispose();
		if (linkListeners != null)
			linkListeners.clear();
		linkListeners = null;
	}

	public Link(ITilesElement parent, XModelObject source) {
		super(parent, source);
	}

	public boolean setTarget() {
		target = getTilesModel().getHelper().getItemOutputTarget(
				(XModelObject) getSource());
		if (target == null) {
			return false;
		}
		if (target.getPath() == null) {
			target = null;
			return false;
		}
		toDefinition = (IDefinition) tilesModel.findElement(target.getPath());
		if (toDefinition == null) {
			target = null;
			return false;
		}
		if (toDefinition != null) {
			((Definition) toDefinition).addInputLink(this);
			((Definition) getParentTilesElement()).fireLinkAdd(this);
		}

		return true;
	}

	public XModelObject getTargetModel() {
		return target;
	}

	public Menu getPopupMenu(Control control, Object environment) {
		if (getSource() == null)
			return null;
		return null;
	}

	public IDefinition getToDefinition() {
		return toDefinition;
	}

	public IDefinition getFromDefinition() {
		return (Definition) getParentTilesElement();
	}

	public void addLinkListener(ILinkListener l) {
		linkListeners.add(l);
	}

	public void removeLinkListener(ILinkListener l) {
		linkListeners.remove(l);
	}

	public void fireLinkChange() {
		List<ILinkListener> targets = new ArrayList<ILinkListener>();
		targets.addAll(linkListeners);
		for (int i = 0; i < targets.size(); i++) {
			ILinkListener listener = (ILinkListener) targets.get(i);
			if (listener != null)
				listener.linkChange(this);
		}
	}

	public void fireLinkRemove() {
		List<ILinkListener> targets = new ArrayList<ILinkListener>();
		targets.addAll(linkListeners);
		for (int i = 0; i < targets.size(); i++) {
			ILinkListener listener = (ILinkListener) targets.get(i);
			if (listener != null)
				listener.linkRemove(this);
		}
		((TilesModel) getTilesModel()).fireLinkRemove(this);
	}

	public void nodeChanged(Object eventData) {
		XModelObject newTarget = getTilesModel().getHelper()
				.getItemOutputTarget((XModelObject) getSource());
		if (target == null && newTarget != null) {
			((Definition) getFromDefinition()).addLink(this);
		} else if (target != newTarget) {
			((Definition) getFromDefinition()).removeLink(this);
			((Definition) getFromDefinition()).addLink(this);
		} else if (newTarget == null) {
			target = null;
		}
	}

}

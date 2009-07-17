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

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.event.XModelTreeEvent;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.web.tiles.ui.editor.model.IDefinition;
import org.jboss.tools.jst.web.tiles.ui.editor.model.IDefinitionListener;
import org.jboss.tools.jst.web.tiles.ui.editor.model.ILink;
import org.jboss.tools.jst.web.tiles.ui.editor.model.ITilesElement;
import org.jboss.tools.jst.web.tiles.ui.editor.model.ITilesElementList;
import org.jboss.tools.jst.web.tiles.ui.editor.model.ITilesModel;

public class Definition extends TilesElement implements IDefinition{
	private boolean expanded = true;
	private boolean hidden = false;
	
	public void collapse() {
		expanded = false;
		ILink link;
		for (int i = 0; i < inputLinks.size(); i++) {
			link = (ILink) inputLinks.get(i);
			((Definition) link.getFromDefinition()).fireLinkRemove(link, 0);
			link.getFromDefinition().hide();
		}
	}

	public void hide() {
		hidden = true;
		expanded = true;
		ILink link;
		for (int i = 0; i < inputLinks.size(); i++) {
			link = (ILink) inputLinks.get(i);

			link.getFromDefinition().hide();
			((Definition) link.getFromDefinition()).fireLinkRemove(link, 0);
		}
		getTilesModel().setHidden(this);
		((TilesModel) getTilesModel()).fireDefinitionRemove(this, 0);
	}

	public void expand() {
		expanded = true;
		ILink link;
		for (int i = 0; i < inputLinks.size(); i++) {
			link = (ILink) inputLinks.get(i);
			link.getFromDefinition().visible();
		}

		for (int i = 0; i < inputLinks.size(); i++) {
			link = (ILink) inputLinks.get(i);
			((Definition) link.getFromDefinition()).fireLinkAdd(link);
		}
	}

	public void visible() {
		hidden = false;
		ILink link;
		getTilesModel().setVisible(this);
		((TilesModel) getTilesModel()).fireDefinitionAdd(this);
		for (int i = 0; i < inputLinks.size(); i++) {
			link = (ILink) inputLinks.get(i);

			link.getFromDefinition().visible();
		}
		for (int i = 0; i < inputLinks.size(); i++) {
			link = (ILink) inputLinks.get(i);

			((Definition) link.getFromDefinition()).fireLinkAdd(link);
		}
	}

	public List getVisibleInputLinks() {
		if (expanded && !hidden)
			return inputLinks;
		else
			return Collections.EMPTY_LIST;
	}

	public boolean isExpanded() {
		if (inputLinks.size() > 0)
			return expanded;
		else
			return false;
	}

	public boolean isCollapsed() {
		return !expanded;
	}

	public boolean hasErrors() {
		return getTilesModel().getHelper()
				.hasErrors((XModelObject) getSource());
	}

	public boolean isHidden() {
		return hidden;
	}

	public static final Color DEFAULT_FOREGROUND_COLOR = new Color(null, 0x00,
			0x00, 0x00);
	public static final Color DEFAULT_BACKGROUND_COLOR = new Color(null, 0xE4,
			0xE4, 0xE4);
	protected Color headerForegroundColor = DEFAULT_FOREGROUND_COLOR;
	protected Color headerBackgroundColor = DEFAULT_BACKGROUND_COLOR;
	List<IDefinitionListener> definitionListeners = new Vector<IDefinitionListener>();
	List<ILink> inputLinks = new Vector<ILink>();
	public ILink link = null;

	public void dispose() {
		super.dispose();
		if (definitionListeners != null)
			definitionListeners.clear();
		definitionListeners = null;
		if (inputLinks != null)
			inputLinks.clear();
		inputLinks = null;
	}

	public void addInputLink(ILink link) {
		if (!inputLinks.contains(link))
			inputLinks.add(link);
	}

	public void removeInputLink(ILink link) {
		inputLinks.remove(link);
	}

	protected Image icon = null;

	public Definition(ITilesModel model, XModelObject groupNode) {
		super(model, groupNode);
		icon = EclipseResourceUtil.getImage(groupNode);
		XModelObject[] objects = model.getHelper().getOutputs(groupNode);
		if (objects.length > 0) {
			link = new Link(this, objects[0]);
		}
	}

	public Image getImage() {
		return icon;
	}

	public String getExtends() {
		if (link != null && link.getToDefinition() != null) {
			return link.getToDefinition().getName();
		}
		return ""; //$NON-NLS-1$
	}

	boolean selected = false;

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean set) {
		boolean oldValue = selected;
		selected = set;
		this.propertyChangeSupport
				.firePropertyChange("selected", oldValue, set); //$NON-NLS-1$
		if (set)
			this.getTilesModel().setSelectedDefinition(this);
	}

	public void clearSelection() {
		ITilesElementList list = getTilesModel().getDefinitionList();
		for (int i = 0; i < list.size(); i++) {
			IDefinition activity = (IDefinition) list.get(i);
			activity.setSelected(false);
		}
	}

	public ILink[] getLinks() {
		return getInputLinks();
	}

	public ILink[] getInputLinks() {
		return (ILink[]) inputLinks.toArray();
	}

	public List<ILink> getListInputLinks() {
		return inputLinks;
	}

	public ILink getLink() {
		return link;
	}

	// Unit messages
	public void fireDefinitionChange() {
		if (definitionListeners.size() == 0)
			return;
		List<IDefinitionListener> targets = new ArrayList<IDefinitionListener>();
		targets.addAll(definitionListeners);
		for (int i = 0; i < targets.size(); i++) {
			IDefinitionListener listener = (IDefinitionListener) targets.get(i);
			if (listener != null) {
				listener.definitionChange();
			}
		}
	}

	public void fireLinkAdd(ILink link) {
		List<IDefinitionListener> listeners = new ArrayList<IDefinitionListener>();
		listeners.addAll(definitionListeners);
		for (int i = 0; i < listeners.size(); i++) {
			IDefinitionListener listener = (IDefinitionListener) listeners
					.get(i);
			if (listener != null && listener.isDefinitionListenerEnable())
				((IDefinitionListener) listeners.get(i)).linkAdd(link);
		}
	}

	public void fireLinkRemove(ILink link, int index) {
		List<IDefinitionListener> listeners = new ArrayList<IDefinitionListener>();
		listeners.addAll(definitionListeners);
		for (int i = 0; i < listeners.size(); i++) {
			IDefinitionListener listener = (IDefinitionListener) listeners
					.get(i);
			if (listener != null && listener.isDefinitionListenerEnable())
				((IDefinitionListener) listeners.get(i)).linkRemove(link);
		}
	}

	// remove state from model

	public void addDefinitionListener(IDefinitionListener listener) {
		definitionListeners.add(listener);
	}

	public void removeDefinitionListener(IDefinitionListener listener) {
		definitionListeners.remove(listener);
	}

	public void removeFromTilesModel() {
	}

	public void remove() {
		if (link != null) {
			ILink l = link;
			link.getToDefinition().removeInputLink(link);
			link.remove();
			link = null;
			fireLinkRemove(l, 0);
		}
	}

	public void nodeChanged(Object eventData) {
		fireDefinitionChange();
		this.propertyChangeSupport.firePropertyChange("name", "", this //$NON-NLS-1$ //$NON-NLS-2$
				.getSourceProperty("name")); //$NON-NLS-1$
	}

	public void addLink(ILink link) {
		this.link = link;
		if (!link.setTarget())
			link = null;
	}

	public void removeLink(ILink lin) {
		ILink l = link;
		link.getToDefinition().removeInputLink(link);
		link.remove();
		link = null;
		fireLinkRemove(l, 0);
	}

	public void structureChanged(Object eventData) {
	}

	public void nodeAdded(Object eventData) {
		XModelObject[] objects = getTilesModel().getHelper().getOutputs(source);
		if (objects.length > 0 && link == null) {
			link = new Link(this, objects[0]);
			if (!link.setTarget())
				link = null;
		}
	}

	public void nodeRemoved(Object eventData) {
		XModelTreeEvent event = (XModelTreeEvent) eventData;
		ITilesElement removedLink = this.getFromMap(event.getInfo());
		if (removedLink == link) {
			ILink l = link;
			link.getToDefinition().removeInputLink(link);
			link.remove();
			link = null;
			fireLinkRemove(l, 0);
		}
	}

	public boolean isElementListListenerEnable() {
		return true;
	}

	public void setElementListListenerEnable(boolean set) {

	}

	public boolean isConfirmed() {
		return !tilesModel.getHelper().isUnconfirmedItem(source);
	}

	public boolean isAnotherTiles() {
		return tilesModel.getHelper().isNotDefinedInThisFile(source);
	}

}

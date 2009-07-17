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
import org.jboss.tools.common.model.ui.action.*;

import java.beans.*;

import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Control;

import org.jboss.tools.common.model.*;
import org.jboss.tools.common.meta.*;
import org.jboss.tools.jst.web.tiles.ui.editor.model.ITilesElement;
import org.jboss.tools.jst.web.tiles.ui.editor.model.ITilesModel;



public class TilesElement implements ITilesElement{

	protected String name = ""; //$NON-NLS-1$
	protected boolean visible = false;
	protected boolean hidden = false;
	protected boolean deleted = false;
	protected ITilesElement parent;
	protected ITilesModel tilesModel;
	protected String iconPath;

	protected PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
			this);
	protected VetoableChangeSupport vetoableChangeSupport = new VetoableChangeSupport(
			this);
	protected XModelObject source;
	protected Hashtable map = new Hashtable();

	public TilesElement() {
	}

	public TilesElement(ITilesElement parent) {
		this.parent = parent;
		tilesModel = getTilesModel();
	}

	public TilesElement(ITilesElement parent, XModelObject source) {
		this.parent = parent;
		this.source = source;
		tilesModel = getTilesModel();
		((TilesModel) tilesModel).putToMap(source.getPath(), this);
	}

	public void dispose() {
		vetoableChangeSupport = null;
		propertyChangeSupport = null;
		if (map != null)
			map.clear();
		map = null;
	}

	public Object getSource() {
		return source;
	}

	public void setSource(Object obj) {
		source = (XModelObject) obj;
	}

	public String getText() {
		return ""; //$NON-NLS-1$
	}

	public ITilesElement getRoot() {
		ITilesElement current = this;
		while (current.getParentTilesElement() != null) {
			current = current.getParentTilesElement();
		}
		return current;
	}

	public ITilesModel getTilesModel() {
		ITilesElement model = getRoot();
		if (model instanceof ITilesElement) {
			return (ITilesModel) model;
		}
		return null;
	}

	public String getJSFElementPath() {
		ITilesElement current = this;
		String path = current.getName();
		while (current.getParentTilesElement() != null) {
			current = current.getParentTilesElement();
			path = current.getName() + "/" + path; //$NON-NLS-1$
		}
		return path;
	}

	public void updateModelModifiedProperty(Object oldValue, Object newValue) {
		if (getTilesModel() != null) {
			if (!oldValue.equals(newValue)) {
				getTilesModel().setModified(true);
			}
		}
	}

	public void updateModelModifiedProperty(int oldValue, int newValue) {
		if (getTilesModel() != null) {
			if (newValue != oldValue)
				;
			getTilesModel().setModified(true);
		}
	}

	public void updateModelModifiedProperty(boolean oldValue, boolean newValue) {
		if (getTilesModel() != null) {
			if (newValue != oldValue)
				;
			getTilesModel().setModified(true);
		}
	}

	public ITilesElement getParentTilesElement() {
		return parent;
	}

	public void setParentTilesElement(ITilesElement element) {
		ITilesElement oldValue = parent;
		parent = element;
		tilesModel = getTilesModel();
		propertyChangeSupport.firePropertyChange("parent", oldValue, element); //$NON-NLS-1$
		updateModelModifiedProperty(oldValue, element);
	}

	public String getName() {
		return source.getAttributeValue(NAME_PROPERTY);
	}

	public String getTarget() {
		return source.getAttributeValue(TARGET_PROPERTY);
	}

	public void setName(String name) throws PropertyVetoException {
		String oldValue = this.name;
		vetoableChangeSupport.fireVetoableChange("name", oldValue, name); //$NON-NLS-1$
		this.name = name;
	}

	public void setSourceProperty(String name, Object value) {
	}

	public Object getSourceProperty(String name) {
		return source.getAttributeValue(name);
	}

	public Object getSourceProperty(int index) {
		return null;
	}

	public int getSourcePropertyCounter() {
		return 0;
	}

	public String[] getSourcePropertyNames() {
		XModelObject mobject = (XModelObject) source;
		XAttribute[] attributes = mobject.getModelEntity().getAttributes();
		String[] attributeNames = new String[attributes.length];
		for (int i = 0; i < attributeNames.length; i++) {
			attributeNames[i] = attributes[i].getName();
		}
		return attributeNames;
	}

	public String[] getSourcePropertyDisplayNames() {
		XModelObject mobject = (XModelObject) source;
		XAttribute[] attributes = mobject.getModelEntity().getAttributes();
		String[] attributeNames = new String[attributes.length];
		for (int i = 0; i < attributeNames.length; i++) {
			attributeNames[i] = attributes[i].getName();
		}
		return attributeNames;
	}

	public void remove() {
	}

	// Support for vetoable change

	public void addVetoableChangeListener(VetoableChangeListener l) {
		vetoableChangeSupport.addVetoableChangeListener(l);
	}

	public void removeVetoableChangeListener(VetoableChangeListener l) {
		vetoableChangeSupport.removeVetoableChangeListener(l);
	}

	public void addVetoableChangeListener(String propertyName,
			VetoableChangeListener l) {
		vetoableChangeSupport.addVetoableChangeListener(propertyName, l);
	}

	public void removeVetoableChangeListener(String propertyName,
			VetoableChangeListener l) {
		vetoableChangeSupport.removeVetoableChangeListener(propertyName, l);
	}

	//Support for unvetoable change

	public void addPropertyChangeListener(PropertyChangeListener l) {
		propertyChangeSupport.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		propertyChangeSupport.removePropertyChangeListener(l);
	}

	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener l) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, l);
	}

	public void removePropertyChangeListener(String propertyName,
			PropertyChangeListener l) {
		propertyChangeSupport.removePropertyChangeListener(propertyName, l);
	}

	public Object clone() {
		TilesElement newElement = new TilesElement();
		newElement.source = source.copy();
		return newElement;
	}

	public Enumeration children() {
		return null;
	}

	public boolean isLeaf() {
		return true;
	}

	public void removeAllListeners() {
		this.propertyChangeSupport = new PropertyChangeSupport(this);
	}

	public void structureChanged(Object eventData) {

	}

	public void nodeChanged(Object eventData) {

	}

	public void nodeAdded(Object eventData) {

	}

	public void nodeRemoved(Object eventData) {

	}

	public TilesModel.TilesHashtable getMap() {
		return ((TilesModel) getTilesModel()).getMap();
	}

	public void removeFromMap(Object key) {
		((TilesModel) getTilesModel()).removeFromMap(key);
	}

	public ITilesElement getFromMap(Object key) {
		return ((TilesModel) getTilesModel()).getFromMap(key);
	}

	public Menu getPopupMenu(Control control, Object environment) {
		if (getSource() == null)
			return null;
		if (((XModelObject) getSource()).getModelEntity().getActionList()
				.getActionItems().length != 0) {
			XModelObjectActionList l = new XModelObjectActionList(
					((XModelObject) getSource()).getModelEntity()
							.getActionList(), ((XModelObject) getSource()),
					null, environment);

			Menu menu = l.createMenu(control);
			return menu;
		}
		return null;
	}

	public Menu getPopupMenu(Control control) {
		return getPopupMenu(control, null);
	}

	public boolean isConfirmed() {
		return false;
	}

}

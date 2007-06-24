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

import org.xml.sax.*;

import java.beans.*;

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Control;

import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.event.*;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;
import org.jboss.tools.jst.web.tiles.TilesPreference;
import org.jboss.tools.jst.web.tiles.model.helpers.TilesStructureHelper;
import org.jboss.tools.jst.web.tiles.ui.editor.model.IDefinition;
import org.jboss.tools.jst.web.tiles.ui.editor.model.ILink;
import org.jboss.tools.jst.web.tiles.ui.editor.model.ITilesElement;
import org.jboss.tools.jst.web.tiles.ui.editor.model.ITilesElementList;
import org.jboss.tools.jst.web.tiles.ui.editor.model.ITilesModel;
import org.jboss.tools.jst.web.tiles.ui.editor.model.ITilesModelListener;
import org.jboss.tools.jst.web.tiles.ui.editor.model.ITilesOptions;
import org.jboss.tools.jst.web.tiles.ui.preferences.TilesEditorTabbedPreferencesPage;

import org.jboss.tools.common.model.plugin.ModelPlugin;
import org.jboss.tools.common.model.ui.action.*;
import org.jboss.tools.common.model.ui.util.ModelUtilities;
import org.jboss.tools.common.model.util.XModelTreeListenerSWTSync;

public class TilesModel extends TilesElement implements ITilesModel, PropertyChangeListener, XModelTreeListener {
	List<IDefinition> visibleDefinitions = new Vector<IDefinition>();
	static final int DEFAULT_VERTICAL_SPACING = 20;
	static final int DEFAULT_HORIZONTAL_SPACING = 185;
	
	public List<IDefinition> getVisibleDefinitionList() {
		return visibleDefinitions;
	}

	public void setHidden(IDefinition definition) {
		visibleDefinitions.remove(definition);
		fireDefinitionRemove(definition, 0);
	}

	public void setVisible(IDefinition definition) {
		visibleDefinitions.add(definition);
		fireDefinitionAdd(definition);
	}

	protected List<ITilesModelListener> strutsModelListeners = new Vector<ITilesModelListener>();
	protected TilesHashtable map = new TilesHashtable();
	protected TilesElementList definitionList = new DefinitionList();
	protected TilesStructureHelper helper = TilesStructureHelper.instance;
	protected TilesOptions options;
	protected boolean modified = false;

	// ------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------

	public TilesModel() {
		try {
			setName(WebUIMessages.STRUTS_MODEL);
		} catch (Exception ex) {
			ModelPlugin.log(ex);
		}
	}

	public void dispose() {
		this.disconnectFromModel();
		if (map != null)
			map.dispose();
		map = null;
		if (strutsModelListeners != null)
			strutsModelListeners.clear();
		strutsModelListeners = null;
		if (definitionList != null)
			definitionList.dispose();
		definitionList = null;
		if (options != null)
			options.dispose();
		options = null;
	}

	public boolean isBorderPaint() {
		return false;
	}

	public ITilesOptions getOptions() {
		return options;
	}

	public TilesModel(Object data) throws SAXException, Exception {
		this();
		setData(data);
		map.setData((XModelObject) data);
	}

	public void updateLinks() {
		IDefinition definition;

		for (int i = 0; i < getDefinitionList().size(); i++) {
			definition = (IDefinition) getDefinitionList().get(i);
			if (definition.getLink() != null)
				((ILink) definition.getLink()).setTarget();
		}
	}

	public Object get(String name) {
		return null;
	}

	public void put(String name, Object value) {

	}

	public TilesStructureHelper getHelper() {
		return helper;
	}

	public int getProcessItemCounter() {
		return definitionList.size();
	}

	public IDefinition getDefinition(int index) {
		return (IDefinition) definitionList.get(index);
	}

	public IDefinition getDefinition(String groupName) {
		return (IDefinition) definitionList.get(groupName);
	}

	public IDefinition getDefinition(Object source) {
		IDefinition[] is = (IDefinition[]) definitionList.elements
				.toArray(new IDefinition[0]);
		for (int i = 0; i < is.length; i++)
			if (is[i].getSource() == source)
				return is[i];
		return null;
	}

	// Module removers

	public void removeGroup(String moduleName) {
	}

	public void removeGroup(IDefinition removeProcessItem) {
	}

	public void propertyChange(PropertyChangeEvent pce) {
	}

	IDefinition selectedDefinition = null;

	public void setSelectedDefinition(IDefinition group) {
		IDefinition oldValue = selectedDefinition;
		selectedDefinition = group;
		propertyChangeSupport.firePropertyChange(
				"selectedProcessItem", oldValue, group); //$NON-NLS-1$
	}

	public IDefinition getSelectedDefinition() {
		return selectedDefinition;
	}

	public String getText() {
		return ""; //$NON-NLS-1$
	}

	XModelTreeListenerSWTSync listener = null;

	public void setData(Object data) throws Exception {
		source = helper.getProcess((XModelObject) data);
		if (source == null) {
			return;
		}
		//helper.autolayout(source);
		map.put(source.getPath(), this);
		definitionList = new DefinitionList(source);
		listener = new XModelTreeListenerSWTSync(this);
		source.getModel().addModelTreeListener(listener);
		options = new TilesOptions();
	}

	public void disconnectFromModel() {
		if (listener != null)
			source.getModel().removeModelTreeListener(listener);
		options.disconnectFromModel();
		map.disconnectFromModel();
	}

	public boolean isEditable() {
		return source != null
				&& source.getModelEntity().isEditable(source, "body"); //$NON-NLS-1$
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean set) {
		boolean oldValue = modified;
		modified = set;
		propertyChangeSupport
				.firePropertyChange("modified", oldValue, modified); //$NON-NLS-1$
	}

	// -----------------------------------------------------------------------
	// fire events
	// -----------------------------------------------------------------------

	public void fireProcessChanged() {
		if (strutsModelListeners == null)
			return;
		List<ITilesModelListener> targets = new ArrayList<ITilesModelListener>();
		targets.addAll(strutsModelListeners);
		for (int i = 0; i < targets.size(); i++) {
			ITilesModelListener listener = (ITilesModelListener) targets.get(i);
			if (listener != null) {
				listener.processChanged();
			}
		}
		setModified(true);
	}

	public void fireDefinitionAdd(IDefinition newDefinition) {
		List<ITilesModelListener> targets = new ArrayList<ITilesModelListener>();
		targets.addAll(strutsModelListeners);
		for (int i = 0; i < targets.size(); i++) {
			ITilesModelListener listener = (ITilesModelListener) targets.get(i);
			if (listener != null) {
				listener.definitionAdd(newDefinition);
			}
		}
		setModified(true);
	}

	public void fireDefinitionRemove(IDefinition newDefinition, int index) {
		List<ITilesModelListener> targets = new ArrayList<ITilesModelListener>();
		targets.addAll(strutsModelListeners);
		for (int i = 0; i < targets.size(); i++) {
			ITilesModelListener listener = (ITilesModelListener) targets.get(i);
			if (listener != null) {
				listener.definitionRemove(newDefinition);
			}
		}
		setModified(true);
	}

	public void fireLinkAdd(ILink newLink) {
		List<ITilesModelListener> targets = new ArrayList<ITilesModelListener>();
		targets.addAll(strutsModelListeners);
		for (int i = 0; i < targets.size(); i++) {
			ITilesModelListener listener = (ITilesModelListener) targets.get(i);
			if (listener != null) {
				listener.linkAdd(newLink);
			}
		}
		setModified(true);
	}

	public void fireLinkRemove(ILink newLink) {
		List<ITilesModelListener> targets = new ArrayList<ITilesModelListener>();
		targets.addAll(strutsModelListeners);
		for (int i = 0; i < targets.size(); i++) {
			ITilesModelListener listener = (ITilesModelListener) targets.get(i);
			if (listener != null) {
				listener.linkRemove(newLink);
			}
		}
		setModified(true);
	}

	public void addTilesModelListener(ITilesModelListener listener) {
		strutsModelListeners.add(listener);
	}

	public void removeTilesModelListener(ITilesModelListener listener) {
		if (strutsModelListeners != null)
			strutsModelListeners.remove(listener);
	}

	public void remove() {
	}

	public ITilesElementList getDefinitionList() {
		return definitionList;
	}

	public void nodeChanged(XModelTreeEvent event) {
		try {
			if (map == null)
				return;
			fireProcessChanged();
			ITilesElement element = (TilesElement) map.get(event.getInfo());
			if (element != null
					&& !event.getModelObject().getPath()
							.equals(event.getInfo())) {
				updateCash((String) event.getInfo());
			}
			String path = event.getModelObject().getPath();
			element = (path == null) ? null : (ITilesElement) map.get(path);
			if (element == null) {
				return;
			}
			element.nodeChanged(event);
		} catch (Exception x) {
			ModelPlugin.log("Error in processing model event", x);
		}
	}

	public void structureChanged(XModelTreeEvent event) {
		TilesElement element;
		try {
			Object obj = event.getModelObject().getPath();
			if (obj == null)
				return;
			if (map == null)
				return;
			element = (TilesElement) map.get(obj);
			if (element == null) {
				return;
			}
			if (event.kind() == XModelTreeEvent.STRUCTURE_CHANGED) {
				element.structureChanged(event);
			} else if (event.kind() == XModelTreeEvent.CHILD_ADDED) {
				element.nodeAdded(event);
			} else if (event.kind() == XModelTreeEvent.CHILD_REMOVED) {
				element.nodeRemoved(event);
			}
		} catch (Exception x) {
			ModelPlugin.log("Error in processing model event", x);
		}
	}

	public void putToMap(Object key, Object value) {
		getMap().put(key, value);
	}

	public void removeFromMap(Object key) {
		getMap().remove(key);
	}

	public ITilesElement getFromMap(Object key) {
		return getMap().get(key);
	}

	public class DefinitionPropertyChangeListener implements
			PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent event) {
			IDefinition processItem = (IDefinition) event.getSource();
			if (event.getPropertyName().equals("selected")) { //$NON-NLS-1$
				if (((Boolean) event.getNewValue()).booleanValue())
					setSelectedDefinition(processItem);
			}
		}
	}

	public class DefinitionList extends TilesElementList {
		protected DefinitionList() {
		}

		public DefinitionList(XModelObject processItemSource) {
			super(TilesModel.this, TilesModel.this.source);
			if (((XModelObject) TilesModel.this.getSource()).getPath() == null)
				return;

			XModelObject[] definitionNodeList = getHelper().getItems(
					processItemSource);

			for (int i = 0; i < definitionNodeList.length; i++) {
				IDefinition newDefinition = new Definition(TilesModel.this,
						definitionNodeList[i]);
				newDefinition.addPropertyChangeListener(
						"selected", new DefinitionPropertyChangeListener()); //$NON-NLS-1$
				add(newDefinition);
				visibleDefinitions.add(newDefinition);
			}
		}

		public void structureChanged(Object eventData) {
		}

		public void nodeAdded(Object eventData) {
			XModelTreeEvent event = (XModelTreeEvent) eventData;
			IDefinition newProcessItem = new Definition(TilesModel.this,
					((XModelObject) event.getInfo()));
			this.add(newProcessItem);
			visibleDefinitions.add(newProcessItem);
			fireDefinitionAdd(newProcessItem);
		}

		public void nodeRemoved(Object eventData) {
			XModelTreeEvent event = (XModelTreeEvent) eventData;
			ITilesElement removedProcessItem = this.getFromMap(event.getInfo());
			int index = this.indexOf(removedProcessItem);
			removedProcessItem.remove();
			this.remove(removedProcessItem);
			visibleDefinitions.remove(removedProcessItem);
			this.removeFromMap(((XModelTreeEvent) eventData).getInfo());
			fireDefinitionRemove((Definition) removedProcessItem, index);
			clearCash((String) event.getInfo());
		}
	}

	public TilesHashtable getMap() {
		return map;
	}

	public ITilesElement findElement(String key) {
		return map.get(key);
	}

	public class TilesHashtable implements XModelTreeListener {
		Hashtable<Object, Object> map = new Hashtable<Object, Object>();

		XModelObject source;

		String name;

		public void dispose() {
			disconnectFromModel();
			if (map != null)
				map.clear();
			map = null;
		}

		public void put(Object key, Object value) {
			map.put(key, value);
		}

		public void setData(XModelObject data) {
			source = data;
			source.getModel().addModelTreeListener(TilesHashtable.this);
			name = source.getAttributeValue("name"); //$NON-NLS-1$
		}

		public void disconnectFromModel() {
			source.getModel().removeModelTreeListener(TilesHashtable.this);
		}

		public ITilesElement get(Object key) {
			return (ITilesElement) map.get(key);
		}

		public void remove(Object key) {
			map.remove(key);
		}

		public void nodeChanged(XModelTreeEvent event) {
			String path;
			TilesElement element;

			if (!source.getAttributeValue("name").equals(name)) { //$NON-NLS-1$
				name = source.getAttributeValue("name"); //$NON-NLS-1$
				Enumeration keys = map.keys();
				while (keys.hasMoreElements()) {
					path = (String) keys.nextElement();
					element = (TilesElement) map.get(path);
					if (element != null) {
						if (element.getSource() != null) {
							map.remove(path);
							map.put(((XModelObject) element.getSource())
									.getPath(), element);
						}
					}
				}
			}
		}

		public void structureChanged(XModelTreeEvent event) {
		}

	}

	protected void clearCash(String path) {
		updateCash(path, true);
	}

	protected void updateCash(String path) {
		updateCash(path, false);
	}

	protected void updateCash(String path, boolean clear) {
		String rpath = path + "/"; //$NON-NLS-1$
		Object[] ks = map.map.keySet().toArray();
		for (int i = 0; i < ks.length; i++) {
			if (!ks[i].equals(path) && !ks[i].toString().startsWith(rpath))
				continue;
			ITilesElement n = (ITilesElement) map.map.get(ks[i]);
			map.map.remove(ks[i]);
			if (clear)
				continue;
			XModelObject o = (XModelObject) n.getSource();
			if (!o.isActive())
				continue;
			map.map.put(o.getPath(), n);
		}
	}

	public Menu getPopupMenu(Control control, Object environment) {
		if (source == null)
			return null;
		if (source.getModelEntity().getActionList().getActionItems().length != 0) {
			XModelObjectActionList l = new XModelObjectActionList(source
					.getModelEntity().getActionList(), source, null,
					environment);

			Menu menu = l.createMenu(control);
			return menu;
		}
		return null;
	}

	public Menu getPopupMenu(Control control) {
		return getPopupMenu(control, null);
	}

	public boolean isConfirmed() {
		return true;
	}

	class TilesOptions implements XModelTreeListener, ITilesOptions {

		XModelObject optionsObject = ModelUtilities.getPreferenceModel()
				.getByPath(TilesEditorTabbedPreferencesPage.TILES_EDITOR_PATH);

		XModelTreeListenerSWTSync optionsListener = new XModelTreeListenerSWTSync(
				this);

		Font definitionNameFont = null;

		public TilesOptions() {
			optionsObject.getModel().addModelTreeListener(optionsListener);
		}

		public void dispose() {
			disconnectFromModel();
			if (definitionNameFont != null && definitionNameFont.isDisposed())
				definitionNameFont.dispose();
			definitionNameFont = null;
		}

		public int getVerticalSpacing() {
			String str = TilesPreference.VERTICAL_SPACING.getValue();
			if (str == null)
				return DEFAULT_VERTICAL_SPACING;
			if (str.indexOf("default") >= 0)return DEFAULT_VERTICAL_SPACING; //$NON-NLS-1$
			try {
				return new Integer(str).intValue();
			} catch (Exception ex) {
				return DEFAULT_VERTICAL_SPACING;
			}
		}

		public int getHorizontalSpacing() {
			String str = TilesPreference.HORIZONTAL_SPACING.getValue();
			if (str == null)
				return DEFAULT_HORIZONTAL_SPACING;
			if (str.indexOf("default") >= 0)return DEFAULT_HORIZONTAL_SPACING; //$NON-NLS-1$
			try {
				return new Integer(str).intValue();
			} catch (Exception ex) {
				return DEFAULT_HORIZONTAL_SPACING;
			}
		}

		public String getAlignment() {
			String str = TilesPreference.TILES_ALIGNMENT.getValue();
			return str;
		}

		public boolean isAnimateLayout() {
			String str = TilesPreference.TILES_ANIMATION.getValue();
			if (str != null && str.equals("yes"))return true; //$NON-NLS-1$
			else
				return false;
		}

		public Font getDefinitionNameFont() {
			String name;
			int size = 8, style = 1;
			int pos, pos2, pos3;
			String str = TilesPreference.DEFINITION_NAME_FONT.getValue();
			pos = str.indexOf(","); //$NON-NLS-1$
			if (pos < 0)
				name = str;
			else {
				name = str.substring(0, pos);
				pos2 = str.indexOf("size="); //$NON-NLS-1$
				if (pos2 >= 0) {
					pos3 = str.indexOf(",", pos2); //$NON-NLS-1$
					if (pos3 < 0)
						size = new Integer(str
								.substring(pos2 + 5, str.length())).intValue();
					else
						size = new Integer(str.substring(pos2 + 5, pos3))
								.intValue();
				}
				pos2 = str.indexOf("style="); //$NON-NLS-1$
				if (pos2 >= 0) {
					pos3 = str.indexOf(",", pos2); //$NON-NLS-1$
					if (pos3 < 0)
						style = new Integer(str.substring(pos2 + 6, str
								.length())).intValue();
					else
						style = new Integer(str.substring(pos2 + 6, pos3))
								.intValue();
				}

			}

			if (definitionNameFont == null) {
				definitionNameFont = new Font(null, name, size, style);
			} else {
				if (!definitionNameFont.getFontData()[0].getName().equals(name)
						|| definitionNameFont.getFontData()[0].getHeight() != size
						|| definitionNameFont.getFontData()[0].getStyle() != style) {
					definitionNameFont = new Font(null, name, size, style);
				}
			}
			return definitionNameFont;
		}

		public void disconnectFromModel() {
			optionsObject.getModel().removeModelTreeListener(optionsListener);
			if (optionsListener != null)
				optionsListener.dispose();
			optionsListener = null;
		}

		public void nodeChanged(XModelTreeEvent event) {
			fireProcessChanged();
			Definition definition;
			for (int i = 0; i < getDefinitionList().size(); i++) {
				definition = (Definition) getDefinitionList().get(i);
				definition.fireDefinitionChange();
			}

		}

		public void structureChanged(XModelTreeEvent event) {
		}
	}

}

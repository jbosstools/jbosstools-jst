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
import java.beans.*;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.jst.web.tiles.ui.editor.model.ITilesElement;
import org.jboss.tools.jst.web.tiles.ui.editor.model.ITilesElementList;
import org.jboss.tools.jst.web.tiles.ui.editor.model.ITilesElementListListener;

public class TilesElementList extends TilesElement implements ITilesElementList, VetoableChangeListener{

	List<ITilesElementListListener> listeners = new Vector<ITilesElementListListener>();
	List<Object> elements = new Vector<Object>();
	boolean elementListListenerEnable = true;
	boolean allowDuplicate = false;

	public TilesElementList() {
	}

	public TilesElementList(ITilesElement parent) {
		super(parent);
	}

	public TilesElementList(ITilesElement parent, XModelObject source) {
		super(parent, source);
	}

	public List<Object> getElements() {
		return elements;
	}

	public TilesElementList(List<Object> elements) {
		this.elements = elements;
	}

	public void dispose() {
		super.dispose();
		if (listeners != null)
			listeners.clear();
		listeners = null;
		if (elements != null)
			elements.clear();
		elements = null;
	}

	public void setAllowDuplicate(boolean set) {
		allowDuplicate = set;
	}

	public boolean isAllowDuplicate() {
		return allowDuplicate;
	}

	public void moveTo(Object object, int index) {
		int currentIndex = indexOf(object);
		if (index < 0 || index >= size())
			return;
		if (currentIndex > index) { // move down
			for (int i = currentIndex - 1; i >= index; i--) {
				Object elementAt = get(i);
				set(i + 1, elementAt);
			}
			set(index, object);
			this.fireElementMoved((ITilesElement) object, index, currentIndex);
		} else if (currentIndex < index) { // move up
			for (int i = currentIndex + 1; i <= index; i++) {
				Object elementAt = get(i);
				set(i - 1, elementAt);
			}
			set(index, object);
			this.fireElementMoved((ITilesElement) object, index, currentIndex);
		}
	}

	public void moveUp(Object object) {
		int currentIndex = indexOf(object);
		if (currentIndex == 0)
			return;
		set(currentIndex, get(currentIndex - 1));
		set(currentIndex - 1, object);
		this.fireElementMoved((ITilesElement) object, currentIndex - 1,
				currentIndex);
	}

	public void moveDown(Object object) {
		int currentIndex = indexOf(object);
		if (currentIndex == size())
			return;
		set(currentIndex, get(currentIndex + 1));
		set(currentIndex + 1, object);
		this.fireElementMoved((ITilesElement) object, currentIndex + 1,
				currentIndex);
	}

	public int size() {
		return elements.size();
	}

	public boolean isEmpty() {
		return elements.isEmpty();
	}

	public boolean contains(Object o) {
		return elements.contains(o);
	}

	public Iterator iterator() {
		return elements.iterator();
	}

	public Object[] toArray() {
		return elements.toArray();
	}

	public Object[] toArray(Object a[]) {
		return elements.toArray(a);
	}

	public boolean add(Object o) {
		ITilesElement element = (ITilesElement) o;
		boolean result = elements.add(element);
		if (result) {
		}
		return result;
	}

	public void add(ITilesElementList list) {
		for (int i = 0; i < list.size(); i++) {
			add((TilesElement) list.get(i));
		}

	}

	public boolean remove(Object o) {
		boolean result = elements.remove(o);
		return result;
	}

	public void remove(Comparator comp) {
		for (int i = size() - 1; i >= 0; i--) {
			if (comp.equals(get(i))) {
				remove(get(i));
			}
			;
		}
	}

	public void removeAll() {
		for (int i = size() - 1; i >= 0; i--) {
			remove(get(i));
		}
	}

	public Object get(int index) {
		return elements.get(index);
	}

	public Object get(String name) {
		if (name == null)
			return null;
		for (int i = 0; i < elements.size(); i++) {
			TilesElement element = (TilesElement) elements.get(i);
			if (name.equals(element.getName()))
				return element;
		}
		return null;
	}

	public Object set(int index, Object element) {
		Object newElement = elements.set(index, element);
		return newElement;
	}

	public void add(int index, Object element) {
		elements.add(index, element);
	}

	public int indexOf(Object o) {
		return elements.indexOf(o);
	}

	public ITilesElement findElement(Comparator comparator) {
		return null;
	}

	public ITilesElementList findElements(Comparator comparator) {
		return null;
	}

	public String getText() {
		return toString();
	}

	public Object clone() {
		List<Object> es = new ArrayList<Object>();
		es.addAll(elements);
		TilesElementList clone = new TilesElementList(es);
		return clone;
	}

	public TilesElementList getClone() {
		TilesElementList list = (TilesElementList) clone();
		return list;
	}

	public void vetoableChange(PropertyChangeEvent evt)
			throws PropertyVetoException {
	}

	public void addTilesElementListListener(ITilesElementListListener l) {
		listeners.add(l);
	}

	public void removeTilesElementListListener(ITilesElementListListener l) {
		listeners.remove(l);
	}

	protected void fireElementMoved(ITilesElement element, int newIndex,
			int oldIndex) {
		for (int i = 0; i < listeners.size(); i++) {
			ITilesElementListListener listener = (ITilesElementListListener) listeners
					.get(i);
			if (listener != null && listener.isElementListListenerEnable())
				listener.listElementMove(this, element, newIndex, oldIndex);
		}
	}

	protected void fireElementAdded(ITilesElement element, int index) {
		for (int i = 0; i < listeners.size(); i++) {
			ITilesElementListListener listener = (ITilesElementListListener) listeners
					.get(i);
			if (listener != null && listener.isElementListListenerEnable())
				listener.listElementAdd(this, element, index);
		}
	}

	protected void fireElementRemoved(ITilesElement element, int index) {
		for (int i = 0; i < listeners.size(); i++) {
			ITilesElementListListener listener = (ITilesElementListListener) listeners
					.get(i);
			if (listener != null && listener.isElementListListenerEnable())
				listener.listElementRemove(this, element, index);
		}
	}

	protected void fireElementChanged(ITilesElement element, int index,
			PropertyChangeEvent event) {
		for (int i = 0; i < listeners.size(); i++) {
			ITilesElementListListener listener = (ITilesElementListListener) listeners
					.get(i);
			if (listener != null && listener.isElementListListenerEnable())
				listener.listElementChange(this, element, index, event);
		}
	}

	public void remove(int index) {
		Object obj = elements.get(index);
		elements.remove(obj);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}

class Comp implements Comparator {
	String name;

	public Comp(String name) {
		this.name = name;
	}

	public int compare(Object o1, Object o2) {
		return 0;
	}

	public boolean equals(Object obj) {
		if (obj instanceof ITilesElement) {
			ITilesElement element = (ITilesElement) obj;
			return element.getName().equals(name);
		}
		return false;
	}
}

class ElementNameComparator implements Comparator {
	String elementName;

	public ElementNameComparator(String elementName) {
		this.elementName = elementName;
	}

	public void setName(String name) {
		elementName = name;
	}

	public boolean equals(Object object) {
		if (object instanceof TilesElement) {
			TilesElement unit = (TilesElement) object;
			return unit.getName().equals(elementName);
		}
		return false;
	}

	public int compare(Object obj1, Object obj2) {
		return 0;
	}

}

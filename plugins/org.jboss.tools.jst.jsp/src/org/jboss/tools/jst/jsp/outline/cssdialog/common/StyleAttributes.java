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
package org.jboss.tools.jst.jsp.outline.cssdialog.common;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.map.WritableMap;

/**
 * Class is used to store an array of style attributes and support listeners.
 */
public class StyleAttributes {

	private IObservableMap attributeMap = null;

	/**
	 * Default constructor.
	 */
	public StyleAttributes() {
		this.attributeMap = new WritableMap();
	}

	/**
	 * Add attribute with the given name and value.
	 * 
	 * @param name
	 * 
	 *            the name of attribute
	 * @param value
	 *            the value of attribute
	 */
	public String put(String name, String value) {
		return (String) attributeMap.put(name.toLowerCase(), value);
	}
	
	public void putAll(Map<String, String> t) {
		attributeMap.putAll(t);
	}

	/**
	 * Remove attribute with the given name.
	 * 
	 * @param name
	 *            the name of attribute to be removed
	 */
	public void remove(String name) {
		attributeMap.remove(name.toLowerCase());
	}

	/**
	 * @see java.util.HashMap#entrySet()
	 */
	public Set<Entry<String, String>> entrySet() {
		return attributeMap.entrySet();
	}

	/**
	 * Get attribute value with the given name.
	 * 
	 * @param name
	 *            the name of attribute to be returned
	 * @return attribute value
	 */
	public String get(Object name) {
		String value = (String) attributeMap.get(name);
		return value != null ? value : Constants.EMPTY;
	}

	/**
	 * @see java.util.HashMap#keySet()
	 */
	public Set<String> keySet() {
		return attributeMap.keySet();
	}

	/**
	 * Clear cache of attributes.
	 */
	public void clear() {
		attributeMap.clear();
	}

	/**
     * 
     */
	public void setStyleProperties(Map<String, String> properties) {
		attributeMap.clear();

		for (Entry<String, String> entry : properties.entrySet()) {
			attributeMap.put(entry.getKey().toLowerCase(), entry.getValue());
		}

	}

	public Map<String, String> getStyleProperties() {
		return attributeMap;
	}

	/**
	 * String representation of style attributes.
	 * 
	 * @return style string representation
	 */
	public String getStyle() {
		// update newStyle value
		StringBuffer buf = new StringBuffer();
		Set<Entry<String, String>> set = entrySet();
		for (Map.Entry<String, String> me : set) {
			if ((me.getValue() != null) && (me.getValue().length() != 0))
				buf.append(me.getKey() + Constants.COLON + me.getValue()
						+ Constants.SEMICOLON);
		}
		return buf.toString();
	}

	public IObservableMap getObservableMap() {
		return attributeMap;
	}

	public void addChangeListener(IChangeListener listener) {
		attributeMap.addChangeListener(listener);
	}

	public void removeChangeListener(IChangeListener listener) {
		attributeMap.removeChangeListener(listener);
	}

}

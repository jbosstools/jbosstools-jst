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
package org.jboss.tools.jst.jsp.outline.cssdialog.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.jboss.tools.jst.jsp.outline.cssdialog.common.Constants;

/**
 * Class is used to store an array of style attributes and support listeners.
 */
public class StyleAttributes {

	private String cssSelector = null;
    private HashMap<String, String> attributeMap = null;
    private ArrayList<ChangeStyleListener> listeners = new ArrayList<ChangeStyleListener>();

    /**
     * Default constructor.
     */
    public StyleAttributes() {
        this.attributeMap = new HashMap<String, String>();
    }

    /**
     * Gets attribute map.
     *
     * @return map of attributes
     */
    public HashMap<String, String> getAttributeMap() {
        return attributeMap;
    }

    /**
     * Sets attribute map.
     *
     * @param attributeMap value
     */
    public void setAttributeMap(HashMap<String, String> attributeMap) {
        this.attributeMap = attributeMap;
        notifyListeners();
    }

    /**
     * Add ChangeStyleListener object.
     *
     * @param listener ChangeStyleListener object to be added
     */
    public void addChangeStyleListener(ChangeStyleListener listener) {
        listeners.add(listener);
    }

    /**
     * Gets an array of ChangeStyleListener object.
     *
     * @return an array of ChangeStyleListener object
     */
    public ChangeStyleListener[] getChangeStyleListeners() {
        return listeners.toArray(new ChangeStyleListener[listeners.size()]);
    }

    /**
     * Remove ChangeStyleListener object passed by parameter.
     *
     * @param listener ChangeStyleListener object to be removed
     */
    public void removeChangeStyleListener(ChangeStyleListener listener) {
        listeners.remove(listener);
    }

    /**
     * Add attribute with the given name and value.
     *
     * @param name the name of attribute
     * @param value the value of attribute
     */
    public void addAttribute(String name, String value) {
        attributeMap.put(name, value);
        notifyListeners();
    }

    /**
     * Remove attribute with the given name.
     *
     * @param name the name of attribute to be removed
     */
    public void removeAttribute(String name) {
        attributeMap.remove(name);
        notifyListeners();
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
     * @param name the name of attribute to be returned
     * @return attribute value
     */
    public String getAttribute(String name) {
    	String value = attributeMap.get(name);
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
        cssSelector = null;
        notifyListeners();
    }

    /**
     * Method is used to notify all subscribed listeners about any changes within style attribute map.
     */
    private void notifyListeners() {
        ChangeStyleEvent event = new ChangeStyleEvent(this);
        for (ChangeStyleListener listener : listeners) {
            listener.styleChanged(event);
        }
    }

	/**
	 * @param cssSelector the cssSelector to set
	 */
	public void setCssSelector(String cssSelector) {
		this.cssSelector = cssSelector;
	}

	/**
	 * @return the cssSelector
	 */
	public String getCssSelector() {
		return cssSelector;
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
            buf.append(me.getKey() + Constants.COLON + me.getValue() + Constants.SEMICOLON);
        }
        return buf.toString();
	}
}

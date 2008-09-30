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
import java.util.Set;
import java.util.Map.Entry;

public class StyleAttributes {

    private HashMap<String, String> attributeMap;
    private ArrayList<ChangeStyleListener> listeners = new ArrayList<ChangeStyleListener>();

    public StyleAttributes() {
	this.attributeMap = new HashMap<String, String>();
    }

    public HashMap<String, String> getAttributeMap() {
	return attributeMap;
    }

    public void setAttributeMap(HashMap<String, String> attributeMap) {
	this.attributeMap = attributeMap;
	addNewListener();
    }

    public void addChangeStyleListener(ChangeStyleListener listener) {
	listeners.add(listener);
    }

    public ChangeStyleListener[] getChangeStyleListeners() {
	return listeners.toArray(new ChangeStyleListener[listeners.size()]);
    }

    public void removeChangeStyleListener(ChangeStyleListener listener) {
	listeners.remove(listener);
    }

    private void addNewListener() {
	ChangeStyleEvent event = new ChangeStyleEvent(this);
	for (ChangeStyleListener listener : listeners)
	    listener.styleChanged(event);
    }

    public void addAttribute(String name, String value) {
	attributeMap.put(name, value);
	addNewListener();
    }

    public void removeAttribute(String name) {
	attributeMap.remove(name);
	addNewListener();
    }

    public Set<Entry<String, String>> entrySet() {
	return attributeMap.entrySet();
    }

    public String getAttribute(String name) {
	return attributeMap.get(name);
    }

    public Set<String> keySet() {
	return attributeMap.keySet();
    }
    
    public void clear() {
	attributeMap.clear();
	addNewListener();
    }

}

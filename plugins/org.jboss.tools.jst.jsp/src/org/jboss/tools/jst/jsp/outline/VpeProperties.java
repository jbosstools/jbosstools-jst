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
package org.jboss.tools.jst.jsp.outline;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.jboss.tools.common.model.ui.objecteditor.ExtendedCellEditorProvider;
import org.jboss.tools.common.model.ui.objecteditor.ExtendedProperties;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.wst.html.core.internal.document.ElementStyleImpl;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class VpeProperties implements ExtendedProperties {
	Node node;
	ElementStyleImpl element = null;
	JSPPropertySourceAdapter propertySource = null;
	HashMap properties = new HashMap();
	String[] names;
	
	public VpeProperties(Node node){
		this.node = node;
		if(node instanceof ElementStyleImpl && node instanceof INodeNotifier) {
			element = (ElementStyleImpl)node;
			JSPPropertySourceAdapter adapter = new JSPPropertySourceAdapter((INodeNotifier)node);
			adapter.setSorter(new AttributeSorter());
			propertySource = adapter;
			IPropertyDescriptor[] ds = propertySource.getPropertyDescriptors();
			names = new String[ds.length];
			for(int i = 0; i < ds.length; i++) {
				names[i] = ds[i].getDisplayName();
				properties.put(ds[i].getDisplayName(), ds[i]);
			}
			final Map weights = propertySource.getWeights();
			Comparator c = new Comparator() {
				public int compare(Object o1, Object o2) {
					String s1 = o1.toString();
					String s2 = o2.toString();
					int w1 = getWeight(s1);
					int w2 = getWeight(s2);
					if(w1 != w2) return w2 - w1;
					return s1.compareTo(s2);
				}
				int getWeight(String s) {
					Integer in = (Integer)weights.get(s);
					return in == null ? 0 : in.intValue();
				}
				
			};
			Arrays.sort(names, c);
		}
	}

	public String[] getAttributes() {
		if(propertySource != null){
			return names;
		}
		NamedNodeMap map = node.getAttributes();
		String[] attributes = new String[map.getLength()]; 
		for(int i=0;i<map.getLength();i++){
			attributes[i] = map.item(i).getNodeName();
		}
		return attributes;
	}

	public String getAttributeValue(String name) {
		if(propertySource != null){
			return (String)propertySource.getPropertyValue(name);
		}
		NamedNodeMap map = node.getAttributes();
		return map.getNamedItem(name).getNodeValue();
	}

	public void setAttributeValue(String name, String value) {
		if(propertySource != null){
			propertySource.setPropertyValue(name, value);
		}
	}

	public boolean isEditableAttribute(String name) {
		return true;
	}
	
	public String getNodeName() {
		return node == null ? "" : node.getNodeName(); //$NON-NLS-1$
	}
	
	public Node getNode() {
		return node;
	}

	public ExtendedCellEditorProvider createCellEditorProvider() {
		return new ExtendedCellEditorProviderImpl();
	}
}

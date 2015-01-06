/******************************************************************************* 
 * Copyright (c) 2013 - 2015 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.angularjs.internal.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.jboss.tools.jst.web.ui.internal.properties.DefaultPropertySetViewer;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class AngularJSPropertySetViewer extends DefaultPropertySetViewer {
//extends AbstractAdvancedPropertySetViewer implements JQueryHTMLConstants {
//	AngularJSPropertySetContext context = new AngularJSPropertySetContext(this);
//	AngularJSLayout layouts = new AngularJSLayout(this);

	List<Pair> pairs = new ArrayList<Pair>();

	public AngularJSPropertySetViewer() {}

	@Override
	public String getCategoryDisplayName() {
		return "AngularJS";
	}

//	@Override
//	protected boolean isStructureChanged(List<IPropertyDescriptor> descriptors) {
//		return context.update();
//	}

	@Override
	public List<IPropertyDescriptor> getFilteredDescriptors(List<IPropertyDescriptor> descriptors) {
		pairs.clear();
		Map<String, Pair> map = new HashMap<String, AngularJSPropertySetViewer.Pair>();

		for (IPropertyDescriptor d: descriptors) {
			if(category != null && category.equals(d.getCategory())) {
				String name = d.getId().toString();
				if(name.startsWith("data-ng")) {
					name = name.substring(5);
					Pair p = map.get(name);
					if(p == null) {
						p = new Pair();
						map.put(name, p);
						pairs.add(p);
					}
					p.data_ng = d;
				} else if(name.startsWith("ng-")) {
					Pair p = map.get(name);
					if(p == null) {
						p = new Pair();
						map.put(name, p);
						pairs.add(p);
					}
					p.ng = d;
				}
			}
		}

		List<IPropertyDescriptor> result = new ArrayList<IPropertyDescriptor>();
		for (Pair p: pairs) {
			if(p.data_ng != null && (p.ng == null || isPropertySet(p.data_ng))) {
				result.add(p.data_ng);
			} else if(p.ng != null) {
				result.add(p.ng);
			}
		}
		return result;
	}

//	@Override
//	public Entry getEditor(String id) {
//		Entry result = super.getEditor(id);
//		if(result == null) {
//			result = super.getEditor("data-" + id);
//		}
//		return result;
//	}

//	@Override
//	protected void layoutEditors(Composite fields, List<Entry> entries) {
//		for (Entry e: entries) {
//			if(!e.isLayout()) {
//				layoutEditor(e, fields, true);
//			}
//		}
//	}
//
//	protected String toVisualValue(String modelValue, IPropertyDescriptor d) {
//		return modelValue;
//	}
//	
//	protected Object toModelValue(Object visualValue, IPropertyDescriptor d) {
//		return visualValue;
//	}
//
//	protected void initEditorProviders() {
//	}

	class Pair {
		IPropertyDescriptor ng = null;
		IPropertyDescriptor data_ng = null;
	}
}

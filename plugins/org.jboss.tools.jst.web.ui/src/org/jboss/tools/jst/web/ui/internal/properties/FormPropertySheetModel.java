/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.internal.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class FormPropertySheetModel implements IPropertySheetModel {
	private IWorkbenchPart part;

	private IPropertySourceProvider propertySourceProvider;

	private Object[] values = null;
	private Map<Object, IPropertySource> sources = new HashMap<Object, IPropertySource>(0);

	/**
	 * List of descriptors;
	 */
	private List<IPropertyDescriptor> descriptors = null;
	/**
	 * Map of descriptors by id converted to string
	 */
	private Map<String, IPropertyDescriptor> allDescriptors = new HashMap<String, IPropertyDescriptor>();
	/**
	 * Map of property values by id.
	 */
	private Map<Object, String> propertyValues = new HashMap<Object, String>();
	
	private List<IPropertySheetModelListener> listeners = new ArrayList<IPropertySheetModelListener>();

	public FormPropertySheetModel() {}

	public void setWorkbenchPart(IWorkbenchPart part) {
		this.part = part;
	}

	public IWorkbenchPart getWorkbenchPart() {
		return part;
	}

	public void setPropertySourceProvider(IPropertySourceProvider provider) {
		this.propertySourceProvider = provider;
	}

	public Object getValue() {
		return values.length == 0 ? null : values[0];
	}

	public void setInput(Object[] objects) {
		values = objects;
		sources = new HashMap<Object, IPropertySource>(values.length * 2 + 1);

		if (values.length == 0) {
//			editValue = null;
		} else {
			// set the first value object as the entry's value
			Object newValue = values[0];

			if (propertySourceProvider != null) {
				//Bug with refresh, clear cache by this call
//				propertySourceProvider.getPropertySource("");//TODO
			}
			// see if we should convert the value to an editable value
			IPropertySource source = getPropertySource(newValue);
			if (source != null) {
				newValue = source.getEditableValue();
			}
//			editValue = newValue;
		}

		// update our child entries
		refreshPropertyDescriptors();

//		refreshChildEntries();

		// inform listeners that our value changed
//		fireValueChanged();
	}

	public IPropertySource getPropertySource() {
		return values.length == 0 ? null : getPropertySource(values[0]);
	}

	public String getValueAsString(IPropertyDescriptor descriptor) {
		String o = propertyValues.get(descriptor.getId());
		return o == null ? "" : o.toString();
		
	}

	/**
	 * Returns an property source for the given object.
	 * 
	 * @param object
	 *            an object for which to obtain a property source or
	 *            <code>null</code> if a property source is not available
	 * @return an property source for the given object
	 * @since 3.1 (was previously private)
	 */
	protected IPropertySource getPropertySource(Object object) {
		if (sources.containsKey(object))
			return (IPropertySource) sources.get(object);

		IPropertySource result = null;
		IPropertySourceProvider provider = propertySourceProvider;

//		if (provider == null && object != null) {
//			provider = (IPropertySourceProvider) ViewsPlugin.getAdapter(object, 
//                    IPropertySourceProvider.class, false);
//        }

		if (provider != null) {
			result = provider.getPropertySource(object);
		} else {
//            result = (IPropertySource)ViewsPlugin.getAdapter(object, IPropertySource.class, false);
        }

		sources.put(object, result);
		return result;
	}

	public void dispose() {
		//dispose cell editor
		//etc
	}

	public List<IPropertyDescriptor> getPropertyDescriptors() {
		if(descriptors == null) {
			descriptors = computeMergedPropertyDescriptors();
		}
		return descriptors;
	}

	public boolean hasDescriptor(String id) {
		return allDescriptors.containsKey(id);
	}

	public IPropertyDescriptor getDescriptor(String id) {
		return allDescriptors.get(id);
	}

	public void refreshPropertyDescriptors() {
		List<IPropertyDescriptor> ds = computeMergedPropertyDescriptors();

		//sort descriptors and assign
		Map<String, IPropertyDescriptor> tree = new TreeMap<String, IPropertyDescriptor>();
		for (IPropertyDescriptor d: ds) {
			tree.put(d.getDisplayName(), d);
		}
		ds = new ArrayList<IPropertyDescriptor>();
		ds.addAll(tree.values());
		descriptors = ds;

		//compute values and present descriptors as map
		allDescriptors.clear();
		Map<Object, String> newPropertyValues = new HashMap<Object, String>();
		if(descriptors.size() > 0) {
			IPropertySource s = getPropertySource(values[0]);
			for (IPropertyDescriptor d: descriptors) {
				Object id = d.getId();
				allDescriptors.put(id.toString(), d);
				Object value = s.getPropertyValue(id);
				newPropertyValues.put(id, value == null ? "" : value.toString());
			}
		}
		propertyValues = newPropertyValues;
	}

	private List<IPropertyDescriptor> computeMergedPropertyDescriptors() {
		if (values.length == 0) {
			return new ArrayList<IPropertyDescriptor>(0);
		}

		IPropertySource firstSource = getPropertySource(values[0]);
		if (firstSource == null) {
			return new ArrayList<IPropertyDescriptor>(0);
		}

		if (values.length == 1) {
			return Arrays.asList(firstSource.getPropertyDescriptors());
		}

		// get all descriptors from each object
		List<Map<Object, IPropertyDescriptor>> propertyDescriptorMaps = new ArrayList<Map<Object,IPropertyDescriptor>>(values.length);
		for (int i = 0; i < values.length; i++) {
			Object object = values[i];
			IPropertySource source = getPropertySource(object);
			if (source == null) {
				// if one of the selected items is not a property source
				// then we show no properties
				return new ArrayList<IPropertyDescriptor>(0);
			}
			// get the property descriptors keyed by id
			propertyDescriptorMaps.add(computePropertyDescriptorsFor(source));
		}

		// intersect
		Map<Object, IPropertyDescriptor> intersection = propertyDescriptorMaps.get(0);
		for (int i = 1; i < propertyDescriptorMaps.size(); i++) {
			// get the current ids
			Object[] ids = intersection.keySet().toArray();
			for (int j = 0; j < ids.length; j++) {
				Object object = propertyDescriptorMaps.get(i).get(ids[j]);
				if (object == null ||
				// see if the descriptors (which have the same id) are
						// compatible
						!((IPropertyDescriptor) intersection.get(ids[j]))
								.isCompatibleWith((IPropertyDescriptor) object)) {
					intersection.remove(ids[j]);
				}
			}
		}

		// sorting is handled in the PropertySheetViewer, return unsorted (in
		// the original order)
		List<IPropertyDescriptor> result = new ArrayList<IPropertyDescriptor>(intersection.size());
		IPropertyDescriptor[] firstDescs = firstSource.getPropertyDescriptors();
		for (int i = 0; i < firstDescs.length; i++) {
			IPropertyDescriptor desc = firstDescs[i];
			if (intersection.containsKey(desc.getId())) {
				result.add(desc);
			}
		}
		return result;
	}

	private Map<Object, IPropertyDescriptor> computePropertyDescriptorsFor(IPropertySource source) {
		IPropertyDescriptor[] descriptors = source.getPropertyDescriptors();
		Map<Object, IPropertyDescriptor> result = new HashMap<Object, IPropertyDescriptor>(descriptors.length * 2 + 1);
		for (int i = 0; i < descriptors.length; i++) {
			result.put(descriptors[i].getId(), descriptors[i]);
		}
		return result;
	}

	public synchronized void addListener(IPropertySheetModelListener listener) {
		listeners.add(listener);
	}

	public synchronized void removeListener(IPropertySheetModelListener listener) {
		listeners.remove(listener);
	}

	synchronized List<IPropertySheetModelListener> copyListeners() {
		return new ArrayList<IPropertySheetModelListener>(listeners);
	}

	public void valueChanged(IPropertyDescriptor d, Object newValue) {
		propertyValues.put(d.getId(), newValue == null ? "" : newValue.toString());

		//fast view update before commit
		List<IPropertySheetModelListener> ls = copyListeners();
		for (IPropertySheetModelListener l: ls) {
			l.valueChanged(d);
		}

		//commit
		IPropertySource s = getPropertySource();
		s.setPropertyValue(d.getId(), newValue);

		//eventually view will be refreshed
	}

}

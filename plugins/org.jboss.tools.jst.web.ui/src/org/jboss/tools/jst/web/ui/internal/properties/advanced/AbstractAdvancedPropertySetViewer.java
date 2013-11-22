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
package org.jboss.tools.jst.web.ui.internal.properties.advanced;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.jboss.tools.common.ui.widget.editor.CompositeEditor;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.common.ui.widget.editor.SwtFieldEditorFactory;
import org.jboss.tools.jst.web.ui.internal.editor.outline.IFormPropertySource;
import org.jboss.tools.jst.web.ui.internal.properties.AbstractPropertySetViewer;
import org.jboss.tools.jst.web.ui.internal.properties.IPropertySheetModelListener;
import org.jboss.tools.jst.web.ui.palette.html.wizard.ComboContentProposalProvider;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public abstract class AbstractAdvancedPropertySetViewer extends AbstractPropertySetViewer implements PropertyChangeListener {
	protected ScrolledComposite scroll;
	protected Composite fields = null;

	protected Map<String, IFieldEditorProvider> editorProviders = new HashMap<String, IFieldEditorProvider>();

	protected List<Entry> entries = new ArrayList<Entry>();
	private Map<String, Entry> entriesByID = new HashMap<String, Entry>();

	protected boolean isUpdating = false;

	public AbstractAdvancedPropertySetViewer() {
		initEditorProviders();
	}

	/**
	 * Helper method to retrieve edited IFile if relevant.
	 * 
	 * @return
	 */
	protected IFile getContextFile() {
		IWorkbenchPart part = model.getWorkbenchPart();
		if(part instanceof IEditorPart) {
			IEditorInput input = ((IEditorPart)part).getEditorInput();
			if(input instanceof IFileEditorInput) {
				return ((IFileEditorInput)input).getFile();
			}
		}
		return null;
	}

	@Override
	public Composite createControl(Composite parent) {
		scroll = new ScrolledComposite(parent, SWT.V_SCROLL);
		scroll.setLayout(new GridLayout());
		fields = new Composite(scroll, SWT.NONE);
		GridData d = new GridData(GridData.FILL_BOTH);
		fields.setLayoutData(d);
		fields.setLayout(new GridLayout(3, false));
		scroll.setContent(fields);
		scroll.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				if(fields != null && !fields.isDisposed()) {
					fields.setSize(scroll.getSize().x, fields.getSize().y);
					fields.layout(true);
				}
			}
		});
		return scroll;
	}

	@Override
	public Control getControl() {
		return scroll;
	}

	/**
	 * Create lightweight objects IFieldEditorProviers that will be used
	 * to create field editors as needed.
	 * Either this method or createEditor(IPropertyDescriptor) 
	 * should be overridden.
	 * 
	 */
	protected void initEditorProviders() {		
	}

	/**
	 * Returns list of property descriptors to be associated with field editors.
	 * It can be just a subset of model field descriptors.
	 * It can create more descriptors, but for each custom descriptor
	 * method getOriginalDescriptor() should return the associated model descriptor.
	 * 
	 * @param descriptors List of model property descriptors
	 * @return List of property descriptors to be associated with field editors
	 */
	protected abstract List<IPropertyDescriptor> getFilteredDescriptors(List<IPropertyDescriptor> descriptors);

	/**
	 * Helper method to be used in getFilteredDescriptors()
	 * @param id
	 * @param result
	 */
	protected void selectDescriptor(String id, List<IPropertyDescriptor> result) {
		if(getModel().hasDescriptor(id)) {
			result.add(getModel().getDescriptor(id));
		}
	}

	/**
	 * Helper method to be used in getFilteredDescriptors()
	 * @param id
	 * @param result
	 */
	protected void selectDescriptors(String[] ids, List<IPropertyDescriptor> result) {
		for (String id: ids) selectDescriptor(id, result);
	}

	/**
	 * Returns true if form should be rebuilt. 
	 * 
	 * This method is called the very first in refresh().
	 * It is encouraged to check and cache for later quick reference 
	 * all changes in context that happened since previous refresh.
	 * 
	 * This method does not need to return 'true' if the set of visual property 
	 * descriptors is expected to be changed. Method refresh() compares 
	 * current and new sets and detects difference.
	 * 
	 * Rather, 'true' should be returned for cases when formal set of visual 
	 * property descriptors is going to remain the same, but associated 
	 * field editors should be replaced because of context.
	 * 
	 * @param descriptors
	 * @return
	 */
	protected boolean isStructureChanged(List<IPropertyDescriptor> descriptors) {
		return false;
	}

	@Override
	public void refresh(List<IPropertyDescriptor> descriptors) {
		boolean isModified = isStructureChanged(descriptors);
		descriptors = getFilteredDescriptors(descriptors);
		isUpdating = true;
		if(!isModified && !isModified(descriptors)) {
			for (int i = 0; i < entries.size(); i++) {
				Entry e = entries.get(i);
				e.update(descriptors.get(i));
			}
		} else {
			//dispose old components
			for (Entry e: entries) {
				if(e.layout) {
					e.editor.removePropertyChangeListener(this);
					e.editor.dispose();
				}
			}
			Control[] cs = fields.getChildren();
			for (int i = 0; i < cs.length; i++) {
				cs[i].dispose();
			}

			//create new entries
			entries.clear();
			entriesByID.clear();			
			for (int i = 0; i < descriptors.size(); i++) {
				IPropertyDescriptor d = descriptors.get(i);
				Entry e = new Entry(d);
				if(e.editor != null) {
					entries.add(e);
					entriesByID.put(d.getId().toString(), e);
				}
			}

			//create ui and init editor values
			if(entries.isEmpty()) {
				Label label = new Label(fields, SWT.NONE);
				label.setText("There is no form for current selection.");
				GridData d = new GridData();
				d.horizontalSpan = 3;
				label.setLayoutData(d);
			} else {
				layoutEditors(fields, entries);
			}
			for (Entry e: entries) {
				e.update();
			}		

			fields.pack(true);
			fields.setSize(scroll.getSize().x, fields.getSize().y);
			
		}
		isUpdating = false;
	}

	/**
	 * This implementation uses IFieldEditorProvider objects created by
	 * method initEditorProviders(). Subclasses are not encouraged to 
	 * override this method.
	 *  
	 * @param d
	 * @return
	 */
	protected IFieldEditor createEditor(IPropertyDescriptor d) {
		IFieldEditorProvider editorProvider = editorProviders.get(d.getId().toString());
		if(editorProvider != null) {
			return editorProvider.createEditor();
		}
		return createTextEditor(d);
	}

	public IFieldEditor createTextEditor(IPropertyDescriptor d) {
		CompositeEditor editor = (CompositeEditor)SwtFieldEditorFactory.INSTANCE.createTextEditor(d.getId().toString(),
									d.getDisplayName() + ":", 
									model.getValueAsString(d),
									"" /* description */);
//		editor.setEnabled(false);
		return editor; 
	}

	/**
	 * Add editors to the base composite.
	 * @param fields
	 * @param entries
	 */
	protected abstract void layoutEditors(Composite fields, List<Entry> entries);

	/**
	 * Returns true if new list of descriptors differs from the current one.
	 * 
	 * @param descriptors
	 * @return
	 */
	protected boolean isModified(List<IPropertyDescriptor> descriptors) {
		if(entries.size() != descriptors.size()) {
			return true;
		}
		for (int i = 0; i < entries.size(); i++) {
			Entry e = entries.get(i);
			IPropertyDescriptor d = descriptors.get(i);
			if(!d.getId().equals(e.descriptor.getId())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void dispose() {
		super.dispose();
		scroll.dispose();
		for (Entry e : entries) {
			e.editor.removePropertyChangeListener(this);
		}
	}

	/**
	 * Returns editor entry by id.
	 * @param id
	 * @return
	 */
	public Entry getEditor(String id) {
		return entriesByID.get(id);
	}

	/**
	 * Checks if editor with id is available.
	 * @param id
	 * @return
	 */
	public boolean hasEditor(String id) {
		return entriesByID.containsKey(id);
	}

	/**
	 * Returns unmodifiable copy of entries currently shown by this property set viewer.
	 * @return
	 */
	public List<Entry> getEntries() {
		return Collections.unmodifiableList(entries);
	}

	/**
	 * Adds editor to assigned parent composite.
	 * @param editor
	 * @param parent
	 */
	public void layoutEditor(String id, Composite parent) {
		if(getEditor(id) != null) {
			layoutEditor(getEditor(id), parent, true);
		}
	}

	/**
	 * Adds editor to assigned parent composite.
	 * @param editor
	 * @param parent
	 */
	public void layoutEditor(final Entry editor, Composite parent) {
		editor.layout(parent);
		editor.editor.addPropertyChangeListener(this);
		Combo c = LayoutUtil.findCombo(editor.editor);
		if(c != null) {
			new ComboContentProposalProvider(c);
		}
		final Text text = LayoutUtil.findText(editor.editor);
		if(text != null && getModel().getPropertySource() instanceof IFormPropertySource) {
			text.addFocusListener(new FocusAdapter() {
				public void focusGained(FocusEvent e) {
					((IFormPropertySource)getModel().getPropertySource()).addContentAssist(text, editor.descriptor);
					text.removeFocusListener(this);
				}
			});
		}
		for (Object o: editor.editor.getEditorControls()) {
			if(o instanceof Widget  && !(o instanceof Label)) {
				final Widget w = (Widget)o;
				w.addListener(SWT.FOCUSED, new Listener() {
					@Override
					public void handleEvent(Event event) {
						editedDescriptor.setDescriptor(editor.descriptor);
					}
				});
			}
		}
	}

	/**
	 * Adds editor to assigned parent composite.
	 * @param editor
	 * @param parent
	 * @param expandCombo
	 */
	public void layoutEditor(Entry editor, Composite parent, boolean expandCombo) {
		layoutEditor(editor, parent);
		if(expandCombo) {
			LayoutUtil.expandCombo(editor.editor);
		}
	}

	/**
	 * Implementation checks and sets flag isUpdated, retrieves
	 * descriptor and calls setModelValue().
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(isUpdating) {
			return;
		}
		isUpdating = true;
		IPropertyDescriptor d = getModel().getDescriptor(evt.getPropertyName());
		if(d != null) {
			setModelValue(d, evt.getNewValue());
		}
		isUpdating = false;
	}

	/**
	 * Returns value that should be set to field editor.
	 * @param d
	 * @return
	 */
	protected String getVisualValue(IPropertyDescriptor d) {
		Entry e = getEditor(d.getId().toString());
		if(!isPropertySet(d) && e != null && e.defaultValue != null) {
			return e.defaultValue.toString();
		} else {
			return toVisualValue(model.getValueAsString(d), d);
		}
	}

	/**
	 * 1. Converts visual value to model value (by default calls toModelValue());
	 *    Or sets model value to null if visual value is the optional default value.
	 * 2. Finds original property descriptor, for which value is set (by default 
	 *    it is the passed descriptor)
	 * 3. Calls EditedDescriptor object associated with model.
	 *     
	 * @param d
	 * @param visualValue
	 */
	protected void setModelValue(IPropertyDescriptor d, Object visualValue) {
		Object value = isOptionalDefaultValue(d, visualValue) ? null : toModelValue(visualValue, d);
		editedDescriptor.setDescriptor(d);
		editedDescriptor.applyValue(value);
	}

	/**
	 * By default, returns true if visual value is default value of the editor,
	 * and either "true" or "false".
	 * @param d
	 * @param visualValue
	 * @return
	 */
	protected boolean isOptionalDefaultValue(IPropertyDescriptor d, Object visualValue) {
		Entry e = getEditor(d.getId().toString());
		if(e != null && e.isDefaultValue(visualValue)) {
			if("true".equals(visualValue.toString()) || "false".equals(visualValue.toString())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns descriptor associated with stored model property for the passed
	 * descriptor associated with a field editor.
	 * 
	 * @param d
	 * @return
	 */
	protected IPropertyDescriptor getOriginalDescriptor(IPropertyDescriptor d) {
		return d;
	}

	/**
	 * Converts model value to visual value to be displayed by the field editor
	 * associated with property descriptor.
	 * 
	 * @param modelValue
	 * @param d
	 * @return
	 */
	protected String toVisualValue(String modelValue, IPropertyDescriptor d) {
		return modelValue;
	}

	/**
	 * Converts visual editor associated with property descriptor to value to 
	 * be set to a model property represented by an original descriptor
	 * (see getOriginalDescriptor()).
	 * 
	 * @param visualValue
	 * @param d
	 * @return
	 */
	protected Object toModelValue(Object visualValue, IPropertyDescriptor d) {
		return visualValue;
	}
	
    protected void createEntryListener() {
    	entryListener = new IPropertySheetModelListener() {
			
			@Override
			public void valueChanged(IPropertyDescriptor descriptor) {
				if(isUpdating) {
					return;
				}
				isUpdating = true;
				Entry editor = getEditor(descriptor.getId().toString());
				if(editor != null) {
					String editorValue = editor.editor.getValueAsString();
					String newValue = getVisualValue(descriptor);
					if(!editorValue.equals(newValue)) {
						editor.editor.setValue(newValue);
					}
				}
				isUpdating = false;
			}
			
			@Override
			public void descriptorsChanged() {
				if(isUpdating) {
					return;
				}
				if(model != null) {
					refresh(model.getPropertyDescriptors());
				}
			}
		};
    }

    /**
     * Wrapper type that bounds editor and descriptor
     * and keeps default editor value.
     */
	public final class Entry {
		private IPropertyDescriptor descriptor;
		private IFieldEditor editor;
		private Object defaultValue = null;

		private boolean layout = false;

		Entry(IPropertyDescriptor descriptor) {
			this.descriptor = descriptor;
			editor = createEditor(descriptor);
			if(editor != null) {
				defaultValue = editor.getValue();
			}
		}

		public void update(IPropertyDescriptor d) {
			descriptor = d;
			update();
		}
	
		public void update() {
			if(layout) editor.setValue(getVisualValue(descriptor));
		}

		public void layout(Composite parent) {
			editor.doFillIntoGrid(parent);
			layout = true;
		}
	
		public boolean isDefaultValue(Object visualValue) {
			return defaultValue != null && visualValue != null 
					&& defaultValue.toString().equals(visualValue.toString());
		}

		public boolean isLayout() {
			return layout;
		}

		public IFieldEditor getEditor() {
			return editor;
		}

		public IPropertyDescriptor getDescriptor() {
			return descriptor;
		}

		/**
		 * For testing
		 * @return
		 */
		public Object getModelValue() {
			return toModelValue(editor.getValue(), descriptor);
		}
	}

}

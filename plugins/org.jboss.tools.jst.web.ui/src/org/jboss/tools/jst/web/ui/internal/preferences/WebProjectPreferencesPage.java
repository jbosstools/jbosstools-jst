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
package org.jboss.tools.jst.web.ui.internal.preferences;

import java.beans.*;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.core.resources.ResourcesPlugin;
import org.jboss.tools.common.model.ui.IValueProvider;
import org.jboss.tools.common.model.ui.attribute.IListContentProvider;
import org.jboss.tools.common.model.ui.attribute.adapter.*;
import org.jboss.tools.common.model.ui.attribute.editor.IPropertyEditor;
import org.jboss.tools.common.model.ui.preferences.*;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.jst.web.project.helpers.IWebProjectTemplate;

public abstract class WebProjectPreferencesPage extends TabbedPreferencesPage {
	protected ProjectXMOBasedPreferencesPage newProjectPage;
	protected XMOBasedPreferencesPage importProjectPage;
	
	private String oldRoot = ""; //$NON-NLS-1$
	private IPropertyEditor defaultRoot;	
	private IPropertyEditor selectRoot = null;
	private IValueProvider defaultRootValueProvider = null;
	private IModelPropertyEditorAdapter versionAdapter = null;
	private IWebProjectTemplate template;
	
	public WebProjectPreferencesPage() {
		template = createTemplate();
		newProjectPage = new ProjectXMOBasedPreferencesPage(getPreferenceModel().getByPath(getNewProjectOptionPath()));
		this.addPreferencePage(newProjectPage);
		importProjectPage = new XMOBasedPreferencesPage(getPreferenceModel().getByPath(getImportProjectOptionPath()));
		this.addPreferencePage(importProjectPage);
	}

	protected abstract String getNewProjectOptionPath();
	protected abstract String getImportProjectOptionPath();
	protected abstract IWebProjectTemplate createTemplate();
	protected abstract String getVersionAttribute();
	
	protected class ProjectXMOBasedPreferencesPage extends XMOBasedPreferencesPage {
		public ProjectXMOBasedPreferencesPage(XModelObject xmo) {
			super(xmo);

			versionAdapter = getSupport().getPropertyEditorAdapterByName(getVersionAttribute());
			((DefaultComboBoxValueAdapter)versionAdapter).setListContentProvider(
				new VersionListContentProvider()
			);
			IModelPropertyEditorAdapter adapter = getSupport().getPropertyEditorAdapterByName("Project Template"); /* "Project Template" */ //$NON-NLS-1$
			((DefaultComboBoxValueAdapter)adapter).setListContentProvider(
				new ProjectTemplatesListContentProvider()
			);
		}

		public void initPageProperties() {
			defaultRoot = (IPropertyEditor)getSupport().getPropertyEditorByName("Use Default Path"); //$NON-NLS-1$
			selectRoot = (IPropertyEditor)getSupport().getPropertyEditorByName("Projects Root"); //$NON-NLS-1$
			defaultRootValueProvider = (IValueProvider)defaultRoot.getAdapter(IValueProvider.class);
			defaultRootValueProvider.addValueChangeListener(new DefaultRootChangeListener());
			versionAdapter.addValueChangeListener(
				new VersionChangeListener(
					(PropertyChangeListener)getSupport().getPropertyEditorByName("Project Template").getFieldEditor(null) //$NON-NLS-1$
				)
			);
			updateRootInit();
		}
	}
	
	private void updateRootInit() {
		if(defaultRoot == null) return; 
		boolean active = "no".equals(defaultRootValueProvider.getValue());  //$NON-NLS-1$
		selectRoot.getFieldEditor(null).setEnabled(active, null);
	}
	
	private void updateRoot() {
		if(defaultRoot == null) return; 
		boolean active = "no".equals(defaultRootValueProvider.getValue());  //$NON-NLS-1$
		selectRoot.getFieldEditor(null).setEnabled(active, null);
		DefaultValueAdapter a = (DefaultValueAdapter)selectRoot.getInput();
		boolean auto = a.isAutoStore();
		a.setAutoStore(false);
		if(!active || oldRoot.length() == 0) oldRoot = a.getStringValue(true);

		String value = (active) ? oldRoot : ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString();
		a.setValue(value);
		a.setAutoStore(auto);
	}
	
	class DefaultRootChangeListener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {
			updateRoot();
		}
	}
	
	class VersionChangeListener implements PropertyChangeListener	{
		PropertyChangeListener nextListener = null;
		
		public VersionChangeListener(PropertyChangeListener nextListener) {
			this.nextListener = nextListener;	
		}
		
		public void propertyChange(PropertyChangeEvent evt) {
			if (nextListener == null) return;
			nextListener.propertyChange(new PropertyChangeEvent(
				evt.getSource(), IPropertyEditor.LIST_CONTENT, null, null)
			);
		}
	}

	class VersionListContentProvider implements IListContentProvider { 
		public Object[] getElements(Object inputElement) {
			return template.getVersionList();
		}
	
		public void dispose() {}
	
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}
	}

	class ProjectTemplatesListContentProvider implements IListContentProvider { 
		public Object[] getElements(Object inputElement) {
			return template.getTemplateList(versionAdapter.getValue().toString());
		}
	
		public void dispose() {}
	
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}
	}
}

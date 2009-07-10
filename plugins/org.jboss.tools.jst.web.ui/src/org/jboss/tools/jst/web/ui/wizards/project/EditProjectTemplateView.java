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
package org.jboss.tools.jst.web.ui.wizards.project;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.*;
import java.util.List;
import org.jboss.tools.common.propertieseditor.PropertiesEditor;
import org.jboss.tools.common.model.ui.attribute.XAttributeSupport;
import org.jboss.tools.common.model.ui.objecteditor.XChildrenEditor;
import org.jboss.tools.common.model.ui.wizards.special.AbstractSpecialWizardStep;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.jboss.tools.common.meta.action.XEntityData;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.options.PreferenceModelUtilities;
import org.jboss.tools.jst.web.WebModelPlugin;
import org.jboss.tools.jst.web.project.helpers.ProjectTemplate;
import org.jboss.tools.jst.web.ui.WebUiPlugin;

public class EditProjectTemplateView extends AbstractSpecialWizardStep {
	ProjectTemplate projectTemplate;
	
	EditProjectTemplateName templateName = new EditProjectTemplateName();
	EditPreprocessingList templatePreprocessing = new EditPreprocessingList();
	XChildrenEditor templateProperties = new PropertiesEditor();
	Control control;

	public Control createControl(Composite parent) {
		projectTemplate = (ProjectTemplate)support.getProperties().get("projectTemplate"); //$NON-NLS-1$
		templateName.init(new NameChangeListener(), projectTemplate, support.getEntityData()[id]);
		templatePreprocessing.setProjectTemplate(projectTemplate);
		templateProperties.setObject(projectTemplate.getProperties());
		Composite c = new Composite(parent, SWT.NONE);

		c.setLayout(new GridLayout());
		Control c1 = templateName.createControl(c);
		c1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Group g = new Group(c, SWT.NONE);
		g.setLayoutData(new GridData(GridData.FILL_BOTH));
		g.setLayout(new GridLayout());
		
		g.setText("Preprocessing");
		TabFolder tabFolder = new TabFolder(g, SWT.NONE);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));

		TabItem preprocessingTab = new TabItem(tabFolder,SWT.NULL);
		preprocessingTab.setText("Files");
		Control c2 = templatePreprocessing.createControl(tabFolder);
		preprocessingTab.setControl(c2);

		TabItem propertiesTab = new TabItem(tabFolder,SWT.NULL);
		propertiesTab.setText("Properties");
		Control c3 = templateProperties.createControl(tabFolder);
		propertiesTab.setControl(c3);

		return control = c;
	}
	
	public void dispose() {
		super.dispose();
			if(control != null && !control.isDisposed()) {
				control.dispose();
				control = null;
			}
	}
	
	public void validate() {
		if(validator == null) return;
		wizard.dataChanged(validator, templateName.support.getValues());
	}
	
	class NameChangeListener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {
			templateName.commit();
			validate();
		}
	}

}

class EditProjectTemplateName {
	XEntityData data;
	ProjectTemplate projectTemplate;
	XAttributeSupport support;
	PropertyChangeListener listener;
	
	public EditProjectTemplateName() {}
	
	public void init(PropertyChangeListener listener, ProjectTemplate t, XEntityData data) {
		this.listener = listener;
		projectTemplate = t;
		this.data = data;
		data.setValue("name", t.getName()); //$NON-NLS-1$
	}
	
	public Control createControl(Composite parent) {
		support = new XAttributeSupport();
		XModelObject o = PreferenceModelUtilities.getPreferenceModel().getRoot();
		support.init(o, data, false);
		support.addPropertyChangeListener(listener);
		return support.createControl(parent);
	}
	
	public void commit() {
		support.store();
		projectTemplate.setName(data.getValue("name")); //$NON-NLS-1$
	}

}

class EditPreprocessingList {
    private final static int SIZING_SELECTION_WIDGET_WIDTH = 400;
    private final static int SIZING_SELECTION_WIDGET_HEIGHT = 300;
	CustomCheckboxTreeAndListGroup selectionGroup;
	ProjectTemplate projectTemplate;
	
	public void setProjectTemplate(ProjectTemplate projectTemplate) {
		this.projectTemplate = projectTemplate;
	}

	public Control createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());

		File root = projectTemplate.getLocation();

		ArrayList input = new ArrayList();
        input.add(root);
        selectionGroup = new CustomCheckboxTreeAndListGroup(composite, input,
                new ResourceContentProvider(ResourceContentProvider.FOLDER), 
                new ResourceLabelProvider(), 
                new ResourceContentProvider(ResourceContentProvider.FILE), 
                new ResourceLabelProvider(), 
                SWT.NONE, SIZING_SELECTION_WIDGET_WIDTH, SIZING_SELECTION_WIDGET_HEIGHT);

        composite.addControlListener(new ControlListener() {
            public void controlMoved(ControlEvent e) {
            }

            public void controlResized(ControlEvent e) {
                TableColumn[] columns = selectionGroup.getListTable().getColumns();
                for (int i = 0; i < columns.length; i++) {
                    columns[i].pack();
                }
            }
        });
        selectionGroup.setTreeChecked(root, false);
        
        Iterator excluded = getIncludedResources().iterator();
        while(excluded.hasNext()) {
        	File o = (File)excluded.next();
        	if(o.isDirectory()) {
        		selectionGroup.setTreeChecked(o, true);
        	} else if(o.isFile()) {
        		selectionGroup.initialCheckListItem((File)o);
        	}
        }

        selectionGroup.setExpansions();
        selectionGroup.addCheckStateListener(new ICheckStateListener() {
			public void checkStateChanged(CheckStateChangedEvent event) {
				validate();
			}
        });
		return composite;
	}
	
	void validate() {
		Set list = new HashSet();
		list.clear();
		Iterator it = selectionGroup.getAllCheckedListItems();
		while(it.hasNext()) {
			list.add((File)it.next());
		}
		File root = projectTemplate.getLocation();
		File[] fs = (File[])list.toArray(new File[0]);
		TreeSet set = new TreeSet();
		for (int i = 0; i < fs.length; i++)	addSelected(set, fs[i], root);
		ArrayList result = projectTemplate.getPreprocessingFiles();
		result.clear();
		result.addAll(set);
	}
	
	void addSelected(Set set, File f, File root) {
		String n = f.getAbsolutePath().replace('\\', '/');
		String r = root.getAbsolutePath().replace('\\', '/') + "/"; //$NON-NLS-1$
		if(n.startsWith(r)) {
			n = n.substring(r.length());
			set.add(n);
		}
	}
	
	Set getIncludedResources() {
		Set set = new HashSet();
		List list = projectTemplate.getPreprocessingFiles();
		for (int i = 0; i < list.size(); i++) {
			String relativePath = list.get(i).toString();
			File f = new File(projectTemplate.getLocation(), relativePath);
			set.add(f);
		}
		return set;
	}
	
}

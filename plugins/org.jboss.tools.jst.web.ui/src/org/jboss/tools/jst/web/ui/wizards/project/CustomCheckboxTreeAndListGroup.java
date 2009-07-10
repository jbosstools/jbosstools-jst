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

import java.io.File;
import java.lang.reflect.Field;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.internal.ide.misc.*;
import org.jboss.tools.jst.web.WebModelPlugin;
import org.jboss.tools.jst.web.ui.WebUiPlugin;

public class CustomCheckboxTreeAndListGroup extends CheckboxTreeAndListGroup {
	Class cls = CheckboxTreeAndListGroup.class;

	public CustomCheckboxTreeAndListGroup(Composite parent, Object rootObject, ITreeContentProvider treeContentProvider, ILabelProvider treeLabelProvider, IStructuredContentProvider listContentProvider, ILabelProvider listLabelProvider, int style, int width, int height) {
		super(parent, rootObject, treeContentProvider, treeLabelProvider,
				listContentProvider, listLabelProvider, style, width, height);
	}

    public void setTreeChecked(Object treeElement, boolean state) {
    	super.setTreeChecked(treeElement, state);
    }

    public void initialUncheckListItem(IResource listElement) {
    	setCurrentTreeSelection(listElement.getParent());
    	super.listItemChecked(listElement, false, true);
    	setCurrentTreeSelection(null);
    }

    public void initialCheckListItem(File listElement) {
   		super.listItemChecked(listElement, true, true);
    	setCurrentTreeSelection(null);
    }
    
    public CheckboxTreeViewer getTreeViewer() {
    	CheckboxTreeViewer viewer = null;
    	try {
    		Field f = cls.getDeclaredField("treeViewer"); //$NON-NLS-1$
    		f.setAccessible(true);
    		viewer = (CheckboxTreeViewer)f.get(this);
		} catch (SecurityException e) {
			WebUiPlugin.getPluginLog().logError(e);
		} catch (NoSuchFieldException e) {
			WebUiPlugin.getPluginLog().logError(e);
		} catch (IllegalArgumentException e) {
			WebUiPlugin.getPluginLog().logError(e);
		} catch (IllegalAccessException e) {
			WebUiPlugin.getPluginLog().logError(e);
		}
		return viewer;
    }
    
    public void setExpansions() {
    	CheckboxTreeViewer viewer = getTreeViewer();
    	if(viewer == null) return;
    	Object[] os = viewer.getCheckedElements();
    	for (int i = 0; i < os.length; i++) {
    		viewer.setExpandedState(os[i], true);
    	}
    }
    
    private void setCurrentTreeSelection(Object element) {
		Field f;
		try {
			f = cls.getDeclaredField("currentTreeSelection"); //$NON-NLS-1$
    		f.setAccessible(true);
    		f.set(this, element);
		} catch (SecurityException e) {
			WebUiPlugin.getPluginLog().logError(e);
		} catch (NoSuchFieldException e) {
			WebUiPlugin.getPluginLog().logError(e);
		} catch (IllegalArgumentException e) {
			WebUiPlugin.getPluginLog().logError(e);
		} catch (IllegalAccessException e) {
			WebUiPlugin.getPluginLog().logError(e);
		}
    }
}

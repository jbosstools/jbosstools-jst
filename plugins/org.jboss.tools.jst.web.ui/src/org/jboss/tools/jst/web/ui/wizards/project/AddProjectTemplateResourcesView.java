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

import java.util.*;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import org.jboss.tools.common.model.ui.wizards.special.AbstractSpecialWizardStep;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.model.*;
import org.jboss.tools.jst.web.project.handlers.*;
import org.jboss.tools.jst.web.ui.WebUiPlugin;

public class AddProjectTemplateResourcesView extends AbstractSpecialWizardStep {
    private final static int SIZING_SELECTION_WIDGET_WIDTH = 400;
    private final static int SIZING_SELECTION_WIDGET_HEIGHT = 300;
    private CustomCheckboxTreeAndListGroup selectionGroup;
    AddProjectTemplateResourcesStep step;

	public Control createControl(Composite parent) {
		step = (AddProjectTemplateResourcesStep)support.getProperties().get("ResourcesStep"); //$NON-NLS-1$
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());

		IProject root = ((AddProjectTemplateSupport)support).getSelectedProject();

		ArrayList<IProject> input = new ArrayList<IProject>();
        input.add(root);
        selectionGroup = new CustomCheckboxTreeAndListGroup(composite, input,
                createTreeContent(), WorkbenchLabelProvider.getDecoratingWorkbenchLabelProvider(),
                getResourceProvider(IResource.FILE), WorkbenchLabelProvider
                        .getDecoratingWorkbenchLabelProvider(), SWT.NONE,
                SIZING_SELECTION_WIDGET_WIDTH, SIZING_SELECTION_WIDGET_HEIGHT);

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
        selectionGroup.setTreeChecked(root, true);
        Iterator excluded = step.getExcludedResources().iterator();
        while(excluded.hasNext()) {
        	Object o = root.getParent().findMember(excluded.next().toString());
        	if(!(o instanceof IFile)) {
        		selectionGroup.setTreeChecked(o, false);
        	} else if(o instanceof IResource) {
        		selectionGroup.initialUncheckListItem((IResource)o);
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
	
	private ITreeContentProvider createTreeContent() {
		return getResourceProvider(IResource.FOLDER | IResource.PROJECT | IResource.ROOT);
	}

    private ITreeContentProvider getResourceProvider(final int resourceType) {
        return new WorkbenchContentProvider() {
            public Object[] getChildren(Object o) {
                if (o instanceof IContainer) {
                    IResource[] members = null;
                    try {
                        members = ((IContainer) o).members();
                    } catch (CoreException e) {
                    	WebUiPlugin.getPluginLog().logError(e);
                        //just return an empty set of children
                        return new Object[0];
                    }

                    //filter out the desired resource types
                    ArrayList<IResource> results = new ArrayList<IResource>();
                    for (int i = 0; i < members.length; i++) {
                        //And the test bits with the resource types to see if they are what we want
                        if ((members[i].getType() & resourceType) > 0) {
                            results.add(members[i]);
                        }
                    }
                    return results.toArray();
                }
                //input element case
                if (o instanceof ArrayList) {
                    return ((ArrayList) o).toArray();
                } 
                return new Object[0];
            }
        };
    }

	public void validate() {
		Set<String> list = new HashSet<String>();
		list.clear();
		Iterator it = selectionGroup.getAllCheckedTreeItems().iterator();
		while(it.hasNext()) {
			String path = ((IResource)it.next()).getFullPath().toString();
			list.add(path);
		}
		it = selectionGroup.getAllCheckedListItems();
		while(it.hasNext()) {
			String path = ((IResource)it.next()).getFullPath().toString();
			list.add(path);
		}
		step.setSelectedResources(list);
		wizard.dataChanged(validator, new Properties());
	}

}

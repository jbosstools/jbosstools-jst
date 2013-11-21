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

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.ISaveablePart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.Page;
import org.eclipse.ui.part.PageBookView;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.IPropertySourceProvider;
import org.eclipse.wst.sse.ui.views.properties.PropertySheetConfiguration;
import org.jboss.tools.jst.jsp.outline.IFormPropertySheetPage;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class FormPropertySheetPage extends Page implements IFormPropertySheetPage, IPropertySheetPage, IAdaptable {
	private IPropertySheetModel model;

	private FormPropertySheetViewer viewer;
	private IWorkbenchPart sourcePart;
	private PartListener partListener = new PartListener();

	private RemoveAction removeAction = new RemoveAction(this);
	
	/**
	 * Part listener which cleans up this page when the source part is closed.
	 * This is hooked only when there is a source part. 
	 */
	private class PartListener implements IPartListener {
		public void partActivated(IWorkbenchPart part) {
		}

		public void partBroughtToTop(IWorkbenchPart part) {
		}

		public void partClosed(IWorkbenchPart part) {
			if (sourcePart == part) {
				if (sourcePart != null)
					sourcePart.getSite().getPage().removePartListener(partListener);
				sourcePart = null;
				if(model != null) {
					model.setWorkbenchPart(null);
				}
				if (viewer != null && !viewer.getControl().isDisposed()) {
					viewer.setInput(new Object[0]);
				}
			}
		}

		public void partDeactivated(IWorkbenchPart part) {
		}

		public void partOpened(IWorkbenchPart part) {
		}
	}

	private class NullPropertySheetConfiguration extends PropertySheetConfiguration {
		public IPropertySourceProvider getPropertySourceProvider(IPropertySheetPage page) {
			return null;
		}
	}
	private final PropertySheetConfiguration NULL_CONFIGURATION = new NullPropertySheetConfiguration();

	private PropertySheetConfiguration fConfiguration;
	private ISelection fInput = null;
	private Object[] fSelectedEntries = null;

	public FormPropertySheetPage() {}

	public IPropertySheetModel getModel() {
		return model;
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if(fConfiguration != null && sourcePart == null && selection == null) {
			//initialization
			sourcePart = part;
			return;
		}
		// skip if we're the source of this selection
		if (part != null && part instanceof PageBookView) {
			if (((PageBookView) part).getCurrentPage() == this)
				return;
		}
		if (getControl() != null && !getControl().isDisposed() && getControl().isVisible()) {
			if(sourcePart != null && sourcePart != part) {
				//Each sheet page is bound to its editor.
				return;
			}
			ISelection preferredSelection = getConfiguration().getInputSelection(part, selection);
			if (!preferredSelection.equals(fInput)) {
				fInput = preferredSelection;
				fSelectedEntries = null;
		        if (sourcePart != null) {
		        	sourcePart.getSite().getPage().removePartListener(partListener);
		        	sourcePart = null;
		        }		        
		        // change the viewer input since the workbench selection has changed.
		        if (selection instanceof IStructuredSelection) {
		        	sourcePart = part;
		        	if(model != null) {
		        		model.setWorkbenchPart(sourcePart);
		        	}
		            viewer.setInput(((IStructuredSelection) preferredSelection).toArray());
		        }
		        if (sourcePart != null) {
		        	sourcePart.getSite().getPage().addPartListener(partListener);
		        }
			}
		}
	}

	@Override
	public Object getAdapter(Class adapter) {
		if (ISaveablePart.class.equals(adapter)) {
			return getSaveablePart();
		}
    	return null;
	}

	protected ISaveablePart getSaveablePart() {
		if (sourcePart instanceof ISaveablePart) {
			return (ISaveablePart) sourcePart;
		}
		return null;
	}

	@Override
	public void createControl(Composite parent) {
		viewer = new FormPropertySheetViewer(parent);
		if(model == null) {
			model = new FormPropertySheetModel();
		}
		viewer.setModel(model);
		
		setPropertySourceProvider(getConfiguration().getPropertySourceProvider(this));

		
        viewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                handleEntrySelection(event.getSelection());
            }
        });
	}

	public void dispose() {
		setConfiguration(null);
		getSite().getWorkbenchWindow().getSelectionService().removePostSelectionListener(this);
		fSelectedEntries = null;
		fInput = null;
		super.dispose();
	}

	public void init(IPageSite pageSite) {
		super.init(pageSite);
		pageSite.getWorkbenchWindow().getSelectionService().addPostSelectionListener(this);
	}

	public void handleEntrySelection(ISelection selection) {
		if (getControl() != null && !getControl().isDisposed() && selection != null) {
			// see PropertySheetPage.handleEntrySelection(selection);
			if (selection instanceof IStructuredSelection) {
				fSelectedEntries = ((IStructuredSelection) selection).toArray();
			}
			else {
				fSelectedEntries = null;
			}
			IPropertyDescriptor descriptor = getDescriptor(selection);
			boolean isEnabled = descriptor != null && model.getPropertySource().isPropertySet(descriptor.getId());
			removeAction.setEnabled(isEnabled);
			String tooltip = removeAction.getText() + (descriptor != null ? " " + descriptor.getDisplayName() : "");
			removeAction.setToolTipText(tooltip);
		}
	}

	private IPropertyDescriptor getDescriptor(ISelection selection) {
		if(selection.isEmpty()) return null;
		if(selection instanceof IStructuredSelection) {
			Object o = ((IStructuredSelection)selection).getFirstElement();
			if(o instanceof IPropertyDescriptor) {
				return (IPropertyDescriptor)o;
			}
		}
		return null;
	}

	@Override
	public Control getControl() {
		return viewer == null ? null : viewer.getControl();
	}

	@Override
	public void setFocus() {
		if(viewer != null) {
			viewer.setFocus();
		}		
	}

	@Override
	public void setConfiguration(PropertySheetConfiguration configuration) {
		if (fConfiguration != null) {
//			fConfiguration.removeContributions(fMenuManager, fToolBarManager, fStatusLineManager);
			fConfiguration.unconfigure();
		}

		fConfiguration = configuration;

		if (fConfiguration != null) {
			setPropertySourceProvider(fConfiguration.getPropertySourceProvider(this));
//			fConfiguration.addContributions(fMenuManager, fToolBarManager, fStatusLineManager);
		}
	}

	public PropertySheetConfiguration getConfiguration() {
		if (fConfiguration == null)
			fConfiguration = NULL_CONFIGURATION;
		return fConfiguration;
	}

    public void setPropertySourceProvider(IPropertySourceProvider provider) {
		if(model == null) {
			model = new FormPropertySheetModel();
		}
        model.setPropertySourceProvider(provider);
        if(viewer != null) {
        	viewer.setModel(model);
        }
    }

    public void makeContributions(IMenuManager menuManager,
            IToolBarManager toolBarManager, IStatusLineManager statusLineManager) {
    	//see PropertySheetPage.makeContributions
    	 toolBarManager.add(removeAction);
    }

    private void makeActions() {
    	//see PropertySheetPage.makeContributions
    }

    @Override
    public void refresh() {
        if (viewer == null) {
			return;
		}
        // calling setInput on the viewer will cause the model to refresh
        viewer.setInput(viewer.getInput());
    }

    /**
     * Access for tests.
     * @return
     */
    public final FormPropertySheetViewer getViewer() {
    	return viewer;
    }
}

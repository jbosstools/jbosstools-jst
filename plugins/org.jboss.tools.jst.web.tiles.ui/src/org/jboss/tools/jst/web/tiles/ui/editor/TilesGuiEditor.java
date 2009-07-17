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
package org.jboss.tools.jst.web.tiles.ui.editor;

import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.layout.GridData;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.jboss.tools.common.editor.AbstractSectionEditor;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.ui.editor.IModelObjectEditorInput;
import org.jboss.tools.jst.web.tiles.ui.TilesUIPlugin;
import org.jboss.tools.jst.web.tiles.ui.editor.model.impl.TilesModel;
import org.xml.sax.SAXException;

public class TilesGuiEditor extends AbstractSectionEditor {
	private TilesEditor gui = null;
	private IModelObjectEditorInput input;
	private boolean isInitialized = false;
	private XModelObject installedProcess = null;
	private ActionRegistry actionRegistry;
	private TilesModel model;
	
	protected boolean isWrongEntity(String entity) {
		return !entity.equals("FileTiles"); //$NON-NLS-1$
	}
	
	public void setInput(IEditorInput input) {
		super.setInput(input);
		if(input instanceof IModelObjectEditorInput) {		
			this.input = (IModelObjectEditorInput)input;
		}
	}
	
    public TilesEditor getGUI(){
    	return gui;
    }

	public ISelectionProvider getSelectionProvider() {
		return (gui == null) ? null : gui.getModelSelectionProvider();
	}

	protected XModelObject getInstalledObject() {
		return installedProcess;
	}
	
	protected void updateGui() {
		if(object != installedProcess) disposeGui(); else if(isInitialized) return;
		isInitialized = true;
		installedProcess = object;
		guiControl.setVisible(object != null);
		if(object == null) return;
		try {
            gui = new TilesEditor((IFileEditorInput)input);
            model = new TilesModel(object);
            model.updateLinks();

			gui.setTilesModel(model);

			gui.init((IEditorSite)getSite(), (IEditorInput)input);
			gui.createPartControl(guiControl);
			control = guiControl.getChildren()[0];
			control.setLayoutData(new GridData(GridData.FILL_BOTH));
			guiControl.layout();
			wrapper.update();
			wrapper.layout();
			
		} catch (SAXException ex) {
			TilesUIPlugin.getPluginLog().logError(ex);
		} catch (PartInitException ex) {
			TilesUIPlugin.getPluginLog().logError(ex);
		}
	}
	
	public TilesEditor getTilesDiagram(){
		return this.gui;
	}
	
  	public void dispose() {
		if(model != null) model.dispose();
		model = null;
		super.dispose();
  	}
	
	protected void disposeGui() {
		if(gui != null) {
			gui.dispose();
			gui = null;
			control.dispose(); 
			control = null;
		}
	}
	protected ActionRegistry getActionRegistry() {
			if (actionRegistry == null)
				actionRegistry = new ActionRegistry();
			return actionRegistry;
		}
	
	protected void createActions() {
	}
	
	public Object getAdapter(Class adapter) {
		if(adapter == ActionRegistry.class || adapter == org.eclipse.gef.editparts.ZoomManager.class){
				if(gui != null)
					return gui.getAdapter(adapter);
		}
		return super.getAdapter(adapter);
	}

}

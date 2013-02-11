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
package org.jboss.tools.jst.web.ui.palette;

import java.net.URL;
import java.net.MalformedURLException;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.core.runtime.Platform;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.event.XModelTreeEvent;
import org.jboss.tools.common.model.event.XModelTreeListener;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.common.model.ui.ModelUIPlugin;
import org.jboss.tools.common.model.ui.views.palette.IPaletteAdapter;
import org.jboss.tools.common.model.ui.views.palette.IPalettePageAdapter;
import org.jboss.tools.common.model.ui.views.palette.PaletteContents;
import org.jboss.tools.common.model.ui.views.palette.PaletteViewPart;
import org.jboss.tools.jst.web.ui.WebUiPlugin;
import org.jboss.tools.jst.web.ui.palette.model.PaletteModel;
import org.jboss.tools.jst.web.ui.palette.model.PaletteRoot;
import org.jboss.tools.jst.web.ui.palette.PaletteUIMessages;

public class PaletteAdapter implements IPaletteAdapter {
	private static final URL BASE_URL = EclipseResourceUtil.getInstallURL(Platform.getBundle(ModelUIPlugin.PLUGIN_ID));
	private static final String IMAGE_PATH = "images/xstudio/palette/"; //$NON-NLS-1$

	private IPalettePageAdapter viewPart = null;
	private PaletteModel model = null; 
	private PaletteViewer viewer = null;
	private Control palette = null;
	private DescriptionManager descriptionManager = null;
	private DropTargetManager dropManager = null;
	private PaletteModelListener modelListener = null;
	private PaletteContents paletteContents;
	
	public void setPaletteViewPart(IPalettePageAdapter viewPart) {
		this.viewPart = viewPart;
	}

	public void initActionBars() {
		IActionBars bars = viewPart.getActionBars();
		if(bars != null) {
			bars.getToolBarManager().add(new PaletteEditAction());
			bars.getToolBarManager().add(new ShowHideTabsAction());
			bars.getToolBarManager().add(new ImportTLDAction());
		}
	}

	public Control createControl(Composite root) {
		model = PaletteModel.getInstance(paletteContents);
		viewer = new PaletteViewer(viewPart, model);
		palette = viewer.createControl(root);
		
		viewer.setPaletteViewerPreferences(new PaletteViewerPreferences());
		PaletteRoot paletteRoot = model.getPaletteRoot();
		viewer.setPaletteRoot(paletteRoot);

//		initActionBars();
		
		descriptionManager = new DescriptionManager(viewer);
		descriptionManager.install(palette);

		dropManager = new DropTargetManager(viewer, model); 
		dropManager.install(palette);

		modelListener = new PaletteModelListener();
		model.addModelTreeListener(modelListener);

		viewer.addDragStartSupport();

		return palette;
	}
	
	public void setEnabled(boolean enabled) {
		if(viewer != null) {
			viewer.setEnabled(enabled);
		}
	}
	
	public void dispose() {
		model.removeModelTreeListener(modelListener);
		dropManager.dispose();
		descriptionManager.dispose();
		viewPart.getActionBars().getToolBarManager().removeAll();
		viewer = null;
		viewPart = null;
	}

	public void setPaletteContents(PaletteContents contents) {
		paletteContents = contents;
		if (model != null) {
			model.setPaletteContents(contents);
		}
	}

	private void reload(XModelObject lastAddedXCat) {
		if(viewer != null) {
			viewer.deselectAll();
		}
		model.load(lastAddedXCat);
		setEnabled(true);
///		setEnabled(viewPart.idEnabled());
	}

	private ImageDescriptor getImageDescriptor(String fileName) {
		try {
			URL url = new URL(BASE_URL, IMAGE_PATH + fileName);
			return ImageDescriptor.createFromURL(url);
		} catch (MalformedURLException e) {
			WebUiPlugin.getPluginLog().logError(e);
		}
		return ImageDescriptor.getMissingImageDescriptor();
	}
	
	private class PaletteEditAction extends Action {
		public PaletteEditAction() {
			super(PaletteUIMessages.PALETTE_EDITOR, PaletteAdapter.this.getImageDescriptor("palette_editor.gif")); //$NON-NLS-1$
			setToolTipText(PaletteUIMessages.PALETTE_EDITOR);
		}
		
		public void run() {
			model.openEditor(palette.getShell());			
		}
	}
	
	private class ShowHideTabsAction extends Action {
		public ShowHideTabsAction() {
			super(PaletteUIMessages.SHOW_HIDE_TABS, PaletteAdapter.this.getImageDescriptor("visibility.gif")); //$NON-NLS-1$
			setToolTipText(PaletteUIMessages.SHOW_HIDE);
		}
		public void run() {
			model.runShowHideDialog();			
		}
	}

	private class ImportTLDAction extends Action {
		public ImportTLDAction() {
			super(PaletteUIMessages.IMPORT_TLD, PaletteAdapter.this.getImageDescriptor("tld_import.gif")); //$NON-NLS-1$
			setToolTipText(PaletteUIMessages.IMPORT);
		}
		public void run() {
			model.runImportTLDDialog();			
		}
	}


	private class PaletteModelListener implements XModelTreeListener {
		private boolean isTransaction = false;
		private boolean isDirty = false;
		private XModelObject lastAddedCat = null;
	
		public void nodeChanged(XModelTreeEvent event) {
			run(event);
		}

		public void structureChanged(XModelTreeEvent event) {
			run(event);
		}

		private void run(XModelTreeEvent event) {
			if("transaction_begin".equals(event.getInfo())) { //$NON-NLS-1$
				isTransaction = true;
				return;
			}
			if("transaction_end".equals(event.getInfo())) { //$NON-NLS-1$
				isTransaction = false;
				if (isDirty) {
					isDirty = false;
					reload(lastAddedCat);
					lastAddedCat = null; 
				}
				return;
			}
			XModel xmodel = model.getXModel();
			XModelObject exo = event.getModelObject();
			boolean q = event.kind() == XModelTreeEvent.STRUCTURE_CHANGED && xmodel.getRoot().getPath().equals(exo.getPath());
			XModelObject xroot = xmodel.getRoot("Palette"); //$NON-NLS-1$
			if (xroot == null || isExist(xroot, event.getModelObject()) || q) {
				if (event.kind() == XModelTreeEvent.CHILD_ADDED && xroot != null && xroot.getPath().equals(exo.getPath())) {
					Object info = event.getInfo();
					if (info instanceof XModelObject) {
						lastAddedCat = (XModelObject)info; 
					}
				}
				if(isTransaction) {
					isDirty = true;
				} else {
					reload(lastAddedCat);
					lastAddedCat = null;
				}
			}
		}

		private boolean isExist(XModelObject root, XModelObject modelObject) {
			if (root == null || modelObject==null) {
				return false;
			}
			if (modelObject.getPath() == null || root.getPath() == null) {
				return false;
			}
			return (modelObject.getPath() + "/").startsWith(root.getPath() + "/"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * Test method
	 * @return
	 */
	public PaletteViewer getViewer() {
		return viewer;
	}

}

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

import java.io.File;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IStorage;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.widgets.Control;
import org.jboss.tools.common.model.XModelException;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.event.ActionDeclinedException;
import org.jboss.tools.common.model.project.IModelNature;
import org.jboss.tools.common.model.ui.dnd.DnDUtil;
import org.jboss.tools.common.model.ui.dnd.ModelTransfer;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.web.ui.WebUiPlugin;
import org.jboss.tools.jst.web.ui.palette.model.IPaletteModel;
import org.jboss.tools.jst.web.ui.palette.model.PaletteModel;
import org.jboss.tools.jst.web.ui.palette.model.PaletteXModelObject;

public class DropTargetManager extends DropTargetAdapter {
	private DropTarget target = null;
	private PaletteViewer viewer;
	private PaletteModel model;
	
	public DropTargetManager(PaletteViewer viewer, IPaletteModel model) {
		this.viewer = viewer;
		this.model = (PaletteModel)model;
	}
	
	public void install(Control palette) {
		dispose();
		Transfer[] types = new Transfer[]{
				ModelTransfer.getInstance(),
				FileTransfer.getInstance(),
				LocalSelectionTransfer.getTransfer()
		};
		target = new DropTarget(palette, DND.DROP_MOVE | DND.DROP_COPY);
		target.setTransfer(types);
		target.addDropListener(this);		
	}

	public void dispose() {
		if (target != null) {
			if(!target.isDisposed()) {
				target.removeDropListener(this);
				target.dispose();
			} 		
			target = null;
		}
	}

	public TransferData getSupportedData(TransferData[] data, Transfer transfer) {
		for (int i = 0; i < data.length; i++) {
			if(transfer.isSupportedType(data[i])) return data[i];
		}
		return null;
	}

	public void dragOver(DropTargetEvent event) {
		XModelObject o = null;
		if(LocalSelectionTransfer.getTransfer().isSupportedType(event.currentDataType)) {
			XModelObject fo = getObjectFromLocalTransfer();
			if(fo != null && DnDUtil.isCopyEnabled(fo, null)) {
				DnDUtil.copy(fo, null);
				o = getModelObject(event.x, event.y);
			}
		} else if(FileTransfer.getInstance().isSupportedType(event.currentDataType)) {
			event.detail = DND.DROP_COPY;
			return;
//		} else if (event.currentDataType.type != ModelTransfer.MODEL_ID) {
		} else if (!ModelTransfer.getInstance().isSupportedType(event.currentDataType)) {
			o = null;
		} else {
			o = getModelObject(event.x, event.y);
		}
		boolean enabled = o != null && DnDUtil.isPasteEnabled(o); 
		event.detail = enabled ? DND.DROP_COPY : DND.DROP_NONE;
	}

	public void drop(DropTargetEvent event) {
		XModelObject o = null;
		if(FileTransfer.getInstance().isSupportedType(event.currentDataType)) {
			File f = getFile(event);
			if(f != null) {
				IFile ef = EclipseResourceUtil.getFile(f.getAbsolutePath());
				XModelObject fo = (ef == null) ? null : EclipseResourceUtil.getObjectByResource(ef);
				if(fo != null && DnDUtil.isCopyEnabled(fo, null)) {
					DnDUtil.copy(fo, null);
					o = getModelObject(event.x, event.y);
				}
			}
		} else if(LocalSelectionTransfer.getTransfer().isSupportedType(event.currentDataType)) {
			XModelObject fo = getObjectFromLocalTransfer();
			if(fo != null && DnDUtil.isCopyEnabled(fo, null)) {
				DnDUtil.copy(fo, null);
				o = getModelObject(event.x, event.y);
			}
		} else if(!ModelTransfer.getInstance().isSupportedType(event.currentDataType)) {
			o = null;
		} else {
			o = getModelObject(event.x, event.y);
		}
		if(o != null) {
			try {
				DnDUtil.paste(o, new Properties());
				model.saveOptions();
			} catch (ActionDeclinedException de) {
				//ignore
			} catch (XModelException e) {
				WebUiPlugin.getDefault().logError(e);
			}
		} else {
			event.detail = DND.DROP_NONE;
		}
	}
		
	private XModelObject getModelObject(int x, int y) {
		Control palette = target.getControl();
		org.eclipse.swt.graphics.Point p = palette.toControl(x, y);
		EditPart part = viewer.findObjectAt(new Point(p.x, p.y));
		if (part instanceof GraphicalEditPart) {
			Object model = part.getModel();
			if (model instanceof PaletteXModelObject) {
				return ((PaletteXModelObject)model).getXModelObject();
			} 
		}
		return null;
	}

	private File getFile(DropTargetEvent event) {
		String[] s = (String[])event.data;
		return(s == null || s.length == 0) ? null : new File(s[0]);
	}
	
	XModelObject getObjectFromLocalTransfer() {
		ISelection s = LocalSelectionTransfer.getTransfer().getSelection();
		if(!(s instanceof IStructuredSelection)) return null;
		IStructuredSelection ss = (IStructuredSelection)s;
		Object obj = ss.getFirstElement();
		if(obj instanceof IStorage && obj.getClass().getName().indexOf("JarEntryFile") >= 0) {  //$NON-NLS-1$
			IStorage st = (IStorage)obj;
			return getObject(st);
		} else if(obj instanceof IFile) {
			return EclipseResourceUtil.getObjectByResource((IFile)obj);
		} else if(obj instanceof XModelObject) {
			return (XModelObject)obj;
		}
		return null;
	}
	
	XModelObject getObject(IStorage st) {
		String str = st.toString();
		IProject p = getProject(str);
		String path = "/" + st.getFullPath().toString(); //$NON-NLS-1$
		IModelNature n = EclipseResourceUtil.getModelNature(p);
		if(n != null) {
			return n.getModel().getByPath(path);
		}
		return null;
	}
	
	IProject getProject(String jarEntryString) {
		if(jarEntryString == null || !jarEntryString.startsWith("JarEntryFile[")) return null; //$NON-NLS-1$
		jarEntryString = jarEntryString.substring("JarEntryFile[".length()); //$NON-NLS-1$
		int i = jarEntryString.indexOf("::"); //$NON-NLS-1$
		if(i < 0) return null;
		String zip = jarEntryString.substring(0, i);
		IFile f = EclipseResourceUtil.getFile(zip);
		return f == null ? null : f.getProject();
	}

}

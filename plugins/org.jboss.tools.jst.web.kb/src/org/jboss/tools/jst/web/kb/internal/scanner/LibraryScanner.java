/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.internal.scanner;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.XModelObjectConstants;
import org.jboss.tools.common.model.filesystems.impl.FileSystemsImpl;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.web.kb.IKbProject;
import org.jboss.tools.jst.web.model.helpers.InnerModelHelper;

/**
 * @author Viacheslav Kabanovich
 */
public class LibraryScanner implements IFileScanner {
	//Now it is absolute file on disk
	IPath sourcePath = null;
	
	public LibraryScanner() {}
	
	public boolean isRelevant(IFile f) {
		if(EclipseResourceUtil.isJar(f.getName())) return true;
		return false;
	}

	public boolean isLikelyComponentSource(IFile f) {
		XModel model = InnerModelHelper.createXModel(f.getProject());
		if(model == null) return false;
		XModelObject o = EclipseResourceUtil.getObjectByResource(model, f);
		if(o == null) return false;
		if(!o.getModelEntity().getName().equals("FileSystemJar")) { //$NON-NLS-1$
			((FileSystemsImpl)o.getModel().getByPath("FileSystems")).updateOverlapped(); //$NON-NLS-1$
			o = EclipseResourceUtil.getObjectByResource(f);
			if(o == null || !o.getModelEntity().getName().equals("FileSystemJar")) return false; //$NON-NLS-1$
		}
		return isLikelyComponentSource(o);
	}

	public LoadedDeclarations parse(IFile f, IKbProject sp) throws ScannerException {
		XModel model = InnerModelHelper.createXModel(f.getProject());
		if(model == null) return null;
		XModelObject o = EclipseResourceUtil.getObjectByResource(model, f);
		if(o == null) return null;
		if(!o.getModelEntity().getName().equals("FileSystemJar")) { //$NON-NLS-1$
			((FileSystemsImpl)o.getModel().getByPath("FileSystems")).updateOverlapped(); //$NON-NLS-1$
			o = EclipseResourceUtil.getObjectByResource(f);
			if(o == null || !o.getModelEntity().getName().equals("FileSystemJar")) return null; //$NON-NLS-1$
		}
		return parse(o, f.getFullPath(), sp);
	}

	public boolean isLikelyComponentSource(XModelObject o) {
		if(o == null) return false;
		if(o.getChildByPath("META-INF") != null) return true; //$NON-NLS-1$
		return false;
	}

	public LoadedDeclarations parse(XModelObject o, IPath path, IKbProject sp) throws ScannerException {
		if(o == null) return null;
		sourcePath = path;
		XModelObject metaInf = o.getChildByPath("META-INF"); //$NON-NLS-1$
		if(metaInf == null) return null;
		
		LoadedDeclarations ds = new LoadedDeclarations();

		if(metaInf != null) {
			XModelObject[] tlds = metaInf.getChildren();
			for (XModelObject tld: tlds) {
				if(isFaceletTaglibFile(tld) || isTLDFile(tld) || isFacesConfigFile(tld)) {
					XMLScanner s = new XMLScanner();				
					LoadedDeclarations ds1 = s.parse(tld, path, sp);
					if(ds1 != null) ds.add(ds1);
				}
				if(tld.getFileType() == XModelObject.FOLDER && tld.getAttributeValue(XModelObjectConstants.ATTR_NAME).equals("resources")) { //$NON-NLS-1$
					JSF2ResourcesScanner s = new JSF2ResourcesScanner();
					LoadedDeclarations ds1 = s.parse(tld, path, sp);
					if(ds1 != null) ds.add(ds1);
				}
			}
		}

		return ds;
	}

	public static boolean isTLDFile(XModelObject o) {
		if(o == null) return false;
		String entity = o.getModelEntity().getName();
		if(entity.startsWith("FileTLD")) return true; //$NON-NLS-1$
		return false;
	}

	public static boolean isFaceletTaglibFile(XModelObject o) {
		if(o == null) return false;
		String entity = o.getModelEntity().getName();
		if(entity.startsWith("FileFaceletTaglib")) return true; //$NON-NLS-1$
		return false;
	}
	
	public static boolean isFacesConfigFile(XModelObject o) {
		if(o == null) return false;
		String entity = o.getModelEntity().getName();
		if(entity.startsWith("FacesConfig")) return true; //$NON-NLS-1$
		return false;
	}

	public static boolean isCompositeComponentFile(XModelObject o) {
		if(o == null) return false;
		String entity = o.getModelEntity().getName();
		if(entity.startsWith(JSF2ResourcesScanner.ENT_COMPOSITE_COMPONENT)) return true; //$NON-NLS-1$
		return false;
	}
	
}


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

import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.XModelObjectConstants;
import org.jboss.tools.common.model.filesystems.impl.FileAnyImpl;
import org.jboss.tools.common.model.filesystems.impl.FileSystemsImpl;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.web.kb.IKbProject;
import org.jboss.tools.jst.web.model.helpers.InnerModelHelper;

/**
 * @author Viacheslav Kabanovich
 */
public class LibraryScanner implements IFileScanner {
	
	public static final String FILE_SYSTEMS_PATH = "FileSystems"; //$NON-NLS-1$
	public static final String FILE_SYSTEM_JAR_PATH = "FileSystemJar"; //$NON-NLS-1$
	public static final String META_INF_PATH = "META-INF"; //$NON-NLS-1$
	
	public static final String JAR_SUFFIX = ".jar";  //$NON-NLS-1$
	public static final String ZIP_SUFFIX = ".zip"; //$NON-NLS-1$
	
	//Now it is absolute file on disk
	IPath sourcePath = null;
	
	public LibraryScanner() {}
	
	public boolean isRelevant(IFile f) {
		String name = f.getName().toLowerCase();
		return name.endsWith(JAR_SUFFIX) || name.endsWith(ZIP_SUFFIX);
	}

	public boolean isLikelyComponentSource(IFile f) {
		XModel model = InnerModelHelper.createXModel(f.getProject());
		boolean result = false;
		if(model != null) {
			XModelObject o = EclipseResourceUtil.getObjectByResource(model, f);
			if(o != null) {
				if(o.getModelEntity().getName().equals(FILE_SYSTEM_JAR_PATH)) {
					result = isLikelyComponentSource(o);
				} else {
					((FileSystemsImpl)o.getModel().getByPath(FILE_SYSTEMS_PATH)).updateOverlapped();
					o = EclipseResourceUtil.getObjectByResource(f);
					result = o != null && o.getModelEntity().getName().equals(FILE_SYSTEM_JAR_PATH);
				}
			}
		}
		return result;
	}

	public LoadedDeclarations parse(IFile f, IKbProject sp) throws ScannerException {
		XModel model = InnerModelHelper.createXModel(f.getProject());
		XModelObject o = EclipseResourceUtil.getObjectByResource(model, f);
		if(!o.getModelEntity().getName().equals(FILE_SYSTEM_JAR_PATH)) {
			((FileSystemsImpl)o.getModel().getByPath(FILE_SYSTEMS_PATH)).updateOverlapped();
			o = EclipseResourceUtil.getObjectByResource(f);
			if(o == null || !o.getModelEntity().getName().equals(FILE_SYSTEM_JAR_PATH)) return null;
		}
		return parse(o, f.getFullPath(), sp);
	}

	public boolean isLikelyComponentSource(XModelObject o) {
		if(o == null) return false;
		if(o.getChildByPath(META_INF_PATH) != null) return true;
		return false;
	}

	public LoadedDeclarations parse(XModelObject o, IPath path, IKbProject sp) throws ScannerException {
		if(o == null) return null;
		sourcePath = path;
		XModelObject metaInf = o.getChildByPath(META_INF_PATH);
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
					Map<IPath,LoadedDeclarations> ds1 = s.parse(tld, path, sp, true);
					if(ds1 != null && ds1.size() > 0) ds.add(ds1.values().iterator().next());
				} else if(isMyFacesMetadata(tld)) {
					MyFacesScanner s = new MyFacesScanner();
					LoadedDeclarations ds1 = s.parse(tld, path, sp);
					if(ds1 != null) ds.add(ds1);		
				}
			}
		}
		XModelObject[] ps = o.getChildren();
		for (int i = 0; i < ps.length; i++) {
			if(ps[i] == metaInf || ps[i].getFileType() != XModelObject.FOLDER) continue;
			LoadedDeclarations ds1 = parseInPackages(ps[i], path, sp);
			if(ds1 != null) ds.add(ds1);
		}

		return ds;
	}

	public LoadedDeclarations parseInPackages(XModelObject o, IPath path, IKbProject sp) throws ScannerException {
		LoadedDeclarations ds = new LoadedDeclarations();
		XModelObject[] tlds = o.getChildren();
		for (XModelObject tld: tlds) {
			if(isFaceletTaglibFile(tld)) {
				XMLScanner s = new XMLScanner();				
				LoadedDeclarations ds1 = s.parse(tld, path, sp);
				ds = add(ds, ds1);
			} else if(tld.getFileType() == XModelObject.FOLDER) {
				LoadedDeclarations ds1 = parseInPackages(tld, path, sp);
				ds = add(ds, ds1);
			} else if(isMyFacesMetadata(tld)) {
				MyFacesScanner s = new MyFacesScanner();
				LoadedDeclarations ds1 = s.parse(tld, path, sp);
				ds = add(ds, ds1);				
			}
		}
		return ds;
	}
	private LoadedDeclarations add(LoadedDeclarations total, LoadedDeclarations addition) {
		if(addition == null || addition.isEmpty()) return total;
		if(total == null) total = new LoadedDeclarations();
		total.add(addition);
		return total;
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
		if(entity.startsWith(JSF2ResourcesScanner.ENT_COMPOSITE_COMPONENT)) return true;
		return false;
	}

	public static boolean isMyFacesMetadata(XModelObject o) {
		if(MyFacesScanner.METADATA_FILE_NAME.equals(o.getPathPart())) {
			return true;
		}
		return false;
	}
	
}


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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jboss.tools.common.meta.XAttribute;
import org.jboss.tools.common.meta.XModelEntity;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelException;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.filesystems.impl.FolderImpl;
import org.jboss.tools.common.model.plugin.ModelPlugin;
import org.jboss.tools.common.model.util.EclipseJavaUtil;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.common.model.util.NamespaceMapping;
import org.jboss.tools.jst.web.kb.IKbProject;
import org.jboss.tools.jst.web.model.helpers.InnerModelHelper;

/**
 * @author Viacheslav Kabanovich
 */
public class XMLScanner implements IFileScanner {
	
	public XMLScanner() {}

	/**
	 * Returns true if file is probable component source - 
	 * has components.xml name or *.component.xml mask.
	 * @param resource
	 * @return
	 */	
	public boolean isRelevant(IFile resource) {
		if(resource.getName().equals("components.xml")) return true; //$NON-NLS-1$
		if(resource.getName().endsWith(".component.xml")) return true; //$NON-NLS-1$
		return false;
	}
	
	/**
	 * This method should be called only if isRelevant returns true;
	 * Makes simple check if this java file contains annotation Name. 
	 * @param resource
	 * @return
	 */
	public boolean isLikelyComponentSource(IFile f) {
		if(!f.isSynchronized(IFile.DEPTH_ZERO) || !f.exists()) return false;
		XModel model = InnerModelHelper.createXModel(f.getProject());
		if(model == null) return false;
		XModelObject o = EclipseResourceUtil.getObjectByResource(model, f);
		if(o == null) return false;
		//TODO
		if(o.getModelEntity().getName().startsWith("FileSeamComponent")) return true; //$NON-NLS-1$
		return false;
	}

	/**
	 * Returns list of components
	 * @param f
	 * @return
	 * @throws ScannerException
	 */
	public LoadedDeclarations parse(IFile f, IKbProject sp) throws ScannerException {
		XModel model = InnerModelHelper.createXModel(f.getProject());
		if(model == null) return null;
		XModelObject o = EclipseResourceUtil.getObjectByResource(model, f);
		return parse(o, f.getFullPath(), sp);
	}
	
	static Set<String> INTERNAL_ATTRIBUTES = new HashSet<String>();
	
	static {
		INTERNAL_ATTRIBUTES.add("NAME"); //$NON-NLS-1$
		INTERNAL_ATTRIBUTES.add("EXTENSION"); //$NON-NLS-1$
		INTERNAL_ATTRIBUTES.add("#comment"); //$NON-NLS-1$
	}
	
	public LoadedDeclarations parse(XModelObject o, IPath source, IKbProject sp) {
		if(o == null) return null;

		if(o.getParent() instanceof FolderImpl) {
			IFile f = ResourcesPlugin.getWorkspace().getRoot().getFile(source);
			if(f != null && f.exists()) {
				try {
					((FolderImpl)o.getParent()).updateChildFile(o, f.getLocation().toFile());
				} catch (XModelException e) {
					ModelPlugin.getPluginLog().logError(e);
				}
				if(o.getParent() == null) {
					boolean b = isLikelyComponentSource(f);
					if(!b) return null;
					o = EclipseResourceUtil.getObjectByResource(o.getModel(), f);
					if(o == null) return null;
				}
			}
		}
		
		LoadedDeclarations ds = new LoadedDeclarations();
		XModelObject[] os = o.getChildren();
		for (int i = 0; i < os.length; i++) {
			XModelEntity componentEntity = os[i].getModelEntity();
			//TODO
//			if(os[i].getModelEntity().getName().startsWith("SeamFactory")) { //$NON-NLS-1$
//				SeamXmlFactory factory = new SeamXmlFactory();
//				factory.setId(os[i]);
//				factory.setSourcePath(source);
//				factory.setName(new XMLValueInfo(os[i], ISeamXmlComponentDeclaration.NAME));
//				factory.setValue(new XMLValueInfo(os[i], "value")); //$NON-NLS-1$
//				factory.setMethod(new XMLValueInfo(os[i], "method")); //$NON-NLS-1$
//				ds.getLibraries().add(factory);
//			}
		}
		return ds;
	}
	
}
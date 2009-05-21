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
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelException;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.filesystems.impl.FolderImpl;
import org.jboss.tools.common.model.plugin.ModelPlugin;
import org.jboss.tools.common.model.project.ext.store.XMLStoreConstants;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.web.kb.IKbProject;
import org.jboss.tools.jst.web.kb.internal.taglib.AbstractAttribute;
import org.jboss.tools.jst.web.kb.internal.taglib.AbstractComponent;
import org.jboss.tools.jst.web.kb.internal.taglib.AbstractTagLib;
import org.jboss.tools.jst.web.kb.internal.taglib.FaceletTag;
import org.jboss.tools.jst.web.kb.internal.taglib.FaceletTagLibrary;
import org.jboss.tools.jst.web.kb.internal.taglib.TLDAttribute;
import org.jboss.tools.jst.web.kb.internal.taglib.TLDLibrary;
import org.jboss.tools.jst.web.kb.internal.taglib.TLDTag;
import org.jboss.tools.jst.web.model.helpers.InnerModelHelper;
import org.jboss.tools.jst.web.model.project.ext.store.XMLValueInfo;

/**
 * @author Viacheslav Kabanovich
 */
public class XMLScanner implements IFileScanner {
	public static final String ATTR_TAGCLASS ="tagclass"; //$NON-NLS-1$
	public static final String ATTR_BODY_CONTENT = "bodycontent"; //$NON-NLS-1$
	
	public XMLScanner() {}

	/**
	 * Returns true if file is probable component source - 
	 * has components.xml name or *.component.xml mask.
	 * @param resource
	 * @return
	 */	
	public boolean isRelevant(IFile resource) {
		if(resource.getName().endsWith(".xml")) return true; //$NON-NLS-1$
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
		if(LibraryScanner.isTLDFile(o) || LibraryScanner.isFaceletTaglibFile(o)) return true;
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
		if(LibraryScanner.isTLDFile(o)) {
			parseTLD(o, source, sp, ds);
		} else if(LibraryScanner.isFaceletTaglibFile(o)) {
			parseFaceletTaglib(o, source, sp, ds);
		}
		return ds;
	}

	private void parseTLD(XModelObject o, IPath source, IKbProject sp, LoadedDeclarations ds) {
		TLDLibrary library = new TLDLibrary();
		library.setId(o);
		library.setURI(new XMLValueInfo(o, AbstractTagLib.URI));
		library.setDisplayName(new XMLValueInfo(o, TLDLibrary.DISPLAY_NAME));
		library.setShortName(new XMLValueInfo(o, "shortname"));
		String version = o.getAttributeValue(TLDLibrary.VERSION);
		if(version == null) {
			if("FileTLD_1_2".equals(o.getModelEntity().getName())) {
				version = "1.2";
			} else {
				version = "1.1";
			}
			library.setVersion(version);
		} else {
			library.setVersion(new XMLValueInfo(o, TLDLibrary.VERSION));
		}

		ds.getLibraries().add(library);

		XModelObject[] ts = o.getChildren();
		for (XModelObject t: ts) {
			if(t.getModelEntity().getName().startsWith("TLDTag")) {
				AbstractComponent tag = new TLDTag();
				tag.setId(t);

				tag.setName(new XMLValueInfo(t, XMLStoreConstants.ATTR_NAME));
				tag.setDescription(new XMLValueInfo(t, AbstractComponent.DESCRIPTION));
				tag.setComponentClass(new XMLValueInfo(t, ATTR_TAGCLASS));
				tag.setCanHaveBody(new XMLValueInfo(t, ATTR_BODY_CONTENT));
				//TODO
//				tag.setComponentType(componentType);
				
				XModelObject[] as = t.getChildren();
				for(XModelObject a: as) {
					if(a.getModelEntity().getName().startsWith("TLDAttribute")) {
						AbstractAttribute attr = new TLDAttribute();
						attr.setId(a);
						attr.setName(new XMLValueInfo(a, XMLStoreConstants.ATTR_NAME));
						attr.setDescription(new XMLValueInfo(a, AbstractComponent.DESCRIPTION));
						attr.setRequired(new XMLValueInfo(a, AbstractAttribute.REQUIRED));
						
						tag.addAttribute(attr);
					}
				}
				
				library.addComponent(tag);
			}
		}
		
	}

	private void parseFaceletTaglib(XModelObject o, IPath source, IKbProject sp, LoadedDeclarations ds) {
		FaceletTagLibrary library = new FaceletTagLibrary();
		library.setId(o);
		library.setURI(new XMLValueInfo(o, AbstractTagLib.URI));

		ds.getLibraries().add(library);

		XModelObject[] os = o.getChildren();
		for (XModelObject t: os) {
			String entity = t.getModelEntity().getName();
			if(entity.startsWith("FaceletTaglibTag")) {
				FaceletTag tag = new FaceletTag();
				tag.setId(t);
				tag.setName(new XMLValueInfo(t, "tag-name"));
				//what else?
				
				library.addComponent(tag);
			} else if(entity.startsWith("FaceletTaglibFunction")) {
				//TODO
			}
		}
	}

}
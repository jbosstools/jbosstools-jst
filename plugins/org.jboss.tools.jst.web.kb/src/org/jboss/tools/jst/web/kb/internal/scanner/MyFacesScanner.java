/******************************************************************************* 
 * Copyright (c) 2010 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/

package org.jboss.tools.jst.web.kb.internal.scanner;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.filesystems.impl.FileAnyImpl;
import org.jboss.tools.common.model.project.ext.IValueInfo;
import org.jboss.tools.common.model.project.ext.impl.ValueInfo;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.common.model.util.XModelObjectLoaderUtil;
import org.jboss.tools.common.xml.XMLEntityResolver;
import org.jboss.tools.common.xml.XMLUtilities;
import org.jboss.tools.jst.web.kb.IKbProject;
import org.jboss.tools.jst.web.kb.internal.taglib.myfaces.MyFacesAttribute;
import org.jboss.tools.jst.web.kb.internal.taglib.myfaces.MyFacesComponent;
import org.jboss.tools.jst.web.kb.internal.taglib.myfaces.MyFacesTagLibrary;
import org.jboss.tools.jst.web.kb.taglib.IAttribute;
import org.jboss.tools.jst.web.model.helpers.InnerModelHelper;
import org.w3c.dom.Element;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class MyFacesScanner implements IFileScanner {
	public static String METADATA_FILE_NAME = "myfaces-metadata.xml"; //$NON-NLS-1$
	static String F_PREFIX = "f"; //$NON-NLS-1$
	static String F_URI = "http://java.sun.com/jsf/core"; //$NON-NLS-1$

	static String H_PREFIX = "h"; //$NON-NLS-1$
	static String H_URI = "http://java.sun.com/jsf/html"; //$NON-NLS-1$

	static String UI_PREFIX = "ui"; //$NON-NLS-1$
	static String UI_URI = "http://java.sun.com/jsf/facelets"; //$NON-NLS-1$
	
	static String C_PREFIX = "c"; //$NON-NLS-1$
	static String C_URI = "http://java.sun.com/jstl/core"; //$NON-NLS-1$
//	static String C_URI = "http://java.sun.com/jsp/jstl/core"; //$NON-NLS-1$

	static String COMPOSITE_PREFIX = "composite"; //$NON-NLS-1$
	static String COMPOSITE_URI = "http://java.sun.com/jsf/composite"; //$NON-NLS-1$
	
	static Map<String, String> prefixToURI = new HashMap<String, String>();
	
	static {
		prefixToURI.put(F_PREFIX, F_URI);
		prefixToURI.put(H_PREFIX, H_URI);
		prefixToURI.put(UI_PREFIX, UI_URI);
		prefixToURI.put(C_PREFIX, C_URI);
		prefixToURI.put(COMPOSITE_PREFIX, COMPOSITE_URI);
	}

	static String TAG_PROPERTY = "property"; //$NON-NLS-1$
	static String TAG_ATTRIBUTE = "attribute"; //$NON-NLS-1$
	static String ATTR_NAME = "name.#text"; //$NON-NLS-1$
	static String ATTR_DESCRIPTION = "desc.#text"; //$NON-NLS-1$
	static String ATTR_REQUIRED = "required.#text"; //$NON-NLS-1$
	static String ATTR_PARENT_CLASS = "parentClassName.#text"; //$NON-NLS-1$
	

	public boolean isRelevant(IFile resource) {
		return resource != null && resource.getName().equals(METADATA_FILE_NAME);
	}

	public boolean isLikelyComponentSource(IFile f) {
		return f != null && f.getName().equals(METADATA_FILE_NAME);
	}

	public LoadedDeclarations parse(IFile f, IKbProject sp)
			throws ScannerException {
		XModel model = InnerModelHelper.createXModel(f.getProject());
		if(model == null) return null;
		XModelObject o = EclipseResourceUtil.getObjectByResource(model, f);
		return parse(o, f.getFullPath(), sp);
	}

	public LoadedDeclarations parse(XModelObject o, IPath source, IKbProject sp) {
		if(o == null) return null;
		
		String text = ((FileAnyImpl)o).getAsText();
		
		Element model = XMLUtilities.getElement(new ByteArrayInputStream(text.getBytes()), XMLEntityResolver.getInstance());
		if(model == null) return null;
		
		Map<String, MyFacesTagLibrary> libraries = new HashMap<String, MyFacesTagLibrary>();
		for (String p: prefixToURI.keySet()) {
			String uri = prefixToURI.get(p);
			MyFacesTagLibrary library = new MyFacesTagLibrary();
			library.setId(o.getPath() + "/" + p); //$NON-NLS-1$
			library.setURI(createValueInfo(uri));
			libraries.put(p, library);
		}

		String[] tagnames = {"component", "tag", "faceletTag"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		Map<String, MyFacesComponent> componentsByClass = new HashMap<String, MyFacesComponent>();
		for (String tag: tagnames) {
			Element[] cs = XMLUtilities.getChildren(model, tag);
			for (Element c : cs) {
				processComponent(c, libraries, componentsByClass);
			}
		}
	
		Set<MyFacesComponent> processed = new HashSet<MyFacesComponent>();
		for (String cls: componentsByClass.keySet()) {
			MyFacesComponent component = componentsByClass.get(cls);
			loadParents(component, componentsByClass, processed);
		}

		LoadedDeclarations ds = new LoadedDeclarations();
	
		for (MyFacesTagLibrary library: libraries.values()) {
			if(library.getComponents().length > 0) {
				ds.getLibraries().add(library);
			}
		}


		return ds;
	}

	void processComponent(Element c, Map<String, MyFacesTagLibrary> libraries, Map<String, MyFacesComponent> componentsByClass) {
		String name = util.getAttribute(c, ATTR_NAME);
		String className = util.getAttribute(c, "className.#text"); //$NON-NLS-1$
		MyFacesComponent component = null;
		MyFacesTagLibrary library = null;
		boolean isNew = false;

		if(!isEmpty(name)) {
			String componentName = name.trim();
			int pi = componentName.indexOf(":"); //$NON-NLS-1$
			if(pi < 0) return;
			String prefix = componentName.substring(0, pi);
			library = libraries.get(prefix);
			if(library == null) {
//				System.out.println("Cannot find library for " + prefix);
				return;
			}
			componentName = componentName.substring(pi + 1);
			component = (MyFacesComponent)library.getComponent(componentName);
			if(component == null) {
				component = new MyFacesComponent();
				component.setId(componentName);
				component.setName(createValueInfo(componentName));
				isNew = true;
			}
		} else if(!isEmpty(className)) {
			component = componentsByClass.get(className);
			if(component == null) {
				component = new MyFacesComponent();
			}
		}

		if(!isEmpty(className)) {
			component.setComponentClass(createValueInfo(className.trim()));
			componentsByClass.put(className, component);
		}
		String type = util.getAttribute(c, "type.#text"); //$NON-NLS-1$
		if(!isEmpty(type)) {
			component.setComponentType(createValueInfo(type.trim()));
		}
		String bodyContent = util.getAttribute(c, "bodyContent.#text"); //$NON-NLS-1$
		if(!isEmpty(bodyContent)) {
			component.setCanHaveBody(createValueInfo(bodyContent.trim()));
		}
		String description = util.getAttribute(c, ATTR_DESCRIPTION);
		if(!isEmpty(description) && !description.trim().equals("no description")) { //$NON-NLS-1$
			component.setDescription(createValueInfo(description.trim()));
		}
		String parentClass = util.getAttribute(c, ATTR_PARENT_CLASS);
		if(!isEmpty(parentClass)) {
			component.setParentClass(createValueInfo(parentClass.trim()));
		}
		
		Element[] attributes = XMLUtilities.getChildren(c, TAG_PROPERTY);
		if(attributes == null || attributes.length == 0) {
			attributes = XMLUtilities.getChildren(c, TAG_ATTRIBUTE);
		}
		if(attributes != null) for (Element a: attributes) {
			processAttribute(a, component);
		}

		if(isNew && library != null) {
			library.addComponent(component);
		}
	}

	void processAttribute(Element a, MyFacesComponent component) {
		String name = util.getAttribute(a, "jspName.#text"); //$NON-NLS-1$
		if(isEmpty(name)) name = util.getAttribute(a, ATTR_NAME);
		if(isEmpty(name)) return;
		name = name.trim();
		MyFacesAttribute attribute = (MyFacesAttribute)component.getAttribute(name);
		boolean isNew = false;
		if(attribute == null) {
			attribute = new MyFacesAttribute();
			attribute.setId(name);
			attribute.setName(createValueInfo(name));
			isNew = true;
		}
		String description = util.getAttribute(a, ATTR_DESCRIPTION);
		if(!isEmpty(description) && !description.trim().equals("no description")) { //$NON-NLS-1$
			attribute.setDescription(createValueInfo(description.trim()));
		}
		String required = util.getAttribute(a, ATTR_REQUIRED);
		if(!isEmpty(required)) {
			attribute.setRequired(createValueInfo(required.trim()));
		}
		if(isNew) {
			component.addAttribute(attribute);
		}
	}

	void loadParents(MyFacesComponent current, Map<String, MyFacesComponent> componentsByClass, Set<MyFacesComponent> processed) {
		if(processed.contains(current)) return;
		processed.add(current);
		String parentClass = current.getParentClass();
		MyFacesComponent parent = componentsByClass.get(parentClass);
		if(parent == null) return;
		loadParents(parent, componentsByClass, processed);
		IAttribute[] as = parent.getAttributes();
		for (IAttribute a: as) {
			if(current.getAttribute(a.getName()) == null) {
				current.addAttribute(a);
			}
		}
	}

	private boolean isEmpty(String s) {
		return s == null || s.trim().length() == 0;
	}
	private IValueInfo createValueInfo(String value) {
		ValueInfo v = new ValueInfo();
		v.setValue(value);
		return v;
	}

	static XModelObjectLoaderUtil util = new XModelObjectLoaderUtil();

}

/******************************************************************************* 
 * Copyright (c) 2009 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.internal.taglib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.project.ext.IValueInfo;
import org.jboss.tools.common.model.project.ext.store.XMLStoreConstants;
import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.common.xml.XMLUtilities;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.internal.KbXMLStoreConstants;
import org.jboss.tools.jst.web.kb.taglib.IAttribute;
import org.jboss.tools.jst.web.kb.taglib.IComponent;
import org.jboss.tools.jst.web.kb.taglib.INameSpace;
import org.jboss.tools.jst.web.kb.taglib.ITagLibrary;
import org.jboss.tools.jst.web.model.project.ext.store.XMLStoreHelper;
import org.w3c.dom.Element;

/**
 * Abstract implementation of ITagLibrary
 * @author Alexey Kazakov
 */
public abstract class AbstractTagLib implements ITagLibrary {

	Object id;

	protected INameSpace nameSpace;
	protected String uri;
	protected IPath source;
	protected IFile resource;
	protected Map<String, IComponent> components = new HashMap<String, IComponent>();

	//locations of xml attributes
	protected Map<String,IValueInfo> attributes = new HashMap<String, IValueInfo>();

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.TagLibrary#getAllComponents()
	 */
	public IComponent[] getComponents() {
		synchronized (components) {
			return components.values().toArray(new IComponent[components.size()]);
		}
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.TagLibrary#getComponent(java.lang.String)
	 */
	public IComponent getComponent(String name) {
		return components.get(name);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.TagLibrary#getComponents(java.lang.String)
	 */
	public IComponent[] getComponents(String nameTemplate) {
		List<IComponent> list = new ArrayList<IComponent>();
		IComponent[] comps = getComponents();
		for (int i = 0; i < comps.length; i++) {
			if(comps[i].getName().startsWith(nameTemplate)) {
				list.add(comps[i]);
			}
		}
		return list.toArray(new IComponent[list.size()]);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.TagLibrary#getComponents(org.jboss.tools.jst.web.kb.KbQuery, org.jboss.tools.jst.web.kb.PageContext)
	 */
	public IComponent[] getComponents(KbQuery query, IPageContext context) {
		String tagName = null;
		boolean mask = false;
		if(query.getType()==KbQuery.Type.TAG_NAME) {
			tagName = query.getValue();
			mask = query.isMask();
		} else {
			tagName = query.getLastParentTag();
		}
		if(tagName == null) {
			return null;
		}
		if(mask) {
			return getComponents(tagName);
		}
		return new IComponent[]{getComponent(tagName)};
	}

	/**
	 * Adds component to tag lib.
	 * @param component
	 */
	public void addComponent(IComponent component) {
		components.put(component.getName(), component);
	}

	/**
	 * @param components the components to set
	 */
	protected void setComponents(Map<String, IComponent> components) {
		this.components = components;
	}

	public IPath getSourcePath() {
		return source;
	}

	public void setSourcePath(IPath source) {
		this.source = source;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.TagLibrary#getResource()
	 */
	public IResource getResource() {
		if(resource != null) return resource;
		if(source != null) {
			resource = ResourcesPlugin.getWorkspace().getRoot().getFile(source);
		}
		return resource;
	}

	/**
	 * @param resource the resource to set
	 */
	public void setResource(IFile resource) {
		this.resource = resource;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IComponent#getNameSpace()
	 */
	public INameSpace getDefaultNameSpace() {
		return nameSpace;
	}

	/**
	 * @param nameSpace the name space to set
	 */
	public void setDefaultNameSpace(INameSpace nameSpace) {
		this.nameSpace = nameSpace;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.TagLibrary#getURI()
	 */
	public String getURI() {
		return uri;
	}

	/**
	 * @param uri the URI to set
	 */
	public void setURI(String uri) {
		this.uri = uri;
	}

	public void setURI(IValueInfo s) {
		uri = s == null ? null : s.getValue();
		attributes.put("uri", s);
	}


	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.ProposalProcessor#getProposals(org.jboss.tools.jst.web.kb.KbQuery, org.jboss.tools.jst.web.kb.PageContext)
	 */
	public TextProposal[] getProposals(KbQuery query, IPageContext context) {
		List<TextProposal> proposals = new ArrayList<TextProposal>();
		IComponent[] components = getComponents(query, context);
		if(query.getType() == KbQuery.Type.TAG_NAME) {
			for (int i = 0; i < components.length; i++) {
				TextProposal proposal = new TextProposal();
				proposal.setContextInfo(components[i].getDescription());
				StringBuffer label = new StringBuffer();
				if(query.getPrefix()!=null) {
					label.append(query.getPrefix() + KbQuery.PREFIX_SEPARATOR);
				}
				label.append(components[i].getName());
				proposal.setLabel(label.toString());

				IAttribute[] attributes = components[i].getPreferableAttributes();
				StringBuffer attributeSB = new StringBuffer();
				for (int j = 0; j < attributes.length; j++) {
					attributeSB.append(" ").append(attributes[j].getName()).append("=\"\"");
				}
				label.append(attributeSB);
				if(!components[i].canHaveBody()) {
					label.append(" /");
				}

				proposal.setReplacementString(label.toString());

				int position = proposal.getReplacementString().indexOf('"');
				if(position!=-1) {
					position ++;
				} else {
					position = proposal.getReplacementString().length();
				}
				proposal.setPosition(position);
				proposals.add(proposal);
			}
		} else {
			for (int i = 0; i < components.length; i++) {
				TextProposal[] componentProposals  = components[i].getProposals(query, context);
				for (int j = 0; j < componentProposals.length; j++) {
					proposals.add(componentProposals[j]);
				}
			}
		}
		return proposals.toArray(new TextProposal[proposals.size()]);
	}

	public void setID(Object id) {
		this.id = id;
	}

	public Object getID() {
		return id;
	}

	public String getXMLClass() {
		return null;
	}
	
	public AbstractTagLib clone() throws CloneNotSupportedException {
		AbstractTagLib t = (AbstractTagLib)super.clone();
		t.components = new HashMap<String, IComponent>();
		t.components.putAll(components);
		return t;
	}

	public Element toXML(Element parent, Properties context) {
		Element element = XMLUtilities.createElement(parent, KbXMLStoreConstants.TAG_LIBRARY);
		if(getXMLClass() != null) {
			element.setAttribute(XMLStoreConstants.ATTR_CLASS, getXMLClass());
		}
		IPath source = getSourcePath();
		if(source != null && !source.equals(context.get(XMLStoreConstants.ATTR_PATH))) {
			element.setAttribute(XMLStoreConstants.ATTR_PATH, source.toString());
		}
		if(id != null) {
			if(id instanceof XModelObject) {
				XModelObject o = (XModelObject)id;
				XMLStoreHelper.saveModelObject(element, o, XMLStoreConstants.TAG_ID, context);
			} else {
				//TODO consider other kinds of id
			}
		}

		saveAttributeValues(element);

		for (IComponent c: components.values()) {
			//TODO save component
		}

		return element;
	}

	protected void saveAttributeValues(Element element) {
		if(uri != null) element.setAttribute(KbXMLStoreConstants.ATTR_URI, uri);
	}

	public void loadXML(Element element, Properties context) {
		String s = element.getAttribute(XMLStoreConstants.ATTR_PATH);
		if(s != null && s.length() > 0) {
			source = new Path(s);
		} else {
			source = (IPath)context.get(XMLStoreConstants.ATTR_PATH);
		}
		Element e_id = XMLUtilities.getUniqueChild(element, XMLStoreConstants.TAG_ID);
		if(e_id != null) {
			String cls = e_id.getAttribute(XMLStoreConstants.ATTR_CLASS);
			if(XMLStoreConstants.CLS_MODEL_OBJECT.equals(cls)) {
				id = XMLStoreHelper.loadModelObject(e_id, context);
			} else {
				//TODO consider other kinds of id
			}
		}

		loadAttributeValues(element);

		//TODO load components

	}

	protected void loadAttributeValues(Element element) {
		if(element.hasAttribute(KbXMLStoreConstants.ATTR_URI)) {
			uri = element.getAttribute(KbXMLStoreConstants.ATTR_URI);
		}
	}

}
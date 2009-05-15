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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.taglib.IAttribute;
import org.jboss.tools.jst.web.kb.taglib.IComponent;
import org.jboss.tools.jst.web.kb.taglib.ITagLibrary;

/**
 * Abstract implementation of ITagLibrary
 * @author Alexey Kazakov
 */
public abstract class AbstractTagLib implements ITagLibrary {

	protected String uri;
	protected IFile resource;
	protected Map<String, IComponent> components = new HashMap<String, IComponent>();

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
		//TODO
		if(resource != null) {
			return resource.getFullPath();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.TagLibrary#getResource()
	 */
	public IResource getResource() {
		return resource;
	}

	/**
	 * @param resource the resource to set
	 */
	public void setResource(IFile resource) {
		this.resource = resource;
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

	public AbstractTagLib clone() throws CloneNotSupportedException {
		AbstractTagLib t = (AbstractTagLib)super.clone();
		t.components = new HashMap<String, IComponent>();
		t.components.putAll(components);
		return t;
	}
}
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

import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.taglib.IAttribute;
import org.jboss.tools.jst.web.kb.taglib.IComponent;
import org.jboss.tools.jst.web.kb.taglib.INameSpace;

/**
 * Abstract implementation of IComponent
 * @author Alexey Kazakov
 */
public abstract class AbstractComponent implements IComponent {

	protected boolean canHaveBody;
	protected String componentClass;
	protected String componentType;
	protected String description;
	protected String name;
	protected INameSpace nameSpace;
	protected Map<String, IAttribute> attributes = new HashMap<String, IAttribute>();
	protected Map<String, IAttribute> preferableAttributes = new HashMap<String, IAttribute>();
	protected Map<String, IAttribute> requiredAttributes = new HashMap<String, IAttribute>();

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IComponent#canHaveBody()
	 */
	public boolean canHaveBody() {
		return canHaveBody;
	}

	/**
	 * @param canHaveBody
	 */
	public void setCanHaveBody(boolean canHaveBody) {
		this.canHaveBody = canHaveBody;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IComponent#getAttribute(java.lang.String)
	 */
	public IAttribute getAttribute(String name) {
		return attributes.get(name);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IComponent#getAttributes()
	 */
	public IAttribute[] getAttributes() {
		synchronized (attributes) {
			return attributes.values().toArray(new IAttribute[attributes.size()]);
		}
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IComponent#getAttributes(java.lang.String)
	 */
	public IAttribute[] getAttributes(String nameTemplate) {
		List<IAttribute> list = new ArrayList<IAttribute>();
		IAttribute[] atts = getAttributes();
		for (int i = 0; i < atts.length; i++) {
			if(atts[i].getName().startsWith(nameTemplate)) {
				list.add(atts[i]);
			}
		}
		return list.toArray(new IAttribute[list.size()]);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IComponent#getAttributes(org.jboss.tools.jst.web.kb.KbQuery, org.jboss.tools.jst.web.kb.IPageContext)
	 */
	public IAttribute[] getAttributes(KbQuery query, IPageContext context) {
		String attrName = null;
		boolean mask = false;
		if(query.getType()==KbQuery.Type.ATTRIBUTE_NAME) {
			attrName = query.getValue();
			mask = query.isMask();
		} else if(query.getType()==KbQuery.Type.ATTRIBUTE_VALUE) {
			attrName = query.getParent();
		}
		if(attrName == null) {
			return null;
		}
		if(mask) {
			return getAttributes(attrName);
		}
		return new IAttribute[]{getAttribute(attrName)};
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IComponent#getComponentClass()
	 */
	public String getComponentClass() {
		return componentClass;
	}

	/**
	 * @param componentClass the component class name to set
	 */
	public void setComponentClass(String componentClass) {
		this.componentClass = componentClass;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IComponent#getComponentType()
	 */
	public String getComponentType() {
		return componentType;
	}

	/**
	 * @param componentType the component type to set
	 */
	public void setComponentType(String componentType) {
		this.componentType = componentType;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IComponent#getDescription()
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IComponent#getName()
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IComponent#getNameSpace()
	 */
	public INameSpace getNameSpace() {
		return nameSpace;
	}

	/**
	 * @param nameSpace the name space to set
	 */
	public void setNameSpace(INameSpace nameSpace) {
		this.nameSpace = nameSpace;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IComponent#getPreferableAttributes()
	 */
	public IAttribute[] getPreferableAttributes() {
		synchronized (preferableAttributes) {
			return preferableAttributes.values().toArray(new IAttribute[preferableAttributes.size()]);
		}
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IComponent#getRequiredAttributes()
	 */
	public IAttribute[] getRequiredAttributes() {
		synchronized (requiredAttributes) {
			return requiredAttributes.values().toArray(new IAttribute[requiredAttributes.size()]);
		}
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.IProposalProcessor#getProposals(org.jboss.tools.jst.web.kb.KbQuery, org.jboss.tools.jst.web.kb.IPageContext)
	 */
	public TextProposal[] getProposals(KbQuery query, IPageContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Adds the attribute to the component.
	 * @param attribute
	 */
	public void addAttribute(IAttribute attribute) {
		attributes.put(attribute.getName(), attribute);
		if(attribute.isPreferable()) {
			preferableAttributes.put(attribute.getName(), attribute);
		}
		if(attribute.isRequired()) {
			requiredAttributes.put(attribute.getName(), attribute);
		}
	}

	/**
	 * Removes the attribute from the component
	 * @param attribute
	 */
	public void removeAttribute(IAttribute attribute) {
		attributes.remove(attribute.getName());
		preferableAttributes.remove(attribute.getName());
		requiredAttributes.remove(attribute.getName());
	}
}
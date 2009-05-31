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
import java.util.List;
import java.util.Properties;

import org.jboss.tools.common.el.core.resolver.ELResolver;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.project.ext.IValueInfo;
import org.jboss.tools.common.model.project.ext.event.Change;
import org.jboss.tools.common.model.project.ext.store.XMLStoreConstants;
import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.internal.KbObject;
import org.jboss.tools.jst.web.kb.internal.KbXMLStoreConstants;
import org.jboss.tools.jst.web.kb.taglib.IAttribute;
import org.jboss.tools.jst.web.model.project.ext.store.XMLValueInfo;
import org.w3c.dom.Element;

/**
 * Abstract implementation of IAttribute
 * @author Alexey Kazakov
 */
public abstract class AbstractAttribute extends KbObject implements IAttribute {
	public static final String REQUIRED = "required";

	protected String description;
	protected String name;
	protected boolean required;

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IAttribute#getDescription()
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

	public void setDescription(IValueInfo s) {
		description = s == null ? null : s.getValue();
		attributesInfo.put(AbstractComponent.DESCRIPTION, s);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IAttribute#getName()
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

	public void setName(IValueInfo s) {
		name = s == null ? null : s.getValue();
		attributesInfo.put(XMLStoreConstants.ATTR_NAME, s);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IAttribute#isPreferable()
	 */
	public boolean isPreferable() {
		return isRequired();
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IAttribute#isRequired()
	 */
	public boolean isRequired() {
		return required;
	}

	/**
	 * @param required the required to set
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}

	public void setRequired(IValueInfo s) {
		required = s != null && "true".equals(s.getValue());
		attributesInfo.put(REQUIRED, s);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.IProposalProcessor#getProposals(org.jboss.tools.jst.web.kb.KbQuery, org.jboss.tools.jst.web.kb.IPageContext)
	 */
	public TextProposal[] getProposals(KbQuery query, IPageContext context) {
		List<TextProposal> proposals = null;
		ELResolver[] resolvers = context.getElResolvers();
		for (int i = 0; i < resolvers.length; i++) {
			List<TextProposal> list = resolvers[i].getCompletions(query.getValue(), false, query.getValue().length(), context);
			if(list == null || list.isEmpty()) continue;
			if(proposals == null) {
				proposals = new ArrayList<TextProposal>();
			}
			proposals.addAll(list);
		}
		if(proposals == null || proposals.isEmpty()) {
			return EMPTY_PROPOSAL_LIST;
		}
		return proposals.toArray(new TextProposal[proposals.size()]);
	}

	public AbstractAttribute clone() throws CloneNotSupportedException {
		return (AbstractAttribute)super.clone();
	}

	public List<Change> merge(KbObject s) {
		List<Change> changes = super.merge(s);
		
		AbstractAttribute a = (AbstractAttribute)s;
		if(!stringsEqual(name, a.name)) {
			changes = Change.addChange(changes, new Change(this, XMLStoreConstants.ATTR_NAME, name, a.name));
			name = a.name;
		}
		if(!stringsEqual(description, a.description)) {
			changes = Change.addChange(changes, new Change(this, AbstractComponent.DESCRIPTION, description, a.description));
			description = a.description;
		}
		if(required != a.required) {
			changes = Change.addChange(changes, new Change(this, REQUIRED, "" + required, "" + a.required));
			required = a.required;
		}
		
		return changes;
	}

	public String getXMLName() {
		return KbXMLStoreConstants.TAG_ATTRIBUTE;
	}
	
	public Element toXML(Element parent, Properties context) {
		Element element = super.toXML(parent, context);
		
		if(attributesInfo.get(XMLStoreConstants.ATTR_NAME) == null && name != null) {
			element.setAttribute(XMLStoreConstants.ATTR_NAME, name);
		}

		return element;
	}

	public void loadXML(Element element, Properties context) {
		super.loadXML(element, context);

		setName(attributesInfo.get(XMLStoreConstants.ATTR_NAME));
		setDescription(attributesInfo.get(AbstractComponent.DESCRIPTION));
		setRequired(attributesInfo.get(REQUIRED));

		if(name == null && element.hasAttribute(XMLStoreConstants.ATTR_NAME)) {
			name = element.getAttribute(XMLStoreConstants.ATTR_NAME);
		}

	}

	@Override
	protected void saveAttributesInfo(Element element, Properties context) {
		if(context.get(XMLStoreConstants.KEY_MODEL_OBJECT) == getId()) {
			
		} else {
			super.saveAttributesInfo(element, context);
		}
	}

	@Override
	protected void loadAttributesInfo(Element element, Properties context) {
		if(context.get(XMLStoreConstants.KEY_MODEL_OBJECT) == getId() && getId() != null) {
			XModelObject a = (XModelObject)getId();
			attributesInfo.put(XMLStoreConstants.ATTR_NAME, new XMLValueInfo(a, XMLStoreConstants.ATTR_NAME));
			attributesInfo.put(AbstractComponent.DESCRIPTION, new XMLValueInfo(a, AbstractComponent.DESCRIPTION));
			attributesInfo.put(REQUIRED, new XMLValueInfo(a, REQUIRED));
		} else {
			super.loadAttributesInfo(element, context);
		}
	}

}
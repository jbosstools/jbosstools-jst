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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.jboss.tools.common.model.project.ext.IValueInfo;
import org.jboss.tools.common.model.project.ext.event.Change;
import org.jboss.tools.common.model.project.ext.store.XMLStoreConstants;
import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.common.xml.XMLUtilities;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.internal.KbObject;
import org.jboss.tools.jst.web.kb.internal.KbXMLStoreConstants;
import org.jboss.tools.jst.web.kb.internal.taglib.composite.CompositeAttribute;
import org.jboss.tools.jst.web.kb.internal.taglib.myfaces.MyFacesAttribute;
import org.jboss.tools.jst.web.kb.taglib.Facet;
import org.jboss.tools.jst.web.kb.taglib.IAttribute;
import org.jboss.tools.jst.web.kb.taglib.IAttributeProvider;
import org.jboss.tools.jst.web.kb.taglib.IContextAttributeProvider;
import org.jboss.tools.jst.web.kb.taglib.IContextComponent;
import org.jboss.tools.jst.web.kb.taglib.ITagLibrary;
import org.w3c.dom.Element;

/**
 * Abstract implementation of IComponent
 * @author Alexey Kazakov
 */
public abstract class AbstractComponent extends KbObject implements IContextComponent {
	public static final String DESCRIPTION = "description"; //$NON-NLS-1$
	public static final String COMPONENT_CLASS = "component-class"; //$NON-NLS-1$
	public static final String COMPONENT_TYPE = "component-type"; //$NON-NLS-1$
	public static final String BODY_CONTENT = "bodycontent"; //$NON-NLS-1$

	protected boolean canHaveBody = true;
	protected String componentClass;
	protected String componentType;
	protected String description;
	protected String name;
	protected boolean hasExtendedAttributes = false;
	protected Map<String, IAttribute> attributes = new HashMap<String, IAttribute>();
	private IAttribute[] attributesArray;
	private Map<String, IAttribute> preferableAttributes = new HashMap<String, IAttribute>();
	private IAttribute[] preferableAttributesArray;
	private Map<String, IAttribute> requiredAttributes = new HashMap<String, IAttribute>();
	private IAttribute[] requiredAttributesArray;
	protected boolean ignoreCase;

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IComponent#canHaveBody()
	 */
	@Override
	public boolean canHaveBody() {
		return canHaveBody;
	}

	/**
	 * @param canHaveBody
	 */
	public void setCanHaveBody(boolean canHaveBody) {
		this.canHaveBody = canHaveBody;
	}

	/**
	 * @param s
	 */
	public void setCanHaveBody(IValueInfo s) {
		canHaveBody = s == null || !"empty".equals(s.getValue()); //$NON-NLS-1$
		attributesInfo.put(BODY_CONTENT, s);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IComponent#getAttribute(java.lang.String)
	 */
	@Override
	public IAttribute getAttribute(String name) {
		return attributes.get(name);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IComponent#getAttribute(KbQuery, java.lang.String)
	 */
	@Override
	public IAttribute[] getAttributes(IPageContext context, KbQuery query, String name) {
		IAttribute atr = attributes.get(name);
		if(atr!=null) {
			return new IAttribute[]{atr};
		}
		return new IAttribute[0];
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IComponent#getAttributes()
	 */
	@Override
	public IAttribute[] getAttributes() {
		if(attributesArray==null) {
			synchronized (attributes) {
				attributesArray = attributes.values().toArray(new IAttribute[attributes.size()]);
			}
		}
		return attributesArray;
	}

	protected IAttributeProvider[] getProviders() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IComponent#getAttributes(java.lang.String)
	 */
	@Override
	public IAttribute[] getAttributes(String nameTemplate) {
		return getAttributes(nameTemplate, null, null);
	}

	public IAttribute[] getAttributes(String nameTemplate, IPageContext context, KbQuery query) {
		List<IAttribute> list = new ArrayList<IAttribute>();
		IAttributeProvider[] providers = getProviders();

		List<IAttribute> atts = new ArrayList<IAttribute>();
		if(providers==null) {
			IAttribute[] atrs = getAttributes();
			for (IAttribute a : atrs) {
				atts.add(a);
			}
		} else {
			for (IAttributeProvider provider : providers) {
				IAttribute[] atrs;
				if (provider instanceof IContextAttributeProvider) {
					atrs = ((IContextAttributeProvider)provider).getAttributes(context, query);
				} else {
					atrs = provider.getAttributes(query);
				}
				for (IAttribute a : atrs) {
					atts.add(a);
				}
			}
		}
		for (IAttribute attribute : atts) {
			if(ignoreCase) {
				if(attribute.getName().toLowerCase().startsWith(nameTemplate.toLowerCase()) && (context==null || checkExtended(attribute, context, query))) {
					list.add(attribute);
				}
			} else if(attribute.getName().startsWith(nameTemplate) && (context==null || checkExtended(attribute, context, query))) {
				list.add(attribute);
			}
		}

		return list.toArray(new IAttribute[list.size()]);
	}

	@Override
	public IAttribute[] getAttributes(KbQuery query, String name) {
		return getAttributes(null, query, name);
	}

	@Override
	public IAttribute[] getAttributes(KbQuery query) {
		return getAttributes(null, query);
	}

	@Override
	public IAttribute[] getAttributes(KbQuery query, IPageContext context) {
		return getAttributes(context, query, false);
	}

	protected IAttribute[] getExtendedAttributes(IPageContext context, KbQuery query) {
		if(hasExtendedAttributes) {
			Set<IAttribute> attrs = new HashSet<IAttribute>();
			synchronized(attributes) {
				for (IAttribute attribute : attributes.values()) {
					if(checkExtended(attribute, context, query)) {
						attrs.add(attribute);
					}
				}
			}
			return attrs.toArray(new IAttribute[0]);
		}
		return getAttributes();
	}

	protected boolean checkExtended(IAttribute attribute, IPageContext context, KbQuery query) {
		return true;
	}

	private static final IAttribute[] EMPTY_ARRAY = new IAttribute[0];

	@Override
	public IAttribute[] getAttributes(IPageContext context, KbQuery query) {
		return getAttributes(context, query, false);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IComponent#getAttribute(KbQuery)
	 */
	@Override
	public IAttribute[] getAttributes(IPageContext context, KbQuery query, boolean includeExtensions) {
		if(!includeExtensions) {
			return getAttributes();
		}
		String attrName = null;
		boolean mask = false;
		if(query.getType()==KbQuery.Type.ATTRIBUTE_NAME) {
			attrName = query.getValue();
			mask = query.isMask();
		} else if(query.getType()==KbQuery.Type.ATTRIBUTE_VALUE) {
			attrName = query.getParent();
		}
		if(attrName == null) {
			return EMPTY_ARRAY;
		}
		if(mask) {
			return getAttributes(attrName, context, query);
		}
		IAttribute[] attrs = getAttributes(context, query, attrName);
		List<IAttribute> list = new ArrayList<IAttribute>();
		for (IAttribute attr : attrs) {
			if(checkExtended(attr, context, query)) {
				list.add(attr);
			}
		}
		return list.toArray(new IAttribute[list.size()]);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IComponent#getComponentClass()
	 */
	@Override
	public String getComponentClass() {
		return componentClass;
	}

	/**
	 * @param componentClass the component class name to set
	 */
	public void setComponentClass(String componentClass) {
		this.componentClass = componentClass;
	}

	public void setComponentClass(IValueInfo s) {
		componentClass = s == null ? null : s.getValue();
		attributesInfo.put(COMPONENT_CLASS, s);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IComponent#getComponentType()
	 */
	@Override
	public String getComponentType() {
		return componentType;
	}

	/**
	 * @param componentType the component type to set
	 */
	public void setComponentType(String componentType) {
		this.componentType = componentType;
	}

	/**
	 * 
	 * @param s
	 */
	public void setComponentType(IValueInfo s) {
		componentType = s == null ? null : s.getValue();
		attributesInfo.put(COMPONENT_TYPE, s);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IComponent#getDescription()
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param s
	 */
	public void setDescription(IValueInfo s) {
		description = s == null ? null : s.getValue();
		attributesInfo.put(DESCRIPTION, s);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IComponent#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param s
	 */
	public void setName(IValueInfo s) {
		name = s == null ? null : s.getValue();
		attributesInfo.put(XMLStoreConstants.ATTR_NAME, s);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IComponent#getPreferableAttributes()
	 */
	@Override
	public IAttribute[] getPreferableAttributes() {
		if(preferableAttributesArray==null) {
			synchronized (preferableAttributes) {
				preferableAttributesArray = preferableAttributes.values().toArray(new IAttribute[preferableAttributes.size()]);
			}
		}
		return preferableAttributesArray;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IComponent#getRequiredAttributes()
	 */
	@Override
	public IAttribute[] getRequiredAttributes() {
		if(requiredAttributesArray==null) {
			synchronized (requiredAttributes) {
				requiredAttributesArray = requiredAttributes.values().toArray(new IAttribute[requiredAttributes.size()]);
			}
		}
		return requiredAttributesArray;
	}

	/**
	 * Facets are a feature of JSF only, they are included into 
	 * the base interface and implementation
	 * for the sake of common approach.
	 */
	@Override
	public Facet getFacet(String name) {
		return null;
	}
	
	public static final Facet[] EMPTY_FACET_SET = new Facet[0];

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IComponent#getFacets()
	 */
	@Override
	public Facet[] getFacets() {
		return EMPTY_FACET_SET;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IComponent#getFacets(java.lang.String)
	 */
	@Override
	public Facet[] getFacets(String nameTemplate) {
		return EMPTY_FACET_SET;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.IProposalProcessor#getProposals(org.jboss.tools.jst.web.kb.KbQuery, org.jboss.tools.jst.web.kb.IPageContext)
	 */
	@Override
	public TextProposal[] getProposals(KbQuery query, IPageContext context) {
		IAttribute[] attributes = getAttributes(context, query, true);
		if(attributes == null || attributes.length == 0) {
			return EMPTY_PROPOSAL_LIST;
		}
		List<TextProposal> proposals = null;
		if(query.getType() == KbQuery.Type.ATTRIBUTE_NAME) {
			proposals = new ArrayList<TextProposal>();
			for (int i = 0; i < attributes.length; i++) {
				TextProposal proposal = new TextProposal();
				proposal.setSource(attributes[i]);
				proposal.setContextInfo(attributes[i].getDescription());
				proposal.setReplacementString(attributes[i].getName());
				proposal.setLabel(attributes[i].getName());
				proposals.add(proposal);
			}
		} else if(query.getType() == KbQuery.Type.ATTRIBUTE_VALUE) {
			for (int i = 0; i < attributes.length; i++) {
				TextProposal[] attributeProposals  = (attributes[i] == null ? null : attributes[i].getProposals(query, context));
				if(attributeProposals != null && attributeProposals.length > 0 && proposals == null) {
					proposals = new ArrayList<TextProposal>();
				}
				for (int j = 0; attributeProposals != null && j < attributeProposals.length; j++) {
					proposals.add(attributeProposals[j]);
				}
			}
		}
		if(proposals == null || proposals.isEmpty()) {
			return EMPTY_PROPOSAL_LIST;
		}
		return proposals.toArray(new TextProposal[proposals.size()]);
	}

	/**
	 * Adds the attribute to the component.
	 * @param attribute
	 */
	public void addAttribute(IAttribute attribute) {
		if(attribute instanceof KbObject) {
			adopt((KbObject)attribute);
		}
		attributes.put(attribute.getName(), attribute);
		if(attribute.isExtended()) {
			hasExtendedAttributes = true;
		}
		if(attribute.isPreferable()) {
			preferableAttributes.put(attribute.getName(), attribute);
		}
		if(attribute.isRequired()) {
			requiredAttributes.put(attribute.getName(), attribute);
		}
		clearAttributeArrays();
	}

	private void clearAttributeArrays() {
		attributesArray = null;
		preferableAttributesArray = null;
		requiredAttributesArray = null;
	}

	/**
	 * Removes the attribute from the component
	 * @param attribute
	 */
	public void removeAttribute(IAttribute attribute) {
		attributes.remove(attribute.getName());
		preferableAttributes.remove(attribute.getName());
		requiredAttributes.remove(attribute.getName());
		if(hasExtendedAttributes) {
			initExtendedAttributeFlag();
		}
		clearAttributeArrays();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IComponent#isExtended()
	 */
	public boolean isExtended() {
		// Return false by default
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IComponent#getTagLib()
	 */
	public ITagLibrary getTagLib() {
		return (ITagLibrary)parent;
	}

	/**
	 * @param ignoreCase the ignoreCase to set
	 */
	protected void setIgnoreCase(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.KbObject#clone()
	 */
	@Override
	public AbstractComponent clone() throws CloneNotSupportedException {
		AbstractComponent copy = (AbstractComponent)super.clone();
		copy.attributes = new HashMap<String, IAttribute>();
		copy.preferableAttributes = new HashMap<String, IAttribute>();
		copy.requiredAttributes = new HashMap<String, IAttribute>();
		IAttribute[] as = getAttributes();
		for (IAttribute a: as) {
			if(a instanceof AbstractAttribute) {
				copy.addAttribute(((AbstractAttribute)a).clone());
			}
			if(a.isExtended()) {
				copy.hasExtendedAttributes = true;
			}
		}
		return copy;
	}

	private void initExtendedAttributeFlag() {
		synchronized (attributes) {
			for (IAttribute a : attributes.values()) {
				if(a.isExtended()) {
					hasExtendedAttributes = true;
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.KbObject#merge(org.jboss.tools.jst.web.kb.internal.KbObject)
	 */
	@Override
	public List<Change> merge(KbObject s) {
		List<Change> changes = super.merge(s);
	
		AbstractComponent c = (AbstractComponent)s;
		if(!stringsEqual(name, c.name)) {
			changes = Change.addChange(changes, new Change(this, XMLStoreConstants.ATTR_NAME, name, c.name));
			name = c.name;
		}
		if(!stringsEqual(description, c.description)) {
			changes = Change.addChange(changes, new Change(this, DESCRIPTION, description, c.description));
			description = c.description;
		}
		if(!stringsEqual(componentClass, c.componentClass)) {
			changes = Change.addChange(changes, new Change(this, COMPONENT_CLASS, componentClass, c.componentClass));
			componentClass = c.componentClass;
		}
		if(!stringsEqual(componentType, c.componentType)) {
			changes = Change.addChange(changes, new Change(this, COMPONENT_CLASS, componentType, c.componentType));
			componentType = c.componentType;
		}
		if(canHaveBody != c.canHaveBody) {
			changes = Change.addChange(changes, new Change(this, BODY_CONTENT, "" + canHaveBody, "" + c.canHaveBody)); //$NON-NLS-1$ //$NON-NLS-2$
			canHaveBody = c.canHaveBody;
		}

		Change children = new Change(this, null, null, null);
		mergeAttributes(c, children);
		changes = Change.addChange(changes, children);

		return changes;
	}

	/**
	 * @param c
	 * @param children
	 */
	public void mergeAttributes(AbstractComponent c, Change children) {
		Map<Object,AbstractAttribute> attributeMap = new HashMap<Object, AbstractAttribute>();
		for (IAttribute a: getAttributes()) {
			if(a instanceof AbstractAttribute) {
				attributeMap.put(((KbObject)a).getId(), (AbstractAttribute)a);
			}
		}
		for (IAttribute a: c.getAttributes()) {
			if(a instanceof AbstractAttribute) {
				AbstractAttribute loaded = (AbstractAttribute)a;
				AbstractAttribute current = attributeMap.remove(loaded.getId());
				if(current == null) {
					addAttribute(loaded);
					Change change = new Change(this, null, null, loaded);
					children.addChildren(Change.addChange(null, change));
				} else {
					removeAttribute(current);
					List<Change> rc = current.merge(loaded);
					if(rc != null) children.addChildren(rc);
					addAttribute(current);
				}
			}
		}
		for (IAttribute a: attributeMap.values()) {
			if(a instanceof AbstractAttribute) {
				AbstractAttribute removed = (AbstractAttribute)a;
				if(attributes.get(removed.getName()) == removed) {
					attributes.remove(removed.getName());
					Change change = new Change(this, null, removed, null);
					children.addChildren(Change.addChange(null, change));
					clearAttributeArrays();
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.KbObject#getXMLName()
	 */
	@Override
	public String getXMLName() {
		return KbXMLStoreConstants.TAG_COMPONENT;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.KbObject#toXML(org.w3c.dom.Element, java.util.Properties)
	 */
	@Override
	public Element toXML(Element parent, Properties context) {
		Element element = super.toXML(parent, context);

		if(attributesInfo.get(XMLStoreConstants.ATTR_NAME) == null && name != null) {
			element.setAttribute(XMLStoreConstants.ATTR_NAME, name);
		}

		for (IAttribute c: getAttributes()) {
			if(c instanceof KbObject) {
				((KbObject)c).toXML(element, context);
			}
		}

		return element;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.KbObject#loadXML(org.w3c.dom.Element, java.util.Properties)
	 */
	@Override
	public void loadXML(Element element, Properties context) {
		super.loadXML(element, context);
	
		setName(attributesInfo.get(XMLStoreConstants.ATTR_NAME));
		if(name == null && element.hasAttribute(XMLStoreConstants.ATTR_NAME)) {
			name = element.getAttribute(XMLStoreConstants.ATTR_NAME);
		}
		setDescription(attributesInfo.get(DESCRIPTION));
		setComponentClass(attributesInfo.get(COMPONENT_CLASS));
		setCanHaveBody(attributesInfo.get(BODY_CONTENT));
		setComponentType(attributesInfo.get(COMPONENT_TYPE));

		Element[] cs = XMLUtilities.getChildren(element, KbXMLStoreConstants.TAG_ATTRIBUTE);
		for (Element e: cs) {
			String cls = e.getAttribute(XMLStoreConstants.ATTR_CLASS);
			AbstractAttribute c = null;
			if(KbXMLStoreConstants.CLS_TLD_LIBRARY.equals(cls)) {
				c = new TLDAttribute();
			} else if(KbXMLStoreConstants.CLS_FACESCONFIG_LIBRARY.equals(cls)) {
				c = new FacesConfigAttribute();
			} else if(KbXMLStoreConstants.CLS_COMPOSITE_LIBRARY.equals(cls)) {
				c = new CompositeAttribute();
			} else if(KbXMLStoreConstants.CLS_FACELET_LIBRARY.equals(cls)) {
				c = new FaceletAttribute();
			} else if(KbXMLStoreConstants.CLS_MYFACES_LIBRARY.equals(cls)) {
				c = new MyFacesAttribute();
			} else {
				//consider other cases;
			}
			if(c != null) {
				c.loadXML(e, context);
				addAttribute(c);
			}
		}
	}
}
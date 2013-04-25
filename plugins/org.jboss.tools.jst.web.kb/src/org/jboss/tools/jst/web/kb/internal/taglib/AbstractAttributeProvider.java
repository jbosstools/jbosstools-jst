/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc. 
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
import java.util.Map;

import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.taglib.IAttributeProvider;

/**
 * @author Alexey Kazakov
 */
public abstract class AbstractAttributeProvider implements IAttributeProvider {

	protected static final CustomTagLibAttribute[] EMPTY = new CustomTagLibAttribute[0];

	protected CustomTagLibComponent parentComponent;
	protected KbQuery query;

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IAttributeProvider#getAttributes(org.jboss.tools.jst.web.kb.KbQuery)
	 */
	@Override
	public CustomTagLibAttribute[] getAttributes(KbQuery query) {
		this.query = query;
		List<CustomTagLibAttribute> attributes = new ArrayList<CustomTagLibAttribute>();
		CustomTagLibAttribute[] attrs = getRequiredAttributes();
		for (CustomTagLibAttribute attr : attrs) {
			attributes.add(attr);
		}
		if(checkComponent()) {
			attrs = getConditionalAttributes();
			for (CustomTagLibAttribute attr : attrs) {
				attributes.add(attr);
			}
		}
		if(!attributes.isEmpty()) {
			if(attributes.get(0).getComponent()==null) {
				for (CustomTagLibAttribute attribute : attributes) {
					attribute.setParentComponent(getComponent());
				}
			}
		}
		return attributes.toArray(new CustomTagLibAttribute[attributes.size()]);
	}

	abstract protected boolean checkComponent();

	abstract protected CustomTagLibAttribute[] getConditionalAttributes();

	protected CustomTagLibAttribute[] getRequiredAttributes() {
		return EMPTY;
	}

	private List<CustomTagLibAttribute> getAllAttributes() {
		List<CustomTagLibAttribute> attributes = new ArrayList<CustomTagLibAttribute>();
		CustomTagLibAttribute[] attrs = getRequiredAttributes();
		for (CustomTagLibAttribute attr : attrs) {
			attributes.add(attr);
		}
		attrs = getConditionalAttributes();
		for (CustomTagLibAttribute attr : attrs) {
			attributes.add(attr);
		}
		return attributes;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IAttributeProvider#getAttribute(java.lang.String)
	 */
	@Override
	public CustomTagLibAttribute getAttribute(KbQuery query, String name) {
		this.query = query;
		CustomTagLibAttribute[] attrs = getAttributes(query);
		for (CustomTagLibAttribute attr : attrs) {
			if(attr.getName().equalsIgnoreCase(name)) {
				if(attr.getComponent()==null) {
					attr.setParentComponent(getComponent());
				}
				return attr;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IAttributeProvider#setComponent(org.jboss.tools.jst.web.kb.taglib.IComponent)
	 */
	@Override
	public void setComponent(CustomTagLibComponent parentComponent) {
		this.parentComponent = parentComponent;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IAttributeProvider#getComponent()
	 */
	@Override
	public CustomTagLibComponent getComponent() {
		return parentComponent;
	}

	protected boolean checkAttribute(AttributeData attribute) {
		Map<String, String> attributes = query.getAttributes();
		if(attributes==null) {
			return false;
		}
		String attr = attributes.get(attribute.name);
		return attribute.value.equalsIgnoreCase(attr);
	}

	protected boolean checkAttributeForTag(AttributeData attribute, String tagName) {
		return checkAttributeForTag(attribute, tagName, false);
	}

	/**
	 * Returns true if the tag or its parent tag (with the name tagName) has the attribute
	 * @param query
	 * @param attribute
	 * @param tagName
	 * @param lookInParentTags
	 * @return
	 */
	protected boolean checkAttributeForTag(AttributeData attribute, String tagName, boolean lookInParentTags) {
		if(!lookInParentTags) {
			return tagName==null?checkAttribute(attribute):checkCurrentTag(tagName) && checkAttribute(attribute);
		}
		KbQuery.Tag[] parents = query.getParentTagsWithAttributes();
		if(parents!=null) {
			for (int i = parents.length-1; i > -1; i--) {
				if(tagName==null || tagName.equalsIgnoreCase(parents[i].getName())) {
					String value = parents[i].getAttributes().get(attribute.name);
					if(attribute.value.equalsIgnoreCase(value)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	protected boolean checkCurrentTag(String tagName) {
		return tagName.equalsIgnoreCase(query.getLastParentTag());
	}

	protected static class AttributeData {
		String name;
		String value;

		public AttributeData(String name, String value) {
			this.name = name;
			this.value = value;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof AttributeData) {
				AttributeData other = (AttributeData)obj;
				return name.equalsIgnoreCase(other.name) && value.equalsIgnoreCase(other.value);
			}
			return false;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return name.hashCode() + value.hashCode();
		}
	}
}
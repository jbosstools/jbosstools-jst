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
package org.jboss.tools.jst.web.kb;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Query object is used to get info from Page Processors.
 * @author Alexey Kazakov
 */
public class KbQuery {

	public static final KbQuery EMPTY = new KbQuery("", new String[0], new Tag[0], new HashMap<String, String>(), "", Type.TAG_NAME, "");

	public static final String PREFIX_SEPARATOR = ":"; //$NON-NLS-1$

	private int offset;
	private String uri;
	private String[] parentTags;
	private Tag[] parentTagsWithAttributes;
	private Map<String, String> attributes;
	private String value;
	private String stringQuery;
	private boolean useAsMask;
	private String prefix;
	private Type type;
	private String parent;

	private Set<String> cachedAttributes = null;

	/**
	 * Type of object for which we want to get info
	 * @author Alexey Kazakov
	 */
	public static enum Type {
		TEXT,
		TAG_NAME,
		ATTRIBUTE_NAME,
		ATTRIBUTE_VALUE,
		TAG_BODY
	}

	public static class Tag {
		private String name;
		private Map<String, String> attributes;

		public Tag(String name, Map<String, String> attributes) {
			this.setName(name);
			this.setAttributes(attributes);
		}

		/**
		 * @return the name
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

		/**
		 * @return the attributes
		 */
		public Map<String, String> getAttributes() {
			return attributes;
		}

		/**
		 * @param attributes the attributes to set
		 */
		public void setAttributes(Map<String, String> attributes) {
			this.attributes = attributes;
		}
	}

	public KbQuery() {
	}

	public KbQuery(String uri, String[] parentTags, Tag[] parentTagsWithAttributes, Map<String, String> attributes, String value, Type type, String parent) {
		this.uri = uri;
		this.parentTags = parentTags;
		this.parentTagsWithAttributes = parentTagsWithAttributes;
		this.attributes = attributes;
		this.value = value;
		this.type = type;
		this.parent = parent;
	}

	/**
	 * Returns URI of tag library for the current tag (type==TAG_NAME or type==ATTRIBUTE_NAME or type==ATTRIBUTE_VALE) or parent tag (type==TEXT)
	 *
	 * @return
	 */
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * The stack of parent tags
	 * @return
	 */
	public String[] getParentTags() {
		return parentTags;
	}

	/**
	 * @deprecated Use setParentTagsWithAttributes() instead
	 * @param parentTags the stack of parent tags
	 */
	public void setParentTags(String[] parentTags) {
		this.parentTags = parentTags;
	}

	/**
	 * return the last parent tag
	 */
	public String getLastParentTag() {
		if(parentTags.length>0) {
			return parentTags[parentTags.length-1];
		}
		return null;
	}

	/**
	 * @deprecated Use getParentTagsWithAttributes() instead
	* @return the name of parent tag (type==TAG_NAME or type==ATTRIBUTE_NAME or type==TEXT) or attribute (type==ATTRIBUTE_VALE)
	*/
	public String getParent() {
		if(type == Type.TAG_NAME) {
			return getLastParentTag();
		}
		return parent;
	}

	/**
	 * @param name the name of parent tag or attribute to set
	 */
	public void setParent(String name) {
		this.parent = name;
	}

	/**
	 * Value of query. For example in case of ATTRIBUTE_NAME type it is an attribute name.
	 * @return
	 */
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * True if the value is a mask. For example we ask all tags which start with "<h:outputT" then the value "outputT" is a mask.
	 * @return
	 */
	public boolean isMask() {
		return useAsMask;
	}

	public void setMask(boolean useAsMask) {
		this.useAsMask = useAsMask;
	}

	/**
	 * Returns type of value
	 * @return
	 */
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * @return offset
	 */
	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * @return the string representation of this query.
	 * In case of tag name this method will return "<h:outputText"
	 * but getValue() will return "outputText".
	 */
	public String getStringQuery() {
		return stringQuery;
	}

	/**
	 * @param stringQuery the stringQuery to set
	 */
	public void setStringQuery(String stringQuery) {
		this.stringQuery = stringQuery;
	}

	/**
	 * @return the tag prefix.
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * @param prefix the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * The existing attributes of the tag.
	 * May be null.
	 * @return the attributes
	 */
	public Map<String, String> getAttributes() {
		return attributes;
	}

	/**
	 * @param attributes the attributes to set
	 */
	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	/**
	 * The stack of the parent tags with attributes.
	 * May return null.
	 * If null then use getParentTags() instead.
	 * @return
	 */
	public Tag[] getParentTagsWithAttributes() {
		return parentTagsWithAttributes;
	}

	public void setParentTagsWithAttributes(Tag[] tags) {
		this.parentTagsWithAttributes = tags;
		if(parentTags==null) {
			parentTags = new String[tags.length];
			for (int i = 0; i < tags.length; i++) {
				parentTags[i] = tags[i].getName();
			}
		}
	}

	/**
	 * Returns set of names of attributes declared in tag libraries,
	 * excluding extensions.
	 * 
	 * This method is used internally to optimize process of 
	 * excluding extensions from retrieved arrays of attributes.  
	 *  
	 * @return
	 */
	public Set<String> getCachedAttributes() {
		return cachedAttributes;
	}

	/**
	 * @see getCachedAttributes()
	 * @param s
	 */
	public void setCachedAttributes(Set<String> s) {
		cachedAttributes = s;
	}
}
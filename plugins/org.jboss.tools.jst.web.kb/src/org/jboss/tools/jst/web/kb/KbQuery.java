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

/**
 * Query object is used to get info from Page Processors.
 * @author Alexey Kazakov
 */
public class KbQuery {

	public static final String PREFIX_SEPARATOR = ":"; //$NON-NLS-1$

	private int offset;
	private String uri;
	private String[] parentTags;
	private String value;
	private String stringQuery;
	private boolean useAsMask;
	private String prefix;
	private Type type;
	private String parent;

	/**
	 * Type of object for which we want to get info
	 * @author Alexey Kazakov
	 */
	public static enum Type {
		TEXT,
		TAG_NAME,
		ATTRIBUTE_NAME,
		ATTRIBUTE_VALUE
	}

	public KbQuery() {
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
}
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

	private int offset;
	private String uri;
	private String[] parentTags;
	private String value;
	private boolean useAsMask;
	private Type type;

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
	 * URI of tag library
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

	public void setParentTags(String[] parentTags) {
		this.parentTags = parentTags;
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
	 * True if the value is a mask. For example we ask all tags which start with "<h:outputT" then the value "outputT" ia a mask.
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
}
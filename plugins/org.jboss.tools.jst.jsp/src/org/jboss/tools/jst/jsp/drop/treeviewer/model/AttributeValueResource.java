/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.jsp.drop.treeviewer.model;

import org.jboss.tools.common.kb.ParamList;
import org.jboss.tools.jst.jsp.outline.ValueHelper;

/**
 * @author Igels
 */
abstract public class AttributeValueResource extends ModelElement implements IAttributeValueContainer {

	public static AttributeValueResource[] EMPTY_LIST = new AttributeValueResource[0];

//	private List children = new ArrayList();
	private ParamList params = new ParamList();

	public AttributeValueResource(ModelElement parent) {
		super(parent);
	}

	public AttributeValueResource(String name, ModelElement parent) {
		super(name, parent);
	}

	public ParamList getParams() {
		return params;
	}

	public void setParams(ParamList params) {
		this.params = params;
	}

	/**
	 * @see ModelElement#compareValue(String)
	 */
	public int compareValue(String value) {
		return value.length();
	}
	
	protected ValueHelper valueHelper;
	protected String query;
	
	public void setQuery(String query, ValueHelper valueHelper) {
		this.query = query;
		this.valueHelper = valueHelper;
	}
	
	public String getQuery() {
		return query;
	}
	
	public ValueHelper getValueHelper() {
		return valueHelper;
	}
}
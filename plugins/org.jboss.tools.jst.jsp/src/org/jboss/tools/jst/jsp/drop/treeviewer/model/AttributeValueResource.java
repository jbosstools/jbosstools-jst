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

import java.util.ArrayList;

import org.jboss.tools.common.kb.ParamList;
import org.jboss.tools.jst.jsp.outline.ValueHelper;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.internal.taglib.CustomProposalType.Param;

/**
 * @author Igels
 */
abstract public class AttributeValueResource extends ModelElement implements IAttributeValueContainer {

	public static AttributeValueResource[] EMPTY_LIST = new AttributeValueResource[0];

//	private List children = new ArrayList();
	private Param[] params = new Param[0];

	public AttributeValueResource(ModelElement parent) {
		super(parent);
	}

	public AttributeValueResource(String name, ModelElement parent) {
		super(name, parent);
	}

	public Param[] getParams() {
		return params;
	}

	public String[] getParamsValues(String paramName) {
		ArrayList<String> result = new ArrayList<String>();
		for(Param param: params) {
			if(paramName.equals(param.getName())) {
				result.add(param.getValue());
			}
		}
		return result.toArray(new String[result.size()]);		
	}

	public void setParams(Param[] params) {
		this.params = params;
	}

	/**
	 * @see ModelElement#compareValue(String)
	 */
	public int compareValue(String value) {
		return value.length();
	}
	
	protected ValueHelper valueHelper;
	protected KbQuery query;
	
	public void setQuery(KbQuery query, ValueHelper valueHelper) {
		this.query = query;
		this.valueHelper = valueHelper;
	}
	
	public KbQuery getQuery() {
		return query;
	}
	
	public ValueHelper getValueHelper() {
		return valueHelper;
	}
}
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
package org.jboss.tools.jst.web.kb.internal.proposal;

import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.IProposalProcessor;
import org.jboss.tools.jst.web.kb.KbQuery;

/**
 * @author Alexey Kazakov
 */
public abstract class CustomProposalType implements IProposalProcessor {

	protected Param[] params;
	protected String type;
	protected IPageContext context;

	protected abstract void init(IPageContext context);

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the params
	 */
	public Param[] getParams() {
		if(params==null) {
			params = new Param[0];
		}
		return params;
	}

	/**
	 * @param params the params to set
	 */
	public void setParams(Param[] params) {
		this.params = params;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.IProposalProcessor#getProposals(org.jboss.tools.jst.web.kb.KbQuery, org.jboss.tools.jst.web.kb.IPageContext)
	 */
	public TextProposal[] getProposals(KbQuery query, IPageContext context) {
		this.context = context;
		init(context);
		return getProposals(query);
	}

	/**
	 * @param name
	 * @return
	 */
	public String getParamValue(String name) {
		if(params==null) {
			return null;
		}
		for (int i = 0; i < params.length; i++) {
			if(name.equals(params[i].getName())) {
				return params[i].getValue();
			}
		}
		return null;
	}

	abstract public TextProposal[] getProposals(KbQuery query);

	public static class Param {

		private String name;
		private String value;

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
		 * @return the value
		 */
		public String getValue() {
			return value;
		}

		/**
		 * @param value the value to set
		 */
		public void setValue(String value) {
			this.value = value;
		}
	}
}
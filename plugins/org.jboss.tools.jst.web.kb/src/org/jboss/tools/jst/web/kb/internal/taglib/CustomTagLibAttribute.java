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

/**
 * @author Alexey Kazakov
 */
public class CustomTagLibAttribute extends AbstractAttribute {

	protected boolean extended = true;
	protected String defaultValue;
	protected Proposal[] proposals;

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IAttribute#isExtended()
	 */
	@Override
	public boolean isExtended() {
		return extended;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IAttribute#setExtended(boolean)
	 */
	@Override
	public void setExtended(boolean extended) {
		this.extended = extended;
	}

	/**
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @param defaultValue the defaultValue to set
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * @return the proposals
	 */
	public Proposal[] getProposals() {
		if(proposals==null) {
			proposals = new Proposal[0];
		}
		return proposals;
	}

	/**
	 * @param proposals the proposals to set
	 */
	public void setProposals(Proposal[] proposals) {
		this.proposals = proposals;
	}

	/**
	 * @author Alexey Kazakov
	 */
	public static class Proposal {

		private Param[] params;
		private String type;

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
	}

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
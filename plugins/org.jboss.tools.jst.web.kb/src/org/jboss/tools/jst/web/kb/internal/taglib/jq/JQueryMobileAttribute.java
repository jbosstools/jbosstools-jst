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
package org.jboss.tools.jst.web.kb.internal.taglib.jq;

import org.jboss.tools.jst.web.kb.internal.proposal.CustomProposalType;
import org.jboss.tools.jst.web.kb.internal.proposal.CustomProposalTypeFactory;
import org.jboss.tools.jst.web.kb.internal.proposal.EnumerationProposalType;
import org.jboss.tools.jst.web.kb.internal.taglib.CustomTagLibAttribute;

/**
 * @author Alexey Kazakov
 */
public class JQueryMobileAttribute extends CustomTagLibAttribute {

	protected String[] values;

	public JQueryMobileAttribute(String name, String description) {
		this(name, description, new String[0]);
	}

	public JQueryMobileAttribute(String name, String description, String[] values) {
		this(name, description, values, null);
	}

	public JQueryMobileAttribute(String name, String description, String[] values, String[] valueDescriptions) {
		this.values = values;
		setDescription(description);
		setExtended(false);
		setIgnoreCase(true);
		setName(name);
		setRequired(false);

		CustomProposalType proposal = new EnumerationProposalType();
		proposal.setType(CustomProposalTypeFactory.ENUMERATION_TYPE);

		CustomProposalType[] proposals = new CustomProposalType[1];
		proposals[0] = proposal;
		CustomProposalType.Param[] params = new CustomProposalType.Param[values.length];
		for (int i = 0; i < values.length; i++) {
			CustomProposalType.Param param = new CustomProposalType.Param();
			param.setValue(values[i]);
			if(valueDescriptions!=null) {
				param.setDescription(valueDescriptions[i]);
			}
			params[i]= param;
		}
		proposal.setParams(params);
		setProposals(proposals);
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("{Name: ").append(name).append("; Values: ");
		for (int i = 0; i < values.length; i++) {
			if(i>0) {
				sb.append('|');
			}
			sb.append(values[i]);
		}
		return sb.append('}').toString();
	}
}
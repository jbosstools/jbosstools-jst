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

import org.jboss.tools.jst.web.project.list.IWebPromptingProvider;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class ValidatorIDProposalType extends ConverterIDProposalType {

	public ValidatorIDProposalType() {}

	@Override
	protected String getListID() {
		return IWebPromptingProvider.JSF_VALIDATOR_IDS;
	}
}
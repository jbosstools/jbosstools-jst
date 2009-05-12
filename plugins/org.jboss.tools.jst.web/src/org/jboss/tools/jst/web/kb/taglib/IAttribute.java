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
package org.jboss.tools.jst.web.kb.taglib;

import org.jboss.tools.jst.web.kb.IProposalProcessor;

/**
 * @author Alexey Kazakov
 */
public interface IAttribute extends IProposalProcessor {

	/**
	 * @return name of attribute
	 */
	String getName();

	/**
	 * @return description
	 */
	String getDescription();

	/**
	 * @return true if the attribute is required.
	 */
	boolean isRequired();

	/**
	 * @return true if the attribute is preferable. E.g. <h:outputText value=""/>
	 */
	boolean isPreferable();
}
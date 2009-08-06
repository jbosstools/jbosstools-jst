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

import org.jboss.tools.jst.web.kb.WebKbPlugin;

/**
 * @author Alexey Kazakov
 */
public class CustomProposalTypeFactory {

	private static final CustomProposalTypeFactory INSTANCE = new CustomProposalTypeFactory();

	public static final String RESOURCE_BUNDLE_NAME_TYPE = "bundleName"; //$NON-NLS-1$
	public static final String ACTION_TYPE = "viewActions"; //$NON-NLS-1$
	public static final String RESOURCE_PATH_TYPE = "file"; //$NON-NLS-1$
	public static final String ENUMERATION_TYPE = "enumeration"; //$NON-NLS-1$
	public static final String FACELETS_JSFC_TYPE = "faceletsJsfCTags"; //$NON-NLS-1$
	public static final String NAME_SPACE_TYPE = "taglib"; //$NON-NLS-1$
	public static final String ID_TYPE = "id"; //$NON-NLS-1$

	private CustomProposalTypeFactory() {
	}

	/**
	 * @return an instance of this factory. 
	 */
	public static CustomProposalTypeFactory getInstance() {
		return INSTANCE;
	}

	/**
	 * Creates Custom Proposal Type by name
	 * @param type
	 * @return
	 */
	public CustomProposalType createProposalType(String type) {
		if(RESOURCE_BUNDLE_NAME_TYPE.equals(type)) {
			return new ResourceBundleNameProposalType();
		}
		if(ACTION_TYPE.equals(type)) {
			return new ActionProposalType();
		}
		if(RESOURCE_PATH_TYPE.equals(type)) {
			return new ResourcePathProposalType();
		}
		if(ENUMERATION_TYPE.equals(type)) {
			return new EnumerationProposalType();
		}
		if(FACELETS_JSFC_TYPE.equals(type)) {
			return new FaceletsJsfCProposalType();
		}
		if(NAME_SPACE_TYPE.equals(type)) {
			return new NameSpaceProposalType();
		}
		if(ID_TYPE.equals(type)) {
			return new IDProposalType();
		}
		
		//WebKbPlugin.getDefault().logError("Unknown proposal type: " + type); //$NON-NLS-1$
		//ExtendedProposalType will report if necessary
	
		ExtendedProposalType proposalType = new ExtendedProposalType();

		return proposalType;
	}
}
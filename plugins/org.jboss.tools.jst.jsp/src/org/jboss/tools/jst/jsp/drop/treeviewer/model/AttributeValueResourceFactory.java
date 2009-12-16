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

import java.text.MessageFormat;
import org.eclipse.ui.IEditorInput;

import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.internal.proposal.CustomProposalType;
import org.jboss.tools.jst.web.kb.internal.proposal.CustomProposalTypeFactory;
import org.jboss.tools.jst.jsp.messages.JstUIMessages;

/**
 * @author Igels
 */
public class AttributeValueResourceFactory {

	static String BUNDLE_NAME_TYPE = CustomProposalTypeFactory.RESOURCE_BUNDLE_NAME_TYPE;
	static String VIEW_ACTIONS_TYPE = CustomProposalTypeFactory.ACTION_TYPE;
	static String IMAGE_FILE_TYPE = CustomProposalTypeFactory.RESOURCE_PATH_TYPE;
	static String ENUMERATION_TYPE = CustomProposalTypeFactory.ENUMERATION_TYPE;
	static String FACELETS_JSFC_TYPE = CustomProposalTypeFactory.FACELETS_JSFC_TYPE;
	static String TAGLIB_TYPE = CustomProposalTypeFactory.NAME_SPACE_TYPE;
	static String ID_TYPE = CustomProposalTypeFactory.ID_TYPE;
	public static String BUNDLE_PROPERTY_TYPE = "bundleProperty"; //$NON-NLS-1$
	public static String BEAN_PROPERTY_TYPE = "beanProperty"; //$NON-NLS-1$
	public static String BEAN_METHOD_BY_SYGNATURE_TYPE = "beanMethodBySignature"; //$NON-NLS-1$
	public static String JSP_PATH_TYPE = "jspPath"; //$NON-NLS-1$
	public static String JSF_VARIABLES_TYPE = "jsfVariables"; //$NON-NLS-1$
	public static String MANAGED_BEAN_NAME_TYPE = "managedBeanName"; //$NON-NLS-1$
	public static String JSF_ID = "jsfID"; //$NON-NLS-1$

	private static AttributeValueResourceFactory INSTANCE = new AttributeValueResourceFactory();

	private AttributeValueResourceFactory() {
	}

	public static AttributeValueResourceFactory getInstance() {
		return INSTANCE;
	}

	public AttributeValueResource createResource(IEditorInput editorInput, IPageContext pageContext, ModelElement root, CustomProposalType proposalType, String type, KbQuery kbQuery) {
		return createResource(editorInput, pageContext, null, root, proposalType, type, kbQuery);
	}

	public AttributeValueResource createResource(IEditorInput editorInput, IPageContext pageContext, String name, ModelElement root, CustomProposalType proposalType, String type, KbQuery kbQuery) {
		if(type == null && proposalType != null) {
			type = proposalType.getType();
		}
		if(BEAN_PROPERTY_TYPE.equals(type)) {
			return new ManagedBeansPropertiesResourceElement(editorInput, name, root);
		} else if(BEAN_METHOD_BY_SYGNATURE_TYPE.equals(type)) {
			return new ManagedBeanMethodResourceElement(editorInput, name, root);
		} else if(BUNDLE_NAME_TYPE.equals(type)) {
			return new BundlesNameResourceElement(editorInput, name, root);
		} else if(BUNDLE_PROPERTY_TYPE.equals(type)) {
			return new BundlesPropertiesResourceElement(editorInput, pageContext, name, root);
		} else if(VIEW_ACTIONS_TYPE.equals(type)) {
			return new ViewActionsResorceElement(editorInput, name, root);
		} else if(ENUMERATION_TYPE.equals(type)) {
			return new EnumerationResourceElement(name, root);
		} else if(JSF_VARIABLES_TYPE.equals(type)) {
			return new JsfVariablesResourceElement(name, root);
		} else if(IMAGE_FILE_TYPE.equals(type)) {
			return new ImageFileResourceElement(editorInput, root);
		} else if(ID_TYPE.equals(type)) {
			return new IDResourceElement("Component IDs", root, pageContext, proposalType, kbQuery); //$NON-NLS-1$
		} else if(CustomProposalTypeFactory.CONVERTER_ID_TYPE.equals(type)) {
			return new IDResourceElement("Converter IDs", root, pageContext, proposalType, kbQuery); //$NON-NLS-1$
		} else if(CustomProposalTypeFactory.VALIDATOR_ID_TYPE.equals(type)) {
			return new IDResourceElement("Validator IDs", root, pageContext, proposalType, kbQuery); //$NON-NLS-1$
		} else if("seamVariables".equals(type)) { //$NON-NLS-1$
			return new SeamVariablesResourceElement(editorInput, "Seam Variables", root); //$NON-NLS-1$
		}
		return new UnknownAttributeValueResource(MessageFormat.format(JstUIMessages.AttributeValueResourceFactory_UnknownResourceType, type), root);
//		throw new RuntimeException("Unknown resource type:" + type);
	}
}
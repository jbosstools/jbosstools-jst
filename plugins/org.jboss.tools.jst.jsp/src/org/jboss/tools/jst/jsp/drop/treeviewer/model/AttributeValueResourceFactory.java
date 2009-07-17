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

import org.jboss.tools.common.kb.KbDinamicResource;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.jsp.messages.JstUIMessages;

/**
 * @author Igels
 */
public class AttributeValueResourceFactory {

	private static AttributeValueResourceFactory INSTANCE = new AttributeValueResourceFactory();

	private AttributeValueResourceFactory() {
	}

	public static AttributeValueResourceFactory getInstance() {
		return INSTANCE;
	}

	public AttributeValueResource createResource(IEditorInput editorInput, IPageContext pageContext, ModelElement root, String type) {
		return createResource(editorInput, pageContext, null, root, type);
	}

	public AttributeValueResource createResource(IEditorInput editorInput, IPageContext pageContext, String name, ModelElement root, String type) {
		if(KbDinamicResource.BEAN_PROPERTY_TYPE.equals(type)) {
			return new ManagedBeansPropertiesResourceElement(editorInput, name, root);
		} else if(KbDinamicResource.BEAN_METHOD_BY_SYGNATURE_TYPE.equals(type)) {
			return new ManagedBeanMethodResourceElement(editorInput, name, root);
		} else if(KbDinamicResource.BUNDLE_NAME_TYPE.equals(type)) {
			return new BundlesNameResourceElement(editorInput, name, root);
		} else if(KbDinamicResource.BUNDLE_PROPERTY_TYPE.equals(type)) {
			return new BundlesPropertiesResourceElement(editorInput, pageContext, name, root);
		} else if(KbDinamicResource.VIEW_ACTIONS_TYPE.equals(type)) {
			return new ViewActionsResorceElement(editorInput, name, root);
		} else if(KbDinamicResource.ENUMERATION_TYPE.equals(type)) {
			return new EnumerationResourceElement(name, root);
		} else if(KbDinamicResource.JSF_VARIABLES_TYPE.equals(type)) {
			return new JsfVariablesResourceElement(name, root);
		} else if(KbDinamicResource.IMAGE_FILE_TYPE.equals(type)) {
			return new ImageFileResourceElement(editorInput, root);
		} else if("seamVariables".equals(type)) { //$NON-NLS-1$
			return new SeamVariablesResourceElement(editorInput, "Seam Variables", root); //$NON-NLS-1$
		}
		return new UnknownAttributeValueResource(MessageFormat.format(JstUIMessages.AttributeValueResourceFactory_UnknownResourceType, type), root);
//		throw new RuntimeException("Unknown resource type:" + type);
	}
}
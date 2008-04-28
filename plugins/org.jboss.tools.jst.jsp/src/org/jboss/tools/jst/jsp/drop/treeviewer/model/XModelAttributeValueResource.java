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

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;

import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.common.model.util.XModelObjectLoaderUtil;
import org.jboss.tools.common.model.ui.editor.IModelObjectEditorInput;
import org.jboss.tools.jst.web.project.list.WebPromptingProvider;

/**
 * @author Igels
 */
abstract public class XModelAttributeValueResource extends AttributeValueResource {

	protected IEditorInput editorInput;
	protected WebPromptingProvider provider;
	protected XModel xModel;
	protected XModelObject xModelObject;
	protected String path;

	public XModelAttributeValueResource(IEditorInput editorInput, ModelElement parent) {
		super(parent);
		this.editorInput = editorInput;
		init();
	}

	public XModelAttributeValueResource(IEditorInput editorInput, String name, ModelElement parent) {
		super(name, parent);
		this.editorInput = editorInput;
		init();
	}

	protected void init() {
		this.provider = WebPromptingProvider.getInstance();

		if(editorInput instanceof IModelObjectEditorInput) {
			xModelObject = ((IModelObjectEditorInput)editorInput).getXModelObject();
			xModel = xModelObject.getModel();
		} else if(editorInput instanceof IFileEditorInput) {
			IFile file = ((IFileEditorInput)editorInput).getFile();
			xModelObject = EclipseResourceUtil.getObjectByResource(file);
			if(xModelObject != null) {
				xModel = xModelObject.getModel();
			}
		}
		if(xModelObject != null) {
			path = XModelObjectLoaderUtil.getResourcePath(xModelObject);
		}
	}

	protected boolean isReadyToUse() {
		return (provider != null && xModel != null);
	}

	public WebPromptingProvider getProvider() {
		return provider;
	}

	public XModel getXModel() {
		return xModel;
	}
}
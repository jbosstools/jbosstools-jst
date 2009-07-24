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
package org.jboss.tools.jst.jsp.support.kb;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;

import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.common.model.ui.editor.IModelObjectEditorInput;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.web.project.list.WebPromptingProvider;

public abstract class WTPKbAbstractModelResource {
	protected IEditorInput fEditorInput;
	protected WebPromptingProvider fProvider;
	protected XModel fXModel;
	protected XModelObject fXModelObject;
	
	public WTPKbAbstractModelResource(IEditorInput fEditorInput) {
		this.fEditorInput = fEditorInput;
		fProvider = WebPromptingProvider.getInstance();
		try {
			if(fEditorInput instanceof IModelObjectEditorInput) {
				fXModelObject = ((IModelObjectEditorInput)fEditorInput).getXModelObject();
			} else if(fEditorInput instanceof IFileEditorInput) {
				IFile file = ((IFileEditorInput)fEditorInput).getFile();
				fXModelObject = EclipseResourceUtil.getObjectByResource(file);
			}
			fXModel = (fXModelObject == null) ? null : fXModelObject.getModel();
		} catch (Exception x) {
			JspEditorPlugin.getPluginLog().logError(x);
			this.fProvider = null;
			this.fXModel = null;
		}
	}

	public XModel getXModel() { 
		return fXModel;
	}

	public XModelObject getXModelObject() { 
		return fXModelObject;
	}

}
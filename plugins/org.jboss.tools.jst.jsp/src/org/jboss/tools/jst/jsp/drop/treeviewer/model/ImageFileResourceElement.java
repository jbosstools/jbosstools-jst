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


import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.ui.IEditorInput;

import org.jboss.tools.jst.jsp.support.kb.WTPKbImageFileResource;
import org.jboss.tools.jst.web.kb.internal.taglib.CustomProposalType.Param;

/**
 * @author Igels
 */
public class ImageFileResourceElement extends AttributeValueResource {

	private ModelElement[] children;

	protected WTPKbImageFileResource wtpKbResource;

	protected ImageFileResourceElement(String name, ModelElement parent) {
		super(name, parent);
	}

	public ImageFileResourceElement(IEditorInput editorInput, ModelElement parent) {
		super(parent);
		wtpKbResource = new WTPKbImageFileResource(editorInput);
		if(wtpKbResource.isReadyToUse()) {
			name = wtpKbResource.getWebRootResource().getName();
		} else {
			name = ""; //$NON-NLS-1$
		}
	}

	public void setParams(Param[] params) {
		super.setParams(params);
		if(params != null && params.length > 0 && wtpKbResource != null) {
			for (Param p: params) {
				wtpKbResource.setConstraint(p.getName(), p.getValue());
			}
		}
	}

	protected String getFullName() {
		return "/"; //$NON-NLS-1$
	}

	public int compareValue(String value) {
		if(value==null) {
			return NOT_EQUAL_VALUES;
		}
		String thisValue = getComparedValue();
		if(wtpKbResource != null && wtpKbResource.getPathAddition() != null && value.startsWith(wtpKbResource.getPathAddition())) {
			thisValue = wtpKbResource.getPathAddition() + thisValue;
		}
		if(value.equals(thisValue)) {
			return EQUAL_VALUES;
		} else if(value.startsWith(thisValue) || thisValue.startsWith(value)) {
			return value.length() - thisValue.length();
		}
		return NOT_EQUAL_VALUES;
	}

	public ModelElement[] getChildren() {
		if(children==null) {
			if(wtpKbResource.isReadyToUse()) {
				WTPKbImageFileResource.ImagePathDescriptor[] pathes = wtpKbResource.getImagesFilesPathes(getFullName());
				children = new ModelElement[pathes.length];
				for(int i=0; i<pathes.length; i++) {
					IResource resource = pathes[i].getResource();
					if(resource instanceof IFolder) {
						children[i] = new ImageFolderElement(resource.getName(), this, wtpKbResource);
					} else {
						children[i] = new ImageFileElement(resource.getName(), this);
					}
				}
			}
		}
		if(children==null) {
			return new ModelElement[0];
		}
		return children;
	}
}
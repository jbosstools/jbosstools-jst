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

import org.jboss.tools.jst.jsp.support.kb.WTPKbImageFileResource;

/**
 * @author Igels
 */
public class ImageFolderElement extends ImageFileResourceElement {

	public ImageFolderElement(String name, ModelElement parent, WTPKbImageFileResource wtpKbResource) {
		super(name, parent);
		this.wtpKbResource = wtpKbResource;
	}

	protected String getFullName() {
		return parent.getFullName() + getName() + "/"; //$NON-NLS-1$
	}
}
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
package org.jboss.tools.jst.web.ui.internal.editor.drop.treeviewer.model;

/**
 * @author Igels
 */
public class UnknownAttributeValueResource extends AttributeValueResource {

	public UnknownAttributeValueResource(ModelElement parent) {
		super(parent);
	}

	public UnknownAttributeValueResource(String name, ModelElement parent) {
		super(name, parent);
	}

	public ModelElement[] getChildren() {
		return new ModelElement[0];
	}
}
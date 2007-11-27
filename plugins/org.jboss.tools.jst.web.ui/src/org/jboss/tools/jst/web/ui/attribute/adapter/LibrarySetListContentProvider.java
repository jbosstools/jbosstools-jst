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
package org.jboss.tools.jst.web.ui.attribute.adapter;

import org.jboss.tools.common.model.ui.attribute.adapter.DefaultXAttributeListContentProvider;

import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.jst.web.project.helpers.LibrarySets;

public class LibrarySetListContentProvider extends DefaultXAttributeListContentProvider {
	XModelObject object;
	
	public void setObject(XModelObject object) {
		this.object = object;
	}
	
	protected void loadTags() {
		tags = LibrarySets.getInstance().getLibrarySetList();
	}

}

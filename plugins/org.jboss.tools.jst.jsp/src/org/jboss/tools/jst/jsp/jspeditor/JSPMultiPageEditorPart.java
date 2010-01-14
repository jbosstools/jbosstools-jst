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
package org.jboss.tools.jst.jsp.jspeditor;


import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.jboss.tools.common.core.resources.XModelObjectEditorInput;

/**
 * 
 */
public abstract class JSPMultiPageEditorPart extends MultiPageEditorPart {

	protected JSPMultiPageEditorPart() {
		super();
	}

	protected IEditorPart getActiveEditor() {
		return super.getActiveEditor();
	}

	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(XModelObjectEditorInput.checkInput(input));
		site.setSelectionProvider(new JSPMultiPageSelectionProvider(this));
	}

}

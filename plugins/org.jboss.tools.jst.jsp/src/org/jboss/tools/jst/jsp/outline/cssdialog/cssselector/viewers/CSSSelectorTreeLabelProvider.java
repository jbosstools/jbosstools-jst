/*******************************************************************************
 * Copyright (c) 2007-2010 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.viewers;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Constants;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.model.CSSRuleContainer;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.model.CSSStyleSheetContainer;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.model.CSSTreeNode;

/**
 * 
 * @author yzhishko
 *
 */

public class CSSSelectorTreeLabelProvider extends LabelProvider {

	private final static ImageDescriptor CSS_STYLE_SHEET_DESCR = JspEditorPlugin.getImageDescriptor(Constants.IMAGE_STYLE_SHEET_LOCATION);
	private final static ImageDescriptor CSS_STYLE_CLASS_DESCR = JspEditorPlugin.getImageDescriptor(Constants.IMAGE_STYLE_CLASS_LOCATION);
	
	@Override
	public Image getImage(Object element) {
		if (((CSSTreeNode) element).getCSSContainer() instanceof CSSStyleSheetContainer) {
			return CSS_STYLE_SHEET_DESCR.createImage();
		}
		if (((CSSTreeNode) element).getCSSContainer() instanceof CSSRuleContainer) {
			return CSS_STYLE_CLASS_DESCR.createImage();
		}
		return super.getImage(element);
	}
	
}

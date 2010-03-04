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
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Constants;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.model.CSSSelectorTableModel;

/**
 * 
 * @author yzhishko
 *
 */

public class CSSSelectorTableViewer extends TableViewer{

	public final static String CSS_SELECTOR_TABLE_VIWER_ID = "css_selector_table_viwer"; //$NON-NLS-1$
	private final static ImageDescriptor CSS_STYLE_CLASS_DESCR = JspEditorPlugin.getImageDescriptor(Constants.IMAGE_STYLE_CLASS_LOCATION);
	
	public CSSSelectorTableViewer(Composite parent, int style) {
		super(parent, style);
		setContentProvider(new CSSSelectorTableContentProvider());
		setLabelProvider(new LabelProvider(){
			@Override
			public Image getImage(Object element) {
				return CSS_STYLE_CLASS_DESCR.createImage();
			}
		});
	}
	
	public void setModel(CSSSelectorTableModel model){
		setInput(model.getContainerList());
	}
	
}

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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;
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

	private CSSSelectorTableModel model;
	
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
		this.model = model;
	}
	
	public CSSSelectorTableModel getModel(){
		return model;
	}
	
	@Override
	public void add(Object element) {
		if (element != null) {
			if (isContain(element)) {
				return;
			}
		}
		super.add(element);
	}
	
	@Override
	public void add(Object[] elements) {
		List<Object> objects = new ArrayList<Object>(0);
		if (elements != null) {
			for (int i = 0; i < elements.length; i++) {
				if (!isContain(elements[i])) {
					objects.add(elements[i]);
				}
			}
		}
		super.add(objects.toArray());
	}
	
	private boolean isContain(Object element){
		TableItem[] items = getTable().getItems();
		if (items != null) {
			for (int i = 0; i < items.length; i++) {
				if (element.equals(items[i].getData())) {
					return true;
				}
			}
		}
		return false;
	}
	
}

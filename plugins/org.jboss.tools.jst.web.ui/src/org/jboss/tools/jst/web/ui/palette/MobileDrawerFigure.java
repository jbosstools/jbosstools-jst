/******************************************************************************* 
 * Copyright (c) 2013-2014 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.palette;

import org.eclipse.draw2d.ButtonModel;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.jboss.tools.jst.web.ui.palette.internal.ListFigure;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.PaletteDrawerImpl;

class MobileDrawerFigure extends CustomDrawerFigure {
	private PaletteDrawerImpl category;
        
	public MobileDrawerFigure(PaletteDrawerImpl category, Control control) {
		super(control);

		this.category = category;

		Figure collapseToggle = (Figure)getChildren().get(0);
		Figure title = (Figure)collapseToggle.getChildren().get(0);
		Figure pinFigure = (Figure)title.getChildren().get(0);
		Figure drawerFigure = (Figure)title.getChildren().get(1);

		if(category.getVersions().length > 0) {
			VersionFigure label = new VersionFigure(category.getVersion().toString(), control);
			
			GridLayout layout = new GridLayout(4, false);
			layout.horizontalSpacing=4;
			title.setLayoutManager(layout);
			layout.setConstraint(drawerFigure, new GridData(SWT.FILL, 0, true, false));
			
			title.add(drawerFigure);
			title.add(label);
			title.add(pinFigure);
		}
	}

	@Override
	protected void handleExpandStateChanged() {
		super.handleExpandStateChanged();
		if(isCalledByButtonModel()) {
			category.getPaletteGroup().getPaletteModel().onCategoryExpandChange(category.getLabel(), isExpanded());
		}
	}

	private boolean isCalledByButtonModel() {
		boolean buttonModel = false;
		for (StackTraceElement s: new Throwable().getStackTrace()) {
			if(ButtonModel.class.getName().endsWith(s.getClassName())) {
				buttonModel = true;
			} else if(MobileDrawerEditPart.class.getName().equals(s.getClassName())) {
				return false;
			}
		}
		return buttonModel;
		
	}
	
	public class VersionFigure extends ListFigure{

		public VersionFigure(String text, Control control){
			super(text, control);
		}

		@Override
		public String[] getValues() {
			return category.getVersions();
		}

		@Override
		public void setSelected(String value) {
			getLabel().setText(value);
			category.setPreferredVersion(value);
			category.loadVersion(value);
		}

	}
}
/******************************************************************************* 
 * Copyright (c) 2014 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.palette.internal;

import org.eclipse.draw2d.ButtonModel;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.jboss.tools.jst.web.ui.palette.CustomDrawerFigure;
import org.jboss.tools.jst.web.ui.palette.MobileDrawerEditPart;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.HTML5DynamicPaletteGroup;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.PaletteDrawerImpl;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.PaletteModelImpl;

public class HTML5DynamicDrawerFigure extends CustomDrawerFigure {
	private PaletteDrawerImpl drawer;
	private HTML5DynamicPaletteGroup group;
	private String[] numbers = new String[]{"  5", "10", "15", "20", "25"};
	private String[] types = new String[]{PaletteSettings.TYPE_MOST_POPULAR, PaletteSettings.TYPE_LAST_USED};
	
	private NumberFigure numberFigure;
	private TypeFigure typeFigure;

        
	public HTML5DynamicDrawerFigure(PaletteDrawerImpl category, Control control) {
		super(control);
		this.drawer = category;
		this.group = (HTML5DynamicPaletteGroup)category.getPaletteGroup();

		Figure collapseToggle = (Figure)getChildren().get(0);
		Figure title = (Figure)collapseToggle.getChildren().get(0);
		Figure pinFigure = (Figure)title.getChildren().get(0);
		Figure drawerFigure = (Figure)title.getChildren().get(1);

		numberFigure = new NumberFigure(""+PaletteSettings.getInstance().getDynamicGroupNumber(), control);
		
		typeFigure = new TypeFigure(PaletteSettings.getInstance().getDynamicGroupType(), control);
		GridLayout layout = new GridLayout(4, false);
		layout.horizontalSpacing=7;

		title.setLayoutManager(layout);
		layout.setConstraint(drawerFigure, new GridData(GridData.FILL_HORIZONTAL));
		
		title.add(drawerFigure);
		title.add(numberFigure);
		title.add(typeFigure);
		title.add(pinFigure);
	}
	
	public void refresh(){
		if(!numberFigure.getSelected().equals(""+PaletteSettings.getInstance().getDynamicGroupNumber())){
			numberFigure.setSelected(""+PaletteSettings.getInstance().getDynamicGroupNumber());
		}
		if(!typeFigure.getSelected().equals(PaletteSettings.getInstance().getDynamicGroupType())){
			typeFigure.setSelected(PaletteSettings.getInstance().getDynamicGroupType());
		}
	}

	@Override
	protected void handleExpandStateChanged() {
		super.handleExpandStateChanged();
		if(isCalledByButtonModel()) {
			group.getPaletteModel().onCategoryExpandChange(PaletteModelImpl.DYNAMIC_PALETTE_GROUP, isExpanded());
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

	public class NumberFigure extends ListFigure{

		public NumberFigure(String text, Control control){
			super(text, control);
		}

		@Override
		public String[] getValues() {
			return numbers;
		}

		@Override
		public void setSelected(String value) {
			getLabel().setText(value);
			PaletteSettings.getInstance().setDynamicGroupNumber(new Integer(value.trim()));
			drawer.loadVersion(group.getSelectedVersionGroup().getVersion());
		}
	}

	public class TypeFigure extends ListFigure{
		public TypeFigure(String text, Control control){
			super(text, control);
		}

		@Override
		public String[] getValues() {
			return types;
		}

		@Override
		public void setSelected(String value) {
			getLabel().setText(value);
			PaletteSettings.getInstance().setDynamicGroupType(value);
			drawer.loadVersion(group.getSelectedVersionGroup().getVersion());
		}
	}
}
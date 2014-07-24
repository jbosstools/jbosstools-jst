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

import org.eclipse.draw2d.ActionEvent;
import org.eclipse.draw2d.ActionListener;
import org.eclipse.draw2d.ButtonModel;
import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.jboss.tools.jst.web.ui.JSTWebUIImages;
import org.jboss.tools.jst.web.ui.palette.model.PaletteCategory;
import org.jboss.tools.jst.web.ui.palette.model.PaletteModel;
import org.jboss.tools.jst.web.ui.palette.model.PaletteRoot;

class MobileDrawerFigure extends CustomDrawerFigure {
	private Control control;
	private static JQueryMobileVersionPopUp popup;

	private PaletteCategory category;
        
	public MobileDrawerFigure(PaletteCategory category, Control control) {
		super(control);

		this.category = category;
		this.control = control;

		Figure collapseToggle = (Figure)getChildren().get(0);
		Figure title = (Figure)collapseToggle.getChildren().get(0);
		Figure pinFigure = (Figure)title.getChildren().get(0);
		Figure drawerFigure = (Figure)title.getChildren().get(1);

		if(category.getAvailableVersions().length > 0) {
			VersionFigure label = new VersionFigure(category.getVersion());
			GridLayout layout = new GridLayout(4, false);
			title.setLayoutManager(layout);
               
			layout.setConstraint(drawerFigure, new GridData(GridData.FILL_HORIZONTAL));
				title.add(drawerFigure);
			title.add(label);
			title.add(pinFigure);
		}
	}

	private Label label = new Label("", JSTWebUIImages.getImage(JSTWebUIImages.getInstance().createImageDescriptor(JSTWebUIImages.DROP_DOWN_LIST_IMAGE)));

	public class VersionFigure extends Clickable{
		private Color backColor = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
		private Color foreColor = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);

		public VersionFigure(String text){
			super(label);
			label.setText(text);
			label.setTextPlacement(Label.WEST);
			setRolloverEnabled(true);
			setBorder(new MarginBorder(2));
			addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent event) {
					popup = new JQueryMobileVersionPopUp(control, VersionFigure.this);
					popup.show(category.getAvailableVersions());
				}
			});
		}

		public void setVersion(String newVersion) {
			((Label)getChildren().get(0)).setText(newVersion);
			PaletteModel paletteModel = ((PaletteRoot)category.getParent()).getPaletteModel();
			paletteModel.getPaletteContents().setPreferredVersion(category.getLabel(), newVersion);
			paletteModel.reloadCategory(category);
		}
                
		public String getVersion() {
			return ((Label)getChildren().get(0)).getText();
		}

		@Override
		protected void paintFigure(Graphics graphics) {
			super.paintFigure(graphics);

			ButtonModel model = getModel();
			if (isRolloverEnabled() && model.isMouseOver()) {
				graphics.setBackgroundColor(backColor);
				graphics.fillRoundRectangle(getClientArea().getCopy().getExpanded(1, 1), 7, 7);

				graphics.setForegroundColor(foreColor);
				graphics.drawRoundRectangle(getClientArea().getCopy().getExpanded(1, 1), 7, 7);
			}
		}
	}
}
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

import org.eclipse.draw2d.ActionEvent;
import org.eclipse.draw2d.ActionListener;
import org.eclipse.draw2d.ButtonModel;
import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.jboss.tools.jst.web.ui.JSTWebUIImages;

abstract public class ListFigure extends Clickable implements IListFigure {
	private Color backColor = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
	private Color foreColor = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
	private ListPopUp popup;

	public ListFigure(String defaultText, final Control control){
		super(new Label(defaultText, JSTWebUIImages.getImage(JSTWebUIImages.getInstance().createImageDescriptor(JSTWebUIImages.DROP_DOWN_LIST_IMAGE))));
		getLabel().setTextPlacement(Label.WEST);
		setRolloverEnabled(true);
		setBorder(new MarginBorder(2));
		addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				popup = new ListPopUp(control, ListFigure.this);
				popup.show(ListFigure.this.getValues());
			}
		});
	}
	
	protected Label getLabel(){
		return (Label)getChildren().get(0);
	}
	
	@Override
	public String getSelected() {
		return getLabel().getText();
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

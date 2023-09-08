/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.palette;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FocusEvent;
import org.eclipse.draw2d.FocusListener;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteSeparator;
import org.jboss.tools.jst.web.ui.palette.internal.HTML5DynamicDrawerFigure;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.HTML5DynamicPaletteGroup;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.PaletteDrawerImpl;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.PaletteTool;

public class MobileDrawerEditPart extends CustomDrawerEditPart {


		public MobileDrawerEditPart(PaletteDrawer drawer) {
                super(drawer);
        }

        @Override
        protected int getLayoutSetting() {
                return PaletteViewerPreferences.LAYOUT_COLUMNS;
        }
        
        public IFigure createFigure() {
        		PaletteDrawerImpl category = (PaletteDrawerImpl)getModel();
        		if(category.getPaletteGroup() instanceof HTML5DynamicPaletteGroup){
        			HTML5DynamicDrawerFigure fig = new HTML5DynamicDrawerFigure(category, getViewer().getControl()) {
                        public IFigure getToolTip() {
                                return createToolTip();
                        }
                };
                fig.setExpanded(getModel().isInitiallyOpen());
                fig.setPinned(getModel().isInitiallyPinned());
                fig.getCollapseToggle().setRequestFocusEnabled(true);
                fig.getCollapseToggle().addFocusListener(new FocusListener() {
                        public void focusGained(FocusEvent fe) {
                                getViewer().select(MobileDrawerEditPart.this);
                        }
                        public void focusLost(FocusEvent fe) {
                        }
                });
                return fig;
  			
        		}else{
                MobileDrawerFigure fig = new MobileDrawerFigure(category, getViewer().getControl()) {
                        public IFigure getToolTip() {
                                return createToolTip();
                        }
                };
                fig.setExpanded(getModel().isInitiallyOpen());
                fig.setPinned(getModel().isInitiallyPinned());
                fig.getCollapseToggle().setRequestFocusEnabled(true);
                fig.getCollapseToggle().addFocusListener(new FocusListener() {
                        public void focusGained(FocusEvent fe) {
                                getViewer().select(MobileDrawerEditPart.this);
                        }
                        public void focusLost(FocusEvent fe) {
                        }
                });
                return fig;
        		}
        }
        
        @Override
		public void refresh() {
			super.refresh();
			updateMessage();
        }
        
        @Override
		public void propertyChange(PropertyChangeEvent evt) {
			super.propertyChange(evt);
			updateMessage();
        }
        
        private FlowPage messageLabel;
        private LayoutManager storedLayout;
        
		private void updateMessage() {
			if(isNoVisibleItems()){
				if(messageLabel == null){
					String message = PaletteUIMessages.NO_LAST_USED_OR_MOST_POPULAR_YET;
					
					if(!isEmpty()){
						message = PaletteUIMessages.NO_ITEMS_FOUND;
					}
					
					messageLabel = new FlowPage();
					TextFlow tf = new TextFlow();
					tf.setForegroundColor(ColorConstants.gray);
		    		tf.setText(message);
		    		messageLabel.add(tf);
					
					storedLayout = getDrawerFigure().getScrollpane().getContents().getLayoutManager();
					GridLayout layout = new GridLayout(1, false);
					layout.setConstraint(messageLabel, new GridData(GridData.FILL_HORIZONTAL));
					
					getDrawerFigure().getScrollpane().getContents().setLayoutManager(layout);
					
				}
				getDrawerFigure().getScrollpane().getContents().add(messageLabel);
			}else{
				if(messageLabel != null){
					getDrawerFigure().getScrollpane().getContents().remove(messageLabel);
					getDrawerFigure().getScrollpane().getContents().setLayoutManager(storedLayout);
					messageLabel = null;
				}
			}
		}
        
        private boolean isNoVisibleItems(){
        	for(Object child : getModel().getChildren()){
        		if(!(child instanceof PaletteSeparator) && ((PaletteTool)child).isVisible()){
        			return false;
        		}
        	}
        	return true;
        }
        
        private boolean isEmpty(){
        	for(Object child : getModel().getChildren()){
        		if(!(child instanceof PaletteSeparator)){
        			return false;
        		}
        	}
        	return true;
        }
}

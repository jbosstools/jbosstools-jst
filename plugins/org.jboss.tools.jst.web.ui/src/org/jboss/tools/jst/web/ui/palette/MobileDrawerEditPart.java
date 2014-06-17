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

import org.eclipse.draw2d.FocusEvent;
import org.eclipse.draw2d.FocusListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.palette.PaletteDrawer;
import org.jboss.tools.jst.web.ui.palette.model.PaletteCategory;

public class MobileDrawerEditPart extends CustomDrawerEditPart {

        public MobileDrawerEditPart(PaletteDrawer drawer) {
                super(drawer);
        }

        @Override
        protected int getLayoutSetting() {
                return PaletteViewerPreferences.LAYOUT_ICONS;
        }
        
        public IFigure createFigure() {
        		PaletteCategory category = (PaletteCategory)getModel();

                MobileDrawerFigure fig = new MobileDrawerFigure(category, getViewer().getControl()) {
                        public IFigure getToolTip() {
                                return createToolTip();
                        }
                };
                fig.setExpanded(getDrawer().isInitiallyOpen());
                fig.setPinned(getDrawer().isInitiallyPinned());
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

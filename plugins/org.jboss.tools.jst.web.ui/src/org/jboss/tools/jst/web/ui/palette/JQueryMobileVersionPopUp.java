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

import org.eclipse.draw2d.ActionEvent;
import org.eclipse.draw2d.ActionListener;
import org.eclipse.draw2d.ButtonModel;
import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.PopUpHelper;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.jboss.tools.jst.web.kb.internal.taglib.html.IHTMLLibraryVersion;
import org.jboss.tools.jst.web.ui.palette.MobileDrawerFigure.VersionFigure;

class JQueryMobileVersionPopUp extends PopUpHelper {
        private VersionFigure figureToShowNear;

        public JQueryMobileVersionPopUp(Control c, VersionFigure figureToShowNear) {
                super(c, SWT.TOOL | SWT.ON_TOP);
                this.figureToShowNear = figureToShowNear;
                getShell().setBackground(ColorConstants.menuBackground);
                getShell().setForeground(ColorConstants.menuForeground);
        }
        
        protected void hookShellListeners() {
                getShell().addFocusListener(new FocusAdapter(){
                        public void focusLost(FocusEvent e) {
                                hide();
                        }
                });
                getShell().addKeyListener(new KeyAdapter(){

                        @Override
                        public void keyPressed(KeyEvent e) {
                                if(e.keyCode == 27){
                                        hide();
                                }
                        }
                        
                });
        }
        
        public boolean isShowing() {
                if (getShell() != null && !getShell().isDisposed()){
                        return getShell().isVisible();
                }
                return false;
        }
        
        private Viewport getViewport(IFigure figure){
        	IFigure parent = figure;
        	while(parent != null){
        		if(parent instanceof Viewport){
        			return (Viewport) parent;
        		}
        		parent = parent.getParent();
        	}
        	return null;
        }
        
        public void displayToolTip(IFigure hoverSource, IFigure tip) {
                getLightweightSystem().setContents(tip);
                Dimension shellSize = getLightweightSystem().getRootFigure()
                                .getPreferredSize().getExpanded(getShellTrimSize());
                org.eclipse.draw2d.geometry.Rectangle rect = hoverSource.getClientArea();
                
                Viewport vp = getViewport(hoverSource);
                
                Point viewportLocation = vp.getViewLocation().getSWTPoint();
                
                Point displayPoint = new Point(rect.x-5, rect.y+10+rect.height-viewportLocation.y);
                
                org.eclipse.swt.graphics.Point absolute;
                absolute = control.toDisplay(new org.eclipse.swt.graphics.Point(
                                displayPoint.x, displayPoint.y));
                
                setShellBounds(absolute.x, absolute.y, shellSize.width,
                                shellSize.height);
                show();
                getShell().forceFocus();
        }
        
        public void show(IHTMLLibraryVersion[] versions){
                String currentVersion = figureToShowNear.getVersion();
                Panel panel = new Panel();
                panel.setLayoutManager(new GridLayout(1, false));
                for(IHTMLLibraryVersion version : versions){
                        ItemFigure label = new ItemFigure(version);
                        panel.add(label);
                        if(version.equals(currentVersion)){
                                label.setSelected(true);
                        }
                }
                displayToolTip(figureToShowNear, panel);
        }
        
        public void close(){
                hide();
        }
        
        public class ItemFigure extends Clickable{
                private Color backColor = ColorConstants.menuBackgroundSelected;
                private Color foreColor = ColorConstants.menuForegroundSelected;
                
                public ItemFigure(final IHTMLLibraryVersion version){
                        super(new Label(version.toString()));
                        setRolloverEnabled(true);
                        setRequestFocusEnabled(false);
                        setFocusTraversable(false);
                        //setBorder(new MarginBorder(2));
                        addActionListener(new ActionListener(){
                                @Override
                                public void actionPerformed(ActionEvent event) {
                                        JQueryMobileVersionPopUp.this.hide();
                                        figureToShowNear.setVersion(version);
                                }
                        });
                }
                
                protected void paintFigure(Graphics graphics) {
                        super.paintFigure(graphics);

                        ButtonModel model = getModel();
                        if ((isRolloverEnabled() && model.isMouseOver()) || isSelected() ) {
                                graphics.setBackgroundColor(backColor);
                                graphics.fillRectangle(getClientArea().getCopy().getExpanded(1, 1));

                                graphics.setForegroundColor(foreColor);
                                graphics.drawRectangle(getClientArea().getCopy().getExpanded(1, 1));
                        }
                }
        }
}

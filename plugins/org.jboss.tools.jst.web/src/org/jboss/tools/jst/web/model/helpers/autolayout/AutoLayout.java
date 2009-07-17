/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.web.model.helpers.autolayout;

import org.jboss.tools.common.model.*;

public class AutoLayout {
	LayuotConstants constants;
    protected Items items;

    public AutoLayout() {
    }

    public void setItems(Items items) {
    	this.items = items;
    	constants = items.constants;
    	constants.update();
    }

    public void setOverride(boolean b) {
        items.setOverride(b);
    }

    public void setProcess(XModelObject process) {
		constants.update();
        items.setProcess(process);
        apply();
        if(items.override) {
			TransitionArranger a = items.createTransitionArranger();
			a.setItems(items.items);
			a.execute();
        }
    }

    private void apply() {
		resetTransitions(); // temporal

        Item[] is = items.items;
        int[] yDeltas = items.groups.yDeltas;
        for (int i = 0; i < is.length; i++) {
            if(is[i].isSet()) continue;
            XModelObject o = is[i].object;
            int x = is[i].ix * constants.deltaX + constants.indentX;
            int y = is[i].iy * constants.deltaY + constants.indentY;
            if(items.isZigzagging()) {
            	if(is[i].ix % 2 == 1) y += 16;
            }
            x += is[i].group.xDeltas[is[i].ix] * constants.incX;
            y += yDeltas[is[i].iy] * constants.incY + is[i].yIndent;
            o.setAttributeValue("shape", "" + x + "," + y + ",0,0"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        }
    }
    
    private void resetTransitions() {
    	if(!items.override) return;
		Item[] is = items.items;
		for (int i = 0; i < is.length; i++) {
			XModelObject o = is[i].object;
			if(o.getModelEntity().getAttribute("link shape") != null) //$NON-NLS-1$
			  o.setAttributeValue("link shape", ""); //$NON-NLS-1$ //$NON-NLS-2$
			XModelObject[] os = items.getOutput(o);
			for (int j = 0; j < os.length; j++) {
//				String attr = (os[j].getModelEntity().getAttribute("link shape") != null) ? "link shape" : "shape";
				os[j].setAttributeValue("shape", ""); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
    	
    }

}
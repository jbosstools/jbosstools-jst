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
import org.jboss.tools.jst.web.model.helpers.WebProcessStructureHelper;
import org.jboss.tools.jst.web.model.process.WebProcessConstants;

public class AutoPlacement {
	LayuotConstants constants = new LayuotConstants();

    private static WebProcessStructureHelper h = new WebProcessStructureHelper();
    private static int SIZE = 5;
    int[][] field = new int[SIZE][SIZE];
    int x0 = -1, y0 = -1;

    public AutoPlacement() {}

    public void place(XModelObject process, XModelObject source, XModelObject target) {
    	constants.update();
        if(target.getAttributeValue(WebProcessConstants.ATT_SHAPE).length() > 0) return;
        source = getItem(process, source);
        if(source == null) return;
        int[] shape = h.asIntArray(source, WebProcessConstants.ATT_SHAPE);
        if(shape == null || shape.length < 4) return;
        x0 = shape[0];
        y0 = shape[1];
        fillField(process);
        int[] p = findPoint();
        if(p != null) target.setAttributeValue(WebProcessConstants.ATT_SHAPE, "" + p[0] + "," + p[1] + ",0,0"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    private XModelObject getItem(XModelObject process, XModelObject source) {
        XModelObject p = source.getParent();
        if(p == process) return source;
        source = p;
        p = p.getParent();
        return (p == process) ? source : null;
    }

    private void fillField(XModelObject process) {
        for (int i = 0; i < SIZE; i++) for (int j = 0; j < SIZE; j++) field[i][j] = 0;
        XModelObject[] items = process.getChildren();
        for (int i = 0; i < items.length; i++) {
            int[] shape = h.asIntArray(items[i], WebProcessConstants.ATT_SHAPE);
            if(shape == null || shape.length < 4) continue;
            int x1 = shape[0], y1 = shape[1];
            int ix = (x1 - x0 + (constants.deltaX / 2)) / constants.deltaX - 1;
            int iy = (y1 - y0 + (constants.deltaY / 2)) / constants.deltaY;
            if(ix < 0 || ix >= SIZE || iy < 0 || iy >= SIZE) continue;
            field[ix][iy] = 1;
        }
    }

    private int[] findPoint() {
        for (int is = 0; is < 2 * SIZE; is++) for (int iy = SIZE - 1; iy >= 0; iy--) {
            int ix = is - iy;
            if(ix < 0 || ix >= SIZE || field[ix][iy] == 1) continue;
            return new int[]{x0 + (ix + 1) * constants.deltaX, y0 + iy * constants.deltaY};
        }
        return null;
    }

}

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

import java.util.*;
import org.jboss.tools.common.model.*;
import org.jboss.tools.jst.web.WebModelPlugin;
import org.jboss.tools.jst.web.model.helpers.WebProcessStructureHelper;
import org.jboss.tools.jst.web.model.process.WebProcessConstants;

public class Items implements WebProcessConstants {
	protected LayuotConstants constants;
    protected WebProcessStructureHelper h = new WebProcessStructureHelper();
    protected XModelObject process;
    protected Item[] items;
    protected Map<String,Item> paths = new HashMap<String,Item>();
    protected Groups groups = new Groups();
    protected boolean override = false;

    public Items() {}

    public void setOverride(boolean b) {
        override = b;
    }

    public void setProcess(XModelObject process) {
        this.process = process;
        try {
        	load();
        } catch (Exception e) {
        	WebModelPlugin.getPluginLog().logError(e);
        }
    }

    private void load() {
        initItems();
        if(isAllSet()) return;
        buildBinds();
        groups.load(items);
        print();
   }

    private void initItems() {
        XModelObject[] is = process.getChildren();
        items = new Item[is.length];
        for (int i = 0; i < is.length; i++) {
            Item item = new Item();
            items[i] = item;
            paths.put(is[i].getPathPart(), item);
            item.n = i;
            item.object = is[i];
            int[] shape = h.asIntArray(is[i], ATT_SHAPE);
            if(!override && shape != null && shape.length > 1) {
                item.x = shape[0];
                item.y = shape[1];
                if(item.x != 0 && item.y != 0) item.isSet = true;
                item.ix = (item.x / constants.deltaX);
                item.iy = (item.y / constants.deltaY);
                if(item.ix < 0) item.ix = 0;
                if(item.iy < 0) item.iy = 0;
                if(item.ix >= Groups.FX) item.ix = Groups.FX - 1; 
				if(item.iy >= Groups.FY) item.iy = Groups.FY - 1; 
            }
			initItem(item);
        }
    }
    
    // override
    protected void initItem(Item item) {}
    
    // override
    
    public XModelObject[] getOutput(XModelObject itemObject) {
    	return itemObject.getChildren();    	
    }

    private boolean isAllSet() {
        for (int i = 0; i < items.length; i++) if(!items[i].isSet()) return false;
        return true;
    }

    private void buildBinds() {
        for (int i = 0; i < items.length; i++) {
            XModelObject[] ts = (items[i].weight < 0 || items[i].isComment())
                                ? new XModelObject[]{items[i].object}
                                : getOutput(items[i].object);
            for (int j = 0; j < ts.length; j++) {
                String target = ts[j].getAttributeValue(ATT_TARGET);
                if(target == null || target.length() == 0) continue;
                Item item2 = (Item)paths.get(target);
                if(item2 == null) {
                	continue;
                } if(items[i].isComment()) {
					item2.addComment(items[i].n);
					items[i].isOwned = true;
				} else if(item2.weight < 0) {
					continue; 
				} else {
                	item2.addInput(items[i].n, ts[j]);
                	items[i].addOutput(item2.n);
				}
            }
        }
    }

   private void print() {
       for (int i = 0; i < items.length; i++)
         items[i].print();
   }

}


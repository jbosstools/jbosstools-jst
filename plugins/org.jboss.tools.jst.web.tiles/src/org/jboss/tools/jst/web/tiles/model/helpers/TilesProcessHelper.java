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
package org.jboss.tools.jst.web.tiles.model.helpers;

import java.util.*;
import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.util.XModelObjectUtil;
import org.jboss.tools.jst.web.tiles.model.*;

public class TilesProcessHelper implements TilesConstants {
	private XModelObject process;
	private XModelObject config;
	private Map items = new HashMap();
	private Map targets = new HashMap();
	XModelObject[] tiles;
	
	public TilesProcessHelper(TilesProcessImpl process) {
		this.process = process;
	}

	public static TilesProcessHelper getHelper(XModelObject process) {
		return ((TilesProcessImpl)process).getHelper();
	}

	private synchronized void reset() {
		items.clear();
		targets.clear();
		this.config = process.getParent();
	}

	public void restoreRefs() {
		((TilesProcessImpl)process).setReference(process.getParent());
	}
	
	Set updateLocks = new HashSet();
	
	public boolean isUpdateLocked() {
		return updateLocks.size() > 0;
	}
	
	public void addUpdateLock(Object lock) {
		updateLocks.add(lock);
	}

	public void removeUpdateLock(Object lock) {
		updateLocks.remove(lock);
	}

	public void updateProcess() {
		if(isUpdateLocked()) return;
		addUpdateLock(this);
		try {
			updateProcess0();
			updateTargets0();
		} finally {
			removeUpdateLock(this);
		}
	}
	private void updateProcess0() {
		reset();
		tiles = config.getChildren(ENT_DEFINITION);
		for (int i = 0; i < tiles.length; i++) {
			String name = tiles[i].getAttributeValue(ATT_NAME);
			XModelObject g = findOrCreateItem(name, name);
			items.put(name, g);
			String ext = tiles[i].getAttributeValue("extends"); //$NON-NLS-1$
			if(ext.length() > 0) {
				targets.put(ext, ext);
			}
		}
        Iterator it = items.keySet().iterator();
        while(it.hasNext()) targets.remove(it.next());
		removeObsoleteItems();
		createRefItems();
		updateItems();
	}

	public XModelObject findOrCreateItem(String path, String pp) {
		XModelObject g = process.getChildByPath(pp);
		if(g == null) {
			g = process.getModel().createModelObject(ENT_PROCESS_ITEM, null);
			g.setAttributeValue(ATT_NAME, pp);
			g.setAttributeValue(ATT_PATH, path);
			process.addChild(g);
		}
		return g;
	}
	
	private void removeObsoleteItems() {
		boolean q = false;
		XModelObject[] ps = process.getChildren(ENT_PROCESS_ITEM);
		for (int i = 0; i < ps.length; i++) {
			String path = ps[i].getPathPart();
			if(!items.containsKey(path) && !targets.containsKey(path)) {
				if(q && "true".equals(ps[i].getAttributeValue("persistent"))) { //$NON-NLS-1$ //$NON-NLS-2$
					items.put(path, ps[i]);
				} else {
					ps[i].removeFromParent();
				}
			}
		}
	}
	
	private void createRefItems() {
		String[] paths = (String[])targets.keySet().toArray(new String[0]);
		for (int i = 0; i < paths.length; i++) {
			XModelObject g = findOrCreateItem(paths[i], paths[i]);
			targets.put(paths[i], g);			
		}
	}
	
	private void updateItems() {
		TilesProcessItemImpl[] gs = (TilesProcessItemImpl[])items.values().toArray(new TilesProcessItemImpl[0]);
		for (int i = 0; i < gs.length; i++) {
			setItemReference(gs[i]);
			updateItem(gs[i]);
		}
		gs = (TilesProcessItemImpl[])targets.values().toArray(new TilesProcessItemImpl[0]);
		for (int i = 0; i < gs.length; i++) {
			gs[i].setReference(null);
			updateItem(gs[i]);
		}
	}
	
	private void setItemReference(TilesProcessItemImpl item) {
		String path = item.getPathPart();
		XModelObject r = config.getChildByPath(path);
		item.setReference(r);
	}
	
	private void updateItem(TilesProcessItemImpl item) {
		if(!item.isUpToDate()) {
			item.notifyUpdate();
			XModelObject r = item.getReference();
			if(r != null) {
				item.setAttributeValue(ATT_PATH, r.getAttributeValue(ATT_NAME));
			}
		}
		updateOutputs(item);
	}
	
	private void updateOutputs(TilesProcessItemImpl item) {
		XModelObject r = item.getReference();
		String ext = (r == null) ? "" : r.getAttributeValue("extends"); //$NON-NLS-1$ //$NON-NLS-2$
		updateOutputs(item, ext, item.getChildren());
	}

	private void updateOutputs(TilesProcessItemImpl item, String ext, XModelObject[] outputs) {
		XModelObject output = null;
		if(ext.length() == 0) {
			for (int i = 0; i < outputs.length; i++) outputs[i].removeFromParent();
			return;
		} else if(outputs.length == 0) {
			output = createOutput(item, item);
		} else {
			output = outputs[0];
		}
		TilesProcessItemOutputImpl r = (TilesProcessItemOutputImpl)output;
		r.setReference(item.getReference());
		updateOutput(r);
	}

	private XModelObject createOutput(XModelObject item, XModelObject r) {
		XModelObject output = item.getModel().createModelObject(ENT_PROCESS_ITEM_OUTPUT, null);
		output.setAttributeValue(ATT_PATH, r.getAttributeValue("extends")); //$NON-NLS-1$
		String name = XModelObjectUtil.createNewChildName("output", item); //$NON-NLS-1$
		output.setAttributeValue(ATT_NAME, name);
		item.addChild(output);
		return output;
	}
	
	private void updateOutput(TilesProcessItemOutputImpl output) {
///		if(output.isUpToDate()) return;
		output.notifyUpdate();
		XModelObject r = output.getReference();		
		String path = r.getAttributeValue("extends"); //$NON-NLS-1$
		output.setAttributeValue(ATT_PATH, path);
		String title = path;
		output.setAttributeValue("title", title); //$NON-NLS-1$
		XModelObject g = findItem(path);
		String target = (g == null) ? "" : g.getPathPart(); //$NON-NLS-1$
		output.setAttributeValue(ATT_TARGET, target);
	}
	
	public XModelObject findItem(String path) {
		XModelObject g = (XModelObject)items.get(path);
		if(g == null) g = (XModelObject)targets.get(path);
		return g;
	}
	
	public void updateTargets() {
		if(isUpdateLocked()) return;
		addUpdateLock(this);
		try {
			updateTargets0();
		} finally {
			removeUpdateLock(this);
		}
	}

	private void updateTargets0() {
		Map ds = TilesDefinitionSet.getInstance(process.getModel()).getDefinitions();
		XModelObject[] ts = (XModelObject[])targets.values().toArray(new XModelObject[0]);
		for (int i = 0; i < ts.length; i++) {
			boolean b = ds.containsKey(ts[i].getAttributeValue(ATT_NAME));
			ts[i].setAttributeValue("confirmed", (b) ? "true" : "false"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	}
	
	public boolean isDisposed() {
		return process == null || !process.isActive();
	}

	
}

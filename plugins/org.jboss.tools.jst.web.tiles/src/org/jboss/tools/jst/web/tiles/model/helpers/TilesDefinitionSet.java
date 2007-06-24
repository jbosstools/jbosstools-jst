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
import org.jboss.tools.common.model.event.*;
import org.jboss.tools.common.model.plugin.ModelPlugin;
import org.jboss.tools.common.model.util.ModelFeatureFactory;
import org.jboss.tools.jst.web.tiles.model.TilesConstants;

public class TilesDefinitionSet implements XModelTreeListener {
	static String[] CONTRIBUTORS = new String[]{
		"org.jboss.tools.struts.model.helpers.TilesContributor"
	};
	static ITilesDefinitionSetContributor[] contributors;
	
	static {
		loadContributors();
	}
	
    static void loadContributors() {
    	ArrayList<ITilesDefinitionSetContributor> list = new ArrayList<ITilesDefinitionSetContributor>();
    	for (int i = 0; i < CONTRIBUTORS.length; i++) {
    		try {
    			Object watcher = ModelFeatureFactory.getInstance().createFeatureInstance(CONTRIBUTORS[i]);
    			if(watcher instanceof ITilesDefinitionSetContributor)
    				list.add((ITilesDefinitionSetContributor)watcher);
    			else
					if(ModelPlugin.isDebugEnabled()) {			
						ModelPlugin.log("Class is not implemented IWatcherContributor interface!");
					}
    		} catch (Exception e) {
    			ModelPlugin.log(e);
    		}
    	}
    	contributors = list.toArray(new ITilesDefinitionSetContributor[0]);
    }

	public static synchronized TilesDefinitionSet getInstance(XModel model) {
    	TilesDefinitionSet instance = (TilesDefinitionSet)model.getManager("TilesDefinitionSet");
        if (instance == null) {
        	instance = new TilesDefinitionSet();
        	instance.setModel(model);
        	model.addManager("TilesDefinitionSet", instance);
        	model.addModelTreeListener(instance);
        }
        return instance;
    }

	XModel model;
	Set tiles = new HashSet();
	Map definitions = new HashMap();
	Set<ITilesDefinitionSetListener> listeners = new HashSet<ITilesDefinitionSetListener>();
	
	void setModel(XModel model) {
		this.model = model;
		update();
	}
	
	public Map getDefinitions() {
		return definitions;
	}
	
	public void addTilesDefinitionSetListener(ITilesDefinitionSetListener listener) {
		listeners.add(listener);
	}

	public void removeTilesDefinitionSetListener(ITilesDefinitionSetListener listener) {
		listeners.remove(listener);
	}

	public void nodeChanged(XModelTreeEvent event) {
		processEvent(event);
	}

	public void structureChanged(XModelTreeEvent event) {
		processEvent(event);
	}
	
	private void processEvent(XModelTreeEvent event) {
		boolean b = false;
		for (int i = 0; !b && i < contributors.length; i++) {
			if(contributors[i].isRelevant(event)) b = true;
		}
		if(b) update();
	}
	
	public void update() {
		Set s = new HashSet();
		for (int i = 0; i < contributors.length; i++) {
			s.addAll(contributors[i].getTileFiles(model));
		}
		tiles = s;
		updateDefinitions();
	}
	
	void updateDefinitions() {
		Map d = new HashMap();
		Iterator it = tiles.iterator();
		while(it.hasNext()) {
			XModelObject o = (XModelObject)it.next();
			XModelObject[] cs = o.getChildren(TilesConstants.ENT_DEFINITION);
			for (int i = 0; i < cs.length; i++) d.put(cs[i].getAttributeValue("name"), cs[i]);
		}
		Map old = definitions;
		definitions = d;
		Set removed = new HashSet();
		Set added = new HashSet();
		it = old.keySet().iterator();
		while(it.hasNext()) {
			String key = it.next().toString();
			if(!d.containsKey(key)) removed.add(old.get(key));
		}
		it = d.keySet().iterator();
		while(it.hasNext()) {
			String key = it.next().toString();
			if(!old.containsKey(key)) added.add(d.get(key));
		}
		if(!removed.isEmpty() || !added.isEmpty()) {
			ITilesDefinitionSetListener[] ls = listeners.toArray(new ITilesDefinitionSetListener[0]);
			for (int i = 0; i < ls.length; i++) {
				try {
					ls[i].definitionsChanged(removed, added);
				} catch (Exception e) {
					ModelPlugin.log(e);
				}
			}
		}
	}

}

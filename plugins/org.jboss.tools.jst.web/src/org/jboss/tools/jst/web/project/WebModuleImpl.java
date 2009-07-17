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
package org.jboss.tools.jst.web.project;

import java.util.*;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.impl.*;

public class WebModuleImpl extends RegularObjectImpl implements WebModuleConstants {
	private static final long serialVersionUID = 1L;

	String lastURI = null;
	long lastURITimeStamp = -1;
	
    public static String toWebModulePathPart(String name) {
        return (name == null) ? null : name.replace('/', '#');
    }
    
    public void setURI(String uri) {
    	if(uri.equals(getURI()) && isNormalized()) return;
    	
   		StringTokenizer st = new StringTokenizer(uri, ","); //$NON-NLS-1$
    	XModelObject[] cs = getChildren();
    	Map<String,XModelObject> map = new HashMap<String,XModelObject>();
    	for (int i = 0; i < cs.length; i++) map.put(cs[i].getPathPart(), cs[i]);
    	Set<String> set = new HashSet<String>();
    	String t = st.nextToken().trim();
    	set.add(t);
    	String to = getAttributeValue(ATTR_URI);
    	String po = getAttributeValue(ATTR_MODEL_PATH);
    	if(!t.equals(to)) {
			setAttributeValue(ATTR_URI, t);
			XModelObject c = map.get(t.replace('/', '#'));
			if(c != null) {
				setAttributeValue(ATTR_MODEL_PATH, c.getAttributeValue(ATTR_MODEL_PATH));
			} else {
				setAttributeValue(ATTR_MODEL_PATH, guessModelPath(t));
			}
    	} else {
    		to = null;
    	}
    	while(st.hasMoreTokens()) {
    		t = st.nextToken().trim();
    		if(set.contains(t)) continue;
    		set.add(t);
    		String p = t.replace('/', '#');
			XModelObject c = map.remove(p);
    		if(c == null) {
    			c = getModel().createModelObject(ENTITY_WEB_CONFIG, null);
    			c.setAttributeValue(ATTR_NAME, getAttributeValue(ATTR_NAME));
    			c.setAttributeValue(ATTR_URI, t);
				if(to != null && to.equals(t)) {
					c.setAttributeValue(ATTR_MODEL_PATH, po);
				} else {
					c.setAttributeValue(ATTR_MODEL_PATH, guessModelPath(t));
				}
    			addChild(c);
    		}
    	}
    	Iterator<XModelObject> it = map.values().iterator();
    	while(it.hasNext()) {
    		XModelObject o = it.next();
    		o.removeFromParent();
    		o.setModified(true);
    	}
    	lastURI = uri;
    	lastURITimeStamp = getTimeStamp();
    }
    
    public String getURI() {
    	if(lastURI != null && lastURITimeStamp == getTimeStamp()) return lastURI;
		String uri = getAttributeValue(ATTR_URI);
		XModelObject[] cs = getChildren();
		for (int i = 0; i < cs.length; i++) {
			uri += "," + cs[i].getAttributeValue(ATTR_URI); //$NON-NLS-1$
		}
		return uri;    	
    }
    
    public boolean isNormalized() {
    	return getAttributeValue(ATTR_URI).indexOf(',') < 0;
    }
    
    public void normalize() {
    	if(!isNormalized()) setURI(getURI()); 
    }

	private String guessModelPath(String uri) {
		if(!uri.startsWith("/")) uri = "/" + uri; //$NON-NLS-1$ //$NON-NLS-2$
		if(!uri.startsWith("/WEB-INF/")) return ""; //$NON-NLS-1$ //$NON-NLS-2$
		String path = uri.substring(8);
		XModelObject o = getModel().getByPath(path);
		return (o != null) ? path : ""; //$NON-NLS-1$
	}
}


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
package org.jboss.tools.jst.web.browser;

import java.util.*;

import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.util.*;

public class BrowserHistory {
	IBrowserContext context; 
	int size = 5;
	ArrayList<String> items = new ArrayList<String>();
	Map<String,XModelObjectCache> map = new HashMap<String,XModelObjectCache>();
	Set<String> justUrls = new HashSet<String>();
	
	public BrowserHistory(IBrowserContext context) {
		this.context = context;
	}
	
	public void add(XModelObject o, String url) {
		validate();
		if(o == null || url == null) return;
		if(items.indexOf(url) >= 0) items.remove(url);
		items.add(0, url);
		map.put(url, new XModelObjectCache(o));
		while(items.size() > size) {
			Object s = items.remove(size);
			if(s != null) map.remove(s);			
		} 
	}
	
	public void add(String url) {
		if(items.indexOf(url) >= 0) items.remove(url);
		items.add(0, url);
		justUrls.add(url);
		map.remove(url);
	}
	
	public boolean isJustUrl(String url) {
		return justUrls.contains(url);
	}
	
	public XModelObject getRunObject(String url) {
		XModelObjectCache c = (XModelObjectCache)map.get(url);
		return (c == null) ? null : c.getObject();
	}
	
	public String[] items() {
		validate();
		if(items.size() < 1) return new String[0];
		ArrayList<String> l = new ArrayList<String>();
		for (int i = 0; i < items.size(); i++) {
			String url = items.get(i).toString();
			if(!url.equals(context.getLastRunURL())) l.add(url);
		}
		return l.toArray(new String[0]);
	}

	public String[] getAllItems() {
		validate();
		return items.toArray(new String[0]);
	}

	void validate() {
		Set<String> urls = new HashSet<String>();
		for (int i = items.size() - 1; i >= 0; i--) {
			String s = items.get(i).toString();
			if(isJustUrl(s)) continue;
			XModelObject o = getRunObject(s);
			String p = context.computeURL(o);
			if(p == null || urls.contains(p) || p.startsWith("%server%")) { //$NON-NLS-1$
				items.remove(i);
				map.remove(s);
			} else if(!s.equals(p)) {
				items.set(i, p);
				map.put(p, map.remove(s));
			}
			urls.add(p);
		}
	}

}

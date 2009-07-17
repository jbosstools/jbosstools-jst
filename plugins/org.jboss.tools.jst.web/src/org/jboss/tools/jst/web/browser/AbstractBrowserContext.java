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

import java.text.MessageFormat;
import org.jboss.tools.common.meta.action.impl.handlers.DefaultCreateHandler;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelException;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;

public abstract class AbstractBrowserContext implements IBrowserContext {
	IPathSource[] sources = new IPathSource[0]; 
	
	BrowserHistory history = new BrowserHistory(this);
	public XModelObject lastRunObject = null;
	long lastRunObjectTimeStamp = -1;
	protected String url = null;
	public String lastRunUrl = null;
	public Object monitor = new Object();
	
	public void addPathSource(IPathSource s) {
		IPathSource[] ss = new IPathSource[sources.length + 1];
		System.arraycopy(sources, 0, ss, 0, sources.length);
		ss[sources.length] = s;
		sources = ss;
	}

	public abstract String getModelActionPath();

	protected boolean inited = false;
	public void init() {
	}

	public void execute(XModelObject object) throws XModelException {
		setLastRunObject(object);
		if(url == null) throw new XModelException(MessageFormat.format(WebUIMessages.AbstractBrowserContext_CannotOpen, DefaultCreateHandler.title(object, false)));
		doExecute(lastRunUrl);
		if(lastRunUrl.equals(url)) {
			history.add(lastRunObject, url);
		} else {
			history.add(lastRunUrl);
		}
	}
	
	protected abstract void doExecute(String lastRunUrl) throws XModelException;
	
	boolean isLastRunObjectUpToDate(XModelObject o) {
		if(o == null) return (lastRunObject == null);
		if(url == null) return false;
		String prefix = getBrowserPrefix(o.getModel());
		if(prefix == null) return false;
		if(!(url.startsWith(prefix) && url.equals(lastRunUrl))) return false;
		if(o != lastRunObject) return false;
		long ts = (o == null) ? -1 : o.getTimeStamp();
		if(lastRunObjectTimeStamp != ts) return false;
		return true;
	}

	public void setLastRunObject(XModelObject o) {
		if(isLastRunObjectUpToDate(o)) return;
		lastRunObject = o;
		lastRunObjectTimeStamp = (o == null) ? -1 : o.getTimeStamp();
		if(o == null) {
			url = null;
			if(lastRunUrl != null && !history.isJustUrl(lastRunUrl)) {
				lastRunUrl = null;
			}
		} else updateRunPath();
		synchronized (monitor) {
			monitor.notifyAll();
		}
	}
    
	public String getLastRunURL() {
		return lastRunUrl;
	}

	public String[] getHistory() {
		return history.items();
	}
	
	private void updateRunPath() {
		lastRunUrl = url = computeURL(lastRunObject);
	}
	
	public String computeURL(XModelObject o) {
		if(o == null || !o.isActive()) return null;
		if(!inited) {
			init();
		}
		String u = null;
		for (int i = 0; i < sources.length && u == null; i++) {
			u = sources[i].computeURL(o);
		}
		return u;
	}
	
	public void activateJustUrl(String url) {
		if(url == null || url.length() == 0) return;
		lastRunUrl = url;
		history.add(url);
	}
	
	public XModelObject activateItem(String url) {
		history.validate();
		XModelObject o = history.getRunObject(url);
		if(o == null) return null;
		setLastRunObject(o);
		return lastRunObject;
	}
	
	public boolean isJustUrl(String url) {
		return url != null && history.isJustUrl(url);
	}
	
	public abstract void runJustUrl();
	
	public XModelObject activateModel(XModel model) {
		String[] items = history.getAllItems();
		for (int i = 0; i < items.length; i++) {
			XModelObject o = history.getRunObject(items[i]);
			if(o != null && o.getModel() == model) {
				activateItem(items[i]);
				return o;
			}
		}
		XModelObject o = model.getRoot();
		setLastRunObject(o);
		return o;
	}
	
	public void revalidate() {
		lastRunObjectTimeStamp = -2;
		setLastRunObject(lastRunObject);
	}

}

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
package org.jboss.tools.jst.web.model.helpers;

import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.filesystems.impl.FileAnyImpl;
import org.jboss.tools.common.model.plugin.ModelPlugin;
import org.jboss.tools.common.model.util.XModelObjectUtil;
import org.jboss.tools.jst.web.WebModelPlugin;
import org.jboss.tools.jst.web.model.WebProcess;

public class WebProcessStructureHelper {
	static String ELM_PROCESS = "process";
	static String ATT_PATH = "path";
	
	public boolean isProcessLoaded(XModelObject process) {
		if(!(process instanceof WebProcess)) {
			if(ModelPlugin.isDebugEnabled()) {			
				WebModelPlugin.getPluginLog().logInfo("wrong argument in isProcessLoaded");
			}
			return false;
		}
		return ((WebProcess)process).isPrepared();
	}

	public XModelObject getParentFile(XModelObject object) {
		while(object != null && object.getFileType() == XModelObject.NONE) object = object.getParent();
		return (object == null || object.getFileType() != XModelObject.FILE) ? null : object;
	}

	public XModelObject getProcess(XModelObject object) {
		XModelObject file = getParentFile(object);
		return (file == null) ? null : file.getChildByPath(ELM_PROCESS);
	}

	public XModelObject getParentProcess(XModelObject element) {
		return getProcess(element);
	}

	public boolean isIncorrect(XModelObject config) {
		return (config == null || "yes".equals(config.getAttributeValue("isIncorrect")));
	}    

	public String getConfigAsText(XModelObject config) {
		FileAnyImpl impl = (FileAnyImpl)config;
		return impl.getAsText(); 
	}

	public boolean hasPageHiddenLinks(XModelObject page) {
		XModelObject[] child = page.getChildren();
		for (int i = 0; i < child.length; i++)
		  if(child[i].getAttributeValue("hidden").equals("yes")) return true;
		return false;
	}

	public boolean isShortcut(XModelObject itemOutput) {
		return "yes".equals(itemOutput.getAttributeValue("shortcut"));
	}

	public boolean isPageConfirmed(XModelObject itemPage) {
		return "true".equals(itemPage.get("confirmed"));
	}

	public boolean areProcessCommentsHidden(XModelObject process) {
		return "yes".equals(process.getAttributeValue("hide comments"));
	}
    
	public void showComments(XModelObject o) {
		XModelObject process = getParentProcess(o);
		if(process == null) return;
		if(areProcessCommentsHidden(process)) 
			process.setAttributeValue("hide comments", "no");    	
	}
    
	public String[] asStringArray(String s) {
		return XModelObjectUtil.asStringArray(s);
	}

	public String[] asStringArray(XModelObject o, String attr) {
		return asStringArray(o.getAttributeValue(attr));
	}

	public int[] asIntArray(XModelObject o, String attr) {
		String[] s = asStringArray(o, attr);
		int[] result = new int[s.length];
		for (int i = 0; i < s.length; i++) {
			try {
				result[i] = Integer.parseInt(s[i]);
			} catch (Exception e) {
				result[i] = 0;
			}
		}
		return result;
	}

	public String toStringValue(String[] list) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list.length; i++) {
			if(list[i] == null) continue;
			String v = list[i].trim();
			if(v.length() == 0) continue;
			if(sb.length() > 0) sb.append(",");
			sb.append(v);
		}
		return sb.toString();
	}

	public String toStringValue(int[] list) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list.length; i++) {
			if(sb.length() > 0) sb.append(",");
			sb.append("" + list[i]);
		}
		return sb.toString();
	}

	public void setAttributeValue(XModelObject object, String name, String value) {
		String v = object.getAttributeValue(name);
		if(v == null || v.equals(value)) return;
		object.setAttributeValue(name, value);
		v = object.getAttributeValue(name);
		if(v == null || !v.equals(value)) return;
		object.setModified(true);
	}

	public void autolayout(XModelObject process) {
		if(process != null) ((WebProcess)process).autolayout();
	}

	public boolean isNodeChangeListenerLocked(XModelObject process) {
		return "true".equals(process.get("isNodeChangeListenerLocked"));  
	}
    
	public void setNodeChangeListenerLock(XModelObject process, boolean b) {
		process.set("isNodeChangeListenerLocked", (b) ? "true" : "");
	}

	public String getPageTitle(XModelObject page) {
		String path = page.getAttributeValue(ATT_PATH);
		return (path != null && path.startsWith("//")) ? path.substring(1) : path;
	}

}

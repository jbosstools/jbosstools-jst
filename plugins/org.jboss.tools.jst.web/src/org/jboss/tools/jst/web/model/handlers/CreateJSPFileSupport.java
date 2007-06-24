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
package org.jboss.tools.jst.web.model.handlers;

import java.io.File;
import java.util.*;
import org.jboss.tools.common.meta.action.impl.WizardDataValidator;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.files.handlers.*;
import org.jboss.tools.common.model.options.PreferenceModelUtilities;
import org.jboss.tools.jst.web.WebModelPlugin;

public class CreateJSPFileSupport extends CreateFileSupport {
	protected Map<String,File> templates = new TreeMap<String,File>();
	TaglibSet taglibs;
	
	public void reset() {
		taglibs = null;
		selectedTaglibs.clear();
		if(getEntityData().length > 1) {
			resetTaglibs();
		}
		super.reset();
	}

	public String[] getActionNames(int stepId) {
		if(stepId == 0) {
			if(getEntityData().length == 1 || taglibs == null) return super.getActionNames(stepId);
			return new String[]{NEXT, FINISH, CANCEL, HELP};
		}
		return new String[]{BACK, FINISH, CANCEL, HELP};
	}

	public void action(String name) throws Exception {
		if(NEXT.equals(name)) {
			initSelectedTaglibs();
			setStepId(1);
		} else if(BACK.equals(name)) {
			saveSelectedTaglibs();
			setStepId(0);
		} else {
			super.action(name);
		}
	}

	public String[] getPageTemplateList() {
		templates.clear();
		File templateDir = new File(getPageTemplatesLocation());
		if (!templateDir.isDirectory()) return new String[0];
		getTemplates(templateDir);
		String nature = getTarget().getModel().getProperties().getProperty("nature");
		if(nature == null) {
		} else if(nature.indexOf("jsf") >= 0) {
			getTemplates(templateDir, "jsf"); 
		}  else if(nature.indexOf("struts") >= 0) {
			getTemplates(templateDir, "struts");
		}
		return (String[])templates.keySet().toArray(new String[0]); 
	}
	
	private static final String NewJSFProjectPath = "%Options%/JSF Studio/Project/New Project";
	private static final String NewStrutsProjectPath = "%Options%/Struts Studio/Project/New Project";
	protected String getDefaultPageTemplate() {
		String defaultPageTemplate = null;
		String nature = getTarget().getModel().getProperties().getProperty("nature");
		try {
			if(nature.indexOf("jsf") >= 0) {
				XModelObject obj = PreferenceModelUtilities.getPreferenceModel().getByPath(NewJSFProjectPath);
				defaultPageTemplate = obj.getAttributeValue("Page Template");
			}  else if(nature.indexOf("struts") >= 0) {
				XModelObject obj = PreferenceModelUtilities.getPreferenceModel().getByPath(NewStrutsProjectPath);
				defaultPageTemplate = obj.getAttributeValue("Page Template");
			}
		} catch (Exception x) {
			//ignore
		}
		return defaultPageTemplate;
	}

	protected File findTemplate(String template) {
		return (File)templates.get(template);
	}
	
	//// templates 
	public String getPageTemplatesLocation() {
		return getTemplatesBase() + "/pages";
	}
	
	private void getTemplates(File parent, String name) {
		File dir = new File(parent, name);
		if(dir.isDirectory()) getTemplates(dir);
	}

	private void getTemplates(File dir) {
		String ext = action.getProperty("extension");
		File[] files = dir.listFiles();
		if(files == null) return;
		for (int i = 0; i < files.length; i++) {
			if(!files[i].isFile()) continue;
			String n = files[i].getName();
			if(ext != null && !n.endsWith("." + ext)) {
				if(!ext.equals("jsp") || n.indexOf('.') >= 0) continue;
			}
			templates.put(n, files[i]);
		}
	}

	private String getTemplatesBase() {
		String root = WebModelPlugin.getTemplateStateLocation();
		return root + "templates";
	}
	
	public WizardDataValidator getValidator(int step) {
		if(step == 1) {
			defaultValidator.setSupport(this, step);
			return defaultValidator;
		} else return super.getValidator(step);
	}

	/// taglibs
	
	private Map<String,String> selectedTaglibs = new HashMap<String,String>();
	
	private void saveSelectedTaglibs() {
		String template = getAttributeValue(0, "template");
		if(template == null) return;
		String taglibs = getAttributeValue(1, "taglibs");
		if(taglibs != null) selectedTaglibs.put(template.trim(), taglibs);
	}
	
	private void resetTaglibs() {
		String extension = action.getProperty("extension");
		if("jsp".equals(extension)) {
			taglibs = new TaglibSet();
		} else if("xhtml".equals(extension)) {
			taglibs = new TaglibSetXHTML();
		} else {
			return;
		}
		taglibs.initTaglibDescriptions(getTarget().getModel());
		String[] s = taglibs.getDescriptions();
		setValueList(1, "taglibs", s);
	}
	
	void initSelectedTaglibs() throws Exception {
		String template = getAttributeValue(0, "template");
		String value = template == null ? null : (String)selectedTaglibs.get(template.trim());
		if(value != null && template.trim().length() > 0) {
			setAttributeValue(1, "taglibs", value);
			return;
		}
		String body = getTemplateBody();
		Set existing = taglibs.getTaglibsFromTemplate(body);
		StringBuffer sb = new StringBuffer();
		Iterator it = existing.iterator();
		while(it.hasNext()) {
			String s = it.next().toString();
			if(sb.length() > 0) sb.append(";");
			sb.append(taglibs.getTaglibDescription(s));
		}
		value = sb.toString();
		setAttributeValue(1, "taglibs", value);
	}

	protected String modifyBody(String body) {
		if(getEntityData().length < 2 || taglibs == null) return body;
		String ts = getAttributeValue(1, "taglibs");
		if(ts.length() == 0 && getStepId() == 0) return body;
		String[] selected = toArray(ts);
		return taglibs.modifyBody(body, selected);
	}
	
	String[] toArray(String s) {
		StringTokenizer st = new StringTokenizer(s, ";,");
		String[] a = new String[st.countTokens()];
		for (int i = 0; i < a.length; i++) a[i] = st.nextToken();
		return a;		
	}
	
}

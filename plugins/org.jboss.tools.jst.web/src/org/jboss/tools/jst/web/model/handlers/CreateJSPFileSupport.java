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
import java.io.IOException;
import java.util.*;
import org.jboss.tools.common.meta.action.impl.WizardDataValidator;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelException;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.files.handlers.*;
import org.jboss.tools.common.model.options.PreferenceModelUtilities;
import org.jboss.tools.common.model.util.ClassLoaderUtil;
import org.jboss.tools.jst.web.WebModelPlugin;
import org.jboss.tools.jst.web.model.helpers.WebAppHelper;
import org.jboss.tools.jst.web.project.WebProject;
import org.jboss.tools.jst.web.tld.TaglibMapping;

public class CreateJSPFileSupport extends CreateFileSupport {
	static {
		ClassLoaderUtil.init();
	}
	
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

	public void action(String name) throws XModelException {
		if(NEXT.equals(name)) {
			try {
				initSelectedTaglibs();
			} catch (IOException e) {
				throw new XModelException(e);
			}
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
		String nature = getTarget().getModel().getProperties().getProperty("nature"); //$NON-NLS-1$
		if(nature == null) {
		} else if(nature.indexOf("jsf") >= 0) { //$NON-NLS-1$
			getTemplates(templateDir, "jsf");  //$NON-NLS-1$
		}  else if(nature.indexOf("struts") >= 0) { //$NON-NLS-1$
			getTemplates(templateDir, "struts"); //$NON-NLS-1$
		}
		return (String[])templates.keySet().toArray(new String[0]); 
	}
	
	private static final String NewJSFProjectPath = "%Options%/JSF Studio/Project/New Project"; //$NON-NLS-1$
	private static final String NewStrutsProjectPath = "%Options%/Struts Studio/Project/New Project"; //$NON-NLS-1$
	protected String getDefaultPageTemplate() {
		String defaultPageTemplate = null;
		String nature = getTarget().getModel().getProperties().getProperty("nature"); //$NON-NLS-1$
		if(nature == null) {
			return null;
		}
		String prefPath = (nature.indexOf("jsf") >= 0) ? NewJSFProjectPath : //$NON-NLS-1$
			(nature.indexOf("struts") >= 0) ? NewStrutsProjectPath : //$NON-NLS-1$
			null;
		if(prefPath == null) return null;
		XModelObject pref = PreferenceModelUtilities.getPreferenceModel().getByPath(prefPath);
		if(pref == null) {
			WebModelPlugin.getPluginLog().logError("Cannot find preference object " + prefPath, new Exception()); //$NON-NLS-1$
			return null;
		}
		defaultPageTemplate = pref.getAttributeValue("Page Template"); //$NON-NLS-1$
		return defaultPageTemplate;
	}

	protected File findTemplate(String template) {
		return (File)templates.get(template);
	}
	
	//// templates 
	public String getPageTemplatesLocation() {
		return getTemplatesBase() + "/pages"; //$NON-NLS-1$
	}
	
	private void getTemplates(File parent, String name) {
		File dir = new File(parent, name);
		if(dir.isDirectory()) getTemplates(dir);
	}

	private void getTemplates(File dir) {
		String ext = action.getProperty("extension"); //$NON-NLS-1$
		File[] files = dir.listFiles();
		if(files == null) return;
		for (int i = 0; i < files.length; i++) {
			if(!files[i].isFile()) continue;
			String n = files[i].getName();
			if(ext != null && !n.endsWith("." + ext)) { //$NON-NLS-1$
				if(!ext.equals("jsp") || n.indexOf('.') >= 0) continue; //$NON-NLS-1$
			}
			templates.put(n, files[i]);
		}
	}

	private String getTemplatesBase() {
		String root = WebModelPlugin.getTemplateStateLocation();
		return root + "templates"; //$NON-NLS-1$
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
		String template = getAttributeValue(0, "template"); //$NON-NLS-1$
		if(template == null) return;
		String taglibs = getAttributeValue(1, "taglibs"); //$NON-NLS-1$
		if(taglibs != null) selectedTaglibs.put(template.trim(), taglibs);
	}
	
	private void resetTaglibs() {
		String extension = action.getProperty("extension"); //$NON-NLS-1$
		if("jsp".equals(extension)) { //$NON-NLS-1$
			taglibs = new TaglibSet();
		} else if("xhtml".equals(extension)) { //$NON-NLS-1$
			taglibs = new TaglibSetXHTML();
		} else {
			return;
		}
		XModel model = getTarget().getModel();
		XModelObject web = WebAppHelper.getWebApp(model);
		if(web != null) {
			TaglibMapping m = WebProject.getInstance(model).getTaglibMapping();
			if(m != null) m.revalidate(web);
		}		
		
		taglibs.initTaglibDescriptions(model);
		String[] s = taglibs.getDescriptions();
		setValueList(1, "taglibs", s); //$NON-NLS-1$
	}
	
	void initSelectedTaglibs() throws IOException {
		String template = getAttributeValue(0, "template"); //$NON-NLS-1$
		String value = template == null ? null : (String)selectedTaglibs.get(template.trim());
		if(value != null && template.trim().length() > 0) {
			setAttributeValue(1, "taglibs", value); //$NON-NLS-1$
			return;
		}
		String body = getTemplateBody();
		Set<String> existing = taglibs.getTaglibsFromTemplate(body);
		StringBuffer sb = new StringBuffer();
		Iterator<String> it = existing.iterator();
		while(it.hasNext()) {
			String s = it.next().toString();
			if(sb.length() > 0) sb.append(";"); //$NON-NLS-1$
			sb.append(taglibs.getTaglibDescription(s));
		}
		value = sb.toString();
		setAttributeValue(1, "taglibs", value); //$NON-NLS-1$
	}

	protected String modifyBody(String body) throws IOException {
		if(getEntityData().length < 2 || taglibs == null) return body;
		if(getStepId() == 0) initSelectedTaglibs();
		String ts = getAttributeValue(1, "taglibs"); //$NON-NLS-1$
		String[] selected = toArray(ts);
		return taglibs.modifyBody(body, selected);
	}
	
	String[] toArray(String s) {
		if(s == null || s.length() == 0) return new String[0];
		StringTokenizer st = new StringTokenizer(s, ";,"); //$NON-NLS-1$
		String[] a = new String[st.countTokens()];
		for (int i = 0; i < a.length; i++) a[i] = st.nextToken();
		return a;		
	}
	
}

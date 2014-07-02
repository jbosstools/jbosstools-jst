/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.palette.html.jquery.wizard;

import java.io.File;

import org.jboss.tools.common.model.options.SharableConstants;
import org.jboss.tools.common.model.ui.editors.dnd.IDropCommand;
import org.jboss.tools.common.model.ui.editors.dnd.IElementGenerator.ElementNode;
import org.jboss.tools.common.model.ui.internal.editors.PaletteItemResult;
import org.jboss.tools.jst.web.kb.internal.taglib.html.jq.JQueryMobileVersion;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.JSPTextEditor;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.dnd.MobilePaletteInsertHelper;
import org.jboss.tools.jst.web.ui.internal.preferences.js.PreferredJSLibVersions;
import org.jboss.tools.jst.web.WebModelPlugin;
import org.jboss.tools.jst.web.ui.palette.html.wizard.AbstractNewHTMLWidgetWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.wizard.VersionedNewHTMLWidgetWizard;
import org.jboss.tools.jst.web.ui.palette.model.PaletteModel;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public abstract class NewJQueryWidgetWizard<P extends NewJQueryWidgetWizardPage> extends VersionedNewHTMLWidgetWizard<JQueryMobileVersion,P> implements JQueryConstants {
	private JQueryVersionPage versionPage;
	
	PreferredJSLibVersions preferredVersions = null;

	public NewJQueryWidgetWizard() {
		super(JQueryMobileVersion.getLatestDefaultVersion());
	}

	@Override
	public void initWithoutUI() {
		addPages();
		if(page != null) {
			page.createFields();
		}
		versionPage.createFields();
	}

	protected void doAddPages() {
		super.doAddPages();
		versionPage = new JQueryVersionPage("jQueryVersion", "Add References to JS/CSS");
		addPage(versionPage);
	}

	public PreferredJSLibVersions getPreferredVersions() {
		return preferredVersions;
	}

	@Override
	public void setCommand(IDropCommand command) {
		super.setCommand(command);
		
		String path = getCommandProperties().getProperty(SharableConstants.PALETTE_PATH);
		if(path != null) {
			for (JQueryMobileVersion v: JQueryMobileVersion.ALL_VERSIONS) {
				if(path.indexOf(PaletteModel.VERSION_PREFIX + v.toString()) > 0) {
					version = v;
				}
			}
		}

		preferredVersions =new PreferredJSLibVersions(getFile(), getVersion());
		preferredVersions.updateLibEnablementAndSelection();		
	}

	protected boolean isMini() {
		return isTrue(EDITOR_ID_MINI);
	}

	protected boolean isLayoutHorizontal() {
		return LAYOUT_HORIZONTAL.equals(page.getEditorValue(EDITOR_ID_LAYOUT));
	}

	@Override
	protected void doPerformFinish() {
		preferredVersions.applyLibPreference(versionPage);
		preferredVersions.saveLibPreference();
		if(page == null || isTrue(AbstractNewHTMLWidgetWizardPage.ADD_JS_CSS_SETTING_NAME)) {
			getCommandProperties().put(MobilePaletteInsertHelper.PROPOPERTY_JQUERY_MOBILE_INSERT_JS_CSS, getVersion());
		}
		super.doPerformFinish();
	}

	protected ElementNode getPageContentNode(ElementNode parent) {
		ElementNode page = parent.addChild(TAG_DIV);
		page.addAttribute(ATTR_DATA_ROLE, ROLE_PAGE);
		page.addAttribute(ATTR_ID, "jbt");
		ElementNode content = page.addChild(TAG_DIV);
		content.addAttribute(ATTR_DATA_ROLE, ROLE_CONTENT);
		return content;
	}

	@Override
	protected void createHead(ElementNode html) {
		String browserType = page.getBrowserType();
		ResourceConstants c = ("mozilla".equals(browserType) || this instanceof NewDialogWizard) ? 
				new ResourceConstants130() : new ResourceConstants130();
		if(version == JQueryMobileVersion.JQM_1_4) {
			c = new ResourceConstants140();
		}
		
		String styleSheetURI = c.getCSSPath();
		String jQueryScriptURI = c.getScriptPath();
		String jQueryMobileScriptURI = c.getMobileScriptPath();
		
		File root = WebModelPlugin.getJSStateRoot(); //new File("/home/slava/Downloads/jquery.mobile-1.2.0.zip"); 

		if(root != null) {
			String prefix = "js/";
			if(root.isDirectory()) {
				styleSheetURI = new File(root, prefix + c.getCSSName()).toURI().toString();
				jQueryScriptURI = new File(root, prefix + c.getScriptName()).toURI().toString();
				jQueryMobileScriptURI = new File(root, prefix + c.getMobileScriptName()).toURI().toString();
			} else if(root.isFile()) {
				String jar = "jar:" + root.toURI().toString() + "!/";
				styleSheetURI = jar + prefix + c.getCSSName();
				jQueryScriptURI = jar + prefix + c.getScriptName();
				jQueryMobileScriptURI = jar + prefix + c.getMobileScriptName();
			}
		}

		ElementNode head = html.addChild(TAG_HEAD);
		if("mozilla".equals(browserType)) {
			StringBuilder sb = new StringBuilder();
			sb.append("\n(function() {\n")
			  .append("  var originalGetComputedStyle = window.getComputedStyle;\n")
			  .append("  window.getComputedStyle = function() {\n")
			  .append("    if (arguments.length == 1) {\n")
			  .append("      // workaround for https://bugzilla.mozilla.org/show_bug.cgi?id=567350 (getComputedStyle requires both arguments to be supplied)\n")
			  .append("      return originalGetComputedStyle.call(this, arguments[0], null);\n")
			  .append("    } else {\n")
			  .append("      return originalGetComputedStyle.apply(this, arguments);\n")
			  .append("    }\n")
			  .append("  };\n")
			  .append("}());\n");
			head.addChild(TAG_SCRIPT, sb.toString());
		}
		head.addChild(TAG_TITLE, "Page Title");
		ElementNode meta = head.addChild(TAG_META);
		meta.addAttribute(ATTR_NAME, "viewport");
		meta.addAttribute(ATTR_CONTENT, "width=device-width, initial-scale=1");
		ElementNode link = head.addChild(TAG_LINK);
		link.addAttribute(ATTR_REL, "stylesheet");
		link.addAttribute(ATTR_HREF, styleSheetURI);
		ElementNode script = head.addChild(TAG_SCRIPT, "");
		script.addAttribute(ATTR_SRC, jQueryScriptURI);
		script = head.addChild(TAG_SCRIPT, "");
		script.addAttribute(ATTR_SRC, jQueryMobileScriptURI);
	}

	abstract class ResourceConstants {
		public String getSite() {
			return "http://code.jquery.com/";
		}
		public String getMobileSite() {
			return "http://code.jquery.com/mobile/";
		}
		public abstract String getVerionFolder();
		public abstract String getCSSName();
		public abstract String getScriptName();
		public abstract String getMobileScriptName();
		
		public String getCSSPath() {
			return getMobileSite() + getVerionFolder() + getCSSName();
		}
			
		public String getScriptPath() {
			return getSite() + getScriptName();
		}
			
		public String getMobileScriptPath() {
			return getMobileSite() + getVerionFolder() + getMobileScriptName();
		}			

	}

	class ResourceConstants120 extends ResourceConstants {
		@Override
		public String getVerionFolder() {
			return "1.2.0/";
		}
		@Override
		public String getCSSName() {
			return "jquery.mobile-1.2.0.min.css";
		}
		@Override
		public String getScriptName() {
			return "jquery-1.8.2.min.js";
		}
		@Override
		public String getMobileScriptName() {
			return "jquery.mobile-1.2.0.min.js";
		}
	}

	class ResourceConstants121 extends ResourceConstants {
		@Override
		public String getVerionFolder() {
			return "1.2.1/";
		}
		@Override
		public String getCSSName() {
			return "jquery.mobile-1.2.1.css";
		}
		@Override
		public String getScriptName() {
			return "jquery-1.8.3.min.js";
		}
		@Override
		public String getMobileScriptName() {
			return "jquery.mobile-1.2.1.js";
		}
	}

	class ResourceConstants130 extends ResourceConstants {
		@Override
		public String getVerionFolder() {
			return "1.3.0-rc.1/";
		}
		@Override
		public String getCSSName() {
			return "jquery.mobile-1.3.0-rc.1.min.css";
		}
		@Override
		public String getScriptName() {
			return "jquery-1.9.0.min.js";
		}
		@Override
		public String getMobileScriptName() {
			return "jquery.mobile-1.3.0-rc.1.min.js";
		}
	}

	class ResourceConstants140 extends ResourceConstants {
		@Override
		public String getVerionFolder() {
			return "1.4.0-rc.1/";
		}
		@Override
		public String getCSSName() {
			return "jquery.mobile-1.4.0-rc.1.min.css";
		}
		@Override
		public String getScriptName() {
			return "jquery-1.10.2.min.js";
		}
		@Override
		public String getMobileScriptName() {
			return "jquery.mobile-1.4.0-rc.1.min.js";
		}
	}

	protected class SearchCapability {
		protected String searchInputID = null;
		protected ElementNode form;

		public SearchCapability(ElementNode parent, String filterInputPrefix) {
			boolean is13 = getVersion() == JQueryMobileVersion.JQM_1_3;
			if(!is13 && isTrue(EDITOR_ID_SEARCH_FILTER)) {
				form = parent.addChild(TAG_FORM);
				ElementNode input = form.addChild(TAG_INPUT);
				input.addAttribute(ATTR_DATA_TYPE, TYPE_SEARCH);
				int k = generateIndex(filterInputPrefix, "", 1);
				searchInputID = filterInputPrefix + k;
				input.addAttribute(ATTR_ID, searchInputID);
			}			
		}

		public void addClassFilterable() {
			if(form != null) {
				form.addAttribute(ATTR_CLASS, CLASS_UI_FILTERABLE);
			}
		}

		public void addDataFilter(ElementNode filterable) {
			if(isTrue(EDITOR_ID_SEARCH_FILTER)) {
				filterable.addAttribute(ATTR_DATA_FILTER, TRUE);
				if(searchInputID != null) {
					filterable.addAttribute(ATTR_DATA_INPUT, "#" + searchInputID);
				}
			}			
		}
		
	}

	/**
	 * Helper method that returns results generated 
	 * by palette item wizard with default settings
	 * for jQuery Mobile category.
	 * 
	 * @param textEditor
	 * @param version
	 * @param item
	 * @return
	 */
	public static PaletteItemResult runWithoutUi(JSPTextEditor textEditor, JQueryMobileVersion version, String item) {
		return runWithoutUi(textEditor, JQueryConstants.JQM_CATEGORY, version.toString(), item);
	}
}

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
import org.jboss.tools.common.model.ui.editors.dnd.IElementGenerator.NodeWriter;
import org.jboss.tools.jst.web.kb.internal.taglib.html.jq.JQueryMobileVersion;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.dnd.MobilePaletteInsertHelper;
import org.jboss.tools.jst.web.WebModelPlugin;
import org.jboss.tools.jst.web.ui.palette.html.wizard.AbstractNewHTMLWidgetWizard;
import org.jboss.tools.jst.web.ui.palette.html.wizard.AbstractNewHTMLWidgetWizardPage;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public abstract class NewJQueryWidgetWizard<P extends NewJQueryWidgetWizardPage> extends AbstractNewHTMLWidgetWizard implements JQueryConstants {
	protected P page;

	protected JQueryMobileVersion version = JQueryMobileVersion.JQM_1_4;

	public NewJQueryWidgetWizard() {
	}

	@Override
	public void setCommand(IDropCommand command) {
		super.setCommand(command);
		String path = getCommandProperties().getProperty(SharableConstants.PALETTE_PATH);
		if(path != null) {
			for (JQueryMobileVersion v: JQueryMobileVersion.ALL_VERSIONS) {
				if(path.indexOf("version:" + v.toString()) > 0) {
					version = v;
				}
			}
		}
	}

	public JQueryMobileVersion getVersion() {
		return version;
	}

	protected void doAddPages() {
		page = createPage();
		addPage(page);
	}

	protected abstract P createPage(); 

	protected boolean isTrue(String editorID) {
		return TRUE.equals(page.getEditorValue(editorID));
	}

	protected boolean isMini() {
		return isTrue(EDITOR_ID_MINI);
	}

	protected String getID(String prefix) {
		if(!page.isIDEnabled()) {
			return null;
		}
		String id = page.getEditorValue(EDITOR_ID_ID);
		if(id.length() == 0) {
			int i = generateIndex(prefix, "", 1);
			id = prefix + i;
		}
		return id;
	}

	protected String addID(String prefix, ElementNode node) {
		String id = getID(prefix);
		if(id != null) {
			node.addAttribute(ATTR_ID, id);
		}
		return id;
	}

	protected boolean isLayoutHorizontal() {
		return LAYOUT_HORIZONTAL.equals(page.getEditorValue(EDITOR_ID_LAYOUT));
	}

	protected static void addClass(StringBuilder cls, String add) {
		if(cls.length() > 0) {
			cls.append(" ");
		}
		cls.append(add);
	}

	@Override
	protected void doPerformFinish() {
		if(isTrue(AbstractNewHTMLWidgetWizardPage.ADD_JS_CSS_SETTING_NAME)) {
			getCommandProperties().setProperty(MobilePaletteInsertHelper.PROPOPERTY_JQUERY_MOBILE_INSERT_JS_CSS, TRUE);
		}
		super.doPerformFinish();
	}

	@Override
	public String getTextForBrowser() {
		ElementNode html = new ElementNode(TAG_HTML, false);
		createHead(html);
		ElementNode body = html.addChild(TAG_BODY);
		createBodyForBrowser(body);

		NodeWriter w = new NodeWriter(false);
		html.flush(w, 0);

		StringBuilder sb = new StringBuilder();
		sb.append(DOCTYPE).append("\n").append(w.getText());

		return sb.toString();
	}

	/**
	 * Override to wrap content.
	 * @param body
	 */
	protected void createBodyForBrowser(ElementNode body) {
		addContent(body);
	}

	protected ElementNode getPageContentNode(ElementNode parent) {
		ElementNode page = parent.addChild(TAG_DIV);
		page.addAttribute(ATTR_DATA_ROLE, ROLE_PAGE);
		page.addAttribute(ATTR_ID, "jbt");
		ElementNode content = page.addChild(TAG_DIV);
		content.addAttribute(ATTR_DATA_ROLE, ROLE_CONTENT);
		return content;
	}

	protected ElementNode getFormNode(ElementNode parent) {
		ElementNode form = parent.addChild(TAG_FORM);
		form.addAttribute(ATTR_ACTION, "#");
		form.addAttribute(ATTR_METHOD, METHOD_GET);
		return form;
	}
	
	private void createHead(ElementNode html) {
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

	protected void addAttributeIfNotEmpty(ElementNode n, String attrName, String editorID) {
		String value = page.getEditorValue(editorID);
		if(value != null && value.length() > 0) {
			n.addAttribute(attrName, value);
		}
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
}

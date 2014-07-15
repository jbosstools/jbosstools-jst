/******************************************************************************* 
 * Copyright (c) 2014 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard;

import java.io.File;

import org.jboss.tools.common.model.options.SharableConstants;
import org.jboss.tools.common.model.ui.editors.dnd.IDropCommand;
import org.jboss.tools.common.model.ui.editors.dnd.IElementGenerator.ElementNode;
import org.jboss.tools.jst.angularjs.AngularJsPlugin;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.dnd.MobilePaletteInsertHelper;
import org.jboss.tools.jst.web.ui.internal.preferences.js.PreferredJSLibVersions;
import org.jboss.tools.jst.web.ui.palette.html.wizard.AbstractNewHTMLWidgetWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.wizard.VersionedNewHTMLWidgetWizard;
import org.jboss.tools.jst.web.ui.palette.model.PaletteModel;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public abstract class NewIonicWidgetWizard<P extends NewIonicWidgetWizardPage> extends VersionedNewHTMLWidgetWizard<IonicVersion,P> implements IonicConstants {
	private IonicVersionPage versionPage;
	
	PreferredJSLibVersions preferredVersions = null;

	public NewIonicWidgetWizard() {
		super(IonicVersion.IONIC_1_0);
	}

	@Override
	public void initWithoutUI() {
		addPages();
		if(page != null) {
			page.createFields();
		}
		versionPage.createFields();
	}

	@Override
	protected void doAddPages() {
		super.doAddPages();
		versionPage = new IonicVersionPage("ionicVersion", "Add References to JS/CSS");
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
			for (IonicVersion v: IonicVersion.ALL_VERSIONS) {
				if(path.indexOf(PaletteModel.VERSION_PREFIX + v.toString()) > 0) {
					version = v;
				}
			}
		}

		preferredVersions = new PreferredJSLibVersions(getFile(), getVersion());
		preferredVersions.updateLibEnablementAndSelection();		
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

	@Override
	protected void createHead(ElementNode html) {
		String browserType = page.getBrowserType();
		
		String styleSheetURI = "css/ionic.min.css";
		String ionicScriptURI = "ionic.bundle.min.js";
		
		File root = AngularJsPlugin.getJSStateRoot();

		if(root != null) {
			String prefix = "js/";
			if(root.isDirectory()) {
				styleSheetURI = new File(root, prefix + styleSheetURI).toURI().toString();
				ionicScriptURI = new File(root, prefix + ionicScriptURI).toURI().toString();
			} else if(root.isFile()) {
				String jar = "jar:" + root.toURI().toString() + "!/";
				styleSheetURI = jar + prefix + styleSheetURI;
				ionicScriptURI = jar + prefix + ionicScriptURI;
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
		script.addAttribute(ATTR_SRC, ionicScriptURI);

		StringBuilder sb = new StringBuilder();
		sb.append("angular.module('starter', ['ionic'])\n")
		  .append(".run(function($ionicPlatform) {\n")
		  .append("  $ionicPlatform.ready(function() {\n")
		  .append("   if(window.StatusBar) {\n")
		  .append("    StatusBar.styleDefault();\n")
		  .append("   }\n")
		  .append("  });\n")
		  .append("})\n");
		head.addChild(TAG_SCRIPT, sb.toString());
	}

	@Override
	protected void createBodyForBrowser(ElementNode body) {
		body.addAttribute("ng-app", "starter");
		addContent(body, true);
	}

	@Override
	protected final void addContent(ElementNode parent) {
		addContent(parent, false);
	}

	protected void addContent(ElementNode parent, boolean browser) {
		
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
//	public static PaletteItemResult runWithoutUi(JSPTextEditor textEditor, JQueryMobileVersion version, String item) {
//		return runWithoutUi(textEditor, JQueryConstants.JQM_CATEGORY, version.toString(), item);
//	}
}

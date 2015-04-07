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
package org.jboss.tools.jst.angularjs.internal.ionic.palette.model;

import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.AddJSCSSREferencesWizard;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewContentWizard;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewFooterBarWizard;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewHeaderBarWizard;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewListWizard;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewNavigationWizard;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewRefresherWizard;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewScrollWizard;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewSideMenuWizard;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewSlideboxWizard;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewSpinnerWizard;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewTabWizard;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewTabsWizard;
import org.jboss.tools.jst.angularjs.internal.ui.AngularJsUIImages;
import org.jboss.tools.jst.web.ui.JSTWebUIImages;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.dnd.MobilePaletteInsertHelper;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.AbstractPaletteCategory;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.LibraryPaletteItemImpl;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.PaletteItemImpl;

public class IonicPageCategory extends AbstractPaletteCategory {
	public IonicPageCategory() {
		PaletteItemImpl item = new LibraryPaletteItemImpl("JS/CSS", // label
				"Add references to <b>Ionic</b> JS and CSS to &lt;head>", // tooltip
				JSTWebUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/JS-CSS.png"), // image path
				AddJSCSSREferencesWizard.class, // wizard class
				MobilePaletteInsertHelper.INSERT_JS_CSS_SIGNATURE // start text
		);
		add(item);
		item = new PaletteItemImpl(
				"Content", // label
				"<html>\n<b>Content:</b><br>\n&lt;ion-content>\n<br>\n\t...<br>\n&lt;/ion-content>\n</html>", // tooltip
				"content ", // keywords
				AngularJsUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/Content.png"), // image path
				NewContentWizard.class, // wizard class
				null // corrector
		);
		add(item);
		item = new PaletteItemImpl(
				"Refresher", // label
				"<html>\n<b>Refresher:</b><br>\n&lt;ion-refresher>\n<br>\n\t...<br>\n&lt;/ion-refresher>\n</html>", // tooltip
				"refresher ", // keywords
				AngularJsUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/IonicRefresher.png"), // image path
				NewRefresherWizard.class, // wizard class
				null // corrector
		);
		add(item);
		item = new PaletteItemImpl(
				"Navigation", // label
				"<html>\n<b>Refresher:</b><br>\n&lt;ion-nav-bar>\n<br>\n\t...<br>\n&lt;/ion-nav-bar>\n</html>", // tooltip
				"nav bar view ", // keywords
				AngularJsUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/IonicNavigation.png"), // image path
				NewNavigationWizard.class, // wizard class
				null // corrector
		);
		add(item);
		item = new PaletteItemImpl(
				"Scroll", // label
				"<html>\n<b>Scroll:</b><br>\n&lt;ion-scroll>\n<br>\n\t...<br>\n&lt;/ion-scroll>\n</html>", // tooltip
				"scroll ", // keywords
				AngularJsUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/Scroll.png"), // image path
				NewScrollWizard.class, // wizard
				null // corrector
		);
		add(item);
		item = new PaletteItemImpl(
				"Header Bar", // label
				"<html>\n<b>Header Bar:</b><br>\n&lt;ion-header-bar class=\"bar-positive\"><br>\n...<br>\n&lt;/ion-header-bar>\n</html>", // tooltip
				"header bar ", // keywords
				AngularJsUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/IonicHeaderBar.png"), // image path
				NewHeaderBarWizard.class, // wizard class
				null // corrector
		);
		add(item);
		item = new PaletteItemImpl(
				"Footer Bar", // label
				"<html>\n<b>Footer Bar:</b><br>\n&lt;ion-footer-bar class=\"bar-positive\"><br>\n...<br>\n&lt;/ion-footer-bar>\n</html>", // tooltip
				"footer bar ", // keywords
				AngularJsUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/IonicFooterBar.png"), // image path
				NewFooterBarWizard.class, // wizard class
				null // corrector
		);
		add(item);
		item = new PaletteItemImpl(
				"Tabs", // label
				"<html>\n<b>Tabs:</b><br>\n&lt;ion-tabs><br>\n...<br>\n&lt;/ion-tabs>\n</html>", // tooltip
				"tab tabs ", // keywords
				AngularJsUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/IonicTabs.png"), // image path
				NewTabsWizard.class, // wizard class
				null // corrector
		);
		add(item);
		item = new PaletteItemImpl(
				"Tab", // label
				"<html>\n<b>Tab:</b><br>\n&lt;ion-tab class=\"bar-positive\"><br>\n...<br>\n&lt;/ion-tab>\n</html>", // tooltip
				"tab tabs ", // keywords
				AngularJsUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/IonicTab.png"), // image path
				NewTabWizard.class, // wizard class
				null // corrector
		);
		add(item);
		item = new PaletteItemImpl(
				"Slidebox", // label
				"<html>\n<b>Slidebox:</b><br>\n&lt;ion-slide-box class=\"bar-positive\"><br>\n...<br>\n&lt;/ion-slide-box>\n</html>", // tooltip
				"slide box ", // keywords
				AngularJsUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/SlideBox.png"), // image path
				NewSlideboxWizard.class, // wizard class
				null // corrector
		);
		add(item);
		item = new PaletteItemImpl(
				"Side Menu", // label
				"<html>\n<b>Side Menu:</b><br>\n&lt;ion-side-menus><br>\n...<br>\n&lt;/ion-side-menus>\n</html>", // tooltip
				"side menu ", // keywords
				AngularJsUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/SideMenu.png"), // image path
				NewSideMenuWizard.class, // wizard class
				null // corrector
		);
		add(item);
		item = new PaletteItemImpl(
				"List", // label
				"<html>\n<b>List:</b><br>\n&lt;ion-list><br>\n...<br>\n&lt;/ion-list>\n</html>", // tooltip
				"list ", // keywords
				AngularJsUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/List.png"), // image path
				NewListWizard.class, // wizard class
				null // corrector
		);
		add(item);


		item = new PaletteItemImpl(
				"Spinner", // label
				"<html>\n<b>Spinner:</b><br>\n&lt;ion-spinner><br>\n...<br>\n&lt;/ion-spinner>\n</html>", // tooltip
				"text input button ", // keywords
				AngularJsUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/IonicSpinner.png"), // image path
				NewSpinnerWizard.class, // wizard class
				null // corrector
		);
		add(item);
	}
}

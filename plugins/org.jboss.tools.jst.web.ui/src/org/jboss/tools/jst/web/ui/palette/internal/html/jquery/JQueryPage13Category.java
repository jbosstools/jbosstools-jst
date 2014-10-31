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
package org.jboss.tools.jst.web.ui.palette.internal.html.jquery;

import org.jboss.tools.jst.web.ui.JSTWebUIImages;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.dnd.MobilePaletteInsertHelper;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.AddJSCSSREferencesWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewCollapsibleSetWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewCollapsibleWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewDialogWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewGridWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewPagePositionCorrector;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewPageWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewPanelPositionCorrector;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewPanelWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewPopupWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewTableWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewWidgetPositionCorrector;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.AbstractPaletteCategory;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.LibraryPaletteItemImpl;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.PaletteItemImpl;
/**
 * 
 * @author Daniel Azarov
 *
 */
public class JQueryPage13Category extends AbstractPaletteCategory {
	protected PaletteItemImpl FIELD_CONTAINER;

	public JQueryPage13Category() {
		PaletteItemImpl item = new LibraryPaletteItemImpl(
				"JS/CSS", // label
				"Add references to <b>jQuery</b>, <b>jQuery Mobile</b> JS and CSS to &lt;head>", // tooltip
				JSTWebUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/JS-CSS.png"), // image path
				AddJSCSSREferencesWizard.class, // wizard class
				MobilePaletteInsertHelper.INSERT_JS_CSS_SIGNATURE // start text
		);
		add(item);
		item = new PaletteItemImpl(
				"Page", // label
				"<html>\n<b>Page:</b><br>\n&lt;div data-role=\"page\">\n<br>\n\t...<br>\n&lt;/div>\n</html>", // tooltip
				"page ", // keywords
				JSTWebUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/Page.png"), // image path
				NewPageWizard.class, // wizard class
				NewPagePositionCorrector.class // corrector
		);
		add(item);
		item = new PaletteItemImpl(
				"Dialog", // label
				"<html>\n<b>Dialog:</b><br>\n&lt;div data-role=\"dialog\"><br>\n\t...<br>\n&lt;/div>\n</html>", // tooltip
				"dialog ", // keywords
				JSTWebUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/Dialog.png"), // image path
				NewDialogWizard.class, // wizard class
				NewPagePositionCorrector.class // corrector
		);
		add(item);
		item = new PaletteItemImpl(
				"Popup", // label
				"<html>\n<b>Popup:</b><br>\n&lt;a href=\"#popupBasic\" data-rel=\"popup\">...&lt;/a><br>\n&lt;div data-role=\"popup\" id=\"popupBasic\"><br>\n\t...<br>\n&lt;/div>\n</html>", // tooltip
				"div popup ", // keywords
				JSTWebUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/Popup.png"), // image path
				NewPopupWizard.class, // wizard class
				NewWidgetPositionCorrector.class // corrector
		);
		add(item);
		item = new PaletteItemImpl(
				"Grid", // label
				"<html>\n<b>Grid:</b><br>\n&lt;div class=\"ui-grid-b\"><br>\n  ...<br>\n&lt;/div>\n</html>", // tooltip
				"div grid ", // keywords
				JSTWebUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/Grid.png"), // image path
				NewGridWizard.class, // wizard class
				NewWidgetPositionCorrector.class // corrector
		);
		add(item);
		FIELD_CONTAINER = new PaletteItemImpl(
				"Field Container", // label
				"<html>\n<b>Field Container:</b><br>\n&lt;div data-role=\"fieldcontain\"><br>\n...<br>\n&lt;/div>\n</html>", // tooltip
				"Field Container", // keywords
				JSTWebUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/FieldContainer.png"), // image path
				null, // wizard
				null, // corrector
				"<div data-role=\"fieldcontain\">", // start text
				"</div>"  // end text
		);
		add(FIELD_CONTAINER);
		item = new PaletteItemImpl(
				"Panel", // label
				"<html>\n<b>Panel:</b><br>\n&lt;div data-role=\"panel\"><br>\n...<br>\n&lt;/div>\n</html>", // tooltip
				"div panel ", // keywords
				JSTWebUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/Panel.png"), // image path
				NewPanelWizard.class, // wizard class
				NewPanelPositionCorrector.class // corrector
		);
		add(item);
		item = new PaletteItemImpl(
				"Table", // label
				"<html>\n<b>Table:</b><br>\n&lt;table data-role=\"table\"><br>\n...<br>\n&lt;/table>\n</html>", // tooltip
				"table ", // keywords
				JSTWebUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/Table.png"), // image path
				NewTableWizard.class, // wizard class
				NewWidgetPositionCorrector.class // corrector
		);
		add(item);
		item = new PaletteItemImpl(
				"Collapsible", // label
				"<html>\n<b>Collapsible:</b><br>\n&lt;div data-role=\"collapsible\"><br>\n...<br>\n&lt;/div>\n</html>", // tooltip
				"fieldset collapsible content block ", // keywords
				JSTWebUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/Collapsible.png"), // image path
				NewCollapsibleWizard.class, // wizard class
				NewWidgetPositionCorrector.class // corrector
		);
		add(item);
		item = new PaletteItemImpl(
				"Collapsible Set", // label
				"<html>\n<b>Collapsible Set:</b><br>\n&lt;div data-role=\"collapsible-set\"><br>\n...<br>\n&lt;/div>\n</html>", // tooltip
				"div collapsible set ", // keywords
				JSTWebUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/CollapsibleSet.png"), // image path
				NewCollapsibleSetWizard.class, // wizard class
				NewWidgetPositionCorrector.class // corrector
		);
		add(item);
	}

}

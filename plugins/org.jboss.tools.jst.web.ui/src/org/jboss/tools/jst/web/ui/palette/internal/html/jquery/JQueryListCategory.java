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
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewFooterPositionCorrector;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewFooterWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewHeaderBarWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewHeaderPositionCorrector;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewListviewWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewNavbarWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewSelectMenuWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewWidgetPositionCorrector;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.AbstractPaletteCategory;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.PaletteItemImpl;
/**
 * 
 * @author Daniel Azarov
 *
 */
public class JQueryListCategory extends AbstractPaletteCategory {

	public JQueryListCategory() {
		PaletteItemImpl item = new PaletteItemImpl(
				"Header Bar", // label
				"<html>\n<b>Header Bar:</b><br>\n&lt;div data-role=\"header\"><br>\n...<br>\n&lt;/div>\n</html>", // tooltip
				"div header bar ", // keywords
				JSTWebUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/Header.png"), // image path
				NewHeaderBarWizard.class, // wizard class
				NewHeaderPositionCorrector.class // corrector
		);
		add(item);
		item = new PaletteItemImpl(
				"Footer Bar", // label
				"<html>\n<b>Footer Bar:</b><br>\n&lt;div data-role=\"footer\"><br>\n...<br>\n&lt;/div>\n</html>", // tooltip
				"div footer bar ", // keywords
				JSTWebUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/Footer.png"), // image path
				NewFooterWizard.class, // wizard class
				NewFooterPositionCorrector.class // corrector
		);
		add(item);
		item = new PaletteItemImpl(
				"Navbar", // label
				"<html>\n<b>Navbar:</b><br>\n&lt;div data-role=\"navbar\"><br>\n...<br>\n&lt;/div>\n</html>", // tooltip
				"div navbar bar ", // keywords
				JSTWebUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/Navbar.png"), // image path
				NewNavbarWizard.class, // wizard class
				NewWidgetPositionCorrector.class // corrector
		);
		add(item);
		item = new PaletteItemImpl(
				"Select", // label
				"<html>\n<b>Select menu:</b><br>\n&lt;label for=\"select-choice-0\" class=\"select\">...&lt;/label><br>\n&lt;select name=\"select-choice-0\"><br>\n...<br>\n&lt;/select>\n</html>", // tooltip
				"div select menu ", // keywords
				JSTWebUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/Select.png"), // image path
				NewSelectMenuWizard.class, // wizard class
				NewWidgetPositionCorrector.class // corrector
		);
		add(item);
		item = new PaletteItemImpl(
				"Listview", // label
				"<html>\n<b>Listview:</b><br>\n&lt;ul data-role=\"listview\"><br>\n...<br>\n&lt;/ul>\n</html>", // tooltip
				"ol ul listview view ", // keywords
				JSTWebUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/Listview.png"), // image path
				NewListviewWizard.class, // wizard class
				NewWidgetPositionCorrector.class // corrector
		);
		add(item);
	}
}

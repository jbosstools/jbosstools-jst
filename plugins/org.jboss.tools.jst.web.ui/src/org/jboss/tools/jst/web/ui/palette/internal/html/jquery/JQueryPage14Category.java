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
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewHeadingWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewTabsWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewWidgetPositionCorrector;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.PaletteItemImpl;
/**
 * 
 * @author Daniel Azarov
 *
 */
public class JQueryPage14Category extends JQueryPage13Category {

	public JQueryPage14Category() {
		super();
		remove(FIELD_CONTAINER);
		PaletteItemImpl item = new PaletteItemImpl(
				"Tabs", // label
				"<html><b>Tabs:</b><br>\n&lt;div data-role=\"tabs\"><br>"+
				"&lt;div data-role=\"navbar\"><br>"+
				"<b>...</b><br>"+
				"&lt;/div><br>"+
				"<b>...</b><br>&lt;/div></html>", // tooltip
				"div tabs ", // keywords
				JSTWebUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/Tabs.png"), // image path
				NewTabsWizard.class, // wizard class
				NewWidgetPositionCorrector.class // corrector
		);
		add(item);
		item = new PaletteItemImpl(
				"Heading", // label
				"<html>\n<b>Heading:</b><br>\n&lt;h3 ><br>\n...<br>\n&lt;/h3>\n</html>", // tooltip
				"div heading ", // keywords
				JSTWebUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/Heading.png"), // image path
				NewHeadingWizard.class, // wizard class
				NewWidgetPositionCorrector.class // corrector
		);
		add(item);
	}
}

/******************************************************************************* 
 * Copyright (c) 2015 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.palette.internal.html.html5;

import org.jboss.tools.jst.web.ui.JSTWebUIImages;
import org.jboss.tools.jst.web.ui.palette.internal.html.html5.wizard.NewMenuWizard;
import org.jboss.tools.jst.web.ui.palette.internal.html.html5.wizard.NewMenuitemWizard;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.AbstractPaletteCategory;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.PaletteItemImpl;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class HTML5MenuCategory extends AbstractPaletteCategory {

	public HTML5MenuCategory() {
		PaletteItemImpl item = new PaletteItemImpl(
				"Menu", // label
				"<html>\n<b>Menu:</b><br>\n&lt;menu>...&lt;/menu>\n</html>", // tooltip
				"menu ", // keywords
				JSTWebUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/Menu.png"), // image path
				NewMenuWizard.class, // wizard class
				null // corrector
		);
		add(item);
		item = new PaletteItemImpl(
				"Menuitem", // label
				"<html>\n<b>Menuitem:</b><br>\n&lt;menuitem type=\"command\">...&lt;/menuitem>\n</html>", // tooltip
				"menu item ", // keywords
				JSTWebUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/Menuitem.png"), // image path
				NewMenuitemWizard.class, // wizard class
				null // corrector
		);
		add(item);
	}
}

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
package org.jboss.tools.jst.web.ui.palette.internal.html.html5;

import org.jboss.tools.jst.web.ui.JSTWebUIImages;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewFormWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewLabelWizard;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.AbstractPaletteCategory;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.PaletteItemImpl;
/**
 * 
 * @author Daniel Azarov
 *
 */
public class HTML5FormCategory extends AbstractPaletteCategory {

	public HTML5FormCategory() {
		PaletteItemImpl item = new PaletteItemImpl(
				"Form", // label
				"<html>\n<b>Form:</b><br>\n&lt;form><br>\n...<br>\n&lt;/form>\n</html>", // tooltip
				"Form", // keywords
				JSTWebUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/Form.png"), // image path
				NewFormWizard.class, // wizard
				null // corrector
		);
		add(item);
		item = new PaletteItemImpl(
				"Label", // label
				"<html>\n<b>Label:</b><br>\n&lt;label><br>\n...<br>\n&lt;/label>\n</html>", // tooltip
				"Label", // keywords
				JSTWebUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/Label.png"), // image path
				NewLabelWizard.class, // wizard
				null // corrector
		);
		add(item);
	}
}

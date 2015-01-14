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
import org.jboss.tools.jst.web.ui.palette.internal.html.html5.wizard.NewButtonWizard;
import org.jboss.tools.jst.web.ui.palette.internal.html.html5.wizard.NewDatalistWizard;
import org.jboss.tools.jst.web.ui.palette.internal.html.html5.wizard.NewListWizard;
import org.jboss.tools.jst.web.ui.palette.internal.html.html5.wizard.NewTableWizard;
import org.jboss.tools.jst.web.ui.palette.internal.html.html5.wizard.NewTextInputWizard;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.AbstractPaletteCategory;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.PaletteItemImpl;
/**
 * 
 * @author Daniel Azarov
 *
 */
public class HTML5FormCategory extends AbstractPaletteCategory {

	public HTML5FormCategory() {
		PaletteItemImpl table = new PaletteItemImpl(
				"Table", // label
				"<html>\n<b>Table:</b><br>\n&lt;table data-role=\"table\"><br>\n...<br>\n&lt;/table>\n</html>", // tooltip
				"table ", // keywords
				JSTWebUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/Table.png"), // image path
				NewTableWizard.class, // wizard class
				null // corrector
		);
		add(table);
		PaletteItemImpl item = new PaletteItemImpl(
				"List", // label
				"<html>\n<b>List:</b><br>\n&lt;ol><br>\n...<br>\n&lt;/ol>\n</html>", // tooltip
				"List", // keywords
				JSTWebUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/HTML5List.png"), // image path
				NewListWizard.class, // wizard
				null // corrector
		);
		add(item);
		item = new PaletteItemImpl(
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
		item = new PaletteItemImpl(
				"Datalist", // label
				"<html>\n<b>Datalist:</b><br>\n&lt;datalist><br>\n...<br>\n&lt;/datalist>\n</html>", // tooltip
				"Datalist", // keywords
				JSTWebUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/Datalist.png"), // image path
				NewDatalistWizard.class, // wizard
				null // corrector
		);
		add(item);
		item = new PaletteItemImpl(
				"Text Input", // label
				"<html>\n<b>Text Input:</b><br>\n&lt;input><br>\n...<br>\n&lt;/input>\n</html>", // tooltip
				"text input button ", // keywords
				JSTWebUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/TextInput.png"), // image path
				NewTextInputWizard.class, // wizard class
				null // corrector
		);
		add(item);
		item = new PaletteItemImpl(
				"Form Button", // label
				"<html>\n<b>Button:</b><br>\n&lt;input type=\"submit\" value=\"Submit\">...&lt;/input>\n</html>", // tooltip
				"button input ", // keywords
				JSTWebUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/FormButton.png"), // image path
				NewButtonWizard.class, // wizard class
				null // corrector
		);
		add(item);
	}
}

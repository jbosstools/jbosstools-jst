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

import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewButtonWizard;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewCheckBoxWizard;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewRadioWizard;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewSpinnerWizard;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewTextInputWizard;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewToggleWizard;
import org.jboss.tools.jst.angularjs.internal.ui.AngularJsUIImages;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.AbstractPaletteCategory;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.PaletteItemImpl;

public class IonicFormCategory extends AbstractPaletteCategory {

	public IonicFormCategory() {
		PaletteItemImpl item = new PaletteItemImpl(
				"Checkbox", // label
				"<html>\n<b>Checkbox:</b><br>\n&lt;ion-checkbox><br>\n...<br>\n&lt;/ion-checkbox>\n</html>", // tooltip
				"check box ", // keywords
				AngularJsUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/IonicCheckbox.png"), // image path
				NewCheckBoxWizard.class, // wizard class
				null // corrector
		);
		add(item);
		item = new PaletteItemImpl(
				"Radio", // label
				"<html>\n<b>Radio:</b><br>\n&lt;ion-radio><br>\n...<br>\n&lt;/ion-radio>\n</html>", // tooltip
				"radio button ", // keywords
				AngularJsUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/IonicRadio.png"), // image path
				NewRadioWizard.class, // wizard class
				null // corrector
		);
		add(item);
		item = new PaletteItemImpl(
				"Toggle", // label
				"<html>\n<b>Toggle:</b><br>\n&lt;ion-toggle><br>\n...<br>\n&lt;/ion-toggle>\n</html>", // tooltip
				"flip toggle switch ", // keywords
				AngularJsUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/IonicToggle.png"), // image path
				NewToggleWizard.class, // wizard class
				null // corrector
		);
		add(item);
		item = new PaletteItemImpl(
				"Button", // label
				"<html>\n<b>Button:</b><br>\n&lt;button><br>\n...<br>\n&lt;/button>\n</html>", // tooltip
				"button ", // keywords
				AngularJsUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/IonicButton.png"), // image path
				NewButtonWizard.class, // wizard class
				null // corrector
		);
		add(item);
		item = new PaletteItemImpl(
				"Text Input", // label
				"<html>\n<b>Text Input:</b><br>\n&lt;input><br>\n...<br>\n&lt;/input>\n</html>", // tooltip
				"text input button ", // keywords
				AngularJsUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/IonicTextInput.png"), // image path
				NewTextInputWizard.class, // wizard class
				null // corrector
		);
		add(item);
	}

}

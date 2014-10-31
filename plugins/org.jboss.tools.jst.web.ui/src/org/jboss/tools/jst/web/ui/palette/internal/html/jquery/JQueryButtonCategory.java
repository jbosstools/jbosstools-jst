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
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewButtonWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewCheckBoxWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewFormButtonWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewGroupedButtonsWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewGroupedCheckboxesWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewLinkWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewRadioWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewRangeSliderWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewTextInputWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewToggleWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewWidgetPositionCorrector;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.AbstractPaletteCategory;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.PaletteItemImpl;
/**
 * 
 * @author Daniel Azarov
 *
 */
public class JQueryButtonCategory extends AbstractPaletteCategory {

	public JQueryButtonCategory() {
		PaletteItemImpl item = new PaletteItemImpl(
				"Button", // label
				"<html>\n<b>Button:</b><br>\n&lt;a href=\"...\" data-role=\"button\">...&lt;/a>\n</html>", // tooltip
				"a button ", // keywords
				JSTWebUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/Button.png"), // image path
				NewButtonWizard.class, // wizard class
				NewWidgetPositionCorrector.class // corrector
		);
		add(item);
		item = new PaletteItemImpl(
				"Buttons", // label
				"<html>\n<b>Grouped buttons:</b><br>\n&lt;div data-role=\"controlgroup\"><br>\n...<br>\n&lt;/div>\n</html>", // tooltip
				"div grouped buttons ", // keywords
				JSTWebUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/Buttons.png"), // image path
				NewGroupedButtonsWizard.class, // wizard class
				NewWidgetPositionCorrector.class // corrector
		);
		add(item);
		item = new PaletteItemImpl(
				"Form Button", // label
				"<html>\n<b>Form Button:</b><br>\n&lt;input><br>\n...<br>\n&lt;/input>\n</html>", // tooltip
				"input form button submit ", // keywords
				JSTWebUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/FormButton.png"), // image path
				NewFormButtonWizard.class, // wizard class
				NewWidgetPositionCorrector.class // corrector
		);
		add(item);
		item = new PaletteItemImpl("Link", // label
				"<html>\n<b>Link:</b><br>\n&lt;a href=\"...\">...&lt;/a>\n</html>", // tooltip
				"a link ", // keywords
				JSTWebUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/Link.png"), // image path
				NewLinkWizard.class, // wizard class
				NewWidgetPositionCorrector.class // corrector
		);
		add(item);
		item = new PaletteItemImpl(
				"Toggle", // label
				"<html><b>Flip toggle switch:</b><br>&lt;label for=\"flip-1\">Switch:&lt;/label><br>&lt;input data-role=\"flipswitch\" type=\"checkbox\"/></html>", // tooltip
				"div flip toggle switch input ", // keywords
				JSTWebUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/Toggle.png"), // image path
				NewToggleWizard.class, // wizard class
				NewWidgetPositionCorrector.class // corrector
		);
		add(item);
		item = new PaletteItemImpl(
				"Radio", // label
				"<html>\n<b>Radio button:</b><br>\n&lt;fieldset data-role=\"controlgroup\"><br>\n&#09;&lt;legend>&lt;/legend><br>\n&#09;&lt;input name=\"radio-choice-1\" id=\"radio-choice-1a\" value=\"A\" type=\"radio\"/><br>\n&#09;&lt;label for=\"radio-choice-1a\">...&lt;/label><br>\n&lt;/fieldset>\n</html>", // tooltip
				"input radio button ", // keywords
				JSTWebUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/Radio.png"), // image path
				NewRadioWizard.class, // wizard class
				NewWidgetPositionCorrector.class // corrector
		);
		add(item);
		item = new PaletteItemImpl(
				"Checkbox", // label
				"<html>\n<b>Checkbox:</b><br>\n&lt;label><br>\n&#09;&lt;input type=\"checkbox\" name=\"checkbox-1\"/><br>\n&#09;...<br>\n&lt;/label>\n</html>", // tooltip
				"input checkbox ", // keywords
				JSTWebUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/Checkbox.png"), // image path
				NewCheckBoxWizard.class, // wizard class
				NewWidgetPositionCorrector.class // corrector
		);
		add(item);
		item = new PaletteItemImpl(
				"Checkboxes", // label
				"<html>\n<b>Grouped checkboxes:</b><br>\n&lt;fieldset data-role=\"controlgroup\"><br>\n&#09;&lt;legend>&lt;/legend><br>\n&#09;&lt;input name=\"checkbox-1a\" id=\"checkbox-1a\" type=\"checkbox\"/><br>\n&#09;&lt;label for=\"checkbox-1a\">...&lt;/label><br>\n&lt;/fieldset>\n</html>", // tooltip
				"input grouped checkbox ", // keywords
				JSTWebUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/Checkboxes.png"), // image path
				NewGroupedCheckboxesWizard.class, // wizard class
				NewWidgetPositionCorrector.class // corrector
		);
		add(item);
		item = new PaletteItemImpl(
				"Slider", // label
				"<html>\n<b>Slider/Range slider:</b><br>\n&lt;label for=\"range-1\">...&lt;/label><br>\n&lt;input name=\"range-1\" data-highlight=\"true\" type=\"range\"/>\n</html>", // tooltip
				"input range slider ", // keywords
				JSTWebUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/Slider.png"), // image path
				NewRangeSliderWizard.class, // wizard class
				NewWidgetPositionCorrector.class // corrector
		);
		add(item);
		item = new PaletteItemImpl(
				"Text Input", // label
				"<html><b>Text Input:</b><br>\n&lt;label for=\"text-1\">Input:&lt;/label><br>&lt;input type=\"text\"/></html>", // tooltip
				"input text ", // keywords
				JSTWebUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/TextInput.png"), // image path
				NewTextInputWizard.class, // wizard
				NewWidgetPositionCorrector.class // corrector
		);
		add(item);
	}
}

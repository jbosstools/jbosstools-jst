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
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewAudioWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewImageWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewVideoWizard;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.AbstractPaletteCategory;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.PaletteItemImpl;
/**
 * 
 * @author Daniel Azarov
 *
 */
public class HTML5MultimediaCategory extends AbstractPaletteCategory {

	public HTML5MultimediaCategory() {
		PaletteItemImpl item = new PaletteItemImpl(
				"Image", // label
				"<html>\n<b>Image:</b><br>\n&lt;img alt=\"\" src=\"\"><br>\n</html>", // tooltip
				"img image ", // keywords
				JSTWebUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/Image.png"), // image path
				NewImageWizard.class, // wizard class
				null // corrector
		);
		add(item);
		item = new PaletteItemImpl(
				"Video", // label
				"<html>\n<b>Video:</b><br>\n&lt;video alt=\"\" src=\"\"><br>\n</html>", // tooltip
				"Video", // keywords
				JSTWebUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/Video.png"), // image path
				NewVideoWizard.class, // wizard class
				null // corrector
		);
		add(item);
		item = new PaletteItemImpl(
				"Audio", // label
				"<html>\n<b>Audio:</b><br>\n&lt;audio alt=\"\" src=\"\"><br>\n</html>", // tooltip
				"Audio", // keywords
				JSTWebUIImages.getInstance().getOrCreateImageDescriptor(
						"palette/Audio.png"), // image path
				NewAudioWizard.class, // wizard class
				null // corrector
		);
		add(item);
	}
}

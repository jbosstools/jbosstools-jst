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

import org.eclipse.jface.resource.ImageDescriptor;
import org.jboss.tools.jst.web.kb.internal.HTML5Recognizer;
import org.jboss.tools.jst.web.kb.taglib.ITagLibRecognizer;
import org.jboss.tools.jst.web.ui.JSTWebUIImages;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.AbstractPaletteGroup;
/**
 * 
 * @author Daniel Azarov
 *
 */
public class HTML5PaletteGroup extends AbstractPaletteGroup {
	
	public HTML5PaletteGroup(){
		add(new HTML50VersionGroup());
	}

	@Override
	public String getName() {
		return "HTML";
	}

	@Override
	public ITagLibRecognizer getRecognizer() {
		return new HTML5Recognizer();
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return JSTWebUIImages.getInstance().getOrCreateImageDescriptor(
				"palette/html5.png");
	}

}

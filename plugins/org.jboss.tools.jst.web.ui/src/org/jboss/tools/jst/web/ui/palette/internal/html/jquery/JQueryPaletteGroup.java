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

import org.eclipse.jface.resource.ImageDescriptor;
import org.jboss.tools.jst.web.kb.internal.JQueryMobileRecognizer;
import org.jboss.tools.jst.web.kb.taglib.ITagLibRecognizer;
import org.jboss.tools.jst.web.ui.JSTWebUIImages;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.AbstractPaletteGroup;
/**
 * 
 * @author Daniel Azarov
 *
 */
public class JQueryPaletteGroup extends AbstractPaletteGroup {
	
	public JQueryPaletteGroup(){
		add(new JQuery13VersionGroup());
		add(new JQuery14VersionGroup());
	}

	@Override
	public String getName() {
		return "jQuery Mobile";
	}

	@Override
	public ITagLibRecognizer getRecognizer() {
		return new JQueryMobileRecognizer();
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return JSTWebUIImages.getInstance().getOrCreateImageDescriptor(
				"palette/jqm.png");
	}

}

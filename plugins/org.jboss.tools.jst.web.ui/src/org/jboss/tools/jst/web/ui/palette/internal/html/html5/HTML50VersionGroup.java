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

import org.jboss.tools.jst.web.kb.internal.taglib.html.HTMLVersion;
import org.jboss.tools.jst.web.kb.internal.taglib.html.IHTMLLibraryVersion;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.AbstractPaletteVersionGroup;
/**
 * 
 * @author Daniel Azarov
 *
 */
public class HTML50VersionGroup extends AbstractPaletteVersionGroup {
	
	public HTML50VersionGroup(){
		add(new HTML5FormCategory());
		add(new HTML5MultimediaCategory());
	}

	@Override
	public IHTMLLibraryVersion getVersion() {
		return HTMLVersion.HTML_5_0;
	}
}

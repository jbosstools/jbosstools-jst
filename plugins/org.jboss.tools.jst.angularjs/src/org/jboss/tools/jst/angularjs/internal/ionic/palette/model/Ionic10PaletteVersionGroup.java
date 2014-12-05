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

import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.IonicVersion;
import org.jboss.tools.jst.web.kb.taglib.IHTMLLibraryVersion;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.AbstractPaletteVersionGroup;

public class Ionic10PaletteVersionGroup extends AbstractPaletteVersionGroup {
	
	public Ionic10PaletteVersionGroup(){
		add(new IonicPageCategory());
		add(new IonicFormCategory());
	}

	@Override
	public IHTMLLibraryVersion getVersion() {
		return IonicVersion.IONIC_1_0;
	}

}

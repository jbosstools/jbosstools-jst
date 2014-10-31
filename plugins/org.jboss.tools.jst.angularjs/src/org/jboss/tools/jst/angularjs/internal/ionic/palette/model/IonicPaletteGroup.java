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

import org.eclipse.jface.resource.ImageDescriptor;
import org.jboss.tools.jst.angularjs.internal.ionic.IonicRecognizer;
import org.jboss.tools.jst.angularjs.internal.ui.AngularJsUIImages;
import org.jboss.tools.jst.web.kb.taglib.ITagLibRecognizer;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.AbstractPaletteGroup;

public class IonicPaletteGroup extends AbstractPaletteGroup {

	public IonicPaletteGroup(){
		add(new Ionic10PaletteVersionGroup());
	}

	@Override
	public String getName() {
		return "Ionic";
	}

	@Override
	public ITagLibRecognizer getRecognizer() {
		return new IonicRecognizer();
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return AngularJsUIImages.getInstance().createImageDescriptor("palette/ionic.png");
	}

}

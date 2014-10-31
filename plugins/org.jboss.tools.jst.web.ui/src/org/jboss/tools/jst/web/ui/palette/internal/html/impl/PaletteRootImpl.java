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
package org.jboss.tools.jst.web.ui.palette.internal.html.impl;

import org.eclipse.gef.palette.PaletteRoot;
import org.jboss.tools.jst.web.ui.palette.model.IPaletteModel;
/**
 * 
 * @author Daniel Azarov
 *
 */
public class PaletteRootImpl extends PaletteRoot{
	private IPaletteModel model;
	
	public PaletteRootImpl(IPaletteModel model){
		super();
		this.model = model;
	}
	
	public IPaletteModel getPaletteModel(){
		return model;
	}
	
}

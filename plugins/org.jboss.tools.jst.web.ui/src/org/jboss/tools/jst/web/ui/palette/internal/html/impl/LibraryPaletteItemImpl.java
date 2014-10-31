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

import org.eclipse.jface.resource.ImageDescriptor;
import org.jboss.tools.jst.web.ui.palette.internal.html.ILibraryPaletteItem;
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteItem;
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteItemWizard;

/**
 * Default implementation for Library Palette Item
 * 
 * @see IPaletteItem
 * @see ILibraryPaletteItem
 * 
 * @author Daniel Azarov
 *
 */
public class LibraryPaletteItemImpl extends PaletteItemImpl implements ILibraryPaletteItem{

	public LibraryPaletteItemImpl(String name, String toolTip,	ImageDescriptor imageDescriptor,
			Class<? extends IPaletteItemWizard> wizardClass,
			String startText) {
		super(name, toolTip, "", imageDescriptor, wizardClass,
				null, startText, "");
		
		setReformat(false);
	}

}

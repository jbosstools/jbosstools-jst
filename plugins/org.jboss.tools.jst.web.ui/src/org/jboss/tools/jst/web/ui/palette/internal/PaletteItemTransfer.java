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
package org.jboss.tools.jst.web.ui.palette.internal;

import org.eclipse.gef.dnd.SimpleObjectTransfer;
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteItem;

public class PaletteItemTransfer extends SimpleObjectTransfer {
	public static final String PALETTE_ITEM = "palette/item"; //$NON-NLS-1$
	public static final int PALETTE_ITEM_ID = registerType(PALETTE_ITEM);

	private static PaletteItemTransfer instance = new PaletteItemTransfer();
	
	public static PaletteItemTransfer getInstance() {
		return instance;
	}

	@Override
	protected int[] getTypeIds() {
		return new int[] {PALETTE_ITEM_ID};
	}

	@Override
	protected String[] getTypeNames() {
		return new String[]{PALETTE_ITEM};
	}
	
	public IPaletteItem getPaletteItem(){
		return (IPaletteItem)getObject();
	}

	public void setPaletteItem(IPaletteItem item){
		setObject(item);
	}
}

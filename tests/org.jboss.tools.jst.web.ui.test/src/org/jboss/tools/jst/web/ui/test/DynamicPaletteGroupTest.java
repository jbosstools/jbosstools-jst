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
package org.jboss.tools.jst.web.ui.test;

import java.util.List;

import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.ui.IEditorPart;
import org.jboss.tools.jst.jsp.test.palette.AbstractPaletteEntryTest;
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteCategory;
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteGroup;
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteItem;
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteVersionGroup;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.PaletteItemImpl;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.PaletteModelImpl;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.PaletteTool;

public class DynamicPaletteGroupTest  extends AbstractPaletteEntryTest {
	IEditorPart editor = null;
	public void tearDown() {
		if(editor != null){
			editor.getSite().getPage().closeEditor(editor, false);
		}
	}
	public void testMaxLongCounts(){
		editor = openEditor("empty.html");
		PaletteViewer viewer = getPaletteViewer();
		
		IPaletteItem item = find(viewer.getPaletteRoot(), "Page");
		assertNotNull("Palette Item \"Page\" not found", item);
		
		zeroAllCounts(item);
		
		item.setCountIndex(Long.MAX_VALUE-5);
		PaletteItemImpl.setStaticCountIndex(Long.MAX_VALUE-5);
		item.setNumberOfCalls(Long.MAX_VALUE-5);
		
		for(long count = 0; count < 10; count++){
			item.called();
		}
		
		assertEquals("Count is wromg", 6, item.getCountIndex());
		assertEquals("Number Of Calls is wrong", 6, item.getNumberOfCalls());
	}
	
	private IPaletteItem find(PaletteContainer container, String name){
		List children = container.getChildren();
		for(Object child : children){
			if(!(child instanceof PaletteContainer)){
				if(child instanceof PaletteTool){
					PaletteTool entry = (PaletteTool)child;
					IPaletteItem item = entry.getPaletteItem();
					if(item.getName().equals(name)){
						return item;
					}
				}
			} else {
				IPaletteItem i = find((PaletteContainer)child, name);
				if(i != null){
					return i;
				}
			}
		}
		return null;
	}
	
	private void zeroAllCounts(IPaletteItem baseItem){
		PaletteModelImpl model = (PaletteModelImpl)baseItem.getCategory().getVersionGroup().getGroup().getPaletteModel();
		
		for(String groupName : model.getPaletteGroups()){
			IPaletteGroup group = model.getPaletteGroup(groupName);
			for(IPaletteVersionGroup version: group.getPaletteVersionGroups()){
				for(IPaletteCategory category :version.getCategories()){
					for(IPaletteItem item :category.getItems()){
						item.setCountIndex(0);
						item.setNumberOfCalls(0);
					}
				}
			}
		}
		PaletteItemImpl.setStaticCountIndex(0);
	}
	
}

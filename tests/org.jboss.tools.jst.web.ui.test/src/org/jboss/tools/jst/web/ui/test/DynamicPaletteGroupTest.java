/******************************************************************************* 
 * Copyright (c) 2014 - 2015 Red Hat, Inc. 
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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
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
	IEditorPart editor = null, editor2 = null;
	protected IProject project2 = null;
	
	public void setUp() {
		super.setUp();
		project2 = ResourcesPlugin.getWorkspace().getRoot().getProject("WebProject");
	}
	public void tearDown() {
		if(editor != null){
			editor.getSite().getPage().closeEditor(editor, false);
		}
		if(editor2 != null){
			editor2.getSite().getPage().closeEditor(editor2, false);
		}
	}
	
	public void testMaxLongCounts(){
		editor = openEditor("empty.html");
		PaletteViewer viewer = getPaletteViewer();
		
		IPaletteItem item = find(viewer.getPaletteRoot(), "Page");
		assertNotNull("Palette Item \"Page\" not found", item);
		
		zeroAllCounts(item);
		
		item.setCountIndex(Long.MAX_VALUE-5);
		((PaletteItemImpl)item).setProjectCountIndex(Long.MAX_VALUE-5);
		item.setNumberOfCalls(Long.MAX_VALUE-5);
		
		for(long count = 0; count < 10; count++){
			item.called();
		}
		
		assertEquals("Count index is wromg", 6, item.getCountIndex());
		assertEquals("Number Of Calls is wrong", 6, item.getNumberOfCalls());
	}
	
	public void testClearDynamicGroup(){
		editor = openEditor("empty.html");
		PaletteViewer viewer = getPaletteViewer();
		
		IPaletteItem item = find(viewer.getPaletteRoot(), "Page");
		assertNotNull("Palette Item \"Page\" not found", item);
		
		zeroAllCounts(item);
		
		item.setCountIndex(Long.MAX_VALUE-5);
		((PaletteItemImpl)item).setProjectCountIndex(Long.MAX_VALUE-5);
		item.setNumberOfCalls(Long.MAX_VALUE-5);
		
		PaletteModelImpl model = (PaletteModelImpl)item.getCategory().getVersionGroup().getGroup().getPaletteModel();
		
		model.clearDynamicGroup();
		
		assertEquals("Count index is wromg", 0, item.getCountIndex());
		assertEquals("Number Of Calls is wrong", 0, item.getNumberOfCalls());
		
	}
	
	public void testCountsFromSameProject(){
		editor = openEditor("empty.html");
		PaletteViewer viewer = getPaletteViewer();
		
		editor2 = openEditor("normal.html");
		PaletteViewer viewer2 = getPaletteViewer();
		
		IPaletteItem item = find(viewer.getPaletteRoot(), "Page");
		assertNotNull("Palette Item \"Page\" not found", item);
		
		IPaletteItem item2 = find(viewer2.getPaletteRoot(), "Page");
		assertNotNull("Palette Item \"Page\" not found", item2);
		
		zeroAllCounts(item);
		zeroAllCounts(item2);
		
		for(long count = 0; count < 10; count++){
			item.called();
		}
		
		assertEquals("Count index is wromg", 10, item.getCountIndex());
		assertEquals("Number Of Calls is wrong", 10, item.getNumberOfCalls());
		assertEquals("Count index is wromg", 10, item2.getCountIndex());
		assertEquals("Number Of Calls is wrong", 10, item2.getNumberOfCalls());
	}
	
	public void testCountsFromDifferentProjects(){
		editor = openEditor("empty.html");
		PaletteViewer viewer = getPaletteViewer();
		
		editor2 = openEditor(project2, "/WebContent/index.html");
		PaletteViewer viewer2 = getPaletteViewer();
		
		IPaletteItem item = find(viewer.getPaletteRoot(), "Page");
		assertNotNull("Palette Item \"Page\" not found", item);
		
		IPaletteItem item2 = find(viewer2.getPaletteRoot(), "Page");
		assertNotNull("Palette Item \"Page\" not found", item);
		
		zeroAllCounts(item);
		zeroAllCounts(item2);
		
		for(long count = 0; count < 10; count++){
			item.called();
		}
		
		assertEquals("Count index is wromg", 10, item.getCountIndex());
		assertEquals("Number Of Calls is wrong", 10, item.getNumberOfCalls());
		assertEquals("Count index is wromg", 0, item2.getCountIndex());
		assertEquals("Number Of Calls is wrong", 0, item2.getNumberOfCalls());
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
		model.setProjectCountIndex(0);
	}
	
}

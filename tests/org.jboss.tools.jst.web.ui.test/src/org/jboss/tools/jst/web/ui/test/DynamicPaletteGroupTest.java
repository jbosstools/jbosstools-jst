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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.ui.IEditorPart;
import org.jboss.tools.jst.jsp.test.palette.AbstractPaletteEntryTest;
import org.jboss.tools.jst.web.ui.palette.internal.PaletteSettings;
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteCategory;
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteGroup;
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteItem;
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteVersionGroup;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.HTML5DynamicPaletteGroup;
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
	
	public void testLastUsedSorting(){
		checkSorting(new PaletteTestItem[]{
				new PaletteTestItem("Page",  1045, 14),
				new PaletteTestItem("Dialog", 923, 23),
				new PaletteTestItem("Grid",   854, 36),
				new PaletteTestItem("Panel",  541, 49),
				new PaletteTestItem("Table",  415, 97),
				new PaletteTestItem("Popup",  315, 87),
				new PaletteTestItem("Tabs",   215, 317),
				new PaletteTestItem("Link",   115, 417),
				new PaletteTestItem("Radio",  15, 717),
				new PaletteTestItem("Slider",  1, 317)
			}, PaletteSettings.TYPE_LAST_USED, false);
	}
	
	public void testLastUsedSortingMaxNumbers(){
		checkSorting(new PaletteTestItem[]{
				new PaletteTestItem("Page",    Long.MAX_VALUE-1, 14),
				new PaletteTestItem("Dialog",  Long.MAX_VALUE-2, 23),
				new PaletteTestItem("Grid",    Long.MAX_VALUE-3, 36),
				new PaletteTestItem("Panel",   Long.MAX_VALUE-4, 49),
				new PaletteTestItem("Table",                  0, 97),
				new PaletteTestItem("Popup",   Long.MAX_VALUE-5, 87),
				new PaletteTestItem("Tabs",    Long.MAX_VALUE-6, 317),
				new PaletteTestItem("Link",    Long.MAX_VALUE-7, 417),
				new PaletteTestItem("Radio",   Long.MAX_VALUE-8, 717),
				new PaletteTestItem("Slider",  Long.MAX_VALUE-9, 317)
			}, PaletteSettings.TYPE_LAST_USED, true);
	}

	public void testMostPopularSorting(){
		checkSorting(new PaletteTestItem[]{
				new PaletteTestItem("Page",   12, 1045),
				new PaletteTestItem("Dialog", 26, 923),
				new PaletteTestItem("Grid",   37, 854),
				new PaletteTestItem("Panel",  48, 541),
				new PaletteTestItem("Table",  20, 415),
				new PaletteTestItem("Popup",  35, 387),
				new PaletteTestItem("Tabs",   25, 217),
				new PaletteTestItem("Link",  115, 117)
			}, PaletteSettings.TYPE_MOST_POPULAR, false);
	}
	
	public void testMostPopularSortingMaxNumbers(){
		checkSorting(new PaletteTestItem[]{
				new PaletteTestItem("Page",   12, Long.MAX_VALUE-1),
				new PaletteTestItem("Dialog", 26, Long.MAX_VALUE-2),
				new PaletteTestItem("Grid",   37, Long.MAX_VALUE-3),
				new PaletteTestItem("Panel",  48, Long.MAX_VALUE-4),
				new PaletteTestItem("Table",   0, Long.MAX_VALUE-5),
				new PaletteTestItem("Popup",  35, Long.MAX_VALUE-6),
				new PaletteTestItem("Tabs",   25, Long.MAX_VALUE-7),
				new PaletteTestItem("Link",  115, Long.MAX_VALUE-8)
			}, PaletteSettings.TYPE_MOST_POPULAR, true);
	}
	
	public void testSamePopularitySorting(){
		checkSorting(new PaletteTestItem[]{
				new PaletteTestItem("Page",  100, 200),
				new PaletteTestItem("Dialog", 90, 200),
				new PaletteTestItem("Grid",   80, 200),
				new PaletteTestItem("Panel",  70, 200),
				new PaletteTestItem("Table",  60, 200)
			}, PaletteSettings.TYPE_MOST_POPULAR, false);
	}
	
	public void testLastUsedSortingWithZeroCounts(){
		checkSorting(new PaletteTestItem[]{
				new PaletteTestItem("Page",  1045, 14),
				new PaletteTestItem("Dialog", 923, 23),
				new PaletteTestItem("Grid",   854, 36),
				new PaletteTestItem("Panel",  0,   49),
				new PaletteTestItem("Table",  415, 97),
				new PaletteTestItem("Popup",  315, 87),
				new PaletteTestItem("Tabs",   215, 317),
				new PaletteTestItem("Link",     0, 417),
				new PaletteTestItem("Radio",   15, 717),
				new PaletteTestItem("Slider",   1, 317)
			}, PaletteSettings.TYPE_LAST_USED, false);
	}

	public void testMostPopularSortingWithZeroCounts(){
		checkSorting(new PaletteTestItem[]{
				new PaletteTestItem("Page",   12, 1045),
				new PaletteTestItem("Dialog", 26, 923),
				new PaletteTestItem("Grid",    0, 854),
				new PaletteTestItem("Panel",  48, 541),
				new PaletteTestItem("Table",  20, 415),
				new PaletteTestItem("Popup",   0, 387),
				new PaletteTestItem("Tabs",   25, 217),
				new PaletteTestItem("Link",  115, 117)
			}, PaletteSettings.TYPE_MOST_POPULAR, false);
	}
	
	public void testSamePopularitySortingWithZeroCounts(){
		checkSorting(new PaletteTestItem[]{
				new PaletteTestItem("Page",  100, 200),
				new PaletteTestItem("Dialog", 90, 200),
				new PaletteTestItem("Grid",    0, 200),
				new PaletteTestItem("Panel",  70, 200),
				new PaletteTestItem("Table",  60, 200)
			}, PaletteSettings.TYPE_MOST_POPULAR, false);
	}
	
	private void checkSorting(PaletteTestItem[] testItems, String sortingType, boolean call){
		PaletteSettings.getInstance().setDynamicGroupNumber(testItems.length);
		PaletteSettings.getInstance().setDynamicGroupType(sortingType);
		
		editor = openEditor("empty.html");
		PaletteViewer viewer = getPaletteViewer();
		
		for(PaletteTestItem testItem : testItems){
			testItem.item = find(viewer.getPaletteRoot(), testItem.name);
			assertNotNull("Palette Item \""+testItem.name+"\" not found", testItem.item);
		}
		
		IPaletteItem baseItem = testItems[0].item;
		
		zeroAllCounts(baseItem);
		
		for(PaletteTestItem testItem : testItems){
			testItem.item.setNumberOfCalls(testItem.numberOfCalls);
			testItem.item.setCountIndex(testItem.countIndex);
		}
		
		if(call){
			for(int index = testItems.length-1; index >= 0; index--){
				if(testItems[index].countIndex > 0){
					testItems[index].item.called();
				}
			}
		}
		
		ArrayList<PaletteTestItem> testList = new ArrayList<PaletteTestItem>();
		
		for(PaletteTestItem testItem : testItems){
			if(testItem.countIndex > 0){
				testList.add(testItem);
			}
		}
		
		PaletteModelImpl model = (PaletteModelImpl)baseItem.getCategory().getVersionGroup().getGroup().getPaletteModel();
		
		HTML5DynamicPaletteGroup dynamicGroup = (HTML5DynamicPaletteGroup)model.getPaletteGroup(PaletteModelImpl.DYNAMIC_PALETTE_GROUP);
		assertNotNull("HTML5DynamicPaletteGroup not found", dynamicGroup);
		
		List<IPaletteItem> items = dynamicGroup.getSelectedVersionGroup().getCategories().get(0).getItems();
		
		assertEquals("Wrong number of items", testList.size(), items.size());
		
		for(int index = 0; index < testList.size(); index++){
			assertEquals("\""+testList.get(index).name+"\" item should be with "+index+" index", testList.get(index).name, items.get(index).getName());
		}
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
	
	static class PaletteTestItem{
		String name;
		long numberOfCalls;
		long countIndex;
		IPaletteItem item;
		
		public PaletteTestItem(String name, long countIndex, long numberOfCalls){
			this.name = name;
			this.countIndex = countIndex;
			this.numberOfCalls = numberOfCalls;
		}
	}
	
}

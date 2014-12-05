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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.ui.IEditorPart;
import org.jboss.tools.jst.jsp.test.palette.AbstractPaletteEntryTest;
import org.jboss.tools.jst.web.ui.palette.PaletteAdapter;
import org.jboss.tools.jst.web.ui.palette.internal.PaletteSettings;
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteItem;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.PaletteTool;
import org.jboss.tools.jst.web.ui.test.palette.TestPaletteGroup;

public class PaletteFilterTest extends AbstractPaletteEntryTest {
	IEditorPart editor = null;

	public PaletteFilterTest() {
		TestPaletteGroup.setEnabled(true);
	}

	public void tearDown() {
		PaletteSettings.getInstance().setRecognizedGroupsOnly(false);
		if(editor != null){
			editor.getSite().getPage().closeEditor(editor, false);
		}
	}
	
	public void testJQM13Recognizer(){
		checkRecognizer("recogn_jqm13.html", new String[]{"jQuery Mobile", "HTML"}, true);
	}

	public void testJQM13Recognizer2(){
		checkRecognizer("recogn_jqm13.html", new String[]{"A Test", "jQuery Mobile", "HTML"}, false);
	}

	public void testJQM14Recognizer(){
		checkRecognizer("recogn_jqm14.html", new String[]{"jQuery Mobile", "HTML"}, true);
	}

	public void testJQM14Recognizer2(){
		checkRecognizer("recogn_jqm14.html", new String[]{"A Test", "jQuery Mobile", "HTML"}, false);
	}
	
	public void testHTML5Recognizer(){
		checkRecognizer("recogn_html5.html", new String[]{"HTML"}, true);
	}

	public void testHTML5Recognizer2(){
		checkRecognizer("recogn_html5.html", new String[]{"A Test", "jQuery Mobile", "HTML"}, false);
	}

	protected void checkRecognizer(String fileName, String[] recognizedGroups, boolean showOnlyRecognizedGroups){
		PaletteSettings.getInstance().setRecognizedGroupsOnly(showOnlyRecognizedGroups);
		editor = openEditor(fileName);
		PaletteViewer viewer = getPaletteViewer();
		assertEntryVisible(viewer.getPaletteRoot(), recognizedGroups);
	}
	
	private void assertEntryVisible(PaletteContainer container, String[] recognizedGroups){
		@SuppressWarnings("rawtypes")
		List children = container.getChildren();
		for(Object child : children){
			if(child instanceof PaletteContainer){
				if(((PaletteContainer) child).isVisible()){
					assertGroupVisible(((PaletteContainer) child).getLabel(), recognizedGroups);
				}else{
					assertGroupNotVisible(((PaletteContainer) child).getLabel(), recognizedGroups);
				}
			}
		}
	}
	
	private void assertGroupVisible(String name, String[] visibleGroups){
		for(String group : visibleGroups){
			if(group.equals(name)){
				return;
			}
		}
		fail("Palette group - "+name+" should not be visible");
	}
	
	private void assertGroupNotVisible(String name, String[] visibleGroups){
		for(String group : visibleGroups){
			if(group.equals(name)){
				fail("Palette group - "+name+" should be visible");
			}
		}
	}

	public void testFilter(){
		PaletteSettings.getInstance().setRecognizedGroupsOnly(false);
		editor = openEditor("empty.html");
		PaletteViewer viewer = getPaletteViewer();
		PaletteAdapter adapter = getPaletteAdapter();
		
		ArrayList<String> names = new ArrayList<String>();
		adapter.filter();
		assertAllVisible(viewer.getPaletteRoot(), names);
		
		if(names.size() > 0){
			adapter.filter(names.get(0));
			assertEntryVisible(viewer.getPaletteRoot(), names.get(0));
		}
		if(names.size() > 1){
			adapter.filter(names.get(1));
			assertEntryVisible(viewer.getPaletteRoot(), names.get(1));
		}
		if(names.size() > 2){
			adapter.filter(names.get(2));
			assertEntryVisible(viewer.getPaletteRoot(), names.get(2));
		}
	}
	
	private void assertEntryVisible(PaletteContainer container, String name){
		@SuppressWarnings("rawtypes")
		List children = container.getChildren();
		for(Object child : children){
			if(!(child instanceof PaletteContainer)){
				if(child instanceof PaletteTool){
					PaletteTool entry = (PaletteTool)child;
					IPaletteItem item = entry.getPaletteItem();
					if(item.getKeywordsAsString().toLowerCase().contains(name.toLowerCase())){
						if(!entry.isVisible()){
							fail("Element - "+name+" must be visible!");
						}
					}else{
						if(entry.isVisible()){
							fail("Element - "+entry.getLabel()+" is visible. Only element - "+name+" must be visible!");
						}
					}
				}
			} else {
				assertEntryVisible((PaletteContainer)child, name);
			}
		}
	}
	
	
	private void assertAllVisible(PaletteContainer container, List<String> names){
		@SuppressWarnings("rawtypes")
		List children = container.getChildren();
		for(Object child : children){
			if(!(child instanceof PaletteContainer)){
				if(child instanceof ToolEntry){
					PaletteEntry entry = (PaletteEntry)child;
					names.add(entry.getLabel());
					if(!entry.isVisible()){
						fail("All elements must be visible!");
					}
				}
			} else {
				assertAllVisible((PaletteContainer)child, names);
			}
		}
	}
}

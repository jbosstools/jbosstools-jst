package org.jboss.tools.jst.web.ui.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.jboss.tools.jst.web.ui.palette.PaletteAdapter;
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteItem;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.PaletteModelImpl;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.PaletteTool;
import org.jboss.tools.jst.web.ui.palette.model.IPaletteModel;

public class PaletteFilterTest  extends TestCase {
	public void testFilter(){
		IPaletteModel model = new PaletteModelImpl();
		//model.setPaletteContents(new PagePaletteContents(null));
		
		PaletteAdapter adapter = new PaletteAdapter();
		
		adapter.testFilter(model, "");
		
		ArrayList<String> names = new ArrayList<String>();
		
		assertAllVisible(model.getPaletteRoot(), names);
		
		if(names.size() > 0){
			checkName(adapter, model, names.get(0));
		}
		if(names.size() > 1){
			checkName(adapter, model, names.get(1));
		}
		if(names.size() > 2){
			checkName(adapter, model, names.get(2));
		}
	}
	
	private void checkName(PaletteAdapter adapter, IPaletteModel model, String name){
		adapter.testFilter(model, name);
		
		assertEntryVisible(model.getPaletteRoot(), name);
	}
	
	private void assertEntryVisible(PaletteContainer container, String name){
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

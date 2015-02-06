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
package org.jboss.tools.jst.web.ui.palette.internal.html.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;
import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.jst.web.kb.taglib.IHTMLLibraryVersion;
import org.jboss.tools.jst.web.kb.taglib.ITagLibRecognizer;
import org.jboss.tools.jst.web.kb.taglib.ITagLibVersionRecognizer;
import org.jboss.tools.jst.web.kb.taglib.ITagLibrary;
import org.jboss.tools.jst.web.ui.palette.internal.PaletteSettings;
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteItem;


public class HTML5DynamicPaletteGroup extends AbstractPaletteGroup {
	private ArrayList<IPaletteItem> items = new ArrayList<IPaletteItem>();
	private ArrayList<ImageDescriptor> descriptors = new ArrayList<ImageDescriptor>();
	private ImageDescriptor imageDescriptor = null;
	
	public HTML5DynamicPaletteGroup(){
		add(new DynamicPaletteVersionGroup());
	}
	
	public void add(IPaletteItem item){
		items.add(item);
	}
	
	public void add(ImageDescriptor imageDEscriptor){
		if(descriptors.size() < 3){
			descriptors.add(imageDEscriptor);
		}
	}
	
	public void initCounts() {
		Collections.sort(items, new LastUsedComparator());
		long count = 1;
		((PaletteItemImpl)items.get(0)).setProjectCountIndex(count);
		for(int index = items.size()-1; index >= 0; index--){
			if(items.get(index).getCountIndex() > 0){
				items.get(index).setCountIndex(count++);
			}
		}
		
		Collections.sort(items, new MostPopularComparator());
		long numberOfCalls = 1;
		for(int index = items.size()-1; index >= 0; index--){
			if(items.get(index).getNumberOfCalls() > 0){
				items.get(index).setNumberOfCalls(numberOfCalls++);
			}
		}
	}
	
	public List<IPaletteItem> getAllItems(){
		return items;
	}
	

	@Override
	public String getName() {
		return "";
	}

	@Override
	public ITagLibRecognizer getRecognizer() {
		return new HTML5DynamicRecognizer();
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		if(imageDescriptor == null && descriptors.size() > 0){
			Image resultImage = new Image(Display.getCurrent(), descriptors.size()*16, 16);
			ImageData data = resultImage.getImageData();
			data.alpha = 0;
			resultImage = new Image(Display.getCurrent(), data);
			final GC gc = new GC(resultImage);
			int x = 0;
			for (ImageDescriptor descriptor : descriptors) {
				if(descriptor != null){
					Image image = descriptor.createImage(Display.getCurrent());
					gc.drawImage(image, x, 0);
					x += image.getBounds().width;
				}
			}
			gc.dispose();
			imageDescriptor = ImageDescriptor.createFromImage(resultImage);
		}
		return imageDescriptor;
	}
	
	private String getIdWithoutVersion(IPaletteItem item){
		return "/"+item.getCategory().getVersionGroup().getGroup().getName()+
				"/"+item.getName();
	}
	
	public class DynamicPaletteVersionGroup extends AbstractPaletteVersionGroup{
		public DynamicPaletteVersionGroup(){
			add(new DynamicPaletteCategoty());
		}

		@Override
		public IHTMLLibraryVersion getVersion() {
			return DynamicVersion.DYNAMIC_1_0;
		}
		
		
	}
	
	public enum DynamicVersion implements IHTMLLibraryVersion {
		DYNAMIC_1_0("1.0");

		String version;

		DynamicVersion(String version) {
			this.version = version;
		}

		@Override
		public String toString() {
			return version;
		}

		@Override
		public boolean isPreferredJSLib(IFile file, String libName) {
			return false;
		}

		@Override
		public boolean isReferencingJSLib(IFile file, String libName) {
			return false;
		}
	}
	
	public class HTML5DynamicRecognizer implements ITagLibVersionRecognizer{

		@Override
		public boolean shouldBeLoaded(ITagLibrary lib, ELContext context) {
			return false;
		}

		@Override
		public boolean isUsed(ELContext context) {
			return true;
		}

		@Override
		public boolean isUsed(IFile file) {
			return true;
		}

		@Override
		public IHTMLLibraryVersion getVersion(ELContext context) {
			return DynamicVersion.DYNAMIC_1_0;
		}
		
	}
	
	public class DynamicPaletteCategoty extends AbstractPaletteCategory{

		@Override
		public List<IPaletteItem> getItems() {
			ArrayList<IPaletteItem> list = new ArrayList<IPaletteItem>();
			ArrayList<String> ids = new ArrayList<String>();
			if(PaletteSettings.TYPE_LAST_USED.equals(PaletteSettings.getInstance().getDynamicGroupType())){
				Collections.sort(items, new LastUsedComparator());
			}else{
				Collections.sort(items, new MostPopularComparator());
			}
			int index = 0;
			for(IPaletteItem item : items){
				if(index >= PaletteSettings.getInstance().getDynamicGroupNumber()){
					break;
				}
				String id = getIdWithoutVersion(item);
				if(!ids.contains(id) && item.getCountIndex() > 0){
					ids.add(id);
					list.add(item);
					index++;
				}
			}
			return list;
		}
	}
	
	public class MostPopularComparator implements Comparator<IPaletteItem>{
		@Override
		public int compare(IPaletteItem i1, IPaletteItem i2) {
			int c = Long.compare(i2.getNumberOfCalls(), i1.getNumberOfCalls());
			if(c == 0){
				c = Long.compare(i2.getCountIndex(), i1.getCountIndex());
			}
			return c;
		}
	}

	public class LastUsedComparator implements Comparator<IPaletteItem>{
		@Override
		public int compare(IPaletteItem i1, IPaletteItem i2) {
			return Long.compare(i2.getCountIndex(), i1.getCountIndex());
		}
	}
}

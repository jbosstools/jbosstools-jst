/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.web.tiles.ui.editor.figures;
import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

import org.jboss.tools.jst.web.tiles.ui.editor.edit.DefinitionEditPart;
import org.jboss.tools.jst.web.tiles.ui.editor.model.IDefinition;
import org.jboss.tools.jst.web.tiles.ui.editor.model.ITilesOptions;

import java.util.*;

public class TilesDiagramLayout extends AbstractLayout{
	Dimension dim = new Dimension(0,0);
	Hashtable<IDefinition,Rectangle> figureDim = new Hashtable<IDefinition,Rectangle>();
	Hashtable<String,IDefinition> figureNames;
	int figureW = 114;
	int figureH = 21;
	int border = 30;
	ITilesOptions options;
	IFigure container;
	
	private int getGridX(){
		return options.getHorizontalSpacing();
	}

	private int getGridY(){
		return options.getVerticalSpacing();
	}
	
	private List<Object> getDefinitions(){
		return ((DiagramFigure)container).getEditPart().getTilesModel().getDefinitionList().getElements();
	}
	
	private DefinitionFigure getFigure(IDefinition definition){
		DefinitionEditPart part = (DefinitionEditPart)((DiagramFigure)container).getEditPart().getViewer().getEditPartRegistry().get(definition);
		if(part == null)return null;
		else return part.getDefinitionFigure();
	}
	
	public TilesDiagramLayout(ITilesOptions options){
		super();
		this.options = options;
	}
	
	public Dimension calculatePreferredSize(IFigure container,int wHint, int hHint){
		calculateDim();
		return this.dim;
	}
	
	private void calculateDim(){
		int x = 0;
		int y = 0;
		for (Iterator i = figureDim.keySet().iterator(); i.hasNext();) {
			Rectangle fb = (Rectangle)figureDim.get(i.next());
			x = Math.max(x,fb.x+fb.width);
			y = Math.max(y,fb.y+fb.height);
		}
		this.dim.width = x;
		this.dim.height = y;
	}
	
	public void layout(IFigure container){
		this.container = container;
		
		figureDim.clear();
		
		List l = getDefinitions();
		figureNames = new Hashtable<String,IDefinition>();
		List<IDefinition> figures = new Vector<IDefinition>(); 
		for (int i = 0; i < l.size(); i++) {
			IDefinition tf = (IDefinition)l.get(i);
			figureNames.put(tf.getName(),tf);
			figures.add(tf);
		}
		
		TreeSet lines = createLine(figures,figureNames);
		Dimension lineSize = new Dimension(0,0);
		for (Iterator i = lines.iterator(); i.hasNext();) {	
			Hashtable hs = (Hashtable)i.next();
			int ret = layoutLine(hs,lineSize,figureNames);
			lineSize.width = 0;
			lineSize.height=ret;
			lineSize.height += getGridY();
		}		
		
		for (int i = 0; i < figures.size(); i++) {
			IDefinition tf = (IDefinition)figures.get(i);
			IFigure fig = getFigure(tf);
			if(fig != null){
				Rectangle r = (Rectangle)figureDim.get(tf);
				if(r!=null)fig.setBounds(r);
			}
		}
		calculateDim();
	}

	public int empty(int y, TreeSet ts){
		int maxY = 0;
		
		for(Iterator i = ts.iterator(); i.hasNext();){
			Rectangle ry = (Rectangle)i.next();
			if(ry.y>maxY)maxY=ry.y;
		}
		int ok = y;
		boolean q = false;
		for(int i=y; i<=maxY; i=i+getGridY()){
			for(Iterator it = ts.iterator(); it.hasNext();){
				Rectangle ry = (Rectangle)it.next();
				if(ry.contains(ry.x+ry.width/2,i+ry.height/2)){
					q = false;
					break;
				}else{
					q = true;
				}
			}
			
			ok = i;
			if(q)break;
		}
		return ok;
	}

	public int layoutLine(Hashtable h, Dimension fSize, Hashtable names){
		int ret = fSize.height;
		int www=0;
		if(h.isEmpty())return ret;
		Dimension s = new Dimension(fSize);

		TreeSet ts = new TreeSet(new WeightComparator(h));
		
		for (Iterator i = h.keySet().iterator(); i.hasNext();) {
			String name = (String)i.next();
			ts.add(name);	
		}
		
		for (Iterator i = ts.iterator(); i.hasNext();) {	
			String name = (String)i.next();
			Hashtable ht = (Hashtable)h.get(name);
			
			IDefinition tf = (IDefinition)names.get(name);
			if(tf!=null){
				IFigure fig = getFigure(tf);
				if(fig != null){
					if(figureDim.containsKey(tf)){
						figureDim.remove(tf);
					}
					www = 20;
					figureDim.put(tf,new Rectangle(new Point(s.width*getGridX()+border,s.height+getGridY()+border+s.width*20),new Dimension(figureW,figureH)));
				
					s.width++;
					ret = layoutLine(ht,s,names);
				}else continue;
			}
			
			s.height += getGridY()+www;
			s.width = fSize.width;
			int ss = Math.max(s.height,ret);
			s.height = ss;
		}
		
		return s.height;
	}
	
	public Hashtable<String,Hashtable> getChilds(IDefinition tf, List<IDefinition> figures, Set<IDefinition> usedFigures){
		usedFigures.add(tf);
		Hashtable<String,Hashtable> childs = new Hashtable<String,Hashtable>();
		for (int i = 0; i < figures.size(); i++) {
			IDefinition tf1 = figures.get(i);
			if(usedFigures.contains(tf1)) continue;
			String ex = tf1.getExtends();
			if(tf.getName().equals(ex)){
				childs.put(tf1.getName(),getChilds(tf1,figures, usedFigures));			
			}
		}
		return childs;
	}
	
	public int getWeight(Hashtable ht){
		int j=ht.size();
		for (Iterator i = ht.keySet().iterator(); i.hasNext();) {
			Hashtable h = (Hashtable)ht.get(i.next());
			if(h!=null){
				j = j+getWeight(h);
			}
		}
		return j;
	}
	
	public TreeSet createLine(List<IDefinition> figures, Hashtable figureNames){
		TreeSet lines = new TreeSet(new WeightComparator());
		Set<IDefinition> usedFigures = new HashSet<IDefinition>();
		
		for (int i = 0; i < figures.size(); i++) {
			IDefinition tf = (IDefinition)figures.get(i);
			String name = tf.getName();
			String ex = tf.getExtends();
			if(ex==null||"".equals(ex)){ //$NON-NLS-1$
				Hashtable<String,Hashtable<String,Hashtable>> line = new Hashtable<String,Hashtable<String,Hashtable>>();
				line.put(name, getChilds(tf,figures, usedFigures));
				lines.add(line);
			}else{
				Object o = figureNames.get(ex);
				if(o==null){
					Hashtable<String,Hashtable<String,Hashtable>> line = new Hashtable<String,Hashtable<String,Hashtable>>();
					line.put(name, getChilds(tf,figures,usedFigures));
					lines.add(line);
				}
			}
		}
		while(usedFigures.size() < figures.size()) {
			IDefinition tf = null;
			for (int i = 0; i < figures.size() && tf == null; i++) {
				tf = figures.get(i);
				if(usedFigures.contains(tf)) tf = null;
			}
			if(tf == null) break;
			Hashtable<String,Hashtable<String,Hashtable>> line = new Hashtable<String,Hashtable<String,Hashtable>>();
			line.put(tf.getName(),getChilds(tf,figures, usedFigures));
			lines.add(line);
		}
		return lines;
	}
	
	public class WeightComparator implements Comparator{
		Hashtable table;
		
		public WeightComparator(Hashtable table){
			this.table = table; 
		}
		
		public WeightComparator(){}
		
		public int compare(Object o1, Object o2){
			int a = 0;
			int b = 0; 
			if(o1 instanceof Hashtable&&o2 instanceof Hashtable){
				a = getWeight((Hashtable)o1);
				b = getWeight((Hashtable)o2);
			}else{
				a = getWeight((Hashtable)this.table.get(o1));
				b = getWeight((Hashtable)this.table.get(o2));
			} 
			return (a <= b) ? 1 : -1;
		}
		public boolean equals(Object obj){
			return true;
		}
	}

}

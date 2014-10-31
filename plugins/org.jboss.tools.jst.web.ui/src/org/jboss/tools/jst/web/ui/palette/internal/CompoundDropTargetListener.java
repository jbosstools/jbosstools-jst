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

import java.util.ArrayList;

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;

public class CompoundDropTargetListener extends DropTargetAdapter {

	private ArrayList<DropTargetListener> listeners = new ArrayList<DropTargetListener>();
	private DropTargetListener activeListener = null;

	public CompoundDropTargetListener() {
	}
	
	public void add(DropTargetListener listener){
		listeners.add(listener);
	}

	public void remove(DropTargetListener listener){
		listeners.remove(listener);
	}

	@Override
	public void dragEnter(DropTargetEvent event) {
		int initialDetail = event.detail;
		activeListener = null;
		for(DropTargetListener listener : listeners){
			listener.dragEnter(event);
			if(event.detail != DND.DROP_NONE){
				activeListener = listener;
				return;
			}else{
				event.detail = initialDetail;
			}
		}
		event.detail = DND.DROP_NONE;
	}
	
	@Override
	public void drop(DropTargetEvent event) {
		if(activeListener != null){
			activeListener.drop(event);
		}
	}
	
	@Override
	public void dragOver(DropTargetEvent event) {
		if(activeListener != null){
			activeListener.dragOver(event);
		}
	}

	@Override
	public void dragLeave(DropTargetEvent event) {
		if(activeListener != null){
			activeListener.dragLeave(event);
		}
	}
	
	@Override
	public void dragOperationChanged(DropTargetEvent event) {
		if(activeListener != null){
			activeListener.dragOperationChanged(event);
		}
	}

	@Override
	public void dropAccept(DropTargetEvent event) {
		if(activeListener != null){
			activeListener.dropAccept(event);
		}
	}

}

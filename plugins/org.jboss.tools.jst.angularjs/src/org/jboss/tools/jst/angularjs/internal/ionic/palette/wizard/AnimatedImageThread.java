/******************************************************************************* 
 * Copyright (c) 2015 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Display;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class AnimatedImageThread extends Thread {
	List<AnimatedImageBinding> bindings = new ArrayList<AnimatedImageBinding>();

	public AnimatedImageThread() {
	}

	public void addBinding(AnimatedImageBinding binding) {
		bindings.add(binding);
	}

	@Override
	public void run() {
		if(bindings.isEmpty()) {
			return;
		}
		long time = System.currentTimeMillis();
		for (AnimatedImageBinding binding: bindings) {
			binding.init(time);
		}
		while(true) {
			if(bindings.get(0).isDisposed()) return;
			long minTime = System.currentTimeMillis() + 1000000;
			for (AnimatedImageBinding binding: bindings) {
				if(binding.getNextTime() < minTime) {
					minTime = binding.getNextTime();
				}
			}
			time = System.currentTimeMillis();
			if(minTime > time) {		
				try {
					Thread.sleep(minTime - time + 1);
					time = System.currentTimeMillis();
				} catch (InterruptedException e) {
				//
				}
			}

			boolean redraw = false;
			for (AnimatedImageBinding binding: bindings) {
				binding.setTime(time);
				if(binding.isReadyToRedraw()) {
					redraw = true;
				}
			}
			if(redraw) {
				redraw();
			}
		}
	}
	
	void redraw() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				for (AnimatedImageBinding binding: bindings) {
					binding.redraw();
				}
			}
		});
	}
}

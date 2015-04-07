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

import org.eclipse.swt.widgets.Button;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class AnimatedImageBinding {
	private AnimatedImage image;
	private Button button;

	private long nextTime = System.currentTimeMillis();
	private boolean redraw = false;

	public AnimatedImageBinding(AnimatedImage image, Button button) {
		this.image = image;
		this.button = button;
	}

	public void init(long time) {
		nextTime = time + image.getDelay();
	}

	public void setTime(long time) {
		while(time > nextTime) {
			nextTime += image.getDelay();
			image = image.next();
			redraw = true;
		}
	}

	public void redraw() {
		if(redraw && !isDisposed()) {
			button.setImage(image.getImage());
			redraw = false;
		}
	}

	public boolean isReadyToRedraw() {
		return redraw;
	}

	public boolean isDisposed() {
		return button.isDisposed();
	}

	public long getNextTime() {
		return nextTime;
	}

}

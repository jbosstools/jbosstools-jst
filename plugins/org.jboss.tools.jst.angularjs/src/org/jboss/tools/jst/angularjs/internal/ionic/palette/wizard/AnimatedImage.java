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

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;
import org.jboss.tools.jst.angularjs.internal.ui.AngularJsUIImages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class AnimatedImage {
	private Image image;
	private int delay;
	private AnimatedImage next;

	private AnimatedImage() {}
	
	public static AnimatedImage create(String key) {
		ImageLoader loader = new ImageLoader();
		loader.load(NewSpinnerWizardPage.class.getResourceAsStream(key));
		ImageData[] data = loader.data;
		AnimatedImage first = new AnimatedImage();
		first.image = new Image(Display.getDefault(), data[0]);
		first.delay = data[0].delayTime;
		
		AnimatedImage previous = first;
		for (int i = 1; i < data.length; i++) {
			previous.next = new AnimatedImage();
			previous.next.image = new Image(Display.getDefault(), data[i]);
			previous.next.delay = data[i].delayTime * 10; // to milliseconds
			previous = previous.next;
		}
		previous.next = first;
		return first;
	}

	public static AnimatedImage create(String prefix, int number, int delay) {
		AnimatedImage first = null;
		AnimatedImage previous = null;
		for (int i = 1; i <= number; i++) {
			AnimatedImage image = new AnimatedImage();
			if(first == null) {
				first = image; 
			} else {
				previous.next = image;
			}			
			image.image = AngularJsUIImages.getInstance().getOrCreateImage(prefix + i + ".png");
			image.delay = delay;
			previous = image;
		}
		previous.next = first;
		return first;
	}

	public Image getImage() {
		return image;
	}

	public int getDelay() {
		return delay;
	}

	public AnimatedImage next() {
		return next;
	}
	
}

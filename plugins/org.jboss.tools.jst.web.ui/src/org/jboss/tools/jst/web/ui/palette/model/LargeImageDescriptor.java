/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.web.ui.palette.model;

import java.util.Hashtable;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.icons.impl.LargeReferencedIcons;

/**
 * @author Viacheslav Kabanovich
 */
public class LargeImageDescriptor extends ImageDescriptor {
	private static Hashtable<String,Image> imageCache = new Hashtable<String,Image>();
	LargeReferencedIcons xicon = new LargeReferencedIcons();
	XModelObject object;

	public LargeImageDescriptor(XModelObject object) {
		this.object = object;
	}

	public ImageData getImageData() {
		return createImage().getImageData();
	}

	public Image createImage(boolean returnMissingImageOnError, Device device) {
		int code = xicon.getHash(object);
		if (code == 0) return null;
		String key = "" + code; //$NON-NLS-1$
		Image img = (Image)imageCache.get(key);
		if (img != null) return img;
		img = xicon.getImage(object);
		if (img != null) imageCache.put(key, img);
		return img;
	}
}

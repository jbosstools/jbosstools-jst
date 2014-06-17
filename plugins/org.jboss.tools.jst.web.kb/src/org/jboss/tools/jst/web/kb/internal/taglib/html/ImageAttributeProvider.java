/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.internal.taglib.html;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IResource;

/**
 * @author Alexey Kazakov
 */
public class ImageAttributeProvider extends FileNameAttributeProvider {

	private static final String ATTRIBUTE_NAME = "src";
	private static final Set<String> EXTENSIONS = new HashSet<String>();
	static {
		EXTENSIONS.add("gif");
		EXTENSIONS.add("png");
		EXTENSIONS.add("jpg");
		EXTENSIONS.add("jpeg");
		EXTENSIONS.add("bmp");
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.html.FileNameAttributeProvider#getAttributeName()
	 */
	@Override
	protected String getAttributeName() {
		return ATTRIBUTE_NAME;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.html.FileNameAttributeProvider#match(org.eclipse.core.resources.IResource)
	 */
	@Override
	protected boolean match(IResource resource) {
		return EXTENSIONS.contains(resource.getFileExtension());
	}
}
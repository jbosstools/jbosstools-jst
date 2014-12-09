/******************************************************************************* 
 * Copyright (c) 2013-2014 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.taglib;

import org.eclipse.core.resources.IFile;
import org.jboss.tools.common.el.core.resolver.ELContext;

/**
 * @author Alexey Kazakov
 */
public interface ITagLibRecognizer {

	/**
	 * Returns true if the library should be loaded by the tag library manager for this context.
	 * @param lib
	 * @return
	 */
	boolean shouldBeLoaded(ITagLibrary lib, ELContext context);

	/**
	 * Returns true if the library is used in this context.
	 * @param context
	 * @return
	 */
	boolean isUsed(ELContext context);

	/**
	 * Returns true if the library is used in this file.
	 * @param file
	 * @return
	 */
	boolean isUsed(IFile file);
}
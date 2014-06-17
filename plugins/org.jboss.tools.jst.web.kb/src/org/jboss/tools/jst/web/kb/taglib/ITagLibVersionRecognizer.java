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
package org.jboss.tools.jst.web.kb.taglib;

import org.jboss.tools.common.el.core.resolver.ELContext;

/**
 * @author Alexey Kazakov
 */
public interface ITagLibVersionRecognizer extends ITagLibRecognizer {

	/**
	 * Returns the version of the library which should be used by the tag library manager or null if the library should not be loaded.
	 * @param context
	 * @return
	 */
	String getVersion(ELContext context);
}
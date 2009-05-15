/******************************************************************************* 
 * Copyright (c) 2009 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb;

import org.eclipse.core.resources.IProjectNature;
import org.jboss.tools.jst.web.kb.taglib.ITagLibrary;

/**
 * 
 * @author V.Kabanovich
 *
 */
public interface IKbProject extends IProjectNature {
	public static String NATURE_ID = Activator.PLUGIN_ID + ".kbnature"; //$NON-NLS-1$

	public ITagLibrary[] getTagLibraries();

	public void resolve();

}

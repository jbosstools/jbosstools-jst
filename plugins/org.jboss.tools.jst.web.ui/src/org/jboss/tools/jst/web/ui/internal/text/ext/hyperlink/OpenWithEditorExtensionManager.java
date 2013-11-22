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
package org.jboss.tools.jst.web.ui.internal.text.ext.hyperlink;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.runnable.ParameterizedRunnable;
import org.jboss.tools.common.el.core.ELCorePlugin;

public class OpenWithEditorExtensionManager {
	public static String EXTENSION_POINT = "org.jboss.tools.jst.web.ui.openWithEditorExtension"; //$NON-NLS-1$

	public static OpenWithEditorExtension[] INSTANCES = getInstances();

	private static OpenWithEditorExtension[] getInstances() {
		List<OpenWithEditorExtension> list = new ArrayList<OpenWithEditorExtension>();
		IExtensionPoint point = Platform.getExtensionRegistry().getExtensionPoint(EXTENSION_POINT);
		IConfigurationElement[] es = point.getConfigurationElements();
		for (IConfigurationElement e: es) {
			
			String id = e.getAttribute("id"); //$NON-NLS-1$
			String editorName = e.getAttribute("editor-name"); //$NON-NLS-1$
			try{
				ParameterizedRunnable editorLauncher = (ParameterizedRunnable)e.createExecutableExtension("class"); //$NON-NLS-1$
				OpenWithEditorExtension extension = new OpenWithEditorExtension(id, editorName, editorLauncher);
				list.add(extension);
			}catch(CoreException ex){
				ELCorePlugin.getDefault().logError(ex);
			}
			
		}
		OpenWithEditorExtension[] instances = list.toArray(new OpenWithEditorExtension[0]);
		
		Arrays.sort(instances);
		
		return instances;
	}
}

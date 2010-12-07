/*******************************************************************************
 * Copyright (c) 2007-2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.jsp.outline.css;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.jboss.tools.jst.jsp.JspEditorPlugin;

/**
 * @author mareshkau
 *
 */
public class AbstractCSSStyleDecorator {

	private static final String CSSStyleDialogExtensionPointName="cssDialog"; //$NON-NLS-1$
	private static final String CSSStyleDialogImplementationId="org.jboss.tools.jst.css.dialog.CSSStyleDialog"; //$NON-NLS-1$
	private  CSSDialogBuilderInterface dialogBuilder;
	
	protected CSSDialogBuilderInterface getCSSDialogBuilder(){
		if(dialogBuilder==null){
				try {
					IExtension visualEditorExtension = Platform.getExtensionRegistry()
							.getExtension(JspEditorPlugin.PLUGIN_ID,
									CSSStyleDialogExtensionPointName,
									CSSStyleDialogImplementationId);
					if (visualEditorExtension != null) {
						IConfigurationElement[] configurationElements = visualEditorExtension
								.getConfigurationElements();
						if (configurationElements != null
								&& configurationElements.length == 1) {
							dialogBuilder =  (CSSDialogBuilderInterface) configurationElements[0]
									.createExecutableExtension("class"); //$NON-NLS-1$
						} else {
							JspEditorPlugin
									.getPluginLog()
									.logError(
											"CSS Dialog Implementation not available"); //$NON-NLS-1$
						}
					} else {
						JspEditorPlugin.getPluginLog().logError(
								"CSS Dialog Implementation not available"); //$NON-NLS-1$
					}
				} catch (CoreException e) {
					JspEditorPlugin.getPluginLog().logError(
							"CSS Dialog Implementation not available" + e); //$NON-NLS-1$
				}
		}
		return dialogBuilder;
	}
}

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



import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

/**
 * Decorator for CSS Dialog
 * @author mareshkau
 *
 */
public class CSSStyleDialogDecorator extends AbstractCSSStyleDecorator{
	
	
	private CSSStyleDialogInterface cssStyleDialog;
	private String oldStyle;
		
	public CSSStyleDialogDecorator(final Shell parentShell,final String style) {
		if(getCSSDialogBuilder()!=null){
			cssStyleDialog=getCSSDialogBuilder().buildCSSStyleDialog(parentShell, style);
		}
		oldStyle=style;
	}

	public String getStyle() {
		if(cssStyleDialog!=null)
		return cssStyleDialog.getStyle();
		else return oldStyle; 
	}

	public int open() {
		if(cssStyleDialog!=null)
		return cssStyleDialog.open();
		else return Window.CANCEL;
	}

}

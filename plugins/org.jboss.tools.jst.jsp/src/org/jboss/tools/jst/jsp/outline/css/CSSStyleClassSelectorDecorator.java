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


import org.eclipse.swt.widgets.Shell;


/**
 * @author mareshkau
 *
 */
public class CSSStyleClassSelectorDecorator extends AbstractCSSStyleDecorator{

	private CSSStyleClassSelectorInterface  cssStyleClassSelector;
	public CSSStyleClassSelectorDecorator(Shell parentShell) {
		if(getCSSDialogBuilder()!=null){
			cssStyleClassSelector=getCSSDialogBuilder().buildCSSClassDialog(parentShell);
		}
	}

	public void setCurrentStyleClass(String value) {
		if(cssStyleClassSelector!=null){
			cssStyleClassSelector.setCurrentStyleClass(value);
		}
		
	}

	public int open() {
		if(cssStyleClassSelector!=null){
			return cssStyleClassSelector.open();
		}
		return org.eclipse.jface.window.Window.CANCEL;
	}

	public String getCSSStyleClasses() {
		if(cssStyleClassSelector!=null){
			return cssStyleClassSelector.getCSSStyleClasses();
		}
		return null;
	}

}

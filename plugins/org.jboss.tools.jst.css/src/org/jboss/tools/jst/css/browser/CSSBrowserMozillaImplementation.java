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
package org.jboss.tools.jst.css.browser;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.TypedEvent;

/**
 * @author mareshkau
 *
 */
class CSSBrowserMozillaImplementation implements CSSBrowserInterface {
	final private Browser browser;

	public CSSBrowserMozillaImplementation(Browser browser) {
		super();
		this.browser = browser;
	}
	public boolean setFocus() {
		return getBrowser().setFocus();
	}
	public void setText(String generateBrowserPage) {
		getBrowser().setText(generateBrowserPage);
	}
	public void setEnabled(boolean isEnabled) {
		getBrowser().setEnabled(isEnabled);
	}
	public void addMouseListener(MouseAdapter mouseAdapter) {
		getBrowser().addMouseListener(mouseAdapter);
	}
	public boolean isBrowserEvent(TypedEvent e){
		return e.widget==getBrowser();
	}
	
	private Browser getBrowser() {
		return this.browser;
	}
	
}

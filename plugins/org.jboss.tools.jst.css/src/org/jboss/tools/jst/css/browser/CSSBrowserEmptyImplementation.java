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

import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.TypedEvent;

/**
 * @author mareshkau
 *
 */
public class CSSBrowserEmptyImplementation implements CSSBrowserInterface {
	
	public boolean setFocus() {
		return false;
	}
	public void setText(String generateBrowserPage) {
	}
	public void setEnabled(boolean isEnabled) {
	}
	public void addMouseListener(MouseAdapter mouseAdapter) {
	}
	public boolean isBrowserEvent(TypedEvent e){
		return false;
	}
}

/******************************************************************************* 
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.jsp.selection.bar;

/**
 * Controller to control state of selection bar
 * 
 * @author mareshkau
 *
 */
public interface ISelectionBarController {
	/**
	 * Shows selection bar
	 */
	public void showSelectionBar();
	/**
	 * Hides selection bar
	 */
	public void hideSelectionBar();
	/**
	 * retry the availability of selection bar
	 */
	public boolean isSelectionBarAvailable();
}

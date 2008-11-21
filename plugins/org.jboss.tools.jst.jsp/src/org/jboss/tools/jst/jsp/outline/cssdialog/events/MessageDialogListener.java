/*******************************************************************************
  * Copyright (c) 2007-2008 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.jst.jsp.outline.cssdialog.events;

/**
 * Defines an object which listens for MessageDialogEvent.
 */
public interface MessageDialogListener  {

	/**
	 * Invoked when the target of the listener has changed its state.
	 * @param event a MessageDialogEvent object
	 */
    public void throwMessage(MessageDialogEvent event);
}

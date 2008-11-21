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

import java.util.EventObject;

import org.eclipse.core.runtime.IStatus;

/**
 * An event which indicates that any dialog contains empty mandatory fields.
 */
public class MessageDialogEvent extends EventObject {

	private static final long serialVersionUID = -5978788274463066057L;

	private IStatus operationStatus = null;

	/**
	 * Constructor.
	 *
	 * @param source the Component that originated the event
	 */
    public MessageDialogEvent(Object source, IStatus operationStatus) {
        super(source);
        this.operationStatus = operationStatus;
    }

	/**
	 * Gets operation status.
	 *
	 * @return the operationStatus
	 */
	public IStatus getOperationStatus() {
		return operationStatus;
	}

}

/*******************************************************************************
 * Copyright (c) 2007-2010 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.selection;

import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.model.CSSSelectorTreeModel;

/**
 * 
 * @author yzhishko
 *
 */

public class CSSSelectorEventManager {

	private static CSSSelectorEventManager instance = new CSSSelectorEventManager();

	private CSSSelectorEventManager() {

	}

	public static CSSSelectorEventManager getInstance() {
		return instance;
	}

	public CSSTreeSelectionChangedEvent createTreeSelectionChangedEvent(
			SelectionChangedEvent event, CSSSelectorTreeModel model) {
		return new CSSTreeSelectionChangedEvent(event.getSelectionProvider(),
				event.getSelection(), model);
	}

	public CSSTableSelectionChangedEvent createTableSelectionChangedEvent(
			SelectionChangedEvent event, CSSSelectorTreeModel model) {
		return new CSSTableSelectionChangedEvent(event.getSelectionProvider(),
				event.getSelection(), model);
	}

}

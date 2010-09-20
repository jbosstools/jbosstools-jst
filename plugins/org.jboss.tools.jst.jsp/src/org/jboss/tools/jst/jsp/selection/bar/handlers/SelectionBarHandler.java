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

package org.jboss.tools.jst.jsp.selection.bar.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * Selection bar handler
 */
public class SelectionBarHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public SelectionBarHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
//		IEditorPart activeEditor = HandlerUtil.getActiveEditorChecked(event);
		HandlerUtil.toggleCommandState(event.getCommand());
//		if(!togleState){
//			((ISelectionBarController)activeEditor).showSelectionBar();
//		}else{
//			((ISelectionBarController)activeEditor).hideSelectionBar();
//		}
		return null;
	}
	
}

/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.web.tiles.ui.editor.dnd;

import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.jst.web.tiles.ui.editor.model.IDefinition;

public class DndUtils {
	static XModelObject clipBoard=null;
	
	public static void copy(IDefinition definition){
		clipBoard = (XModelObject)definition.getSource();
	}
	
	public static void paste(IDefinition definition){
		if(clipBoard != null){
			clipBoard.setAttributeValue("extends", definition.getName()); //$NON-NLS-1$
			clipBoard = null;
		}
	}
}

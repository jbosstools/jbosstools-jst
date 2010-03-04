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

package org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.viewers;

import java.util.List;

import org.eclipse.jface.viewers.Viewer;
import org.jboss.tools.common.model.ui.attribute.IListContentProvider;

/**
 * 
 * @author yzhishko
 *
 */

public class CSSSelectorTableContentProvider implements IListContentProvider{

	@SuppressWarnings("unchecked")
	public Object[] getElements(Object inputElement) {
		return ((List<String>)inputElement).toArray();
	}

	public void dispose() {
		
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		
	}

}

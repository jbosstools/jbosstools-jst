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

import java.util.HashSet;
import java.util.Set;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.model.CSSTreeNode;

/**
 * 
 * @author yzhishko
 *
 */

public class CSSSelectorFilter extends ViewerFilter{

	private Set<String> filterNames = new HashSet<String>(0);
	
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (filterNames.contains(((CSSTreeNode)element).toString())) {
			return false;
		}
		return true;
	}
	
	public void removeFilterName(String name){
		filterNames.remove(name);
	}
	
	public void addFilterName(String name){
		filterNames.add(name);
	}
	
	public void removeAllFilters(){
		filterNames.clear();
	}
	
}

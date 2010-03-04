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
package org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author yzhishko
 *
 */

public class CSSSelectorTableModel {

	private List<String> containerList = new ArrayList<String>(0);
	
	public List<String> getContainerList() {
		return containerList;
	}

	public CSSSelectorTableModel(String... classNames) {
		initModel(classNames);
	}
	
	private void initModel(String... classNames){
		if (classNames != null) {
			for (int i = 0; i < classNames.length; i++) {
				containerList.add(classNames[i]);
			}
		}
	}
	
	
	
}

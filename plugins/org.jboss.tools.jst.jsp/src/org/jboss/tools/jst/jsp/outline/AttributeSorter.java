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
package org.jboss.tools.jst.jsp.outline;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.ui.views.properties.IPropertySheetEntry;
import org.eclipse.ui.views.properties.PropertySheetSorter;

public class AttributeSorter extends PropertySheetSorter {
	Map weights = new HashMap();

	public int compare(IPropertySheetEntry entryA, IPropertySheetEntry entryB) {
		String displayNameA = entryA.getDisplayName();
		String displayNameB = entryB.getDisplayName();
		int weightA = getWeight(displayNameA);
		int weightB = getWeight(displayNameB);
		if(weightA != weightB) return weightB - weightA;
		return getCollator().compare(displayNameA, displayNameB);
	}
	
	public void setWeight(String displayName, int weight) {
		weights.put(displayName, new Integer(weight));
	}
	
	public int getWeight(String displayName) {
		Integer i = (Integer)weights.get(displayName);
		return (i == null) ? 0 : i.intValue();
	}
	
	public void clear() {
		weights.clear();
	}

}

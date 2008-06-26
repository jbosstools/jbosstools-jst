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
package org.jboss.tools.jst.web.ui.navigator;

import java.text.Collator;
import java.util.Comparator;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.jboss.tools.common.model.XModelObject;

public class XSorter extends ViewerSorter {
	XComparator c = new XComparator();
	
	public XSorter() {}
	
    public int compare(Viewer viewer, Object e1, Object e2) {
		if(e1 == e2) return 0;
		if(e1 instanceof XModelObject && e2 instanceof XModelObject) {
			return c.compare(e1, e2);
		}
		return super.compare(viewer, e1, e2);
    }

	class XComparator implements Comparator<Object> {

		public int compare(Object o1, Object o2) {
			if(o1 == o2) return 0;
			if(o1 instanceof XModelObject && o2 instanceof XModelObject) {
				XModelObject x1 = (XModelObject)o1;
				XModelObject x2 = (XModelObject)o2;
				XModelObject[] os = x1.getParent().getChildren();
				for (int i = 0; i < os.length; i++) {
					if(os[i] == x1) return -1;
					if(os[i] == x2) return 1;
				}
			} else {
				return getCollator().compare(o1, o2);
			}
			return 0;
		}
		
	}

}

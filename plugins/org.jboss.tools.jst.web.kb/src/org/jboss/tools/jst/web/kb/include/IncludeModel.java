/******************************************************************************* 
 * Copyright (c) 2012 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.include;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.jboss.tools.common.el.core.resolver.Var;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class IncludeModel {

	private final static IncludeModel instance = new IncludeModel();

	public static IncludeModel getInstance() {
		return instance;
	}

	private Map<IPath, List<PageInclude>> directReferences = new HashMap<IPath, List<PageInclude>>();
	private Map<IPath, List<PageInclude>> parentReferences = new HashMap<IPath, List<PageInclude>>();

	private IncludeModel() {}

	public synchronized void clean(IPath parent) {
		List<PageInclude> old = directReferences.remove(parent);
		if(old != null && !old.isEmpty()) {
			for (IPath child: parentReferences.keySet()) {
				Iterator<PageInclude> is = parentReferences.get(child).iterator();
				while(is.hasNext()) {
					PageInclude i = is.next();
					if(i.getParent().equals(parent)) {
						is.remove();
					}
				}
			}
		}
	}

	public synchronized void addInclude(IPath parent, PageInclude include) {
		List<PageInclude> current = directReferences.get(parent);
		if(current == null) {
			current = new ArrayList<PageInclude>();
			directReferences.put(parent, current);
		}
		current.add(include);
		IPath child = include.getPath();
		List<PageInclude> is = parentReferences.get(child);
		if(is == null) {
			is = new ArrayList<PageInclude>();
			parentReferences.put(child, is);
		}
		is.add(include);
	}

	public synchronized List<Var> getVars(IPath page) {
		List<Var> result = new ArrayList<Var>();
		List<PageInclude> is = parentReferences.get(page);
		if(is != null) {
			for (PageInclude i: is) {
				result.addAll(i.getVars());
			}
		}
		return result;
	}
	

}

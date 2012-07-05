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

import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.jboss.tools.common.el.core.resolver.Var;

/**
 * Parameters defined within elements <ui:include>, <ui:composition>, <ui:decorate>
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class PageInclude {
	IPath parent;
	IPath path;
	List<Var> vars;

	public PageInclude(IPath parent, IPath path, List<Var> vars) {
		this.parent = parent;
		this.path = path;
		this.vars = vars;
	}

	public IPath getParent() {
		return parent;
	}

	public IPath getPath() {
		return path;
	}

	public List<Var> getVars() {
		return vars;
	}
}

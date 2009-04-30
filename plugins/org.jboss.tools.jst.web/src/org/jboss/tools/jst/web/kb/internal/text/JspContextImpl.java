/******************************************************************************* 
 * Copyright (c) 2009 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.internal.text;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.Region;
import org.jboss.tools.common.el.core.resolver.ELResolver;
import org.jboss.tools.common.el.core.resolver.Var;
import org.jboss.tools.jst.web.kb.PageContext;
import org.jboss.tools.jst.web.kb.ResourceBundle;
import org.jboss.tools.jst.web.kb.taglib.TagLibrary;

/**
 * JSP page context
 * @author Alexey Kazakov
 */
public class JspContextImpl implements PageContext {

	private IFile resource;
	private TagLibrary[] libs;
	private ELResolver[] elResolvers;
	private Map<Region, Var[]> vars = new HashMap<Region, Var[]>();

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.common.kb.text.PageContext#getResource()
	 */
	public IFile getResource() {
		return resource;
	}

	public void setResource(IFile resource) {
		this.resource = resource;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.common.kb.text.PageContext#getLibraries()
	 */
	public TagLibrary[] getLibraries() {
		return libs;
	}

	public void setLibraries(TagLibrary[] libs) {
		this.libs = libs;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.common.kb.text.PageContext#getElResolvers()
	 */
	public ELResolver[] getElResolvers() {
		return elResolvers;
	}

	public void setElResolvers(ELResolver[] elResolvers) {
		this.elResolvers = elResolvers;
	}

	private final static Var[] EMPTY_VAR_ARRAY = new Var[0]; 

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.common.kb.text.PageContext#getVars(int)
	 */
	public Var[] getVars(int offset) {
		for (Region region : vars.keySet()) {
			if(offset>=region.getOffset() && offset<=region.getOffset() + region.getLength()) {
				return vars.get(region);
			}
		}
		return EMPTY_VAR_ARRAY;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.common.kb.text.PageContext#getResourceBundles()
	 */
	public ResourceBundle[] getResourceBundles() {
		// TODO
		return null;
	}
}
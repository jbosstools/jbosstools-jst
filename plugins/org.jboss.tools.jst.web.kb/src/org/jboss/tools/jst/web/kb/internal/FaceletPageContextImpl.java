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
package org.jboss.tools.jst.web.kb.internal;

import java.util.Map;

import org.jboss.tools.jst.web.kb.IFaceletPageContext;

/**
 * Facelet page context
 * @author Alexey Kazakov
 */
public class FaceletPageContextImpl extends JspContextImpl implements IFaceletPageContext {

	private IFaceletPageContext parentContext;
	private Map<String, String> params;

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.IFaceletPageContext#getParams()
	 */
	public Map<String, String> getParams() {
		return params;
	}

	/**
	 * @param params the params to set
	 */
	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.IFaceletPageContext#getParentContext()
	 */
	public IFaceletPageContext getParentContext() {
		return parentContext;
	}

	/**
	 * @param parentContext the parent facelet context to set
	 */
	public void setParentContext(IFaceletPageContext parentContext) {
		this.parentContext = parentContext;
	}
}
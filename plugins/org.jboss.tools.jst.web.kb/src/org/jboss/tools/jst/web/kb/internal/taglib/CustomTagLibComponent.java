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
package org.jboss.tools.jst.web.kb.internal.taglib;

import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.PageProcessor;
import org.jboss.tools.jst.web.kb.taglib.IAttribute;
import org.jboss.tools.jst.web.kb.taglib.IComponent;
import org.jboss.tools.jst.web.kb.taglib.ICustomTagLibComponent;
import org.jboss.tools.jst.web.kb.taglib.ITagLibrary;

/**
 * @author Alexey Kazakov
 */
public class CustomTagLibComponent extends AbstractComponent implements ICustomTagLibComponent {

	protected boolean extended = true;
	protected CustomTagLibrary parentTagLib;

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IComponent#isExtended()
	 */
	@Override
	public boolean isExtended() {
		return extended;
	}

	/**
	 * @param extended
	 */
	public void setExtended(boolean extended) {
		this.extended = extended;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.IComponent#getTagLib()
	 */
	@Override
	public ITagLibrary getTagLib() {
		return parentTagLib;
	}

	/**
	 * @param parentTagLib the parent tag lib to set
	 */
	public void setParentTagLib(CustomTagLibrary parentTagLib) {
		this.parentTagLib = parentTagLib;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.AbstractComponent#checkExtended(org.jboss.tools.jst.web.kb.taglib.IAttribute, org.jboss.tools.jst.web.kb.IPageContext, org.jboss.tools.jst.web.kb.KbQuery)
	 */
	@Override
	protected boolean checkExtended(IAttribute attribute, IPageContext context, KbQuery query) {
		if(!attribute.isExtended()) {
			return true;
		}
		IComponent[] parentComponents = PageProcessor.getInstance().getComponents(query, context, false);
		for (IComponent component : parentComponents) {
			IAttribute at = component.getAttribute(attribute.getName());
			if(at!=null && !at.isExtended()) {
				return true;
			}
		}
		return false;
	}
}
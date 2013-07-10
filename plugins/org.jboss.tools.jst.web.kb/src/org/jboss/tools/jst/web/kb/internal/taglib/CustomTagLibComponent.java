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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.PageProcessor;
import org.jboss.tools.jst.web.kb.taglib.IAttribute;
import org.jboss.tools.jst.web.kb.taglib.IAttributeProvider;
import org.jboss.tools.jst.web.kb.taglib.IComponent;
import org.jboss.tools.jst.web.kb.taglib.ICustomTagLibComponent;
import org.jboss.tools.jst.web.kb.taglib.ITagLibrary;

/**
 * @author Alexey Kazakov
 */
public class CustomTagLibComponent extends AbstractComponent implements ICustomTagLibComponent {

	protected boolean extended = true;
	protected CustomTagLibrary parentTagLib;
	protected IAttributeProvider[] providers;

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.AbstractComponent#getAttribute(java.lang.String)
	 */
	@Override
	public IAttribute[] getAttributes(KbQuery query, String name) {
		if(providers!=null) {
			List<IAttribute> result = new ArrayList<IAttribute>();
			for (IAttributeProvider provider : providers) {
				IAttribute attribute = provider.getAttribute(query, name);
				if(attribute!=null) {
					result.add(attribute);
				}
			}
			return result.toArray(new IAttribute[result.size()]);
		}
		return super.getAttributes(query, name);
	}

	@Override
	public IAttribute[] getAttributes(KbQuery query) {
		if(providers!=null) {
			List<IAttribute> result = new ArrayList<IAttribute>();
			for (IAttributeProvider provider : providers) {
				IAttribute[] attributes = provider.getAttributes(query);
				for (IAttribute attribute: attributes) {
					result.add(attribute);
				}
			}
			return result.toArray(new IAttribute[result.size()]);
		}
		return super.getAttributes(query);
	}
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
		Set<String> cachedAttributes = query.getCachedAttributes();
		if(cachedAttributes == null) {
			cachedAttributes = new HashSet<String>();
			query.setCachedAttributes(cachedAttributes);
			IComponent[] parentComponents = PageProcessor.getInstance().getComponents(query, context, false);
			for (IComponent component : parentComponents) {
				IAttribute[] ats = component.getAttributes(query);
				for (IAttribute at : ats) {
					if(!at.isExtended()) {
						cachedAttributes.add(at.getName());
					}
				}
			}
		}
		return cachedAttributes.contains(attribute.getName());
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.ICustomTagLibComponent#getProvider()
	 */
	@Override
	public IAttributeProvider[] getProviders() {
		return providers;
	}

	public void setProviders(IAttributeProvider[] providers) {
		this.providers = providers;
		if(providers!=null) {
			for (IAttributeProvider provider : providers) {
				provider.setComponent(this);
			}
		}
	}
}
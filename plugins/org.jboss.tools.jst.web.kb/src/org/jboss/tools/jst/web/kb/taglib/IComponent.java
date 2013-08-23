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
package org.jboss.tools.jst.web.kb.taglib;

import org.eclipse.core.resources.IResource;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.IProposalProcessor;
import org.jboss.tools.jst.web.kb.KbQuery;

/**
 * @author Alexey Kazakov
 */
public interface IComponent extends IProposalProcessor {

	/**
	 * @return component name
	 */
	String getName();

	/**
	 * @return description
	 */
	String getDescription();

	/**
	 * @return true if the tag can have a body
	 */
	boolean canHaveBody();

	/**
	 * @return the component type
	 */
	String getComponentType();

	/**
	 * @return the component class name
	 */
	String getComponentClass();

	/**
	 * Returns all attributes declared in current tag library
	 * for this component. Extension attributes are not excluded.
	 * For custom tag libraries that declare attribute providers,
	 * method getAttributes(KbQuery) should be rather used
	 * since attribute providers should be accessed with query.
	 * @return all attributes of this component
	 */
	IAttribute[] getAttributes();

	/**
	 * Returns a list of attributes matching nameTemplate,
	 * with extension attributes excluded if they do not 
	 * extend a 'normal' attribute definition.
	 * @param nameTemplate
	 * @return attributes with names which start with given template.
	 */
	IAttribute[] getAttributes(String nameTemplate);

	/**
	 * @return all required attributes of this component
	 */
	IAttribute[] getRequiredAttributes();

	/**
	 * @return all preferable attributes of this component
	 */
	IAttribute[] getPreferableAttributes();

	/**
	 * @deprecated Use getAttributes(KbQuery query, String name) instead
	 * @param name
	 * @return attribute by name
	 */
	@Deprecated
	IAttribute getAttribute(String name);

	/**
	 * Returns all attributes with the given name declared 
	 * for the component in current tag library.
	 * Extension attributes are not excluded.
	 * Default implementation returns same result as getAttributes().
	 * For custom custom tag library that defines attribute 
	 * providers, they are queried, while getAttributes() 
	 * in that case may return empty list.
	 * @deprecated Use IContextComponent.getAttributes(IPageContext context, KbQuery query, String name) instead
	 * @param query
	 * @param name
	 * @return attributes by name
	 */
	@Deprecated
	IAttribute[] getAttributes(KbQuery query, String name);

	/**
	 * Return list of attributes with extension attributes 
	 * excluded if they do not extend a 'normal' attribute definition.
	 * @deprecated Use IContextComponent.getAttributes(IPageContext context, KbQuery query, boolean includeExtensions) instead
	 * @param query
	 * @param context
	 * @return
	 */
	@Deprecated
	public IAttribute[] getAttributes(KbQuery query, IPageContext context);

	/**
	 * Facets are a feature of JSF only, they are included into 
	 * the base interface for the sake of common approach.
	 * 
	 * @return all facets of this component
	 */
	Facet[] getFacets();

	/**
	 * @param nameTemplate
	 * @return facets with names which start with given template.
	 */
	Facet[] getFacets(String nameTemplate);

	/**
	 * @param name
	 * @return facet by name
	 */
	Facet getFacet(String name);

	/**
	 * Returns "true" if the component is relevant only if this component exists in other tag-libs (tld, faclets, ...).
	 * If there are not any other components with the same name in other tag libs then this component should be ignored.
	 * @return
	 */
	boolean isExtended();

	/**
	 * @return parent tag lib.
	 */
	ITagLibrary getTagLib();

	/**
	 * Returns the resource where this component is defined. May be a jar file. 
	 * @return
	 */
	IResource getResource();
}
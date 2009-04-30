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

/**
 * @author Alexey Kazakov
 */
public interface FacesConfigTagLibrary extends TagLibrary {

	/**
	 * @param type
	 * @return component by type
	 */
	Component getComponentByType(String type);

	/**
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
}
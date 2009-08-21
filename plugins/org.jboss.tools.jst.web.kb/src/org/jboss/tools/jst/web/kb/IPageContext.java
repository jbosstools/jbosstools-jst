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
package org.jboss.tools.jst.web.kb;

import java.util.List;
import java.util.Map;

import org.eclipse.jface.text.IDocument;
import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.common.el.core.resolver.Var;
import org.jboss.tools.jst.web.kb.taglib.INameSpace;
import org.jboss.tools.jst.web.kb.taglib.ITagLibrary;

/**
 * Page context
 * @author Alexey Kazakov
 */
public interface IPageContext extends ELContext {

	/**
	 * Returns libraries which should be used in this context  
	 * @return
	 */
	ITagLibrary[] getLibraries();

	/**
	 * Returns resource bundles
	 * @return
	 */
	IResourceBundle[] getResourceBundles();

	/**
	 * Returns IDocument for source file
	 * @return
	 */
	IDocument getDocument();

	/**
	 * Returns "var" attributes which are available in particular offset.
	 * @param offset
	 * @return
	 */
	Var[] getVars(int offset);

	/**
	 * Returns map of name spaces which are set in particular offset.
	 * Key is URI of name space.
	 * @return
	 */
	Map<String, List<INameSpace>> getNameSpaces(int offset);
}
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

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IDocument;
import org.jboss.tools.common.el.core.resolver.ELResolverFactoryManager;
import org.jboss.tools.jst.web.kb.internal.JspContextImpl;

/**
 * @author Alexey Kazakov
 */
public class PageContextFactory {

	/**
	 * Creates a page context for given resource and offset.
	 * @param file JSP or Facelet
	 * @param offset
	 * @return
	 */
	public static IPageContext createPageContext(IFile file, int offset) {
		// TODO
		return null;
	}

	/**
	 * Creates a jsp context for given resource and offset.
	 * @param file JSP
	 * @param offset
	 * @return
	 */
	public static IPageContext createJSPContext(IFile file, int offset) {
		JspContextImpl context = new JspContextImpl();
//		context.s
		// TODO
		return null;
	}

	/**
	 * Creates a jsp context for given resource, document and offset.
	 * @param file JSP
	 * @param offset
	 * @param document
	 * @return
	 */
	public static IPageContext createJSPContext(IFile file, IDocument document, int offset) {
		JspContextImpl context = new JspContextImpl();
		context.setDocument(document);
		context.setElResolvers(ELResolverFactoryManager.getInstance().getResolvers(file));
//		context.s
		// TODO
		return null;
	}

	/**
	 * Creates a facelet context for given resource and offset.
	 * @param file Facelet
	 * @param offset
	 * @return
	 */
	public static IFaceletPageContext createFaceletPageContext(IFile file, int offset) {
		// TODO
		return null;
	}
}
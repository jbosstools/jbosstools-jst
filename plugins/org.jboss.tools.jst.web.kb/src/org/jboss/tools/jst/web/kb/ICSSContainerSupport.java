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

import org.eclipse.core.resources.IFile;
import org.jboss.tools.jst.web.kb.PageContextFactory.CSSStyleSheetDescriptor;

/**
 * The interface defines the methods to collect
 * CSS Stylesheets used within the page.
 * 
 * @author Victor Rubezhny
 *
 */
public interface ICSSContainerSupport {

	/**
	 * Adds the CSS StyleSheet object found within the page
	 * 
	 * @param cssStyleSheet
	 * @param source
	 */
	void addCSSStyleSheetDescriptor(CSSStyleSheetDescriptor cssStyleSheet);
	
	IFile getResource();
	
	/**
	 * Returns the list of all the collected CSS StyleSheet objects
	 * 
	 * @return
	 */
	List<CSSStyleSheetDescriptor> getCSSStyleSheetDescriptors();
}

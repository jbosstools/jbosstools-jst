/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.jst.jsp.outline.cssdialog.common;

import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IDocument;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;

/**
 * @author Sergey Dzmitrovich
 * 
 */
public interface ICSSDialogModel {

	public String addCSSRule(String selector);

	public String getCSSStyleText(String selectorLabel);
	
	public String getCSSRuleText(String selectorLabel);
	
	public IDocument getDocument();

	public IFile getFile();

	public IndexedRegion getIndexedRegion(String selectorLabel);

	public String getSelectorLabel(int offset);

	public List<String> getSelectorLabels();

	public void reinit();

	public void release();

	public void save();

	public void setFile(IFile file);

	public void updateCSSStyle(String selectorLabel, StyleAttributes styleAttributes);
	
	public Map<String, String> getClassProperties(final String selectorLabel);

}

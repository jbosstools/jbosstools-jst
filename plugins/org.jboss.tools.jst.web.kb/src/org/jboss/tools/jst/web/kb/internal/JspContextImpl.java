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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.tools.jst.web.kb.IIncludedContextSupport;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.IResourceBundle;
import org.jboss.tools.jst.web.kb.internal.taglib.NameSpace;
import org.jboss.tools.jst.web.kb.taglib.INameSpace;

/**
 * JSP page context
 * @author Alexey Kazakov
 */
public class JspContextImpl extends XmlContextImpl {
	protected List<IPageContext> fIncludedContexts = null;

	
	
	@Override
	public void addIncludedContext(IPageContext includedContext) {
		if (fIncludedContexts == null) {
			fIncludedContexts = new ArrayList<IPageContext>();
		}
		fIncludedContexts.add(includedContext);
	}

	@Override
	public List<IPageContext> getIncludedContexts() {
		return fIncludedContexts;
	}

	@Override
	public Map<String, List<INameSpace>> getNameSpaces(int offset) {
		Map<String, List<INameSpace>> superNameSpaces = super.getNameSpaces(offset);
		
		List<INameSpace> fakeForHtmlNS = new ArrayList<INameSpace>();
		fakeForHtmlNS.add(new NameSpace("", "")); //$NON-NLS-1$ //$NON-NLS-2$
		superNameSpaces.put("", fakeForHtmlNS); //$NON-NLS-1$
		
		return superNameSpaces;
	}
	
}
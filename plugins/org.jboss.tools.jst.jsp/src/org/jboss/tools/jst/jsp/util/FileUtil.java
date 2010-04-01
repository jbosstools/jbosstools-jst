/*******************************************************************************
 * Copyright (c) 2007-2010 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.jsp.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;

/**
 * 
 * @author yzhishko
 *
 */

public class FileUtil {

	public static IJavaElement searchForClass(IJavaProject javaProject, String className) throws JavaModelException {
//		 Get the search pattern
	    SearchPattern pattern = SearchPattern.createPattern(className, IJavaSearchConstants.TYPE, IJavaSearchConstants.DECLARATIONS, SearchPattern.R_EXACT_MATCH | SearchPattern.R_CASE_SENSITIVE);
	    // Get the search scope
	    IJavaSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaElement[] { javaProject });

	    final List<SearchMatch> matches = new ArrayList<SearchMatch>();
	    // Get the search requestor
	    SearchRequestor requestor = new SearchRequestor() {
			public void acceptSearchMatch(SearchMatch match) throws CoreException {
				matches.add(match);
			}
	    };

	    // Search
	    SearchEngine searchEngine = new SearchEngine();
	    try {
	    	searchEngine.search(pattern, new SearchParticipant[] {SearchEngine.getDefaultSearchParticipant()}, scope, requestor, null);
	    } catch (CoreException ex) {
	    	// Ignore
//	    	ExtensionsPlugin.log(ex);
	    }
	    for (Iterator i = matches.iterator(); i != null && i.hasNext();) {
	    	IJavaElement element = (IJavaElement)((SearchMatch)i.next()).getElement();
	    	String classQualifiedName = getQualifiedClassName(element);
	    	if (className.equals(classQualifiedName)) 
	    		return element;
	    }
	    return javaProject.findType(className, new NullProgressMonitor());
	}

	private static String getQualifiedClassName(IJavaElement element) {
		if(element instanceof IType) {
			return ((IType)element).getFullyQualifiedName('.');
		}
		return null;
	}
	
}

/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.jsp.outline;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.text.IDocument;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.taglib.CustomTagLibManager;
import org.jboss.tools.jst.web.kb.taglib.ICustomTagLibrary;
import org.osgi.framework.Bundle;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class CategoryProviderFactory {
	private static final CategoryProviderFactory instance = new CategoryProviderFactory();

	public static CategoryProviderFactory getInstance() {
		return instance;
	}

	private List<CategoryDescriptor> knownDescriptors = new ArrayList<CategoryDescriptor>();
	/**
	 * While we have the only provider JQueryCategoryProvider, let us just add it explicitly.
	 * As soon as other providers appear, classes will be obtained from an extension point. 
	 */
	private CategoryProviderFactory() {
		Bundle b = Platform.getBundle("org.jboss.tools.jst.web.ui");
		if(b != null) {
			try {
				Class<?> c = b.loadClass("org.jboss.tools.jst.web.ui.internal.properties.angular.AngularJSPropertySetViewer");
				knownDescriptors.add(new CategoryDescriptor("AngularJS", "angularJS", AngularCategoryFilter.class, c));
			} catch (ClassNotFoundException e) {
			}
			try {
				Class<?> c = b.loadClass("org.jboss.tools.jst.web.ui.internal.properties.jquery.JQueryPropertySetViewer");
				knownDescriptors.add(new CategoryDescriptor("jQuery", "jQueryMobile",  JQueryCategoryFilter.class, c));
			} catch (ClassNotFoundException e) {
			}
			try {
				Class<?> c = b.loadClass("org.jboss.tools.jst.web.ui.internal.properties.html.HTMLPropertySetViewer");
				knownDescriptors.add(new CategoryDescriptor("HTML", "htmlFile", c));
			} catch (ClassNotFoundException e) {
			}
		}
	}

	public CategoryDescriptor[] getCategoryDescriptors(IPageContext context) {
		List<IFormCategoryDescriptor> result = new ArrayList<IFormCategoryDescriptor>();
		for (CategoryDescriptor d: knownDescriptors) {
			ICustomTagLibrary l = findLibrary(d.getURI());
			if(l != null && l.getRecognizer() != null
					&& l.getRecognizer().shouldBeLoaded(l, context)) {
				result.add(d);
			}
		}
		return result.toArray(new CategoryDescriptor[0]);
	}

	static ICustomTagLibrary findLibrary(String uri) {
		for (ICustomTagLibrary l: CustomTagLibManager.getInstance().getLibraries()) {
			if(uri.equals(l.getURI())) return l;			
		}
		return null;
	}

	@Deprecated
	public ICategoryProvider[] getProviders(IDocument document, KbQuery kbQuery) {
		return new ICategoryProvider[0];
	}
}

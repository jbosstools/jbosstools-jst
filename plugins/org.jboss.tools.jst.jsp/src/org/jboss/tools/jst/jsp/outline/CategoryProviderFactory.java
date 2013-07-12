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

import org.eclipse.jface.text.IDocument;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.web.kb.KbQuery;

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

	private List<Class<? extends ICategoryProvider>> knownProviders = new ArrayList<Class<? extends ICategoryProvider>>();

	/**
	 * While we have the only provider JQueryCategoryProvider, let us just add it explicitly.
	 * As soon as other providers appear, classes will be obtained from an extension point. 
	 */
	private CategoryProviderFactory() {
		knownProviders.add(JQueryCategoryProvider.class);
	}

	public ICategoryProvider[] getProviders(IDocument document, KbQuery kbQuery) {
		List<ICategoryProvider> categoryProviders = new ArrayList<ICategoryProvider>();
		for (Class<? extends ICategoryProvider> cls: knownProviders) {
			try {
				ICategoryProvider categoryProvider = cls.newInstance();
				if(categoryProvider.init(document, kbQuery)) {
					categoryProviders.add(categoryProvider);
				}
			} catch (InstantiationException e) {
				JspEditorPlugin.getDefault().logError(e);
			} catch (IllegalAccessException e) {
				JspEditorPlugin.getDefault().logError(e);
			}
		}
		return categoryProviders.isEmpty() ? new ICategoryProvider[0] 
				: categoryProviders.toArray(new ICategoryProvider[categoryProviders.size()]);
	}
}

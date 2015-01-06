/******************************************************************************* 
 * Copyright (c) 2013 - 2015 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.internal.editor.outline;

import org.jboss.tools.jst.web.ui.WebUiPlugin;
import org.jboss.tools.jst.web.ui.internal.editor.outline.JSPPropertySourceAdapter.ICategoryFilter;
import org.jboss.tools.jst.web.ui.internal.properties.IPropertySetViewer;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class CategoryDescriptor implements IFormCategoryDescriptor {
	String name;
	String uri;
	Class<? extends ICategoryFilter> filterClass;
	Class<? extends IPropertySetViewer> uiClass;

	public CategoryDescriptor(String name, String uri, Class<? extends IPropertySetViewer> uiClass) {
		this.name = name;
		this.uiClass = uiClass;
		this.uri = uri;
	}

	public CategoryDescriptor(String name, String uri, Class<? extends ICategoryFilter> filterClass, Class<? extends IPropertySetViewer> uiClass) {
		this(name, uri, uiClass);
		this.filterClass = filterClass;
	}

	public String getName() {
		return name;
	}

	public Class<? extends IPropertySetViewer> getUIClass() {
		return uiClass;
	}

	public String getURI() {
		return uri;
	}

	public ICategoryFilter createCategoryFilter() {
		if(filterClass == null) return null;
		try {
			return filterClass.newInstance();
		} catch (IllegalAccessException e) {
			WebUiPlugin.getDefault().logError(e);
		} catch (InstantiationException e) {
			WebUiPlugin.getDefault().logError(e);
		}
		return null;
	}
}

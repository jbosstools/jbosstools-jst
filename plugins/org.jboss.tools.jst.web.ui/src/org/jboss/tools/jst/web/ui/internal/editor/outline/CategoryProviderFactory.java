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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.text.IDocument;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.taglib.CustomTagLibManager;
import org.jboss.tools.jst.web.kb.taglib.ICustomTagLibrary;
import org.jboss.tools.jst.web.ui.WebUiPlugin;
import org.jboss.tools.jst.web.ui.internal.editor.outline.JSPPropertySourceAdapter.ICategoryFilter;
import org.jboss.tools.jst.web.ui.internal.properties.IPropertySetViewer;
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteGroup;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class CategoryProviderFactory {
	private static String POINT_ID = "org.jboss.tools.jst.web.ui.CategoryDescriptor"; //$NON-NLS-1$
	private static final CategoryProviderFactory instance = new CategoryProviderFactory();

	public static CategoryProviderFactory getInstance() {
		return instance;
	}

	private List<CategoryDescriptor> knownDescriptors = new ArrayList<CategoryDescriptor>();

	private CategoryProviderFactory() {
		IExtensionPoint point = Platform.getExtensionRegistry().getExtensionPoint(POINT_ID);
		for (IConfigurationElement element : point.getConfigurationElements()) {
			String name = element.getAttribute("name"); //$NON-NLS-1$
			if(name == null){
				WebUiPlugin.getDefault().logError("Attribute \"name\" should be defined in extension point " + element.getNamespaceIdentifier());
			}
			String uri = element.getAttribute("uri"); //$NON-NLS-1$
			if(uri == null){
				WebUiPlugin.getDefault().logError("Attribute \"uri\" should be defined in extension point " + element.getNamespaceIdentifier());
			}
			Class<? extends IPropertySetViewer> propertySetViewerClass = getPropertySetViewerClass(element);
			if(propertySetViewerClass == null){
				WebUiPlugin.getDefault().logError("Attribute \"property-set-viewer-class\" should be defined in extension point " + element.getNamespaceIdentifier());
			}
			Class<? extends ICategoryFilter> categoryFilterClass = getCategoryFilterClass(element);
			if(name != null && uri != null && propertySetViewerClass != null){
				knownDescriptors.add(new CategoryDescriptor(name, uri,  categoryFilterClass, propertySetViewerClass));
			}
		}
	}
	
	private static Class<? extends ICategoryFilter> getCategoryFilterClass(IConfigurationElement element) {
		if(element.getAttribute("category-filter-class") != null){
			try {
				Object o = element.createExecutableExtension("category-filter-class"); //$NON-NLS-1$
				if(o instanceof ICategoryFilter) {
					return (Class<? extends ICategoryFilter>)o.getClass();
				} else {
					WebUiPlugin.getDefault().logError("Category Filter " + element.getAttribute("category-filter-class") + " should implement ICategoryFilter.");
				}
			} catch(CoreException e) {
				WebUiPlugin.getDefault().logError(e);
			}
		}
		return null;
	}

	private static Class<? extends IPropertySetViewer> getPropertySetViewerClass(IConfigurationElement element) {
		try {
			Object o = element.createExecutableExtension("property-set-viewer-class"); //$NON-NLS-1$
			if(o instanceof IPropertySetViewer) {
				return (Class<? extends IPropertySetViewer>)o.getClass();
			} else {
				WebUiPlugin.getDefault().logError("Property Set Viewer " + element.getAttribute("property-set-viewer-class") + " should implement IPropertySetViewer.");
			}
		} catch(CoreException e) {
			WebUiPlugin.getDefault().logError(e);
		}
		return null;
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

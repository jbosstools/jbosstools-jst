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
package org.jboss.tools.jst.jsp.i18n;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.RegistryFactory;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;
import org.jboss.tools.jst.jsp.JspEditorPlugin;

/**
 * Aggregates extensions of all {@code localeProvider} extension points.
 * This class is a singleton.
 * <P>
 * Typical use of this class:
 * <code>MainLocaleProvider.getInstance().getLocale(editor)</code>
 *
 * @author yradtsevich
 */
public class MainLocaleProvider implements ILocaleProvider {
	private static final String ELEMENT_NATURE = "nature"; //$NON-NLS-1$
	private static final String ATTRIBUTE_ID = "id"; //$NON-NLS-1$
	private static final String ATTRIBUTE_CLASS = "class"; //$NON-NLS-1$
	private static final String ELEMENT_LOCALE_PROVIDER = "localeProvider"; //$NON-NLS-1$
	private Map<String, ? extends List<IExtension>> natureToExtensions;
	private Map<IExtension, ILocaleProvider> extensionToProvider
			= new HashMap<IExtension, ILocaleProvider>();
	private IExtensionPoint extensionPoint;
	private static MainLocaleProvider instance;

	private String localeString = ""; //$NON-NLS-1$
	
	/**
	 *  The constructor
	 */
	private MainLocaleProvider() {
		extensionPoint = RegistryFactory.getRegistry().getExtensionPoint(JspEditorPlugin.EXTESION_POINT_LOCALE_PROVIDER);
		initNatureExtensionsMap();
	}

	/**
	 * Returns a shared instance of {@link MainLocaleProvider}.
	 */
	public static MainLocaleProvider getInstance() {
		if (instance == null) {
			instance = new MainLocaleProvider();
		}
		return instance;
	}
	
	/**
	 * Tries to determine the locale of the {@code editor} using
	 * {@code localeProvider} extensions. Returns the default
	 * system locale if nothing found (never returns {@code null}).
	 */
	public Locale getLocale(ITextEditor editor) {
		IEditorInput editorInput = editor.getEditorInput();
		if (editorInput instanceof IFileEditorInput) {
			IProject project = ((IFileEditorInput)editorInput)
					.getFile().getProject();
	
			try {
				if(project.isAccessible()) {
					String[] natures = project.getDescription().getNatureIds();
					for (String natureId : natures) {
						for (ILocaleProvider provider : getProviders(natureId)) {
							Locale locale = provider.getLocale(editor);
							if (locale != null) {
								localeString = provider.getLocaleString();
								return locale;
							}
						}
					}
				}
			} catch (CoreException e) {
				JspEditorPlugin.getPluginLog().logError(
						MessageFormat.format("Error in getting locale for {0}.",((IFileEditorInput)editorInput)
								.getFile().getLocation().toOSString()), e);
			}
		}
	
		return Locale.getDefault();
	}

	private void initNatureExtensionsMap() {
		Map<String, ArrayList<IExtension>> natureExtensionsMap
				= new HashMap<String, ArrayList<IExtension>>();
		
		IExtension[] extensions
				= extensionPoint.getExtensions();

		for (IExtension extension : extensions) {
			IConfigurationElement[] elements
					= extension.getConfigurationElements();
			for (IConfigurationElement element : elements) {
				if (ELEMENT_NATURE.equals(element.getName())) {
					String natureId = element.getAttribute(ATTRIBUTE_ID);
					ArrayList<IExtension> extensionList
							= natureExtensionsMap.get(natureId);
	
					if (extensionList == null) {
						extensionList = new ArrayList<IExtension>();
						natureExtensionsMap.put(natureId, extensionList);
					}
	
					extensionList.add(extension);
				}
			}
		}

		for (ArrayList<IExtension> configurationsList
				: natureExtensionsMap.values()) {
			configurationsList.trimToSize();
		}

		this.natureToExtensions = natureExtensionsMap;
	}

	/**
	 * Returns all available instances of {@link ILocaleProvider} for given
	 * {@code natureId}. It loads extensions using lazy initialization. 
	 */
	private List<ILocaleProvider> getProviders(String natureId) {
		List<IExtension> extensions = natureToExtensions.get(natureId);
		if (extensions == null) {
			return Collections.emptyList();
		}

		List<ILocaleProvider> providers
				= new ArrayList<ILocaleProvider>(extensions.size());

		for (IExtension extension : extensions) {
			ILocaleProvider provider
					= extensionToProvider.get(extension);
			
			if (provider == null) {
				provider = createLocaleProvider(extension);					
			}
			if (provider != null) {
				providers.add(provider);
			}
		}

		return providers;		
	}

	private ILocaleProvider createLocaleProvider(IExtension extension) {
		ILocaleProvider provider = null;
		try {
			for (IConfigurationElement element
					: extension.getConfigurationElements()) {
				if (ELEMENT_LOCALE_PROVIDER.equals(element.getName())) {
					provider = (ILocaleProvider) element
					.createExecutableExtension(ATTRIBUTE_CLASS);
					extensionToProvider.put(extension, provider);
					break;
				}
			}
		} catch (InvalidRegistryObjectException e) {
			JspEditorPlugin.getPluginLog().logError(
					"The extension registry object " //$NON-NLS-1$
					+ "is no longer valid.", e); //$NON-NLS-1$
		} catch (CoreException e) {
			JspEditorPlugin.getPluginLog().logError(
					"CoreException occured.", e); //$NON-NLS-1$
		}
		return provider;
	}

	public String getLocaleString() {
		return localeString;
	}
	
}

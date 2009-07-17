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
package org.jboss.tools.jst.web.kb.taglib;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.jboss.tools.jst.web.kb.WebKbPlugin;
import org.jboss.tools.jst.web.kb.internal.taglib.CustomTagLibAttribute;
import org.jboss.tools.jst.web.kb.internal.taglib.CustomTagLibrary;
import org.jboss.tools.jst.web.kb.internal.taglib.HTMLTagLibrary;
import org.osgi.framework.Bundle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * @author Alexey Kazakov
 */
public class CustomTagLibManager {

	public static final String FACELETS_UI_TAG_LIB_URI = "http://java.sun.com/jsf/facelets"; //$NON-NLS-1$
	public static final String FACELETS_HTML_TAG_LIB_URI = "http://www.w3.org/1999/xhtml/facelets"; //$NON-NLS-1$

	private static final CustomTagLibManager INSTANCE = new CustomTagLibManager();

	private ICustomTagLibrary[] libs = null;
	private CustomTagLibAttribute[] extensions = null;

	private CustomTagLibManager() {
	}

	/**
	 * @return an instance of this manager. 
	 */
	public static CustomTagLibManager getInstance() {
		return INSTANCE;
	}

	/**
	 * Returns all custom tag libraries which are defined via extension point org.jboss.tools.jst.web.kb.KbTagLib.
	 * @param project
	 * @param uri
	 * @return
	 */
	public ICustomTagLibrary[] getLibraries() {
		if(libs==null) {
			init();
		}
		return libs;
	}

	/**
	 * @return Array of attributes witch should be used only if they are present in other tag libraries. 
	 */
	public CustomTagLibAttribute[] getComponentExtensions() {
		if(extensions==null) {
			init();
		}
		return extensions;
	}

	private void init() {
		Set<ICustomTagLibrary> libSet = new HashSet<ICustomTagLibrary>();
		Set<CustomTagLibAttribute> extensionSet = new HashSet<CustomTagLibAttribute>();
        IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = registry.getExtensionPoint("org.jboss.tools.jst.web.kb.tagLib"); //$NON-NLS-1$
		if (extensionPoint != null) { 
			IExtension[] extensions = extensionPoint.getExtensions();
			for (int i=0; i<extensions.length; i++) {
				IExtension extension = extensions[i];
				IConfigurationElement[] elements = extension.getConfigurationElements();
				for(int j=0; j<elements.length; j++) {
					String elementName = elements[j].getName();
					if("tag-lib".equals(elementName)) { //$NON-NLS-1$
						String uri = elements[j].getAttribute("uri"); //$NON-NLS-1$
						String location = elements[j].getAttribute("location"); //$NON-NLS-1$
						String version = elements[j].getAttribute("version"); //$NON-NLS-1$
						String name = elements[j].getAttribute("name"); //$NON-NLS-1$
						Bundle sourcePlugin = Platform.getBundle(elements[j].getNamespaceIdentifier());
						File schemaLocation = null;
						try {
							String pluginPath = FileLocator.resolve(sourcePlugin.getEntry("/")).getPath(); //$NON-NLS-1$
							if(uri==null || uri.length()==0 || location==null || location.length()==0) {
								WebKbPlugin.getDefault().logWarning("Incorrect org.jboss.tools.jst.web.kb.KbTagLib extension in " + pluginPath + " plugin. URI or location can't be empty."); //$NON-NLS-1$ //$NON-NLS-2$
								continue;
							}
							schemaLocation = new File(pluginPath, location);
						} catch (IOException e) {
							WebKbPlugin.getDefault().logError(e);
							continue;
						}
						if(schemaLocation.isFile()) {
							CustomTagLibrary lib = FACELETS_HTML_TAG_LIB_URI.equals(uri)?new HTMLTagLibrary(schemaLocation, uri, version, name):new CustomTagLibrary(schemaLocation, uri, version, name);
							libSet.add(lib);
						} else {
							WebKbPlugin.getDefault().logWarning("Can't load KB schema: " + schemaLocation); //$NON-NLS-1$
						}
					} else if("component-extension".equals(elementName)) { //$NON-NLS-1$
						String location = elements[j].getAttribute("location"); //$NON-NLS-1$
						Bundle sourcePlugin = Platform.getBundle(elements[j].getNamespaceIdentifier());
						File schemaLocation = null;
						try {
							String pluginPath = FileLocator.resolve(sourcePlugin.getEntry("/")).getPath(); //$NON-NLS-1$
							if(location==null || location.length()==0) {
								WebKbPlugin.getDefault().logWarning("Incorrect org.jboss.tools.jst.web.kb.KbTagLib extension in " + pluginPath + " plugin. Location can't be empty."); //$NON-NLS-1$ //$NON-NLS-2$
								continue;
							}
							schemaLocation = new File(pluginPath, location);
						} catch (IOException e) {
							WebKbPlugin.getDefault().logError(e);
							continue;
						}
						if(schemaLocation.isFile()) {
							Document document = null;
							try {
								DocumentBuilder builder = CustomTagLibrary.createDocumentBuilder(false);
								document = builder.parse(schemaLocation);
							} catch (SAXException e) {
								WebKbPlugin.getDefault().logError(e);
							} catch (IOException e) {
								WebKbPlugin.getDefault().logError(e);
							} catch (ParserConfigurationException e) {
								WebKbPlugin.getDefault().logError(e);
							}
							Element root = document.getDocumentElement();
							CustomTagLibAttribute[] attributes = CustomTagLibrary.getAttributes(root);
							for (int k = 0; k < attributes.length; k++) {
								extensionSet.add(attributes[k]);
							}
						} else {
							WebKbPlugin.getDefault().logWarning("Can't load KB schema: " + schemaLocation); //$NON-NLS-1$
						}
					}
				}
			}
		}
		libs = libSet.toArray(new ICustomTagLibrary[0]);
		extensions = extensionSet.toArray(new CustomTagLibAttribute[0]);
	}
}
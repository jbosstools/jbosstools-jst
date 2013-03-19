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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.runtime.CoreException;
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
	
	private InputStream getInputStream(String location) {
		try {
			URL url = FileLocator.resolve(new URL(location));
			return url.openConnection().getInputStream();
		} catch(IOException ex) {
			WebKbPlugin.getDefault().logError(ex);
			return null;
		}
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
					String location = elements[j].getAttribute("location");
					if(CustomTagLibrary.TAG_LIB.equals(elementName)) {
						String uri = elements[j].getAttribute("uri"); //$NON-NLS-1$
						String version = elements[j].getAttribute("version"); //$NON-NLS-1$
						String name = elements[j].getAttribute("name"); //$NON-NLS-1$
						String recognizerClassName = elements[j].getAttribute("recognizer"); //$NON-NLS-1$
						ITagLibRecognizer recognizer = null;
						if(recognizerClassName!=null) {
							try {
								Object rec = elements[j].createExecutableExtension("recognizer");
								if(rec instanceof ITagLibRecognizer) {
									recognizer = (ITagLibRecognizer)rec;
								} else {
									WebKbPlugin.getDefault().logError("Custom Tag Lib recognizer (class name: " + recognizerClassName + ", contributer: " + elements[j].getContributor().getName() + ") must implement " + ITagLibRecognizer.class.getName());
								}
							} catch (CoreException e) {
								WebKbPlugin.getDefault().logError(e);
							}
						}
						ICustomTagLibrary lib = null;
						if(location!=null) {
							InputStream schemaStream = getInputStream(location);
							lib = FACELETS_HTML_TAG_LIB_URI.equals(uri)?new HTMLTagLibrary(elements[j].getContributor().getName(), schemaStream, uri, version, name):new CustomTagLibrary(elements[j].getContributor().getName(), schemaStream, uri, version, name);
						} else {
							try {
								Object cls = elements[j].createExecutableExtension("class");
								if(cls instanceof ICustomTagLibrary) {
									lib = (ICustomTagLibrary)cls;
								} else {
									WebKbPlugin.getDefault().logError("Custom Tag Lib (class name: " + cls + " registred in plugin.xml, contributer: " + elements[j].getContributor().getName() + ") must implement " + ICustomTagLibrary.class.getName());
								}
							} catch (CoreException e) {
								WebKbPlugin.getDefault().logError(e);
							}
						}
						lib.setRecognizer(recognizer);
						libSet.add(lib);
					} else if(CustomTagLibrary.COMPONET_EXTENSION.equals(elementName)) {
						InputStream schemaStream = getInputStream(location);
						Document document = null;
						try {
							DocumentBuilder builder = CustomTagLibrary.createDocumentBuilder(false);
							document = builder.parse(schemaStream);
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
					}
				}
			}
		}
		libs = libSet.toArray(new ICustomTagLibrary[0]);
		extensions = extensionSet.toArray(new CustomTagLibAttribute[0]);
	}
}
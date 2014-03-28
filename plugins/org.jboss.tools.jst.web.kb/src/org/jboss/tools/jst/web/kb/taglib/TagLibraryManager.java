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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.codehaus.plexus.util.IOUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.wst.xml.core.internal.XMLCorePlugin;
import org.eclipse.wst.xml.core.internal.catalog.Catalog;
import org.eclipse.wst.xml.core.internal.catalog.CatalogEntry;
import org.eclipse.wst.xml.core.internal.catalog.provisional.ICatalog;
import org.eclipse.wst.xml.core.internal.catalog.provisional.ICatalogElement;
import org.eclipse.wst.xml.core.internal.catalog.provisional.INextCatalog;
import org.jboss.tools.jst.web.kb.IKbProject;
import org.jboss.tools.jst.web.kb.KbProjectFactory;
import org.jboss.tools.jst.web.kb.WebKbPlugin;

/**
 * @author Alexey Kazakov
 */
public class TagLibraryManager {

	/**
	 * Returns all tag libraries which have given URI and which are available in the project.
	 * @param project
	 * @param uri
	 * @return
	 */
	public static ITagLibrary[] getLibraries(IProject project, String uri) {
		IKbProject kbProject = KbProjectFactory.getKbProject(project, true);
		if(kbProject == null) {
			return new ITagLibrary[0];
		}
		return uri==null?kbProject.getProjectTagLibraries():kbProject.getTagLibraries(uri);
	}

	/**
	 * Returns all the tag libraries which defined in the resource (TLD, facelet tag lib, etc.)
	 * @param file
	 * @return
	 */
	public static ITagLibrary[] getLibraries(IResource resource) {
		IKbProject kbProject = KbProjectFactory.getKbProject(resource.getProject(), true);
		if(kbProject == null) {
			return new ITagLibrary[0];
		}
		return kbProject.getTagLibraries(resource.getFullPath());
	}

	/**
	 * Returns all tag libraries which are available in the project.
	 * @param project
	 * @return
	 */
	public static ITagLibrary[] getLibraries(IProject project) {
		return getLibraries(project, null);
	}

	/**
	 * Returns TLD which are available for all the projects
	 * even if the lib are not in the project classpath.
	 * This TLD should be registered in XML Catalog via plugin.xml.
	 * @param uri
	 * @return
	 */
	public static File getStaticTLD(String uri) {
		try {
			if (uri != null) {
	        	String id = XMLCorePlugin.getDefault().getDefaultXMLCatalog().resolveURI(uri);
	        	if(id==null) {
	        		id = XMLCorePlugin.getDefault().getDefaultXMLCatalog().resolveSystem(uri);
	        	}
	        	if(id==null) {
	        		id = XMLCorePlugin.getDefault().getDefaultXMLCatalog().resolvePublic(uri, uri);
	        	}
	        	if(id!=null) {
	        		File file = convertUriToFile(id);
	        		if(file.exists()) {
	        			return file;
	        		}
	        	}
			}
		} catch (IOException e) {
			WebKbPlugin.getDefault().logError(e);
		}

		return null;
	}

	/**
	 * Returns all the tag libraries which are available for all the projects
	 * even if the lib are not in the project classpath.
	 * These libs should be registered in XML Catalog via plugin.xml.
	 * @param uri
	 * @return
	 */
	public static List<File> getStaticTagLibs() {
		List<File> files = new ArrayList<File>();
		try {
			INextCatalog[] catalogs = XMLCorePlugin.getDefault().getDefaultXMLCatalog().getNextCatalogs();
			for (INextCatalog catalog : catalogs) {
				ICatalog c = catalog.getReferencedCatalog();
				if(c instanceof Catalog) {
					ICatalogElement[] elements = ((Catalog)c).getCatalogElements();
					for (ICatalogElement element : elements) {
						if(element instanceof CatalogEntry) {
							CatalogEntry entry = (CatalogEntry)element;
							String uri = entry.getURI();
							if(uri!=null && (uri.endsWith(".tld") || (uri.endsWith(".xml") && uri.indexOf("taglib")>0)) && (uri.startsWith("file:") || uri.startsWith("jar:"))) {
				        		File file = convertUriToFile(uri);
				        		if(file!=null) {
				        			files.add(file);
				        		}
							}
						}
					}
				}
			}
		} catch (IOException e) {
			WebKbPlugin.getDefault().logError(e);
		}
		return files;
	}

	private static Map<String, File> tempFiles = new ConcurrentHashMap<String, File>();

	private static File convertUriToFile(String uri) throws IOException {
		File file = tempFiles.get(uri);
		if(file!=null && file.exists()) {
			return file;
		}

		URL url = new URL(uri);
		String filePath = url.getFile();
		file = new File(filePath);
		if(!file.exists()) {
			URLConnection c = url.openConnection();
			if(c instanceof JarURLConnection) {
				JarURLConnection connection = (JarURLConnection)c;
				JarFile jar = connection.getJarFile();
				JarEntry entry = connection.getJarEntry();

				File entryFile = new File(entry.getName());
				String name = entryFile.getName();
				String prefix = name;
				String sufix = null;
				int i = name.lastIndexOf('.');
				if(i>0 && i<name.length()) {
					prefix = name.substring(0, i);
					sufix = name.substring(i);
				}

				WebKbPlugin plugin = WebKbPlugin.getDefault();
				if(plugin != null) {
					//The plug-in instance can be null at shutdown, when the plug-in is stopped. 
					IPath path = plugin.getStateLocation();
					File tmp = new File(path.toFile(), "tmp"); //$NON-NLS-1$
					tmp.mkdirs();
					file = File.createTempFile(prefix, sufix, tmp);
					file.deleteOnExit();

					InputStream in = null;
					try {
						in = jar.getInputStream(entry);
						FileOutputStream out = new FileOutputStream(file);
						IOUtil.copy(in, out);
					} finally {
						IOUtil.close(in);
					}
				}
			}
		}
		if(file.exists()) {
			tempFiles.put(uri, file);
		} else {
			file = null;
		}
		return file;
	}
}
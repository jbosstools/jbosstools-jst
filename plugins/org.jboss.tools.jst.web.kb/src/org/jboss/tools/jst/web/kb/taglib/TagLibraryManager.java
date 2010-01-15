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
import java.net.URL;

import org.eclipse.core.resources.IProject;
import org.eclipse.wst.xml.core.internal.XMLCorePlugin;
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
		return uri==null?kbProject.getTagLibraries():kbProject.getTagLibraries(uri);
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
	        		File file = new File(new URL(id).getFile());
	        		if(file.exists()) {
	        			return file;
	        		}
	        	}
			}
		} catch (IOException e) {
			WebKbPlugin.getDefault().logError(e);
			return null;
		}

		return null;
	}
}
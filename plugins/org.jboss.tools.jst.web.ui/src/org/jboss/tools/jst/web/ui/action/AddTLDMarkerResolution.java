/******************************************************************************* 
 * Copyright (c) 2010 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.jst.web.ui.action;

import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.DocumentProviderRegistry;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.jboss.tools.jst.web.ui.WebUiPlugin;

/**
 * The Marker Resolution that adds tag lib declaration to jsp or xhtml file
 * 
 * @author Daniel Azarov
 *
 */
public class AddTLDMarkerResolution implements IMarkerResolution{
	
	public static HashMap<String, String> libs = new HashMap<String, String>();
	static{
		libs.put("s", "http://jboss.com/products/seam/taglib");  //$NON-NLS-1$//$NON-NLS-2$
		libs.put("ui", "http://java.sun.com/jsf/facelets"); //$NON-NLS-1$ //$NON-NLS-2$
		libs.put("f", "http://java.sun.com/jsf/core"); //$NON-NLS-1$ //$NON-NLS-2$
		libs.put("h", "http://java.sun.com/jsf/html"); //$NON-NLS-1$ //$NON-NLS-2$
		libs.put("rich", "http://richfaces.org/rich"); //$NON-NLS-1$ //$NON-NLS-2$
		libs.put("a4j", "http://richfaces.org/a4j"); //$NON-NLS-1$ //$NON-NLS-2$
		libs.put("a", "http://richfaces.org/a4j"); //$NON-NLS-1$ //$NON-NLS-2$
		libs.put("c", "http://java.sun.com/jstl/core"); //$NON-NLS-1$ //$NON-NLS-2$
		libs.put("jsp", "http://java.sun.com/JSP/Page"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public String getLabel() {
		return "Insert tag library defenition";
	}
	
	public static String getPrifix(String message){
		String prefix="";
		
		int start = message.indexOf("(");
		if(start < 0)
			return null;
		
		int end = message.indexOf(":", start);
		if(end < 0)
			return null;
		
		prefix = message.substring(start+1, end);
		
		return prefix;
	}
	
	public static IDocument getDocument(IFile file) {
		FileEditorInput input = new FileEditorInput(file);
		IDocumentProvider provider= DocumentProviderRegistry.getDefault().getDocumentProvider(input);
		try {
			provider.connect(input);
		} catch (CoreException e) {
			WebUiPlugin.getPluginLog().logError(e);
		}
		return provider.getDocument(input);
	}

	public void run(IMarker marker) {
		
	}
}
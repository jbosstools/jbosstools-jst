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

import java.util.Properties;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.jboss.tools.jst.jsp.jspeditor.dnd.PaletteTaglibInserter;
import org.jboss.tools.jst.web.ui.Messages;
import org.jboss.tools.jst.web.ui.WebUiPlugin;

/**
 * The Marker Resolution that adds tag lib declaration to jsp or xhtml file
 * 
 * @author Daniel Azarov
 *
 */
public class AddTLDMarkerResolution implements IMarkerResolution{
	private IDocumentProvider provider;
	private FileEditorInput input;
	private Properties properties;
	
	
	public AddTLDMarkerResolution(IDocumentProvider provider, FileEditorInput input, Properties properties){
		this.provider = provider;
		this.input = input;
		this.properties = properties;
	}

	public String getLabel() {
		return Messages.AddTLDMarkerResolution_Name;
	}
	
	

	public void run(IMarker marker) {
		IDocument document = provider.getDocument(input);
		PaletteTaglibInserter inserter = new PaletteTaglibInserter();
		inserter.inserTaglib(document, properties);
		try{
			provider.saveDocument(new NullProgressMonitor(), input, document, true);
		}catch(CoreException ex){
			WebUiPlugin.getPluginLog().logError(ex);
		}
	}
}
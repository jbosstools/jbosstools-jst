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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator;
import org.jboss.tools.common.model.ui.views.palette.PaletteInsertHelper;
import org.jboss.tools.jst.jsp.jspeditor.dnd.JSPPaletteInsertHelper;
import org.jboss.tools.jst.jsp.jspeditor.dnd.PaletteTaglibInserter;
import org.jboss.tools.jst.web.ui.WebUiPlugin;

/**
 * Shows the Marker Resolutions for Unknown tag JSP Problem Marker
 * 
 * @author Daniel Azarov
 *
 */
public class JSPProblemMarkerResolutionGenerator implements IMarkerResolutionGenerator {
	public IMarkerResolution[] getResolutions(IMarker marker) {
		try{
			if(isOurCase(marker)){
				return new IMarkerResolution[] {
					new AddTLDMarkerResolution()
				};
			}
			
		}catch(CoreException ex){
			WebUiPlugin.getPluginLog().logError(ex);
		}
		return new IMarkerResolution[]{};
	}
	
	private boolean isOurCase(IMarker marker) throws CoreException{
		String message = (String)marker.getAttribute(IMarker.MESSAGE);
		final int start = ((Integer)marker.getAttribute(IMarker.CHAR_START)).intValue();
		final int end = ((Integer)marker.getAttribute(IMarker.CHAR_END)).intValue();
		if(!message.startsWith("Unknown tag"))
			return false;
		
		String prefix = AddTLDMarkerResolution.getPrifix(message);
		if(prefix == null)
			return false;
		
		//System.out.println("Prefix - "+prefix);
		
		if(!AddTLDMarkerResolution.libs.containsKey(prefix))
			return false;
		
		IFile file = (IFile)marker.getResource();
		
		//System.out.println("File - "+file.getFullPath());
		
		IDocument document = AddTLDMarkerResolution.getDocument(file);
		
		Properties properties = new Properties();
		properties.put(JSPPaletteInsertHelper.PROPOPERTY_TAGLIBRARY_URI, AddTLDMarkerResolution.libs.get(prefix));
		properties.put(JSPPaletteInsertHelper.PROPOPERTY_DEFAULT_PREFIX, prefix);
		properties.put(PaletteInsertHelper.PROPOPERTY_SELECTION_PROVIDER, new ISelectionProvider() {
			
			public void setSelection(ISelection selection) {
			}
			
			public void removeSelectionChangedListener(
					ISelectionChangedListener listener) {
			}
			
			public ISelection getSelection() {
				return new TextSelection(start, end-start);
			}
			
			public void addSelectionChangedListener(ISelectionChangedListener listener) {
			}
		});
		properties.put(JSPPaletteInsertHelper.PROPOPERTY_ADD_TAGLIB, "true"); //$NON-NLS-1$
		
		Properties p = PaletteTaglibInserter.getPrefixes(document, properties);
		
		if(p.containsValue(prefix))
			return false;

		
//		ArrayList<TaglibData> includeTaglibs = new ArrayList<TaglibData>();
		
//		List<TaglibData> taglibs = XmlUtil.getTaglibsForJSPDocument(document, includeTaglibs);
//		
//		for(TaglibData data : taglibs){
//			System.out.println("Taglib prefix - "+data.getPrefix()+" URI - "+data.getUri());
//		}
		
//		if(XmlUtil.getTaglibForPrefix(prefix, taglibs) != null)
//			return false;
			
		return true;
	}
}
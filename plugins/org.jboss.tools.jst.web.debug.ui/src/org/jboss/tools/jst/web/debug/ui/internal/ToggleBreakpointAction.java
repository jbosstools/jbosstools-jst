/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.web.debug.ui.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IBreakpointManager;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.IVerticalRulerInfo;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.AbstractMarkerAnnotationModel;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.IUpdate;

import org.jboss.tools.common.model.plugin.ModelPlugin;
import org.jboss.tools.jst.web.debug.ui.xpl.WebDebugUIPlugin;
import org.jboss.tools.jst.web.debug.xpl.JspDebugUtils;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;

public class ToggleBreakpointAction extends Action implements IUpdate 
{
	private IVerticalRulerInfo verticalRulerInfo;
	private ITextEditor textEditor;
	private List markers;
	
	public ToggleBreakpointAction(IVerticalRulerInfo verticalRulerInfo, ITextEditor textEditor)
	{
	
		this.verticalRulerInfo = verticalRulerInfo;
		this.textEditor = textEditor;
	}

	public void run()
	{
		if (markers.isEmpty())
			addMarker();
		else
			removeMarkers(markers);
	}

	public void update() 
	{
		markers = getMarkers();
		setText(markers.isEmpty() ? WebUIMessages.ADD_JSP_BREAKPOINT : WebUIMessages.REMOVE_JSP_BREAKPOINT);
	}
	
	protected void addMarker() 
	{
		IEditorInput editorInput = textEditor.getEditorInput();
		IDocument document = getDocument();
		int rulerLine = verticalRulerInfo.getLineOfLastMouseButtonActivity();
		
		try 
		{
			int lineNumber = rulerLine + 1;
			if (lineNumber > 0) 
			{
				IRegion line = document.getLineInformation(lineNumber - 1);
				int start = line.getOffset();
				int end = start + line.getLength() - 1;
				JspDebugUtils.createJspLineBreakpoint(getResource(), lineNumber, start, end);
			}
		} catch (BadLocationException ex)	{
			WebDebugUIPlugin.getPluginLog().logError(ex);
		} 	catch (DebugException ex)	{
			WebDebugUIPlugin.getPluginLog().logError(ex);
		}
	}
	
	protected void removeMarkers(List markers) 
	{
		IBreakpointManager breakpointManager = DebugPlugin.getDefault().getBreakpointManager();
		try 
		{
			Iterator iterator = markers.iterator();
			while (iterator.hasNext()) 
			{
				IBreakpoint breakpoint = breakpointManager.getBreakpoint((IMarker)iterator.next());
				breakpointManager.removeBreakpoint(breakpoint, true);
			}
		} 	catch (CoreException ex)	{
			WebDebugUIPlugin.getPluginLog().logError(ex);
		}
	}
	
	protected List getMarkers() 
	{
		List breakpoints = new ArrayList();
		
		IResource resource = getResource();
		IDocument document = getDocument();
		AbstractMarkerAnnotationModel model = getAnnotationModel();
		
		if (model != null) 
		{
			try 
			{
				IMarker markers[] = null;
				if (resource instanceof IFile)
					markers = resource.findMarkers(IBreakpoint.BREAKPOINT_MARKER, true, IResource.DEPTH_INFINITE);
				else 
				{
					IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
					markers = root.findMarkers(IBreakpoint.BREAKPOINT_MARKER, true, IResource.DEPTH_INFINITE);
				}
				
				if (markers != null) 
				{
					IBreakpointManager breakpointManager = DebugPlugin.getDefault().getBreakpointManager();
					for (int i= 0; i < markers.length; i++) 
					{
						IBreakpoint breakpoint = breakpointManager.getBreakpoint(markers[i]);
						if (breakpoint != null && breakpointManager.isRegistered(breakpoint) && 
								includesRulerLine(model.getMarkerPosition(markers[i]), document))
							breakpoints.add(markers[i]);
					}
				}
			}  catch (CoreException ex)	{
				WebDebugUIPlugin.getPluginLog().logError(ex);
			}
		}
		return breakpoints;
	}
	
	protected IResource getResource() 
	{
		IEditorInput input = textEditor.getEditorInput();
		IResource resource = (IResource)input.getAdapter(IFile.class);
		
		if (resource == null) 
			resource = (IResource)input.getAdapter(IResource.class);
			
		return resource;
	}
	
	protected IDocument getDocument() 
	{
		IDocumentProvider provider = textEditor.getDocumentProvider();
		return provider.getDocument(textEditor.getEditorInput());
	}
	
	protected AbstractMarkerAnnotationModel getAnnotationModel() 
	{
		IDocumentProvider provider = textEditor.getDocumentProvider();
		IAnnotationModel model= provider.getAnnotationModel(textEditor.getEditorInput());
		if (model instanceof AbstractMarkerAnnotationModel) {
			return (AbstractMarkerAnnotationModel) model;
		}
		return null;
	}
	
	protected boolean includesRulerLine(Position position, IDocument document) 
	{
		if (position != null) 
		{
			try 
			{
				int markerLine = document.getLineOfOffset(position.getOffset());
				int line = verticalRulerInfo.getLineOfLastMouseButtonActivity();
				if (line == markerLine) 
					return true;
			} 
			catch (BadLocationException ex) 
			{
			}
		}
		return false;
	}
}

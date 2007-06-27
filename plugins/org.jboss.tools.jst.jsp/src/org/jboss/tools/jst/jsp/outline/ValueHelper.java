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
package org.jboss.tools.jst.jsp.outline;

import java.util.*;
import org.eclipse.core.resources.*;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.jspeditor.*;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jst.jsp.core.internal.contentmodel.*;
import org.eclipse.jst.jsp.core.internal.contentmodel.tld.*;
import org.eclipse.ui.*;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.sse.ui.internal.IModelProvider;
import org.jboss.tools.jst.jsp.editor.IVisualContext;
import org.jboss.tools.jst.jsp.editor.IVisualController;
import org.jboss.tools.jst.jsp.editor.IVisualEditor;

import org.jboss.tools.jst.jsp.drop.treeviewer.model.AttributeValueResource;
import org.jboss.tools.jst.jsp.drop.treeviewer.model.AttributeValueResourceFactory;
import org.jboss.tools.jst.jsp.drop.treeviewer.model.ModelElement;
import org.jboss.tools.jst.jsp.drop.treeviewer.model.RootElement;
import org.jboss.tools.common.kb.AttributeDescriptor;
import org.jboss.tools.common.kb.AttributeValueDescriptor;
import org.jboss.tools.common.kb.KbConnectorFactory;
import org.jboss.tools.common.kb.KbConnectorType;
import org.jboss.tools.common.kb.KbException;
import org.jboss.tools.common.kb.KbTldResource;
import org.jboss.tools.common.kb.TagDescriptor;
import org.jboss.tools.common.kb.wtp.TLDVersionHelper;
import org.jboss.tools.common.kb.wtp.WtpKbConnector;
import org.jboss.tools.common.model.plugin.ModelPlugin;
import org.jboss.tools.common.model.ui.ModelUIPlugin;
import org.jboss.tools.jst.jsp.support.kb.WTPTextJspKbConnector;
import org.jboss.tools.jst.web.tld.TaglibMapping;
import org.jboss.tools.jst.web.tld.VpeTaglibManager;

public class ValueHelper {
	private IEditorInput editorInput = null;
	private WTPTextJspKbConnector wtpTextJspKbConnector = null;
//	VpePageContext 
	IVisualContext pageContext = null;
	WtpKbConnector pageConnector = null;

	public ValueHelper() {
		boolean b = init();
		if(!b) init2();
	}
	
	public IVisualController getController() {
		IEditorPart editor = ModelUIPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if(!(editor instanceof JSPMultiPageEditor)) return null;
		JSPTextEditor jspEditor = ((JSPMultiPageEditor)editor).getJspEditor();
		IVisualEditor v = ((JSPMultiPageEditor)editor).getVisualEditor();
		if(v == null) return null;
		return v.getController();
	}

	boolean init() {
		if(pageContext != null || pageConnector != null) return true;
		IEditorPart editor = ModelUIPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if(!(editor instanceof JSPMultiPageEditor)) return false;
		JSPTextEditor jspEditor = ((JSPMultiPageEditor)editor).getJspEditor();
		IVisualEditor v = ((JSPMultiPageEditor)editor).getVisualEditor();
		if(v == null) return false;
		IVisualController c = v.getController();
		if(c == null) return false;
		pageContext = c.getPageContext();
		editorInput = jspEditor.getEditorInput();
		
		wtpTextJspKbConnector = jspEditor.getWTPTextJspKbConnector();
		return pageContext != null || pageConnector != null;
	}

	public ModelElement getInitalInput(String query) {
		AttributeDescriptor descriptor = getAttributeDescriptor(query);
		if(descriptor == null) return new RootElement("root", new AttributeValueResource[0]);
		AttributeValueDescriptor[] valueDescriptors = descriptor.getValueDesriptors();
		AttributeValueResource[] elements = new AttributeValueResource[valueDescriptors.length];
		ModelElement root = new RootElement("root", elements);
		for (int i = 0; i < valueDescriptors.length; i++) {
			AttributeValueResource resource = AttributeValueResourceFactory.getInstance().createResource(editorInput, wtpTextJspKbConnector, root, valueDescriptors[i].getType());
			resource.setParams(valueDescriptors[i].getParams());
			resource.setQuery(query, this);
			elements[i] = resource;
		}
		return root;
	}

	public boolean isAvailable(String query) {
		RootElement root = (RootElement)getInitalInput(query);
		return (root != null && root.getChildren().length > 0);
		///return getAttributeDescriptor(query) != null;
	}

	public AttributeDescriptor getAttributeDescriptor(String query) {
		if(!init()) return null;
		AttributeDescriptor result = null;
		try {
			result = getPageConnector().getAttributeInformation(query);
		} catch (KbException e) {
			JspEditorPlugin.getPluginLog().logError(e);
		}
		return result;
	}

	public TagDescriptor getTagDescriptor(String query) {
		if(!init()) return null;
		TagDescriptor result = null;
		try {
			result = getPageConnector().getTagInformation(query);
		} catch (KbException e) {
			JspEditorPlugin.getPluginLog().logError(e);
		}
		return result;
	}

	public IEditorInput getEditorInput() {
		return editorInput;
	}

	public IProject getProject() {
		if(!(editorInput instanceof IFileEditorInput)) return null;
		IFile file = ((IFileEditorInput)editorInput).getFile();
		return file == null ? null : file.getProject();
	}

	public WTPTextJspKbConnector getConnector() {
		return wtpTextJspKbConnector;
	}

	public VpeTaglibManager getTaglibManager() {
		init();
		return pageContext;
	}
	
	public WtpKbConnector getPageConnector() {
		if(pageContext != null) return pageContext.getConnector();
		return pageConnector;
	}
	
	public IDocument getDocument() {
		IEditorPart editor = ModelUIPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if(editor instanceof JSPMultiPageEditor) {
			JSPTextEditor jspEditor = ((JSPMultiPageEditor)editor).getJspEditor();
			return jspEditor.getDocumentProvider().getDocument(editor.getEditorInput());				
		} else if(editor instanceof StructuredTextEditor) {
			StructuredTextEditor jspEditor = ((StructuredTextEditor)editor);
			return jspEditor.getDocumentProvider().getDocument(editor.getEditorInput());
		}
		
		return null;		
	}
	
	//Support of StructuredTextEditor
	boolean init2() {
		if(pageContext != null || pageConnector != null) return true;
		IEditorPart editor = ModelUIPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if(!(editor instanceof StructuredTextEditor)) return false;
		StructuredTextEditor jspEditor = ((StructuredTextEditor)editor);
		editorInput = jspEditor.getEditorInput();
		IDocument document = jspEditor.getDocumentProvider().getDocument(editorInput);
		if(document == null) return false;
		installActivePropmtSupport(jspEditor, document);
		getConnector(document);
		return pageContext != null || pageConnector != null;
	}

	private void installActivePropmtSupport(StructuredTextEditor jspEditor, IDocument document) {
		IStructuredModel model = null;		
		try {
			model = StructuredModelManager.getModelManager().getExistingModelForRead(document);
			if(wtpTextJspKbConnector == null && model != null && 
					(getContentType(model).toLowerCase().indexOf("jsp") != -1 || 
					getContentType(model).toLowerCase().indexOf("html") != -1)) {
				clearTextConnectors();
				wtpTextJspKbConnector = (WTPTextJspKbConnector)wtpTextConnectors.get(editorInput);
				if(wtpTextJspKbConnector == null) {
					wtpTextJspKbConnector = new WTPTextJspKbConnector(jspEditor.getEditorInput(), document, model);
					wtpTextConnectors.put(editorInput, wtpTextJspKbConnector);
				}
///				wtpTextJspKbConnector.setTaglibManagerProvider(parentEditor);
			}
		} catch(Exception x) {
			JspEditorPlugin.getPluginLog().logError("Error in activating prompting suppport", x);
		} finally {
			if(model != null) {
				model.releaseFromRead();
			}
		}
	}
	private String getContentType(IStructuredModel model) {
		String type = null;
		try {
			type = model.getContentTypeIdentifier();
		} finally {
			if (type == null) type = "";
		}
		return type;
	}
	void getConnector(IDocument document) {
		try {
			pageConnector = (WtpKbConnector)KbConnectorFactory.getIntstance().createConnector(KbConnectorType.JSP_WTP_KB_CONNECTOR, document);
			registerTaglibs(pageConnector, document);
		} catch (Exception e) {
			JspEditorPlugin.getPluginLog().logError(e);
		}
	}
	boolean registerTaglibs(WtpKbConnector wtpKbConnector, IDocument document) {
		if(wtpKbConnector == null) return false;
		TLDCMDocumentManager manager = TaglibController.getTLDCMDocumentManager(document);
		if(manager != null) {
			List list = manager.getTaglibTrackers();
			for (int i = 0; i < list.size(); i++) {
				TaglibTracker tracker = (TaglibTracker)list.get(i);
				if(tracker == null) continue;
				String version = TLDVersionHelper.getTldVersion(tracker);
				KbTldResource resource = new KbTldResource(tracker.getURI(), "", tracker.getPrefix(), version);
				wtpKbConnector.registerResource(resource);
			}
			return true;
		}
		return false;
	}
	
	static Map wtpTextConnectors = new HashMap();
	
	void clearTextConnectors() {
		IWorkbenchPage workbenchPage = getWorkbenchPage();
		if(workbenchPage == null) {
			wtpTextConnectors.clear();
			return;
		}
		Iterator it = wtpTextConnectors.keySet().iterator();
		while(it.hasNext()) {
			IEditorInput input = (IEditorInput)it.next();
			if(workbenchPage.findEditor(input) == null) {
				it.remove();
			}
		}
	}
	private IWorkbenchPage getWorkbenchPage() {
		ModelUIPlugin plugin = ModelUIPlugin.getDefault();
		IWorkbench workbench = (plugin == null) ? null : plugin.getWorkbench();
		IWorkbenchWindow window = (workbench == null) ? null : workbench.getActiveWorkbenchWindow();
		return (window == null) ? null : window.getActivePage();
	}	

}

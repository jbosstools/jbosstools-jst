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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jst.jsp.core.internal.contentmodel.TaglibController;
import org.eclipse.jst.jsp.core.internal.contentmodel.tld.TLDCMDocumentManager;
import org.eclipse.jst.jsp.core.internal.contentmodel.tld.TaglibTracker;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jboss.tools.common.kb.AttributeDescriptor;
import org.jboss.tools.common.kb.AttributeValueDescriptor;
import org.jboss.tools.common.kb.KbConnectorFactory;
import org.jboss.tools.common.kb.KbConnectorType;
import org.jboss.tools.common.kb.KbException;
import org.jboss.tools.common.kb.KbTldResource;
import org.jboss.tools.common.kb.TagDescriptor;
import org.jboss.tools.common.kb.wtp.JspWtpKbConnector;
import org.jboss.tools.common.kb.wtp.TLDVersionHelper;
import org.jboss.tools.common.kb.wtp.WtpKbConnector;
import org.jboss.tools.common.model.project.IPromptingProvider;
import org.jboss.tools.common.model.ui.ModelUIPlugin;
import org.jboss.tools.common.model.util.ModelFeatureFactory;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.contentassist.RedHatHtmlContentAssistProcessor;
import org.jboss.tools.jst.jsp.drop.treeviewer.model.AttributeValueResource;
import org.jboss.tools.jst.jsp.drop.treeviewer.model.AttributeValueResourceFactory;
import org.jboss.tools.jst.jsp.drop.treeviewer.model.ModelElement;
import org.jboss.tools.jst.jsp.drop.treeviewer.model.RootElement;
import org.jboss.tools.jst.jsp.editor.IVisualContext;
import org.jboss.tools.jst.jsp.editor.IVisualController;
import org.jboss.tools.jst.jsp.editor.IVisualEditor;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.jsp.jspeditor.JSPTextEditor;
import org.jboss.tools.jst.jsp.support.kb.WTPTextJspKbConnector;
import org.jboss.tools.jst.web.tld.TaglibData;
import org.jboss.tools.jst.web.tld.VpeTaglibManager;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class ValueHelper {
	private IEditorInput editorInput = null;
	private WTPTextJspKbConnector wtpTextJspKbConnector = null;
	private boolean isFacelets = false;
	
	public static IPromptingProvider seamPromptingProvider;
	
	static {
		Object o = ModelFeatureFactory.getInstance().createFeatureInstance("org.jboss.tools.seam.internal.core.el.SeamPromptingProvider");
		if(o instanceof IPromptingProvider) {
			seamPromptingProvider = (IPromptingProvider)o;
		}
	}
	
	IVisualContext iVisualContext = null;
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
		if(iVisualContext != null || pageConnector != null) return true;
		IEditorPart editor = ModelUIPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if(!(editor instanceof JSPMultiPageEditor)) return false;
		JSPTextEditor jspEditor = ((JSPMultiPageEditor)editor).getJspEditor();
		
		//Added By Max Areshkau
		//Fix for JBIDE-788
		iVisualContext = jspEditor.getPageContext();
		
		editorInput = jspEditor.getEditorInput();
		
		wtpTextJspKbConnector = jspEditor.getWTPTextJspKbConnector();
		
		if(iVisualContext != null) {
			updateFacelets();
		}
		return iVisualContext != null || pageConnector != null;
	}

	public ModelElement getInitalInput(String query) {
		AttributeDescriptor descriptor = getAttributeDescriptor(query);
		if(descriptor == null) return new RootElement("root", new ArrayList<AttributeValueResource>());
		AttributeValueDescriptor[] valueDescriptors = descriptor.getValueDesriptors();
		List<AttributeValueResource> elements = new ArrayList<AttributeValueResource>();
		ModelElement root = new RootElement("root", elements);
		for (int i = 0; i < valueDescriptors.length; i++) {
			AttributeValueResource resource = AttributeValueResourceFactory.getInstance().createResource(editorInput, wtpTextJspKbConnector, root, valueDescriptors[i].getType());
			resource.setParams(valueDescriptors[i].getParams());
			resource.setQuery(query, this);
			elements.add(resource);
		}
		if(seamPromptingProvider != null && getFile() != null) {
			Properties p = new Properties();
			p.put("file", getFile());
			List list = seamPromptingProvider.getList(null, "seam.is_seam_project", null, p);
			if(list != null) {
				AttributeValueResource resource = AttributeValueResourceFactory.getInstance().createResource(editorInput, wtpTextJspKbConnector, root, "seamVariables");
				resource.setQuery(query, this);
				elements.add(resource);
			}
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
	
	public IFile getFile() {
		if(!(editorInput instanceof IFileEditorInput)) return null;
		return ((IFileEditorInput)editorInput).getFile();
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
		if(iVisualContext!=null && iVisualContext instanceof VpeTaglibManager) {
		
			return (VpeTaglibManager)iVisualContext;
		} else {
			
			return null;
		}
	}
	
	public WtpKbConnector getPageConnector() {
		if(iVisualContext != null) return iVisualContext.getConnector();
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
		if(iVisualContext != null || pageConnector != null) return true;
		IEditorPart editor = ModelUIPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if(!(editor instanceof StructuredTextEditor)) return false;
		StructuredTextEditor jspEditor = ((StructuredTextEditor)editor);
		editorInput = jspEditor.getEditorInput();
		IDocument document = jspEditor.getDocumentProvider().getDocument(editorInput);
		if(document == null) return false;
		installActivePropmtSupport(jspEditor, document);
		getConnector(document);
		return iVisualContext != null || pageConnector != null;
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
	
	public boolean isFacetets() {
		return isFacelets;
	}

	public void updateFacelets() {
		VpeTaglibManager tldManager = getTaglibManager();
		if(tldManager == null) return;
		List<TaglibData> list = tldManager.getTagLibs();
		if(list == null) return;
		isFacelets = false;
		IDocument document = getDocument();
		JspWtpKbConnector kbConnector = (JspWtpKbConnector)wtpTextJspKbConnector.getConnector();
		kbConnector.unregisterAllResources(true);
		for(int i = 0; i < list.size(); i++) {
			TaglibData data = list.get(i);
			RedHatHtmlContentAssistProcessor.registerTld(data, kbConnector, document, editorInput);
			isFacelets = isFacelets || data.getUri().equals(RedHatHtmlContentAssistProcessor.faceletUri);
		}
		if(isFacelets) {
			kbConnector.registerResource(RedHatHtmlContentAssistProcessor.faceletHtmlResource);
			kbConnector.unregisterJspResource();
		}
	}
	
	public String getFaceletJsfTag(Element element) {
		if(!isFacelets) return null;
		String name = element.getNodeName();
		if(name.indexOf(':') >= 0) return null;
		
		NamedNodeMap attributes = element.getAttributes();
		Node jsfC = attributes.getNamedItem(RedHatHtmlContentAssistProcessor.JSFCAttributeName);
		if(jsfC != null && (jsfC instanceof Attr)) {
			Attr jsfCAttribute = (Attr)jsfC;
			String jsfTagName = jsfCAttribute.getValue();
			if(jsfTagName != null && jsfTagName.indexOf(':') > 0) {
				return jsfTagName;
			}
		}
		return null;
	}
}

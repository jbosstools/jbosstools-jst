/*******************************************************************************
 * Copyright (c) 2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.text.ext.hyperlink;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.AbstractHyperlinkDetector;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.xml.core.internal.document.ElementImpl;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.jboss.tools.common.text.ext.util.StructuredModelWrapper;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class JQueryMobileHyperlinkDetector extends AbstractHyperlinkDetector{

	@Override
	public IHyperlink[] detectHyperlinks(ITextViewer textViewer,
			IRegion region, boolean canShowMultipleHyperlinks) {
		List<IHyperlink> links = new ArrayList<IHyperlink>();
		
		IFile file = getFile();
		
		if(file == null)
			return null;
		
		StructuredModelWrapper smw = new StructuredModelWrapper();
		smw.init(textViewer.getDocument());
		try {
			Document xmlDocument = smw.getDocument();
			if (xmlDocument == null)
				return null;
			
			Node n = findNodeForOffset(xmlDocument, region.getOffset());
			
			if(n != null){
				if(n.getNodeType() == Node.ELEMENT_NODE && n instanceof ElementImpl){
					ITextRegion textRegion = ((ElementImpl)n).getStartStructuredDocumentRegion();
					Region nodeRegion = new Region(textRegion.getStart(), textRegion.getLength());
					String id = findID(n);
					if(id != null){
						String url = file.getLocation().toFile().toURI()+"#"+id; //$NON-NLS-1$
						String shortName = file.getName()+"#"+id; //$NON-NLS-1$
						links.add(new OpenWithBrowserSimHyperlink(textViewer.getDocument(), nodeRegion, shortName, url));
						links.add(new OpenWithBrowserHyperlink(textViewer.getDocument(), nodeRegion, shortName, url));
					}
				}
			}
		
			if (links.size() == 0)
				return null;
			return (IHyperlink[])links.toArray(new IHyperlink[links.size()]);
		} finally {
			smw.dispose();
		}
	}
	
	static public Node findNodeForOffset(Node node, int offset) {
		return (node instanceof IDOMNode) ? findNodeForOffset((IDOMNode)node, offset) : null;
	}

	static public Node findNodeForOffset(IDOMNode node, int offset) {
		if(node == null) return null;
		if (!node.contains(offset)) return null;
			
		if (node.hasChildNodes()) {
			// Try to find the node in children
			NodeList children = node.getChildNodes();
			for (int i = 0; children != null && i < children.getLength(); i++) {
				IDOMNode child = (IDOMNode)children.item(i);
				if (child.contains(offset)) {
					return findNodeForOffset(child, offset);
				}
			}
		}
		
		// Return the node itself
		return node;
	}
	
	private String findID(Node node){
		if(node.getNodeName().equalsIgnoreCase("div")){ //$NON-NLS-1$
			if(checkAttribute(node, "data-role", "page")){ //$NON-NLS-1$ //$NON-NLS-2$ 
				return getAttributeValue(node, "id"); //$NON-NLS-1$
			}
		}
		return null;
	}
	
	private String getAttributeValue(Node node, String attributeName){
		NamedNodeMap attributes = node.getAttributes();
		if(attributes != null){
			Node attribute = attributes.getNamedItem(attributeName);
			if(attribute != null){
				return attribute.getNodeValue();
			}
		}
		return null;
	}
	
	private boolean checkAttribute(Node node, String attributeName, String attributeValue){
		NamedNodeMap attributes = node.getAttributes();
		if(attributes != null){
			Node attribute = attributes.getNamedItem(attributeName);
			if(attribute != null && attribute.getNodeValue().equalsIgnoreCase(attributeValue)){
				return true;
			}
		}
		return false;
	}
	
	private IFile getFile(){
		IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if(part != null){
			IEditorInput input = part.getEditorInput();
			if(input instanceof FileEditorInput)
				return ((FileEditorInput)input).getFile();
		}
		return null;
	}
}

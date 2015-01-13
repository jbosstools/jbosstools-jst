/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.web.ui.internal.text.ext.hyperlink.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.AbstractHyperlinkDetector;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.jboss.tools.common.text.ext.util.StructuredModelWrapper;
import org.jboss.tools.common.text.ext.util.Utils;
import org.jboss.tools.common.text.ext.util.Utils.AttrNodePair;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;
import org.jboss.tools.jst.web.ui.WebUiPlugin;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DatalistHyperlinkDetector extends AbstractHyperlinkDetector{

	@Override
	public IHyperlink[] detectHyperlinks(ITextViewer textViewer,
			IRegion region, boolean canShowMultipleHyperlinks) {
		List<IHyperlink> links = new ArrayList<IHyperlink>();
		
		StructuredModelWrapper smw = new StructuredModelWrapper();
		smw.init(textViewer.getDocument());
		try {
			Document xmlDocument = smw.getDocument();
			if (xmlDocument == null)
				return null;
			
			AttrNodePair pair = Utils.findAttrNodePairForOffset(xmlDocument, region.getOffset());
			
			if(pair != null && pair.getNode() != null && pair.getAttribute() != null){
				if("input".equalsIgnoreCase(pair.getNode().getNodeName()) && "list".equalsIgnoreCase(pair.getAttribute().getNodeName())){
					IRegion sourceRegion = null;
					try {
						sourceRegion = Utils.getAttributeValueRegion(textViewer.getDocument(), (Attr)pair.getAttribute());
					} catch (BadLocationException e) {
						WebUiPlugin.getDefault().logError(e);
					}
					
					if(sourceRegion != null && 
							region.getOffset() >= sourceRegion.getOffset() && 
							region.getOffset() <= (sourceRegion.getOffset()+sourceRegion.getLength())){
						
						String attrValue = pair.getAttribute().getNodeValue();
						IndexedRegion node = findNodeWithId(xmlDocument, "datalist", attrValue);
						
						if(node != null){
							Region targetRegion = new Region(node.getStartOffset(), node.getEndOffset() - node.getStartOffset());
							links.add(new JumpToHyperlink(NLS.bind(WebUIMessages.ShowDatalist, attrValue), textViewer.getDocument(), sourceRegion, targetRegion));
						}
					}
				}
			}
		} finally {
			smw.dispose();
		}
		if (links.size() == 0)
			return null;
		return (IHyperlink[])links.toArray(new IHyperlink[links.size()]);
		
	}
	
	private IndexedRegion  findNodeWithId(Node node, String nodeName, String id){
		if(node instanceof IndexedRegion && nodeName.equalsIgnoreCase(node.getNodeName())){
			if(node.hasAttributes()){
				NamedNodeMap attributes = node.getAttributes();
				Node attr = attributes.getNamedItem("id");
				if(attr != null && id.equalsIgnoreCase(attr.getNodeValue())){
					return (IndexedRegion)node;
				}
			}
		}
		NodeList list = node.getChildNodes();
		for(int i = 0; i < list.getLength(); i++){
			Node child = list.item(i);
			IndexedRegion result = findNodeWithId(child, nodeName, id);
			if(result != null){
				return result;
			}
		}
		return null;
	}
}

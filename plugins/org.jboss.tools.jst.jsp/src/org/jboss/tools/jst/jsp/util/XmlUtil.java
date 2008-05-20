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
package org.jboss.tools.jst.jsp.util;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jst.jsp.core.internal.contentmodel.TaglibController;
import org.eclipse.jst.jsp.core.internal.contentmodel.tld.TLDCMDocumentManager;
import org.eclipse.jst.jsp.core.internal.contentmodel.tld.TaglibTracker;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.web.tld.TaglibData;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XmlUtil {
	
	private static final String XMLNS = "xmlns"; //$NON-NLS-1$
	
	private static final String XMLNS_WITH_PREFIX = "xmlns:"; //$NON-NLS-1$

	public static Element getDocumentElement(String xmlFileName) throws Exception {
		FileInputStream inStream = null;
		try {
			inStream = new FileInputStream(xmlFileName);
			InputSource inSource = new InputSource(inStream);
			return getDocumentElement(inSource);
		} finally {
			try {
				if (inStream != null) inStream.close();
			} catch (Exception e) {
				JspEditorPlugin.getPluginLog().logError(e);
			}
		}
	}

	public static Element getDocumentElement(InputSource is) throws Exception {
		return getDocument(is).getDocumentElement();
	}
    
	public static Document getDocument(InputSource is) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(is);
	}
    
	public static void removeChildren(Node node) {
		NodeList children = node.getChildNodes();
		if (children != null) {
			int len = children.getLength();
			for (int i = len - 1; i >= 0; i--) {
				node.removeChild(children.item(i));
			}
		}
	}
	
	/**
	 * Returns List of taglibs which are available for current node.
	 *  
	 * @param node
	 * @return
	 */
	public static List<TaglibData> processNode(Node node, List<TaglibData> includeTaglibData) {
		
		List<TaglibData> taglibs = new ArrayList<TaglibData>();
		
		if (node == null) {
			
			return taglibs;
		}
		
		Node currentNode = node;
		do {
			NamedNodeMap attribList = currentNode.getAttributes();
			if (null != attribList) {
				for (int i = 0; i < attribList.getLength(); i++) {
					Node tmp = attribList.item(i);
					processAttribute(taglibs,(Attr)tmp, false);
				}
			}	
			currentNode = currentNode.getParentNode();			

		} while(currentNode!=null);

		for (TaglibData taglibData : includeTaglibData) {
			addTaglib(taglibs, taglibData.getUri(),taglibData.getPrefix(), true, false);
		}
		return taglibs;
	}
	
	/**
	 * Finds and returns taglibs for JSP documents
	 * @return
	 */
	public static List<TaglibData> getTaglibsForJSPDocument(IDocument document,List<TaglibData> includeTaglibs) {
		
		List<TaglibData> taglibData = new ArrayList<TaglibData>();
		
		TLDCMDocumentManager tldcmDocumentManager= TaglibController.getTLDCMDocumentManager(document);
		if(tldcmDocumentManager!=null) {
				List<TaglibTracker> taglibs_JSP =  tldcmDocumentManager.getTaglibTrackers();
				for (TaglibTracker taglibTracker : taglibs_JSP) {
					addTaglib(taglibData, taglibTracker.getURI(), taglibTracker.getPrefix(), true,false);
				}
		}
		//add inner taglibs
		for (TaglibData taglib : includeTaglibs) {
			
			addTaglib(taglibData, taglib.getUri(), taglib.getPrefix(), true, false);
		}
		return taglibData;
	}
	/**
	 * Processes taglib attribute
	 * @param taglibs
	 * @param attr
	 * @param bScopePrefix
	 */	
	private static void processAttribute(List<TaglibData> taglibs, Attr attr, boolean bScopePrefix) {

		String name = attr.getName();
		if (!name.startsWith(XMLNS)) {
			return;
		}
		
		if(XMLNS.equals(name)) {

			name=""; //$NON-NLS-1$
		} else {
			
			name=name.substring(XMLNS_WITH_PREFIX.length());
		}
		addTaglib(taglibs , attr.getValue(), name, true, bScopePrefix);
		return;
	}
	
	/**
	 * Adds taglib to current taglibs
	 * @param taglibs
	 * @param newUri
	 * @param newPrefix
	 * @param ns
	 * @param bScopePrefix
	 */
	private static void addTaglib(List<TaglibData> taglibs, String newUri, String newPrefix, boolean ns, boolean bScopePrefix) {	
		boolean bHasSame = false;
		for (int i = 0; i < taglibs.size(); i++) {
			TaglibData taglib = (TaglibData)taglibs.get(i);

				if(newUri.equals(taglib.getUri())) {
					return;
				}
				if(newPrefix.equals(taglib.getPrefix())) {
					return;
				}
		}
		if (!bHasSame) {
			taglibs.add(new TaglibData(taglibs.size(), newUri, newPrefix, ns));
		}
	}
	/**
	 * Returns Taglib data by prefix
	 * 
	 * @param prefix
	 * @param taglibData
	 * @return
	 */
	public static TaglibData getTaglibForPrefix(String prefix, List<TaglibData> taglibData){
				
			for (TaglibData data : taglibData) {
				
				if(data.getPrefix()!=null && data.getPrefix().equalsIgnoreCase(prefix)) {
					 return data;
				}
			}
			
			return null;
	}
}

/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.jst.jsp.jspeditor.dnd;

import java.util.Properties;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocumentType;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.jboss.tools.common.model.options.SharableConstants;
import org.jboss.tools.common.model.ui.views.palette.PaletteInsertHelper;
import org.jboss.tools.common.refactoring.MarkerResolutionUtils;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.web.kb.internal.JQueryRecognizer;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/***
 * 
 * @author Daniel Azarov
 *
 */

public class MobilePaletteInsertHelper extends PaletteInsertHelper {
	public static final String PROPOPERTY_JQUERY_MOBILE_INSERT_JS_CSS   = "insert jquery mobile js css"; //$NON-NLS-1$
	
	// palettePath - %Palette%/Mobile/jQuery Mobile/1.page/0. JS#CSS
	private static final String INSERT_JS_CSS_SIGNATURE = "<jquery.mobile.js.css>"; //$NON-NLS-1$
	
	private static final String MOBILE_PATH = "/Mobile/jQuery Mobile/"; //$NON-NLS-1$
	
	private static final String META = "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"";
	private static final String LINK = "<link rel=\"stylesheet\" href=\"http://code.jquery.com/mobile/1.3.0/jquery.mobile-1.3.0.min.css\" /";
	private static final String FIRST_SCRIPT = "<script src=\"http://code.jquery.com/jquery-1.9.1.min.js\"></script";
	private static final String SECOND_SCRIPT = "<script src=\"http://code.jquery.com/mobile/1.3.0/jquery.mobile-1.3.0.min.js\"></script";
	
	private static final int INSERT_AFTER_OPEN_NODE = 1;
	private static final int INSERT_AFTER_CLOSE_NODE = 2;
	private static final int INSERT_BEFORE_OPEN_NODE = 3;
	private static final int INSERT_BEFORE_CLOSE_NODE = 4;
	private static final int INSERT_AFTER_ALL = 5;
	
	private StringBuffer globalBuffer = new StringBuffer();
	private int globalPosition = -1, goobalLength = 0;

	private IDOMNode doctypeNode = null, htmlNode = null, headNode = null, bodyNode = null;;
	
	static MobilePaletteInsertHelper instance = new MobilePaletteInsertHelper();
	

	public static MobilePaletteInsertHelper getInstance() {
		return instance;
	}
	
	public boolean isMobile(ISourceViewer v, Properties p, String[] texts) {
		if(p.containsKey(SharableConstants.PALETTE_PATH)){
			String path = p.getProperty(SharableConstants.PALETTE_PATH);
			if(path.contains(MOBILE_PATH)){
				modify(v, p, texts);
				return true;
			}
		}
		return false;
	}
	
	@Override
	protected void modify(ISourceViewer v, Properties p, String[] texts) {
		p.put("viewer", v);
		String startText = texts[0];
		
		boolean insert  = startText != null && startText.startsWith(INSERT_JS_CSS_SIGNATURE);
		
		IFile file = MarkerResolutionUtils.getFile();
		
		if(insert || (p.containsKey(PROPOPERTY_JQUERY_MOBILE_INSERT_JS_CSS) && !JQueryRecognizer.containsJQueryJSReference(file))) {
			insertJS_CSS(v.getDocument());
			if(insert){
				texts[0] = "";	
			}
		}
	}
	
	public void test_insert_JS_CSS(IDocument document){
		insertJS_CSS(document);
	}
	
	private void writeBuffer(IDocument document) throws BadLocationException{
		if(globalBuffer.length() > 0 && globalPosition >= 0 && globalPosition <= document.getLength()){
			document.replace(globalPosition, goobalLength, globalBuffer.toString());
			globalBuffer = new StringBuffer();
			globalPosition = -1;
		}
	}
	
	private void copyAttributes(IDOMNode node){
		NamedNodeMap attributes = node.getAttributes();
		for(int i = 0; i < attributes.getLength(); i++){
			Node attribute = attributes.item(i);
			if(i != 0){
				globalBuffer.append(",");
			}
			globalBuffer.append(" ").append(attribute.getNodeName()).append("=\"").append(attribute.getNodeValue()).append("\"");
		}
	}
	
	private void insertNode(IDocument document, IDOMNode relatedNode, IDOMNode node, String spaces, String text, int mode, boolean forceWrite) throws BadLocationException{
		boolean newLineBefore = true;
		boolean newLineAfter = false;
		boolean copyAttributes = false;
		if(node != null){
			writeBuffer(document);
			if(node.getEndStructuredDocumentRegion() == null && !(node instanceof IDOMDocumentType)){
				globalPosition = node.getStartStructuredDocumentRegion().getStartOffset();
				goobalLength = node.getStartStructuredDocumentRegion().getLength();
				copyAttributes = true;
				newLineBefore = false;
			}else{
				return;
			}
		}
		
		if(mode == INSERT_AFTER_OPEN_NODE){
			if(globalPosition == -1 && relatedNode != null){
				if(relatedNode.getStartStructuredDocumentRegion() != null){
					if(relatedNode.getEndStructuredDocumentRegion() != null || relatedNode instanceof IDOMDocumentType){
						globalPosition = relatedNode.getStartStructuredDocumentRegion().getEndOffset();
						goobalLength = 0;
					}
				}
			}
			
		}else if(mode == INSERT_AFTER_CLOSE_NODE){
			if(globalPosition == -1 && relatedNode != null){
				if(relatedNode.getEndStructuredDocumentRegion() != null){
					globalPosition = relatedNode.getEndStructuredDocumentRegion().getEndOffset();
					goobalLength = 0;
				}
			}
		}else if(mode == INSERT_AFTER_ALL){
			writeBuffer(document);
			if(globalPosition == -1){
				globalPosition = document.getLength();
				goobalLength = 0;
			}
		}else if(mode == INSERT_BEFORE_CLOSE_NODE){
			writeBuffer(document);
			if(globalPosition == -1){
				if(relatedNode != null){
					globalPosition = relatedNode.getEndStructuredDocumentRegion().getStartOffset()-1;
					goobalLength = 0;
				}else{
					globalPosition = document.getLength();
					goobalLength = 0;
				}
			}
		}else if(mode == INSERT_BEFORE_OPEN_NODE){
			if(relatedNode != null){
				writeBuffer(document);
				if(globalPosition == -1){
					globalPosition = relatedNode.getStartStructuredDocumentRegion().getStartOffset()-1;
					goobalLength = 0;
					newLineBefore = false;
					newLineAfter = true;
				}
			}
		}
		
		if(newLineBefore){
			globalBuffer.append(spaces);
		}
		
		globalBuffer.append(text);
		
		if(copyAttributes){
			copyAttributes(node);
			if(htmlNode != null && htmlNode.getNodeName().equalsIgnoreCase(node.getNodeName())){
				htmlNode = null;
			}else if(headNode != null && headNode.getNodeName().equalsIgnoreCase(node.getNodeName())){
				headNode = null;
			}else if(bodyNode != null && bodyNode.getNodeName().equalsIgnoreCase(node.getNodeName())){
				bodyNode = null;
			}
		}
		
		globalBuffer.append(">");
		
		if(newLineAfter){
			globalBuffer.append("\n");
		}
		
		if(forceWrite){
			writeBuffer(document);
		}
	}
	
	private void insertJS_CSS(IDocument document){
		IStructuredModel model = null;
		try{
			model = StructuredModelManager.getModelManager().getExistingModelForRead((IStructuredDocument)document);
			IDOMDocument xmlDocument = (model instanceof IDOMModel) ? ((IDOMModel) model).getDocument() : null;
			if(xmlDocument != null){
				// analyze xml document
				doctypeNode = findDocumentType(xmlDocument, "html");
				
				htmlNode = findNode(xmlDocument, "html");
				
				if(htmlNode == null){
					bodyNode = findNode(xmlDocument, "body");
					headNode = findNode(xmlDocument, "head");
				}else{
					bodyNode = findNode(htmlNode, "body");
					headNode = findNode(htmlNode, "head");
				}
				
				boolean metaExists = checkNode(headNode, "meta", "name", "(viewport)");
				
				boolean linkExists = checkNode(headNode, "link", "href", ".*(jquery.mobile-).*(.css)");
				
				boolean firstScriptExists = checkNode(headNode, "script", "src", ".*(jquery-).*(.js)");
				
				boolean secondScriptExists = checkNode(headNode, "script", "src", ".*(jquery.mobile-).*(.js)");
				
				// insert tags if needed
				insertNode(document, doctypeNode, htmlNode, "\n", "<html", INSERT_AFTER_OPEN_NODE, false);
				
				insertNode(document, htmlNode, headNode, "\n  ", "<head", INSERT_AFTER_OPEN_NODE, false);
				
				if(!metaExists){
					insertNode(document, headNode, null, "\n    ", META, INSERT_AFTER_OPEN_NODE, false);
				}
				if(!linkExists){
					insertNode(document, headNode, null, "\n    ", LINK, INSERT_AFTER_OPEN_NODE, false);
				}
				if(!firstScriptExists){
					insertNode(document, headNode, null, "\n    ", FIRST_SCRIPT, INSERT_AFTER_OPEN_NODE, false);
				}
				if(!secondScriptExists){
					insertNode(document, headNode, null, "\n    ", SECOND_SCRIPT, INSERT_AFTER_OPEN_NODE, false);
				}
				
				insertNode(document, bodyNode, headNode, "\n  ", "</head", INSERT_BEFORE_OPEN_NODE, false);

				insertNode(document, headNode, bodyNode, "\n  ", "<body", INSERT_AFTER_CLOSE_NODE, false);

				insertNode(document, htmlNode, bodyNode, "\n  ", "</body", INSERT_BEFORE_CLOSE_NODE, false);

				insertNode(document, null, htmlNode, "\n", "</html", INSERT_AFTER_ALL, true);
			}
		} catch (BadLocationException e) {
			JspEditorPlugin.getDefault().logError(e);
		}finally{
			if (model != null) model.releaseFromRead();
		}
	}
	
	public static IDOMDocumentType findDocumentType(Node rootNode, String nodeName){
		if(rootNode != null){
			NodeList list = rootNode.getChildNodes();
			for(int i = 0; i < list.getLength(); i++){
				Node child = list.item(i);
				if(child instanceof IDOMDocumentType){
					if(child.getNodeName().equalsIgnoreCase(nodeName)){
						return (IDOMDocumentType)child;
					}
				}
			}
		}
		return null;
	}
	
	public static IDOMNode findNode(Node rootNode, String nodeName){
		if(rootNode != null){
			NodeList list = rootNode.getChildNodes();
			for(int i = 0; i < list.getLength(); i++){
				Node child = list.item(i);
				if(child instanceof Element){
					if(child.getNodeName().equalsIgnoreCase(nodeName)){
						return (IDOMNode)child;
					}
				}
			}
		}
		return null;
	}
	
	public static boolean checkNode(IDOMNode node, String nodeName, String attributeName, String attributePattern){
		if(node != null){
			NodeList list = node.getChildNodes();
			for(int i = 0; i < list.getLength(); i++){
				Node child = list.item(i);
				if(child.getNodeName().equalsIgnoreCase(nodeName)){
					NamedNodeMap attributes = child.getAttributes();
					Node attribute = attributes.getNamedItem(attributeName);
					if(attribute != null){
						if(Pattern.matches(attributePattern, attribute.getNodeValue())){
							return true;
						}
					}
				}
			}
		}
		return false;
	}
}

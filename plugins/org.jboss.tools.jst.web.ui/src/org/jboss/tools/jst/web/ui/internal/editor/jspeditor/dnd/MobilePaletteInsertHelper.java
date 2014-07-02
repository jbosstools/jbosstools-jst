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
package org.jboss.tools.jst.web.ui.internal.editor.jspeditor.dnd;

import java.util.List;
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
import org.jboss.tools.common.model.ui.editors.dnd.IElementGenerator;
import org.jboss.tools.common.model.ui.views.palette.PaletteInsertHelper;
import org.jboss.tools.common.refactoring.MarkerResolutionUtils;
import org.jboss.tools.jst.web.kb.internal.JQueryRecognizer;
import org.jboss.tools.jst.web.kb.internal.taglib.html.jq.JQueryMobileVersion;
import org.jboss.tools.jst.web.ui.WebUiPlugin;
import org.jboss.tools.jst.web.ui.internal.preferences.js.PreferredJSLibVersions;
import org.jboss.tools.jst.web.ui.palette.model.PaletteModel;
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
	public static final String INSERT_JS_CSS_SIGNATURE = "<jquery.mobile.js.css>"; //$NON-NLS-1$
	
	private static final String MOBILE_PATH = "/Mobile/jQuery Mobile/"; //$NON-NLS-1$

	private static String link(String href) {
		return "<link rel=\"stylesheet\" href=\"" + href + "\" /";
	}

	private static String script(String src) {
		return "<script src=\"" + src + "\"></script";
	}

	private static final String META = "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"";

	private static final int INSERT_AFTER_OPEN_NODE = 1;
	private static final int INSERT_AFTER_CLOSE_NODE = 2;
	private static final int INSERT_BEFORE_OPEN_NODE = 3;
	private static final int INSERT_BEFORE_CLOSE_NODE = 4;
	private static final int INSERT_AFTER_ALL = 5;
	
	private StringBuffer globalBuffer = new StringBuffer();
	private int globalPosition = -1, goobalLength = 0;

	private IDOMNode doctypeNode = null, htmlNode = null, headNode = null, bodyNode = null;
	private String baseNodeIndent = "";
	
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
		
		if(insert || (p.containsKey(PROPOPERTY_JQUERY_MOBILE_INSERT_JS_CSS)/* && !JQueryRecognizer.containsJQueryJSReference(file)*/)) {
			insertJsCss(v, p.get(PROPOPERTY_JQUERY_MOBILE_INSERT_JS_CSS));
			if(insert){
				texts[0] = "";	
			}
		}
	}

//	private Object getJQueryMobileVersion(Properties p) {
//		if(p.containsKey(SharableConstants.PALETTE_PATH)) {
//			String path = p.getProperty(SharableConstants.PALETTE_PATH);
//			for (JQueryMobileVersion v: JQueryMobileVersion.ALL_VERSIONS) {
//				if(path.indexOf(PaletteModel.VERSION_PREFIX + v.toString()) > 0) {
//					return v;
//				}
//			}
//		}
//		return JQueryMobileVersion.getLatestDefaultVersion();
//	}
	
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
	
	private void insertNode(IDocument document, IDOMNode relatedNode, IDOMNode node, int indentWidth, String text, int mode, boolean forceWrite) throws BadLocationException{
		boolean newLineBefore = true;
		boolean newLineAfter = false;
		boolean copyAttributes = false;
		
		String lineDelimiter = getLineDelimiter(document);
		int tabWidth = IElementGenerator.NodeWriter.getTabWidth();
		
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
					globalPosition = relatedNode.getEndStructuredDocumentRegion().getStartOffset();
					globalPosition -= countSpacesBeforeIncludingNewLine(document, globalPosition);
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
					globalPosition = relatedNode.getStartStructuredDocumentRegion().getStartOffset();
					if(!checkForNewLine(document, globalPosition)){
						newLineAfter = true;
					}
					globalPosition -= countSpacesBeforeIncludingNewLine(document, globalPosition);
					goobalLength = 0;
					newLineBefore = true;
				}
			}
		}
		
		if(newLineBefore){
			globalBuffer.append(lineDelimiter);
			globalBuffer.append(baseNodeIndent);
			String indent = createIndent(indentWidth*tabWidth);
			globalBuffer.append(indent);
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
			globalBuffer.append(lineDelimiter);
		}
		
		if(forceWrite){
			writeBuffer(document);
		}
	}
	
	private String getNodeIndent(IDocument document, IDOMNode node){
		if(node.getStartStructuredDocumentRegion() != null){
			int offset = node.getStartStructuredDocumentRegion().getStartOffset();
			int number = countSpacesBefore(document, offset);
			int position = offset - number;
			if(position < offset && position >= 0){
				try{
					String spaces = document.get(position, number);
					return spaces;
				}catch(BadLocationException ex){
					WebUiPlugin.getDefault().logError(ex);
				}
			}
		}
		return "";
	}
	
	private int countSpacesBefore(IDocument document, int offset){
		int position = offset -1;
		int number = 0;
		try{
			while(position >= 0){
				String text = document.get(position, 1);
				if(!text.equals(" ") && !text.equals("\t")){
					return number;
				}
				number++;
				position--;
			}
		}catch(BadLocationException ex){
			WebUiPlugin.getDefault().logError(ex);
		}
		
		return number;
	}
	
	private boolean checkForNewLine(IDocument document, int offset){
		int position = offset -1;
		try{
			while(position >= 0){
				String text = document.get(position, 1);
				if(text.equals("\n")){
					return true;
				}
				if(!text.trim().equals("")){
					return false;
				}
				position--;
			}
		}catch(BadLocationException ex){
			WebUiPlugin.getDefault().logError(ex);
		}
		
		return false;
	}
	
	private int countSpacesBeforeIncludingNewLine(IDocument document, int offset){
		int position = offset -1;
		int number = 0;
		try{
			while(position >= 0){
				String text = document.get(position, 1);
				if(!text.trim().equals("")){
					return number;
				}
				number++;
				position--;
				if(text.equals("\n")){
					return number;
				}
			}
		}catch(BadLocationException ex){
			WebUiPlugin.getDefault().logError(ex);
		}
		return number;
	}
	
	private void insertJsCss(ISourceViewer viewer, Object version) {
		IDocument document = viewer.getDocument();
		
		IStructuredModel model = null;
		try{
			model = StructuredModelManager.getModelManager().getExistingModelForRead((IStructuredDocument)document);
			IDOMDocument xmlDocument = (model instanceof IDOMModel) ? ((IDOMModel) model).getDocument() : null;
			if(xmlDocument != null){
				
				// analyze xml document
				doctypeNode = findDocumentType(xmlDocument, "html");
				
				htmlNode = findNode(xmlDocument, "html");
				
				baseNodeIndent = "";
				
				if(htmlNode == null){
					bodyNode = findNode(xmlDocument, "body");
					headNode = findNode(xmlDocument, "head");
				}else{
					bodyNode = findNode(htmlNode, "body");
					
					headNode = findNode(htmlNode, "head");
					if(headNode != null){
						baseNodeIndent = getNodeIndent(document, headNode);
					}else{
						baseNodeIndent = getNodeIndent(document, htmlNode);
					}
				}
				
				boolean metaExists = checkNode(headNode, "meta", "name", "(viewport)");
				
				PreferredJSLibVersions preferredVersions = new PreferredJSLibVersions(MarkerResolutionUtils.getFile(), version);
				preferredVersions.updateLibEnablementAndSelection();
				String[][] urls = preferredVersions.getURLs(headNode);
				
//				boolean linkExists = checkNode(headNode, "link", "href", ".*(jquery\\.mobile-).*(.css)");
//				
//				boolean firstScriptExists = checkNode(headNode, "script", "src", ".*(jquery-).*(.js)");
//				
//				boolean secondScriptExists = checkNode(headNode, "script", "src", ".*(jquery.mobile-).*(.js)");
				
				// insert tags if needed
				insertNode(document, doctypeNode, htmlNode, 0, "<html", INSERT_AFTER_OPEN_NODE, false);
				
				insertNode(document, htmlNode, headNode, 0, "<head", INSERT_AFTER_OPEN_NODE, false);
				
				if(!metaExists){
					insertNode(document, headNode, null, 1, META, INSERT_AFTER_OPEN_NODE, false);
				}
				
				for (String css: urls[0]) {
					insertNode(document, headNode, null, 1, link(css), INSERT_AFTER_OPEN_NODE, false);
				}
				for (String js: urls[1]) {
					insertNode(document, headNode, null, 1, script(js), INSERT_AFTER_OPEN_NODE, false);
				}
//				if(!linkExists){
//					insertNode(document, headNode, null, 1, link(version.getCSS()), INSERT_AFTER_OPEN_NODE, false);
//				}
//				if(!firstScriptExists){
//					insertNode(document, headNode, null, 1, script(version.getJQueryJS()), INSERT_AFTER_OPEN_NODE, false);
//				}
//				if(!secondScriptExists){
//					insertNode(document, headNode, null, 1, script(version.getJQueryMobileJS()), INSERT_AFTER_OPEN_NODE, false);
//				}
				
				insertNode(document, bodyNode, headNode, 0, "</head", INSERT_BEFORE_OPEN_NODE, false);

				insertNode(document, headNode, bodyNode, 0, "<body", INSERT_AFTER_CLOSE_NODE, false);

				insertNode(document, htmlNode, bodyNode, 0, "</body", INSERT_BEFORE_CLOSE_NODE, false);

				insertNode(document, null, htmlNode, 0, "</html", INSERT_AFTER_ALL, true);
			}
		} catch (BadLocationException e) {
			WebUiPlugin.getDefault().logError(e);
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

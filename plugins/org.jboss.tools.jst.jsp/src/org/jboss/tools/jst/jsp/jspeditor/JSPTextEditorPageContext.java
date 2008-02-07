/******************************************************************************* 

* Copyright (c) 2007 Red Hat, Inc.
* Distributed under license by Red Hat, Inc. All rights reserved.
* This program is made available under the terms of the
* Eclipse Public License v1.0 which accompanies this distribution,
* and is available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*     Red Hat, Inc. - initial API and implementation
******************************************************************************/
package org.jboss.tools.jst.jsp.jspeditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.text.IDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.jboss.tools.common.kb.KbConnectorFactory;
import org.jboss.tools.common.kb.KbConnectorType;
import org.jboss.tools.common.kb.wtp.WtpKbConnector;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.editor.IVisualContext;
import org.jboss.tools.jst.web.tld.TaglibData;
import org.jboss.tools.jst.web.tld.VpeTaglibListener;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Vitali (vyemialyanchyk@exadel.com)
 *
 * Class responsible to register TagLib data suitable for the current context 
 */
public class JSPTextEditorPageContext implements IVisualContext {

	protected ArrayList<TaglibData> taglibs = new ArrayList<TaglibData>();
	protected Map<String, String> taglibMap = new HashMap<String, String>();
	// this is just reference to VpeTemplateManager.templateTaglibs
	private Map<String,String> templateTaglibs = null;
	protected WtpKbConnector connector = null;
	//protected IDocument document = null;
	protected IDOMDocument document = null;
	protected Node referenceNode = null;
	protected int freeID = 0;
	protected ArrayList<VpeTaglibListener> taglibListeners = new ArrayList<VpeTaglibListener>();
	
	public JSPTextEditorPageContext() {
		// simple tests
		//addTaglib(123, "vitaliNewUri", "vitaliNewPrefix", true);
		//addTaglib(123, "http://java.sun.com/jsf/facelets", "1xmlns:ui11", true);
		//addTaglib(234, "http://java.sun.com/jsf/html", "2xmlns:ui22", true);
		//addTaglib(345, "http://richfaces.org/rich", "3xmlns:ui33", true);
	}

	public void clearAll() {
		taglibs.clear();
		taglibMap.clear();
		templateTaglibs = null;
	}
	
	public void dispose() {
		clearAll();
		connector = null;
		document = null;
		referenceNode = null;
		taglibListeners = null;
	}

	public void setReferenceNode(Node refNode) {
		if (referenceNode == refNode) {
			return;
		}
		referenceNode = refNode;
		updateTagLibs();
	}
	
	//public void setDocument(IDocument doc) {
	public void setDocument(IDOMDocument doc) {
		if (document == doc) {
			return;
		}
		document = doc;
		setReferenceNode(document);
		try {
			connector = (WtpKbConnector)KbConnectorFactory.getIntstance().createConnector(KbConnectorType.JSP_WTP_KB_CONNECTOR, document);
		} catch (InstantiationException e) {
			JspEditorPlugin.getPluginLog().logError(e);
		} catch (IllegalAccessException e) {
			JspEditorPlugin.getPluginLog().logError(e);
		} catch (ClassNotFoundException e) {
			JspEditorPlugin.getPluginLog().logError(e);
		}
		updateTagLibs();
	}
	
	public boolean processAttribute(Attr attr, boolean bScopePrefix) {
		if (null == attr) {
			return false;
		}
		String startStr = "xmlns:";
		String name = attr.getName();
		if (!name.startsWith(startStr)) {
			return false;
		}
		name = name.substring(startStr.length());
		addTaglib(freeID++, attr.getValue(), name, true, bScopePrefix);
		return true;
	}
	
	public boolean processNode(Node node) {
		if (null == node) {
			return false;
		}
		String nodeName = node.getNodeName();
		// example: add some filter here
		//if (!"ui:composition".equalsIgnoreCase(nodeName)) {
		//	return false;
		//}
		boolean bTestRes = false;
		NamedNodeMap attribList = node.getAttributes();
		if (null != attribList) {
			for (int i = 0; i < attribList.getLength(); i++) {
				Node tmp = attribList.item(i);
				bTestRes = processAttribute((Attr)tmp, false);
			}
		}
		NodeList nodeList = node.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node tmp = nodeList.item(i);
			bTestRes = processNode(tmp);
		}
		return true;
	}
	
	public void collectDocumentTagLibs() {
		if (null == document) {
			return;
		}
		NodeList nodeList = document.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			processNode(node);
		}
	}

	public boolean processNodeUp(Node node) {
		if (null == node) {
			return false;
		}
		boolean bTestRes = false;
		NamedNodeMap attribList = node.getAttributes();
		if (null != attribList) {
			for (int i = 0; i < attribList.getLength(); i++) {
				Node tmp = attribList.item(i);
				bTestRes = processAttribute((Attr)tmp, true);
			}
		}
		bTestRes = processNodeUp(node.getParentNode());
		return true;
	}
	
	public void collectRefNodeTagLibs() {
		processNodeUp(referenceNode);
	}
	
	public void updateTagLibs() {
		taglibs.clear();
		freeID = 0;
		// collect all taglibs for the document - from top tree node to leafs
		//collectDocumentTagLibs();
		// collect all taglibs starting from reference node through its parents to the top node
		// parent's taglibs has lower priority then child taglibs
		collectRefNodeTagLibs();
		//
		for (Iterator<VpeTaglibListener> it = taglibListeners.iterator(); it.hasNext(); ) {
			it.next().taglibPrefixChanged(null);
		}
		rebuildTaglibMap();
	}

	private boolean rebuildTaglibMap() {
		taglibMap.clear();
		if (null == templateTaglibs) {
			return false;
		}
		Set<String> prefixSet = new HashSet<String>();
		for (int i = 0; i < taglibs.size(); i++) {
			TaglibData taglib = (TaglibData)taglibs.get(i);
			String prefix = taglib.getPrefix();
			if (!prefixSet.contains(prefix)) {
				String templatePrefix = templateTaglibs.get(taglib.getUri());
				if (templatePrefix != null) {
					taglibMap.put(prefix, templatePrefix);
				}
				prefixSet.add(prefix);
			}
		}
		return true;
	}

	/**
	 * This is a way to use templateTaglibs from
	 * org.jboss.tools.vpe.editor.template.VpeTemplateManager;
	 * this is just reference to VpeTemplateManager.templateTaglibs
	 * getter is prohibited here 
	 **/
	public void setTemplateTaglibs(Map<String,String> templateTaglibs) {
		this.templateTaglibs = templateTaglibs;
		rebuildTaglibMap();
	}

	/**
	 * Return template taglib prefix using prefix as a key.
	 * @return
	 */
	public String getTemplateTaglibPrefix(String sourceTaglibPrefix) {
		return taglibMap.get(sourceTaglibPrefix);
	}

	// adds new tag library
	// removes tag library with id - if newUri or newPrefix is null
	public void addTaglib(int id, String newUri, String newPrefix, boolean ns, boolean bScopePrefix) {
		if (newUri == null || newPrefix == null) {
			for (int i = 0; i < taglibs.size(); i++) {
				TaglibData taglib = (TaglibData)taglibs.get(i);
				if (taglib.getId() == id) {
					taglibs.remove(i);
					break;
				}
			}
			return;
		}
		boolean bHasSame = false;
		for (int i = 0; i < taglibs.size(); i++) {
			TaglibData taglib = (TaglibData)taglibs.get(i);
			if (taglib.getId() == id) {
				if (!newUri.equals(taglib.getUri()) || !newPrefix.equals(taglib.getPrefix())) {
					taglibs.set(i, new TaglibData(id, newUri, newPrefix, ns));
				}
				return;
			}
			if (bScopePrefix && newPrefix.equals(taglib.getPrefix())) {
				return;
			}
			if (newUri.equals(taglib.getUri()) && newPrefix.equals(taglib.getPrefix()) && ns == taglib.isNs()) {
				bHasSame = true;
				break;
			}
		}
		if (!bHasSame) {
			taglibs.add(new TaglibData(id, newUri, newPrefix, ns));
		}
	}

	// implements IVisualContext
	public WtpKbConnector getConnector() {
		return connector;
	}

	// implements IVisualContext
	public void refreshBundleValues() {
		updateTagLibs();
	}

	// implements IVisualContext
	public List<TaglibData> getTagLibs() {
		List<TaglibData> clone = new ArrayList<TaglibData>();
		Iterator<TaglibData> iter = taglibs.iterator();
		while (iter.hasNext()) {
			TaglibData taglib = (TaglibData)iter.next();
			//if (!taglib.inList(clone)) {
			if (!clone.contains(taglib)) {
				clone.add(taglib);
			}
		}
		return clone;
	}

	// implements IVisualContext
	public void addTaglibListener(VpeTaglibListener listener) {
		if (taglibListeners.contains(listener)) {
			return;
		}
		taglibListeners.add(listener);
	}

	// implements IVisualContext
	public void removeTaglibListener(VpeTaglibListener listener) {
		taglibListeners.remove(listener);
	}

}

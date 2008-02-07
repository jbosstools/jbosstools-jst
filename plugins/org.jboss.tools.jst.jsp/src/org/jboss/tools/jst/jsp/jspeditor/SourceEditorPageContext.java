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
import java.util.Iterator;
import java.util.List;
import org.jboss.tools.common.kb.KbConnectorFactory;
import org.jboss.tools.common.kb.KbConnectorType;
import org.jboss.tools.common.kb.wtp.WtpKbConnector;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.editor.IVisualContext;
import org.jboss.tools.jst.jsp.util.XmlUtil;
import org.jboss.tools.jst.web.tld.TaglibData;
import org.jboss.tools.jst.web.tld.VpeTaglibListener;
import org.jboss.tools.jst.web.tld.VpeTaglibManager;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Max Areshkau (mareshkau@exadel.com)
 *
 * Class responsible to register TagLib data suitable for the current context 
 */
public class SourceEditorPageContext implements IVisualContext,VpeTaglibManager {

	protected List<TaglibData> taglibs = null;
	
	private WtpKbConnector connector = null;
	private Node referenceNode = null;
	
	public void clearAll() {
		
		setTaglibs(null);
	}
	
	public void dispose() {
		clearAll();
		connector = null;
		referenceNode = null;

	}
	/**
	 * Sets current node in scope of which we will be call context assistent
	 * @param refNode
	 */
	public void setReferenceNode(Node refNode) {
	
		if ((refNode==null)||(refNode.equals(referenceNode))) {
			return;
		}
			referenceNode = refNode;
			updateTagLibs();
	
	}
	
	public void setDocument(Document doc) {
		
//		setReferenceNode(doc);
		try {
			connector = (WtpKbConnector)KbConnectorFactory.getIntstance().createConnector(KbConnectorType.JSP_WTP_KB_CONNECTOR, doc);
		} catch (InstantiationException e) {
			JspEditorPlugin.getPluginLog().logError(e);
		} catch (IllegalAccessException e) {
			JspEditorPlugin.getPluginLog().logError(e);
		} catch (ClassNotFoundException e) {
			JspEditorPlugin.getPluginLog().logError(e);
		}
	}

	public void collectRefNodeTagLibs() {
		
		if(referenceNode==null) {
			return;
		}
			
		if(referenceNode.getNodeType()==Node.DOCUMENT_NODE) {
			NodeList nodes =referenceNode.getChildNodes();
			
			for(int i=0;i<nodes.getLength();i++) {
				Node node =nodes.item(i);
				List<TaglibData> result =XmlUtil.processNode(node);
				if(result!=null&&result.size()>0) {
					setTaglibs(result);
					break;
				}
			}
		} else {
			
			setTaglibs(XmlUtil.processNode(referenceNode));
		}
	}
	
	public void updateTagLibs() {
		collectRefNodeTagLibs();
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

		Iterator<TaglibData> iter = getTaglibs() .iterator();
		while (iter.hasNext()) {
			TaglibData taglib = (TaglibData)iter.next();
			if (!taglib.inList(clone)) {
				clone.add(taglib);
			}
		}
		return clone;
	}

	// implements IVisualContext
	public void addTaglibListener(VpeTaglibListener listener) {
		//just a stub
	}

	// implements IVisualContext
	public void removeTaglibListener(VpeTaglibListener listener) {
		//just a stub
	}

	/**
	 * @return the taglibs
	 */
	private List<TaglibData> getTaglibs() {
		if(taglibs==null) {
			taglibs= new ArrayList<TaglibData>();
		}
		return taglibs;
	}

	/**
	 * @param taglibs the taglibs to set
	 */
	private void setTaglibs(List<TaglibData> taglibs) {
		this.taglibs = taglibs;
	}

}

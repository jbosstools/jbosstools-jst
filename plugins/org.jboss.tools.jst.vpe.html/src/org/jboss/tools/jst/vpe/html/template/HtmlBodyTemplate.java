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
package org.jboss.tools.jst.vpe.html.template;

import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.VpeAbstractTemplate;
import org.jboss.tools.vpe.editor.template.VpeCreationData;
import org.jboss.tools.vpe.editor.util.HTML;
import org.jboss.tools.vpe.editor.util.VpeStyleUtil;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNamedNodeMap;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.xpcom.XPCOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * @author ezheleznyakov@exadel.com
 * 
 */
public class HtmlBodyTemplate extends VpeAbstractTemplate {

	private nsIDOMElement bodyOld;
	private static String STYLE_FOR_DIV = ""; //$NON-NLS-1$
	private static String ID = "id"; //$NON-NLS-1$

	/**
	 * 
	 */
	public VpeCreationData create(VpePageContext pageContext, Node sourceNode,
			nsIDOMDocument visualDocument) {

		goToTree(visualDocument.getChildNodes().item(0));

		nsIDOMNamedNodeMap attrsMap = bodyOld.getAttributes();
		long len = attrsMap.getLength();
		int j = 0;
		for (int i = 0; i < len; i++) {
			nsIDOMNode attr = attrsMap.item(j);
			if (ID.equalsIgnoreCase(attr.getNodeName())) {
				j++;
				continue;
			}
			bodyOld.removeAttribute(attr.getNodeName());
		}

		for (int i = 0; i < sourceNode.getAttributes().getLength(); i++) {
			String name = sourceNode.getAttributes().item(i).getNodeName();
			if(ID.equalsIgnoreCase(name))
				continue;
			String value = sourceNode.getAttributes().item(i).getNodeValue();
			// all full path for 'url'
			if (VpeStyleUtil.ATTRIBUTE_STYLE.equalsIgnoreCase(name))
				value = VpeStyleUtil.addFullPathIntoURLValue(value, pageContext
						.getEditPart().getEditorInput());
			if (VpeStyleUtil.PARAMETR_BACKGROND.equalsIgnoreCase(name))
				value = VpeStyleUtil.addFullPathIntoBackgroundValue(value,
						pageContext.getEditPart().getEditorInput());
			//FIX FOR JBIDE-1568, added by Max Areshkau
			try{
				bodyOld.setAttribute(name, value);
			}catch(XPCOMException ex ) {
				//jsut ignore it
			}
			
		}

		nsIDOMElement div = visualDocument.createElement(HTML.TAG_DIV);
		div.setAttribute(VpeStyleUtil.ATTRIBUTE_STYLE, STYLE_FOR_DIV);

		return new VpeCreationData(div);
	}

	/**
	 * 
	 * @param node
	 */
	private void goToTree(nsIDOMNode node) {

		for (int i = 0; i < node.getChildNodes().getLength(); i++)
			if (HTML.TAG_BODY.equalsIgnoreCase(node.getChildNodes().item(i)
					.getNodeName()))
				bodyOld = (nsIDOMElement) node.getChildNodes().item(i)
						.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
			else
				goToTree(node.getChildNodes().item(i));
	}

	/**
	 * Checks, whether it is necessary to re-create an element at change of
	 * attribute
	 * 
	 * @param pageContext
	 *            Contains the information on edited page.
	 * @param sourceElement
	 *            The current element of the source tree.
	 * @param visualDocument
	 *            The document of the visual tree.
	 * @param visualNode
	 *            The current node of the visual tree.
	 * @param data
	 *            The arbitrary data, built by a method <code>create</code>
	 * @param name
	 *            Attribute name
	 * @param value
	 *            Attribute value
	 * @return <code>true</code> if it is required to re-create an element at
	 *         a modification of attribute, <code>false</code> otherwise.
	 */
	public boolean isRecreateAtAttrChange(VpePageContext pageContext,
			Element sourceElement, nsIDOMDocument visualDocument,
			nsIDOMElement visualNode, Object data, String name, String value) {
		return true;
	}
}
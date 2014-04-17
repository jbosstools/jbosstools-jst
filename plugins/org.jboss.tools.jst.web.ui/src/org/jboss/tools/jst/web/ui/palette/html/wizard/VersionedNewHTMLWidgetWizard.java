/******************************************************************************* 
 * Copyright (c) 2014 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.palette.html.wizard;

import org.jboss.tools.common.model.ui.editors.dnd.IElementGenerator.ElementNode;
import org.jboss.tools.common.model.ui.editors.dnd.IElementGenerator.NodeWriter;


/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public abstract class VersionedNewHTMLWidgetWizard<V, P extends VersionedNewHTMLWidgetWizardPage> extends AbstractNewHTMLWidgetWizard {
	protected V version;
	protected P page;

	public VersionedNewHTMLWidgetWizard(V defaultVersion) {
		version = defaultVersion;
	}

	public V getVersion() {
		return version;
	}

	protected abstract P createPage(); 

	protected void doAddPages() {
		page = createPage();
		addPage(page);
	}

	protected boolean isTrue(String editorID) {
		return TRUE.equals(page.getEditorValue(editorID));
	}

	protected String getID(String prefix) {
		if(!page.isIDEnabled()) {
			return null;
		}
		String id = page.getEditorValue(EDITOR_ID_ID);
		if(id.length() == 0) {
			int i = generateIndex(prefix, "", 1);
			id = prefix + i;
		}
		return id;
	}

	protected String addID(String prefix, ElementNode node) {
		String id = getID(prefix);
		if(id != null) {
			node.addAttribute(ATTR_ID, id);
		}
		return id;
	}

	@Override
	public String getTextForBrowser() {
		ElementNode html = new ElementNode(TAG_HTML, false);
		createHead(html);
		ElementNode body = html.addChild(TAG_BODY);
		createBodyForBrowser(body);

		NodeWriter w = new NodeWriter(false);
		html.flush(w, 0);

		StringBuilder sb = new StringBuilder();
		sb.append(DOCTYPE).append("\n").append(w.getText());

		return sb.toString();
	}

	/**
	 * Override to wrap content.
	 * @param body
	 */
	protected void createBodyForBrowser(ElementNode body) {
		addContent(body);
	}

	protected void createHead(ElementNode html) {
	}

	protected ElementNode getFormNode(ElementNode parent) {
		ElementNode form = parent.addChild(TAG_FORM);
		form.addAttribute(ATTR_ACTION, "#");
		form.addAttribute(ATTR_METHOD, METHOD_GET);
		return form;
	}

	protected static void addClass(StringBuilder cls, String add) {
		if(cls.length() > 0) {
			cls.append(" ");
		}
		cls.append(add);
	}

	protected void addAttributeIfNotEmpty(ElementNode n, String attrName, String editorID) {
		String value = page.getEditorValue(editorID);
		if(value != null && value.length() > 0) {
			n.addAttribute(attrName, value);
		}
	}

}

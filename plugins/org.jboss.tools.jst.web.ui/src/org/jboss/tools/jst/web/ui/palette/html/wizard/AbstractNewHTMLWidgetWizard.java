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
package org.jboss.tools.jst.web.ui.palette.html.wizard;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.jboss.tools.common.model.ui.editors.dnd.DefaultDropWizardPage;
import org.jboss.tools.common.model.ui.editors.dnd.IDropCommand;
import org.jboss.tools.common.model.ui.editors.dnd.IDropWizard;
import org.jboss.tools.common.model.ui.editors.dnd.IDropWizardModel;
import org.jboss.tools.jst.jsp.jspeditor.dnd.PaletteDropCommand;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class AbstractNewHTMLWidgetWizard extends Wizard implements PropertyChangeListener, IDropWizard, HTMLConstants {
	protected IDropCommand command;
	Set<String> ids = new HashSet<String>();

	public AbstractNewHTMLWidgetWizard() {}

	@Override
	public final void addPages() {
		doAddPages();
		getWizardModel().addPropertyChangeListener(this);		
	}

	protected void doAddPages() {
		
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		IWizardPage[] pages = getPages();
		for (int i = 0; i < pages.length; i++) {
			DefaultDropWizardPage page = (DefaultDropWizardPage)pages[i];
			page.runValidation();
		}
	}
	
	@Override
	public boolean performFinish() {
		generateData();
		command.execute();		
		return true;
	}

	/**
	 * Generates data to be inserted into the edited file.
	 * Text is split into startText and endText so that 
	 * after insertion cursor in editor is put between
	 * startText and endText. If there is no need to add to
	 * inner content of the widget, all data may be provided 
	 * in startText.
	 * This method may be overridden by subclasses, but 
	 * in most cases it is not needed.
	 */
	protected void generateData() {
		ElementNode root = createRoot();
		addContent(root);
		NodeWriter w = new NodeWriter(true);
		root.flush(w, 0);
		String[] result = w.getResult();
		String startText = result[0];
		if(startText.length() > 0) {
			getCommandProperties().setProperty("start text", startText);
		}
		String endText = result.length < 2 ? "" : result[1];
		if(endText.length() > 0) {
			getCommandProperties().setProperty("end text", endText);
		}
	}

	protected Properties getCommandProperties() {
		return ((PaletteDropCommand)command).getProperties();
	}

	/**
	 * Should be overrided to provide html presentation of the widget. 
	 * @return
	 */
	protected void addContent(ElementNode parent) {
	}

	/**
	 * Should be overrided to provide required environment for widget. 
	 * @return
	 */
	public String getTextForBrowser() {
		ElementNode html = new ElementNode(TAG_HTML, false);
		ElementNode body = html.addChild(TAG_BODY);
		addContent(body);
		NodeWriter sb = new NodeWriter(false);
		body.flush(sb, 0);
		return sb.getText();
	}

	public String getTextForTextView() {
		ElementNode root = createRoot();
		addContent(root);
		NodeWriter sb = new NodeWriter(false);
		root.flush(sb, 0);
		return sb.getText();
	}
	@Override
	public void setCommand(IDropCommand command) {
		this.command = command;
		collectAllIDs();
	}

	public boolean isIDAvailable(String id) {
		return !ids.contains(id);
	}

	@Override
	public IDropWizardModel getWizardModel() {
		return command.getDefaultModel();
	}

	public void dispose() {
		getWizardModel().removePropertyChangeListener(this);
		super.dispose();
	}

	/**
	 * Finds index such that String id = maskPrefix + index + maskSuffix;
	 * will be new id and/or name on the page.
	 * 
	 * @param maskPrefix
	 * @param maskSuffix
	 * @param countFrom
	 * @return
	 */
	protected int generateIndex(String maskPrefix, String maskSuffix, int countFrom) {
		while(ids.contains(maskPrefix + countFrom + maskSuffix)) countFrom++;
		return countFrom;
	}

	/**
	 * Collects all values of attributes 'id' and 'name' on the page.
	 * Result set is used by generateIndex.
	 * @see generateIndex()
	 */
	private void collectAllIDs() {
		String text = command.getDefaultModel().getDropData().getSourceViewer().getDocument().get();
		int index = 0;
		while(index >= 0 && index < text.length()) {
			int i1 = next(text, ATTR_NAME, index);
			int i2 = next(text, ATTR_ID, index);
			index = (i1 < 0) ? i2 : (i2 < 0) ? i1 : (i1 < i2) ? i1 : i2;
			if(index < 0) return;
			readValue(text, index);
		}
	}

	private int next(String text, String attrName, int from) {
		int i = text.indexOf(attrName, from);
		while(i >= 0) {
			if(i == 0 || Character.isWhitespace(text.charAt(i - 1))) {
				i += attrName.length();
				for (; ; i++) {
					if(i >= text.length()) {
						return -1;
					}
					char ch = text.charAt(i);
					if(ch == '=') {
						return i;
					}
					if(!Character.isWhitespace(ch)) {
						break;
					}
				}
			}
			i = text.indexOf(attrName, i + attrName.length());
		}
		return -1;
	}
	private void readValue(String text, int from) {
		int i = text.indexOf("\"", from);
		if(i < 0) {
			return;
		}
		int j = text.indexOf("\"", i + 1);
		if(j < 0) {
			return;
		}
		String value = text.substring(i + 1, j).trim();
		ids.add(value);
	}

	public static ElementNode createRoot() {
		return new RootNode();
	}
	
	static class RootNode extends ElementNode {
		public RootNode() {
			super(null, false);
		}
		public void flush(NodeWriter sb, int indent) {
			for (ElementNode c: children) {
				c.flush(sb, indent);
			}
		}
	}

	protected static ElementNode SEPARATOR = new ElementNode(null, true) {
		public void flush(NodeWriter writer, int indent) {
			writer.next();
		}
	};

	public static class NodeWriter {
		List<StringBuilder> builders = new ArrayList<StringBuilder>();
		StringBuilder current = null;
		boolean separate = true;

		public NodeWriter(boolean separate) {
			next();
			this.separate = separate;
		}
		
		public void next() {
			if(separate) {
				while(current != null && current.length() > 0 && (current.charAt(current.length() - 1) == '\r'
						|| current.charAt(current.length() - 1) == '\n')) {
					current.setLength(current.length() - 1);
				}
				current = new StringBuilder();
				builders.add(current);
			}
		}
		public NodeWriter append(String s) {
			current.append(s);
			return this;
		}

		public String[] getResult() {
			String[] result = new String[builders.size()];
			for (int i = 0; i < result.length; i++) result[i] = builders.get(i).toString(); 
			return result;
		}

		public String getText() {
			if(builders.size() == 1) {
				return builders.get(0).toString();
			}
			StringBuilder result = new StringBuilder();
			for (int i = 0; i < builders.size(); i++) result.append(builders.get(i).toString());
			return result.toString();
		}
	}

	/**
	 * Stores in a simple way html content to be flushed into text.
	 *
	 */
	public static class ElementNode {
		String name;
		List<AttributeNode> attributes = new ArrayList<AttributeNode>();
		List<ElementNode> children = new ArrayList<ElementNode>();
		boolean empty;
		String text = null;

		public ElementNode(String name, boolean empty) {
			this.name = name;
			this.empty = empty;
		}

		public ElementNode(String name, String text) {
			this.name = name;
			this.text = (text == null) ? null : escapeHtml(text, false);
			this.empty = text == null;
		}

		public void addAttribute(String name, String value) {
			attributes.add(new AttributeNode(name, value));
		}

		public ElementNode addChild(String name) {
			ElementNode c = new ElementNode(name, true);
			children.add(c);
			empty = false;
			return c;
		}
		 
		public ElementNode addChild(String name, String text) {
			ElementNode c = new ElementNode(name, text);
			children.add(c);
			empty = false;
			return c;
		}
	
		public List<ElementNode> getChildren() {
			return children;
		}
		 
		public void flush(NodeWriter sb, int indent) {
			if(indent >= 0) {
				addIndent(sb, indent);
			}
			sb.append("<").append(name);
			for (AttributeNode a: attributes) {
				a.flush(sb);
			}
			if(empty) {
				sb.append("/>");
			} else if(text != null) {
				sb.append(">");
				for (ElementNode c: children) {
					c.flush(sb, -1);
				}
				sb.append(text).append("</").append(name).append(">");
			} else {
				sb.append(">").append("\n");
				for (ElementNode c: children) {
					c.flush(sb, indent + 1);
				}
				addIndent(sb, indent);
				sb.append("</").append(name).append(">");
			}
			if(indent >= 0) {
				sb.append("\n");
			}
		}
		private void addIndent(NodeWriter sb, int indent) {
			for (int i = 0; i < indent; i++) {
				sb.append("  ");
			}
		}
	}

	public static class AttributeNode {
		String name;
		String value;
		public AttributeNode(String name, String value) {
			this.name = name;
			this.value = escapeHtml(value, true);
		}

		public void flush(NodeWriter sb) {
			sb.append(" ").append(name).append("=\"").append(value).append("\"");
		}
	}

	public static String escapeHtml(String text, boolean isAttribute) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < text.length(); i++) {
			char ch = text.charAt(i);
			if(ch == '<') {
				sb.append("&lt;");
			} else if(ch == '>') {
				sb.append("&gt;");
			} else if(ch == '&' && !isEscapedSequence(text, i)) {
				sb.append("&amp;");
			} else if(isAttribute && ch == '"') {
				sb.append("&quot;");
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	static boolean isEscapedSequence(String text, int p) {
		if(text.charAt(p) != '&') {
			return false;
		}
		for (int i = p + 1; i < text.length(); i++) {
			char ch = text.charAt(i);
			if(ch == '&') return false;
			if(ch == ';') return true;
		}
		return false;
	}
}


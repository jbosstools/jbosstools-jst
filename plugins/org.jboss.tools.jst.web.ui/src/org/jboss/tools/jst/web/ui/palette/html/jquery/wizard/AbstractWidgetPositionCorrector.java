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
package org.jboss.tools.jst.web.ui.palette.html.jquery.wizard;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.xml.core.internal.document.ElementImpl;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.jboss.tools.common.model.ui.views.palette.IPositionCorrector;
import org.jboss.tools.common.xml.XMLUtilities;
import org.jboss.tools.jst.web.html.JQueryHTMLConstants;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
@SuppressWarnings("restriction")
public abstract class AbstractWidgetPositionCorrector implements IPositionCorrector, JQueryHTMLConstants {
	protected IDocument fDocument;

	protected Element body;
	
	//node for selection offset
	protected Node c;
	
	//selection offset
	int offset;

	/**
	 * Default implementation computes body element and element 'c' that contains offset of the selection.
	 * for non-null elements, inner implementation doCorrectSelection() is invoked, otherwise, selection 
	 * is returned unmodified.
	 */
	@Override
	public ITextSelection correctSelection(Document document, ITextSelection selection) {
		init(document, selection);
		if(c != null) {
			return doCorrectSelection(selection);
		}
		return selection;
	}

	protected void init(Document document, ITextSelection selection) {
		c = null;
		if(!(document instanceof IDOMDocument) || selection == null) {
			return;
		}
		fDocument = ((IDOMDocument)document).getStructuredDocument();
		Element root = document.getDocumentElement();
		if(root == null) {
			return;
		}
		body = XMLUtilities.getUniqueChild(root, TAG_BODY);
		if(body == null) {
			body = root;
		}

		offset = selection.getOffset();
		IDOMDocument doc = (IDOMDocument)document;
		IndexedRegion region = doc.getModel().getIndexedRegion(offset);

		if(region instanceof Node) {
			c = (Node)region;
		}
	}

	/**
	 * Inner implementation that is called after init().
	 * @param selection
	 * @return
	 */
	abstract protected ITextSelection doCorrectSelection(ITextSelection selection);

	public String getWarningMessage(Document document, ITextSelection selection) {
		return null;
	}

	protected static boolean isSection(Node n) {
		if(n.getAttributes() == null) return false;
		Node a = n.getAttributes().getNamedItem(ATTR_DATA_ROLE);
		if(a instanceof Attr) {
			String dataRole = ((Attr) a).getValue();
			if(ROLE_CONTENT.equals(dataRole)
				|| ROLE_HEADER.equals(dataRole)
				|| ROLE_FOOTER.equals(dataRole)
				|| ROLE_PANEL.equals(dataRole)) {
				return true;
			}
		}
		return false;
	}

	protected static boolean isContentSection(Node n) {
		return isSection(n, ROLE_CONTENT);
	}

	protected static boolean isSection(Node n, String dataRole) {
		if(n.getAttributes() == null) return false;
		Node a = n.getAttributes().getNamedItem(ATTR_DATA_ROLE);
		if(a instanceof Attr) {
			String _dataRole = ((Attr) a).getNodeValue();
			if(dataRole.equals(_dataRole)) {
				return true;
			}
		}
		return false;
	}

	protected static boolean isPage(Node n) {
		if(n.getAttributes() == null) return false;
		Node a = n.getAttributes().getNamedItem(ATTR_DATA_ROLE);
		if(a instanceof Attr) {
			String dataRole = ((Attr) a).getValue();
			if(ROLE_PAGE.equals(dataRole) || ROLE_DIALOG.equals(dataRole)) {
				return true;
			}
		}
		return false;
	}

	protected static boolean hasSectionParent(Node n) {
		Node p = n.getParentNode();
		while(p != null) {
			if(isSection(p)) return true;
			p = p.getParentNode();
		}
		return false;
	}

	protected static Node getPageParent(Node n) {
		Node p = n.getParentNode();
		while(p != null) {
			if(isPage(p)) return p;
			p = p.getParentNode();
		}
		return null;
	}

	protected static boolean isInStartTag(Node n, int offset) {
		if(n instanceof ElementImpl) {
			ElementImpl e = (ElementImpl)n;
			return e.getStartOffset() <= offset && offset <= e.getStartEndOffset();
		}
		return false;
	}

	protected static boolean isInEndTag(Node n, int offset) {
		if(n instanceof ElementImpl) {
			ElementImpl e = (ElementImpl)n;
			return e.getEndStartOffset() <= offset && offset <= e.getEndOffset();
		}
		return false;
	}

	protected static boolean isInside(ElementImpl e, int offset) {
		return e.getStartEndOffset() <= offset && e.getEndStartOffset() >= offset;
	}

	protected static List<ElementImpl> getChildrenWithDatarole(Element parent) {
		return getChildrenWithDatarole(parent, null);
	}

	/**
	 * If datarole is null, all elements with any data-role are returned.
	 * @param parent
	 * @param datarole
	 * @return
	 */
	protected static List<ElementImpl> getChildrenWithDatarole(Element parent, String datarole) {
		List<ElementImpl> result = new ArrayList<ElementImpl>();
		NodeList list = parent.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node n = list.item(i);
			if(n instanceof ElementImpl) {
				ElementImpl c = (ElementImpl)n;
				if(c.hasAttribute(ATTR_DATA_ROLE)) {
					if(datarole == null || datarole.equals(c.getAttribute(ATTR_DATA_ROLE))) {
						result.add(c);
					}
				}
			}
		}
		return result;
	}

	protected static List<ElementImpl> getPages(Element body) {
		List<ElementImpl> result = new ArrayList<ElementImpl>();
		for (Element e: getChildrenWithDatarole(body)) {
			if(isPage(e)) {
				result.add((ElementImpl)e);
			}
		}
		return result;
	}

	protected static List<ElementImpl> getPagesWithSections(Element body) {
		List<ElementImpl> result = new ArrayList<ElementImpl>();
		for (Element e: getChildrenWithDatarole(body)) {
			if(isPage(e) && !getSections(e).isEmpty()) {
				result.add((ElementImpl)e);
			}
		}
		return result;
	}

	protected static List<ElementImpl> getSections(Element page) {
		List<ElementImpl> result = new ArrayList<ElementImpl>();
		for (Element e: getChildrenWithDatarole(page)) {
			if(isSection(e)) {
				result.add((ElementImpl)e);
			}
		}
		return result;
	}

	protected static ElementImpl getContentSection(Element page) {
		return getFirstElement(getChildrenWithDatarole(page, ROLE_CONTENT));
	}

	static ElementImpl getHeaderSection(Element page) {
		return getFirstElement(getChildrenWithDatarole(page, ROLE_HEADER));
	}

	static ElementImpl getFooterSection(Element page) {
		return getFirstElement(getChildrenWithDatarole(page, ROLE_FOOTER));
	}

	static <T> T getFirstElement(List<T> list) {
		return list.isEmpty() ? null : list.get(0);
	}

	protected static List<ElementImpl> getPanels(Element page) {
		return getChildrenWithDatarole(page, ROLE_PANEL);
	}

	protected static int getClosestInnerPosition(ElementImpl e, int offset) {
		int start = e.getStartEndOffset();
		int end = e.getEndStartOffset();
		if(end < 0) return start;
		return offset < start ? start : offset > end ? end : offset;
	}

	protected static int getClosestInnerEdge(ElementImpl e, int offset) {
		int start = e.getStartEndOffset();
		int end = e.getEndStartOffset();
		if(end < 0) return start;
		return getClosest(offset, start, end);
	}

	protected static int getClosestOuterEdge(ElementImpl e, int offset) {
		int start = e.getStartOffset();
		int end = e.getEndOffset();
		return getClosest(offset, start, end);
	}

	protected static int getClosest(int offset, int a, int b) {
		return Math.abs(offset - a) <= Math.abs(b - offset) ? a : b;
	}

	/**
	 * Selects element out of list, closest to the offset.
	 * @param es
	 * @param offset
	 * @return
	 */
	protected static ElementImpl getClosestElement(List<ElementImpl> es, int offset) {
		int bestDistance = Integer.MAX_VALUE;
		ElementImpl result = null;
		for (ElementImpl e: es) {
			if(e.getStartOffset() <= offset && e.getEndOffset() >= offset) {
				return e;
			}
			int o = getClosestInnerPosition(e, offset);
			int dist = Math.abs(o - offset);
			if(dist < bestDistance) {
				bestDistance = dist;
				result = e;
			}
		}
		return result;
	}

}

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
package org.jboss.tools.jst.jsp.selection;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import org.eclipse.swt.graphics.Point;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;

public class SourceSelection {
	private Node startNode;
	private int startOffset;
	private Node endNode;
	private int endOffset;
	private List selectedNodes;
	private Attr focusAttribute;
	private Point focusAttributeRange;

	public SourceSelection(Node startNode, int startOffset, Node endNode, int endOffset) {
		this.startNode = startNode;
		this.startOffset = startOffset;
		this.endNode = endNode;
		this.endOffset = endOffset;
		this.selectedNodes = getSelNodes();
	}

	public List getSelectedNodes() {
		return selectedNodes;
	}

	private List getSelNodes() {
		List nodes = Collections.EMPTY_LIST;
		VpeNodeIterator nodeIterator = new VpeNodeIterator(startNode, endNode);
		Node nextNode = nodeIterator.nextNode();
		if (nextNode != null) nodes = new ArrayList();
//		Node commonAncestor = getCommonAncestor();
		while (nextNode != null) {
			SelectedNodeInfo nextNodeInfo = null;
			int type = nextNode.getNodeType();
			if (type == Node.TEXT_NODE || type == Node.COMMENT_NODE) {
				if (nextNode == startNode && nextNode == endNode) {
					nextNodeInfo = new SelectedNodeInfo(nextNode, startOffset, endOffset);
				} else if (nextNode == startNode) {
					nextNodeInfo = new SelectedNodeInfo(nextNode, startOffset, nextNode.getNodeValue().length());
				} else if (nextNode == endNode) {
					nextNodeInfo = new SelectedNodeInfo(nextNode, 0, endOffset);
				} else {
					nextNodeInfo = new SelectedNodeInfo(nextNode, 0, nextNode.getNodeValue().length());
				}
			} else if (nextNode.getNodeType() == Node.ELEMENT_NODE) {
				if (!inParents(nextNode, endNode)) {
					nextNodeInfo = new SelectedNodeInfo(nextNode);
				}
			}
			if (nextNodeInfo != null) nodes.add(nextNodeInfo);
			nextNode = nodeIterator.nextNode();
		}
		return nodes;
	}

	private boolean inParents(Node node1, Node node2) {
		if (node2 != null) {
			Node p = node2.getParentNode();
			while (p != null) {
				if (p == node1)
					return true;
				p = p.getParentNode();
			}
		}
		return false;

	}

	public Node getCommonAncestor() {
		if (endNode == null)
			return null;

		for (Node na = endNode; na != null; na = na.getParentNode()) {
			for (Node ta = startNode; ta != null; ta = ta.getParentNode()) {
				if (ta == na)
					return ta;
			}
		}

		return null; // not found
	}

	public boolean isOneNodeSelected() {
		return startNode == endNode;
	}

	public static class VpeNodeIterator {
		private Node nextNode;
		private Node endNode;

		public VpeNodeIterator(Node startNode, Node endNode) {
			this.nextNode = startNode;
			this.endNode = endNode;
		}

		public Node nextNode() {
			for (Node node = getNextNode(); node != null; node = getNextNode()) {
				return node;
			}
			return null;
		}

		private final Node getNextNode() {
			if (this.nextNode == null)
				return null;
			Node oldNext = this.nextNode;
			Node child = this.nextNode.getFirstChild();
			if (child != null) {
				this.nextNode = child;
				return oldNext;
			}
			for (Node node = this.nextNode; node != null && !node.equals(endNode); node = node.getParentNode()) {
				Node next = node.getNextSibling();
				if (next != null) {
					this.nextNode = next;
					return oldNext;
				}
			}
			this.nextNode = null;
			return oldNext;
		}
	}

	public Node getEndNode() {
		return endNode;
	}

	public Node getStartNode() {
		return startNode;
	}

	public int getEndOffset() {
		return endOffset;
	}

	public int getStartOffset() {
		return startOffset;
	}

	public boolean isCollapsed() {
		return isOneNodeSelected() && startOffset == endOffset;
	}

	public Node getFocusNode() {
//		return focusNode;
		return endNode;
	}

	public int getFocusOffset() {
		return endOffset;
	}

	public void setEndOffset(int endOffset) {
		this.endOffset = endOffset;
	}

	public void setStartOffset(int startOffset) {
		this.startOffset = startOffset;
	}

	public Attr getFocusAttribute() {
		return focusAttribute;
	}

	public void setFocusAttribute(Attr focusAttribute) {
		this.focusAttribute = focusAttribute;
	}

	public Point getFocusAttributeRange() {
		return focusAttributeRange;
	}

	public void setFocusAttributeRange(Point focusAttributeRange) {
		this.focusAttributeRange = focusAttributeRange;
	}
}

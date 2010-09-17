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

import org.w3c.dom.Node;

public class SelectedNodeInfo {
	private Node node;
	private int startOffset;
	private int endOffset;

	public SelectedNodeInfo(Node node) {
		this.node = node;
	}
	public SelectedNodeInfo(Node node, int startOffset, int endOffset) {
		this(node);
		this.startOffset = startOffset;
		this.endOffset = endOffset;
	}

	public Node getNode() {
		return node;
	}

	public int getStartOffset() {
		return startOffset;
	}

	public int getEndOffset() {
		return endOffset;
	}

	public String toString() {
		if (node != null) {
			return node.toString();
		}
		return super.toString();
	}

}

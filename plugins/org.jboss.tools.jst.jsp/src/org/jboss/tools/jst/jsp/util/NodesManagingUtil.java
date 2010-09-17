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

package org.jboss.tools.jst.jsp.util;

import org.eclipse.swt.graphics.Point;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * @author S.Dzmitrovich
 * 
 */
public class NodesManagingUtil {

	/**
	 * 
	 * @param node
	 * @return
	 */
	public static int getNodeLength(Node node) {

		if ((node instanceof IDOMAttr)
				&& (((IDOMAttr) node).getValueSource() != null)) {
			return ((IDOMAttr) node).getValueSource().length();
		} else if (node instanceof IndexedRegion) {
			return ((IndexedRegion) node).getEndOffset()
					- ((IndexedRegion) node).getStartOffset();
		}
		return 0;
	}

	/**
	 * get start offset of node
	 * 
	 * @param node
	 * @return
	 */
	public static int getStartOffsetNode(Node node) {

		if (node instanceof IDOMAttr) {
			return ((IDOMAttr) node).getValueRegionStartOffset() + 1;
		} else if (node instanceof IndexedRegion) {
			return ((IndexedRegion) node).getStartOffset();
		}
		return 0;
	}

	/**
	 * get end offset of node
	 * 
	 * @param node
	 * @return
	 */
	public static int getEndOffsetNode(Node node) {

		if (node instanceof IDOMAttr) {
			return getStartOffsetNode(node) + getNodeLength(node);
		} else if (node instanceof IndexedRegion) {
			return ((IndexedRegion) node).getEndOffset();
		}
		return 0;
	}

	/**
	 * 
	 * @param node
	 * @return
	 */
	public static Point getNodeRange(Node node) {

		return new Point(getStartOffsetNode(node), getNodeLength(node));

	}

	/**
	 * if position belong to node return true
	 * 
	 * @param node
	 * @param position
	 * @return
	 */
	public static boolean isNodeContainsPosition(Node node, int position) {
		return NodesManagingUtil.getStartOffsetNode(node) <= position
				&& NodesManagingUtil.getEndOffsetNode(node) >= position;
	}

	/**
	 * 
	 * @param pageContext
	 * @param startPosition
	 * @param endPosition
	 * @return
	 */
	public static String getSourceText(Node node) {

		return ((IDOMNode) node).getSource();
	}

}

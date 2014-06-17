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

import java.util.List;

import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.wst.xml.core.internal.document.ElementImpl;
import org.w3c.dom.Node;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
@SuppressWarnings("restriction")
public class NewPanelPositionCorrector extends AbstractWidgetPositionCorrector {

	@Override
	protected ITextSelection doCorrectSelection(ITextSelection selection) {
		ElementImpl page = isPage(c) ? (ElementImpl)c : (ElementImpl)getPageParent(c);
		if(page == null) {
//			page = getClosestElement(getPages(body), offset);
			if(c.getParentNode() == body) {
				return selection;
			} else {
				Node n = c;
				Node p = n.getParentNode();
				while(p != null && p != body) {
					n = p;
					p = n.getParentNode();
				}
				if(p == body) {
					return new TextSelection(fDocument, getClosestOuterEdge((ElementImpl)n, offset), 0);
				} else {
					return new TextSelection(fDocument, getClosestInnerEdge((ElementImpl)body, offset), 0);
				}
			}
		} else {
			List<ElementImpl> panels = getPanels(page);
			ElementImpl panel = getClosestElement(panels, offset);
			if(panel != null) {
				int p1 = getClosestOuterEdge(panel, offset);
				int p2 = getClosestInnerEdge(page, offset);
				return new TextSelection(fDocument, getClosest(offset, p1, p2), 0);
			}
			return new TextSelection(fDocument, getClosestInnerEdge(page, offset), 0);
		}
	}

}

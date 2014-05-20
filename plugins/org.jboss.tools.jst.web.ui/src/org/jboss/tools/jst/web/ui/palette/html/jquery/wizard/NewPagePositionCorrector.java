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

import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.xml.core.internal.document.ElementImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
@SuppressWarnings("restriction")
public class NewPagePositionCorrector extends AbstractWidgetPositionCorrector {

	@Override
	protected ITextSelection doCorrectSelection(ITextSelection selection) {
			Node n = c.getParentNode();
			while(!n.getNodeName().equals(body.getNodeName())) {
				c = n;
				n = n.getParentNode();
				if(n == null) {
					if(body.getTagName().equals(TAG_BODY)) {
						int b = ((ElementImpl)body).getStartEndOffset();
						int e = ((ElementImpl)body).getEndStartOffset();
						if(Math.abs(offset - b) < Math.abs(e - offset)) {
							return new TextSelection(fDocument, b, 0);
						} else {
							return new TextSelection(fDocument, e, 0);
						}
					} else {
						int e = ((ElementImpl)body).getEndStartOffset();
						return new TextSelection(fDocument, e, 0);
					}
				}
			}
			if(c instanceof Text) {
				return selection; //correct place
			} else if(c instanceof Element) {
				int b = ((IndexedRegion)c).getStartOffset();
				int e = ((IndexedRegion)c).getEndOffset();
				if(Math.abs(offset - b) < Math.abs(e - offset)) {
					return new TextSelection(fDocument, b, 0);
				} else {
					return new TextSelection(fDocument, e, 0);
				}
			}

		return selection;
	}

}

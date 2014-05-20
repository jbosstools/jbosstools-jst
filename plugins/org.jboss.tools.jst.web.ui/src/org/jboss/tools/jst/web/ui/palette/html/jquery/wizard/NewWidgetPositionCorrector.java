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
import org.eclipse.wst.xml.core.internal.document.ElementImpl;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;
import org.w3c.dom.Document;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
@SuppressWarnings("restriction")
public class NewWidgetPositionCorrector extends AbstractWidgetPositionCorrector {

	@Override
	protected ITextSelection doCorrectSelection(ITextSelection selection) {
		if(isSection(c)) {
			ElementImpl e = (ElementImpl)c;
			if(isInStartTag(c, offset)) {
				return new TextSelection(fDocument, e.getStartEndOffset(), 0);
			} else if(isInEndTag(c, offset)) {
				return new TextSelection(fDocument, e.getEndStartOffset(), 0);
			}
		}
		if(hasSectionParent(c)) {
			return selection;
		}

		ElementImpl page = isPage(c) ? (ElementImpl)c : (ElementImpl)getPageParent(c);
		if(page != null && getSections(page).isEmpty()) {
			page = null;
		}
		if(page == null) {
			page = getClosestElement(getPagesWithSections(body), offset);
		}
		if(page != null) {
			ElementImpl section = null;
			if(!isInside(page, offset)) {
				section = getContentSection(page);
			}
			if(section == null) {						
				section = getClosestElement(getSections(page), offset);
			}
			if(section != null) {
				int pos = getClosestInnerPosition(section, offset);
				return pos == offset ? selection : new TextSelection(fDocument, pos, 0);
			}
		}
		return selection;
	}

	public String getWarningMessage(Document document, ITextSelection selection) {
		init(document, selection);
		if(c != null && (isSection(c) || hasSectionParent(c))) {
			return null;
		}
		if(c != null && body != null && !getPagesWithSections(body).isEmpty()) {
			return null;
		}
		return WizardMessages.noPlaceForWidgetWarning;
	}

}

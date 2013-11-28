/******************************************************************************* 
 * Copyright (c) 2010-2013 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.jst.web.ui.internal.editor.contentassist;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.common.el.core.ca.MessagesELTextProposal;
import org.jboss.tools.common.el.ui.internal.info.ELInfoHover;

/**
 * Class to provide EL proposals to Content Assistant. The main purpose is to
 * provide correct additional proposal information based on IJavaElement objects
 * collected for the proposal.
 * 
 * @author Victor Rubezhny
 */
@SuppressWarnings("restriction")
public class AutoELContentAssistantProposal extends
		AutoContentAssistantProposal {
	private IJavaElement[] fJavaElements;
	private MessagesELTextProposal fPropertySource;
	private Object fAdditionalProposalInfo;

	/**
	 * Constructs the proposal object
	 * 
	 * @param replacementString
	 * @param replacementOffset
	 * @param replacementLength
	 * @param cursorPosition
	 * @param image
	 * @param displayString
	 * @param contextInformation
	 * @param elements
	 * @param relevance
	 */
	public AutoELContentAssistantProposal(String replacementString,
			int replacementOffset, int replacementLength, int cursorPosition,
			Image image, String displayString, String alternateMatch,
			IContextInformation contextInformation, IJavaElement[] elements,
			int relevance) {
		super(replacementString, replacementOffset, replacementLength,
				cursorPosition, image, displayString, alternateMatch,
				contextInformation, null, relevance);
		this.fJavaElements = elements;
		this.fPropertySource = null;
	}

	/**
	 * Constructs the proposal object
	 * 
	 * @param replacementString
	 * @param replacementOffset
	 * @param replacementLength
	 * @param cursorPosition
	 * @param image
	 * @param displayString
	 * @param contextInformation
	 * @param properySource
	 * @param relevance
	 */
	public AutoELContentAssistantProposal(String replacementString,
			int replacementOffset, int replacementLength, int cursorPosition,
			Image image, String displayString, String alternateMatch,
			IContextInformation contextInformation,
			MessagesELTextProposal propertySource, int relevance) {
		super(replacementString, replacementOffset, replacementLength,
				cursorPosition, image, displayString, alternateMatch,
				contextInformation, null, relevance);
		this.fJavaElements = null;
		this.fPropertySource = propertySource;
	}
	
	private static final String EMPTY_ADDITIONAL_INFO = new String();
	/*
	 * @see org.eclipse.jface.text.contentassist.ICompletionProposalExtension5#getAdditionalProposalInfo(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public Object getAdditionalProposalInfo(IProgressMonitor monitor) {
		if (fAdditionalProposalInfo == null) {
			if (this.fJavaElements != null && this.fJavaElements.length > 0) {
				fAdditionalProposalInfo = ELInfoHover.getHoverInfo(fJavaElements, monitor);
			} else if (fPropertySource != null) {
				fAdditionalProposalInfo = ELInfoHover.getHoverInfo(
						fPropertySource.getBaseName(), fPropertySource.getPropertyName(), 
						fPropertySource.getAllObjects(), monitor);
			}
		}
		if (fAdditionalProposalInfo == null) 
			fAdditionalProposalInfo = EMPTY_ADDITIONAL_INFO;
		return fAdditionalProposalInfo;
	}

	/**
	 * Return cursor position of proposal replacement string.
	 * 
	 * Method is added because of JBIDE-7168
	 */
	public int getCursorPosition() {
		int cursorPosition = -1;

		int openingQuoteInReplacement = getReplacementString().lastIndexOf('(');
		int closingQuoteInReplacement = getReplacementString().lastIndexOf(')');
		int openingQuoteInDisplay = getDisplayString().lastIndexOf('(');
		int closingQuoteInDisplay = getDisplayString().lastIndexOf(')');

		if (openingQuoteInReplacement != -1
				&& closingQuoteInReplacement != -1
				&& openingQuoteInDisplay != -1
				&& closingQuoteInDisplay != -1
				&& (closingQuoteInReplacement - openingQuoteInReplacement) != (closingQuoteInDisplay - openingQuoteInDisplay)) {
			cursorPosition = openingQuoteInReplacement + 1;
		}

		return cursorPosition > -1 ? cursorPosition : super.getCursorPosition();
	}
}
